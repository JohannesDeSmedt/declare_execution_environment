package be.kuleuven.liris.declareexecutionenvironment.constraint.unary;

import be.kuleuven.liris.declareexecutionenvironment.activity.Activity;
import org.processmining.plugins.declare.visualizing.ConstraintDefinition;

public class Exactly extends Unary{
	
	private final int minimum;
	private final int maximum;
	private int left;
	private int toGo;
	
	public Exactly(String constraint, ConstraintDefinition c, Activity ante, int max, int min) {
		super(constraint, c, ante);
		minimum = min;
		maximum = max;
		violated=true;
	}
	
	public void reset(){
		violated=true;
		left=minimum;
		toGo=maximum;
	}

	public int getMin(){
		return minimum;
	}
	
	public int getMax(){
		return maximum;
	}
	
	public int getLeft(){
		return left;
	}
	
	public int getToGo(){
		return toGo;
	}
	
	public void fire(){
		toGo--;
		left--;
		if(toGo==0 && left==0)
			violated=false;
	}

	@Override
	public String getRegex() {
		String regex = "[^A]*(A[^A]*){"+minimum+"}";
//		String regex = "[^A]*A[^A]*";
//		for(int i=2;i<maximum+1;i++){
//			regex += "A[^A]*";
//		}
		//regex = regex.replace("A", "("+antecedent.getName()+")");
		return regex;
	}
	
}
