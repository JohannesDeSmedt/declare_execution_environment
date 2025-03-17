package be.kuleuven.liris.declareexecutionenvironment.petrinet;

/** Author: Johannes De Smedt
 *  E-mail: johannes.desmedt@kuleuven.be
 *  October 2015
 */

import java.util.HashMap;
import java.util.HashSet;
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
import org.processmining.plugins.declare.visualizing.ActivityDefinition;
import org.processmining.plugins.declare.visualizing.AssignmentModel;
import org.processmining.plugins.declare.visualizing.ConstraintDefinition;
import org.processmining.plugins.declareminer.visualizing.DeclareMap;


public class PetrinetConstructBuilder{

	public static Transition end;
	public static Place startP;
	public static Place endP;

	public static HashSet<Transition> visitedL;
	public static HashSet<Transition> visitedR;
	public static HashMap<Transition,HashSet<Place>> transDep; 
	public static HashSet<Place> depPlaces;
	public static HashMap<Transition,HashSet<Place>> transInDep;
	public static HashMap<Transition,HashSet<Place>> transOutDep;	
	public static Marking iniM;
	public static HashMap<ConstraintDefinition, Pair<HashSet<DirectedGraphNode>,HashSet<DirectedGraphEdge<?,?>>>> constraintConstructs;
	public static HashSet<DirectedGraphNode> nodes;
	public static HashSet<DirectedGraphEdge<?,?>> edges;
	public static ResetInhibitorNetImpl resetnet;
	public static DeclareMap map;
	public static HashMap<Pair<Transition,Transition>,Pair<Place,Place>> notCoExTrans;
	public static HashMap<Place, Pair<Transition,Transition>> violCon;
	public static int exclusiveChoiceCount;
	public static HashMap<Place,Pair<Transition,Transition>> choiceP;
	public static HashSet<PetrinetEdge<?,?>> toAvoid;
	public static HashMap<DirectedGraphNode,String> nodeLabels;
	public static HashMap<String,String> descriptions;
	
	public static HashSet<Transition> altTransBegin = new HashSet<Transition>();
	public static HashSet<Transition> altTransEnd = new HashSet<Transition>();
	public static HashMap<Transition, HashMap<Integer,HashSet<Transition>>> altGraph = new HashMap<Transition, HashMap<Integer,HashSet<Transition>>>();
	
	public PetrinetConstructBuilder(AssignmentModel model){
		//DeclareMap decMap = new DeclareMap(decMapIn.getModel(),decMapIn.getModelCh(),decMapIn.getView(),decMapIn.getViewCh(),decMapIn.getBroker(),decMapIn.getBrokerCh());
		HashSet<String> alphabet = new HashSet<String>();
		
		resetnet = new ResetInhibitorNetImpl("Declare reset net");		
		descriptions = new HashMap<String,String>();		
		iniM = new Marking();
		
		end = resetnet.addTransition("END");		
		endP = (Place) resetnet.addArc(end, resetnet.addPlace("end_p")).getTarget();

		/*** Create alphabet ***/
		for(ActivityDefinition aD: model.getActivityDefinitions()){
			System.out.println(aD.toString());
			alphabet.add(aD.toString());
		}			
		
		for(String s: alphabet)			
			if(!s.equals(end.getLabel()))
				resetnet.addTransition(s);
		
		//Add unary constraints
	
		Integer l = 120;
		HashMap<ActivityDefinition, String> tempActMap1 = new HashMap<ActivityDefinition, String>();
		HashMap<String,ActivityDefinition> tempActMap2 = new HashMap<String,ActivityDefinition>();
		for(ActivityDefinition a: model.getActivityDefinitions()){
			tempActMap1.put(a, l.toString());
			tempActMap2.put(l.toString(), a);
			l++;
		}

	}

	
	public static Pair<ResetInhibitorNetImpl,Marking> addConstraint(Transition A, Transition B, ResetInhibitorNetImpl resetnet, String c, Transition endT, Marking m){
		iniM = m;
		end = endT;
		nodes = new HashSet<DirectedGraphNode>();
		
//		switch (c.getName()){
		switch (c) {
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
//		case "not co-existence":
//			addNotCoExistence(resetnet, A, B);
//			break;	
		case "not succession":
			addNotSuccession(resetnet, A, B);
			break;
		case "not chain succession":
			addNotChainSuccession(resetnet, A, B);
			break;
		case "choice":
			addChoice(resetnet, A, B);
			break;
//		case "exclusive choice":
//			addExclusiveChoice(resetnet, A, B);
//			break;
		case "responded existence":
			addRespondedExistence(resetnet, A, B);
			break;
		case "co-existence":
			addCoExistence(A, B);
			break;	
		case "last":
			addLast(resetnet, A, 1);
			break;
		}
		
		//constraintConstructs.put(c, new Pair<HashSet<DirectedGraphNode>,HashSet<DirectedGraphEdge<?,?>>>(nodes,edges));
		//System.out.println("Nodes "+nodes.toString()+"  edges "+edges.toString());
		return new Pair<ResetInhibitorNetImpl,Marking>(resetnet,iniM);
	}
	
