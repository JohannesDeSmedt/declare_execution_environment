package be.kuleuven.liris.declareexecutionenvironment.constraint.unary;

import be.kuleuven.liris.declareexecutionenvironment.activity.Activity;
import org.processmining.plugins.declare.visualizing.ConstraintDefinition;

public class Last extends Unary{

	public Last(String constraint, ConstraintDefinition c, Activity ante) {
		super(constraint, c, ante);
	}

	@Override
	public void fire() {
		
	}


	@Override
	public String getRegex() {
		String regex = ".*A";
		//regex = regex.replace("A", "("+antecedent.getName()+")");
		return regex;
	}
	
}
