package be.kuleuven.liris.declareexecutionenvironment.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.processmining.framework.util.Pair;
import org.processmining.models.semantics.IllegalTransitionException;

import be.kuleuven.liris.declareexecutionenvironment.activity.Activity;
import be.kuleuven.liris.declareexecutionenvironment.constraint.Constraint;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.Binary;
import be.kuleuven.liris.declareexecutionenvironment.constraint.unary.Unary;
import be.kuleuven.liris.declareexecutionenvironment.semantics.DeclareMapSemantics;
import be.kuleuven.liris.declareexecutionenvironment.visualizing.DeclareDotGenerator;

public class StateSpaceDecMod {
	
	private DeclareMapSemantics semantics;
	private AnnotatedDeclareModel model;
	private Set<StateSpaceState> states = new HashSet<StateSpaceState>();
	private int state_no = 0;
	private Set<String> connections = new HashSet<String>();
	private HashMap<Integer,HashSet<StateSpaceState>> clusters = new HashMap<Integer,HashSet<StateSpaceState>>();
//	private HashMap<Integer,HashSet<String>> subgraphconnections = new HashMap<Integer,HashSet<String>>();
	private HashSet<StateSpaceConnection> shared = new HashSet<StateSpaceConnection>();
	private HashSet<StateSpaceConnection> non_shared_intercluster_connections = new HashSet<StateSpaceConnection>();
	private HashMap<Integer,String> cluster_color_map = new HashMap<Integer,String>();
	private int initial_cluster = -1;
	
