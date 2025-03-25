package be.kuleuven.liris.declareexecutionenvironment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.processmining.framework.util.Pair;
import org.processmining.models.semantics.IllegalTransitionException;
import be.kuleuven.liris.declareexecutionenvironment.model.AnnotatedDeclareModel;
import be.kuleuven.liris.declareexecutionenvironment.model.StateSpaceDecMod;
import be.kuleuven.liris.declareexecutionenvironment.model.StateSpaceDecModFSA;
import be.kuleuven.liris.declareexecutionenvironment.testmodels.Model1;
import be.kuleuven.liris.declareexecutionenvironment.visualizing.DeclareDotGenerator;
import dk.brics.automaton.Automaton;

public class ExecutionStarter {

	public static void main(String[] args) throws IllegalTransitionException {
			
		Model1 m = new Model1();
		Pair<Automaton, AnnotatedDeclareModel> models = m.getModels();
		Automaton aut = models.getFirst();
		AnnotatedDeclareModel model = models.getSecond();
		
//        writeStringToFile(aut.toDot(), "graph.dot");
//        convertDotToPng("graph.dot", "graph.png");
				
        StateSpaceDecModFSA state_space = new StateSpaceDecModFSA(model);
        writeStringToFile(state_space.toDot(), "graphSS.dot");
        convertDotToPng("graphSS.dot", "graphSS.png");
        
		
        writeStringToFile(state_space.toClusteredModelDot(), "model_dot.dot");
        convertDotToPng("model_dot.dot", "model_dot.png");
        
        File g = new File("graph.dot"); 
        g.delete();
        File gs = new File("graphSS.dot"); 
//        gs.delete();
        File mm = new File("model_dot.dot"); 
        mm.delete();
	}
	
    public static void writeStringToFile(String content, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
            System.out.println("Successfully wrote DOT string to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void convertDotToPng(String dotFilePath, String pngFilePath) {
        try {
            // Create the command to execute
            ProcessBuilder processBuilder = new ProcessBuilder("dot", "-Tpng", dotFilePath, "-o", pngFilePath);
            processBuilder.redirectErrorStream(true);

            // Start the process
            Process process = processBuilder.start();

            // Wait for the process to complete
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("Successfully converted " + dotFilePath + " to " + pngFilePath);
            } else {
                System.err.println("Failed to convert " + dotFilePath + " to " + pngFilePath);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
