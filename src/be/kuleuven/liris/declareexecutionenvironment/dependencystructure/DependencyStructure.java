package be.kuleuven.liris.declareexecutionenvironment.dependencystructure;

import java.util.Collection;
import java.util.HashSet;

import be.kuleuven.liris.declareexecutionenvironment.constraint.Constraint;

public abstract class DependencyStructure {

	protected HashSet<Constraint> dependentConstraints;
	protected HashSet<DependencyStructure> dependentStructures;
	protected String caption;
	
	public DependencyStructure(){
		dependentConstraints = new HashSet<Constraint>();		
		dependentStructures = new HashSet<DependencyStructure>();
	}
	
	public void addConstraint(Constraint depCon){
		dependentConstraints.add(depCon);
	}
	
	
	public void addNestedDependency(DependencyStructure nestedDDS){
		dependentStructures.add(nestedDDS);
	}
	
	public Collection<Constraint> getDependentConstraints(){
		return dependentConstraints;
	}
	
	public Collection<DependencyStructure> getDependentStructures(){
		return dependentStructures;
	}
	
	public Collection<Constraint> getAllDependentConstraints(){
		Collection<Constraint> colC = new HashSet<Constraint>();
		for(Constraint c: dependentConstraints)
			colC.add(c);
		for(DependencyStructure cDS: dependentStructures)
			colC.addAll(cDS.getAllDependentConstraints());			
		return colC;		
	}

	
//	public String toString(){
//		String output="";
//		output += constraint.toString();
//		for(Constraint d: dependentConstraints)
//			output += "\n-- "+d.toString();
//		for(ConstraintDependencyStructure dds: dependentStructures)
//			output += "\nDown one level\n"+dds.toString();			
//		return output;
//	}
	
	public abstract String toString();
	
	public String getCaption(){
		return caption;
	}
}
