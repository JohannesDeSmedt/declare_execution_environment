package be.kuleuven.liris.declareexecutionenvironment.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.graphbased.directed.petrinet.impl.ResetInhibitorNetImpl;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.declare.visualizing.ActivityDefinition;
import org.processmining.plugins.declare.visualizing.AssignmentModel;
import org.processmining.plugins.declare.visualizing.ConstraintDefinition;
import org.processmining.plugins.declare.visualizing.ConstraintTemplate;
import org.processmining.plugins.declare.visualizing.Parameter;

import be.kuleuven.liris.declareexecutionenvironment.activity.Activity;
import be.kuleuven.liris.declareexecutionenvironment.constraint.Constraint;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.Binary;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.Choice;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.CoExistence;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.ExclusiveChoice;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.RespondedExistence;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.negative.NotCoExistence;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.negative.NotSuccession;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.ordered.Precedence;
import be.kuleuven.liris.declareexecutionenvironment.constraint.unary.Absence;
import be.kuleuven.liris.declareexecutionenvironment.constraint.unary.Exactly;
import be.kuleuven.liris.declareexecutionenvironment.constraint.unary.Existence;
import be.kuleuven.liris.declareexecutionenvironment.constraint.unary.Last;
import be.kuleuven.liris.declareexecutionenvironment.constraint.unary.Unary;
import be.kuleuven.liris.declareexecutionenvironment.dependencystructure.ActivityDependencyStructure;
import be.kuleuven.liris.declareexecutionenvironment.dependencystructure.ConstraintDependencyStructure;
import be.kuleuven.liris.declareexecutionenvironment.dependencystructure.DependencyStructure;
import be.kuleuven.liris.declareexecutionenvironment.petrinet.PetrinetConstructBuilder;

public class AnnotatedDeclareModel{

	private Collection<Constraint> constraintSet = new HashSet<Constraint>(); 	
	private Collection<Activity> activitySet = new HashSet<Activity>();
	private HashSet<ConstraintDependencyStructure> dependencyStructureSet = new HashSet<ConstraintDependencyStructure>();;
	
	private AssignmentModel model;
	private ResetInhibitorNetImpl riNet = new ResetInhibitorNetImpl("Declare reset net");
	private Marking iniM;
	private Activity endA;
	private HashMap<Transition,Activity> aToT = new HashMap<Transition,Activity>();
	
	private HashSet<Activity> visitedL = new HashSet<Activity>();
	private HashSet<Activity> visitedR = new HashSet<Activity>();
	private HashSet<Constraint> visitedC = new HashSet<Constraint>();	
	
	public AnnotatedDeclareModel(Collection<Activity> activities, Collection<Constraint> constraints){
		this.activitySet = activities;
		this.constraintSet = constraints;
	}
	
	public AnnotatedDeclareModel(Set<Activity> activities, Set<Constraint> constraints){
		this.model = null;
		riNet = new ResetInhibitorNetImpl("Declare reset net");	
		iniM = new Marking();		
		Transition end = riNet.addTransition("END");		
		constraintSet = new HashSet<Constraint>();
		activitySet = activities;

		for (Activity a : activities){
			Transition t = riNet.addTransition(a.getName());
			a.setTransition(t);
			aToT.put(t, a);
		}
		
		for (Constraint c : constraints) {
			System.out.println("Constraint: " + c.getName());
			
			if(c instanceof Unary){
				Unary u = (Unary) c;
				Constraint cNew = DeclareTemplateToConstraint.convert(c.getName(), null, u.getAntecedent(), null);
				constraintSet.add(cNew);
				
				riNet = PetrinetConstructBuilder.addConstraint(u.getAntecedent().getTransition(), null, riNet, c.getName(), end, iniM).getFirst();
				iniM = PetrinetConstructBuilder.addConstraint(u.getAntecedent().getTransition(), null, riNet, c.getName(), end, iniM).getSecond();
			}else{
				Binary b = (Binary) c;
				Constraint cNew = DeclareTemplateToConstraint.convert(c.getName(), null, b.getAntecedent(), b.getConsequent());
				constraintSet.add(cNew);
				
				riNet = PetrinetConstructBuilder.addConstraint(b.getAntecedent().getTransition(), b.getConsequent().getTransition(), riNet, c.getName(), end, iniM).getFirst();
				iniM = PetrinetConstructBuilder.addConstraint(b.getAntecedent().getTransition(), b.getConsequent().getTransition(), riNet, c.getName(), end, iniM).getSecond();
			}
		}
		PetrinetConstructBuilder.finishNet(riNet, end);
		//Set end activity
		for(Activity a: activitySet){
			boolean last = false;
			for(Unary c: a.getUnaries())
				if(c instanceof Last){
					last = true;
					break;
				}
			if(last && a.getUpperBound()==1){
				//endA = a;
				PetrinetConstructBuilder.setEnd(a.getTransition(), riNet);
			}
		}
		
		int ac = 97;
		int i = activities.size();
		endA = new Activity("END", null, end, (char) (ac+i));
		activitySet.add(endA);
		aToT.put(end, endA);
		addDependencyInformation();
	}
	
