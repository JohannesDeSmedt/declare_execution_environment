package org.processmining.plugins.decmod2rinet;

/** Author: Johannes De Smedt
 *  E-mail: johannes.desmedt@kuleuven.be
 *  October 2015
 */

import java.util.HashSet;

import org.processmining.plugins.declareminer.visualizing.ActivityDefinition;
import org.processmining.plugins.declareminer.visualizing.ConstraintDefinition;
import org.processmining.plugins.declareminer.visualizing.ConstraintTemplate;
import org.processmining.plugins.declareminer.visualizing.DeclareMap;

public class UnaryBuilder {
	static DeclareMap map;
	static DeclareMapGraph DJMap;
	
	public static Object[]/*DeclareMapGraph*/ addUnaries(DeclareMap mapIn, DeclareMapGraph DJMapin){
		map = mapIn;
		DJMap = DJMapin;
		
		
		for(ActivityDefinition a: DJMap.adMap.keySet()){
			
			if(!(DJMap.incomingUn(DJMap.actToInt(a))==null)){
			
				
			//Existence
				
			if(DJMap.incomingUn(DJMap.actToInt(a)).containsKey("existence")){
				Integer cardin = DJMap.incomingUn(DJMap.actToInt(a)).get("existence").get(0);
				
				//Incoming
				if(!(DJMap.incomingBin(DJMap.actToInt(a))==null)){
					
				for(String s: DJMap.incomingBin(DJMap.actToInt(a)).keySet()){
					//System.out.println("Reviewing "+a.toString()+" and "+s);
					switch(s){
					case "precedence":
						 for(Integer o: DJMap.incomingBin(DJMap.actToInt(a)).get(s)){
							 System.out.println("p - In :"+DJMap.actToInt(o));
							 updateUnary(o, "existence", 1);
						 }; break;
					case "succession":
						 for(Integer o: DJMap.incomingBin(DJMap.actToInt(a)).get(s)){
							 System.out.println("s - In :"+DJMap.actToInt(o));
							 updateUnary(o, "existence", 1);
						 }; break;
					case "alternate precedence":						
						 for(Integer o: DJMap.incomingBin(DJMap.actToInt(a)).get(s)){
							 System.out.println("ap - In :"+DJMap.actToInt(o));
							 updateUnary(o, "existence", cardin);							 
						 }; break;
					case "alternate succession":
						 for(Integer o: DJMap.incomingBin(DJMap.actToInt(a)).get(s)){
							 System.out.println("as - In :"+DJMap.actToInt(o));
							 updateUnary(o, "existence", cardin);
						 }; break;
					case "chain precedence":
						 for(Integer o: DJMap.incomingBin(DJMap.actToInt(a)).get(s)){
							 System.out.println("cp - In :"+DJMap.actToInt(o));
							 updateUnary(o, "existence", cardin);
						 }; break;
					case "chain succession":
						 for(Integer o: DJMap.incomingBin(DJMap.actToInt(a)).get(s)){
							 System.out.println("cs - In :"+DJMap.actToInt(o));
							 updateUnary(o, "existence", cardin);
						 }; break;
					}
				}
				}
				
				//Outgoing
				if(!(DJMap.outgoingBin(DJMap.actToInt(a))==null)){
				for(String s: DJMap.outgoingBin(DJMap.actToInt(a)).keySet()){
					System.out.println("Reviewing "+a.toString()+" and "+s);
					switch(s){
					case "response":
						 for(Integer o: DJMap.outgoingBin(DJMap.actToInt(a)).get(s)){
							 //System.out.println("p - In :"+DJMap.actToInt(o));
							 updateUnary(o, "existence", 1);
						 }; break;
					case "succession":
						 for(Integer o: DJMap.outgoingBin(DJMap.actToInt(a)).get(s)){
							 //System.out.println("s - In :"+DJMap.actToInt(o));
							 updateUnary(o, "existence", 1);
						 }; break;
					case "alternate response":
						 for(Integer o: DJMap.outgoingBin(DJMap.actToInt(a)).get(s)){
							 //System.out.println("ap - In :"+DJMap.actToInt(o));
							 updateUnary(o, "existence", cardin);
						 }; break;
					case "alternate succession":
						 for(Integer o: DJMap.outgoingBin(DJMap.actToInt(a)).get(s)){
							// System.out.println("as - In :"+DJMap.actToInt(o));
							 updateUnary(o, "existence", cardin);
						 }; break;
					case "chain response":
						 for(Integer o: DJMap.outgoingBin(DJMap.actToInt(a)).get(s)){
							 //System.out.println("cp - In :"+DJMap.actToInt(o));
							 updateUnary(o, "existence", cardin);
						 }; break;
					case "chain succession":
						 for(Integer o: DJMap.outgoingBin(DJMap.actToInt(a)).get(s)){
							 //System.out.println("cs - In :"+DJMap.actToInt(o));
							 updateUnary(o, "existence", cardin);
						 }; break;
					}
				}
				}				
				
			}
			
			//Exactly
			if(DJMap.incomingUn(DJMap.actToInt(a)).containsKey("exactly")){
				
				Integer cardin = DJMap.incomingUn(DJMap.actToInt(a)).get("exactly").get(0);
				
				//Incoming
				if(!(DJMap.incomingBin(DJMap.actToInt(a))==null)){
				for(String s: DJMap.incomingBin(DJMap.actToInt(a)).keySet()){
					System.out.println("Reviewing "+a.toString()+" and "+s);
					switch(s){
					case "precedence":
						 for(Integer o: DJMap.incomingBin(DJMap.actToInt(a)).get(s)){
							 System.out.println("p - In :"+DJMap.actToInt(o));
							 updateUnary(o, "existence", 1);
						 }; break;
					case "succession":
						 for(Integer o: DJMap.incomingBin(DJMap.actToInt(a)).get(s)){
							 System.out.println("s - In :"+DJMap.actToInt(o));
							 updateUnary(o, "existence", 1);
						 }; break;
					case "alternate precedence":
						 for(Integer o: DJMap.incomingBin(DJMap.actToInt(a)).get(s)){
							 System.out.println("ap - In :"+DJMap.actToInt(o));
							 updateUnary(o, "existence", cardin);
						 }; break;
					case "alternate response":
						 for(Integer o: DJMap.incomingBin(DJMap.actToInt(a)).get(s)){
							 System.out.println("ap - In :"+DJMap.actToInt(o));
							 updateUnary(o, "absence", cardin+1);
						 }; break;
					case "alternate succession":
						 for(Integer o: DJMap.incomingBin(DJMap.actToInt(a)).get(s)){
							 System.out.println("as - In :"+DJMap.actToInt(o));
							 updateUnary(o, "exactly", cardin);
						 }; break;
					case "chain precedence":
						 for(Integer o: DJMap.incomingBin(DJMap.actToInt(a)).get(s)){
							 System.out.println("cp - In :"+DJMap.actToInt(o));
							 updateUnary(o, "existence", cardin);
						 }; break;
					case "chain succession":
						 for(Integer o: DJMap.incomingBin(DJMap.actToInt(a)).get(s)){
							 System.out.println("cs - In :"+DJMap.actToInt(o));
							 updateUnary(o, "exactly", cardin);
						 }; break;
					}
				}
				}
				
				//Outgoing
				if(!(DJMap.outgoingBin(DJMap.actToInt(a))==null)){
				for(String s: DJMap.outgoingBin(DJMap.actToInt(a)).keySet()){
					System.out.println("Reviewing "+a.toString()+" and "+s);
					switch(s){
					case "response":
						 for(Integer o: DJMap.outgoingBin(DJMap.actToInt(a)).get(s)){
							 System.out.println("p - In :"+DJMap.actToInt(o));
							 updateUnary(o, "existence", 1);
						 }; break;
					case "succession":
						 for(Integer o: DJMap.outgoingBin(DJMap.actToInt(a)).get(s)){
							 System.out.println("s - In :"+DJMap.actToInt(o));
							 updateUnary(o, "existence", 1);
						 }; break;
					case "alternate response":
						 for(Integer o: DJMap.outgoingBin(DJMap.actToInt(a)).get(s)){
							 System.out.println("ap - In :"+DJMap.actToInt(o));
							 updateUnary(o, "existence", cardin);
						 }; break;
					case "alternate succession":
						 for(Integer o: DJMap.outgoingBin(DJMap.actToInt(a)).get(s)){
							 System.out.println("as - In :"+DJMap.actToInt(o));
							 updateUnary(o, "exactly", cardin);
						 }; break;
					case "chain response":
						 for(Integer o: DJMap.outgoingBin(DJMap.actToInt(a)).get(s)){
							 System.out.println("cp - In :"+DJMap.actToInt(o));
							 updateUnary(o, "existence", cardin);
						 }; break;
					case "chain succession":
						 for(Integer o: DJMap.outgoingBin(DJMap.actToInt(a)).get(s)){
							 System.out.println("cs - In :"+DJMap.actToInt(o));
							 updateUnary(o, "exactly", cardin);
						 }; break;
					}
				}
				}							
			}			
			
			//Absence
			if(DJMap.incomingUn(DJMap.actToInt(a)).containsKey("absence")){				
				Integer cardin = DJMap.incomingUn(DJMap.actToInt(a)).get("absence").get(0);
				
				//Incoming
				if(!(DJMap.incomingBin(DJMap.actToInt(a))==null)){
				for(String s: DJMap.incomingBin(DJMap.actToInt(a)).keySet()){
					System.out.println("Reviewing "+a.toString()+" and "+s);
					switch(s){
					case "alternate response":
						 for(Integer o: DJMap.incomingBin(DJMap.actToInt(a)).get(s)){
							 System.out.println("ap - In :"+DJMap.actToInt(o));
							 updateUnary(o, "absence", cardin);
						 }; break;
					case "alternate succession":
						 for(Integer o: DJMap.incomingBin(DJMap.actToInt(a)).get(s)){
							 System.out.println("as - In :"+DJMap.actToInt(o));
							 updateUnary(o, "absence", cardin);
						 }; break;
					case "chain response":
						 for(Integer o: DJMap.incomingBin(DJMap.actToInt(a)).get(s)){
							 System.out.println("cp - In :"+DJMap.actToInt(o));
							 updateUnary(o, "absence", cardin);
						 }; break;
					case "chain succession":
						 for(Integer o: DJMap.incomingBin(DJMap.actToInt(a)).get(s)){
							 System.out.println("cs - In :"+DJMap.actToInt(o));
							 updateUnary(o, "absence", cardin);
						 }; break;
					}
				}
				}
				
				//Outgoing
				if(!(DJMap.outgoingBin(DJMap.actToInt(a))==null)){
				for(String s: DJMap.outgoingBin(DJMap.actToInt(a)).keySet()){
					System.out.println("Reviewing "+a.toString()+" and "+s);
					switch(s){
					case "alternate precedence":
						 for(Integer o: DJMap.outgoingBin(DJMap.actToInt(a)).get(s)){
							 System.out.println("ap - In :"+DJMap.actToInt(o));
							 updateUnary(o, "absence", cardin);
						 }; break;
					case "alternate succession":
						 for(Integer o: DJMap.outgoingBin(DJMap.actToInt(a)).get(s)){
							 System.out.println("as - In :"+DJMap.actToInt(o));
							 updateUnary(o, "exactly", cardin);
						 }; break;
					case "chain precedence":
						 for(Integer o: DJMap.outgoingBin(DJMap.actToInt(a)).get(s)){
							 System.out.println("cp - In :"+DJMap.actToInt(o));
							 updateUnary(o, "absence", cardin);
						 }; break;
					case "chain succession":
						 for(Integer o: DJMap.outgoingBin(DJMap.actToInt(a)).get(s)){
							 System.out.println("cs - In :"+DJMap.actToInt(o));
							 updateUnary(o, "absence", cardin);
						 }; break;
					}
				}
				}				
			}
		}
		}
		
//		System.out.println("\n\nFinished adding unaries");
//		for(org.processmining.plugins.declareminer.visualizing.ConstraintDefinition cd: map.getModel().getConstraintDefinitions())
//			System.out.println("Contains "+cd.getCaption());
		
		return new Object[]{DJMap,map};
	}
	
	
	private static int[] cardinalities(String constraint){
		
		int lower=0,upper=0;
		if(constraint.contains("existence")){
			lower = Integer.parseInt(constraint.replace("existence", ""));
			upper = 0;
		}
		if(constraint.contains("exactly")){	
			lower = Integer.parseInt(constraint.replace("exactly", ""));
			upper = Integer.parseInt(constraint.replace("exactly", ""));
		}
		if(constraint.contains("absence")){
			lower = 0;
			upper = Integer.parseInt(constraint.replace("absence", ""))-1;
		}		
		System.out.println("Lower "+lower+" upper "+upper);
		return new int[]{lower,upper};		
	}
	
	
	private static void updateUnary(Integer o, String constraint, Integer cardin){
		if(!(DJMap.incomingUn(o)==null)){
			
			switch(constraint){
				case "exactly":
					if(DJMap.incomingUn(o).containsKey("absence") && (DJMap.incomingUn(o).containsKey("existence") || DJMap.incomingUn(o).containsKey("exactly"))){
						System.out.println("\n\n\n ---Error1--- \n\n\n");
					}
					if(DJMap.incomingUn(o).containsKey("absence")){
						if(DJMap.incomingUn(o).get("absence").get(0)-1<cardin)
							System.out.println("\n\n\n ---Error2--- \n\n\n");
						else{
							DJMap.incomingUn(o).remove("absence");
							DJMap.addNewUnaryConstraint(DJMap.actToInt(o), "exactly"+cardin);
							
						}
					}
					if(DJMap.incomingUn(o).containsKey("existence")){
						if(DJMap.incomingUn(o).get("existence").get(0)>cardin)
							System.out.println("\n\n\n ---Error3--- \n\n\n");
						else{
							DJMap.incomingUn(o).remove("existence");
							DJMap.addNewUnaryConstraint(DJMap.actToInt(o), "exactly"+cardin);
						}
					};
					break;
				case "existence":
					if(DJMap.incomingUn(o).containsKey("exactly")){
						if(DJMap.incomingUn(o).get("exactly").get(0)<cardin){
							System.out.println("\n\n\n ---Error4--- \nCase existence - exactly"+DJMap.incomingUn(o).get("exactly").get(0)+" "+cardin+"\n\n");}
						
					}
					if(DJMap.incomingUn(o).containsKey("existence")){
						if(DJMap.incomingUn(o).get("existence").get(0)<cardin){
							DJMap.incomingUn(o).remove("existence");
							//DJMap.addNewUnaryConstraint(DJMap.actToInt(o), "existence"+cardin);
							
							
							insertConstraintIntoModel(constraint,cardin,o,true);
							System.out.println("For "+DJMap.actToInt(o));
							System.out.println("---Error5--- \nCase existence - existence"+DJMap.incomingUn(o).get("existence").get(0)+" "+cardin+"\n\n");
						}
					}
					if(DJMap.incomingUn(o).containsKey("absence")){
						if(DJMap.incomingUn(o).get("absence").get(0)-1<cardin)
							System.out.println("\n\n\n ---Error6--- \n\n\n");
						else{
							if(!DJMap.incomingUn(o).containsKey("existence"))
								DJMap.addNewUnaryConstraint(DJMap.actToInt(o), "existence"+cardin);
						}
					};
					break;
				case "absence":
					if(DJMap.incomingUn(o).containsKey("absence")){
						if( DJMap.incomingUn(o).get("absence").get(0)-1>cardin)
							System.out.println("\n\n\n ---Error7--- \n\n\n");	
					}
					if(DJMap.incomingUn(o).containsKey("exactly")){
						if(DJMap.incomingUn(o).get("exactly").get(0)>cardin)
							System.out.println("\n\n\n ---Error8--- \n\n\n");						
					}
					if(DJMap.incomingUn(o).containsKey("existence")){
						if(DJMap.incomingUn(o).get("existence").get(0)<cardin-1){
							if(!DJMap.incomingUn(o).containsKey("absence"))
								DJMap.addNewUnaryConstraint(DJMap.actToInt(o), "absence"+cardin);
						}else{
							System.out.println("\n\n\n ---Error9--- \n\n\n");
						}
					}; 
					break;
				}
		 }else{
			 insertConstraintIntoModel(constraint,cardin,o,false);			
				//Wouter
		 }
	}	
	
