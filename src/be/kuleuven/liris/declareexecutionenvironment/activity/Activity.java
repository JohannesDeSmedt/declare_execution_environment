package be.kuleuven.liris.declareexecutionenvironment.activity;

import java.util.HashSet;

import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.plugins.declare.visualizing.ActivityDefinition;

import be.kuleuven.liris.declareexecutionenvironment.constraint.Constraint;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.Binary;
import be.kuleuven.liris.declareexecutionenvironment.constraint.unary.Unary;
import be.kuleuven.liris.declareexecutionenvironment.dependencystructure.ActivityDependencyStructure;

public class Activity {

	private final String name;
	private final char c;
	private int upperBound = Integer.MAX_VALUE;
	private int lowerBound = 0;
	private int finalUpperbound;
	private int finalLowerbound;
	private boolean enabled = false;
	private int timesFired = 0;
	
	private ActivityDefinition actDef;
	private Transition transition;
	
	private HashSet<Binary> incomingConstraints;
	private HashSet<Binary> outgoingConstraints;
	private HashSet<Unary> unaries;
	private HashSet<String> explanations = new HashSet<String>();
	private HashSet<Constraint> explanation_constraints = new HashSet<Constraint>();
	private ActivityDependencyStructure dependencyStructure;
	
	public Activity(String name, ActivityDefinition a, Transition t, char c){
		this.name = name;
		this.c = c;
		
		actDef = a;
		transition = t;
		
		incomingConstraints = new HashSet<Binary>();
		outgoingConstraints = new HashSet<Binary>();
		unaries = new HashSet<Unary>();
	}
	
	public void addDependencyStructure(ActivityDependencyStructure aD){
		this.dependencyStructure = aD;
	}
	
	public char getCharacter(){
		return c;
	}
		
	public ActivityDependencyStructure getDependencyStructure(){
		return dependencyStructure;
	}
	
	//Assuming names are unique
	public boolean equals(Activity a){
		if(name.equals(a.name))
			return true;
		else
			return false;
	}
	
	public HashSet<String> getExplanations(){
		return explanations;
	}
	
	public boolean isEnabled(){
		return enabled;
	}
	
	public void reset(){
		resetExplanations();
		lowerBound = finalLowerbound;
		upperBound = finalUpperbound;
		timesFired = 0;
	}
	
	public void resetExplanations(){
		explanations = new HashSet<String>();
	}
	
	public void fire(){
		setUpperBound(upperBound-1, null);
		setLowerBound(lowerBound-1, null);
		explanation_constraints = new HashSet<Constraint>();
		for(Binary c: incomingConstraints)
			c.fireConsequent();
		for(Binary c: outgoingConstraints)
			c.fireAntecedent();
		for(Unary c: unaries)
			c.fire();
		timesFired++;
	}
	
	public HashSet<Constraint> getExplanationConstraints(){
		return explanation_constraints;
	}
	
	public void lockBounds(){
		finalLowerbound = lowerBound;
		finalUpperbound = upperBound;
	}
		
	public void addIncomingConstraint(Binary c){
		incomingConstraints.add(c);
	}
	
	public HashSet<Binary> getIncomingConstraints(){
		return incomingConstraints;
	}
	
	public void addOutgoingConstraint(Binary c){
		outgoingConstraints.add(c);
	}
	
	public HashSet<Binary> getOutgoingConstraints(){
		return outgoingConstraints;
	}
	
	public ActivityDefinition getActDef(){
		return actDef;
	}

	public void setTransition(Transition t) {
		this.transition = t;
	}
	
	public Transition getTransition(){
		return transition;
	}
	
	public void addUnary(Unary u){
		unaries.add(u);
	}
	
	public HashSet<Unary> getUnaries(){
		return unaries;
	}
	
	public void setUpperBound(int uB, Constraint c){
		System.out.println("Changing UB of "+name+" through "+c+" to "+uB+" vs current "+upperBound);
		if (uB != upperBound  && uB < (Integer.MAX_VALUE / 2)) {
			if (uB >= 0 && uB < (Integer.MAX_VALUE / 2))
				upperBound = Math.max(0, uB);
			if (c != null && uB < (Integer.MAX_VALUE / 2)){
				if (upperBound < (Integer.MAX_VALUE / 2)) {
					explanations.add(getName() + "(" + getLowerBound() + ","
							+ getUpperBound() + "), upper bound changed by "
							+ c + " to " + upperBoundToString(upperBound));
					explanation_constraints.add(c);
				}else {
					explanations.add("<html>" + getName() + "("
							+ getLowerBound()
							+ ",&infin), upper bound changed by "
							+ c + " to " + upperBoundToString(upperBound) + "</html>");
					explanation_constraints.add(c);
				}
			}
		}
	}
	
	public String upperBoundToString(int upperBound){
		String ub = "";
		if(upperBound<Integer.MAX_VALUE/2)
			ub = upperBound+"";
		else
			ub = "<html>&infin</html>";
		return ub;
	}	

	public void setLowerBound(int lB, Constraint c){
		System.out.println("Changing LB of "+name+" through "+c+" to "+lB+" vs current "+lowerBound);
		if (lB != lowerBound) {
			if (c != null){
				if (upperBound < (Integer.MAX_VALUE / 2)) {
					explanations.add(getName() + "(" + getLowerBound() + ","
							+ getUpperBound() + "), lower bound changed by "
							+ c + " to " + lB);
					explanation_constraints.add(c);
				}else {
					explanations.add("<html>" + getName() + "("
							+ getLowerBound()
							+ ",&infin), lower bound changed by "
							+ c + " to " + lB + "</html>");
					explanation_constraints.add(c);
				}
			}
			lowerBound = Math.max(lB, 0);
		}
	}
	
	public void enable(){
		enabled = true;
	}
	
	public void disable(String c){
		if(c!=null && enabled==true)
			explanations.add(getName()+" disabled by "+c);
		enabled = false;
	}

	public int getUpperBound(){
		return upperBound;
	}
	
	public int getLowerBound(){
		return lowerBound;
	}
	
	public String getName(){
		return name;
	}
	
	public int getTimesFired(){
		return timesFired;
	}
	
	public String toString(){
		return getName();
	}
}
