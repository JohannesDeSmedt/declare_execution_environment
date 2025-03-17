package be.kuleuven.liris.declareexecutionenvironment.old;

import java.util.Collection;
import java.util.HashSet;

import be.kuleuven.liris.declareexecutionenvironment.DeclareTemplateName;

public class DeclareDependencyStructure {

	DeclareConstraint constraint;
	HashSet<DeclareConstraint> dependentConstraints;
	HashSet<DeclareDependencyStructure> dependentStructures;
	
	public DeclareDependencyStructure(DeclareConstraint mainConstraint){
		constraint = mainConstraint;
		dependentConstraints = new HashSet<DeclareConstraint>();		
		dependentStructures = new HashSet<DeclareDependencyStructure>();
	}
	
	public DeclareConstraint getMainConstraint(){
		return constraint;
	}
	
	public DeclareTemplateName getStructureType(){
		return constraint.getType();
	}
	
	public void addConstraint(DeclareConstraint depCon){
		dependentConstraints.add(depCon);
	}
	
	public void addNestedDependency(DeclareDependencyStructure nestedDDS){
		dependentStructures.add(nestedDDS);
	}
	
	public Collection<DeclareConstraint> getDependentConstraints(){
		return dependentConstraints;
	}
	
	public Collection<DeclareConstraint> getAllDependentConstraints(){
		Collection<DeclareConstraint> cD = new HashSet<DeclareConstraint>();
		for(DeclareConstraint c: dependentConstraints)
			cD.add(c);
		for(DeclareDependencyStructure dDS: dependentStructures){
			cD.add(dDS.getMainConstraint());
			cD.addAll(dDS.getAllDependentConstraints());		
		}
		return cD;
	}
	
	public Collection<DeclareDependencyStructure> getDependentStructures(){
		return dependentStructures;
	}
	
	public String toString(){
		String output="";
		output += constraint.toString();
		for(DeclareConstraint d: dependentConstraints)
			output += "\n-- "+d.toString();
		for(DeclareDependencyStructure dds: dependentStructures)
			output += "\nDown one level\n"+dds.toString();			
		return output;
	}
}
