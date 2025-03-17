package be.kuleuven.liris.declareexecutionenvironment;

/** Author: Johannes De Smedt
 *  E-mail: johannes.desmedt@kuleuven.be
 *  October 2015
 */

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import org.processmining.framework.util.Pair;

import be.kuleuven.liris.declareexecutionenvironment.old.DeclareConstraint;
import be.kuleuven.liris.declareexecutionenvironment.old.DeclareDependencyStructure;
import be.kuleuven.liris.declareexecutionenvironment.old.DeclareMapGraph;

public class AnnotatedDeclareMapGraph extends DeclareMapGraph{
	static HashSet<Character> visitedL;
	static HashSet<Character> visitedR;
	static HashSet<DeclareConstraint> visitedC;
	static HashMap<Character, Pair<HashSet<Character>,HashSet<Character>>> dep;
	static DeclareDependencyStructure dds;
	static HashSet<DeclareDependencyStructure> ds;
	
	public Pair<HashSet<Character>, HashSet<Character>> getDepAct(Character a){
		if(dep.get(a)!=null)
			return dep.get(a);
		else
			return null;
	}
		
	public AnnotatedDeclareMapGraph(Collection<Character> alphabet, HashSet<DeclareConstraint> constraintSet, HashMap<String,Character> actToChar, HashMap<Character,String> charToAct){//DeclareMapGraph dmgIn){
		super(alphabet, constraintSet, actToChar, charToAct);

		System.out.println("Chars: "+charToAct.toString());
		visitedL = new HashSet<Character>();
		visitedR = new HashSet<Character>();
		visitedC = new HashSet<DeclareConstraint>();
		dep = new HashMap<Character, Pair<HashSet<Character>,HashSet<Character>>>();
		ds = new HashSet<DeclareDependencyStructure>();
		
		for(Character a: alphabet){
			HashSet<Character> inDep = new HashSet<Character>();
			HashSet<Character> outDep = new HashSet<Character>();					
			dep.put(a, new Pair<HashSet<Character>,HashSet<Character>>(inDep, outDep));
		}
		
		
		// Add Declare dependency structures
		for(DeclareConstraint c: getConstraints()){
			//System.out.println("Checking out "+c.toString());
			visitedL.clear();
			visitedR.clear();
			visitedC.clear();
			if(c.isType(DeclareTemplateName.Not_Succession)){	
				//System.out.println("\n\nNot succession "+c.toString()+" ante "+charToAct.get(c.getAntecedent())+" cons "+charToAct.get(c.getConsequent()));
				dds = new DeclareDependencyStructure(c);
				dds = checkBackward(c.getConsequent(), dds); 
				dds = checkForward(c.getConsequent(), dds);
				//System.out.println("DDS: "+dds.toString());
				if(!dds.getDependentConstraints().isEmpty() || !dds.getDependentStructures().isEmpty())
					ds.add(dds);
			}else if(c.isType(DeclareTemplateName.Exclusive_Choice) || c.isType(DeclareTemplateName.Not_CoExistence)){
				dds = new DeclareDependencyStructure(c);
				dds = checkBackward(c.getConsequent(), dds); 
				dds = checkForward(c.getConsequent(), dds);
				if(!dds.getDependentConstraints().isEmpty() || !dds.getDependentStructures().isEmpty())
					ds.add(dds);
				
				visitedL.clear();
				visitedR.clear();
				visitedC.clear();
				
				dds = new DeclareDependencyStructure(c);
				dds = checkBackward(c.getAntecedent(), dds);
				dds = checkForward(c.getAntecedent(), dds);
				if(!dds.getDependentConstraints().isEmpty() || !dds.getDependentStructures().isEmpty())
					ds.add(dds);
			}else if(c.isAbsence()){
				//System.out.println("\n\nAbsence "+c.toString());
				dds = new DeclareDependencyStructure(c);
				dds = checkBackward(c.getAntecedent(), dds);
				if(!dds.getDependentConstraints().isEmpty() || !dds.getDependentStructures().isEmpty())
					ds.add(dds);
			}
		}
		for(DeclareDependencyStructure d: ds)
			System.out.println("\nNew D: "+d.toString());
	}
	
	private DeclareDependencyStructure checkBackward(Character c, DeclareDependencyStructure deDeSt){
		if(incomingBin(c)!=null && !visitedL.contains(c)){
			//System.out.println("Checking backwards for "+charToAct.get(c));
			visitedL.add(c);
			for(DeclareConstraint conS: incomingBin(c).keySet()){
				boolean check = conS.isType(DeclareTemplateName.Response) || conS.isType(DeclareTemplateName.Succession) ||
						conS.isType(DeclareTemplateName.Alternate_Response) || conS.isType(DeclareTemplateName.Alternate_Succession) ||
						conS.isType(DeclareTemplateName.Chain_Response) || conS.isType(DeclareTemplateName.Chain_Succession) ||
						conS.isType(DeclareTemplateName.Responded_Existence) || conS.isType(DeclareTemplateName.CoExistence);
				if(check && !visitedC.contains(conS)){
					//System.out.println("Adding "+conS.toString());
					visitedC.add(conS);
					deDeSt.addConstraint(conS);
					deDeSt = checkBackward(conS.getAntecedent(), deDeSt);
					deDeSt = checkForward(conS.getAntecedent(), deDeSt);
				}
				if(conS.isType(DeclareTemplateName.Choice))
					deDeSt.addConstraint(conS);
			}
		}
		if(incomingUn(c)!=null){
			for(DeclareConstraint conS: incomingUn(c).keySet()){
				if(conS.isExistence()){
					deDeSt.addConstraint(conS);
					//System.out.println("Added unary "+c.toString());
				}
			}
		}
		return deDeSt;
	}
	
	private DeclareDependencyStructure checkForward(Character c, DeclareDependencyStructure deDeSt){
		if(outgoingBin(c)!=null && !visitedR.contains(c)){
			//System.out.println("Checking forward for "+charToAct.get(c));
			visitedR.add(c);
			for(DeclareConstraint conS: outgoingBin(c).keySet()){
				boolean check = conS.isType(DeclareTemplateName.Precedence) || conS.isType(DeclareTemplateName.Succession) ||
						conS.isType(DeclareTemplateName.Alternate_Precedence) || conS.isType(DeclareTemplateName.Alternate_Succession) ||
						conS.isType(DeclareTemplateName.Chain_Precedence) || conS.isType(DeclareTemplateName.Chain_Succession) ||
						conS.isType(DeclareTemplateName.CoExistence);
				if(check && !visitedC.contains(conS)){
					//System.out.println("Adding "+conS.toString());
					visitedC.add(conS);
					DeclareDependencyStructure nestedDDS = new DeclareDependencyStructure(conS);
					nestedDDS = checkBackward(conS.getConsequent(), nestedDDS);
					nestedDDS = checkForward(conS.getConsequent(), nestedDDS);
					deDeSt.addNestedDependency(nestedDDS);
				}
				if(conS.isType(DeclareTemplateName.Choice))
					deDeSt.addConstraint(conS);
			}			
		}
		return deDeSt;
	}

	public Collection<DeclareDependencyStructure> getDDSs(){
		return ds;
	}
	
}
