package be.kuleuven.liris.declareexecutionenvironment.constraint.unary;

import be.kuleuven.liris.declareexecutionenvironment.activity.Activity;
import be.kuleuven.liris.declareexecutionenvironment.constraint.Constraint;
import org.processmining.plugins.declare.visualizing.ConstraintDefinition;

public abstract class Unary extends Constraint {

	protected final Activity antecedent;
	
	public abstract void fire();
	
	public Unary(String constraint, ConstraintDefinition c, Activity ante) {
		super(constraint, c);
		antecedent = ante;
		this.activated=true;
	}
	
	public Activity getAntecedent(){
		return antecedent;
	}
	
	public String toString(){
		return name + "("+antecedent.getName()+")";
	}

	@Override
	public void reset() {
		
		
	}
}
