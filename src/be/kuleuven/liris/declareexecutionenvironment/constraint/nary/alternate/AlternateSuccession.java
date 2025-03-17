package be.kuleuven.liris.declareexecutionenvironment.constraint.nary.alternate;

import be.kuleuven.liris.declareexecutionenvironment.activity.Activity;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.ordered.Succession;
import org.processmining.plugins.declare.visualizing.ConstraintDefinition;

public class AlternateSuccession extends Succession{

	public AlternateSuccession(String constraint, ConstraintDefinition c, Activity ante, Activity conseq) {
		super(constraint, c, ante, conseq);
	}

	
	public void fireAntecedent(){
		violated = true;
		activated = true;
	}
	
	public void fireConsequent(){
		violated = false;
		activated = false;
	}
	
	@Override
	public String getRegex() {
		String regex = "[^AB]*(A[^AB]*B[^AB]*)*";
		//regex = regex.replace("A", "("+antecedent.getName()+")");
		//regex = regex.replace("B", "("+consequent.getName()+")");
		return regex;
	}
	
}

