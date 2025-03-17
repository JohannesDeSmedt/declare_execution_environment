package be.kuleuven.liris.declaremodelchecker;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.processmining.framework.util.Pair;
import org.processmining.plugins.declare.visualizing.ConstraintDefinition;

import be.kuleuven.liris.declareexecutionenvironment.ExecutionStarter;
import be.kuleuven.liris.declareexecutionenvironment.activity.Activity;
import be.kuleuven.liris.declareexecutionenvironment.constraint.Constraint;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.Binary;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.alternate.AlternatePrecedence;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.alternate.AlternateResponse;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.alternate.AlternateSuccession;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.negative.NotSuccession;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.ordered.Precedence;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.ordered.Response;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.ordered.Succession;
import be.kuleuven.liris.declareexecutionenvironment.constraint.unary.Exactly;
import be.kuleuven.liris.declareexecutionenvironment.constraint.unary.Existence;
import be.kuleuven.liris.declareexecutionenvironment.constraint.unary.Unary;
import be.kuleuven.liris.declareexecutionenvironment.model.AnnotatedDeclareModel;
import be.kuleuven.liris.declareexecutionenvironment.model.StateSpaceDecMod;
import be.kuleuven.liris.declareexecutionenvironment.model.StateSpaceDecModFSA;
import be.kuleuven.liris.declareexecutionenvironment.model.StateSpaceState;
import be.kuleuven.liris.declareutilities.DeclareRegexUtilities;
import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;

public class Bisimulation {

	public static Random rand = new Random(306);
	
