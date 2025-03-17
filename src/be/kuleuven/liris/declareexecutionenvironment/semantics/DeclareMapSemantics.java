package be.kuleuven.liris.declareexecutionenvironment.semantics;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.MissingResourceException;

import org.processmining.framework.util.Pair;
import org.processmining.models.graphbased.directed.petrinet.PetrinetEdge;
import org.processmining.models.graphbased.directed.petrinet.PetrinetNode;
import org.processmining.models.graphbased.directed.petrinet.ResetInhibitorNet;
import org.processmining.models.graphbased.directed.petrinet.elements.Arc;
import org.processmining.models.graphbased.directed.petrinet.elements.InhibitorArc;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.impl.ResetInhibitorNetImpl;
import org.processmining.models.semantics.IllegalTransitionException;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.models.semantics.petrinet.impl.PetrinetSemanticsFactory;

import be.kuleuven.liris.declareexecutionenvironment.activity.Activity;
import be.kuleuven.liris.declareexecutionenvironment.activity.UnaryPropagator;
import be.kuleuven.liris.declareexecutionenvironment.constraint.Constraint;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.Binary;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.ExclusiveChoice;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.alternate.AlternatePrecedence;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.alternate.AlternateResponse;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.chain.ChainPrecedence;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.chain.ChainResponse;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.negative.NotChainSuccession;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.negative.NotCoExistence;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.negative.NotSuccession;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.ordered.Precedence;
import be.kuleuven.liris.declareexecutionenvironment.model.AnnotatedDeclareModel;

public class DeclareMapSemantics {

	private AnnotatedDeclareModel aMod;
	private static ResetInhibitorNetImpl riNet;
	private Marking m;
	private static org.processmining.models.semantics.petrinet.ResetInhibitorNetSemantics riSemantics;
	private Collection<Activity> enabledActivities;
	private static Marking initialMarking = new Marking();
	private boolean deadlock = false;
	private HashSet<Constraint> current_explanations = new HashSet<Constraint>();
	
	public DeclareMapSemantics(AnnotatedDeclareModel aMod){	
		System.out.println("Constraints: " + aMod.getConstraints());
		HashMap<Activity, Pair<Integer,Integer>> begin_bounds = getCurrentBounds(aMod.getActivities(true));
		System.out.println("Begin bounds: " + begin_bounds);
		
		UnaryPropagator.PropagateAllActivities(aMod.getActivities(true));
		for(Activity aT: aMod.getActivities(true))
			aT.lockBounds();
		
		this.aMod = aMod;
		riNet = aMod.getRiNet();
		m = aMod.getInitialMarking();
		for(Place p: m)
			initialMarking.add(p);
					
		riSemantics = PetrinetSemanticsFactory.regularResetInhibitorNetSemantics(ResetInhibitorNet.class);	
		HashMap<Activity, Pair<Integer,Integer>> end_bounds = getCurrentBounds();
		calculateExplanationConstraints(begin_bounds, end_bounds);
		reset();
	}
	
	public Collection<Activity> reset(){	
		this.deadlock = false;
		
		//permanentlyDisabled = new HashSet<Activity>();
		riSemantics.initialize(riNet.getTransitions(), initialMarking);	
		for(Constraint c: aMod.getConstraints())
			c.reset();			
		for(Activity a: aMod.getActivities(true))
			a.reset();
		Collection<Activity> enabled_acts = calculateEnabledActivities(aMod.getEnd());
						
		return enabled_acts;
	}
	
	public void fire(Character c) throws IllegalTransitionException{
		for(Activity a: aMod.getActivities(true))
			if(c.equals(a.getCharacter()))
				fire(a);
	}
	
	public void fire(String s) throws IllegalTransitionException{
		for(Activity a: aMod.getActivities(true))
			if(s.equals(a.getName()))
				fire(a);
	}
	
