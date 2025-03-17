package be.kuleuven.liris.declareexecutionenvironment.model;

import be.kuleuven.liris.declareexecutionenvironment.activity.Activity;

public class StateSpaceConnection {
	
	private StateSpaceState s1;
	private StateSpaceState s2;
	private Activity transition;
	private String label;
	private String explanation_label = "";
	
	public StateSpaceConnection(StateSpaceState s1, StateSpaceState s2, Activity transition, String label) {
		this.s1 = s1;
		this.s2 = s2;
		this.transition = transition;
		this.label = label;
	}
	
	public StateSpaceState getS1() {
		return s1;
	}
	
	public StateSpaceState getS2() {
		return s2;
	}
	
	public void setS1(StateSpaceState sss) {
		s1 = sss;
	}
	
	public void setS2(StateSpaceState sss) {
		s2 = sss;
	}
	
	public Activity getTransition() {
		return transition;
	}
	
	public boolean equals(StateSpaceConnection so) {
		if (so.getS1() == s1 && so.getS2() == s2 && so.getTransition() == transition) 
			return true;
		return false;
	}
	
	public String toString() {
		return s1.getName() + "->" + s2.getName() + ":" + transition.getName();
	}
	
	public void setExplanationLabel(String el) {
		explanation_label = el;
	}

	public String getExplanationLabel() {
		return explanation_label;
	}
	
	public String getLabelWithNewNames(String n1, String n2, boolean merged) {
		if(!merged) 
			return n1 +" -> " + n2 + "[label= \"" + transition.getName() + "_" + explanation_label + "\"]";
		else
			return n1 +" -> " + n2 + "[ltail=cluster_" + s1.getCluster() + ", lhead=cluster_"+ s2.getCluster() +", label= \"" + transition.getName() + "_" +  explanation_label + "\"]";
	}
	
	public String getLabel(boolean merged) {
		if(!merged) 
			return s1.getName() +" -> " + s2.getName() + "[label= \"" + transition.getName() + "_" + explanation_label + "\"]";
		else
			return s1.getName() +" -> " + s2.getName() + "[ltail=cluster_" + s1.getCluster() + ", lhead=cluster_"+ s2.getCluster() +", label= \"" + transition.getName() + "_" +  explanation_label + "\"]";
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