	private static void insertConstraintIntoModel(String constraint, Integer cardin, Integer o, boolean remove){				
		DJMap.addNewUnaryConstraint(DJMap.actToInt(o), constraint+cardin);
		
		if(remove){
			System.out.println("Going to remove "+DJMap.actToInt(o).toString());
			HashSet<ConstraintDefinition> tR = new HashSet<ConstraintDefinition>();
			for(ConstraintDefinition c: map.getModel().getConstraintDefinitions()){
				if(c.isUnary()){		
					System.out.println("1. Is unary!" + c.getCaption());
					if(c.getBranches(c.getFirstParameter()).iterator().next().equals(DJMap.actToInt(o))){					
						tR.add(c);
					}
				}
			}
			HashSet<org.processmining.plugins.declare.visualizing.ConstraintDefinition> tR2 = new HashSet<org.processmining.plugins.declare.visualizing.ConstraintDefinition>();
			for(org.processmining.plugins.declare.visualizing.ConstraintDefinition c: map.getModelCh().getConstraintDefinitions()){
				if(c.isUnary()){		
					System.out.println("2. Is unary!" + c.getCaption()+ " vs "+DJMap.actToInt(o).toString());
					if(c.getBranches(c.getFirstParameter()).iterator().next().toString().equals(DJMap.actToInt(o).toString())){					
						tR2.add(c);
					}
				}
			}
			for(org.processmining.plugins.declare.visualizing.ConstraintDefinition c: map.getModelCh().getConstraintDefinitions()){
				System.out.println("Model contains: "+c.getCaption());
			}
			
			for(ConstraintDefinition c: tR)
				System.out.println("Tr: "+c.getCaption());
			for(org.processmining.plugins.declare.visualizing.ConstraintDefinition c: tR2)
				System.out.println("Tr2: "+c.getCaption());
			if(!(tR==null)){
				for(ConstraintDefinition c: tR)
					map.getModel().deleteConstraintDefinition(c);
				for(ConstraintDefinition c: tR)
					map.getView().deleteConstraintDefiniton(c);
				for(org.processmining.plugins.declare.visualizing.ConstraintDefinition c: tR2)
					map.getModelCh().deleteConstraintDefinition(c);
				for(org.processmining.plugins.declare.visualizing.ConstraintDefinition c: tR2)
					map.getViewCh().deleteConstraintDefiniton(c);
			}	
		}
		
	 	ConstraintTemplate newET = new ConstraintTemplate(
				50000, map.getModel().getLanguage());
		newET.addParameter("A");
		newET.setName(constraint+cardin);
		org.processmining.plugins.declareminer.visualizing.ConstraintDefinition newE = 
				new org.processmining.plugins.declareminer.visualizing.ConstraintDefinition(5000/*Wouter*/+o/*Wouter*/, map.getModel(), newET);
		newE.addBranch(newET.getFirstParameter(), DJMap.actToInt(o));
		map.getView().addConstraintDefinition(newE);
		map.getModel().addConstraintDefiniton(newE);
		//Wouter
		
		org.processmining.plugins.declare.visualizing.Language language = new org.processmining.plugins.declare.visualizing.Language(map.getModel().getLanguage().getId(), map.getModel().getLanguage().getName());
		org.processmining.plugins.declare.visualizing.ConstraintTemplate template = new org.processmining.plugins.declare.visualizing.ConstraintTemplate(newET.getId(), language);
		template.addParameter("A");
		template.setName(constraint+cardin);
		org.processmining.plugins.declare.visualizing.AssignmentModel anAssignmentModel = new org.processmining.plugins.declare.visualizing.AssignmentModel(language);
		org.processmining.plugins.declare.visualizing.ConstraintDefinition definition=new org.processmining.plugins.declare.visualizing.ConstraintDefinition(newE.getId(), anAssignmentModel , template);
		org.processmining.plugins.declare.visualizing.ActivityDefinition actdef = new org.processmining.plugins.declare.visualizing.ActivityDefinition(DJMap.actToInt(o).getName(),DJMap.actToInt(o).getId(), anAssignmentModel);
		definition.addBranch(template.getFirstParameter(), actdef);
		
		String display = ""+cardin;
		if(constraint.contains("exactly")){
		}
		else if(constraint.contains("existence")){
			display=display+"..";
		}
		else if(constraint.contains("absence")){
			display=".."+display;
		}
		definition.setDisplay(display);
		
		map.getModelCh().addConstraintDefiniton(definition);
		map.getViewCh().addConstraintDefinition(definition);		
	}
	
}
