package be.kuleuven.liris.declareexecutionenvironment.constraint.nary.chain;

import be.kuleuven.liris.declareexecutionenvironment.activity.Activity;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.alternate.AlternateResponse;
import org.processmining.plugins.declare.visualizing.ConstraintDefinition;

public class ChainResponse extends AlternateResponse{

	public ChainResponse(String constraint, ConstraintDefinition c, Activity ante, Activity conseq) {
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
		String regex = "[^A]*(AB[^A]*)*";
		//regex = regex.replace("A", "("+antecedent.getName()+")");
		//regex = regex.replace("B", "("+consequent.getName()+")");
		return regex;
	}
	
	
}
