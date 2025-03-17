package be.kuleuven.liris.declareexecutionenvironment.constraint.nary.ordered;

import be.kuleuven.liris.declareexecutionenvironment.activity.Activity;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.CoExistence;
import org.processmining.plugins.declare.visualizing.ConstraintDefinition;

public class Succession extends CoExistence{

	public Succession(String constraint, ConstraintDefinition c, Activity ante, Activity conseq) {
		super(constraint, c, ante, conseq);
	}

	public void reset(){
		activated = false;
		violated = false;
	}
	
	
	public void fireAntecedent(){
		activated = true;
		violated = true;
	}
	
	public void fireConsequent(){
//		activated = false;
		violated = false;
	}	
	
	@Override
	public String getRegex() {
		String regex ="[^AB]*(A.*B)*[^AB]*";
		//regex = regex.replace("A", "("+antecedent.getName()+")");
		//regex = regex.replace("B", "("+consequent.getName()+")");
		return regex;
	}
	
}