	public static void finishNet(ResetInhibitorNetImpl resetnet, Transition end){
		Place endPlace = resetnet.addPlace("end_place");
		resetnet.addArc(end, endPlace);
		for(Transition t: resetnet.getTransitions())
			if(!t.equals(end))
				resetnet.addInhibitorArc(endPlace, t);
	}
	
	/*** For activity that is equal to a single end event ***/
	public static void setEnd(Transition t, ResetInhibitorNetImpl resetnet){
		for(PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> e: resetnet.getInEdges(end)){
			boolean add = true;
			for(PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> ee: resetnet.getInEdges(e.getSource()))
				if(ee.getSource().equals(t)){
					add = false;
					break;
				}
			for(PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> ee: resetnet.getOutEdges(e.getSource()))
				if(ee.getTarget().equals(t)){
					add = false;
					break;
				}			
			if(add){
				if(e instanceof InhibitorArc){
					resetnet.addInhibitorArc((Place) e.getSource(), t);
				}else if(e instanceof ResetArc){
					resetnet.addResetArc((Place) e.getSource(), t);
				}else{
					resetnet.addArc((Place) e.getSource(), t);
				}
			}
		}
		for(PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> e: resetnet.getOutEdges(end))
			resetnet.addArc(t, (Place) e.getTarget());
		//resetnet.removeNode(end);
	}
	
	/*** Unary ***/	
	private static ResetInhibitorNetImpl addExistenceN(ResetInhibitorNetImpl resetnet, Transition in, Integer n){	
		Arc e = resetnet.addArc(in, resetnet.addPlace(in.getLabel()+"-existence"+n));
		Place beginP = (Place) e.getTarget();
		
		//iniM.add(beginP,n);
		
		//for(int i=0;i<n;i++){

		//Arc eL = 
	    resetnet.addArc(beginP,end,n);
		//}
		
		return resetnet;
	}
	
	private static ResetInhibitorNetImpl addInit(ResetInhibitorNetImpl resetnet, Transition in, Integer n){	
		ResetArc r1 =  resetnet.addResetArc(resetnet.addPlace(in.getLabel()+"-init"+n),in);
		Place helpP = (Place) r1.getSource();
		iniM.add(helpP);
		
		for(Transition t:resetnet.getTransitions())
			if(t!=in /*&& t!=start*/){
				//InhibitorArc i = 
				resetnet.addInhibitorArc(helpP, t);			
			}
		return resetnet;
	}
	
	private static ResetInhibitorNetImpl addLast(ResetInhibitorNetImpl resetnet, Transition in, Integer n){	
		Place helpP = (Place) resetnet.addArc(
				in, resetnet.addPlace(in.getLabel()+"-last"+n)).getTarget();
		resetnet.addArc(helpP, end);
				
//		for(PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> e: resetnet.getInEdges(end)){	
//			if (e instanceof InhibitorArc){
//				System.out.println("Place: "+((Place)e.getSource()).getLabel()+" vs "+in.getLabel()+" "+((Place)e.getSource()).getLabel().contains(in.getLabel()));
//				if(!((Place)e.getSource()).getLabel().contains(in.getLabel()))
//					resetnet.addInhibitorArc((Place) e.getSource(),in);
//			}
//		}
		
		for(Transition t:resetnet.getTransitions())
			if(t!=in && t!=end /*&& t!=start*/)
				resetnet.addResetArc(helpP, t);			
		return resetnet;
	}
		
	private static ResetInhibitorNetImpl addAbsenceN(ResetInhibitorNetImpl resetnet, Transition in, Integer n){
		Arc a = resetnet.addArc(resetnet.addPlace(in.getLabel()+"-absence"+(n)), in);
		Place beginP = (Place) a.getSource();

		iniM.add(beginP, n-1);
		return resetnet;
	}
	
	private static ResetInhibitorNetImpl addExactlyN(ResetInhibitorNetImpl resetnet, Transition in, Integer n){
		Arc a = resetnet.addArc(resetnet.addPlace(in.getLabel()+"-exactly"+n), in);
		Place beginP = (Place) a.getSource();
	
		iniM.add(beginP, n);
		resetnet.addInhibitorArc(beginP, end);
		return resetnet;
	}
	
