package be.kuleuven.liris.declareexecutionenvironment.constraint.nary.ordered;

import be.kuleuven.liris.declareexecutionenvironment.activity.Activity;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.RespondedExistence;
import org.processmining.plugins.declare.visualizing.ConstraintDefinition;

public class Response extends RespondedExistence{

	public Response(String constraint, ConstraintDefinition c, Activity ante, Activity conseq) {
		super(constraint, c, ante, conseq);
		reset();
	}

	public void reset(){
		activated = false;
		violated = false;
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
		String regex = "[^A]*(A.*B)*[^A]*";
		//regex = regex.replace("A", "("+antecedent.getName()+")");
		//regex = regex.replace("B", "("+consequent.getName()+")");
		return regex;
	}
}
