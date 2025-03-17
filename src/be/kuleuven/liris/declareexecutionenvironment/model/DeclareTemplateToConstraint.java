package be.kuleuven.liris.declareexecutionenvironment.model;

import org.processmining.plugins.declare.visualizing.ConstraintDefinition;

import be.kuleuven.liris.declareexecutionenvironment.activity.Activity;
import be.kuleuven.liris.declareexecutionenvironment.constraint.Constraint;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.Choice;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.CoExistence;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.ExclusiveChoice;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.RespondedExistence;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.alternate.AlternatePrecedence;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.alternate.AlternateResponse;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.alternate.AlternateSuccession;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.chain.ChainPrecedence;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.chain.ChainResponse;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.chain.ChainSuccession;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.negative.NotChainSuccession;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.negative.NotCoExistence;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.negative.NotSuccession;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.ordered.Precedence;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.ordered.Response;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.ordered.Succession;
import be.kuleuven.liris.declareexecutionenvironment.constraint.unary.Absence;
import be.kuleuven.liris.declareexecutionenvironment.constraint.unary.Exactly;
import be.kuleuven.liris.declareexecutionenvironment.constraint.unary.Existence;
import be.kuleuven.liris.declareexecutionenvironment.constraint.unary.Init;
import be.kuleuven.liris.declareexecutionenvironment.constraint.unary.Last;
import be.kuleuven.liris.declareexecutionenvironment.constraint.unary.Unary;

public class DeclareTemplateToConstraint {

		//private static HashMap<String,DeclareTemplateName> map;
		
