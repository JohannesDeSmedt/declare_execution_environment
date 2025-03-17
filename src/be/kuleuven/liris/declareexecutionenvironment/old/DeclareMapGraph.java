package be.kuleuven.liris.declareexecutionenvironment.old;

/** Author: Johannes De Smedt
 *  E-mail: johannes.desmedt@kuleuven.be
 *  October 2015
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import be.kuleuven.liris.declareexecutionenvironment.DeclareTemplateName;


public class DeclareMapGraph {
	static HashMap<Character, HashMap<Integer, HashMap<DeclareConstraint,ArrayList<Character>>>> DJMap = new HashMap<Character, HashMap<Integer, HashMap<DeclareConstraint,ArrayList<Character>>>>();
	private Collection<Character> alphabet;
	private HashSet<DeclareConstraint> constraintSet;
	private HashMap<String,Character> actToChar = new HashMap<String,Character>();
	protected HashMap<Character,String> charToAct = new HashMap<Character,String>();
	
	public DeclareMapGraph(Collection<Character> alphabet, HashSet<DeclareConstraint> constraintSet, HashMap<String,Character> actToChar, HashMap<Character,String> charToAct){		
		this.alphabet=alphabet;
		this.constraintSet=constraintSet;
		this.actToChar=actToChar;
		this.charToAct=charToAct;
				
		for(DeclareConstraint d: constraintSet){
			if(d.isUnary() && !d.isType(DeclareTemplateName.Last) && !d.isType(DeclareTemplateName.Init)){
				addNewUnaryConstraint(d.getAntecedent(), d);
			}else if(d.isBinary())
				addNewBinaryConstraint(d.getAntecedent(),d.getConsequent(),d);
		}		
	}
	
	public static void addNewUnaryConstraint(Character a, DeclareConstraint d){
		if(DJMap.containsKey(a)){
			if(DJMap.get(a).containsKey(1)){
				if(DJMap.get(a).get(1).containsKey(d)){
					DJMap.get(a).get(1).get(d).add(a);
				}else{
					ArrayList<Character> aL = new ArrayList<Character>();
					aL.add(a);
					HashMap<DeclareConstraint, ArrayList<Character>> hM = new HashMap<DeclareConstraint, ArrayList<Character>>();
					hM.put(d, aL);
					DJMap.get(a).get(1).putAll(hM);
				}						
			}else{
				ArrayList<Character> aL = new ArrayList<Character>();
				aL.add(a);
				HashMap<DeclareConstraint, ArrayList<Character>> hM = new HashMap<DeclareConstraint, ArrayList<Character>>();
				hM.put(d, aL);
				HashMap<Integer, HashMap<DeclareConstraint,ArrayList<Character>>> hM2 = new  HashMap<Integer, HashMap<DeclareConstraint,ArrayList<Character>>>();
				hM2.put(1,hM);
				DJMap.get(a).putAll(hM2);
			}
					
		}else{
			ArrayList<Character> aL = new ArrayList<Character>();
			aL.add(a);
			HashMap<DeclareConstraint, ArrayList<Character>> hM = new HashMap<DeclareConstraint, ArrayList<Character>>();
			hM.put(d, aL);
			HashMap<Integer, HashMap<DeclareConstraint,ArrayList<Character>>> hM2 = new  HashMap<Integer, HashMap<DeclareConstraint,ArrayList<Character>>>();
			hM2.put(1,hM);
			DJMap.put(a, hM2);					
		}
	}
	
	public static void addNewBinaryConstraint(Character a, Character b, DeclareConstraint d){
		if(DJMap.containsKey(a)){
			if(DJMap.get(a).containsKey(2)){
				if(DJMap.get(a).get(2).containsKey(d)){
					DJMap.get(a).get(2).get(d).add(b);
				}else{				
					ArrayList<Character> aL = new ArrayList<Character>();
					aL.add(b);
					HashMap<DeclareConstraint, ArrayList<Character>> hM = new HashMap<DeclareConstraint, ArrayList<Character>>();
					hM.put(d, aL);
					//System.out.println("hM is "+hM.toString());
					DJMap.get(a).get(2).putAll(hM);
				}
			}
			else{
				ArrayList<Character> aL = new ArrayList<Character>();
				aL.add(b);
				HashMap<DeclareConstraint, ArrayList<Character>> hM = new HashMap<DeclareConstraint, ArrayList<Character>>();
				hM.put(d, aL);
				HashMap<Integer, HashMap<DeclareConstraint,ArrayList<Character>>> hM2 = new  HashMap<Integer, HashMap<DeclareConstraint,ArrayList<Character>>>();
				hM2.put(2,hM);
				DJMap.get(a).putAll(hM2);
			}						
					
		}else{
			ArrayList<Character> aL = new ArrayList<Character>();
			aL.add(b);
			HashMap<DeclareConstraint, ArrayList<Character>> hM = new HashMap<DeclareConstraint, ArrayList<Character>>();
			hM.put(d, aL);
			HashMap<Integer, HashMap<DeclareConstraint,ArrayList<Character>>> hM2 = new  HashMap<Integer, HashMap<DeclareConstraint,ArrayList<Character>>>();
			hM2.put(2,hM);
			DJMap.put(a, hM2);					
		}
		
		if(DJMap.containsKey(b)){
			if(DJMap.get(b).containsKey(3)){
				if(DJMap.get(b).get(3).containsKey(d)){
					DJMap.get(b).get(3).get(d).add(a);
				}else{
					ArrayList<Character> aL = new ArrayList<Character>();
					aL.add(a);
					HashMap<DeclareConstraint, ArrayList<Character>> hM = new HashMap<DeclareConstraint, ArrayList<Character>>();
					hM.put(d, aL);	
					DJMap.get(b).get(3).putAll(hM);
				}
			}else{
				ArrayList<Character> aL = new ArrayList<Character>();
				aL.add(a);
				HashMap<DeclareConstraint, ArrayList<Character>> hM = new HashMap<DeclareConstraint, ArrayList<Character>>();
				hM.put(d, aL);
				HashMap<Integer, HashMap<DeclareConstraint,ArrayList<Character>>> hM2 = new  HashMap<Integer, HashMap<DeclareConstraint,ArrayList<Character>>>();
				hM2.put(3,hM);
				DJMap.get(b).putAll(hM2);
			}							
		}else{
			ArrayList<Character> aL = new ArrayList<Character>();
			aL.add(a);
			HashMap<DeclareConstraint, ArrayList<Character>> hM = new HashMap<DeclareConstraint, ArrayList<Character>>();
			hM.put(d, aL);
			HashMap<Integer, HashMap<DeclareConstraint,ArrayList<Character>>> hM2 = new  HashMap<Integer, HashMap<DeclareConstraint,ArrayList<Character>>>();
			hM2.put(3,hM);
			DJMap.put(b, hM2);					
		}
	}
	
	public Collection<Character> getAlphabet(){
		return alphabet;
	}
	
	public HashSet<DeclareConstraint> getConstraints(){
		return constraintSet;
	}	
	
	public HashMap<DeclareConstraint, ArrayList<Character>> incomingUn(Character i){		
		if(DJMap.get(i)!=null)
			return DJMap.get(i).get(1);		
		else 
			return null;
	}
	
	public String getActofChar(Character c){
		return charToAct.get(c);
	}
	
	public Character getActofChar(String s){
		return actToChar.get(s);
	}
	
	public HashMap<DeclareConstraint, ArrayList<Character>> incomingBin(Character i){		
		if(DJMap.get(i)!=null)
			return DJMap.get(i).get(3);			
		else 
			return null;
	}
	
	public HashMap<DeclareConstraint, ArrayList<Character>> outgoingBin(Character i){		
		if(DJMap.get(i)!=null)
			return DJMap.get(i).get(2);		
		else 
			return null;
	}
	
}