	/*** Simple ordered ***/
	private static ResetInhibitorNetImpl addPrecedence(ResetInhibitorNetImpl resetnet, Transition in, Transition out) {
		Place helpPlace = resetnet.addPlace(in+"-precedence-"+out);
		nodes.add(helpPlace);

		resetnet.addArc(in, helpPlace);
		resetnet.addArc(helpPlace,out);
		resetnet.addArc(out, helpPlace);

		return resetnet;
	}
	
	private static ResetInhibitorNet addResponse(ResetInhibitorNet resetnet, Transition in, Transition out){
		Place helpPlace = resetnet.addPlace(in+"-response-"+out);
					
		resetnet.addArc(in, helpPlace);
		resetnet.addInhibitorArc(helpPlace, end);
		resetnet.addResetArc(helpPlace, out);
		return resetnet;
	}
	
	/*** Alternating ordered ***/
	private static ResetInhibitorNet addAlternateResponse(ResetInhibitorNet resetnet, Transition in, Transition out){
		Arc a1 = resetnet.addArc(resetnet.addPlace(in.getLabel()+"-alternate response_A-"+out.getLabel()), in);
		Place beginPi = (Place) a1.getSource();
		//ResetArc r1 = 
		resetnet.addResetArc(beginPi, in);
		ResetArc r2 = resetnet.addResetArc(resetnet.addPlace(in.getLabel()+"-alternate response_B-"+out.getLabel()), out);
		Place beginPo = (Place) r2.getSource();
		//Arc a2 = 
		resetnet.addArc(in, beginPo);
		//Arc a3 = 
		resetnet.addArc(out, beginPi);

		//InhibitorArc i1 = 
		resetnet.addInhibitorArc(beginPo, end);
		iniM.add(beginPi);
		return resetnet;
	}
	
	private static ResetInhibitorNet addAlternatePrecedence(ResetInhibitorNet resetnet, Transition in, Transition out){
		Arc a1 = resetnet.addArc(resetnet.addPlace(in.getLabel()+"-alternate precedence_a-"+out.getLabel()), out);
		Place beginPo = (Place) a1.getSource();		
		//ResetArc r1 = 
		resetnet.addResetArc(beginPo, out);
		//Arc a2 = 
		resetnet.addArc(in, beginPo);

		return resetnet;
	}
	
//	private static ResetInhibitorNet addAlternateSuccession(ResetInhibitorNet resetnet, Transition in, Transition out){
//		Arc a1 = resetnet.addArc(resetnet.addPlace(in.getLabel()+"-alternate succession_A-"+out.getLabel()), in);
//		Place beginPi = (Place) a1.getSource();
//		Arc a2 = resetnet.addArc(resetnet.addPlace(out.getLabel()+"-alternate succession_B-"+in.getLabel()), out);
//		Place beginPo = (Place) a2.getSource();
//		//Arc a3 = 
//		resetnet.addArc(in, beginPo);
//		//Arc a4 = 
//		resetnet.addArc(out, beginPi);
//		//InhibitorArc i1 = 
//		resetnet.addInhibitorArc(beginPo, end);
//
//		iniM.add(beginPi);
//		return resetnet;
//	}
		
	/*** Chain ordered ***/
	private static ResetInhibitorNet addChainResponse(ResetInhibitorNet resetnet, Transition in, Transition out, boolean succession){
		ResetArc r1 = resetnet.addResetArc(resetnet.addPlace(in.getLabel()+"-chain response-"+out.getLabel()), out);
		Place beginPo = (Place) r1.getSource();
		resetnet.addArc(in, beginPo);
		resetnet.addInhibitorArc(beginPo, in);		
		
		
		
		for(Transition t:resetnet.getTransitions())
			if(t!=in && t!=out /*&& t!=start*/){
				//InhibitorArc i = 
				resetnet.addInhibitorArc(beginPo, t);
			}
		return resetnet;
	}
	