	public AnnotatedDeclareModel(AssignmentModel model){
		this.model = model;
		riNet = new ResetInhibitorNetImpl("Declare reset net");	
		iniM = new Marking();		
		Transition end = riNet.addTransition("END");		
		
		int ac = 97;
		int i = 0;
		for (ActivityDefinition a : model.getActivityDefinitions()){
			Transition t = riNet.addTransition(a.getName());
			Activity aN = new Activity(a.getName(), a, t, (char) (ac+i));			
			activitySet.add(aN);
			aToT.put(t, aN);
			i++;
		}
		
		for (ConstraintDefinition c : model.getConstraintDefinitions()) {
			System.out.println("Constraint: " + c.getName());
			Integer f = 0;
			Activity a = null, b = null;
			Constraint cNew = null;
			
			for (Parameter p : c.getParameters()) {
				if (f == 0){
					ActivityDefinition ad = c.getBranches(p).iterator().next();
					for(Activity a1: activitySet)
						if(a1.getActDef().equals(ad))
							a = a1;							
				}
				if(c.isBinary()){
					if (f == 1){
						ActivityDefinition ad = c.getBranches(p).iterator().next();
						for(Activity a1: activitySet)
							if(a1.getActDef().equals(ad))
								b = a1;		
					}
										
				}
				f++;
			}
			cNew = DeclareTemplateToConstraint.convert(c.getName(), c, a, b);	
			if(c.isUnary()){
				//System.out.println("## Adding "+cNew);
				riNet = PetrinetConstructBuilder.addConstraint(a.getTransition(), null, riNet, c.getName(), end, iniM).getFirst();
				iniM = PetrinetConstructBuilder.addConstraint(a.getTransition(), null, riNet, c.getName(), end, iniM).getSecond();
			}else{
				riNet = PetrinetConstructBuilder.addConstraint(a.getTransition(), b.getTransition(), riNet, c.getName(), end, iniM).getFirst();
				iniM = PetrinetConstructBuilder.addConstraint(a.getTransition(), b.getTransition(), riNet, c.getName(), end, iniM).getSecond();
			}
			constraintSet.add(cNew);
		}
		PetrinetConstructBuilder.finishNet(riNet, end);
		//Set end activity
		for(Activity a: activitySet){
			boolean last = false;
			for(Unary c: a.getUnaries())
				if(c instanceof Last){
					last = true;
					break;
				}
			if(last && a.getUpperBound()==1){
				//endA = a;
				PetrinetConstructBuilder.setEnd(a.getTransition(), riNet);
			}
		}
		endA = new Activity("END", null, end, (char) (ac+i));
		activitySet.add(endA);
		aToT.put(end, endA);
		addDependencyInformation();
	}
		
	private void addDependencyInformation(){
		//Add dependency information
		for(Constraint c: constraintSet){
			visitedL.clear();
			visitedR.clear();
			visitedC.clear();
			if(c instanceof NotSuccession){	
				DependencyStructure dds = new ConstraintDependencyStructure(c);
				dds = checkBackward(((Binary) c).getConsequent(), dds); 
				dds = checkForward(((Binary) c).getConsequent(), dds);
				Collection<Constraint> dependentConstraints = dds.getDependentConstraints();
				dependentConstraints.remove(c);
				if(!dependentConstraints.isEmpty()  || !dds.getDependentStructures().isEmpty())
					dependencyStructureSet.add((ConstraintDependencyStructure) dds);
			}else if(c instanceof ExclusiveChoice || c instanceof NotCoExistence){
				DependencyStructure dds = new ConstraintDependencyStructure(c);
				dds = checkBackward(((Binary) c).getConsequent(), dds); 
				dds = checkForward(((Binary) c).getConsequent(), dds);
				Collection<Constraint> dependentConstraints = dds.getDependentConstraints();
				dependentConstraints.remove(c);
				if(!dependentConstraints.isEmpty()  || !dds.getDependentStructures().isEmpty())
					dependencyStructureSet.add((ConstraintDependencyStructure) dds);
				
				visitedL.clear();
				visitedR.clear();
				visitedC.clear();
				
				dds = new ConstraintDependencyStructure(c);
				dds = checkBackward(((Binary) c).getAntecedent(), dds);
				dds = checkForward(((Binary) c).getAntecedent(), dds);
				if(!dds.getDependentConstraints().isEmpty() || !dds.getDependentStructures().isEmpty())
					dependencyStructureSet.add((ConstraintDependencyStructure) dds);
			}else if(c instanceof Absence || c instanceof Exactly){
				DependencyStructure dds = new ConstraintDependencyStructure(c);
				dds = new ConstraintDependencyStructure(c);
				dds = checkBackward(((Unary) c).getAntecedent(), dds);
				Collection<Constraint> dependentConstraints = dds.getDependentConstraints();
				dependentConstraints.remove(c);
				if(!dependentConstraints.isEmpty() || !dds.getDependentStructures().isEmpty())
					dependencyStructureSet.add((ConstraintDependencyStructure) dds);
			}
		}
		
		for(Activity a: getActivities(true)){
			DependencyStructure aD = new ActivityDependencyStructure(a);
			visitedL.clear();
			visitedR.clear();
			visitedC.clear();
			aD = checkBackward(a, aD);
			aD = checkForward(a, aD);
			a.addDependencyStructure((ActivityDependencyStructure) aD);
		}
	}
		
