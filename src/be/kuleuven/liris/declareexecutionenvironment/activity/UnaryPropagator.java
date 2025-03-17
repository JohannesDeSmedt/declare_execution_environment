package be.kuleuven.liris.declareexecutionenvironment.activity;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.Binary;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.Choice;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.ExclusiveChoice;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.alternate.AlternatePrecedence;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.alternate.AlternateResponse;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.alternate.AlternateSuccession;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.chain.ChainPrecedence;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.negative.NotCoExistence;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.ordered.Precedence;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.ordered.Response;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.ordered.Succession;

public class UnaryPropagator {


	public static void PropagateAllActivities(Collection<Activity> activities){
		Set<Activity> checks = new HashSet<Activity>(activities);
		
		while(!checks.isEmpty()){
			HashMap<Activity,Integer> changes = new HashMap<Activity,Integer>();
			for(Activity a: activities)
				changes.put(a, 0);
			for(Activity a: activities){
				for(Binary c: a.getIncomingConstraints())
					changes = propagate(changes, c.getAntecedent(),a,c);
				
				for(Binary c: a.getOutgoingConstraints())
					changes = propagate(changes, a,c.getConsequent(),c);	
			}
			for(Activity a: changes.keySet())
				if(changes.get(a).equals(0))
					checks.remove(a);
		}
	}
	
	private static HashMap<Activity, Integer> propagate(HashMap<Activity,Integer> changes, Activity ante, Activity conseq, Binary c){
		int aLB = ante.getLowerBound();
		int aUB = ante.getUpperBound();
		int cLB = conseq.getLowerBound();
		int cUB = conseq.getUpperBound();
		
//		System.out.println("Propagate for " + c);
		
		if(c instanceof Precedence){
			//Semantics
			
			if(conseq.getLowerBound()>0 && !c.isActivated())
				ante.setLowerBound(Math.max(1, ante.getLowerBound()),c);
//			if(ante.getUpperBound()<1 && !c.isActivated())
//				ante.setLowerBound(0,c.toString());		
			
			if(c.getAntecedent().getUpperBound()<=0 && !c.isActivated())
				c.getConsequent().setUpperBound(0,c);
		}
		
		if(c instanceof Response){
			if(ante.getLowerBound()>0 || c.isActivated())
				conseq.setLowerBound(Math.max(1, conseq.getLowerBound()),c);
			if(conseq.getUpperBound()<1)
				ante.setUpperBound(0,c);
			
			if(c.getConsequent().getUpperBound()==1 && c.getAntecedent().getLowerBound()>0){
				c.getConsequent().disable(c.toString());
			}
		}
		
		if(c instanceof Succession){
//			System.out.println("Succession check:" + aLB+" "+ aUB+ " " + cLB + " " + cUB);
			//if(!c.isActivated())
			//	c.getConsequent().disable(c.toString());
			
			if(conseq.getLowerBound()>0 && !c.isActivated()) {
				ante.setLowerBound(Math.max(1, ante.getLowerBound()),c);
			}
			
			if(c.getAntecedent().getUpperBound()<=0 && !c.isActivated()) {
				c.getConsequent().setUpperBound(0,c);
			}
			
			if(ante.getLowerBound()>0 || !c.isAccepting())
				conseq.setLowerBound(Math.max(1, conseq.getLowerBound()),c);
			if(conseq.getUpperBound()<1)
				ante.setUpperBound(0,c);
			
			if(c.getConsequent().getUpperBound()==1 && c.getAntecedent().getLowerBound()>0){
				c.getConsequent().disable(c.toString());
			}
		}

		if(c instanceof AlternatePrecedence){
			if(!(c instanceof ChainPrecedence))
				ante.setLowerBound(Math.max(ante.getLowerBound(), conseq.getLowerBound()-((c.isActivated()) ? 1 : 0)),c);
			conseq.setUpperBound(Math.min(ante.getUpperBound()+((c.isActivated()) ? 1 : 0), conseq.getUpperBound()),c);
		}
		
		if(c instanceof AlternateResponse){			
			conseq.setLowerBound(Math.max(ante.getLowerBound()+((c.isActivated()) ? 1 : 0),conseq.getLowerBound()),c);
			ante.setUpperBound(Math.min(conseq.getUpperBound()-((c.isActivated()) ? 1 : 0),ante.getUpperBound()),c);
		}
		
		if(c instanceof AlternateSuccession){
			//if(conseq.getLowerBound()>0)
			ante.setLowerBound(Math.max(conseq.getLowerBound()-((c.isActivated()) ? 1 : 0), ante.getLowerBound()),c);
			conseq.setUpperBound(Math.min(ante.getUpperBound()+((c.isActivated()) ? 1 : 0), conseq.getUpperBound()),c);
			conseq.setLowerBound(Math.max(ante.getLowerBound()+((c.isActivated()) ? 1 : 0),conseq.getLowerBound()),c);
			ante.setUpperBound(Math.min(conseq.getUpperBound()-((c.isActivated()) ? 1 : 0),ante.getUpperBound()),c);
		}
		
//		if(c instanceof ChainPrecedence){
//			ante.setLowerBound(Math.max(ante.getLowerBound(), conseq.getLowerBound()), c.toString());
//		}
			
		if(c instanceof Choice){
			if(c.getAntecedent().getTimesFired()+c.getConsequent().getTimesFired()<1){
				if(c.getAntecedent().getUpperBound()==0)
					c.getConsequent().setLowerBound(1,c);
				if(c.getConsequent().getUpperBound()==0)
					c.getAntecedent().setLowerBound(1,c);
			}
		}
		
		
		if(c instanceof ExclusiveChoice || c instanceof NotCoExistence){
			if(conseq.getLowerBound()>0)
				ante.setUpperBound(0,c);
			if(ante.getLowerBound()>0)
				conseq.setUpperBound(0,c);
			
			if(c.getAntecedent().getTimesFired()>0){
				c.getConsequent().setUpperBound(0,c);;
			}
			if(c.getConsequent().getTimesFired()>0)
				c.getAntecedent().setUpperBound(0,c);
		}
		
		
		changes = calculateChange(changes, ante, aLB, aUB);
		changes = calculateChange(changes, conseq, cLB, cUB);			
		return changes;
	}
	
