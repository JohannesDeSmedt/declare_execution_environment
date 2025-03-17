package be.kuleuven.liris.declareexecutionenvironment.constraint.nary.alternate;

import be.kuleuven.liris.declareexecutionenvironment.activity.Activity;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.ordered.Precedence;
import org.processmining.plugins.declare.visualizing.ConstraintDefinition;

public class AlternatePrecedence extends Precedence{

	public AlternatePrecedence(String constraint, ConstraintDefinition c, Activity ante, Activity conseq) {
		super(constraint, c, ante, conseq);
	}
	

	@Override
	public void fireAntecedent() {
		activated=true;		
	}

	@Override
	public void fireConsequent() {
		activated=false;		
	}
	
	@Override
	public String getRegex() {
		String regex = "[^B]*(A[^B]*B[^B]*)*";
		//regex = regex.replace("A", "("+antecedent.getName()+")");
		//regex = regex.replace("B", "("+consequent.getName()+")");
		return regex;
	}
	
	
}
