package be.kuleuven.liris.declareexecutionenvironment.testmodels;

import java.util.HashSet;
import java.util.Set;

import org.processmining.framework.util.Pair;

import be.kuleuven.liris.declareexecutionenvironment.activity.Activity;
import be.kuleuven.liris.declareexecutionenvironment.constraint.Constraint;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.Binary;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.alternate.AlternatePrecedence;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.alternate.AlternateResponse;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.alternate.AlternateSuccession;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.chain.ChainPrecedence;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.chain.ChainResponse;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.chain.ChainSuccession;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.negative.NotSuccession;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.ordered.Precedence;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.ordered.Response;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.ordered.Succession;
import be.kuleuven.liris.declareexecutionenvironment.constraint.unary.Exactly;
import be.kuleuven.liris.declareexecutionenvironment.constraint.unary.Existence;
import be.kuleuven.liris.declareexecutionenvironment.constraint.unary.Init;
import be.kuleuven.liris.declareexecutionenvironment.constraint.unary.Unary;
import be.kuleuven.liris.declareexecutionenvironment.model.AnnotatedDeclareModel;
import be.kuleuven.liris.declareutilities.DeclareRegexUtilities;
import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;

public class Model1 {

	private Automaton aut;
	private AnnotatedDeclareModel model;
	
	public Model1() {
		int ac = 97;
		int i = 0;
		
		Set<Activity> activities = new HashSet<Activity>();
		Set<Constraint> constraints = new HashSet<Constraint>();
		Activity a = new Activity("a", null, null, (char) 97);
		Activity b = new Activity("b", null, null, (char) 98);
		Activity c = new Activity("c", null, null, (char) 99);
		Activity d = new Activity("d", null, null, (char) 100);
		Activity e = new Activity("e", null, null, (char) 101);
		Activity f = new Activity("f", null, null, (char) 102);
		
		Init in = new Init("init", null, a);
		
		Precedence pab = new Precedence("precedence", null, a, b);
//		Choice pab = new Coexistence("chain response", null, a, b);
//		Existence a1 = new Existence("existence2", null, a,  2);
		
		
		Precedence pbc = new Precedence("precedence", null, b, c);
		Succession sad = new Succession("succession", null, a, d);
		ChainResponse crde = new ChainResponse("chain response", null, d, e);
		Succession saf = new Succession("succession", null, a, f);
		AlternateSuccession asad = new AlternateSuccession("alternate succession", null, a, d);
		AlternateSuccession ascd = new AlternateSuccession("alternate succession", null, c, d);
		AlternateResponse arda = new AlternateResponse("alternate response", null, d, a);
//		AlternateSuccession ascd = new AlternateSuccession("alternate succession", null, c, d);
		NotSuccession nsad = new NotSuccession("not succession", null, a, d);
		NotSuccession nsab = new NotSuccession("not succession", null, a, b);
//		Exactly a1 = new Exactly("exactly3", null, a, 3, 3);
		Exactly a2 = new Exactly("exactly2", null, a, 2, 2);
		Exactly d1 = new Exactly("exactly1", null, d, 1, 1);
		Exactly d2 = new Exactly("exactly2", null, d, 2, 2);
		Exactly b2 = new Exactly("exactly2", null, b, 2, 2);
//		Exactly f1 = new Exactly("exactly1", null, f, 1, 1);
		Init ia = new Init("init", null, a);
		

		activities.add(a);
		activities.add(b);
		activities.add(c);
		activities.add(d);
//		activities.add(e);
//		activities.add(f);
		
//		constraints.add(in);
		constraints.add(pab);
		constraints.add(pbc);
//		constraints.add(nsad);
//		constraints.add(nsab);
//		constraints.add(ascd);
		
//		constraints.add(a1);
		constraints.add(a2);
//		constraints.add(b2);
		constraints.add(d2);
//		constraints.add(d1);
//		constraints.add(sad);
//		constraints.add(saf);
//		constraints.add(crde);
//		constraints.add(f1);
//		constraints.add(asad);
//		constraints.add(arda);
//		constraints.add(ia);
		
		RegExp r = new RegExp(DeclareRegexUtilities.alphabetToString(activities)+"*");

		aut = r.toAutomaton();
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
				System.out.println(cc.getRegex() + un.getAntecedent());
				aut = aut.intersection(DeclareRegexUtilities.regexToAutomaton(activities, un.getRegex(), un.getAntecedent(), un.getAntecedent()));
			}
//			System.out.println(aut.toDot());
		}
		aut.reduce();
		aut.minimize();
		
		model = new AnnotatedDeclareModel(activities, constraints);
	}
	
	public Pair<Automaton, AnnotatedDeclareModel> getModels(){
		return new Pair<Automaton, AnnotatedDeclareModel>(aut, model);
	}
	
}
