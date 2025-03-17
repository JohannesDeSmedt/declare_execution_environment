package org.processmining.plugins.decmod2rinet;

/** Author: Johannes De Smedt
 *  E-mail: johannes.desmedt@kuleuven.be
 *  October 2015
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.processmining.framework.util.Pair;
import org.processmining.models.graphbased.directed.DirectedGraphEdge;
import org.processmining.models.graphbased.directed.DirectedGraphNode;
import org.processmining.models.graphbased.directed.petrinet.PetrinetEdge;
import org.processmining.models.graphbased.directed.petrinet.PetrinetNode;
import org.processmining.models.graphbased.directed.petrinet.ResetInhibitorNet;
import org.processmining.models.graphbased.directed.petrinet.elements.Arc;
import org.processmining.models.graphbased.directed.petrinet.elements.InhibitorArc;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.ResetArc;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.graphbased.directed.petrinet.impl.ResetInhibitorNetImpl;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.declareminer.visualizing.ActivityDefinition;
import org.processmining.plugins.declareminer.visualizing.ConstraintConnector;
import org.processmining.plugins.declareminer.visualizing.ConstraintDefinition;
import org.processmining.plugins.declareminer.visualizing.DeclareMap;
import org.processmining.plugins.declareminer.visualizing.Parameter;

public class PetrinetBuilder {

	public static Transition end;
	public static Place startP;
	public static Place endP;
	public static HashSet<Place> inhibPlaces;
	public static HashMap<Place,Integer> existencePlaces;
	public static HashSet<Place> exactlyPlaces;
	public static HashSet<Place> absencePlaces;
	public static HashSet<Place> precedencePlaces;
	public static HashSet<Place> coexPlaces;
	public static HashSet<Pair<Transition,Transition>> alternates;
	public static HashMap<Transition,Pair<Transition,Place>> notSuccessionTransitions;
	public static HashMap<Transition,Place> exactlyAbsenceTransitions;
	public static HashMap<Transition,Place> exactlyExistenceTransitions;
	public static HashSet<Place> alternatePrecPlaces;
	public static HashSet<Place> chainPrecPlaces;
	public static HashMap<Pair<Transition,Transition>,Place> respEx;
	public static HashMap<Pair<Transition,Transition>,Place> coEx;
	public static HashMap<Transition,Pair<Place,Transition>> chainRespPlaces;
	public static HashMap<Pair<Transition,Transition>,Place> chainPrecTrans;
	public static HashMap<Pair<Transition,Transition>,Place> chainRespTrans;
	public static HashSet<Transition> visitedL;
	public static HashSet<Transition> visitedR;
	public static HashMap<Transition,HashSet<Place>> transDep; 
	public static HashSet<Place> depPlaces;
	public static HashMap<Transition,HashSet<Place>> transInDep;
	public static HashMap<Transition,HashSet<Place>> transOutDep;	
	public static Marking iniM;
	public static AnnotatedDeclareMapGraph aDmg;
	public static HashMap<ConstraintDefinition, Pair<HashSet<DirectedGraphNode>,HashSet<DirectedGraphEdge>>> constraintConstructs;
	public static HashSet<DirectedGraphNode> nodes;
	public static HashSet<DirectedGraphEdge> edges;
	public static ResetInhibitorNetImpl resetnet;
	public HashSet<DependencyStructure> dpS;
	public static DependencyStructure dp;
	public static DeclareMap map;
	public static HashMap<Pair<Transition,Transition>,Pair<Place,Place>> notCoExTrans;
	public static HashMap<Place, Pair<Transition,Transition>> violCon;
	public static int exclusiveChoiceCount;
	public static HashMap<Place,Pair<Transition,Transition>> choiceP;
	public static HashSet<PetrinetEdge> toAvoid;
	public static HashMap<DirectedGraphNode,String> nodeLabels;
	public static HashMap<String,String> descriptions;
	
	public static HashSet<Transition> altTransBegin = new HashSet<Transition>();
	public static HashSet<Transition> altTransEnd = new HashSet<Transition>();
	public static HashMap<Transition, HashMap<Integer,HashSet<Transition>>> altGraph = new HashMap<Transition, HashMap<Integer,HashSet<Transition>>>();
	
	public PetrinetBuilder(DeclareMap decMapIn/*, HashSet<String> alphabet*/){
		DeclareMap decMap = new DeclareMap(decMapIn.getModel(),decMapIn.getModelCh(),decMapIn.getView(),decMapIn.getViewCh(),decMapIn.getBroker(),decMapIn.getBrokerCh());
		HashSet<String> alphabet = new HashSet<String>();
		
		resetnet = new ResetInhibitorNetImpl("Declare reset net");		
		
		inhibPlaces = new HashSet<Place>();
		existencePlaces = new HashMap<Place,Integer>();
		exactlyPlaces = new HashSet<Place>();
		absencePlaces = new HashSet<Place>();
		precedencePlaces = new HashSet<Place>();
		coexPlaces = new HashSet<Place>();
		notSuccessionTransitions = new HashMap<Transition,Pair<Transition,Place>>();
		exactlyAbsenceTransitions = new HashMap<Transition,Place>();
		exactlyExistenceTransitions = new HashMap<Transition,Place>();
		alternates = new HashSet<Pair<Transition,Transition>>();
		chainRespPlaces = new HashMap<Transition,Pair<Place,Transition>>() ;
		constraintConstructs = new HashMap<ConstraintDefinition, Pair<HashSet<DirectedGraphNode>,HashSet<DirectedGraphEdge>>>();
		dpS = new HashSet<DependencyStructure>();
		alternatePrecPlaces = new HashSet<Place>();
		chainPrecPlaces = new HashSet<Place>();
		violCon = new HashMap<Place, Pair<Transition,Transition>>();
		notCoExTrans = new HashMap<Pair<Transition,Transition>,Pair<Place,Place>>();
		toAvoid = new HashSet<PetrinetEdge>();
		nodeLabels = new HashMap<DirectedGraphNode,String>();
		descriptions = new HashMap<String,String>();
		choiceP = new HashMap<Place,Pair<Transition,Transition>>();
		respEx = new HashMap<Pair<Transition,Transition>,Place>();
		coEx = new HashMap<Pair<Transition,Transition>,Place>();
		chainPrecTrans = new HashMap<Pair<Transition,Transition>,Place>();
		chainRespTrans = new HashMap<Pair<Transition,Transition>,Place>();
		
		iniM = new Marking();
		
		end = resetnet.addTransition("END");		
		endP = (Place) resetnet.addArc(end, resetnet.addPlace("end_p")).getTarget();

		/*** Create alphabet ***/
		for(ActivityDefinition aD: decMapIn.getModel().getActivityDefinitions()){
			System.out.println(aD.toString());
			alphabet.add(aD.toString());
		}			
		
		for(String s: alphabet)			
			if(!s.equals(end.getLabel()))
				resetnet.addTransition(s);
		
		
		//Add unary constraints
		DeclareMapGraph dMG = new DeclareMapGraph(decMap);
			
		Object[] dmgs = UnaryBuilder.addUnaries(decMap, dMG);
		dmgs = UnaryBuilder.addUnaries(decMap, dMG);
		dmgs = UnaryBuilder.addUnaries(decMap, dMG);
		dmgs = UnaryBuilder.addUnaries(decMap, dMG);
		dmgs = UnaryBuilder.addUnaries(decMap, dMG);
		dMG = (DeclareMapGraph) dmgs[0];
		decMap = (DeclareMap) dmgs[1];
	
		Integer l = 120;
		HashMap<ActivityDefinition, String> tempActMap1 = new HashMap<ActivityDefinition, String>();
		HashMap<String,ActivityDefinition> tempActMap2 = new HashMap<String,ActivityDefinition>();
		for(ActivityDefinition a: decMap.getModel().getActivityDefinitions()){
			tempActMap1.put(a, l.toString());
			tempActMap2.put(l.toString(), a);
			l++;
		}
		
		
		ConstraintDescriptions desc = new ConstraintDescriptions();

		for(ConstraintDefinition c: decMap.getModel().getConstraintDefinitions()){
			if(c.isUnary()){
				ActivityDefinition a = c.getBranches(c.getFirstParameter()).iterator().next();
				descriptions.put(c.getCaption(),desc.getDescription(c.getName()).replace("%%", "["+tempActMap1.get(a)+"]"));
			}if(c.isBinary()){
				ActivityDefinition a = c.getBranches(c.getFirstParameter()).iterator().next();
				Iterator<Parameter> parIt = c.getParameters().iterator(); parIt.next();
				ActivityDefinition b = c.getBranches(parIt.next()).iterator().next();
				String toWrite = desc.getDescription(c.getName()).replace("%%", "["+tempActMap1.get(a)+"]").replace("**", "["+tempActMap1.get(b)+"]");
				descriptions.put(c.getCaption(),toWrite);		
			}
		}
		
		for(String s: descriptions.keySet()){
			for(String i: tempActMap2.keySet()){
				descriptions.put(s, descriptions.get(s).replace(i, tempActMap2.get(i).toString()));
			}
		}
		
		aDmg = new AnnotatedDeclareMapGraph(dMG);	
	
		resetnet = addConstraints(resetnet, decMap);
				
		decMap.setaDmg(aDmg);
				
		System.out.println("_____________________\n\n\n\n");
		/** Adding dependency structures **/
		for(Transition t: resetnet.getTransitions()){
			visitedL = new HashSet<Transition>();
			visitedR = new HashSet<Transition>();
			visitedL.add(end);visitedR.add(end);
			
			if(notSuccessionTransitions.containsKey(t) && exactlyAbsenceTransitions.containsKey(t)){		
				//System.out.println("Has both!");
				DependencyStructure d = new DependencyStructure(new Pair<Transition,Place>(t,notSuccessionTransitions.get(t).getSecond()),"ns",null,notSuccessionTransitions.get(t).getFirst(),false);
				DependencyStructure d2 = new DependencyStructure(new Pair<Transition,Place>(t,exactlyAbsenceTransitions.get(t)),"un",exactlyAbsenceTransitions.get(t),null,false);
				
				//System.out.println("First the Unaries...");
				checkBackward(t, d2);
				//checkForward(t, d2);	
				
				visitedL = new HashSet<Transition>();
				visitedR = new HashSet<Transition>();
				visitedL.add(end);visitedR.add(end);visitedL.add(t);visitedR.add(t);
				
				//System.out.println("\nNext the NSs");
				checkBackward(notSuccessionTransitions.get(t).getFirst(), d);
				checkForward(notSuccessionTransitions.get(t).getFirst(), d);
				
				Collection<Place> test = d.getPlaces();
				test.remove(d.getP());
				if(!test.isEmpty())
					dpS.add(d);
				test = d2.getPlaces();
				test.remove(d2.getP());
				if(!test.isEmpty())
					dpS.add(d2);
				//dpS.add(d2);
			}else if(notSuccessionTransitions.containsKey(t)){				
				//System.out.println(t.getLabel()+" has a NS dep ");								
				DependencyStructure d = new DependencyStructure(new Pair<Transition,Place>(t,notSuccessionTransitions.get(t).getSecond()),"ns",null,notSuccessionTransitions.get(t).getFirst(),false);
				
				checkBackward(notSuccessionTransitions.get(t).getFirst(), d);
				checkForward(notSuccessionTransitions.get(t).getFirst(), d);
				
				Collection<Place> test = d.getPlaces();
				test.remove(d.getP());
				if(!test.isEmpty())
					dpS.add(d);
				//dpS.add(d);
			}else if(exactlyAbsenceTransitions.containsKey(t)){			
				System.out.println(t.getLabel()+" has an un dep ");
										
				DependencyStructure d = new DependencyStructure(new Pair<Transition,Place>(t,exactlyAbsenceTransitions.get(t)),"un",exactlyAbsenceTransitions.get(t),null,false);
				
				checkBackward(t, d);
				checkForward(t, d);		
				
				//System.out.println("DP is "+d.toString());
				Collection<Place> test = d.getPlaces();
				test.remove(d.getP());
				if(!test.isEmpty() || (test.isEmpty() && !d.getDP().isEmpty()))
					dpS.add(d);
				//dpS.add(d);				
				//System.out.println("\n\nAdded DPS: "+d.toString());
			}else{
				for(Pair<Transition,Transition> pair: notCoExTrans.keySet()){
					if(pair.getFirst().equals(t)){
						DependencyStructure d = new DependencyStructure(new Pair<Transition,Place>(pair.getSecond(), notCoExTrans.get(pair).getSecond()),"ex",notCoExTrans.get(pair).getSecond(),null,false);
				
						checkBackward(pair.getSecond(), d);
						checkForward(pair.getSecond(), d);
						//System.out.println("D: "+d.toString());
						dpS.add(d);
					}
					if(pair.getSecond().equals(t)){
						DependencyStructure d = new DependencyStructure(new Pair<Transition,Place>(pair.getFirst(), notCoExTrans.get(pair).getFirst()),"ex",notCoExTrans.get(pair).getFirst(),null,false);
				
						checkBackward(pair.getFirst(), d);
						checkForward(pair.getFirst(), d);
						//System.out.println("D: "+d.toString());
						dpS.add(d);
					}
				}
			}
		}	
		
		HashSet<DependencyStructure> toRemove = new HashSet<DependencyStructure>();
		for(DependencyStructure ds: dpS){
			System.out.println("\n\nDS "+ds.toString());
			if(ds.getPlaces().size()==0)
				toRemove.add(ds);
		}
		dpS.removeAll(toRemove);
		
		
		map = decMap;	
		
		for(DirectedGraphNode n: resetnet.getNodes())
			nodeLabels.put(n, n.getLabel());
		
				
		System.out.println("\n\nChecking alternates--");
		for(Transition t: resetnet.getTransitions()){
			boolean in = false;
			boolean out = false;
			boolean existence = false;
			existence = exactlyExistenceTransitions.containsKey(t);
			//System.out.println("Checking "+t.getLabel()+" "+existence);
			
			HashSet<Transition> inT = new HashSet<Transition>();
			HashSet<Transition> outT = new HashSet<Transition>();
			boolean alternate = false;
			
			for(PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> e: resetnet.getInEdges(t)){
				if((e.getSource().getLabel().contains("alternate precedence") || e.getSource().getLabel().contains("alternate response_B")
						|| ((e.getSource().getLabel().contains("response") || e.getSource().getLabel().contains("precedence")) && !(e.getSource().getLabel().contains("alternate")))
						) && !e.getSource().getLabel().contains("not")&& !e.getSource().getLabel().contains("chain")){
					//in=true;
					alternate=true;
					for(PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> e2: resetnet.getInEdges(e.getSource())){
						if(exactlyExistenceTransitions.containsKey((Transition) e2.getSource())){
							inT.add((Transition) e2.getSource());
							in=true;
						}
					}
				}
			}
			for(PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> e: resetnet.getOutEdges(t)){
				if((e.getTarget().getLabel().contains("alternate precedence") || e.getTarget().getLabel().contains("alternate response_B")
						|| ((e.getTarget().getLabel().contains("response") || e.getTarget().getLabel().contains("precedence")) && !(e.getTarget().getLabel().contains("alternate")))
						) && !e.getTarget().getLabel().contains("not") && !e.getTarget().getLabel().contains("chain")){
					//out=true;
					alternate=true;
					for(PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> e2: resetnet.getOutEdges(e.getTarget()))
						if(exactlyExistenceTransitions.containsKey((Transition) e2.getTarget())){
							outT.add((Transition) e2.getTarget());
							out=true;
						}
				}
			}
//			for(Pair<Transition,Transition> p : chainPrecTrans.keySet()){
//				if(t.equals(p.getFirst())){
//					alternate=true;
//					if(exactlyExistenceTransitions.containsKey(p.getSecond())){
//						outT.add(p.getSecond());
//						out=true;
//					}
//				}				
//			}
//			for(Pair<Transition,Transition> p : chainRespTrans.keySet()){
//				if(t.equals(p.getFirst())){
//					alternate=true;
//					if(exactlyExistenceTransitions.containsKey(p.getSecond())){
//						outT.add(p.getSecond());
//						out=true;
//					}
//				}				
//			}
//			for(Pair<Transition,Transition> p : chainPrecTrans.keySet()){
//				if(t.equals(p.getSecond())){
//					alternate=true;
//					if(exactlyExistenceTransitions.containsKey(p.getFirst())){
//						inT.add(p.getFirst());
//						in=true;
//					}
//				}				
//			}
//			for(Pair<Transition,Transition> p : chainRespTrans.keySet()){
//				if(t.equals(p.getSecond())){
//					alternate=true;
//					if(exactlyExistenceTransitions.containsKey(p.getFirst())){
//						inT.add(p.getFirst());
//						in=true;
//					}
//				}				
//			}
			
			HashMap<Integer,HashSet<Transition>> toW = new HashMap<Integer,HashSet<Transition>>();
			toW.put(1, inT);
			toW.put(2, outT);
			if(alternate)
				altGraph.put(t, toW);
			if((!in && out) && !t.equals(end) && existence)
				altTransBegin.add(t);			

			if((in && !out) && !t.equals(end) && existence)
				altTransEnd.add(t);
	
		}	
		System.out.println(altTransBegin+" are the entrance of an alternate chain");
		System.out.println(altTransEnd+" are the end of an alternate chain");	
		System.out.println("\n\nAltgraph: ");
		for(Transition t: altGraph.keySet()){
			System.out.println("For "+t.getLabel()+" in: "+altGraph.get(t).get(1).toString()+" Out: "+altGraph.get(t).get(2).toString());
		}
		
	}

	public Object[] returnOutput(){
		return new Object[]{resetnet,iniM, map, constraintConstructs, end, dpS, existencePlaces};
	}
	
	private static void checkBackward(Transition t, DependencyStructure dp){		
		if(!visitedL.contains(t)){
			visitedL.add(t);
			for(PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> e: resetnet.getInEdges(t)){
				if(!toAvoid.contains(e)){
					Place source = (Place) e.getSource();
					String label = e.getSource().getLabel();
					if((label.contains("response") || label.contains("succession")) && !label.contains("not") && !label.contains("alternate response_A")){
						dp.addT(t);
						for(PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> e2: resetnet.getInEdges(source)){
							dp.addPlaceBw(source,(Transition) e2.getSource());
							if(!e2.getSource().equals(t)){
								checkBackward((Transition) e2.getSource(), dp);
								checkForward((Transition) e2.getSource(), dp);
							}
						}
					}else if(exactlyPlaces.contains(source)){
						dp.addT(t);
						dp.addPlaceBw(source, t);
					}else if(label.contains("choice")){
						dp.addT(t);
						dp.addPlaceBw(source, t);
					}
				}
			}
			for(Pair<Transition,Transition> p: respEx.keySet()){
				if(p.getSecond().equals(t)){
					//System.out.println("Going to make another DP for "+p.getFirst()+" and "+p.getSecond()+" p is "+respEx.get(p));
					DependencyStructure d = new DependencyStructure(new Pair<Transition,Place>(t,respEx.get(p)),"respex",null,null,true);
					d.addPlaceBw(respEx.get(p), p.getFirst());
					dp.addDepStruc(d);

					checkBackward(p.getFirst(), d);
					checkForward(p.getFirst(), d);
				}					
			}
		}//else{
		//	System.out.println("This will cause a deadlock!");
		//}
	}
	
	private static void checkForward(Transition t, DependencyStructure dp){		
		if(!visitedR.contains(t)){
			visitedR.add(t);
			for(PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> e: resetnet.getOutEdges(t)){
				if(!toAvoid.contains(e)){
					Place target = (Place) e.getTarget();	
					String label = e.getTarget().getLabel();					
					if((label.contains("precedence") || label.contains("succession")) && !label.contains("not")
							&& (resetnet.getArc(target, t)==null)){
						System.out.println("Exists!!"+resetnet.getArc(target, t));
						System.out.println("------Adding "+t.getLabel()+" for "+target.getLabel());
						dp.addT(t);
						for(PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> e2: resetnet.getOutEdges(target)){
							if(!e2.getTarget().equals(t) && !(e2 instanceof ResetArc)){
								DependencyStructure d = new DependencyStructure(new Pair<Transition,Place>(t,target),dp.getType(),null,null,false);
								d.addPlaceBw(target, (Transition) e2.getTarget());
								dp.addDepStruc(d);

								checkBackward((Transition) e2.getTarget(), d);
								checkForward((Transition) e2.getTarget(), d);
							}
						}				
					}else if(existencePlaces.containsKey(target)){
						dp.addT(t);
						dp.addPlaceBw(target, t);
					}	
				}
			}
			// ONLY FOR CHAIN PRECEDENCE
			for(PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> e: resetnet.getInEdges(t)){
				//System.out.println("R but IN! - For "+t.getLabel()+" label "+e.getLabel());
				if(!toAvoid.contains(e)){
					//System.out.println("Not avoided");
					Place source = (Place) e.getSource();	
					String label = e.getSource().getLabel();
					if(label.contains("chain precedence") && e instanceof ResetArc){
						dp.addT(t);
						for(PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> e2: resetnet.getOutEdges(source)){
							if(e2 instanceof InhibitorArc){
								DependencyStructure d = new DependencyStructure(new Pair<Transition,Place>(t,source),dp.getType(),null,null,false);
								d.addPlaceBw(source, (Transition) e2.getTarget());
								dp.addDepStruc(d);
								//System.out.println("New dependency structure for "+source.getLabel());							
								
								checkBackward((Transition) e2.getTarget(), d);
								checkForward((Transition) e2.getTarget(), d);
							}
						}						
					}
				}
			}
		}
	}	
	
	

	
	private static ResetInhibitorNetImpl addConstraints(ResetInhibitorNetImpl resetnet, DeclareMap map){
		HashSet<String> constraintSet = new HashSet<String>();		
		for (ConstraintDefinition c : map.getModel().getConstraintDefinitions()) {
			if(!constraintSet.contains(c.getCaption())){
			System.out.println("Constraint: " + c.getName());
			Transition A = null,B = null;
			Integer f = 0;
			for (Parameter p : c.getParameters()) {
				System.out.println(c.getBranches(p).iterator().next()
						.toString());
				if (f == 0)
					A = getTransByName(resetnet, c.getBranches(p).iterator().next()
						.toString());
				if (f == 1)
					B = getTransByName(resetnet, c.getBranches(p).iterator().next()
							.toString());
				f++;
			}
			resetnet = addConstraint(A,B,c.getName(),resetnet, c);
			constraintSet.add(c.getCaption());
			}
		}	
		return resetnet;
	}
	
		
	private static Transition getTransByName(ResetInhibitorNet net, String name){
		for (Transition tB : net.getTransitions()){
			if (tB.getLabel().equals(name))
				return tB;
		}
		return null;
	}
	
	private static ResetInhibitorNetImpl addConstraint(Transition A, Transition B, String con, ResetInhibitorNetImpl resetnet, ConstraintDefinition c){
		nodes = new HashSet<DirectedGraphNode>();
		edges = new HashSet<DirectedGraphEdge>();
		
		switch (con){
		case "init":
			addInit(resetnet, A, 1);
			break;
		case "exactly1":
			addExactlyN(resetnet, A, 1);
			break;
		case "exactly2":
			addExactlyN(resetnet, A, 2);
			break;
		case "exactly3":
			addExactlyN(resetnet, A, 2);
			break;
		case "existence":
			addExistenceN(resetnet, A, 1);
			break;
		case "existence1":
			addExistenceN(resetnet, A, 1);
			break;
		case "existence2":
			addExistenceN(resetnet, A, 2);
			break;
		case "existence3":
			addExistenceN(resetnet, A, 3);
			break;
		case "absence1":
			addAbsenceN(resetnet, A, 1);
			break;
		case "absence2":
			addAbsenceN(resetnet, A, 2);
			break;	
		case "absence3":
			addAbsenceN(resetnet, A, 3);
			break;	
		case "precedence":
			addPrecedence(resetnet, A, B);
			break;		
		case "response":
			addResponse(resetnet, A, B);
			break;
		case "succession":
			addResponse(resetnet, A, B);
			addPrecedence(resetnet, A, B);
			break;
		case "alternate precedence":
			addAlternatePrecedence(resetnet, A, B);
			break;		
		case "alternate response":
			addAlternateResponse(resetnet, A, B);
			break;
		case "alternate succession":
			//addAlternateSuccession(resetnet, A, B);
			addAlternatePrecedence(resetnet,A,B);
			addAlternateResponse(resetnet,A,B);
			break;
		case "chain precedence":
			addChainPrecedence(resetnet, A, B);
			break;		
		case "chain response":
			addChainResponse(resetnet, A, B, false);
			break;
		case "chain succession":
			//addChainSuccession(resetnet, A, B);
			addChainResponse(resetnet,A,B, true);
			addChainPrecedence(resetnet,A,B);
			break;	
		case "not co-existence":
			addNotCoExistence(resetnet, A, B);
			break;	
		case "not succession":
			addNotSuccession(resetnet, A, B);
			break;
		case "not chain succession":
			addNotChainSuccession(resetnet, A, B);
			break;
		case "choice":
			addChoice(resetnet, A, B);
			break;
		case "exclusive choice":
			addExclusiveChoice(resetnet, A, B);
			break;
		case "responded existence":
			addRespondedExistence(resetnet, A, B);
			break;
		case "co-existence":
			addCoExistence(A, B);
			break;	
		case "last":
			addLast(A, 1);
			break;
		}
		
		constraintConstructs.put(c, new Pair<HashSet<DirectedGraphNode>,HashSet<DirectedGraphEdge>>(nodes,edges));
		System.out.println("Nodes "+nodes.toString()+"  edges "+edges.toString());
		return resetnet;
	}
	
	/*** Unary ***/	
	private static ResetInhibitorNetImpl addExistenceN(ResetInhibitorNetImpl resetnet, Transition in, Integer n){	
		Arc e = resetnet.addArc(in, resetnet.addPlace(in.getLabel()+"-existence"+n));
		Place beginP = (Place) e.getTarget();
		existencePlaces.put(beginP,n);
		
		for(int i=0;i<n;i++){
			 Arc eL = resetnet.addArc(beginP,end);
			 edges.add(eL);
		}
		
		exactlyExistenceTransitions.put(in, beginP);
		nodes.add(beginP);
		edges.add(e);		
		violCon.put(beginP, new Pair<Transition,Transition>(in,in));
		return resetnet;
	}
	
	private static ResetInhibitorNetImpl addInit(ResetInhibitorNetImpl resetnet, Transition in, Integer n){	
		ResetArc r1 =  resetnet.addResetArc(resetnet.addPlace(in.getLabel()+"-init"+n),in);
		Place helpP = (Place) r1.getSource();
		//resetnet.addArc(start, helpP);
		iniM.add(helpP);
		
		for(Transition t:resetnet.getTransitions())
			if(t!=in /*&& t!=start*/){
				InhibitorArc i = resetnet.addInhibitorArc(helpP, t);			
				toAvoid.add(i);
				edges.add(i);
			}
		
		nodes.add(helpP);edges.add(r1);
		return resetnet;
	}
	
	private static ResetInhibitorNetImpl addLast(Transition in, Integer n){	
		Place helpP = (Place) resetnet.addArc(in, resetnet.addPlace(in.getLabel()+"-last"+n)).getTarget();
		resetnet.addArc(helpP, end);
				
		for(PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> e: resetnet.getInEdges(end)){	
			if (e instanceof InhibitorArc){
				System.out.println("Place: "+((Place)e.getSource()).getLabel()+" vs "+in.getLabel()+" "+((Place)e.getSource()).getLabel().contains(in.getLabel()));
				if(!((Place)e.getSource()).getLabel().contains(in.getLabel()))
					resetnet.addInhibitorArc((Place) e.getSource(),in);
			}
		}
		
		for(Transition t:resetnet.getTransitions())
			if(t!=in && t!=end /*&& t!=start*/)
				resetnet.addResetArc(helpP, t);			
		return resetnet;
	}
		
	private static ResetInhibitorNetImpl addAbsenceN(ResetInhibitorNetImpl resetnet, Transition in, Integer n){
		Arc a = resetnet.addArc(resetnet.addPlace(in.getLabel()+"-absence"+(n)), in);
		Place beginP = (Place) a.getSource();
		absencePlaces.add(beginP);

		iniM.add(beginP, n-1);
		exactlyAbsenceTransitions.put(in,beginP);
		edges.add(a);
		nodes.add(beginP);
		return resetnet;
	}
	
	private static ResetInhibitorNetImpl addExactlyN(ResetInhibitorNetImpl resetnet, Transition in, Integer n){
		Arc a = resetnet.addArc(resetnet.addPlace(in.getLabel()+"-exactly"+n), in);
		Place beginP = (Place) a.getSource();
		exactlyPlaces.add(beginP);

		iniM.add(beginP, n);
		edges.add(resetnet.addInhibitorArc(beginP, end));
		exactlyAbsenceTransitions.put(in, beginP);
		exactlyExistenceTransitions.put(in, beginP);		
		violCon.put(beginP, new Pair<Transition,Transition>(in,in));
		
		edges.add(a);
		nodes.add(beginP);		
		return resetnet;
	}
	
	/*** Simple ordered ***/
	private static ResetInhibitorNetImpl addPrecedence(ResetInhibitorNetImpl resetnet, Transition in, Transition out) {
		Place helpPlace = resetnet.addPlace(in+"-precedence-"+out);
		nodes.add(helpPlace);
		//nodes.add(in);
		//nodes.add(out);
		edges.add(resetnet.addArc(in, helpPlace));
		edges.add(resetnet.addArc(helpPlace,out));
		edges.add(resetnet.addArc(out, helpPlace));
		
		precedencePlaces.add(helpPlace);		
		return resetnet;
	}
	
	private static ResetInhibitorNet addResponse(ResetInhibitorNet resetnet, Transition in, Transition out){
		Place helpPlace = resetnet.addPlace(in+"-response-"+out);
		nodes.add(helpPlace);
		//nodes.add(in);
		//nodes.add(out);
				
		edges.add(resetnet.addArc(in, helpPlace));
		edges.add(resetnet.addInhibitorArc(helpPlace, end));
		edges.add(resetnet.addResetArc(helpPlace, out));

		violCon.put(helpPlace, new Pair<Transition,Transition>(in,out));
		return resetnet;
	}
	
	/*** Alternating ordered ***/
	private static ResetInhibitorNet addAlternateResponse(ResetInhibitorNet resetnet, Transition in, Transition out){
		Arc a1 = resetnet.addArc(resetnet.addPlace(in.getLabel()+"-alternate response_A-"+out.getLabel()), in);
		Place beginPi = (Place) a1.getSource();
		ResetArc r1 = resetnet.addResetArc(beginPi, in);
		ResetArc r2 = resetnet.addResetArc(resetnet.addPlace(in.getLabel()+"-alternate response_B-"+out.getLabel()), out);
		Place beginPo = (Place) r2.getSource();
		Arc a2 = resetnet.addArc(in, beginPo);
		Arc a3 = resetnet.addArc(out, beginPi);

		InhibitorArc i1 = resetnet.addInhibitorArc(beginPo, end);
		inhibPlaces.add(beginPo);

		iniM.add(beginPi);
		alternates.add(new Pair<Transition,Transition>(out,in));
		violCon.put(beginPo, new Pair<Transition,Transition>(in,out));
		nodes.add(beginPi); nodes.add(beginPo);
		edges.add(a1);edges.add(a2);edges.add(a3);edges.add(r1);edges.add(r2);edges.add(i1);
		return resetnet;
	}
	
	private static ResetInhibitorNet addAlternatePrecedence(ResetInhibitorNet resetnet, Transition in, Transition out){
		Arc a1 = resetnet.addArc(resetnet.addPlace(in.getLabel()+"-alternate precedence_a-"+out.getLabel()), out);
		Place beginPo = (Place) a1.getSource();		
		ResetArc r1 = resetnet.addResetArc(beginPo, out);
		Arc a2 = resetnet.addArc(in, beginPo);

		precedencePlaces.add(beginPo);
		alternates.add(new Pair<Transition,Transition>(in,out));
		alternatePrecPlaces.add(beginPo);
		nodes.add(beginPo);
		edges.add(a1);edges.add(a2);edges.add(r1);
		return resetnet;
	}
	
	private static ResetInhibitorNet addAlternateSuccession(ResetInhibitorNet resetnet, Transition in, Transition out){
		Arc a1 = resetnet.addArc(resetnet.addPlace(in.getLabel()+"-alternate succession_A-"+out.getLabel()), in);
		Place beginPi = (Place) a1.getSource();
		Arc a2 = resetnet.addArc(resetnet.addPlace(out.getLabel()+"-alternate succession_B-"+in.getLabel()), out);
		Place beginPo = (Place) a2.getSource();
		Arc a3 = resetnet.addArc(in, beginPo);
		Arc a4 = resetnet.addArc(out, beginPi);
		InhibitorArc i1 = resetnet.addInhibitorArc(beginPo, end);
		inhibPlaces.add(beginPo);

		iniM.add(beginPi);
		precedencePlaces.add(beginPo);
		alternatePrecPlaces.add(beginPo);
		nodes.add(beginPo);nodes.add(beginPi);
		edges.add(a1);edges.add(a2);edges.add(a3);edges.add(a4);edges.add(i1);
		
		violCon.put(beginPo, new Pair<Transition,Transition>(in,out));
		return resetnet;
	}
		
	/*** Chain ordered ***/
	private static ResetInhibitorNet addChainResponse(ResetInhibitorNet resetnet, Transition in, Transition out, boolean succession){
		ResetArc r1 = resetnet.addResetArc(resetnet.addPlace(in.getLabel()+"-chain response-"+out.getLabel()), out);
		Place beginPo = (Place) r1.getSource();
		edges.add(resetnet.addArc(in, beginPo));
		edges.add(resetnet.addInhibitorArc(beginPo, in));		
		
		nodes.add(beginPo);edges.add(r1);
		inhibPlaces.add(beginPo);
		
		//if(!succession)
			chainRespPlaces.put(in, new Pair<Place,Transition>(beginPo,out));
		
		for(Transition t:resetnet.getTransitions())
			if(t!=in && t!=out /*&& t!=start*/){
				InhibitorArc i = resetnet.addInhibitorArc(beginPo, t);
				toAvoid.add(i);
				edges.add(i);
			}
		alternates.add(new Pair<Transition,Transition>(out,in));
		
		violCon.put(beginPo, new Pair<Transition,Transition>(in,out));
		chainRespTrans.put(new Pair<Transition,Transition>(in,out),beginPo);
		return resetnet;
	}
	
	private static ResetInhibitorNet addChainPrecedence(ResetInhibitorNet resetnet, Transition in, Transition out){
		ResetArc r1 = resetnet.addResetArc(resetnet.addPlace(in.getLabel()+"-chain precedence-"+out.getLabel()), in); 
		Place beginPo = (Place) r1.getSource();
		edges.add(resetnet.addArc(out, beginPo));
		edges.add(resetnet.addInhibitorArc(beginPo, out));
	
		iniM.add(beginPo);
		nodes.add(beginPo);edges.add(r1);
		
		for(Transition t:resetnet.getTransitions())
			if(t!=in && t!=out && t!=end/*&& t!=start*/){
				Arc a = resetnet.addArc(t,beginPo);
				toAvoid.add(a);
				edges.add(a);
			}
		precedencePlaces.add(beginPo);
		alternatePrecPlaces.add(beginPo);
		alternates.add(new Pair<Transition,Transition>(in,out));
		chainPrecPlaces.add(beginPo);
		chainPrecTrans.put(new Pair<Transition,Transition>(in,out),beginPo);
		return resetnet;
	}
	
	private static ResetInhibitorNet addChainSuccession(ResetInhibitorNet resetnet, Transition in, Transition out){		

		Place beginPo = (Place) resetnet.addArc(in, resetnet.addPlace(in.getLabel()+"-chain succession_a-"+out.getLabel())).getTarget();
		toAvoid.add(resetnet.addInhibitorArc(beginPo, in));
		resetnet.addResetArc(beginPo, out);
		
		//Place endPi = (Place) resetnet.addArc(out, resetnet.addPlace(in.getLabel()+"-chain succession_b-"+out.getLabel())).getTarget();
		Arc r = resetnet.addArc(out, resetnet.addPlace(in.getLabel()+"-chain succession_b-"+out.getLabel()));
		toAvoid.add(r);
		Place endPi = (Place) r.getTarget();
		toAvoid.add(resetnet.addInhibitorArc(endPi, out));
		toAvoid.add(resetnet.addResetArc(endPi, in));

		iniM.add(endPi);
		inhibPlaces.add(endPi);
		
		
		for(Transition t:resetnet.getTransitions())
			if(t!=in && t!=out){
				InhibitorArc rr = resetnet.addInhibitorArc(beginPo, t);
				toAvoid.add(rr);
			}
		precedencePlaces.add(endPi);
		
		violCon.put(beginPo, new Pair<Transition,Transition>(in,out));
		return resetnet;
	}
	
	/*** Negative ***/
	private static ResetInhibitorNetImpl addNotCoExistence(ResetInhibitorNetImpl resetnet, Transition in, Transition out){
		Arc a1 = resetnet.addArc(in, resetnet.addPlace(in.getLabel()+"-not co-existence_a-"+out.getLabel()));
		Place helpPi = (Place) a1.getTarget();
		Arc a2 = resetnet.addArc(out, resetnet.addPlace(in.getLabel()+"-not co-existence_b-"+out.getLabel()));
		Place helpPo = (Place) a2.getTarget();
		edges.add(resetnet.addInhibitorArc(helpPi, out));
		edges.add(resetnet.addInhibitorArc(helpPo, in));
		
		nodes.add(helpPo);nodes.add(helpPi);edges.add(a1);edges.add(a2);
		
		
		if(aDmg.getDepAct(in.getLabel())!=null)
			addExEdges(in,helpPi,helpPo);		
				
		if(aDmg.getDepAct(out.getLabel())!=null)
			addExEdges(out,helpPo,helpPi);
				
		notCoExTrans.put(new Pair<Transition,Transition>(in,out), new Pair<Place,Place>(helpPi,helpPo));
	
		return resetnet;
	}
	
	private static void addExEdges(Transition t, Place helpPi, Place helpPo){
		System.out.println("Testing "+t.getLabel());
		if(aDmg.getDepAct(t.getLabel()).getFirst()!=null){
			for(String s: aDmg.getDepAct(t.getLabel()).getFirst()){
				//System.out.println("L1- "+s);
				edges.add(resetnet.addArc(getTransitionsByLabel(resetnet, s).iterator().next(), helpPi));
				addExEdges(getTransitionsByLabel(resetnet, s).iterator().next(), helpPi, helpPo);
			}
			for(String s: aDmg.getDepAct(t.getLabel()).getFirst()){
				//System.out.println("L2- "+s);
				edges.add(resetnet.addInhibitorArc(helpPo, getTransitionsByLabel(resetnet, s).iterator().next()));
				addExEdges(getTransitionsByLabel(resetnet, s).iterator().next(), helpPi, helpPo);
			}	
		}
		
		if(aDmg.getDepAct(t.getLabel()).getSecond()!=null){
			for(String s: aDmg.getDepAct(t.getLabel()).getSecond()){
				//System.out.println("R1- "+s);
				edges.add(resetnet.addArc(getTransitionsByLabel(resetnet, s).iterator().next(), helpPi));
				addExEdges(getTransitionsByLabel(resetnet, s).iterator().next(), helpPi, helpPo);
			}
			for(String s: aDmg.getDepAct(t.getLabel()).getSecond()){
				//System.out.println("R2- "+s);
				edges.add(resetnet.addInhibitorArc(helpPo, getTransitionsByLabel(resetnet, s).iterator().next()));
				addExEdges(getTransitionsByLabel(resetnet, s).iterator().next(), helpPi, helpPo);
			}
		}	
	}

	private static ResetInhibitorNetImpl addNotSuccession(ResetInhibitorNetImpl resetnet, Transition in, Transition out){
		Arc a1 = resetnet.addArc(in, resetnet.addPlace(in.getLabel()+"-not succession-"+out.getLabel())); 
		Place helpP = (Place) a1.getTarget();
		InhibitorArc i1 = resetnet.addInhibitorArc(helpP, out);
	
		notSuccessionTransitions.put(in, new Pair<Transition,Place>(out,helpP));
		nodes.add(helpP);edges.add(a1);edges.add(i1);
		return resetnet;
	}

	private static ResetInhibitorNetImpl addNotChainSuccession(ResetInhibitorNetImpl resetnet, Transition in, Transition out){
		Place helpP = (Place) resetnet.addArc(in, resetnet.addPlace(in.getLabel()+"-not chain succession-"+out.getLabel())).getTarget();
		resetnet.addInhibitorArc(helpP, out);
		
		for(Transition t:resetnet.getTransitions())
			if(t!=in && t!=out)
				resetnet.addResetArc(helpP, t);		
		
		violCon.put(helpP, new Pair<Transition,Transition>(in,out));
		return resetnet;
	}
	
	/*** Choice ***/
	private static ResetInhibitorNetImpl addChoice(ResetInhibitorNetImpl resetnet, Transition in, Transition out){
		ResetArc a1 = resetnet.addResetArc(resetnet.addPlace(in.getLabel()+"-choice-"+out.getLabel()), in);
		Place helpP = (Place) a1.getSource();
		ResetArc r = resetnet.addResetArc(helpP, out);
		edges.add(r);
		edges.add(resetnet.addInhibitorArc(helpP,end));

		iniM.add(helpP);
		
		choiceP.put(helpP, new Pair<Transition,Transition>(in,out));
		
		nodes.add(helpP);edges.add(a1);
		violCon.put(helpP, new Pair<Transition,Transition>(in,out));
		return resetnet;
	}
	
	private static ResetInhibitorNetImpl addExclusiveChoice(ResetInhibitorNetImpl resetnet, Transition in, Transition out){
		Arc a1 = resetnet.addArc(in, resetnet.addPlace(in.getLabel()+"-exclusive choice_a-"+out.getLabel()));
		Place helpPi = (Place) a1.getTarget();
		Arc a2 = resetnet.addArc(out, resetnet.addPlace(in.getLabel()+"-exclusive choice_b-"+out.getLabel()));
		Place helpPo = (Place) a2.getTarget();
		InhibitorArc i1 = resetnet.addInhibitorArc(helpPi, out);
		InhibitorArc i2 = resetnet.addInhibitorArc(helpPo, in);
		ResetArc r1 = resetnet.addResetArc(resetnet.addPlace(in.getLabel()+"-exclusive choice_c-"+out.getLabel()),in);
		Place helpP = (Place) r1.getSource();
		ResetArc r2 = resetnet.addResetArc(helpP, out);
		InhibitorArc i3 = resetnet.addInhibitorArc(helpP, end);
		
		iniM.add(helpP);
		
		nodes.add(helpP);nodes.add(helpPo);nodes.add(helpPi);
		edges.add(a1);edges.add(a2);edges.add(i1);edges.add(i2);edges.add(i3);edges.add(r1);edges.add(r2);
		
		int exclusiveChoiceCountIn = 1;
		int exclusiveChoiceCountOut = 1;
		
		
		if(aDmg.getDepAct(in.getLabel())!=null)
			if(aDmg.getDepAct(in.getLabel()).getFirst()!=null)
				addExEdges(in,helpPi,helpPo);					
		
		if(aDmg.getDepAct(out.getLabel())!=null)
			addExEdges(out,helpPi,helpPo);
			
		System.out.println("Exclusive choice counts - in:"+exclusiveChoiceCountIn+" out "+exclusiveChoiceCountOut);
		
		notCoExTrans.put(new Pair<Transition,Transition>(in,out), new Pair<Place,Place>(helpPi,helpPo));
		violCon.put(helpP, new Pair<Transition,Transition>(in,out));
		
		return resetnet;
	}	

	
	/*** Binary Existence ***/
	private static ResetInhibitorNetImpl addRespondedExistence(ResetInhibitorNetImpl resetnet, Transition in, Transition out){
		Place helpPa = (Place) resetnet.addArc(in, resetnet.addPlace(in.getLabel()+"-responded existence_a-"+out.getLabel())).getTarget();
		Place helpPb = (Place) resetnet.addArc(out, resetnet.addPlace(in.getLabel()+"-responded existence_b-"+out.getLabel())).getTarget();
		resetnet.addInhibitorArc(helpPa, end);
		Transition invi = (Transition) resetnet.addArc(helpPb, resetnet.addTransition("invisible_re")).getTarget();
		invi.setInvisible(true);
		resetnet.addResetArc(helpPb, invi);
		resetnet.addResetArc(helpPa, invi);
		
		inhibPlaces.add(helpPa);
//		Place helpPc = (Place) resetnet.addArc(invi, resetnet.addPlace(in.getLabel()+"-responded existence_c-"+out.getLabel())).getTarget();
//		resetnet.addInhibitorArc(helpPc, in);
//		resetnet.addInhibitorArc(helpPc, out);		
//		inhibPlaces.add(helpPc);
		
		violCon.put(helpPa, new Pair<Transition,Transition>(in,out));
		respEx.put(new Pair<Transition,Transition>(in,out), helpPb);
		return resetnet;
	}
	
	public static Set<Transition> getTransitionsByLabel(ResetInhibitorNet petriNet, String label) {
		Set<Transition> transitions = new HashSet<Transition>();
		for (Transition t : petriNet.getTransitions()) {
			if (t.getLabel().equals(label))
				transitions.add(t);
		}
		return transitions;
	}
	
	
	private static ResetInhibitorNetImpl addCoExistence(Transition in, Transition out){
		Place helpPa = (Place) resetnet.addArc(in, resetnet.addPlace(in.getLabel()+"-co-existence_a-"+out.getLabel())).getTarget();
		Place helpPb = (Place) resetnet.addArc(out, resetnet.addPlace(in.getLabel()+"-co-existence_b-"+out.getLabel())).getTarget();
				
		Transition invi = (Transition) resetnet.addResetArc(helpPb, resetnet.addTransition("invisible_ce")).getTarget();
		invi.setInvisible(true);
		resetnet.addResetArc(helpPa, invi);
		resetnet.addArc(helpPa, invi);
		resetnet.addArc(helpPb, invi);
		
		resetnet.addInhibitorArc(helpPa, end);
		resetnet.addInhibitorArc(helpPb, end);

		inhibPlaces.add(helpPa);
		inhibPlaces.add(helpPb);
		coexPlaces.add(helpPa);
		coexPlaces.add(helpPb);
		
		violCon.put(helpPa, new Pair<Transition,Transition>(in,out));
		violCon.put(helpPb, new Pair<Transition,Transition>(in,out));
		return resetnet;
	}
	
	
}


