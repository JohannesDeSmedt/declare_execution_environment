package be.kuleuven.liris.declareexecutionenvironment;

import java.util.HashMap;

public class ConstraintDescriptions {

	static HashMap<DeclareTemplateName, String> descriptions;
	
	public ConstraintDescriptions(){
		descriptions = new HashMap<DeclareTemplateName,String>();
		descriptions.put(DeclareTemplateName.Existence, "%% has to be executed at least once.");
		descriptions.put(DeclareTemplateName.Existence2, "%% has to be executed at least twice.");
		descriptions.put(DeclareTemplateName.Existence3, "%% has to be executed at least three times.");
		descriptions.put(DeclareTemplateName.Absence2, "%% can happen at most once.");
		descriptions.put(DeclareTemplateName.Absence3, "%% can happen at most twice.");
		descriptions.put(DeclareTemplateName.Exactly1, "%% has to happen exactly once.");
		descriptions.put(DeclareTemplateName.Exactly2, "%% has to happen exactly twice.");
		descriptions.put(DeclareTemplateName.Exactly3, "%% has to happen exactly three times.");
		descriptions.put(DeclareTemplateName.Init, "%% has to be executed first.");
		descriptions.put(DeclareTemplateName.Last, "%% has to be executed last.");
		descriptions.put(DeclareTemplateName.Responded_Existence, "If %% happens (at least once) then ** has to have (at least once) happened before or has to happen after %%.");
		descriptions.put(DeclareTemplateName.CoExistence, "If %% happens (at least once) then ** has to have (at least once) happened before or has to happen after %%, and vice versa.");
		descriptions.put(DeclareTemplateName.Precedence, "** has to be preceded by %%. ** can happen only after %% has happened.");
		descriptions.put(DeclareTemplateName.Response, "Whenever activity %% is executed, activity ** has to be executed eventually afterwards.");
		descriptions.put(DeclareTemplateName.Succession, "** has to be preceded by %%. ** can happen only after %% has happened and whenever activity %% is executed, activity ** has to be executed eventually afterwards.");
		descriptions.put(DeclareTemplateName.Alternate_Response, "After each %% is executed at least one ** is executed. Another %% can be executed again only after the next **.");
		descriptions.put(DeclareTemplateName.Alternate_Precedence, "** cannot happen before %%. After is happens, it cannot happen before the next %% again.");
		descriptions.put(DeclareTemplateName.Alternate_Succession, "After each %% is executed at least one ** is executed. Another %% can be executed again only after the next ** and ** cannot happen before %%. After is happens, it cannot happen before the next %% again.");
		descriptions.put(DeclareTemplateName.Chain_Response, "After each %%, ** has to occur immediately afterwards.");
		descriptions.put(DeclareTemplateName.Chain_Precedence, "** can be executed only right after %%.");
		descriptions.put(DeclareTemplateName.Chain_Succession, "After each %%, ** has to occur immediately afterwards and ** can be executed only right after %%.");
		descriptions.put(DeclareTemplateName.Choice, "At least either %% or ** occur at least once (both can happen as well).");
		descriptions.put(DeclareTemplateName.Exclusive_Choice, "Either %% or ** has to happen, but not both.");
		descriptions.put(DeclareTemplateName.Not_CoExistence, "%% and ** cannot be executed together.");
		descriptions.put(DeclareTemplateName.Not_Succession, "Before **, there cannot be %% and after %%, there cannot be **.");
		descriptions.put(DeclareTemplateName.Not_Chain_Succession, "** can never happen immediately after %%.");
	}
	
	public static String getDescription(DeclareTemplateName constraint){
		return descriptions.get(constraint);
	}
}
