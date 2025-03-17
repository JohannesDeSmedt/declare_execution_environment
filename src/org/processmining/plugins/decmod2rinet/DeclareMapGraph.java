package org.processmining.plugins.decmod2rinet;

/** Author: Johannes De Smedt
 *  E-mail: johannes.desmedt@kuleuven.be
 *  October 2015
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.processmining.framework.util.Pair;
import org.processmining.plugins.declareminer.visualizing.ActivityDefinition;
import org.processmining.plugins.declareminer.visualizing.ConstraintDefinition;
import org.processmining.plugins.declareminer.visualizing.DeclareMap;
import org.processmining.plugins.declareminer.visualizing.Parameter;

public class DeclareMapGraph {
	static HashMap<ActivityDefinition,Integer> adMap = new HashMap<ActivityDefinition,Integer>();
	static HashMap<Integer, HashMap<Integer, HashMap<String,ArrayList<Integer>>>> DJMap = new HashMap<Integer, HashMap<Integer, HashMap<String,ArrayList<Integer>>>>();
	
	public DeclareMapGraph(DeclareMap map){		
		int i=0;
		for(ActivityDefinition aD: map.getModel().getActivityDefinitions()){
			adMap.put(aD, i);
			i++;			
		}
		//System.out.println("adMap "+adMap.toString());
		
		
		for(ConstraintDefinition cD: map.getModel().getConstraintDefinitions()){
				int j=0;
				Iterator<Parameter> p = cD.getParameters().iterator();
				ActivityDefinition a = cD.getFirstBranch(p.next());
				ActivityDefinition b = null;
								
				if(cD.isUnary() && !cD.getCaption().contains("init") && !cD.getName().equals("last")){
					System.out.println("Constraint: "+cD.getName());
					addNewUnaryConstraint(a, cD.getName());
				}
				
				if(cD.isBinary()){
					b = cD.getFirstBranch(p.next());				
				addNewBinaryConstraint(a,b,cD);				
				}
				
		}
	}
	
	public static void addNewUnaryConstraint(ActivityDefinition a, String cD){
		int cardin = (int) cardinality(cD)[0];
		//System.out.println("Cardin "+cardin);
		cD = (String) cardinality(cD)[1];
		
		if(DJMap.containsKey(adMap.get(a))){
			if(DJMap.get(adMap.get(a)).containsKey(1)){
				//System.out.println("Contains 1");
				if(DJMap.get(adMap.get(a)).get(1).containsKey(cD)){
					//System.out.println("Contains "+cD.toString());
					DJMap.get(adMap.get(a)).get(1).get(cD).add(cardin);
				}else{
					//System.out.println("Adding "+cardin+" "+cD);
					ArrayList<Integer> aL = new ArrayList<Integer>();
					aL.add(cardin);
					HashMap<String, ArrayList<Integer>> hM = new HashMap<String, ArrayList<Integer>>();
					hM.put(cD, aL);
					DJMap.get(adMap.get(a)).get(1).putAll(hM);
				}						
			}else{
				ArrayList<Integer> aL = new ArrayList<Integer>();
				aL.add(cardin);
				HashMap<String, ArrayList<Integer>> hM = new HashMap<String, ArrayList<Integer>>();
				hM.put(cD, aL);
				HashMap<Integer, HashMap<String,ArrayList<Integer>>> hM2 = new  HashMap<Integer, HashMap<String,ArrayList<Integer>>>();
				hM2.put(1,hM);
				DJMap.get(adMap.get(a)).putAll(hM2);
			}
					
		}else{
			ArrayList<Integer> aL = new ArrayList<Integer>();
			aL.add(cardin);
			HashMap<String, ArrayList<Integer>> hM = new HashMap<String, ArrayList<Integer>>();
			hM.put(cD, aL);
			HashMap<Integer, HashMap<String,ArrayList<Integer>>> hM2 = new  HashMap<Integer, HashMap<String,ArrayList<Integer>>>();
			hM2.put(1,hM);
			DJMap.put(adMap.get(a), hM2);					
		}
	}
	
	public static void addNewBinaryConstraint(ActivityDefinition a, ActivityDefinition b, ConstraintDefinition cD){
		if(DJMap.containsKey(adMap.get(a))){
			if(DJMap.get(adMap.get(a)).containsKey(2)){
				if(DJMap.get(adMap.get(a)).get(2).containsKey(cD.getName())){
					DJMap.get(adMap.get(a)).get(2).get(cD.getName()).add(adMap.get(b));
				}else{				
					ArrayList<Integer> aL = new ArrayList<Integer>();
					aL.add(adMap.get(b));
					HashMap<String, ArrayList<Integer>> hM = new HashMap<String, ArrayList<Integer>>();
					hM.put(cD.getName(), aL);
					//System.out.println("hM is "+hM.toString());
					DJMap.get(adMap.get(a)).get(2).putAll(hM);
				}
			}
			else{
				ArrayList<Integer> aL = new ArrayList<Integer>();
				aL.add(adMap.get(b));
				HashMap<String, ArrayList<Integer>> hM = new HashMap<String, ArrayList<Integer>>();
				hM.put(cD.getName(), aL);
				HashMap<Integer, HashMap<String,ArrayList<Integer>>> hM2 = new  HashMap<Integer, HashMap<String,ArrayList<Integer>>>();
				hM2.put(2,hM);
				DJMap.get(adMap.get(a)).putAll(hM2);
			}						
					
		}else{
			ArrayList<Integer> aL = new ArrayList<Integer>();
			aL.add(adMap.get(b));
			HashMap<String, ArrayList<Integer>> hM = new HashMap<String, ArrayList<Integer>>();
			hM.put(cD.getName(), aL);
			HashMap<Integer, HashMap<String,ArrayList<Integer>>> hM2 = new  HashMap<Integer, HashMap<String,ArrayList<Integer>>>();
			hM2.put(2,hM);
			DJMap.put(adMap.get(a), hM2);					
		}
		
		if(DJMap.containsKey(adMap.get(b))){
			if(DJMap.get(adMap.get(b)).containsKey(3)){
				if(DJMap.get(adMap.get(b)).get(3).containsKey(cD.getName())){
					DJMap.get(adMap.get(b)).get(3).get(cD.getName()).add(adMap.get(a));
				}else{
					ArrayList<Integer> aL = new ArrayList<Integer>();
					aL.add(adMap.get(a));
					HashMap<String, ArrayList<Integer>> hM = new HashMap<String, ArrayList<Integer>>();
					hM.put(cD.getName(), aL);	
					DJMap.get(adMap.get(b)).get(3).putAll(hM);
				}
			}else{
				ArrayList<Integer> aL = new ArrayList<Integer>();
				aL.add(adMap.get(a));
				HashMap<String, ArrayList<Integer>> hM = new HashMap<String, ArrayList<Integer>>();
				hM.put(cD.getName(), aL);
				HashMap<Integer, HashMap<String,ArrayList<Integer>>> hM2 = new  HashMap<Integer, HashMap<String,ArrayList<Integer>>>();
				hM2.put(3,hM);
				DJMap.get(adMap.get(b)).putAll(hM2);
			}							
		}else{
			ArrayList<Integer> aL = new ArrayList<Integer>();
			aL.add(adMap.get(a));
			HashMap<String, ArrayList<Integer>> hM = new HashMap<String, ArrayList<Integer>>();
			hM.put(cD.getName(), aL);
			HashMap<Integer, HashMap<String,ArrayList<Integer>>> hM2 = new  HashMap<Integer, HashMap<String,ArrayList<Integer>>>();
			hM2.put(3,hM);
			DJMap.put(adMap.get(b), hM2);					
		}
	}
	
	public Integer actToInt(ActivityDefinition a){
		return adMap.get(a);
	}
	
	public ActivityDefinition actToInt(Integer i){
		for(ActivityDefinition a: adMap.keySet())
			if(adMap.get(a).equals(i))
				return a;
		return null;
	}	
		
	public HashMap<String, ArrayList<Integer>> incomingUn(Integer i){		
		if(DJMap.get(i)!=null)
			return DJMap.get(i).get(1);		
		else 
			return null;
	}
	
	public HashMap<String, ArrayList<Integer>> incomingBin(Integer i){		
		if(DJMap.get(i)!=null)
			return DJMap.get(i).get(3);			
		else 
			return null;
	}
	
	public HashMap<String, ArrayList<Integer>> outgoingBin(Integer i){		
		if(DJMap.get(i)!=null)
			return DJMap.get(i).get(2);		
		else 
			return null;
	}
	
	private static Object[] cardinality(String constraint){
		if(constraint.equals("existence"))
			return new Object[]{1,"existence"};
		else if(constraint.contains("existence"))
			return new Object[]{Integer.parseInt(constraint.replace("existence", "")),"existence"};
		if(constraint.contains("exactly"))
			return new Object[]{Integer.parseInt(constraint.replace("exactly", "")),"exactly"};
		if(constraint.contains("absence"))
			return new Object[]{Integer.parseInt(constraint.replace("absence", "")),"absence"};		
		return null;
	}
	
	
}