	public Collection<Activity> fire(Activity a) throws IllegalTransitionException{
//		System.out.println("Firing: "+a);
		Collection<Activity> enabled_acts = null;
		HashMap<Activity, Pair<Integer,Integer>> begin_bounds = getCurrentBounds();
		
		try{			
			riSemantics.executeExecutableTransition(a.getTransition());
			if(!enabledActivities.contains(a))
				throw new MissingResourceException(null,null,null);
			
//			if(a.equals(aMod.getEnd())){
//				enabledActivities.clear();
//			}else{
				a.fire();
				enabled_acts = calculateEnabledActivities(a);
	//		}
		}catch(IllegalTransitionException iT){
			StringBuilder output = new StringBuilder();
			output.append(a+" is not enabled because of [");
			for(PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> e: riNet.getInEdges(a.getTransition())){
				if(e instanceof Arc){
					if(!riSemantics.getCurrentState().contains((Place) e.getSource())){
						output.append(" missing token in "+ e.getSource().getLabel());
					}
				}else if(e instanceof InhibitorArc){
					if(riSemantics.getCurrentState().contains(e.getSource())){
						output.append(" inhibition by "+e.getSource().getLabel());
					}
				}
			}
			output.append("]");
//			System.out.println(output);
		}catch(MissingResourceException mR){
			StringBuilder output = new StringBuilder();
			output.append("\n"+a.getExplanations());
//			System.out.println(output);
		}
		HashMap<Activity, Pair<Integer,Integer>> end_bounds = getCurrentBounds();
//		System.out.println("End bounds: " + end_bounds);
		calculateExplanationConstraints(begin_bounds, end_bounds);
		
		return enabled_acts;
	}
	
	private Collection<Activity> calculateEnabledActivities(Activity firedActivity)  {		
		enabledActivities = new HashSet<Activity>();	
		HashSet<Activity> removed = new HashSet<Activity>();
		
//		System.out.println("Begin bounds: " + begin_bounds);
		
		for(Activity a: aMod.getActivities(true)){
			a.resetExplanations();
			if(riSemantics.getExecutableTransitions().contains(a.getTransition())){
				a.enable();
			}else{
				a.disable(null);
			}
		}
//		System.out.println("EA:" + enabledActivities);
//		for(Constraint c: firedActivity.getOutgoingConstraints()){			
//			if(c instanceof ChainPrecedence){
//				firedActivity.setLowerBound(0, "pre-alteration");
//				break;
//			}
//		}
		
		for(Constraint c: firedActivity.getIncomingConstraints())
			if(c instanceof ChainPrecedence){
				((ChainPrecedence) c).getAntecedent().setLowerBound(0, c);
				break;
			}
		
		for(Activity a: aMod.getActivities(false)){
			int sum = 0;
			for(Binary c: a.getOutgoingConstraints()){
				if(c instanceof ChainPrecedence){
					sum += ((ChainPrecedence) c).getConsequent().getLowerBound();
				}
			a.setLowerBound(Math.max(a.getLowerBound(), sum), c);
			}
		}
		
		// Changes due to propagation
		UnaryPropagator.PropagateAllActivities(aMod.getActivities(true));
		
//		System.out.println("Unary progragation:" + enabledActivities);
		
		for(Activity a: aMod.getActivities(false)){
			int sum = 0;
			for(Binary c: a.getOutgoingConstraints()){
				if(c instanceof ChainPrecedence){
					sum += ((ChainPrecedence) c).getConsequent().getLowerBound();
				}
				a.setLowerBound(Math.max(a.getLowerBound(), sum), c);
			}
			
		}
		
		//enabledActivities.removeAll(calculateHiddenDependencies());	
		Pair<Collection<Activity>, Collection<Constraint>> removed_alternates = checkAlternates(firedActivity);
		enabledActivities.removeAll(removed_alternates.getFirst());
		this.current_explanations.addAll(removed_alternates.getSecond());
		
		//UnaryPropagator.PropagateAllActivities(aMod.getActivities(true));
		
		//disablePermanently();		
		//enabledActivities.removeAll(permanentlyDisabled);		
		for(Activity a: aMod.getActivities(true)){
			if(!a.isEnabled() || a.getUpperBound()==0){
				if(a.getLowerBound()>a.getUpperBound()) 
					deadlock = true;
				a.disable("Upperbound 0");
				if(enabledActivities.contains(a))
					enabledActivities.remove(a);
			}else{
				enabledActivities.add(a);
			}
		}
		
//		System.out.println("\n|| Activity information ||");
//		for (Activity a : aMod.getActivities(true))
//			System.out
//					.println("Activity " + a + " lb " + a.getLowerBound()
//							+ " ub " + a.getUpperBound() + " enabled: "
//							+ a.isEnabled());
//		System.out.println("|| Activity information ||\n");
		
		
		return enabledActivities;
	}
	
	public boolean deadlocked() {
		return this.deadlock;
	}
	
