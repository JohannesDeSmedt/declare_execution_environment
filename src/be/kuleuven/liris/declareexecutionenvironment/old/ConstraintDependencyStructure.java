package be.kuleuven.liris.declareexecutionenvironment.old;

import java.util.Collection;
import java.util.HashSet;

import minerful.concept.constraint.Constraint;
import minerful.concept.constraint.ConstraintFamily;

public class ConstraintDependencyStructure {

	Constraint constraint;
	HashSet<Constraint> dependentConstraints;
	HashSet<ConstraintDependencyStructure> dependentStructures;
	
	public ConstraintDependencyStructure(Constraint mainConstraint){
		constraint = mainConstraint;
		dependentConstraints = new HashSet<Constraint>();		
		dependentStructures = new HashSet<ConstraintDependencyStructure>();
	}
	
	public Constraint getMainConstraint(){
		return constraint;
	}
	
	public ConstraintFamily getStructureType(){
		return constraint.getFamily();
	}
	
	public void addConstraint(Constraint depCon){
		dependentConstraints.add(depCon);
	}
	
	public void addNestedDependency(ConstraintDependencyStructure nestedDDS){
		dependentStructures.add(nestedDDS);
	}
	
	public Collection<Constraint> getDependentConstraints(){
		return dependentConstraints;
	}
	
	public Collection<ConstraintDependencyStructure> getDependentStructures(){
		return dependentStructures;
	}
	
	public Collection<Constraint> getAllDependentConstraints(){
		Collection<Constraint> colC = new HashSet<Constraint>();
		for(Constraint c: dependentConstraints)
			colC.add(c);
		for(ConstraintDependencyStructure cDS: dependentStructures){
			colC.add(cDS.getMainConstraint());
			colC.addAll(cDS.getAllDependentConstraints());			
		}
		return colC;		
	}
	
	public String toString(){
		String output="";
		output += constraint.toString();
		for(Constraint d: dependentConstraints)
			output += "\n-- "+d.toString();
		for(ConstraintDependencyStructure dds: dependentStructures)
			output += "\nDown one level\n"+dds.toString();			
		return output;
	}
}
