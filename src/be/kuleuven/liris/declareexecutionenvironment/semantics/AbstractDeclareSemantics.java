package be.kuleuven.liris.declareexecutionenvironment.semantics;

/** Author: Johannes De Smedt
 *  E-mail: johannes.desmedt@kuleuven.be
 *  October 2015
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import org.processmining.framework.util.Pair;
import org.processmining.models.graphbased.directed.DirectedGraphNode;
import org.processmining.models.graphbased.directed.petrinet.PetrinetEdge;
import org.processmining.models.graphbased.directed.petrinet.PetrinetNode;
import org.processmining.models.graphbased.directed.petrinet.ResetInhibitorNet;
import org.processmining.models.graphbased.directed.petrinet.ResetNet;
import org.processmining.models.graphbased.directed.petrinet.elements.Arc;
import org.processmining.models.graphbased.directed.petrinet.elements.InhibitorArc;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.ResetArc;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.semantics.IllegalTransitionException;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.decmod2rinet.DependencyStructure;
import org.processmining.plugins.decmod2rinet.PetrinetBuilder;

abstract class AbstractDeclareSemantics {

	protected Marking state;
	private Collection<Transition> transitions;
	private HashSet<DependencyStructure> dpS;
	private HashSet<Transition> permDisabled;
	
	private static ResetInhibitorNet riNet;
	private static Transition end;
	private static HashMap<Place,Integer> existenceP;
	private HashSet<Place> absenceP;
	private static HashSet<Place> exactlyP;
	private static HashMap<Transition,Place> exactlyAbsenceTransitions;
	private static HashMap<Transition,Place> exactlyExistenceTransitions;
	private static HashSet<Pair<Transition,Transition>> alternates;
	private static HashMap<Transition,Pair<Place,Transition>> chainRespPlaces;
	private static HashSet<Place> altPrecPlaces;
	private static HashSet<Place> chainPrecPlaces;
	private static HashMap<Pair<Transition,Transition>,Place> chainPrecTrans;
	private static HashMap<Pair<Transition,Transition>,Place> chainRespTrans;
	private static HashMap<Pair<Transition,Transition>,Pair<Place,Place>> notCoExTrans;
	private HashMap<DirectedGraphNode, String> nodeLabels;
	private HashSet<String> ex;
	private HashSet<String> exPerms;
	private HashSet<Place> exPlaces;
	private HashMap<Place,Pair<Transition,Transition>> choiceP;
	private boolean accepting;
	private HashMap<Transition, String> transitionExplanations;

	
	public static HashSet<Transition> altTransBegin;
	public static HashSet<Transition> altTransEnd;
	public static HashMap<Transition, HashMap<Integer,HashSet<Transition>>> altGraph;
	private HashSet<Transition> searched;
	private HashMap<Transition,Integer> noOcAlt;
	private HashMap<Transition,Integer> maxOc;
	
	public AbstractDeclareSemantics() {
		this(null);
	}

	public AbstractDeclareSemantics(Marking state) {
		this.state = state;
	}

	public void initialize(Collection<Transition> transitions, Marking state, final PetrinetBuilder pn) {
		this.transitions = transitions;
		this.permDisabled = new HashSet<Transition>();
		transitionExplanations = new HashMap<Transition, String>();
		setCurrentState(state);
		
		this.dpS = new HashSet<DependencyStructure>();
		for(DependencyStructure d: pn.dpS)
			dpS.add(d.clone(d));
				
		this.riNet = pn.resetnet;
		this.existenceP = pn.existencePlaces;
		this.exactlyP = pn.exactlyPlaces;
		this.alternates = pn.alternates;
		this.exactlyAbsenceTransitions = pn.exactlyAbsenceTransitions;
		this.exactlyExistenceTransitions = pn.exactlyExistenceTransitions;
		this.altPrecPlaces = pn.alternatePrecPlaces;
		this.chainRespPlaces = pn.chainRespPlaces;
		this.chainPrecPlaces = pn.chainPrecPlaces;
		this.end = pn.end;
		this.notCoExTrans = pn.notCoExTrans;
		this.choiceP = pn.choiceP;
		this.absenceP = pn.absencePlaces;
		this.accepting = false;
		searched = new HashSet<Transition>();
		noOcAlt = new HashMap<Transition,Integer>();
		maxOc = new HashMap<Transition,Integer>();
		
		this.altTransBegin = pn.altTransBegin;
		this.altTransEnd = pn.altTransEnd;
		this.altGraph = pn.altGraph;
		this.chainPrecTrans = pn.chainPrecTrans;
		this.chainRespTrans = pn.chainRespTrans;
		
		ex = new HashSet<String>();
		exPerms = new HashSet<String>();
		exPlaces = new HashSet<Place>();
		nodeLabels = pn.nodeLabels;
	}

	protected Collection<Transition> getTransitions() {
		return Collections.unmodifiableCollection(transitions);
	}

	protected boolean isEnabled(Marking state, Marking required, Transition t) {
		if (required.isLessOrEqual(state)) {
			for (PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> e : t.getGraph().getInEdges(t)) {
				if (e instanceof InhibitorArc) {
					InhibitorArc arc = (InhibitorArc) e;
					if (state.occurrences(arc.getSource()) > 0) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	public Marking getCurrentState() {
		return state;
	}
		
	public HashSet<String> getExplanations(){
		return ex;
	}

	public HashSet<Place> getExPlaces(){
		return exPlaces;
	}
	
	public boolean isAccepting(){
		return accepting;
	}

	public void setCurrentState(Marking currentState) {
		state = currentState;
	}
	
	public /*PetrinetExecutionInformation*/String executeExecutableTransition(Transition toExecute) throws IllegalTransitionException {
	
		if(!this.getExecutableTransitions().contains(toExecute)){
			String explanation = "";
			if(transitionExplanations.get(toExecute)!=null){
				explanation = transitionExplanations.get(toExecute);
			}else{
				for(PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> e: riNet.getInEdges(toExecute)){
					if(!getCurrentState().contains((Place) e.getSource()) && e instanceof Arc)
						explanation = explanation+"\n--"+nodeLabels.get(toExecute)+" is disabled because of "+nodeLabels.get(e.getSource());
					else if(getCurrentState().contains((Place) e.getSource()) && e instanceof InhibitorArc)
						explanation = explanation+"\n--"+nodeLabels.get(toExecute)+" is disabled because of "+nodeLabels.get(e.getSource());
				}
			}
			return explanation;//throw new IllegalTransitionException(toExecute, newState);
		}
		
		for(Transition t: riNet.getTransitions()){
			for(Pair<Transition,Transition> atp: alternates){
				if(atp.getSecond().equals(t)){
					System.out.println("\n\nChecking alternates with pair "+atp.toString());
					for(Pair<Transition,Transition> atpI: alternates){
						if(!atpI.getFirst().equals(t))
							System.out.println(t.toString()+" is beginning of alternate chain");
					}
				}
			}
		}
				
		Marking required = getRequired(toExecute);
		Marking newState = new Marking(state);
		if (!isEnabled(state, required, toExecute)) {
			throw new IllegalTransitionException(toExecute, newState);
		}
		Marking produced = getProduced(toExecute);
		newState.addAll(produced);
		Marking toRemove = getRemoved(toExecute);
		newState.removeAll(toRemove);
		state = newState;
		
		//////////////////
		//Declare addition
		//// Permanently disable dependent transitions
		HashSet<DependencyStructure> tR = new HashSet<DependencyStructure>();
		for(DependencyStructure d: dpS){
			if(!d.isEx()){
				if(!d.isUn()){
					if(toExecute.equals(d.getT())){
						for(Place p: d.getPlaces())
							if(!(d.getTforP(p)==null)){
								permDisabled.add(d.getTforP(p));	
								exPlaces.add(p);
								exPerms.add(nodeLabels.get(d.getTforP(p))+" is permanently disabled because of "+nodeLabels.get(d.getP()));
								transitionExplanations.put(d.getTforP(p), "\n--"+nodeLabels.get(d.getTforP(p))+" is permanently disabled because of "+nodeLabels.get(d.getP()));
							}
						disableLowerLevel(d);
						tR.add(d);
					}
				}
				else{
					if(getCurrentState().occurrences(d.getUP())==0){
						if(toExecute.equals(d.getT())){
							for(Place p: d.getPlaces()){
								if(!(d.getTforP(p)==null)){
									permDisabled.add(d.getTforP(p));	
									exPlaces.add(p);
									exPerms.add(nodeLabels.get(d.getTforP(p))+" is permanently disabled because of "+nodeLabels.get(d.getP()));
									transitionExplanations.put(d.getTforP(p), "\n--"+nodeLabels.get(d.getTforP(p))+" is permanently disabled because of "+nodeLabels.get(d.getP()));
								}
							}
							disableLowerLevel(d);
							tR.add(d);
						}
					}
				}
			}
			HashSet<DependencyStructure> gR = new HashSet<DependencyStructure>();
			for(DependencyStructure s: d.getDP()){
				if(s.getDir()){
					if(getCurrentState().contains(s.getP())){
						System.out.println("Removing "+s.toString());
						gR.add(s);
					}
				}
				gR = testLowerDPs(s,gR);
			}			
			HashSet<DependencyStructure> a = d.getDP();
			//a.removeAll(gR);
			d.setDP(a);
		}
		dpS.removeAll(tR);
		
		if(toExecute.equals(end))
			permDisabled.addAll(transitions);
				
		return null;//new PetrinetExecutionInformation(required, toRemove, produced, toExecute);
	}
	
	public HashSet<DependencyStructure> testLowerDPs(DependencyStructure s, HashSet<DependencyStructure> tR){
		for(DependencyStructure l: s.getDP()){
			if(l.getDir())
				if(getCurrentState().contains(l.getP()))
					tR.add(l);
			tR = testLowerDPs(l,tR);
		}	
		return tR;
	}
	

	public Collection<Transition> getExecutableTransitions() {
		ex = new HashSet<String>();
		ex.addAll(exPerms);
		exPlaces = new HashSet<Place>();
		if (state == null) {
			return null;
		}
		// the tokens are divided over the places according to state
		ArrayList<Transition> enabled = new ArrayList<Transition>();
		for (Transition trans : getTransitions()) {
			if (isEnabled(state, getRequired(trans), trans)) {
				enabled.add(trans);
			}
		}
		
			
		//////////////////
		//Declare addition		
		Collection<Transition> tR = new HashSet<Transition>();
		for(Transition t: enabled)
			tR.addAll(calculateHiddenDependencies(t));
				
		/** Remove alternates from execution set **/
		enabled = disableAlternates(enabled);

		noOcAlt = new HashMap<Transition,Integer>();
		
		//Lower bound
		for(Transition t: altTransBegin){
			searched.clear();
			searched.add(end);
			
			int nSTF = calculateNoOfOcurrence(noOcAlt, t);
			noOcAlt.put(t, nSTF);
			searched.add(t);
			noOcAlt = markMinAlternates(noOcAlt, t);			
			//System.out.println("Map: "+noOcAlt.toString());		
		}	
		for(Transition t: altTransEnd){
			searched.clear();
			searched.add(end);
			
			int nSTF = calculateNoOfOcurrence(noOcAlt, t);
			noOcAlt.put(t, nSTF);
			searched.add(t);
			noOcAlt = markMinAlternates(noOcAlt, t);	
			//System.out.println("Map: "+noOcAlt.toString());		
		}	
		System.out.println("!!!!Lower bound map: "+noOcAlt.toString());		
		
		
		//Upper bound
		for(Transition t: maxOc.keySet()){
			//System.out.println("Searching the max oc for "+t.getLabel()+" max: "+maxOc.toString());
			searched.clear();
			searched.add(end);
			
			searched.add(t);
			maxOc = markMaxOccurrence(maxOc, t);			
		}	
		for(Transition t: maxOc.keySet())
			if(maxOc.get(t)<1){
				enabled.remove(t);	
				System.out.println("What am I using this for!?? Removing "+t.toString());
			}

		System.out.println("!!!!Upper bound map: "+maxOc.toString());	
		
		
		/**
		 * 
		 */
		/** Remove chains from execution set **/
		for(Transition t: enabled)
			if(chainRespPlaces.containsKey(t)){
				System.out.println("T is "+t);
				boolean check = false;
				System.out.println("Checking it for "+chainRespPlaces.get(t).getSecond().toString());
				for(PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> e: riNet.getInEdges(chainRespPlaces.get(t).getSecond())){
					if(e instanceof Arc && !chainPrecPlaces.contains(e.getSource())){
						if(!(getCurrentState().contains(e.getSource()))){
							check = true;
							System.out.println("Because of "+e.toString());
							break;
						}
					}else if(e instanceof InhibitorArc && !chainPrecPlaces.contains(e.getSource())){
						if(riNet.getResetArc((Place) e.getSource(), t) == null){
							System.out.println("There exists a connection");
							if((getCurrentState().contains(e.getSource()))){
								check = true;
								System.out.println("Because of "+e.toString());
								break;
							}
						}
					}
				}
				System.out.println("Verdict is "+check);
				if((!enabled.contains(chainRespPlaces.get(t).getSecond())) && check){
					tR.add(t);
					ex.add(nodeLabels.get(t)+" can't fire because "+nodeLabels.get(chainRespPlaces.get(t).getSecond())+" can't fire immediately after it");
				}
			}
			
		
		/** Add explanation for exclusive choice **/
		for(Pair<Transition,Transition> nceInfo: notCoExTrans.keySet())
			if(getCurrentState().contains(notCoExTrans.get(nceInfo).getFirst())){
				exPerms.add(nceInfo.getFirst().getLabel()+" or a dependent activity has fired and excluded "+nceInfo.getSecond().getLabel()+" and its dependent activities");
				transitionExplanations.put(nceInfo.getFirst(), "\n--"+nceInfo.getSecond().getLabel()+" or a dependent activity has fired and excluded "+nceInfo.getFirst().getLabel()+" and its dependent activities");
			}else if(getCurrentState().contains(notCoExTrans.get(nceInfo).getSecond())){
				exPerms.add(nceInfo.getSecond().getLabel()+" or a dependent activity has fired and excluded "+nceInfo.getFirst().getLabel()+" and its dependent activities");
				transitionExplanations.put(nceInfo.getFirst(), "\n--"+nceInfo.getSecond().getLabel()+" or a dependent activity has fired and excluded "+nceInfo.getFirst().getLabel()+" and its dependent activities");
			}
		
				
		enabled.removeAll(tR);
		enabled.removeAll(permDisabled);					
		
		if(enabled.contains(end))
			accepting=true;
		else
			accepting=false;
		
		return enabled;
	}
	
	private HashMap<Transition, Integer> markMinAlternates(HashMap<Transition, Integer> map, Transition t){		
		for(Transition tI: altGraph.get(t).get(1)){
			if(!searched.contains(tI)){
				searched.add(tI);						
				int no = calculateNoOfOcurrence(map, tI);
				int filled=0;
				String s = checkRelationType(tI,t).getFirst();
				Place p = checkRelationType(tI,t).getSecond();
				if(getCurrentState().contains(p))
					filled++;
				//System.out.println("Searching in for "+t.getLabel()+" and "+tI.getLabel()+" which is "+nodeLabels.get(p)+" filled "+ filled);
				switch (s){
				case "prec":
					if(map.get(t)>no)
						map.put(tI, (1-filled));
					else
						map.put(tI, no);
					break;
				case "resp":
					if(no>0)
						map.put(t, Math.max(map.get(t),1));
					map.put(tI, no);
					break;
				case "altprec":
					if(map.get(t)>no)
						map.put(tI, map.get(t)-filled);
					else
						map.put(tI, no);
					break;
				case "altres":
					if(map.get(t)<no+filled)
						map.put(t, no+filled);
					map.put(t, Math.max(map.get(t), filled));
					map.put(tI, no);
					break;
				}					
				//System.out.println("Map is now "+noOcAlt.toString());
				map = markMinAlternates(map, tI);
			}
		}
		for(Transition tO: altGraph.get(t).get(2)){
			if(!searched.contains(tO)){
				searched.add(tO);
				int no = calculateNoOfOcurrence(map, tO);	
				int filled=0;
				String s = checkRelationType(t,tO).getFirst();
				Place p = checkRelationType(t,tO).getSecond();
				if(getCurrentState().contains(p))
					filled++;
				System.out.println("Searching in for "+t.getLabel()+" and "+tO.getLabel()+" which is "+nodeLabels.get(p)+" filled "+filled+" and no is "+no);
				System.out.println("Map is "+noOcAlt.toString());
				switch (s){
				case "prec":
					if(map.get(t)<no)
						map.put(t, 1-filled);
					map.put(tO, no);
					break;
				case "resp":
					if(map.get(t)>0 || filled>0)	
						map.put(tO, Math.max(1,no));
					else
						map.put(tO, no);
					break;
				case "altprec":
					if(map.get(t)+filled<no)
						map.put(t, no-filled);
					map.put(tO,no);
					break;
				case "altres":
					if(map.get(t)>no)	
						map.put(tO, map.get(t));
					else
						map.put(tO, no);
					break;
				}
				//System.out.println("Map is now "+noOcAlt.toString());
				map = markMinAlternates(map, tO);
			}
		}
		return map;
	}

	private HashMap<Transition, Integer> markMaxOccurrence(HashMap<Transition, Integer> map, Transition t){		
		for(Transition tI: altGraph.get(t).get(1)){
			if(!searched.contains(tI)){
				searched.add(tI);				

				int filled=0;
				String s = checkRelationType(tI,t).getFirst();
				Place p = checkRelationType(tI,t).getSecond();
				if(getCurrentState().contains(p))
					filled++;
				
				switch (s){
				case "resp":
					if(map.containsKey(t)){
						map.put(tI, Math.min(map.get(t), 1));
					}
					break;
				case "altres":
					if(map.containsKey(t)){
						map.put(tI, Math.max(map.get(t)-filled, noOcAlt.get(tI)));
						if(map.get(t)-filled<1)
							ex.add(tI.getLabel()+" cannot fire because "+t.getLabel()+" can fire only one time anymore and "+nodeLabels.get(p)+" is already activated");
					}
					break;
				}								
				map = markMaxOccurrence(map, tI);
			}
		}
		for(Transition tO: altGraph.get(t).get(2)){
			if(!searched.contains(tO)){
				searched.add(tO);
				//System.out.println("Searching in for "+t.getLabel()+" and "+tO.getLabel()+" which is "+nodeLabels.get(getAltPlace(t,tO)));
				int no = calculateNoOfOcurrence(map, tO);	
				int filled=0;
				String s = checkRelationType(t,tO).getFirst();
				Place p = checkRelationType(t,tO).getSecond();
				if(getCurrentState().contains(p))
					filled++;
				switch (s){
				case "altprec":
					if(map.containsKey(t))
						map.put(tO, Math.max(map.get(t)+filled,noOcAlt.get(tO)));
						if(map.get(t)+filled<1)
							ex.add(tO.getLabel()+" cannot fire because "+t.getLabel()+" cannot fire anymore");
					break;
				}
				map = markMaxOccurrence(map, tO);
			}
		}
		return map;
	}
	
	private Pair<String,Place> checkRelationType(Transition t, Transition t2){
		String s = "";
		Place p = null;
		for(PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> e: riNet.getInEdges(t2)){
			for(PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> e2: riNet.getInEdges(e.getSource())){
				if(e2.getSource().equals(t)){
					p = (Place) e.getSource();					
					if(nodeLabels.get(p).contains("precedence") && !nodeLabels.get(p).contains("alternate")){
						s="prec";
					}					
					if(nodeLabels.get(p).contains("response") && !nodeLabels.get(p).contains("alternate")){
						s="resp";
					}	
					if(nodeLabels.get(p).contains("precedence") && nodeLabels.get(p).contains("alternate")){
						s="altprec";
					}	
					if(nodeLabels.get(p).contains("response") && nodeLabels.get(p).contains("alternate")){
						s="altres";
					}	
					break;
				}
			}
		}
		for(Pair<Transition,Transition> transPair: chainRespTrans.keySet()){
			if(transPair.getFirst().equals(t) && transPair.getSecond().equals(t2)){
				s="chainresp";
				p=chainRespTrans.get(transPair);
			}
		}
		for(Pair<Transition,Transition> transPair: chainPrecTrans.keySet()){
			if(transPair.getFirst().equals(t) && transPair.getSecond().equals(t2)){
				s="chainprec";
				p=chainPrecTrans.get(transPair);
			}
		}
		return new Pair<String,Place>(s,p);
	}
	
	private Integer calculateNoOfOcurrence(HashMap<Transition,Integer> noOcAlt, Transition t){
		int nSTF = 0;
		if(noOcAlt.containsKey(t))
			nSTF = noOcAlt.get(t);
		else{
			for(PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> e: riNet.getInEdges(t))
				if(exactlyP.contains(e.getSource()))
					nSTF = getCurrentState().occurrences(e.getSource());
			for(PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> e: riNet.getOutEdges(t))
				if(existenceP.containsKey(e.getTarget()))
					nSTF = existenceP.get(e.getTarget()) - getCurrentState().occurrences(e.getTarget());
		}
		return Math.max(0, nSTF);
	}
	
	
	
	private ArrayList<Transition> disableAlternates(ArrayList<Transition> tR){
		for(Pair<Transition,Transition> pT: alternates){
			if(exactlyExistenceTransitions.containsKey(pT.getSecond())){	
				Place p2 = exactlyExistenceTransitions.get(pT.getSecond());
				if(exactlyAbsenceTransitions.containsKey(pT.getFirst())){	
					if(existenceP.containsKey(p2)){								
						if(getCurrentState().occurrences(exactlyAbsenceTransitions.get(pT.getFirst()))
								< (existenceP.get(p2)-getCurrentState().occurrences(p2)) && getCurrentState().occurrences(exactlyAbsenceTransitions.get(pT.getFirst()))>0)								
							{
							tR.remove(pT.getFirst());
							//System.out.println("Your peer "+pT.getSecond().getLabel()+" has " +getCurrentState().occurrences(exactlyExistenceTransitions.get(pT.getSecond()))+" tokens ");
							ex.add(pT.getFirst().getLabel()+" is disabled because it can only fire "+getCurrentState().occurrences(exactlyAbsenceTransitions.get(pT.getFirst()))
								+" time(s) anymore, while "+ nodeLabels.get(pT.getSecond())+" still has to fire "+(existenceP.get(p2)-getCurrentState().occurrences(p2))+" time(s)");
							exPlaces.add(p2);
							transitionExplanations.put(pT.getFirst(), "\n--"+pT.getFirst().getLabel()+" is disabled because it can only fire "+getCurrentState().occurrences(exactlyAbsenceTransitions.get(pT.getFirst()))
								+" time(s) anymore, while "+ nodeLabels.get(pT.getSecond())+" still has to fire "+(existenceP.get(p2)-getCurrentState().occurrences(p2))+" time(s)");
							}
					}else{ 
						if(getCurrentState().occurrences(exactlyAbsenceTransitions.get(pT.getFirst()))
								<= getCurrentState().occurrences(exactlyExistenceTransitions.get(pT.getSecond())) && getCurrentState().occurrences(exactlyAbsenceTransitions.get(pT.getFirst()))>0){
							tR.remove(pT.getFirst());
							ex.add(pT.getFirst().getLabel()+" is disabled because it can only fire "+getCurrentState().occurrences(exactlyAbsenceTransitions.get(pT.getFirst()))
									+" time(s) anymore, while "+ nodeLabels.get(pT.getSecond())+" still has to fire "+getCurrentState().occurrences(exactlyExistenceTransitions.get(pT.getSecond()))+" time(s)");
							exPlaces.add(p2);
							transitionExplanations.put(pT.getFirst(), "\n--"+pT.getFirst().getLabel()+" is disabled because it can only fire "+getCurrentState().occurrences(exactlyAbsenceTransitions.get(pT.getFirst()))
									+" time(s) anymore, while "+ nodeLabels.get(pT.getSecond())+" still has to fire "+getCurrentState().occurrences(exactlyExistenceTransitions.get(pT.getSecond()))+" time(s)");
						}
					}
				}
			}				
		}
		return tR;
	}
	
	
	private void disableLowerLevel(DependencyStructure dp){
		HashSet<DependencyStructure> tA  = new HashSet<DependencyStructure>();
		for(DependencyStructure d: dp.getDP()){
			if(!getCurrentState().contains(d.getP())){
				for(Place p: d.getPlaces())
					if(!(d.getTforP(p)==null)){
						
						permDisabled.add(d.getTforP(p));
						exPlaces.add(p);
						ex.add(nodeLabels.get(d.getTforP(p))+" is permanently disabled because of "+nodeLabels.get(d.getP()));
						transitionExplanations.put(d.getTforP(p), "\n--"+nodeLabels.get(d.getTforP(p))+" is permanently disabled because of "+nodeLabels.get(d.getP()));
					}
				disableLowerLevel(d);
			}
			else{
				if(getCurrentState().contains(d.getP()))
					maxOc.put(d.getTforP(d.getP()), 1);
				else
					maxOc.put(d.getTforP(d.getP()), 0);
				if(altPrecPlaces.contains(d.getP())){
					d.setType("un");
					d.setT(d.getTforP(d.getP()));
					tA.add(d);
					d.setUP(d.getP());
				}
			}
		}
		dpS.addAll(tA);
	}
	
	
	private HashSet<Transition> calculateHiddenDependencies(Transition t){
		HashSet<Transition> tR = new HashSet<Transition>();				
		for(DependencyStructure d: dpS){
			if(t.equals(d.getT())){
				for(Place p: d.getPlaces()){					
					if(!existenceP.containsKey(p)){
						if(d.isNS() && getCurrentState().contains(p)){
							tR = checkForInnerViolations(p,t,tR,d,false);
						}else if(d.isUn()){
							if(getCurrentState().occurrences(d.getUP())==1 && getCurrentState().contains(p)){	
								boolean resolver = false;
								for(PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> e: riNet.getOutEdges(p))
									if(e.getTarget().equals(t)){
										resolver = true;
										break;
									}
								if(!resolver){
									tR = checkForInnerViolations(p,t,tR,d,true);
								}
							}
						}
					}else if(existenceP.containsKey(p) && !d.getTforP(p).equals(t)){	
						if(d.isUn()){
							if(getCurrentState().occurrences(d.getUP())==1){
								if(getCurrentState().occurrences(p)<existenceP.get(p)){
									ex.add(t.getLabel()+ " is disabled because it can fire only one time anymore and "+nodeLabels.get(p)+" is not fulfilled yet");
									tR.add(t);
									exPlaces.add(p);
								}
							}
						}else{
							if(getCurrentState().occurrences(p)<existenceP.get(p)){
								ex.add(t.getLabel()+ " is disabled because "+" "+nodeLabels.get(p)+" is not fulfilled yet");
								tR.add(t);
								exPlaces.add(p);
							}
						}
					}
			}	
			tR.addAll(getLowerLevel(d, d.getT(),d));				
			}
		}
		return tR;
	}
	
	private HashSet<Transition> checkForInnerViolations(Place p, Transition t, HashSet<Transition> tR, DependencyStructure d, boolean unary){
		if(choiceP.containsKey(p)){
			if(getCurrentState().contains(p)){
				if(choiceP.get(p).getFirst().equals(d.getTforP(p))){
					if(permDisabled.contains(choiceP.get(p).getSecond())){
						tR.add(t);
						if(unary)
							ex.add(t.getLabel()+ " is disabled because it can fire only one time anymore and "+nodeLabels.get(p)+" is not fulfilled yet");
						else
							ex.add(t.getLabel()+ " is disabled because "+" "+nodeLabels.get(p)+" is not fulfilled yet");
						exPlaces.add(p);
					}
				}else{
					if(permDisabled.contains(choiceP.get(p).getFirst())){
						tR.add(t);
						if(unary)
							ex.add(t.getLabel()+ " is disabled because it can fire only one time anymore and "+nodeLabels.get(p)+" is not fulfilled yet");
						else
							ex.add(t.getLabel()+ " is disabled because "+" "+nodeLabels.get(p)+" is not fulfilled yet");
						exPlaces.add(p);
					}
				}
			}
		}else{
			tR.add(t);
			ex.add(t.getLabel()+ " is disabled because it can fire only one time anymore and "+nodeLabels.get(p)+" is not fulfilled yet");
			exPlaces.add(p);
		}		
		System.out.println("tR after searching "+t.getLabel()+" and "+nodeLabels.get(p)+" is "+tR.toString());
		return tR;
	}

	
	private HashSet<Transition> getLowerLevel(DependencyStructure d, Transition t, DependencyStructure origD){
		HashSet<Transition> tR = new HashSet<Transition>();
		for(DependencyStructure dL: d.getDP()){		
			if(!getCurrentState().contains(dL.getP()) && !noOcAlt.containsKey(dL.getT())){
				checkForInnerViolationsLower(t,tR,origD,dL);
				tR.addAll(getLowerLevel(dL, t, d));
			}			
			if(noOcAlt.containsKey(dL.getT()) && !dL.getDir()){
				System.out.println("Error for "+nodeLabels.get(dL.getP())+" and "+t.toString());
				System.out.println("Testing "+dL.getTforP(dL.getP()));
				if(noOcAlt.containsKey(dL.getTforP(dL.getP()))){
					if(noOcAlt.get(dL.getTforP(dL.getP()))>1){
						checkForInnerViolationsLower(t,tR,origD,dL);
						tR.addAll(getLowerLevel(dL, t, d));	
						Transition tI = dL.getT();
						for(Transition tO: altGraph.get(tI).get(2)){
							if(noOcAlt.get(tO)>noOcAlt.get(tI)){
								ex.add(nodeLabels.get(t) + " is disabled because "+nodeLabels.get(tI)+" still has to fire at least "+noOcAlt.get(tI)+" times for "+tO.getLabel()+" ("+noOcAlt.get(tO)+") ");
							}
						}
					}
					else if(noOcAlt.get(dL.getT())==1){	
						System.out.println("\nI'm here! Adding? "+getLowerLevelDependencies(dL.getTforP(dL.getP()),t));
						if(getLowerLevelDependencies(dL.getTforP(dL.getP()),t))
							tR.add(t);					
					}
				}
			}
		}	
		System.out.println("A- TR: "+tR.toString());
		return tR;
	}
	
	private boolean getLowerLevelDependencies(Transition t, Transition mainTrans){
		boolean violation=false;
		for(Transition tI: altGraph.get(t).get(1)){
			if(noOcAlt.get(tI)>0){
				System.out.println("Checking "+t.getLabel()+" and "+mainTrans.getLabel());
				if(!mainTrans.equals(tI) && !t.equals(tI)){
					violation=true;
					Place p=checkRelationType(tI,t).getSecond();
					if(getCurrentState().contains(p)){
						ex.add(nodeLabels.get(mainTrans)+" is disabled because "+nodeLabels.get(tI)+" still has to fire for "+nodeLabels.get(p));
					}else{
						ex.add(nodeLabels.get(mainTrans)+" is disabled because "+nodeLabels.get(tI)+" still has to fire "+noOcAlt.get(tI)+" time(s)");
					}
					getLowerLevelDependencies(tI, mainTrans);
				}
			}
		}
		return violation;
	}
	
	private HashSet<Transition> checkForInnerViolationsLower(Transition t, HashSet<Transition> tR, DependencyStructure origD, DependencyStructure dL){
		for(Place p: dL.getPlaces()){
			if(origD.isNS() && (getCurrentState().contains(p))){   
				if(checkPlaceForViolation(p)){
					tR.add(t);
					ex.add(nodeLabels.get(t) + " is disabled because "+" "+dL.getTforP(dL.getP())+" cannot ensure resolving "+nodeLabels.get(p)+" yet due to "+nodeLabels.get(dL.getP()));
					exPlaces.add(p);
				}
			}else if(origD.isUn()){
				for(Place pp: origD.getPlaces())
					System.out.println("PP: "+pp.getLabel());
				if(getCurrentState().occurrences(origD.getUP())==1 
						&& getCurrentState().contains(p) && !existenceP.containsKey(p) 
						&& !altPrecPlaces.contains(p)){	
					boolean resolver = false;
					for(PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> e: riNet.getOutEdges(p))
						if(e.getTarget().equals(t)){
							resolver = true;
							break;
						}
					if(!resolver && checkPlaceForViolation(p)){
						tR.add(t);
						ex.add(nodeLabels.get(t) + " is disabled because "+ " "+dL.getT()+" cannot ensure resolving "+nodeLabels.get(p)+" yet due to "+nodeLabels.get(dL.getP()));
						exPlaces.add(p);
					}
				}
			}
		}
		return tR;
	}
	
	protected boolean checkPlaceForViolation(Place p){
		boolean result=true;
		if(existenceP.containsKey(p)){
			if(getCurrentState().occurrences(p)<existenceP.get(p))
				result= true;
			else
				result= false;
		}else if(exactlyP.contains(p)){
			if(getCurrentState().occurrences(p)==existenceP.get(p))
				result= true;
			else
				result= false;
		}
		return result;
	}
		

	protected Marking getRequired(Transition trans) {
		Collection<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> edges = trans.getGraph().getInEdges(
				trans);
		Marking required = new Marking();
		for (PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> e : edges) {
			if (e instanceof Arc) {
				Arc arc = (Arc) e;
				required.add((Place) arc.getSource(), arc.getWeight());
			}
		}
		return required;

	}

	protected Marking getProduced(Transition trans) {
		Collection<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> edges = trans.getGraph().getOutEdges(
				trans);
		Marking produced = new Marking();
		for (PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> e : edges) {
			if (e instanceof Arc) {
				Arc arc = (Arc) e;
				produced.add((Place) arc.getTarget(), arc.getWeight());
			}
		}

		return produced;

	}

	protected Marking getRemoved(Transition trans) {
		Collection<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> edges = trans.getGraph().getInEdges(
				trans);
		Marking removed = new Marking(getRequired(trans));
		for (PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> e : edges) {
			if (e instanceof ResetArc) {
				ResetArc arc = (ResetArc) e;
				removed.add(arc.getSource(), state.occurrences(arc.getSource()));
			}
		}
		return removed;
	}

	public String toString() {
		return "Declare R/I Semantics";
	}
	
	public HashSet<DependencyStructure> getDPs(){
		return dpS;
	}

	public int hashCode() {
		return getClass().hashCode();
	}

	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		return this.getClass().equals(o.getClass());
	}
}