	private HashMap<Activity, Pair<Integer,Integer>> getCurrentBounds(Collection<Activity> acts){
		HashMap<Activity, Pair<Integer,Integer>> ma = new HashMap<Activity, Pair<Integer,Integer>>();
		for(Activity as: acts)
			ma.put(as, new Pair<Integer,Integer>(as.getLowerBound(), as.getUpperBound()));
		return ma;
	}	
	
	private HashMap<Activity, Pair<Integer,Integer>> getCurrentBounds(){
		HashMap<Activity, Pair<Integer,Integer>> ma = new HashMap<Activity, Pair<Integer,Integer>>();
		for(Activity as: aMod.getActivities(true))
			ma.put(as, new Pair<Integer,Integer>(as.getLowerBound(), as.getUpperBound()));
		return ma;
	}	
	
	private void calculateExplanationConstraints(HashMap<Activity, Pair<Integer,Integer>> bb, HashMap<Activity, Pair<Integer,Integer>> eb){
		HashSet<Constraint> explanations = new HashSet<Constraint>();
		for(Activity as: aMod.getActivities(true)) {
//			System.out.println(as + " "+ bb.get(as) +"  " + eb.get(as));
			if(!bb.get(as).equals(eb.get(as)))
				explanations.addAll(as.getExplanationConstraints());
		}
		this.current_explanations = explanations;
		System.out.println("EXPLANATION: " + explanations);
//		System.exit(0);
	}
	
	public HashSet<Constraint> getExplanationConstraints(){
		return current_explanations;
	}
	
	private Pair<Collection<Activity>, Collection<Constraint>> checkAlternates(Activity firedActivity){
		HashSet<Activity> removeables = new HashSet<Activity>();
		HashSet<Constraint> naughties = new HashSet<Constraint>();
		
		for(Constraint c: aMod.getConstraints()){
			
			if(c instanceof AlternatePrecedence){
				Binary ap = (Binary) c;
				if(ap.getAntecedent().getUpperBound()<ap.getConsequent().getLowerBound()) {
					ap.getAntecedent().disable(ap.getConsequent().toString());
					naughties.add(c);
				}
			}
			
			if(c instanceof AlternateResponse){
				Binary ar = (Binary) c;
				if(ar.getConsequent().getUpperBound()<=ar.getAntecedent().getLowerBound() && ar.getConsequent().getUpperBound()<Integer.MAX_VALUE/2) {
					ar.getConsequent().disable(ar.getAntecedent().toString());
					naughties.add(c);
				}
			}
			
			if(c instanceof ChainPrecedence){
				Binary cp = (Binary) c;
				if(!firedActivity.equals((cp.getAntecedent()))) {
					cp.fireConsequent();
					naughties.add(c);
				}
			}
			
			
			//Not succession
			if(c instanceof NotSuccession){
				Binary ns = (Binary) c;
				//System.out.println("Doing "+c.toString());
				//System.out.println("Ante LB "+ns.getAntecedent().getLowerBound()+" - "+ns.getConsequent().getLowerBound());
				if(ns.getAntecedent().getTimesFired()>0) {
					ns.getConsequent().setUpperBound(0,c);
					naughties.add(c);
				}
				if(ns.getConsequent().getLowerBound()>0){
					//System.out.println("Does this hold and why? "+ns.getConsequent().getLowerBound());
					ns.getAntecedent().disable(c.toString());
					naughties.add(c);
					//removeables.add(ns.getAntecedent());
				}
			}
			
			
			//Chain response
			if(c instanceof ChainResponse){
				Binary cr = (Binary) c;
				if(!enabledInNextPosition(cr.getConsequent(),cr)) {
					cr.getAntecedent().disable(cr.toString());
					naughties.add(c);
				}
			}				

			//Not chain succession
			if(c instanceof NotChainSuccession){
				Binary ncs = (Binary) c;	
				boolean resolutor = false;
				for(Activity a: aMod.getActivities(false)){
					if (!a.equals(ncs.getAntecedent())
							&& enabledInNextPosition(a,ncs) 
							/*&& a.getLowerBound()>0*/){
						resolutor = true;
						break;
					}
				}
				if(!resolutor) {
					ncs.getAntecedent().disable(ncs.toString());
					naughties.add(c);
				}
			}
		}		
		Pair<Collection<Activity>, Collection<Constraint>> info = new Pair<Collection<Activity>, Collection<Constraint>>(removeables, naughties);
		return info;
	}
	
