package be.kuleuven.liris.declareexecutionenvironment.constraint;

import org.processmining.plugins.declare.visualizing.ConstraintDefinition;

public abstract class Constraint {

	protected final String name;
	protected boolean violated=false;
	protected ConstraintDefinition conDef;
	protected boolean activated=false;
	
	public Constraint(String constraint, ConstraintDefinition c){
		name = constraint;
		conDef = c;
	}	
	
	public String getName(){
		return name;
	}
	
	public ConstraintDefinition getCD(){
		return conDef;
	}
	
	public abstract void reset();
	
	public boolean isAccepting(){
		return !violated;
	}
	
	public boolean isActivated(){
		return activated;
	}
		
	public abstract String getRegex();
	
}