	public StateSpaceDecMod(AnnotatedDeclareModel model_in) throws IllegalTransitionException {
		
		model = model_in;
		semantics = new DeclareMapSemantics(model);
		
		// Create initial state
		Collection<Activity> ena = semantics.getEnabledActivities();
		HashSet<Constraint> explanation_constraints = semantics.getExplanationConstraints();
		System.out.println("EC: " + explanation_constraints);
		
		HashMap<Activity,Pair<Integer,Integer>> activity_table = new HashMap<Activity,Pair<Integer,Integer>>();
		for(Activity aa: model.getActivities(false)) 
			activity_table.put(aa, new Pair<Integer,Integer>(aa.getLowerBound(), aa.getUpperBound()));
		
		HashMap<Constraint,Pair<Boolean,Boolean>> constraint_table = new HashMap<Constraint,Pair<Boolean,Boolean>>();
		for(Constraint c: model.getConstraints()) 
			constraint_table.put(c, new Pair<Boolean,Boolean>(c.isAccepting(), c.isActivated()));
		
		ArrayList<Activity> firing_sequence = new ArrayList<Activity>();
		StateSpaceState s0 = new StateSpaceState(0, "State_0", activity_table, firing_sequence, true, ena, constraint_table, explanation_constraints);
		
		states.add(s0);
		
		System.out.println("Start state space exploration");
		extendStateSpace(s0);
		
		state_no = states.size();
		
		boolean done = true;
		while(!done) {
			done = true;
			for(StateSpaceState s: states) {
				boolean change = false;
				for(StateSpaceState s2: states) {
					if (s.getName() == s2.getName())
						continue;
//					System.out.println("Comparing "+ s +" and " + s2);
					HashSet<Activity> sso1 = new HashSet<Activity>();
					for(StateSpaceConnection ss: s.getOutgoingConnections()) 
						if(ss.getS2()!= s && ss.getS2() != s2)
							sso1.add(ss.getTransition());
					HashSet<Activity> sso2 = new HashSet<Activity>();
					for(StateSpaceConnection ss: s2.getOutgoingConnections())
						if(ss.getS2()!= s && ss.getS2() != s2)
							sso2.add(ss.getTransition());
//					sso1.remove(s2);
//					sso1.remove(s);
//					sso2.remove(s);
//					sso2.remove(s2);
//					System.out.println("Compare: "+ sso1 +" and " + sso2);
					HashSet<Activity> ssi1 = new HashSet<Activity>();
					for(StateSpaceConnection ss: s.getIncomingConnections())
						if(ss.getS1()!= s && ss.getS1()!=s2)
							ssi1.add(ss.getTransition());
					HashSet<Activity> ssi2 = new HashSet<Activity>();
					for(StateSpaceConnection ss: s2.getIncomingConnections())
						if(ss.getS1()!= s && ss.getS1()!=s2)
							ssi2.add(ss.getTransition());
//					ssi1.remove(s2);
//					ssi1.remove(s);
//					ssi2.remove(s);
//					ssi2.remove(s2);
					
//					System.out.println("Compare: "+ ssi1 +" and " + ssi2);
					
					if((sso1.equals(sso2) && ssi1.equals(ssi2) )//|| (ssi1.isEmpty() || ssi1.isEmpty())) 
							&& s.getActivityTable().equals(s2.getActivityTable())) {
//						System.out.println("Merge: "+ s +" and " + s2);
//						System.out.println("Compare: "+ sso1 +" and " + sso2 + " eq " + sso1.equals(sso2));
//						System.out.println("Compare: "+ ssi1 +" and " + ssi2 + " eq " + ssi1.equals(ssi2));
						state_no ++;
						StateSpaceState merge_state = new StateSpaceState(state_no, "State_"+ state_no, s.getActivityTable(), s.getFiringSequence(), s.isStart() || s2.isStart(), s.getEnabledActivities(), s.getConstraintTable());
						
						HashSet<StateSpaceConnection> new_ins = new HashSet<StateSpaceConnection>();
						for(StateSpaceConnection ssc: s.getIncomingConnections()) { 
							ssc.getS1().removeOutgoingConnection(ssc);
							StateSpaceConnection new_s = new StateSpaceConnection(ssc.getS1(), merge_state, ssc.getTransition(), ssc.getExplanationLabel());
							ssc.getS1().addOutgoingConnection(new_s);
							merge_state.addIncomingConnection(new_s);
							new_ins.add(new_s);
						}
						for(StateSpaceConnection ssc: s2.getIncomingConnections()) { 
							ssc.getS1().removeOutgoingConnection(ssc);
							StateSpaceConnection new_s = new StateSpaceConnection(ssc.getS1(), merge_state, ssc.getTransition(), ssc.getExplanationLabel());
							if(!new_ins.contains(new_s)) {
								ssc.getS1().addOutgoingConnection(new_s);
								merge_state.addIncomingConnection(new_s);
							}
						}
						HashSet<StateSpaceConnection> new_outs = new HashSet<StateSpaceConnection>();
						for(StateSpaceConnection ssc: s.getOutgoingConnections()) { 
							ssc.getS2().removeIncomingConnection(ssc);
							StateSpaceConnection new_s = new StateSpaceConnection(merge_state, ssc.getS2(), ssc.getTransition(), ssc.getExplanationLabel());
							ssc.getS2().addIncomingConnection(new_s);
							merge_state.addOutgoingConnection(new_s);
							new_outs.add(new_s);
						}
						for(StateSpaceConnection ssc: s2.getOutgoingConnections()) { 
							ssc.getS2().removeIncomingConnection(ssc);
							StateSpaceConnection new_s = new StateSpaceConnection(merge_state, ssc.getS2(), ssc.getTransition(), ssc.getExplanationLabel());
							if(!new_outs.contains(new_s)) {
								ssc.getS2().addIncomingConnection(new_s);
								merge_state.addOutgoingConnection(new_s);
							}
						}
						states.remove(s);
						states.remove(s2);
						states.add(merge_state);
						done = false;
						change = true;
						break;
					}
				}
				if(change)
					break;
			}
		}
//		done = false;
//		while(!done) {
//			done = true;
//			for(StateSpaceState s: states) {
//				boolean change = false;
//				for(StateSpaceState s2: states) {
//					if (s.getName() == s2.getName())
//						continue;
//					if(s.getEnabledActivities().equals(s2.getEnabledActivities())) {
//						HashSet<StateSpaceState> sso1 = new HashSet<StateSpaceState>();
//						for(StateSpaceConnection ss: s.getOutgoingConnections())
//							sso1.add(ss.getS2());
//						HashSet<StateSpaceState> sso2 = new HashSet<StateSpaceState>();
//						for(StateSpaceConnection ss: s2.getOutgoingConnections())
//							sso2.add(ss.getS2());
//						sso1.remove(s2);
//						sso1.remove(s);
//						sso2.remove(s);
//						sso2.remove(s2);
//	//					System.out.println("Compare: "+ sso1 +" and " + sso2);
//						HashSet<StateSpaceState> ssi1 = new HashSet<StateSpaceState>();
//						for(StateSpaceConnection ss: s.getIncomingConnections())
//							ssi1.add(ss.getS1());
//						HashSet<StateSpaceState> ssi2 = new HashSet<StateSpaceState>();
//						for(StateSpaceConnection ss: s2.getIncomingConnections())
//							ssi2.add(ss.getS1());
//					}
//					
//				}
//			}
//		}
	}
	
