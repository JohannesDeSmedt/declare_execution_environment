package be.kuleuven.liris.declareexecutionenvironment.pruner;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import be.kuleuven.liris.declareexecutionenvironment.constraint.Constraint;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.Binary;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.CoExistence;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.RespondedExistence;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.alternate.AlternatePrecedence;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.alternate.AlternateResponse;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.alternate.AlternateSuccession;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.ordered.Precedence;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.ordered.Response;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.ordered.Succession;
import be.kuleuven.liris.declareexecutionenvironment.constraint.unary.Last;
import be.kuleuven.liris.declareexecutionenvironment.constraint.unary.Unary;

public class DeclareConstraintSetPruner {
	
	public static Collection<Constraint> prune(Collection<Constraint> input) {
		
		//DeclareMap output = new DeclareMap(outputI.getModel(), outputI.getModelCh(),
		//		outputI.getView(), outputI.getViewCh(), outputI.getBroker(), outputI.getBrokerCh());
		
		System.out.println("Number of constraints before: "+input.size());
		for(Constraint c: input)
			System.out.println("Before ---- "+c);
					
				
		/****** Remove "alternate" where unnecessary ******/
		input = removeAlternates(input);
		
		/****** Remove response/precedence where succession ******/
		input = removeResPres(input);
		
		/****** Remove co-existence rules ******/	
		input = removeCoexistence(input);
						
		/****** Remove response when consequent is last ******/	
		input = removeWhenLast(input);
		
		//TODO: remove response, succession when LAST,...	
		//TODO: remove not succession when LAST,...
		
		/****** Remove transitive rules ******/
		Set<Constraint> toRemove = new HashSet<Constraint>();
		
		for(Constraint c: input){
			if(c instanceof Precedence || c instanceof Response || c instanceof Succession || c instanceof CoExistence){
				Binary bC = (Binary) c;
				for(Constraint c2: input){
					if(c.getClass().equals(c2.getClass()) && !c.equals(c2)){
						Binary bC2 = (Binary) c2;
						if(bC.getConsequent().equals(bC2.getAntecedent())){
							for(Constraint c3: input){
								if(c.getClass().equals(c3.getClass()) && !c2.equals(c3)){
									Binary bC3 = (Binary) c3;
									if(bC.getAntecedent().equals(bC3.getAntecedent()) && bC2.getConsequent().equals(bC3.getConsequent()))	
										toRemove.add(c);
								}
							}
						}
					}
				}
				
			}
		}
		input.removeAll(toRemove);		
		System.out.println("Number of constraints after: "+input.size());
		for(Constraint c: input)
			System.out.println("After ---- "+c);
		
		return input;
}

	
	private static Collection<Constraint> removeAlternates(Collection<Constraint> input) {
		Collection<Constraint> toRemove = new HashSet<Constraint>();
		Collection<Constraint> toAdd = new HashSet<Constraint>();

		for(Constraint c: input){			
			if(c instanceof Binary){
				Binary bC = (Binary) c;
				
				if(bC.getAntecedent().getUpperBound()==1 || bC.getConsequent().getUpperBound()==1){
					if(bC instanceof AlternatePrecedence){
						toRemove.add(c);
						toAdd.add(new Precedence("precedence",null,bC.getAntecedent(),bC.getConsequent()));						
					}else if(bC instanceof AlternateResponse){
						toRemove.add(c);
						toAdd.add(new Response("response",null,bC.getAntecedent(),bC.getConsequent()));	
					}else if(bC instanceof AlternateSuccession){
						toRemove.add(c);
						toAdd.add(new Succession("succession",null,bC.getAntecedent(),bC.getConsequent()));	
					}
				}
			}
		}
		input.removeAll(toRemove);
		input.removeAll(toAdd);
		return input;
	}
		
	private static Collection<Constraint> removeWhenLast(Collection<Constraint> input) {
		Collection<Constraint> toRemove = new HashSet<Constraint>();

		for(Constraint c: input){
			if(c instanceof Response){
				Binary bC = (Binary) c;
				for(Unary u: bC.getConsequent().getUnaries()){
					if(u instanceof Last){
						toRemove.add(c);
					}
				}
			}
		}
		input.removeAll(toRemove);
		return input;
	}
	
	private static Collection<Constraint> removeResPres(Collection<Constraint> input){
		Collection<Constraint> toRemove = new HashSet<Constraint>();

		for(Constraint c: input){
			if(c instanceof Response || c instanceof Precedence){
				Binary bC = (Binary) c;
				for(Constraint c2: input){
					if(c2 instanceof Succession){
						Binary bC2 = (Binary) c2;
						if(bC.getAntecedent().equals(bC2.getAntecedent()) && bC.getConsequent().equals(bC2.getConsequent())){
							toRemove.add(c);
						}
					}
				}
			}
		}
		input.removeAll(toRemove);	
		return input;	
	}
	
	private static Collection<Constraint> removeCoexistence(Collection<Constraint> input){
		Collection<Constraint> toRemove = new HashSet<Constraint>();

		for(Constraint c: input){
			if(c instanceof CoExistence){
				Binary bC = (Binary) c;
				if(bC.getAntecedent().getLowerBound()>0 && bC.getConsequent().getLowerBound()>0){
					toRemove.add(c);
				}
			}
			if(c instanceof RespondedExistence){
				Binary bC = (Binary) c;
				if(bC.getConsequent().getLowerBound()>0){
					toRemove.add(c);
				}
			}
		}
		input.removeAll(toRemove);
		return input;
	}
	
		
}

