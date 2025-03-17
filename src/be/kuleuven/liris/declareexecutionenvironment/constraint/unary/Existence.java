package be.kuleuven.liris.declareexecutionenvironment.constraint.unary;

import be.kuleuven.liris.declareexecutionenvironment.activity.Activity;
import org.processmining.plugins.declare.visualizing.ConstraintDefinition;

public class Existence extends Unary {

	private final int minimum;
	private int toGo;
	
	public Existence(String constraint, ConstraintDefinition c, Activity ante, int min) {
		super(constraint, c, ante);
		minimum = min;
		reset();
	}
	
	public void reset(){
		violated=true;
		toGo=minimum;
	}

	public int getMin(){
		return minimum;
	}
	
	public int getToGo(){
		return toGo;		
	}
	
	public void fire(){
		toGo--;
		if(toGo<=0){
			violated=false;
			System.out.println("Violated is "+violated);
		}
	}
	
	@Override
	public String getRegex() {
		String regex = ".*(A.*){"+minimum+"}";
		//regex = regex.replace("A", "("+antecedent.getName()+")");
		return regex;
	}
	
}
