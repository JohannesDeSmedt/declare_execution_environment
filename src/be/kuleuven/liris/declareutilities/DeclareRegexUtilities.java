package be.kuleuven.liris.declareutilities;

import java.util.Collection;
import java.util.HashMap;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;

public class DeclareRegexUtilities {
	
	public static HashMap<String, String> loadBinaryConstraintSet(){
		HashMap<String,String> decCon = new HashMap<String,String>();
		
		decCon.put("response","[^A]*(A.*B)*[^A]*"); 
		decCon.put("precedence","[^B]*(A(.)*B)*[^B]*"); 
		decCon.put("succession","[^AB]*(A.*B)*[^AB]*"); 
		
		decCon.put("alternate response","[^A]*(A[^A]*B[^A]*)*");
		decCon.put("alternate precedence","[^B]*(A[^B]*B[^B]*)*"); 
		//decCon.put("alternate precedence","[^B]*([A][^B]*[B])*[^B]*"); 
		decCon.put("alternate succession","[^AB]*(A[^AB]*B[^AB]*)*"); 
		
		decCon.put("chain precedence","[^B]*(AB[^B]*)*"); 
		decCon.put("chain response","[^A]*(AB[^A]*)*"); 
		decCon.put("chain succession","[^AB]*(AB[^AB]*)*"); 
		
		decCon.put("not co-existence","[^AB]*((A[^B]*)|(B[^A]*))?"); 
		decCon.put("not succession","[^A]*(A[^B]*)*"); 
		decCon.put("not chain succession","[^A]*(A+[^AB][^A]*)*A*"); 
		
		decCon.put("choice",".*[AB].*"); 
		decCon.put("exclusive choice","([^B]*A[^B]*)|([^A]*B[^A]*)"); 
		
		decCon.put("responded existence","[^A]*((A.*B.*)|(B.*A.*))?"); 
		decCon.put("co-existence","[^AB]*((A.*B.*)|(B.*A.*))?");
		
		return decCon;
	}
	
	public static HashMap<String, String> loadUnaryConstraintSet(boolean includeEndpoints){
		HashMap<String,String> decCon = new HashMap<String,String>();
		
		if(includeEndpoints){
			decCon.put("last",".*A"); 
			//decCon.put("init","(A.*)?"); 
			decCon.put("init","A.*"); 
		}
		
		decCon.put("existence",".*(A.*){1}"); 
		decCon.put("existence2",".*(A.*){2}"); 
		decCon.put("existence3",".*(A.*){3}"); 
		
		decCon.put("absence1","[^A]*(A?[^A]*){0}"); 
		decCon.put("absence2","[^A]*(A?[^A]*){1}"); 
		decCon.put("absence3","[^A]*(A?[^A]*){2}"); 
		
		decCon.put("exactly1","[^A]*A[^A]*"); 
		decCon.put("exactly2","[^A]*A[^A]*A[^A]*"); 
		decCon.put("exactly3","[^A]*A[^A]*A[^A]*A[^A]*"); 		
		return decCon;
	}
		
	public static Automaton regexToAutomaton(Collection<?> alphabet, String regex, Object A, Object B){
		String alpha = alphabetToString(alphabet);
				
//		A= "("+A+")";
//		B= "("+B+")";
		
		//System.out.println("Regex - "+regex);			
		regex = regex.replace("A", ""+A);
		regex = regex.replace("B", ""+B);
		//System.out.println("Regex - "+regex);		
		
		regex = regex.replace("^"+A+B, alpha.replace(""+A, "").replace(""+B, ""));
		//System.out.println("Regex - "+regex);
		regex = regex.replace("^"+A, alpha.replace(""+A, ""));
		//System.out.println("Regex - "+regex);
		regex = regex.replace("^"+B, alpha.replace(""+B, ""));
		//System.out.println("Regex - "+regex);		
		regex = regex.replace(".", "["+alpha+"]");
		//System.out.println("Regex: "+regex);
		
		RegExp r = new RegExp(regex);			
		return r.toAutomaton();
	}
	
	public static String alphabetToString(Collection<?> hs){
		String s ="(";		
		for(Object a: hs)
			s = s+a+"|";
		s = s.substring(0, s.length()-1);
		s += ")";
		return s;
	}	
	
}
