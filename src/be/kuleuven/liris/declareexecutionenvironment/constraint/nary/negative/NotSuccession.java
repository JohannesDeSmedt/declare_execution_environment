package be.kuleuven.liris.declareexecutionenvironment.constraint.nary.negative;

import be.kuleuven.liris.declareexecutionenvironment.activity.Activity;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.Binary;
import org.processmining.plugins.declare.visualizing.ConstraintDefinition;

public class NotSuccession extends Binary{

	public NotSuccession(String constraint, ConstraintDefinition c, Activity ante, Activity conseq) {
		super(constraint, c, ante, conseq);
	}
	
	public void fireAntecedent(){
		consequent.setUpperBound(0, this);
	}

	@Override
	public void reset() {

	}

	@Override
	public void fireConsequent() {

	}

	@Override
	public String getRegex() {
		String regex = "[^A]*(A[^B]*)*";
		//regex = regex.replace("A", "("+antecedent.getName()+")");
		//regex = regex.replace("B", "("+consequent.getName()+")");
		return regex;
	}
	
}
