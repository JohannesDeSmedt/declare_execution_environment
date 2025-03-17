package be.kuleuven.liris.declareexecutionenvironment.old;

import be.kuleuven.liris.declareexecutionenvironment.DeclareTemplateName;

public class MFConstraintToDeclareConstraintConvertor {
		
	public static DeclareConstraint convert(String constraintName, Character a, Character b){
		DeclareConstraint d = null;
		switch(constraintName){
		case "Response":
			d = new DeclareConstraint(DeclareTemplateName.Response, a, b);
			break;
		case "Precedence":
			d = new DeclareConstraint(DeclareTemplateName.Precedence, a, b);
			break;
		case "Succession":
			d = new DeclareConstraint(DeclareTemplateName.Succession, a, b);
			break;
		case "AlternateResponse":
			d = new DeclareConstraint(DeclareTemplateName.Alternate_Response, a, b);
			break;
		case "AlternatePrecedence":
			d = new DeclareConstraint(DeclareTemplateName.Alternate_Precedence, a, b);
			break;
		case "AlternateSuccession":
			d = new DeclareConstraint(DeclareTemplateName.Alternate_Succession, a, b);
			break;
		case "ChainResponse":
			d = new DeclareConstraint(DeclareTemplateName.Chain_Response, a, b);
			break;
		case "ChainPrecedence":
			d = new DeclareConstraint(DeclareTemplateName.Chain_Precedence, a, b);
			break;
		case "ChainSuccession":
			d = new DeclareConstraint(DeclareTemplateName.Chain_Succession, a, b);
			break;
		case "RespondedExistence":
			d = new DeclareConstraint(DeclareTemplateName.Responded_Existence, a, b);
			break;
		case "CoExistence":
			d = new DeclareConstraint(DeclareTemplateName.CoExistence, a, b);
			break;
		case "NotSuccession":
			d = new DeclareConstraint(DeclareTemplateName.Not_Succession, a, b);
			break;
		case "NotChainSuccession":
			d = new DeclareConstraint(DeclareTemplateName.Not_Chain_Succession, a, b);
			break;
		case "NotCoExistence":
			d = new DeclareConstraint(DeclareTemplateName.Not_CoExistence, a, b);
			break;		
		case "Init":
			d = new DeclareConstraint(DeclareTemplateName.Init, a, b);
			break;	
		case "End":
			d = new DeclareConstraint(DeclareTemplateName.Last, a, b);
			break;	
		case "AtMostOne":
			d = new DeclareConstraint(DeclareTemplateName.Absence2, a, b);
			break;	
		case "Participation":
			d = new DeclareConstraint(DeclareTemplateName.Existence, a, b);
			break;	
		}			
		return d;
	}
	
}
