package org.processmining.plugins.decmod2rinet;

import java.util.HashMap;

public class ConstraintDescriptions {

	static HashMap<String,String> descriptions;
	
	public ConstraintDescriptions(){
		descriptions = new HashMap<String,String>();
		descriptions.put("existence", "%% has to be executed at least once.");
		descriptions.put("existence1", "%% has to be executed at least once.");
		descriptions.put("existence2", "%% has to be executed at least twice.");
		descriptions.put("existence3", "%% has to be executed at least three times.");
		descriptions.put("absence2", "%% can happen at most once.");
		descriptions.put("absence3", "%% can happen at most twice.");
		descriptions.put("exactly1", "%% has to happen exactly once.");
		descriptions.put("exactly2", "%% has to happen exactly twice.");
		descriptions.put("exactly3", "%% has to happen exactly three times.");
		descriptions.put("init", "%% has to be executed first.");
		descriptions.put("last", "%% has to be executed last.");
		descriptions.put("responded existence", "If %% happens (at least once) then ** has to have (at least once) happened before or has to happen after %%.");
		descriptions.put("co-existence", "If %% happens (at least once) then ** has to have (at least once) happened before or has to happen after %%, and vice versa.");
		descriptions.put("precedence", "** has to be preceded by %%. ** can happen only after %% has happened.");
		descriptions.put("response", "Whenever activity %% is executed, activity ** has to be executed eventually afterwards.");
		descriptions.put("succession", "** has to be preceded by %%. ** can happen only after %% has happened and whenever activity %% is executed, activity ** has to be executed eventually afterwards.");
		descriptions.put("alternate response", "After each %% is executed at least one ** is executed. Another %% can be executed again only after the next **.");
		descriptions.put("alternate precedence", "** cannot happen before %%. After is happens, it cannot happen before the next %% again.");
		descriptions.put("alternate succession", "After each %% is executed at least one ** is executed. Another %% can be executed again only after the next ** and ** cannot happen before %%. After is happens, it cannot happen before the next %% again.");
		descriptions.put("chain response", "After each %%, ** has to occur immediately afterwards.");
		descriptions.put("chain precedence", "** can be executed only right after %%.");
		descriptions.put("chain succession", "After each %%, ** has to occur immediately afterwards and ** can be executed only right after %%.");
		descriptions.put("choice", "At least either %% or ** occur at least once (both can happen as well).");
		descriptions.put("exclusive choice", "Either %% or ** has to happen, but not both.");
		descriptions.put("not co-existence", "%% and ** cannot be executed together.");
		descriptions.put("not succession", "Before **, there cannot be %% and after %%, there cannot be **.");
		descriptions.put("not chain succession", "** can never happen immediately after %%.");
	}
	
	public static String getDescription(String constraint){
		return descriptions.get(constraint);
	}
}