	private static ResetInhibitorNet addChainPrecedence(ResetInhibitorNet resetnet, Transition in, Transition out){
		ResetArc r1 = resetnet.addResetArc(resetnet.addPlace(in.getLabel()+"-chain precedence-"+out.getLabel()), in); 
		Place beginPo = (Place) r1.getSource();
		resetnet.addArc(out, beginPo);
		resetnet.addInhibitorArc(beginPo, out);
	
		iniM.add(beginPo);
		
		for(Transition t:resetnet.getTransitions())
			if(t!=in && t!=out && t!=end/*&& t!=start*/){
				//Arc a = 
				resetnet.addArc(t,beginPo);
			}
		return resetnet;
	}
	
//	private static ResetInhibitorNet addChainSuccession(ResetInhibitorNet resetnet, Transition in, Transition out){		
//
//		Place beginPo = (Place) resetnet.addArc(in, resetnet.addPlace(in.getLabel()+"-chain succession_a-"+out.getLabel())).getTarget();
//		resetnet.addInhibitorArc(beginPo, in);
//		resetnet.addResetArc(beginPo, out);
//		
//		//Place endPi = (Place) resetnet.addArc(out, resetnet.addPlace(in.getLabel()+"-chain succession_b-"+out.getLabel())).getTarget();
//		Arc r = resetnet.addArc(out, resetnet.addPlace(in.getLabel()+"-chain succession_b-"+out.getLabel()));
//
//		Place endPi = (Place) r.getTarget();
//		resetnet.addInhibitorArc(endPi, out);
//		resetnet.addResetArc(endPi, in);
//
//		iniM.add(endPi);
//		
//		for(Transition t:resetnet.getTransitions())
//			if(t!=in && t!=out){
//				InhibitorArc rr = resetnet.addInhibitorArc(beginPo, t);
//
//			}
//		return resetnet;
//	}
	
//	/*** Negative ***/
//	private static ResetInhibitorNetImpl addNotCoExistence(ResetInhibitorNetImpl resetnet, Transition in, Transition out){
//		Arc a1 = resetnet.addArc(in, resetnet.addPlace(in.getLabel()+"-not co-existence_a-"+out.getLabel()));
//		Place helpPi = (Place) a1.getTarget();
//		Arc a2 = resetnet.addArc(out, resetnet.addPlace(in.getLabel()+"-not co-existence_b-"+out.getLabel()));
//		Place helpPo = (Place) a2.getTarget();
//		edges.add(resetnet.addInhibitorArc(helpPi, out));
//		edges.add(resetnet.addInhibitorArc(helpPo, in));
//		
//		nodes.add(helpPo);nodes.add(helpPi);edges.add(a1);edges.add(a2);
//		
//		
//		if(aDmg.getDepAct(in.getLabel())!=null)
//			addExEdges(in,helpPi,helpPo);		
//				
//		if(aDmg.getDepAct(out.getLabel())!=null)
//			addExEdges(out,helpPo,helpPi);
//				
//		notCoExTrans.put(new Pair<Transition,Transition>(in,out), new Pair<Place,Place>(helpPi,helpPo));
//	
//		return resetnet;
//	}
//	
//	private static void addExEdges(Transition t, Place helpPi, Place helpPo){
//		System.out.println("Testing "+t.getLabel());
//		if(aDmg.getDepAct(t.getLabel()).getFirst()!=null){
//			for(String s: aDmg.getDepAct(t.getLabel()).getFirst()){
//				//System.out.println("L1- "+s);
//				edges.add(resetnet.addArc(getTransitionsByLabel(resetnet, s).iterator().next(), helpPi));
//				addExEdges(getTransitionsByLabel(resetnet, s).iterator().next(), helpPi, helpPo);
//			}
//			for(String s: aDmg.getDepAct(t.getLabel()).getFirst()){
//				//System.out.println("L2- "+s);
//				edges.add(resetnet.addInhibitorArc(helpPo, getTransitionsByLabel(resetnet, s).iterator().next()));
//				addExEdges(getTransitionsByLabel(resetnet, s).iterator().next(), helpPi, helpPo);
//			}	
//		}
//		
//		if(aDmg.getDepAct(t.getLabel()).getSecond()!=null){
//			for(String s: aDmg.getDepAct(t.getLabel()).getSecond()){
//				//System.out.println("R1- "+s);
//				edges.add(resetnet.addArc(getTransitionsByLabel(resetnet, s).iterator().next(), helpPi));
//				addExEdges(getTransitionsByLabel(resetnet, s).iterator().next(), helpPi, helpPo);
//			}
//			for(String s: aDmg.getDepAct(t.getLabel()).getSecond()){
//				//System.out.println("R2- "+s);
//				edges.add(resetnet.addInhibitorArc(helpPo, getTransitionsByLabel(resetnet, s).iterator().next()));
//				addExEdges(getTransitionsByLabel(resetnet, s).iterator().next(), helpPi, helpPo);
//			}
//		}	
//	}