	public String toClusteredModelDot() {
		StringBuilder dot = new StringBuilder();
        dot.append("digraph DeclareModel {graph [compound=true] \n");
        dot.append("  rankdir=TB;\n");
        dot.append("  node [shape=box, style=filled, fillcolor=lightgray];\n");
		
        for(StateSpaceState ssn: states)
        	System.out.println(ssn + " " + ssn.getOutgoingConnections());
        	
        
        int seqno = 0;        
        HashMap<Integer, String> cluster_single_node_map = new HashMap<Integer,String>();
		for(Integer cluster: clusters.keySet()) {
			dot.append("subgraph cluster_"+cluster+" {\nlabel= cluster_"+cluster +";\nstyle=filled;\nfillcolor=\""+cluster_color_map.get(cluster) +"\";\n");
			HashMap<String,String> act_map = new HashMap<String,String>();
			
			HashMap<Activity, Pair<Integer, Integer>> act_table = clusters.get(cluster).iterator().next().getActivityTable();
			HashSet<Activity> removed = new HashSet<Activity>();
			
			HashSet<Activity> all_cluster_symbols = new HashSet<Activity>();
			
			for(StateSpaceState sc: clusters.get(cluster)) {
				for(StateSpaceConnection sscsc: sc.getIncomingConnections())
					all_cluster_symbols.add(sscsc.getTransition());
				for(StateSpaceConnection sscsc: sc.getOutgoingConnections())
					all_cluster_symbols.add(sscsc.getTransition());
			}
			
	        for(Activity a: model.getActivities(true)) {
	        	if(act_table.get(a).getSecond()==0 || !all_cluster_symbols.contains(a)) {
	        		removed.add(a);
	        		continue;
	        	}
	        	
	        	act_map.put(a.getName(), "a_"+seqno);
	        	seqno++;
	        	String ub_string = "";
	    		if(act_table.get(a).getSecond()<Integer.MAX_VALUE/2)
	    			ub_string = act_table.get(a).getSecond()+"";
	    		else
	    			ub_string = "&infin;";
	        	dot.append(String.format("  \"%s\" [label=\"%s\"];\n", act_map.get(a.getName()), a.getName()+ " (" + act_table.get(a).getFirst()+","+ub_string+")"));
	        	cluster_single_node_map.put(cluster, act_map.get(a.getName()));
	        }
	        	
	        for(Constraint c: model.getConstraints()) {
	        	if(!(c instanceof Unary)) {
	        		Binary b = (Binary) c;
	        		if(removed.contains(b.getAntecedent()) || removed.contains(b.getConsequent()))
	        			continue;
	        		String style = DeclareDotGenerator.getEdgeStyle(b);
	        		dot.append(String.format("  \"%s\" -> \"%s\" [label= \"" + c.getName() + "\", %s];\n", act_map.get(b.getAntecedent().getName()), act_map.get(b.getConsequent().getName()), style));
	        	}
	        }       	        
	        dot.append("}\n");     
	        
		}
		
		HashSet<String> added_nsic = new HashSet<String>();
		for(StateSpaceConnection ssc:shared) {				
			String label = ssc.getLabelWithNewNames(cluster_single_node_map.get(ssc.getS1().getCluster()), cluster_single_node_map.get(ssc.getS2().getCluster()), true) +"\n";
			added_nsic.add(label);
			dot.append(label);		
		}
		HashSet<String> transition_labels = new HashSet<String>();
		for(StateSpaceConnection ssc:non_shared_intercluster_connections) {	
//			System.out.println("Adding: "+ ssc);
			String label = "";
			label += cluster_single_node_map.get(ssc.getS1().getCluster());
			label += " -> ";
			label += cluster_single_node_map.get(ssc.getS2().getCluster());
			label += "[ltail=cluster_" + ssc.getS1().getCluster() + ", lhead=cluster_"+ ssc.getS2().getCluster() +", label= \"";
			label += ssc.getTransition() + "_" + ssc.getExplanationLabel() +"\"]\n";
			if(!added_nsic.contains(label)) {
//				System.out.println(ssc);
				transition_labels.add(label);
			}
		}
//		System.out.println(transition_labels);
		for(String tl: transition_labels)
			dot.append(tl + "\n");		
    	dot.append("initial [style=invis, shape=plaintext,label=\"\"];\n");
    	dot.append("  initial -> " + cluster_single_node_map.get(initial_cluster) +  "[lhead=cluster_"+ initial_cluster + "];\n");
		
		dot.append("}\n");
		return dot.toString();
	}
	
