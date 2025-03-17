package be.kuleuven.liris.declareexecutionenvironment.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.processmining.framework.util.Pair;

import be.kuleuven.liris.declareexecutionenvironment.activity.Activity;
import be.kuleuven.liris.declareexecutionenvironment.constraint.Constraint;

public class StateSpaceState {

	private HashMap<Activity,Pair<Integer,Integer>> activity_table;
	private HashMap<Constraint,Pair<Boolean,Boolean>> constraint_table;
	private List<Activity> firing_sequence;
	private String name;
	private int no;
	private boolean start;
	private Collection<Activity> enabled_activities;
	private int cluster;
	private HashSet<StateSpaceConnection> incomingConnections = new HashSet<StateSpaceConnection>();
	private HashSet<StateSpaceConnection> outgoingConnections = new HashSet<StateSpaceConnection>();
	private HashSet<Constraint> explanations;
	
	public StateSpaceState(int no, String name, HashMap<Activity,Pair<Integer,Integer>> at, List<Activity> fs,
			boolean start, 
			Collection<Activity> enabled_activities, 
			HashMap<Constraint,Pair<Boolean,Boolean>> constraint_table) {
//		System.out.println("Creating " + name);
		this.no = no;
		this.name = name;
		this.activity_table = at;
		this.firing_sequence = fs;
		this.start = start;
		this.enabled_activities = enabled_activities;
		this.cluster = -1;
		this.constraint_table = constraint_table;
	}
	
	public StateSpaceState(int no, String name, HashMap<Activity,Pair<Integer,Integer>> at, List<Activity> fs, 
			boolean start, 
			Collection<Activity> enabled_activities, 
			HashMap<Constraint,Pair<Boolean,Boolean>> constraint_table, 
			HashSet<Constraint> explanations) {
//		System.out.println("Creating " + name);
		this.no = no;
		this.name = name;
		this.activity_table = at;
		this.firing_sequence = fs;
		this.start = start;
		this.enabled_activities = enabled_activities;
		this.cluster = -1;
		this.explanations = explanations;
		this.constraint_table = constraint_table;
	}
	

	
	public String generateDot() {
        StringBuilder dot = new StringBuilder();

        StringBuilder table = new StringBuilder();
        table.append("<<TABLE BORDER=\"1\" CELLBORDER=\"1\" CELLSPACING=\"0\">\n");
        table.append("<TR><TD><B>Act.</B></TD><TD><B>LB</B></TD><TD><B>UB</B></TD></TR>\n");


        
        for (Activity activity : activity_table.keySet()) {
            Pair<Integer, Integer> values = activity_table.get(activity);
            int ub = values.getSecond();
            String ub_string = "";
    		if(ub<Integer.MAX_VALUE/2)
    			ub_string = ub+"";
    		else
    			ub_string = "&infin;";
            
            table.append("<TR><TD>").append(activity.getName()).append("</TD>")
                 .append("<TD>").append(values.getFirst()).append("</TD>")
                 .append("<TD>").append(ub_string).append("</TD></TR>\n");
        }
        table.append("<TR><TD><B>Const.</B></TD><TD><B>Acc</B></TD><TD><B>C</B></TD></TR>\n");
        for(Constraint c: constraint_table.keySet()) {
            table.append("<TR><TD>").append(c.toString()).append("</TD>")
            .append("<TD>").append(constraint_table.get(c).getFirst()).append("</TD>")
            .append("<TD>").append(constraint_table.get(c).getSecond()).append("</TD></TR>\n");
        }
        table.append("</TABLE>>");
//        table.append(this.enabled_activities);
        if (start) {
        	dot.append("initial [shape=plaintext,label=\"\"];\n");
        	dot.append("  initial -> " + this.name +  ";\n");
        }
        dot.append(this.name + "    [shape=box,style=rounded,xlabel=state_" + this.no + " ,label=").append(table).append("];\n");
        return dot.toString();
    }
	