		public static Constraint convert(String constraintName, ConstraintDefinition cDec, Activity a, Activity b){
			Constraint c = null;
			
			switch(constraintName){
			case "init":
				Unary i = new Init(constraintName, cDec, a);
				a.addUnary(i);
				c = i;
				break;
			case "last":
				Unary l = new Last(constraintName, cDec, a);
				a.addUnary(l);
				c = l;
				break;
			case "existence":
				Unary e = new Existence(constraintName, cDec, a, 1);
				a.setLowerBound(1,e);
				a.addUnary(e);
				c = e;
				break;
			case "existence1":
				Unary e1 = new Existence(constraintName, cDec, a, 1);
				a.setLowerBound(1,e1);
				a.addUnary(e1);
				c = e1;
				break;
			case "existence2":
				Unary e2 = new Existence(constraintName, cDec, a, 2);
				a.setLowerBound(2,e2);
				a.addUnary(e2);
				c = e2;
				break;
			case "existence3":
				Unary e3 = new Existence(constraintName, cDec, a, 3);
				a.setLowerBound(3,e3);
				a.addUnary(e3);
				c = e3;
				break;
			case "absence":
				Unary ab = new Absence(constraintName, cDec, a, 1);
				a.setUpperBound(0, ab);
				a.addUnary(ab);
				c = ab;
				break;
			case "absence2":
				Unary ab2  = new Absence(constraintName, cDec, a, 2);
				a.setUpperBound(1,ab2);
				a.addUnary(ab2);
				c = ab2;
				break;
			case "absence3":
				Unary ab3 = new Absence(constraintName, cDec, a, 3);
				a.setUpperBound(2,ab3 );
				a.addUnary(ab3);
				c = ab3;
				break;
			case "exactly":
				Unary ex = new Exactly(constraintName, cDec, a, 1, 1);
				a.setLowerBound(1, ex);
				a.setUpperBound(1, ex);
				a.addUnary(ex);
				c = ex;
				break;
			case "exactly1":
				Unary ex1 = new Exactly(constraintName, cDec, a, 1, 1);
				a.setLowerBound(1,ex1);
				a.setUpperBound(1,ex1);
				a.addUnary(ex1);
				c = ex1;
				break;
			case "exactly2":
				Unary ex2 = new Exactly(constraintName, cDec, a, 2, 2);
				a.setLowerBound(2, ex2);
				a.setUpperBound(2, ex2);
				a.addUnary(ex2);
				c = ex2;
				break;
			case "exactly3":
				Unary ex3 = new Exactly(constraintName, cDec, a, 3, 3);	
				a.setLowerBound(3, ex3);
				a.setUpperBound(3, ex3);
				a.addUnary(ex3);
				c = ex3;
				break;
			case "responded existence":
				RespondedExistence re = new RespondedExistence(constraintName, cDec, a, b);
				a.addOutgoingConstraint(re);
				b.addIncomingConstraint(re);
				c = re;
				break;
			case "co-existence":
				CoExistence ce = new CoExistence(constraintName, cDec, a, b);
				a.addOutgoingConstraint(ce);
				b.addIncomingConstraint(ce);
				c = ce;
				break;
			case "precedence":
				Precedence p = new Precedence(constraintName, cDec, a, b);
				a.addOutgoingConstraint(p);
				b.addIncomingConstraint(p);
				c = p;
				break;
			case "response":
				Response r = new Response(constraintName, cDec, a, b);	
				a.addOutgoingConstraint(r);
				b.addIncomingConstraint(r);
				c = r;
				break;
			case "succession":
				Succession s = new Succession(constraintName, cDec, a, b);	
				a.addOutgoingConstraint(s);
				b.addIncomingConstraint(s);
				c = s;
				break;
			case "alternate precedence":
				AlternatePrecedence ap = new AlternatePrecedence(constraintName, cDec, a, b);	
				a.addOutgoingConstraint(ap);
				b.addIncomingConstraint(ap);
				c = ap;
				break;
			case "alternate response":
				AlternateResponse ar = new AlternateResponse(constraintName, cDec, a, b);
				a.addOutgoingConstraint(ar);
				b.addIncomingConstraint(ar);
				c = ar;
				break;
			case "alternate succession":
				AlternateSuccession as = new AlternateSuccession(constraintName, cDec, a, b);	
				a.addOutgoingConstraint(as);
				b.addIncomingConstraint(as);
				c = as;
				break;
			case "chain precedence":
				ChainPrecedence cp = new ChainPrecedence (constraintName, cDec, a, b);	
				a.addOutgoingConstraint(cp);
				b.addIncomingConstraint(cp);
				c = cp;
				break;
			case "chain response":
				ChainResponse cr = new ChainResponse(constraintName, cDec, a, b);	
				a.addOutgoingConstraint(cr);
				b.addIncomingConstraint(cr);
				c = cr;
				break;
			case "chain succession":
				ChainSuccession cs = new ChainSuccession(constraintName, cDec, a, b);	
				a.addOutgoingConstraint(cs);
				b.addIncomingConstraint(cs);
				c = cs;
				break;
			case "choice":
				Choice ch = new Choice(constraintName, cDec, a, b);	
				a.addOutgoingConstraint(ch);
				b.addIncomingConstraint(ch);
				c = ch;
				break;
			case "exclusive choice":
				ExclusiveChoice ec = new ExclusiveChoice(constraintName, cDec, a, b);	
				a.addOutgoingConstraint(ec);
				b.addIncomingConstraint(ec);
				c = ec;
				break;
			case "not succession":
				NotSuccession ns = new NotSuccession(constraintName, cDec, a, b);	
				a.addOutgoingConstraint(ns);
				b.addIncomingConstraint(ns);
				c = ns;
				break;
			case "not co-existence":
				NotCoExistence nce = new NotCoExistence(constraintName, cDec, a, b);	
				a.addOutgoingConstraint(nce);
				b.addIncomingConstraint(nce);
				c = nce;
				break;
			case "not chain succession":
				NotChainSuccession ncs = new NotChainSuccession(constraintName, cDec, a, b);	
				a.addOutgoingConstraint(ncs);
				b.addIncomingConstraint(ncs);
				c = ncs;
				break;
			}
			return c;
		}
		
}