	public String toDot() {
		StringBuilder dot = new StringBuilder();
        dot.append("digraph G {graph [compound=true] \n");
        for(StateSpaceState s: states) {
        	dot.append(s.generateDot());
        }
//        dot.append("}\n");
//        System.out.println("Clusters:" + clusters);
//		System.out.println(subgraphconnections);
		
		shared = new HashSet<StateSpaceConnection>();
		HashSet<StateSpaceConnection> added = new HashSet<StateSpaceConnection>();
		HashSet<StateSpaceConnection> forget = new HashSet<StateSpaceConnection>();
		
		clusters = new HashMap<Integer,HashSet<StateSpaceState>>();
		for(StateSpaceState s: states) {
			for(Integer c: clusters.keySet()) {
				if(clusters.get(c).iterator().next().sameClusterCheck(s)) {
					clusters.get(c).add(s);
					s.setCluster(c);
					break;
				}
			}
			if(s.getCluster()==-1) {
				HashSet<StateSpaceState> hss = new HashSet<StateSpaceState>();
				hss.add(s);
				s.setCluster(clusters.size());
				clusters.put(clusters.size(), hss);
			}
		}
		
		
		for(Integer cluster: clusters.keySet()) {
			int no_connections = 0;
//			System.out.println("Cluster: "+cluster);
//			System.out.println("Cluster: "+clusters.get(cluster));
			cluster_color_map.put(cluster, generateRandomColor());
			dot.append("subgraph cluster_"+cluster+" {\nlabel= cluster_"+cluster +";\nstyle=filled;\nfillcolor=\""+cluster_color_map.get(cluster) +"\";\n");
			
			
			HashSet<StateSpaceConnection> sscs = new HashSet<StateSpaceConnection>();
			for(StateSpaceState s: clusters.get(cluster)) { 
				sscs.addAll(s.getOutgoingConnections());
				if(s.isStart())
					initial_cluster = cluster;
			}
			
			HashSet<Activity> link_symbols = new HashSet<Activity>();
			for(StateSpaceConnection s: sscs)
				link_symbols.add(s.getTransition());
			
			for(Activity t: link_symbols) {
//				System.out.println("Transition:" + t);
				HashSet<StateSpaceConnection> ssc_transition = new HashSet<StateSpaceConnection>();
				boolean out = false;
				for(StateSpaceConnection ssc: sscs) {
					if(ssc.getTransition()== t) {
						ssc_transition.add(ssc);
						if(ssc.getS2().getCluster()!= cluster)
							out=true;
					}
				}
				if(ssc_transition.size() == clusters.get(cluster).size() && out) {
//					System.out.println("Connections:" + ssc_transition);
					int c1 = -1;
					int no_f = 0;
					HashSet<StateSpaceState> receiving_states = new HashSet<StateSpaceState>();
					for(StateSpaceConnection ps: ssc_transition) {
						int c2 = ps.getS2().getCluster();
						if ((c1 != c2) && (c1!=-1)) {
							break;
						}else {
							c1 = c2;
							no_f++;
							receiving_states.add(ps.getS2());
						}
					}
//					System.out.println("Connections:" + no_f + " " + clusters.get(c1).size());
					if(/*c1 != -1 &&*/ clusters.get(c1).size() == receiving_states.size()) {
						StateSpaceConnection use = ssc_transition.iterator().next();
//						shared.add(use);
//						for(StateSpaceConnection ps: ssc_transition) {
//							if(ps!= use)
//								forget.add(ps);
//						}
					}
//					System.out.println("Shared:" + shared);
				}
					
			}
			for(StateSpaceState s1: clusters.get(cluster)) {
				boolean connected = false;
				for(StateSpaceConnection sc: s1.getIncomingConnections())
					if(!shared.contains(sc) && !added.contains(sc) && sc.getS1().getCluster()==cluster ){
						added.add(sc);
						dot.append(sc.getLabel(false)+"\n");	
						no_connections++;
						connected = true;
					}
				for(StateSpaceConnection sc: s1.getOutgoingConnections())
					if(!shared.contains(sc) && !added.contains(sc) && sc.getS2().getCluster()==cluster ){
						added.add(sc);
						dot.append(sc.getLabel(false)+"\n");	
						no_connections++;
						connected = true;
					}
				if(!connected)
					dot.append(s1.getName() + " -> " + s1.getName() +"[style=invis]\n");
			}
				
			dot.append("}\n");
		}
		for(StateSpaceState s: states) {
			for(StateSpaceConnection si: s.getIncomingConnections()) {
				if(!shared.contains(si) && !si.getS1().equals(si.getS2()) && !(si.getS1().getCluster()==si.getS2().getCluster()))
					non_shared_intercluster_connections.add(si);
				if(!added.contains(si) && !shared.contains(si) && !forget.contains(si)) {
					dot.append(si.getLabel(false) +"\n");	
					added.add(si);
				}
			}
			for(StateSpaceConnection so: s.getIncomingConnections()) {
				if(!shared.contains(so) && !so.getS1().equals(so.getS2()) && !(so.getS1().getCluster()==so.getS2().getCluster()))
					non_shared_intercluster_connections.add(so);
				if(!added.contains(so) && !shared.contains(so) && !forget.contains(so)) {
					dot.append(so.getLabel(false) +"\n");	
					added.add(so);
				}
			}
		}
//		System.out.println("Non-shared intercluster connections:" + non_shared_intercluster_connections);
	
		for(StateSpaceConnection ssc:shared)
			dot.append(ssc.getLabel(true) +"\n");		

        for(String s: connections) {
        	dot.append(s + "\n");
        }
        dot.append("}\n");
        
        return dot.toString();
	}
	
