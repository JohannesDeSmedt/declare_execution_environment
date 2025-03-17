package org.processmining.plugins.decmod2rinet;

/** Author: Johannes De Smedt
 *  E-mail: johannes.desmedt@kuleuven.be
 *  October 2015
 */

import java.util.HashMap;
import java.util.HashSet;

import org.processmining.framework.util.Pair;
import org.processmining.plugins.declareminer.visualizing.ActivityDefinition;

public class AnnotatedDeclareMapGraph {
	static DeclareMapGraph dmg;
	static HashSet<Integer> visited;
	static HashMap<Integer, Pair<HashSet<String>,HashSet<String>>> dep;
	
	public DeclareMapGraph getDmg(){
		return dmg;
	}
	
	public Pair<HashSet<String>, HashSet<String>> getDepAct(String a){
		for(ActivityDefinition aD: dmg.adMap.keySet()){
			if(aD.toString().equals(a.toString()))
				return dep.get(dmg.actToInt(aD));
		}
		return null;
	}
	
	public AnnotatedDeclareMapGraph(DeclareMapGraph dmgIn){
		dmg = dmgIn;
		visited = new HashSet<Integer>();
		dep = new HashMap<Integer, Pair<HashSet<String>,HashSet<String>>>();
				
		for(ActivityDefinition aD: dmg.adMap.keySet()){
			//System.out.println("\n--- "+aD.toString());
			HashSet<Integer> inDep = new HashSet<Integer>();
			HashSet<Integer> outDep = new HashSet<Integer>();		
			visited.clear();	
			
			inDep = getIncomingDep(inDep, dmg.actToInt(aD));
			visited.clear();
			outDep = getOutgoingDep(outDep, dmg.actToInt(aD));
						
			HashSet<String> dI = new HashSet<String>();
			for(Integer i: inDep)
				dI.add(dmgIn.actToInt(i).toString());
			HashSet<String> dO = new HashSet<String>();
			for(Integer i: outDep)
				dO.add(dmgIn.actToInt(i).toString());
			
			dep.put(dmg.actToInt(aD), new Pair<HashSet<String>,HashSet<String>>(dI, dO));
		}
		
	}
	
	private HashSet<Integer> getIncomingDep(HashSet<Integer> inDep, Integer act){
			
		if(dmg.incomingBin(act)!=null && !visited.contains(act)){
			visited.add(act);
			
			for(String conS: dmg.incomingBin(act).keySet()){
				if((conS.contains("response")||conS.contains("succession")) && !(conS.contains("not"))){
					for(Integer i: dmg.incomingBin(act).get(conS)){
						//System.out.println("L - Checking "+dmg.actToInt(i).toString());
						inDep.add(i);
						inDep = getIncomingDep(inDep, i);
						inDep = getOutgoingDep(inDep, i);
					}
				}
			}	
		}			
		return inDep;		
	}
	
	
	private HashSet<Integer> getOutgoingDep(HashSet<Integer> inDep, Integer act){
		
		if(dmg.outgoingBin(act)!=null && !visited.contains(act)){
			visited.add(act);
			
			for(String conS: dmg.outgoingBin(act).keySet()){
				//System.out.println("R - Checking "+conS);
				if((conS.contains("precedence")||conS.contains("succession")) && !(conS.contains("not"))){
					for(Integer i: dmg.outgoingBin(act).get(conS)){
						inDep.add(i);
						inDep = getIncomingDep(inDep, i);
						inDep = getOutgoingDep(inDep, i);
					}
				}
			}		
		}
		
		return inDep;		
	}
	
}
