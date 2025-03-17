package be.kuleuven.liris.declareexecutionenvironment.constraint.unary;

import org.processmining.plugins.declare.visualizing.ConstraintDefinition;

import be.kuleuven.liris.declareexecutionenvironment.activity.Activity;


public class Absence extends Unary {

	private final int maximum;
	private int left;
	
	public Absence(String constraint, ConstraintDefinition c, Activity ante, int max) {
		super(constraint, c, ante);
		maximum = max-1;
		reset();
	}
	
	public void reset(){
		violated=false;
		left=maximum;
	}
	
	public int getMax(){
		return maximum;
	}
	
	public int getLeft(){
		return left;
	}
	
	public void fire(){
		left--;
		if(left<0)
			violated=true;
	}

	
	@Override
	public String getRegex() {
		String regex = "[^A]*(A?[^A]*){"+maximum+"}";
		//regex = regex.replace("A", "("+antecedent.getName()+")");
		return regex;
	}

	
}