    private static String generateRandomColor() {
        Random rand = new Random();
        int r = rand.nextInt(256);
        int g = rand.nextInt(256);
        int b = rand.nextInt(256);
        int a = 128; // 50% opacity (0-255 scale, where 255 is fully opaque)
        return String.format("#%02x%02x%02x%02x", r, g, b, a);
    }
	
	public void extendStateSpace(StateSpaceState s_in) throws IllegalTransitionException {
		
//		System.out.println("Enabled activities: " + semantics.getEnabledActivities());
//		System.out.println("Activity table: " + s_in.getActivityTable());
		for(Activity a: semantics.getEnabledActivities()) {
//			System.out.println("FIRE " + a + " in state " + s_in);
			semantics.reset();
//			System.out.println("----------------BEGIN SEQUENCE-------------------------");
			for(Activity a_e: s_in.getFiringSequence())
				semantics.fire(a_e);
			Collection<Activity> enabled_activities = semantics.fire(a);
			HashSet<Constraint> explanation_constraints = semantics.getExplanationConstraints();
			if(semantics.deadlocked()) {
				enabled_activities.remove(a);
				continue;
			}
			HashMap<Constraint,Pair<Boolean,Boolean>> constraint_table = new HashMap<Constraint,Pair<Boolean,Boolean>>();
			for(Constraint c: model.getConstraints()) 
				constraint_table.put(c, new Pair<Boolean,Boolean>(c.isAccepting(),c.isActivated()));
			
			
//			System.out.println("----------------END SEQUENCE----------------------------");
			
			
			List<Activity> new_firing_sequence = new ArrayList<Activity>(s_in.getFiringSequence());
			new_firing_sequence.add(a);
			
			HashMap<Activity,Pair<Integer,Integer>> activity_table = new HashMap<Activity,Pair<Integer,Integer>>();
			for(Activity aa: model.getActivities(false)) 
				activity_table.put(aa, new Pair<Integer,Integer>(aa.getLowerBound(), aa.getUpperBound()));
			
			
			StateSpaceState s = new StateSpaceState(states.size(), "State_"+ states.size(), activity_table, new_firing_sequence, false, enabled_activities, constraint_table);
			
			StateSpaceState found = null;
			for(StateSpaceState ss: states) {
				if (ss.equals(s)) {
					found = ss;
					break;

//					boolean income_check = true;
//					for(StateSpaceConnection sic: ss.getIncomingConnections()) {
//						if(!sic.getS1().getEnabledActivities().equals(s_in.getEnabledActivities()))
//							income_check = false;
//					}
//					
//					if(//ss.equals(s_in) || 
//						//	!ss.transitionInIncomingSet(a) || 
//							(ss.transitionInIncomingSet(a) && ss.getInConnectionForTransition(a).getS1().equals(ss)) || 
//							(ss.transitionInIncomingSet(a) 
////									&& income_check)
//									&& ss.getInConnectionForTransition(a).getS1().getEnabledActivities().equals(s_in.getEnabledActivities()))
//							) {
//						System.out.println(ss);
//						System.out.println(s_in);
//						System.out.println("Found");
//						found = ss;
//	
//						break;
//					}
				}
			}
			if(found == null) {
//				System.out.println("State added to state space");
				states.add(s);
				
				if (s.sameClusterCheck(s_in)) {
					if(s_in.getCluster()==-1) {
						HashSet<StateSpaceState> states = new HashSet<StateSpaceState>();
						states.add(s_in);
						states.add(s);
//						subgraphconnections.put(clusters.size()-1, new HashSet<String>());
					}else {
						s.setCluster(s_in.getCluster());
					}
				}
				StateSpaceConnection ssc = new StateSpaceConnection(s_in, s, a, "");
				ssc.setExplanationLabel(explanation_constraints.toString());
				s_in.addOutgoingConnection(ssc);
				s.addIncomingConnection(ssc);
				extendStateSpace(s);
			}else {
//				System.out.println("State contained " + found.getName()+ " vs. " + s_in.getName());
				StateSpaceConnection ssc = new StateSpaceConnection(s_in, found, a, "");
				ssc.setExplanationLabel(explanation_constraints.toString());
				s_in.addOutgoingConnection(ssc);
				found.addIncomingConnection(ssc);
				
				if (found.sameClusterCheck(s_in) && (s_in != found)) {
					if(s_in.getCluster()==-1 & found.getCluster()==-1) {
						HashSet<StateSpaceState> states = new HashSet<StateSpaceState>();
						states.add(s_in);
						states.add(found);
//						found.setCluster(clusters.size());
//						s_in.setCluster(clusters.size());
//						clusters.put(clusters.size(), states);
//						subgraphconnections.put(clusters.size()-1, new HashSet<String>());
					}else if(found.getCluster()==-1){
						found.setCluster(s_in.getCluster());
//						clusters.get(s_in.getCluster()).add(found);
					}else {
						s_in.setCluster(found.getCluster());
//						clusters.get(found.getCluster()).add(s_in);
					}
				}
			}
		}
		
	}
	
	public int getNoStates() {
		return this.states.size();
	}
	
	public Set<StateSpaceState> getStates(){
		return this.states;
	}
	
    public static String escapeDotString(String input) {
        if (input == null) return "";

        return input.replace("\\", "\\\\")  // Escape backslashes
                    .replace("\"", "\\\"")  // Escape double quotes
                    .replace("\n", "\\n")  // Escape newlines
        			.replace("[", "\\[")  // Escape brackets
        			.replace("]", "\\]"); // Escape brackets
    }

}