	private DependencyStructure checkBackward(Activity a, DependencyStructure deDeSt){
		if(!a.getIncomingConstraints().isEmpty() && !visitedL.contains(a)){
			visitedL.add(a);
			for(Binary conS: a.getIncomingConstraints()){
				boolean check = conS instanceof RespondedExistence || conS instanceof CoExistence;
				if(check && !visitedC.contains(conS)){
					//System.out.println("Adding "+conS.toString());
					visitedC.add(conS);
					deDeSt.addConstraint(conS);
					deDeSt = checkBackward(conS.getAntecedent(), deDeSt);
					deDeSt = checkForward(conS.getAntecedent(), deDeSt);
				}
				if(conS instanceof Choice)
					deDeSt.addConstraint(conS);
			}
		}
		if(!a.getUnaries().isEmpty()){
			for(Unary u: a.getUnaries()){
				if(u instanceof Existence || u instanceof Exactly){
					deDeSt.addConstraint(u);
				}
			}
		}
		return deDeSt;
	}
	
	private DependencyStructure checkForward(Activity a, DependencyStructure deDeSt){
		if(!a.getOutgoingConstraints().isEmpty() && !visitedR.contains(a)){
			visitedR.add(a);
			for(Binary conS: a.getOutgoingConstraints()){
				boolean check = conS instanceof Precedence || conS instanceof CoExistence;
				if(check && !visitedC.contains(conS)){
					visitedC.add(conS);
					DependencyStructure nestedDDS = new ConstraintDependencyStructure(conS);
					nestedDDS = checkBackward(conS.getConsequent(), nestedDDS);
					nestedDDS = checkForward(conS.getConsequent(), nestedDDS);
					deDeSt.addNestedDependency(nestedDDS);
				}
				if(conS instanceof Choice)
					deDeSt.addConstraint(conS);
			}			
		}
		return deDeSt;
	}

	public void printActivityInformation(){
		for(Activity a: activitySet)
			System.out.println(a.getName()+" has "
		+a.getIncomingConstraints().size()
		+" incoming and "+a.getOutgoingConstraints().size()
		+" outgoing constraints and "+a.getUnaries().size()+" unaries");
	}
	
	public Collection<Activity> getActivities(boolean keepEnd){
		Collection<Activity> result = activitySet;
		if(!keepEnd)
			result.remove(endA);
		return result;
	}
	
	public Collection<Character> getCharAlphabet(){
		Collection<Character> result = new HashSet<Character>();
		for(Activity a: getActivities(true))
			result.add(a.getCharacter());
		return result;
	}
	
	public Collection<Constraint> getConstraints(){
		return constraintSet;
	}	
	
	public Activity transitionToActivity(Transition t){
		return aToT.get(t);
	}
	
	public int getDependencyScore(){
		int depScore = 0;
		for(ConstraintDependencyStructure d: dependencyStructureSet){
			depScore += d.getDependencyScore();
		}
		return depScore;
	}
	
	public HashSet<ConstraintDependencyStructure> getDependencyStructures(){
		return dependencyStructureSet;
	}
	
	public ResetInhibitorNetImpl getRiNet(){
		return riNet;
	}
	
	public Activity getEnd(){
		return endA;
	}

	public Marking getInitialMarking(){
		return iniM;
	}
	
	public Activity getActivityByDefinition(ActivityDefinition ad){
		for(Activity a: activitySet){
			if(a.getActDef().equals(ad))
				return a;
		}
		return null;
	}
	
	public Activity getActivityByName(String aS){
		for(Activity a: activitySet){
			if(a.getName().equals(aS))
				return a;
		}
		return null;
	}
	
	public void removeActivity(String aIn){
		for(Activity a: activitySet)
			if(aIn.equals(a.getName())){
				removeActivity(a);
				break;
			}
	}
	
	public void removeActivity(Activity a){
		System.out.println("1. ActivitySet: "+activitySet);
		System.out.println("1. ConstraintSet: "+constraintSet);
		for(Constraint c: a.getIncomingConstraints()){
			constraintSet.remove(c);
			model.deleteConstraintDefinition(c.getCD());
		}
		for(Constraint c: a.getOutgoingConstraints()){
			constraintSet.remove(c);
			model.deleteConstraintDefinition(c.getCD());
		}
		for(Constraint c: a.getUnaries()){
			constraintSet.remove(c);
			model.deleteConstraintDefinition(c.getCD());
		}		
		activitySet.remove(a);
		model.deleteActivityDefinition(a.getActDef());
		System.out.println("2. ConstraintSet: "+constraintSet);
		System.out.println("2. ActivitySet: "+activitySet);
	}
	
	public Constraint getConstraintByName(String cS){
		for(Constraint c: constraintSet){
			if(c.getName().equals(cS))
				return c;
		}
		return null;
	}
		
}
