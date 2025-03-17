package be.kuleuven.liris.declareexecutionenvironment;

import java.util.HashMap;

public class DeclareTemplateConversion{

	private HashMap<String,DeclareTemplateName> map;
	
	public DeclareTemplateConversion(){
		map = new HashMap<String,DeclareTemplateName>();
		map.put("existence", DeclareTemplateName.Existence);
		map.put("existence1", DeclareTemplateName.Existence);
		map.put("existence2", DeclareTemplateName.Existence2);
		map.put("existence3", DeclareTemplateName.Existence3);
		
		map.put("exactly", DeclareTemplateName.Exactly1);
		map.put("exactly1", DeclareTemplateName.Exactly1);
		map.put("exactly2", DeclareTemplateName.Exactly2);
		map.put("exactly3", DeclareTemplateName.Exactly3);
		
		map.put("absence2", DeclareTemplateName.Absence2);
		map.put("absence3", DeclareTemplateName.Absence3);
		
		map.put("init", DeclareTemplateName.Init);
		map.put("last", DeclareTemplateName.Last);
		
		map.put("choice", DeclareTemplateName.Choice);
		map.put("exclusive choice", DeclareTemplateName.Exclusive_Choice);

		map.put("responded existence", DeclareTemplateName.Responded_Existence);
		map.put("co-existence", DeclareTemplateName.CoExistence);
		
		map.put("response", DeclareTemplateName.Response);
		map.put("precedence", DeclareTemplateName.Precedence);
		map.put("succession", DeclareTemplateName.Succession);
		
		map.put("alternate response", DeclareTemplateName.Alternate_Response);
		map.put("alternate precedence", DeclareTemplateName.Alternate_Precedence);
		map.put("alternate succession", DeclareTemplateName.Alternate_Succession);
		
		map.put("chain response", DeclareTemplateName.Chain_Response);
		map.put("chain precedence", DeclareTemplateName.Chain_Precedence);
		map.put("chain succession", DeclareTemplateName.Chain_Succession);
		
		map.put("not co-existence", DeclareTemplateName.Not_CoExistence);		
		map.put("not succession", DeclareTemplateName.Not_Succession);
		map.put("not chain succession", DeclareTemplateName.Not_Chain_Succession);
	}

	public DeclareTemplateName translate(String s){
		return map.get(s);
	}
	
	public String translate(DeclareTemplateName d){
		for(String s: map.keySet()){
			if(map.get(s).equals(d))
				return s;
		}
		return null;
	}	
}