	private static ResetInhibitorNetImpl addNotSuccession(ResetInhibitorNetImpl resetnet, Transition in, Transition out){
		Arc a1 = resetnet.addArc(in, resetnet.addPlace(in.getLabel()+"-not succession-"+out.getLabel())); 
		Place helpP = (Place) a1.getTarget();
		//InhibitorArc i1 = 
		resetnet.addInhibitorArc(helpP, out);
		
		return resetnet;
	}

	private static ResetInhibitorNetImpl addNotChainSuccession(ResetInhibitorNetImpl resetnet, Transition in, Transition out){
		Place helpP = (Place) resetnet.addArc(in, resetnet.addPlace(in.getLabel()+"-not chain succession-"+out.getLabel())).getTarget();
		resetnet.addInhibitorArc(helpP, out);
		
		for(Transition t:resetnet.getTransitions())
			if(t!=in && t!=out)
				resetnet.addResetArc(helpP, t);		
		
		return resetnet;
	}
	
	/*** Choice ***/
	private static ResetInhibitorNetImpl addChoice(ResetInhibitorNetImpl resetnet, Transition in, Transition out){
		ResetArc a1 = resetnet.addResetArc(resetnet.addPlace(in.getLabel()+"-choice-"+out.getLabel()), in);
		Place helpP = (Place) a1.getSource();
		//ResetArc r = 
		resetnet.addResetArc(helpP, out);
		
		resetnet.addInhibitorArc(helpP,end);

		iniM.add(helpP);
		
		return resetnet;
	}
	
//	private static ResetInhibitorNetImpl addExclusiveChoice(ResetInhibitorNetImpl resetnet, Transition in, Transition out){
//		Arc a1 = resetnet.addArc(in, resetnet.addPlace(in.getLabel()+"-exclusive choice_a-"+out.getLabel()));
//		Place helpPi = (Place) a1.getTarget();
//		Arc a2 = resetnet.addArc(out, resetnet.addPlace(in.getLabel()+"-exclusive choice_b-"+out.getLabel()));
//		Place helpPo = (Place) a2.getTarget();
//		InhibitorArc i1 = resetnet.addInhibitorArc(helpPi, out);
//		InhibitorArc i2 = resetnet.addInhibitorArc(helpPo, in);
//		ResetArc r1 = resetnet.addResetArc(resetnet.addPlace(in.getLabel()+"-exclusive choice_c-"+out.getLabel()),in);
//		Place helpP = (Place) r1.getSource();
//		ResetArc r2 = resetnet.addResetArc(helpP, out);
//		InhibitorArc i3 = resetnet.addInhibitorArc(helpP, end);
//		
//		iniM.add(helpP);
//		
//		nodes.add(helpP);nodes.add(helpPo);nodes.add(helpPi);
//		edges.add(a1);edges.add(a2);edges.add(i1);edges.add(i2);edges.add(i3);edges.add(r1);edges.add(r2);
//		
//		int exclusiveChoiceCountIn = 1;
//		int exclusiveChoiceCountOut = 1;
//		
//		
//		if(aDmg.getDepAct(in.getLabel())!=null)
//			if(aDmg.getDepAct(in.getLabel()).getFirst()!=null)
//				addExEdges(in,helpPi,helpPo);					
//		
//		if(aDmg.getDepAct(out.getLabel())!=null)
//			addExEdges(out,helpPi,helpPo);
//			
//		System.out.println("Exclusive choice counts - in:"+exclusiveChoiceCountIn+" out "+exclusiveChoiceCountOut);
//		
//		notCoExTrans.put(new Pair<Transition,Transition>(in,out), new Pair<Place,Place>(helpPi,helpPo));
//		violCon.put(helpP, new Pair<Transition,Transition>(in,out));
//		
//		return resetnet;
//	}	

	
	/*** Binary Existence ***/
	private static ResetInhibitorNetImpl addRespondedExistence(ResetInhibitorNetImpl resetnet, Transition in, Transition out){
		Place helpPa = (Place) resetnet.addArc(in, resetnet.addPlace(in.getLabel()+"-responded existence_a-"+out.getLabel())).getTarget();
		Place helpPb = (Place) resetnet.addArc(out, resetnet.addPlace(in.getLabel()+"-responded existence_b-"+out.getLabel())).getTarget();
		resetnet.addInhibitorArc(helpPa, end);
		Transition invi = (Transition) resetnet.addArc(helpPb, resetnet.addTransition("invisible_re")).getTarget();
		invi.setInvisible(true);
		resetnet.addResetArc(helpPb, invi);
		resetnet.addResetArc(helpPa, invi);

		
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

		return resetnet;
	}
	
	
}


