package be.kuleuven.liris.declareexecutionenvironment.constraint.nary;

import be.kuleuven.liris.declareexecutionenvironment.activity.Activity;
import org.processmining.plugins.declare.visualizing.ConstraintDefinition;

public class ExclusiveChoice extends Binary{

	public ExclusiveChoice(String constraint, ConstraintDefinition c, Activity ante, Activity conseq) {
		super(constraint, c, ante, conseq);
		reset();
	}

	public void reset(){
		violated=true;
	}
	
	public void fireAntecedent(){
		this.activated=true;
		violated = false;
		consequent.setUpperBound(0,this);
	}
	
	public void fireConsequent(){
		this.activated=true;
		violated = false;
		antecedent.setUpperBound(0,this);
	}

	@Override
	public String getRegex() {
		String regex = "([^B]*A[^B]*)|([^A]*B[^A]*)";
		//regex = regex.replace("A", "("+antecedent.getName()+")");
		//regex = regex.replace("B", "("+consequent.getName()+")");
		return regex;
	}

}
