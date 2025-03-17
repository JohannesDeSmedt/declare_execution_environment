package be.kuleuven.liris.declareexecutionenvironment.old;

import java.util.Collection;
import java.util.Set;

import minerful.concept.ProcessModel;
import minerful.concept.constraint.Constraint;

import org.processmining.plugins.decmod2rinet.DependencyStructure;


public class MFProcessModelAnnotation {
	
	public static void annotate(ProcessModel model) throws Exception {
//		Collection<Character> alphabet = model.getTaskCharArchive().getAlphabet();
//		
//		for(Character a: alphabet){
//			Set<Constraint> cons = model.bag.getConstraintsOf(model.getTaskCharArchive().getTaskChar(a));
//			System.out.println(cons.toString());
//		}
		
	}	
	
	protected static void getLowerLevel(DependencyStructure d){
		System.out.println("For "+d.getP().toString());
		System.out.println(d.getPlaces().toString());
		for(DependencyStructure ds: d.getDP())
			getLowerLevel(ds);		
	}
}
