package be.kuleuven.liris.declareexecutionenvironment.constraint.nary;

import be.kuleuven.liris.declareexecutionenvironment.activity.Activity;
import org.processmining.plugins.declare.visualizing.ConstraintDefinition;

public class Choice extends Binary{

	public Choice(String constraint, ConstraintDefinition c, Activity ante, Activity conseq) {
		super(constraint, c, ante, conseq);
		reset();
	}
	
	public void reset(){
		violated=true;
	}
	
	public void fireAntecedent(){
		violated = false;
	}
	
	public void fireConsequent(){
		violated = false;
	}

	@Override
	public String getRegex() {
		String regex = ".*[AB].*";
		//regex = regex.replace("A", "("+antecedent.getName()+")");
		//regex = regex.replace("B", "("+consequent.getName()+")");
		return regex;
	}


}