	public static void main(String[] args) throws Exception {
		int max_a = 20;
		int min_a = 4;
		int max_c = 20;
		int min_c = 5;
		
		for(int i=0; i<1000; i++) {
		rand = new Random(i+1);
		
		Class<?>[] param_bin = {String.class, ConstraintDefinition.class, Activity.class, Activity.class};		
		Class<?>[] param_un = {String.class, ConstraintDefinition.class, Activity.class, Integer.class, Integer.class};	
		HashMap<Class<?>,String> constraint_templates = new HashMap<Class<?>,String>();
		constraint_templates.put(AlternateSuccession.class, "alternate succession");
		constraint_templates.put(AlternateResponse.class, "alternate response");
//		constraint_templates.put(AlternatePrecedence.class, "alternate precedence");
		constraint_templates.put(Succession.class, "succession");
		constraint_templates.put(Precedence.class, "precedence");		
		constraint_templates.put(Response.class, "response");		
		constraint_templates.put(NotSuccession.class, "not succession");		
//		constraint_templates.put(Exactly.class, "exactly");		
		HashMap<Class<?>,Class<?>[]> constructor_map = new HashMap<Class<?>,Class<?>[]>();
		constructor_map.put(AlternateSuccession.class, param_bin);
		constructor_map.put(AlternateResponse.class, param_bin);
//		constructor_map.put(AlternatePrecedence.class, param_bin);
		constructor_map.put(Succession.class, param_bin);
		constructor_map.put(Precedence.class, param_bin);		
		constructor_map.put(Response.class, param_bin);		
		constructor_map.put(NotSuccession.class, param_bin);		
//		constructor_map.put(Exactly.class, param_un);		
	
		
		Set<Activity> activities = new HashSet<Activity>();
		Set<Constraint> constraints = new HashSet<Constraint>();
		
		int no_activities = rand.nextInt((max_a - min_a) + 1) + min_a;
		int no_constraints = rand.nextInt((max_c - min_c) + 1) + min_c;
		for(int a=0; a<no_activities;a++) {
			String name = "" + (char) (97 + a);
			Activity new_a = new Activity(name, null, null, (char) (97+a));
			activities.add(new_a);
		}
//		System.out.println("Activities:" + activities);
		List<Activity> activity_list = new ArrayList<Activity>(activities);
		HashSet<Activity> used = new HashSet<Activity>();
		
		List<Class<?>> random_list = new ArrayList<Class<?>>(constraint_templates.keySet());
		for(int c=0; c<no_constraints; c++) {
			Class<?> cc = random_list.get(rand.nextInt((constraint_templates.size())));

			
			Pair<Activity,Activity> ap = getActivityPair(activity_list);
			Activity a_no = ap.getFirst();
			Activity b_no = ap.getSecond();
			used.add(a_no);
			used.add(b_no);
						
			Constructor<?> constructor = cc.getConstructor(constructor_map.get(cc));
				
			Object[] arguments = {constraint_templates.get(cc), null, a_no, b_no};
			Object instance = constructor.newInstance(arguments);

			
//			System.out.println(instance);
			constraints.add((Constraint) instance);
		}
		activities.retainAll(used);
		RegExp r = new RegExp(DeclareRegexUtilities.alphabetToString(activities)+"*");

		Automaton aut = r.toAutomaton();
		long start_aut = System.currentTimeMillis();
		for(Constraint cc: constraints) {
//			System.out.println(cc);
//			System.out.println(aut.toDot());
			if(cc instanceof Binary) {
				Binary bin = (Binary) cc;
//				System.out.println(cc.getRegex() + bin.getAntecedent() + bin.getConsequent());
				Automaton constraint_automaton = DeclareRegexUtilities.regexToAutomaton(activities, bin.getRegex(), bin.getAntecedent(), bin.getConsequent());
//				System.out.println("Constraint automaton:" + constraint_automaton.toDot());
				aut = aut.intersection(DeclareRegexUtilities.regexToAutomaton(activities, bin.getRegex(), bin.getAntecedent(), bin.getConsequent()));
			}
			else {
				Unary un = (Unary) cc;
//				System.out.println(cc.getRegex() + un.getAntecedent());
				aut = aut.intersection(DeclareRegexUtilities.regexToAutomaton(activities, un.getRegex(), un.getAntecedent(), un.getAntecedent()));
			}
		}
		aut.reduce();
		aut.minimize();
		long end_aut = System.currentTimeMillis();
		System.out.println("Time automaton construction: " + (end_aut-start_aut));
		System.out.println("#states: " + aut.getNumberOfStates());
		
		if(!aut.isEmpty()) {
//			System.out.println(aut.toDot());
			AnnotatedDeclareModel model = new AnnotatedDeclareModel(activities, constraints);
			
			long start_my = System.currentTimeMillis();
//			StateSpaceDecModFSA state_space_fsa = new StateSpaceDecModFSA(model);
//			model = new AnnotatedDeclareModel(activities, constraints);
			StateSpaceDecModFSA state_space = new StateSpaceDecModFSA(model);
			long end_my = System.currentTimeMillis();
			System.out.println("Time automaton construction: " + (end_my-start_my));
			System.out.println("#states: " + state_space.getNoStates());
			
			
			for(StateSpaceState sss: state_space.getStates()) {
				for(StateSpaceState sss2: state_space.getStates()) {
					if (sss.getName() != sss2.getName()) {
						if (sss.getActivityTable().equals(sss2.getActivityTable()) && sss.getConstraintTable().equals(sss2.getConstraintTable()))
							throw new Exception();
					}					
				}
			}
			
			
//	        ExecutionStarter.writeStringToFile(aut.toDot(), "graph.dot");
//	        ExecutionStarter.convertDotToPng("graph.dot", "graph.png");
//					
//			ExecutionStarter.writeStringToFile(state_space.toDot(), "graphSS.dot");
//			ExecutionStarter.convertDotToPng("graphSS.dot", "graphSS.png");
//	        
////			ExecutionStarter.writeStringToFile(state_space_fsa.toDot(), "graphFSASS.dot");
////			ExecutionStarter.convertDotToPng("graphFSASS.dot", "graphFSASS.png");
////	        			
//			ExecutionStarter.writeStringToFile(state_space.toClusteredModelDot(), "model_dot.dot");
//			ExecutionStarter.convertDotToPng("model_dot.dot", "model_dot.png");
//	        
//	        File g = new File("graph.dot"); 
//	        g.delete();
//	        File gs = new File("graphSS.dot"); 
//	        gs.delete();
//	        File gs2 = new File("graphFSASS.dot"); 
//	        gs2.delete();
//	        File mm = new File("model_dot.dot"); 
//	        mm.delete();
	    	if(aut.getNumberOfStates()>state_space.getNoStates())
	    		throw new Exception();
		}
	
		}
	}
	
	public static Pair<Activity, Activity> getActivityPair(List<Activity> activity_list){
		Activity a_no = null;
		Activity b_no = null;
		while(a_no == b_no) {
			a_no = activity_list.get(rand.nextInt(activity_list.size()));
			b_no = activity_list.get(rand.nextInt(activity_list.size()));
		}
		return new Pair<Activity,Activity>(a_no, b_no);
	}
 	
    public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
        int x = rand.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

}
