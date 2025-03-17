package be.kuleuven.liris.declareexecutionenvironment.constraint.nary.chain;

import be.kuleuven.liris.declareexecutionenvironment.activity.Activity;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.alternate.AlternatePrecedence;
import org.processmining.plugins.declare.visualizing.ConstraintDefinition;

public class ChainPrecedence extends AlternatePrecedence{
	
	public ChainPrecedence(String constraint, ConstraintDefinition c, Activity ante, Activity conseq) {
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
		String regex = "[^B]*(AB[^B]*)*";
		//regex = regex.replace("A", "("+antecedent.getName()+")");
		//regex = regex.replace("B", "("+consequent.getName()+")");
		return regex;
	}
	
	
}
