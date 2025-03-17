package be.kuleuven.liris.declareexecutionenvironment.old;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import minerful.concept.ProcessModel;
import minerful.concept.TaskChar;
import minerful.concept.constraint.Constraint;
import minerful.concept.constraint.relation.RelationConstraint;

import org.processmining.plugins.declareminer.visualizing.ActivityDefinition;
import org.processmining.plugins.declareminer.visualizing.ConstraintDefinition;
import org.processmining.plugins.declareminer.visualizing.DeclareMap;
import org.processmining.plugins.declareminer.visualizing.Parameter;

import be.kuleuven.liris.declareexecutionenvironment.AnnotatedDeclareMapGraph;
import be.kuleuven.liris.declareexecutionenvironment.DeclareTemplateConversion;
import be.kuleuven.liris.declareexecutionenvironment.activity.Activity;

public class DeclareModelToAnnotatedDeclareGraph {

	static Collection<Character> alphabet = new HashSet<Character>();
	static HashSet<DeclareConstraint> constraints = new HashSet<DeclareConstraint>(); 
	
	private static HashSet<Activity> activitySet;
	
	
	//For alphabet conversion
	static HashMap<String,Character> actToChar = new HashMap<String,Character>();
	static HashMap<Character,String> charToAct = new HashMap<Character,String>();
	static HashMap<DeclareConstraint,Constraint> dCtC;
	
	public static AnnotatedDeclareMapGraph mapToADMG(DeclareMap map){
		int i=97;
		
		activitySet = new HashSet<Activity>();
		
		for (ActivityDefinition a : map.getModel().getActivityDefinitions()) {
			Character c = (char) i;
			charToAct.put(c, a.getName());
			actToChar.put(a.getName(), c);
			alphabet.add(c);
			i++;
		}

		//System.out.println("Alphabet mapping " + charToAct.toString());
		System.out.println("Activity set: "+activitySet.toString());

		DeclareTemplateConversion convertor = new DeclareTemplateConversion();
		for (ConstraintDefinition c : map.getModel().getConstraintDefinitions()) {
			//System.out.println("Constraint: " + c.getName());
			Integer f = 0;
			Character a = null, b = (char) 6;
			for (Parameter p : c.getParameters()) {
				//System.out.println(c.getBranches(p).iterator().next()
				//		.toString());
				if (f == 0)
					a = actToChar.get(c.getBranches(p).iterator().next()
							.toString());
				if (f == 1)
					b = actToChar.get(c.getBranches(p).iterator().next()
							.toString());
				f++;
			}

			DeclareConstraint constraint = new DeclareConstraint(
					convertor.translate(c.getName()), a, b);
			constraints.add(constraint);
		}
		
		//Make 'Johannes' Declare graph and annotate it
		return new AnnotatedDeclareMapGraph(alphabet, constraints, actToChar, charToAct);				
	}	
	
	public static Object[] modelToADMG(ProcessModel minerFulModel){
		int i=97;
		
		//For (MINERful) Declare models
		HashMap<TaskChar,Character> tcToChar = new HashMap<TaskChar,Character>();
		HashMap<Character,TaskChar> charToTc = new HashMap<Character,TaskChar>();
		dCtC = new HashMap<DeclareConstraint,Constraint>();
		
		for(TaskChar tc: minerFulModel.getProcessAlphabet()){
			Character c = (char) i;
			charToTc.put(c, tc);
			tcToChar.put(tc, c);
			actToChar.put(tc.toString(), c);
			charToAct.put(c, tc.toString());
			alphabet.add(c);
			i++;
		}
		
		for(TaskChar ch: minerFulModel.getProcessAlphabet()){
			for(Constraint c: minerFulModel.bag.getConstraintsOf(ch)){
				Character a=null,b= (char) 6;
				a= tcToChar.get(c.getParameters().get(0).getFirstTaskChar());
				if(c instanceof RelationConstraint)
					b = tcToChar.get(c.getParameters().get(1).getFirstTaskChar());
				DeclareConstraint newDC = MFConstraintToDeclareConstraintConvertor.convert(c.getName(),a,b);
				
				boolean add = true;
				for(DeclareConstraint checkD: constraints)
					if(checkD.equals(newDC))
						add=false;
				if(add){
					constraints.add(newDC);
					dCtC.put(newDC, c);
					//System.out.println("Adding: "+c.getName()+"  "+charToAct.get(a)+" - "+charToAct.get(b));
				}
			}
		}
		
		AnnotatedDeclareMapGraph a = new AnnotatedDeclareMapGraph(alphabet, constraints, actToChar, charToAct);
		//System.out.println("Conversion: "+dCtC.toString());
		
		Collection<ConstraintDependencyStructure> cDD = new HashSet<ConstraintDependencyStructure>();
		for(DeclareDependencyStructure d: a.getDDSs()){
			ConstraintDependencyStructure cD = new ConstraintDependencyStructure(dCtC.get(d.getMainConstraint()));
			for(DeclareConstraint dCi: d.getDependentConstraints())
				cD.addConstraint(dCtC.get(dCi));
			cD = getCDLevels(cD,d.getDependentStructures());
			cDD.add(cD);
		}
		
		//Make 'Johannes' Declare graph and annotate it
		return new Object[]{a,cDD};			
	}
	
	private static ConstraintDependencyStructure getCDLevels(ConstraintDependencyStructure cD, Collection<DeclareDependencyStructure> dCol){
		for(DeclareDependencyStructure dN: dCol){
			ConstraintDependencyStructure cDnested = new ConstraintDependencyStructure(dCtC.get(dN.getMainConstraint()));
			for(DeclareConstraint dCi: dN.getDependentConstraints())
				cD.addConstraint(dCtC.get(dCi));
			cDnested = getCDLevels(cDnested,dN.getDependentStructures());
			cD.addNestedDependency(cDnested);
		}
		return cD;
	}
	
}
