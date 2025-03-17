package be.kuleuven.liris.declareexecutionenvironment.dependencystructure;

import java.util.Collection;
import java.util.HashSet;

import be.kuleuven.liris.declareexecutionenvironment.activity.Activity;
import be.kuleuven.liris.declareexecutionenvironment.constraint.Constraint;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.Binary;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.Choice;
import be.kuleuven.liris.declareexecutionenvironment.constraint.unary.Unary;

public class ActivityDependencyStructure extends DependencyStructure {

	private Activity activity;
	
	public ActivityDependencyStructure(Activity mainActivity){
		super();
		activity = mainActivity;
		caption = "Dependency structure for "+mainActivity;
	}
	
	public Activity getMainActivity(){
		return activity;
	}
			
	public Collection<Activity> getActivities(){
		HashSet<Activity> activities = new HashSet<Activity>();
		for(Constraint c: dependentConstraints){
			if(c instanceof Unary){
				activities.add(((Unary)c).getAntecedent());
			}else{
				if(c instanceof Choice){
					boolean found = false;
					for(Constraint cI: getAllDependentConstraints()){
						if((((Binary) c).getAntecedent().getOutgoingConstraints().contains(cI)
								|| ((Binary) c).getAntecedent().getIncomingConstraints().contains(cI)) 
								&& (!c.equals(cI))
								){
							activities.add(((Binary) c).getAntecedent());
							found=true;
							break;
						}
					}
					if(!found)
						activities.add(((Binary) c).getConsequent());
				}else{
					activities.add(((Binary)c).getAntecedent());
					activities.add(((Binary)c).getConsequent());
				}
			}
		}
		return activities;
	}
	
	@Override
	public String toString(){
		String output="";
		output += activity.toString();
		for(Constraint d: dependentConstraints)
			output += "\n-- "+d.toString();
		for(DependencyStructure dds: dependentStructures)
			output += "\nDown one level\n"+dds.toString();			
		return output;
	}
	
}