	private static HashMap<Activity,Integer> calculateChange(HashMap<Activity,Integer> changes, Activity a, Integer lb, Integer ub){
		if(a.getLowerBound()!=lb && a.getUpperBound()!=ub){
			changes.put(a, 3);
			//System.out.println("Changes in bounds:");
			//System.out.println(a+" previous LB "+lb +" and UB "+ub+" vs new LB "+a.getLowerBound()+" and UB "+a.getUpperBound());
		}else if(a.getLowerBound()!=lb){
			if(changes.get(a)==2){
				changes.put(a,3);
				//System.out.println("Changes in bounds:");
				//System.out.println(a+" previous LB "+lb +" and UB "+ub+" vs new LB "+a.getLowerBound()+" and UB "+a.getUpperBound());
			}else {
				changes.put(a, 1);
				//System.out.println("Changes in bounds:");
				//System.out.println(a+" previous LB "+lb +" and UB "+ub+" vs new LB "+a.getLowerBound()+" and UB "+a.getUpperBound());
			}
		}else if(a.getUpperBound()!=ub){
			if(changes.get(a)==1){
				changes.put(a,3);
				//System.out.println("Changes in bounds:");
				//System.out.println(a+" previous LB "+lb +" and UB "+ub+" vs new LB "+a.getLowerBound()+" and UB "+a.getUpperBound());
			}else {
				changes.put(a, 2);
				//System.out.println("Changes in bounds:");
				//System.out.println(a+" previous LB "+lb +" and UB "+ub+" vs new LB "+a.getLowerBound()+" and UB "+a.getUpperBound());
			}
		}
		
		return changes;
	}
	
}