	private boolean enabledInNextPosition(Activity a, Binary binC){		
		if(!a.isEnabled()){
			boolean containsPrecedence = false;
			for (Binary incoming : a.getIncomingConstraints()) {
				if (incoming instanceof Precedence){
					if(!incoming.isActivated() 
							&& !incoming.getAntecedent().equals(binC.getAntecedent())){
						return false;
					}
				}
			}
			return !containsPrecedence;
		}else{
			for(Binary incoming: a.getIncomingConstraints()){
				if((incoming instanceof NotChainSuccession 
						|| incoming instanceof NotSuccession || incoming instanceof ExclusiveChoice
						|| incoming instanceof NotCoExistence) 
						&& binC.getAntecedent().equals(incoming.getAntecedent()))
					return false;
			}				
		}
		return true;
	}
	
	
//	private void disablePermanently(){
//		for(Activity a: aMod.getActivities(true)){
//			if(a.getUpperBound()==0){
//				permanentlyDisabled.add(a);
//				for(ConstraintDependencyStructure d: aMod.getDependencyStructures()){
//					System.out.println("Checking out "+d.getMainConstraint().toString() +" for "+a);
//					if(d.getMainConstraint() instanceof NotSuccession){
//						if(((Binary) d.getMainConstraint()).getConsequent().equals(a)){
//							permanentlyDisabled.addAll(d.getActivities());
//							disableLowerLevel(d);
//						}
//					}else if(d.getMainConstraint() instanceof Unary){
//						if(d.getMainConstraint() instanceof Unary){
//							if(((Unary) d.getMainConstraint()).getAntecedent().equals(a)){
//								permanentlyDisabled.addAll(d.getActivities());
//								disableLowerLevel(d);
//							}
//						}else if(d.getMainConstraint() instanceof Binary){
//							if(((Binary) d.getMainConstraint()).getAntecedent().equals(a)){
//								permanentlyDisabled.addAll(d.getActivities());
//								disableLowerLevel(d);
//							}
//						}
//					}
//				}
//			}
//		}
//	}
	
//	private void disableLowerLevel(ConstraintDependencyStructure d){
//		for(ConstraintDependencyStructure dL: d.getDependentStructures()){
//			if(!((Binary) dL.getMainConstraint()).isActivated()){
//				permanentlyDisabled.addAll(dL.getActivities());
//				disableLowerLevel(dL);
//			}
//		}
//	}
	
//	private Collection<Activity> calculateHiddenDependencies(){
//		Collection<Activity> removeables = new HashSet<Activity>();	
//		
//		for(ConstraintDependencyStructure d: aMod.getDependencyStructures()){
//			//For not succession
//			if(d.getMainConstraint() instanceof NotSuccession){
//				Activity mainAntecedent = ((Binary) d.getMainConstraint()).getAntecedent();
//				removeables.addAll(getInnerViolations(d, removeables, mainAntecedent, ((Binary) d.getMainConstraint()).getConsequent()));
//				removeables.addAll(getLowerLevel(d, removeables, mainAntecedent));
//			}
//			
//			//For unaries
//			if(d.getMainConstraint() instanceof Unary){
//				Activity mainAntecedent = ((Unary) d.getMainConstraint()).getAntecedent();
//				if(mainAntecedent.getUpperBound()<2){
//					removeables.addAll(getInnerViolations(d, removeables, mainAntecedent, mainAntecedent));
//					removeables.addAll(getLowerLevel(d, removeables, mainAntecedent));
//				}
//			}			
//		}
//		return removeables;
//	}
	
//	private Collection<Activity> getInnerViolations(ConstraintDependencyStructure d, Collection<Activity> removeables, Activity mainAntecedent, Activity comparator){
//		for(Constraint c: d.getDependentConstraints()){
//			if(c instanceof Unary){
//				if(!c.isAccepting()){
//					Activity aa = ((Unary) c).getAntecedent();					
//					if(!comparator.equals(aa) || d.getMainConstraint() instanceof Binary)
//						removeables.add(mainAntecedent);
//				}
//			}else if (c instanceof Binary){
//				if(!(c instanceof Choice)){
//					if(((Binary) c).isActivated()){
//						if(!((Binary) c).getConsequent().equals(comparator) || d.getMainConstraint() instanceof Binary)
//							removeables.add(mainAntecedent);	
//					}
//				}else{
//					Choice choice = (Choice) c;
//					if(!d.getActivities().contains(choice.getAntecedent())){
//						if(choice.getAntecedent().getUpperBound()<1 && !choice.isAccepting()){
//							removeables.add(mainAntecedent);
//						}
//					}else{
//						if(choice.getConsequent().getUpperBound()<1 && !choice.isAccepting()){
//							removeables.add(mainAntecedent);
//						}
//					}
//					System.out.println("Still have to implement choice!");
//				}
//			}
//		}
//		return removeables;
//	}

	
//	private Collection<Activity> getLowerLevel(ConstraintDependencyStructure d, Collection<Activity> removeables, Activity mainAntecedent){
//		for(ConstraintDependencyStructure dL: d.getDependentStructures()){
//			Binary mainConstraint = (Binary) dL.getMainConstraint();
//			System.out.println("Trying to scrutize "+dL.getMainConstraint());
//			
//			//Niet geactiveerd en geen lowerbound problemen
//			System.out.println("What to test?"+mainConstraint.isActivated()+" - "+mainConstraint.getAntecedent().getLowerBound()+
//					" "+mainConstraint+" and "+mainConstraint.getConsequent().getLowerBound());
//			if(!mainConstraint.isActivated() && !(mainConstraint.getAntecedent().getLowerBound()>1)){
//				removeables.addAll(getInnerViolations(dL, removeables, mainAntecedent, ((Binary) dL.getMainConstraint()).getAntecedent()));
//				removeables.addAll(getLowerLevel(dL, removeables, mainAntecedent));
//			}else if(mainConstraint.getAntecedent().getLowerBound()>0){
//				if(mainConstraint.getConsequent().getLowerBound()>1){
//					removeables.addAll(getInnerViolations(dL, removeables, mainAntecedent, ((Binary) dL.getMainConstraint()).getAntecedent()));
//					removeables.addAll(getLowerLevel(dL, removeables, mainAntecedent));
//		
//				}else if(mainConstraint.getAntecedent().getLowerBound()==1){
//					
//				}
//			}
//			
////			if(!getCurrentState().contains(dL.getP()) && !noOcAlt.containsKey(dL.getT())){
////				checkForInnerViolationsLower(t,tR,origD,dL);
////				tR.addAll(getLowerLevel(dL, t, d));
////			}			
////			if(noOcAlt.containsKey(dL.getT()) && !dL.getDir()){
////
////				if(noOcAlt.containsKey(dL.getTforP(dL.getP()))){
////					if(noOcAlt.get(dL.getTforP(dL.getP()))>1){
////						checkForInnerViolationsLower(t,tR,origD,dL);
////						tR.addAll(getLowerLevel(dL, t, d));	
////						Transition tI = dL.getT();
////						for(Transition tO: altGraph.get(tI).get(2)){
////							if(noOcAlt.get(tO)>noOcAlt.get(tI)){
////								ex.add(nodeLabels.get(t) + " is disabled because "+nodeLabels.get(tI)+" still has to fire at least "+noOcAlt.get(tI)+" times for "+tO.getLabel()+" ("+noOcAlt.get(tO)+") ");
////							}
////						}
////					}
////					else if(noOcAlt.get(dL.getT())==1){	
////						System.out.println("\nI'm here! Hahahahaha, adding? "+getLowerLevelDependencies(dL.getTforP(dL.getP()),t));
////						if(getLowerLevelDependencies(dL.getTforP(dL.getP()),t))
////							tR.add(t);					
////					}
////				}
////			}
//		}	
//
//		return removeables;
//	}
	
	
//	private boolean getLowerLevelDependencies(Activity t, Activity mainTrans){
//		boolean violation=false;
//		for(Transition tI: altGraph.get(t).get(1)){
//			if(noOcAlt.get(tI)>0){
//				if(!mainTrans.equals(tI)){
//					violation=true;
//					Place p=checkRelationType(tI,t).getSecond();
//					if(getCurrentState().contains(p)){
//						ex.add(nodeLabels.get(mainTrans)+" is disabled because "+nodeLabels.get(tI)+" still has to fire for "+nodeLabels.get(p));
//					}else{
//						ex.add(nodeLabels.get(mainTrans)+" is disabled because "+nodeLabels.get(tI)+" still has to fire "+noOcAlt.get(tI)+" time(s)");
//					}
//					getLowerLevelDependencies(tI, mainTrans);
//				}
//			}
//		}
//		return violation;
//	}

	
	public Collection<Activity> getEnabledActivities(){
		return enabledActivities;		
	}
	
}
