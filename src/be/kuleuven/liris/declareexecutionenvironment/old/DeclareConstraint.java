package be.kuleuven.liris.declareexecutionenvironment.old;

import be.kuleuven.liris.declareexecutionenvironment.DeclareTemplateName;

public class DeclareConstraint {

	private boolean existence;
	private boolean absence;
	private boolean unary;
	private boolean binary;
	private String description;
	private DeclareTemplateName type;
	private Character antecedent;
	private Character consequent;
	private int cardinality;
	
	public DeclareConstraint(DeclareTemplateName type, Character antecedent, Character consequent){
		this.antecedent=antecedent;		
		
		if(consequent.equals((char) 6)){
			unary=true;
			binary=false;
		}else{
			binary=true;
			unary=false;
			this.consequent=consequent;		
		}
		//ConstraintDescriptions descriptions = new ConstraintDescriptions();		
		//description = descriptions.getDescription(type);		
		this.type = type;
		
		absence=false;
		existence=false;
		
		switch (type) {
		case Exactly1:
			cardinality = 1;
			absence=true;
			existence=true;
			break;
		case Exactly2:
			cardinality = 2;
			absence=true;
			existence=true;
			break;
		case Exactly3:
			cardinality = 3;
			absence=true;
			existence=true;
			break;
		case Existence:
			cardinality = 1;
			existence=true;
			break;
		case Existence2:
			cardinality = 2;
			existence=true;
			break;
		case Existence3:
			cardinality = 3;
			existence=true;
			break;
//		case Absence:
//			cardinality = 1;
//			absence=true;
//			break;
		case Absence2:
			cardinality = 2;
			absence=true;
			break;
		case Absence3:
			cardinality = 3;
			absence=true;
			break;
		default:
			cardinality = 0;
			break;
		}
	}
	
	public boolean equals(DeclareConstraint d){
		boolean result = false;
		if(d.getType().equals(type))
			if(d.getAntecedent().equals(antecedent))
				if(d.getConsequent().equals(consequent))
					result=true;		
		return result;
	}
	
	public String toString(){
		if(isBinary())
			return type.toString() + "(" + antecedent + "," + consequent +")";
		else if(isUnary())
			return type.toString() + "(" + antecedent +")";
		else
			return "empty";
	}
	
	public Character getAntecedent(){
		return antecedent;
	}
	
	public boolean isAbsence(){
		return absence;
	}
	
	public boolean isExistence(){
		return existence;
	}
	
	public Character getConsequent(){
		return consequent;
	}
	
	public String getDescription(){
		return description;
	}
	
	public boolean isUnary(){
		return unary;
	}
	
	public boolean isBinary(){
		return binary;
	}
	
	public DeclareTemplateName getType(){
		return type;
	}
	
	public boolean isType(DeclareTemplateName name){
		if(type.equals(name))
			return true;
		else
			return false;
	}	
	
	public int getCardinality(){
		return cardinality;
	}
}
