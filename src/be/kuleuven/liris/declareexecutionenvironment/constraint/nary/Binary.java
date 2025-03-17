package be.kuleuven.liris.declareexecutionenvironment.constraint.nary;

import be.kuleuven.liris.declareexecutionenvironment.activity.Activity;
import be.kuleuven.liris.declareexecutionenvironment.constraint.Constraint;
import org.processmining.plugins.declare.visualizing.ConstraintDefinition;

public abstract class Binary extends Constraint{

	protected final Activity antecedent;
	protected final Activity consequent;
//	protected boolean activated;
	
	public Binary(String constraint, ConstraintDefinition c, Activity ante, Activity conseq) {
		super(constraint, c);
		antecedent = ante;
		consequent = conseq;
		reset();
	}
	
	public abstract void reset();
	
	public abstract void fireAntecedent();
	
	public abstract void fireConsequent();
	
	
	public Activity getAntecedent(){
		return antecedent;
	}

	public Activity getConsequent(){
		return consequent;
	}
	
	public String toString(){
		return name+"("+antecedent.getName()+","+consequent.getName()+")";
	}
	
}
