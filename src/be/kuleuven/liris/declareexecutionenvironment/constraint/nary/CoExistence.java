package be.kuleuven.liris.declareexecutionenvironment.constraint.nary;

import be.kuleuven.liris.declareexecutionenvironment.activity.Activity;
import org.processmining.plugins.declare.visualizing.ConstraintDefinition;

public class CoExistence extends Binary{
	
	private boolean antecFired;
	private boolean conseqFired;

	public CoExistence(String constraint, ConstraintDefinition c, Activity ante, Activity conseq) {
		super(constraint, c, ante, conseq);
		reset();
	}
	
	public void reset(){
		violated=false;
		antecFired=false;
		conseqFired=false;
	}

	public void fireAntecedent(){
		if(conseqFired)
			violated = false;
		antecFired=true;
	}
	
	public void fireConsequent(){
		if(antecFired)
			violated = false;
		conseqFired=true;
	}

	@Override
	public String getRegex() {
		String regex = "[^AB]*((A.*B.*)|(B.*A.*))?";
		//regex = regex.replace("A", "("+antecedent.getName()+")");
		//regex = regex.replace("B", "("+consequent.getName()+")");
		return regex;
	}
	
}
