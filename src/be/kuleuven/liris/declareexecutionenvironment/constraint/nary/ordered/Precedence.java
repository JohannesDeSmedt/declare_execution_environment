package be.kuleuven.liris.declareexecutionenvironment.constraint.nary.ordered;

import be.kuleuven.liris.declareexecutionenvironment.activity.Activity;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.Binary;
import org.processmining.plugins.declare.visualizing.ConstraintDefinition;

public class Precedence extends Binary{

	public Precedence(String constraint, ConstraintDefinition c, Activity ante, Activity conseq) {
		super(constraint, c, ante, conseq);
		reset();
	}

	public void reset(){
		activated = false;
		violated = false;
	}
	
	@Override
	public void fireAntecedent() {
		activated=true;		
	}

	@Override
	public void fireConsequent() {
		//activated=false;		
	}

	@Override
	public String getRegex() {
		String regex = "[^B]*(A(.)*B)*[^B]*";
		//regex = regex.replace("A", "("+antecedent.getName()+")");
		//regex = regex.replace("B", "("+consequent.getName()+")");
		return regex;
	}
	
}
