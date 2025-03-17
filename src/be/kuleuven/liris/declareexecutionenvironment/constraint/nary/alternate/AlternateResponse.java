package be.kuleuven.liris.declareexecutionenvironment.constraint.nary.alternate;

import be.kuleuven.liris.declareexecutionenvironment.activity.Activity;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.ordered.Response;
import org.processmining.plugins.declare.visualizing.ConstraintDefinition;

public class AlternateResponse extends Response{

	public AlternateResponse(String constraint, ConstraintDefinition c, Activity ante, Activity conseq) {
		super(constraint, c, ante, conseq);
	}

	
	public void fireAntecedent(){
		violated = true;
		activated = true;
		//consequent.setLowerBound(Math.max(antecedent.getLowerBound()+((isActivated()) ? 1 : 0),consequent.getLowerBound()));
	}
	
	public void fireConsequent(){
		violated = false;
		activated = false;
		//consequent.setLowerBound(Math.max(consequent.getLowerBound(), 0));
	}
	
	@Override
	public String getRegex() {
		String regex = "[^A]*(A[^A]*B[^A]*)*";
		//regex = regex.replace("A", "("+antecedent.getName()+")");
		//regex = regex.replace("B", "("+consequent.getName()+")");
		return regex;
	}
	
	
}
