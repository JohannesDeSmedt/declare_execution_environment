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
import be.kuleuven.liris.declareutilities.DeclareRegexUtilities;
import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;

public class StateSpaceDecModFSA {
	
	private DeclareMapSemantics semantics;
	private Collection<Activity> activities;
	private AnnotatedDeclareModel model;
	private Automaton aut;
	private Set<StateSpaceState> states = new HashSet<StateSpaceState>();
	private int state_no = 0;
	private Set<String> connections = new HashSet<String>();
	private HashMap<Integer,HashSet<StateSpaceState>> clusters = new HashMap<Integer,HashSet<StateSpaceState>>();
//	private HashMap<Integer,HashSet<String>> subgraphconnections = new HashMap<Integer,HashSet<String>>();
	private HashSet<StateSpaceConnection> shared = new HashSet<StateSpaceConnection>();
	private HashSet<StateSpaceConnection> non_shared_intercluster_connections = new HashSet<StateSpaceConnection>();
	private HashMap<Integer,String> cluster_color_map = new HashMap<Integer,String>();
	private HashMap<State, StateSpaceState> sss_to_autstate = new HashMap<State, StateSpaceState>();
	private int initial_cluster = -1;
	
	public StateSpaceDecModFSA(AnnotatedDeclareModel model_in) throws IllegalTransitionException {
		this.activities = model_in.getActivities(true);
		RegExp r = new RegExp(DeclareRegexUtilities.alphabetToString(activities)+"*");

		aut = r.toAutomaton();
		for(Constraint cc: model_in.getConstraints()) {
			if(cc instanceof Binary) {
				Binary bin = (Binary) cc;
				Automaton constraint_automaton = DeclareRegexUtilities.regexToAutomaton(activities, bin.getRegex(), bin.getAntecedent(), bin.getConsequent());
				aut = aut.intersection(DeclareRegexUtilities.regexToAutomaton(activities, bin.getRegex(), bin.getAntecedent(), bin.getConsequent()));
			}
			else {
				Unary un = (Unary) cc;
				aut = aut.intersection(DeclareRegexUtilities.regexToAutomaton(activities, un.getRegex(), un.getAntecedent(), un.getAntecedent()));
			}
		}
		aut.reduce();
		aut.minimize();
		
		model = model_in;
		semantics = new DeclareMapSemantics(model);
		
		State initial_state = aut.getInitialState();	
		Collection<Activity> ena_aut = transitionsToActivities(initial_state.getTransitions());
		
		// Create initial state
		Collection<Activity> ena = semantics.getEnabledActivities();
		assert ena_aut == ena;
		
		HashSet<Constraint> explanation_constraints = semantics.getExplanationConstraints();
		for(Constraint c: model.getConstraints()) {
			c.isAccepting();
		}
		System.out.println("Initial state explanations: " + explanation_constraints);
		
		HashMap<Activity,Pair<Integer,Integer>> activity_table = new HashMap<Activity,Pair<Integer,Integer>>();
		for(Activity aa: model.getActivities(false)) 
			activity_table.put(aa, new Pair<Integer,Integer>(aa.getLowerBound(), aa.getUpperBound()));
		
		HashMap<Constraint,Pair<Boolean,Boolean>> constraint_table = new HashMap<Constraint,Pair<Boolean,Boolean>>();
		for(Constraint c: model.getConstraints()) 
			constraint_table.put(c, new Pair<Boolean,Boolean>(c.isAccepting(), c.isActivated()));
		
		ArrayList<Activity> firing_sequence = new ArrayList<Activity>();
		StateSpaceState s0 = new StateSpaceState(0, "State_0", activity_table, firing_sequence, true, ena, constraint_table, explanation_constraints);
		sss_to_autstate.put(initial_state, s0);
		states.add(s0);
		
		System.out.println("Start state space exploration");
		extendStateSpace(initial_state);
		
		state_no = states.size();	
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
						shared.add(use);
						for(StateSpaceConnection ps: ssc_transition) {
							if(ps!= use)
								forget.add(ps);
						}
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
	
	private Collection<Activity> transitionsToActivities(Set<Transition> transitions){
		Collection<Activity> act = new HashSet<Activity>();
		for(Transition t: transitions) {
			for(char c = t.getMin(); c<= t.getMax(); c++) {
				Activity outcome = transitionCharToActivity(c);
				if(outcome != null)
					act.add(outcome);
			}
		}
		return act;
	}
	
	private Activity transitionCharToActivity(char t){
		for(Activity a: activities) 
			if(a.getCharacter() == t)
				return a;	
		return null;
	}
	
    private static String generateRandomColor() {
        Random rand = new Random();
        int r = rand.nextInt(256);
        int g = rand.nextInt(256);
        int b = rand.nextInt(256);
        int a = 128; // 50% opacity (0-255 scale, where 255 is fully opaque)
        return String.format("#%02x%02x%02x%02x", r, g, b, a);
    }
	
	public void extendStateSpace(State s_in) throws IllegalTransitionException {
		
		System.out.println("Enabled activities: " + semantics.getEnabledActivities());
//		System.out.println("Activity table: " + s_in.getActivityTable());
//		for(Activity a: semantics.getEnabledActivities()) {
		
		StateSpaceState sss = sss_to_autstate.get(s_in);
		for(Transition t: s_in.getTransitions()) {

			for(char c = t.getMin(); c<= t.getMax(); c++) {
				
				Activity outcome = transitionCharToActivity(c);
				
				
				if(outcome != null) {
					Activity a = outcome;
					System.out.println("FIRE " + a + " in state " + sss);
					semantics.reset();
					
					System.out.println("----------------BEGIN SEQUENCE-------------------------");
					for(Activity a_e: sss.getFiringSequence())
						semantics.fire(a_e);
					Collection<Activity>  enabled_activities = semantics.fire(a);
					HashSet<Constraint> explanation_constraints = semantics.getExplanationConstraints();
					if(semantics.deadlocked()) {
						enabled_activities.remove(a);
						continue;
					}
					Collection<Activity> enabled_aut = transitionsToActivities(t.getDest().getTransitions());
					assert enabled_aut == enabled_activities;
					System.out.println("----------------END SEQUENCE----------------------------");
					
					
					List<Activity>  new_firing_sequence = new ArrayList<Activity>(sss.getFiringSequence());
					new_firing_sequence.add(a);
					
					HashMap<Activity,Pair<Integer,Integer>> activity_table = new HashMap<Activity,Pair<Integer,Integer>>();
					for(Activity aa: model.getActivities(false)) 
						activity_table.put(aa, new Pair<Integer,Integer>(aa.getLowerBound(), aa.getUpperBound()));
					
					
					HashMap<Constraint,Pair<Boolean,Boolean>> constraint_table = new HashMap<Constraint,Pair<Boolean,Boolean>>();
					for(Constraint cv: model.getConstraints()) 
						constraint_table.put(cv, new Pair<Boolean,Boolean>(cv.isAccepting(), cv.isActivated()));
					StateSpaceState s = new StateSpaceState(states.size(), "State_"+ states.size(), activity_table, new_firing_sequence, false, enabled_activities, constraint_table);

					
					if(!sss_to_autstate.containsKey(t.getDest())) {
						states.add(s);
						System.out.println("Adding new state");
						sss_to_autstate.put(t.getDest(), s);
						StateSpaceConnection ssc = new StateSpaceConnection(sss, s, a, "");
						ssc.setExplanationLabel(explanation_constraints.toString());
						sss.addOutgoingConnection(ssc);
						s.addIncomingConnection(ssc);
						extendStateSpace(t.getDest());
					}else {
						StateSpaceConnection ssc = new StateSpaceConnection(sss, sss_to_autstate.get(t.getDest()), a, "");
						ssc.setExplanationLabel(explanation_constraints.toString());
						sss.addOutgoingConnection(ssc);
						sss_to_autstate.get(t.getDest()).addIncomingConnection(ssc);
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
