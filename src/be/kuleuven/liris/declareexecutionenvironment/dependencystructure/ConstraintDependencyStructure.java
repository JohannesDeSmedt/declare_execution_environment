package be.kuleuven.liris.declareexecutionenvironment.dependencystructure;

import java.util.Collection;
import java.util.HashSet;

import be.kuleuven.liris.declareexecutionenvironment.activity.Activity;
import be.kuleuven.liris.declareexecutionenvironment.constraint.Constraint;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.Binary;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.Choice;
import be.kuleuven.liris.declareexecutionenvironment.constraint.unary.Unary;

public class ConstraintDependencyStructure extends DependencyStructure{

	private Constraint constraint;
		
	public ConstraintDependencyStructure(Constraint mainConstraint){
		super();
		constraint = mainConstraint;
		caption = "Dependency structure for "+mainConstraint;
	}
	
	public Constraint getMainConstraint(){
		return constraint;
	}
			
	public Collection<Activity> getActivities(){
		HashSet<Activity> activities = new HashSet<Activity>();
		for(Constraint c: dependentConstraints){
			if(c instanceof Unary){
				activities.add(((Unary)c).getAntecedent());
			}else{
				if(c instanceof Choice){
					boolean found = false;
					for(Constraint cI: getAllDependentConstraints()){
						if( (((Binary) c).getAntecedent().getOutgoingConstraints().contains(cI)
								|| ((Binary) c).getAntecedent().getIncomingConstraints().contains(cI)) 
								&& (!c.equals(cI) 
										|| ((Binary) c).getAntecedent().getIncomingConstraints().contains(getMainConstraint()))
								){
							activities.add(((Binary) c).getAntecedent());
							found=true;
							break;
						}
					}
					if(!found)
						activities.add(((Binary) c).getConsequent());
				}else{
					activities.add(((Binary)c).getAntecedent());
					activities.add(((Binary)c).getConsequent());
				}
			}
		}
		return activities;
	}
		
	public int getDependencyScore(){
		return calculateDependencyScore(1);
	}
		
	@Override
	public Collection<Constraint> getAllDependentConstraints(){
		Collection<Constraint> colC = new HashSet<Constraint>();
		for(Constraint c: dependentConstraints)
			colC.add(c);
		for(DependencyStructure cDS: dependentStructures){
			colC.add(getMainConstraint());
			colC.addAll(cDS.getAllDependentConstraints());			
		}
		return colC;		
	}
	
	private int calculateDependencyScore(int level){
		int score = 0;
		for(DependencyStructure ds: getDependentStructures())
			score += ((ConstraintDependencyStructure) ds).calculateDependencyScore(level+1);
		score += getDependentConstraints().size() + 1;
		return level * score;
	}
		
	public String toString(){
		String output="";
		output += constraint.toString();
		for(Constraint d: dependentConstraints)
			output += "\n-- "+d.toString();
		for(DependencyStructure dds: dependentStructures)
			output += "\nDown one level\n"+dds.toString();			
		return output;
	}
	
}
