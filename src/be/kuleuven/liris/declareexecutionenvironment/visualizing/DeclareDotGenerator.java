package be.kuleuven.liris.declareexecutionenvironment.visualizing;
import java.util.*;

import be.kuleuven.liris.declareexecutionenvironment.ExecutionStarter;
import be.kuleuven.liris.declareexecutionenvironment.activity.Activity;
import be.kuleuven.liris.declareexecutionenvironment.constraint.Constraint;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.Binary;
import be.kuleuven.liris.declareexecutionenvironment.constraint.unary.Unary;
import be.kuleuven.liris.declareexecutionenvironment.model.AnnotatedDeclareModel;

public class DeclareDotGenerator {
    private final Set<String> activities = new HashSet<>();
    private final Map<String, String> unaryConstraints = new HashMap<>();


    public static String generateDot(AnnotatedDeclareModel aMod) {
        StringBuilder dot = new StringBuilder();
        dot.append("digraph DeclareModel {\n");
        dot.append("  rankdir=TB;\n");
        dot.append("  node [shape=box, style=filled, fillcolor=lightgray];\n");

        for(Activity a: aMod.getActivities(true)) 
        	dot.append(String.format("  \"%s\";\n", a.getName()));
//            dot.append(String.format("    \"%s_%s\" [shape=house, label=\"%s\", fillcolor=orange];\n", a.getName(), a.getUnaries(), ""));
//        dot.append("  }\n");
        
//        // Subgraph for unary constraints (houses)
//        dot.append("  { rank=same;\n");
//        for (String activity : activities) {
//        	activity
//            if (unaryConstraints.containsKey(activity)) {
//                String constraint = unaryConstraints.get(activity);
//                String label = constraint.equals("Init") ? "🏠 Init" : "🏠 Last";
//                dot.append(String.format("    \"%s_%s\" [shape=house, label=\"%s\", fillcolor=orange];\n", activity, constraint, label));
//            }
//        }
//        dot.append("  }\n");
        
        for(Constraint c: aMod.getConstraints()) {
        	if(!(c instanceof Unary)) {
        		Binary b = (Binary) c;
        		String style = getEdgeStyle(b);
        		dot.append(String.format("  \"%s\" -> \"%s\" [label= \"" + c.getName() + "\", %s];\n", b.getAntecedent(), b.getConsequent(), style));
        	}
        }

//        // Add activities
//        for (String activity : activities) {
//            dot.append(String.format("  \"%s\";\n", activity));
//        }

        dot.append("}\n");
        return dot.toString();
    }



    public static String getEdgeStyle(Constraint constraintType) {
        return switch (constraintType.getName()) {
            case "response" -> "arrowhead=normal";
            case "precedence" -> "arrowhead=odot,  arrowtail=normal, dir=both";
            case "alternate precedence" -> "label=<<TABLE BORDER=\"0\"><TR><TD>━━</TD></TR><TR><TD>━━</TD></TR></TABLE>>"; //arrowhead=dot, style=dashed, label=\"AltPrec\"";
            case "alternate succession" -> "arrowhead=normal, dir=both, label=\"AltSucc\"";
            case "chain precedence" -> "arrowhead=dot, style=bold";
            case "chain succession" -> "arrowhead=normal, style=bold";
            case "not succession" -> "arrowhead=tee, style=dashed";
            case "not co-existence" -> "arrowhead=tee, style=dashed, label=\"NotCoex\"";
            default -> "arrowhead=normal";
        };
    }


}
