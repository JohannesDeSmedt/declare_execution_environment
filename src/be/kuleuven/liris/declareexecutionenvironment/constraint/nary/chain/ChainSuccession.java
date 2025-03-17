package be.kuleuven.liris.declareexecutionenvironment.constraint.nary.chain;

import be.kuleuven.liris.declareexecutionenvironment.activity.Activity;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.alternate.AlternateSuccession;
import org.processmining.plugins.declare.visualizing.ConstraintDefinition;

public class ChainSuccession extends AlternateSuccession{

	public ChainSuccession(String constraint, ConstraintDefinition c, Activity ante, Activity conseq) {
		super(constraint, c, ante, conseq);
		// TODO Auto-generated constructor stub
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
		String regex = "[^AB]*(AB[^AB]*)*";
		//regex = regex.replace("A", "("+antecedent.getName()+")");
		//regex = regex.replace("B", "("+consequent.getName()+")");
		return regex;
	}
	
	
}