	public void addIncomingConnection(StateSpaceConnection si) {
		this.incomingConnections.add(si);
	}
	
	public void addOutgoingConnection(StateSpaceConnection so) {
		this.outgoingConnections.add(so);
	}
	
	public HashSet<StateSpaceConnection> getIncomingConnections() {
		return this.incomingConnections;
	}
	
	public HashSet<StateSpaceConnection> getOutgoingConnections() {
		return this.outgoingConnections;
	}
	
	public List<Activity> getFiringSequence(){
		return this.firing_sequence;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setCluster(int ci) {
		this.cluster = ci;
	}
	
	public int getCluster() {
		return this.cluster;
	}
	
	public HashMap<Activity,Pair<Integer,Integer>> getActivityTable(){
		return this.activity_table;
	}
	
	public String toString() {
		return this.name + " " + this.enabled_activities.toString();
	}
	
	public boolean transitionInIncomingSet(Activity a) {
		for(StateSpaceConnection s: this.getIncomingConnections()) {
			if(s.getTransition().equals(a))
				return true;
		}
		return false;
		
	}
	
	public boolean isStart() {
		return start;
	}
	
	public StateSpaceConnection getInConnectionForTransition(Activity a) {
		for(StateSpaceConnection s: this.getIncomingConnections()) {
			if(s.getTransition().equals(a))
				return s;
		}
		return null;
		
	}
	
	public void removeIncomingConnection(StateSpaceConnection ic) {
		this.incomingConnections.remove(ic);
	}
	
	public void removeOutgoingConnection(StateSpaceConnection oc) {
		this.outgoingConnections.remove(oc);
	}
	
	public boolean equals(StateSpaceState o) {
//		System.out.println("Comparing "+ o + " and " + this);
//		System.out.println(o.getFiringSequence());
//		System.out.println(this.getFiringSequence());
//		System.out.println(o.getActivityTable());
//		System.out.println(this.getActivityTable());
//		System.out.println(o.getEnabledActivities());
//		System.out.println(this.getEnabledActivities());
		HashSet<StateSpaceState> ss1 = new HashSet<StateSpaceState>();
		for(StateSpaceConnection ss: getOutgoingConnections())
			ss1.add(ss.getS2());
		HashSet<StateSpaceState> ss2 = new HashSet<StateSpaceState>();
		for(StateSpaceConnection ss: getOutgoingConnections())
			ss2.add(ss.getS2());
		
		if(o.getActivityTable().equals(this.getActivityTable()) && this.getConstraintTable().equals(o.getConstraintTable())/*&& !ss1.isEmpty()*/ && o.getEnabledActivities().equals(this.getEnabledActivities())) {
			return true;
		}
		return false;
	}
	
	public boolean sameClusterCheck(StateSpaceState ss) {
//		System.out.println("Comparing " + ss.getActivityTable()+" and "+this.getActivityTable() + " " + ss.getActivityTable().equals(this.getActivityTable()));
		if(ss.getActivityTable().equals(this.getActivityTable())) 
			return true;	
		return false;
	}
	
	public Collection<Activity> getEnabledActivities(){
		return enabled_activities;
	}
	
	public HashMap<Constraint,Pair<Boolean,Boolean>> getConstraintTable(){
		return this.constraint_table;
	}
	
	public boolean equals(Object o) {
//		System.out.println("Comparing "+ o + " and " + this);
		if (o instanceof StateSpaceState) {
			StateSpaceState ss = (StateSpaceState) o;
//			System.out.println(ss.getFiringSequence());
//			System.out.println(this.getFiringSequence());
//			System.out.println(ss.getActivityTable());
//			System.out.println(this.getActivityTable());
//			System.out.println(ss.getEnabledActivities());
//			System.out.println(this.getEnabledActivities());
			
			if(ss.getActivityTable().equals(this.getActivityTable()) && ss.getEnabledActivities().equals(this.getEnabledActivities()))
				return true;
		}
		return false;
	}
	
}
