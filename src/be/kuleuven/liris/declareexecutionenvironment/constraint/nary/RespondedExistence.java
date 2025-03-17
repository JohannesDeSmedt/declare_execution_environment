package be.kuleuven.liris.declareexecutionenvironment.constraint.nary;

import be.kuleuven.liris.declareexecutionenvironment.activity.Activity;
import org.processmining.plugins.declare.visualizing.ConstraintDefinition;

public class RespondedExistence extends Binary{

	private boolean conseqFired;
	
	public RespondedExistence(String constraint, ConstraintDefinition c, Activity ante, Activity conseq) {
		super(constraint, c, ante, conseq);
		reset();
	}

	public void reset(){
		violated=false;
		conseqFired = false;
	}
	
	public void fireAntecedent(){
		if(!conseqFired)
			violated = true;
	}
	
	public void fireConsequent(){
		violated = false;
		conseqFired = true;
	}

	@Override
	public String getRegex() {
		String regex = "[^A]*((A.*B.*)|(B.*A.*))?";
		//regex = regex.replace("A", "("+antecedent.getName()+")");
		//regex = regex.replace("B", "("+consequent.getName()+")");
		return regex;
	}
	
}
