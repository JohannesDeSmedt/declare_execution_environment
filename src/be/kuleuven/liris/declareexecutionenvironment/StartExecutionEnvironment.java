package be.kuleuven.liris.declareexecutionenvironment;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.OptionalDataException;
import java.util.concurrent.ExecutionException;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.processmining.framework.connections.ConnectionCannotBeObtained;
import org.processmining.models.semantics.IllegalTransitionException;

import be.kuleuven.liris.declareexecutionenvironment.model.AnnotatedDeclareModel;
import be.kuleuven.liris.declareexecutionenvironment.model.DeclareModel;
import be.kuleuven.liris.declareexecutionenvironment.visualizing.ExecutionVisualizator;
import be.kuleuven.liris.ioutilities.IOHelp;

public class StartExecutionEnvironment {
	
	public static void main(String[] args) throws Exception {
		DeclareModel declareModel = IOHelp.loadDeclareModel();
		AnnotatedDeclareModel aDec = new AnnotatedDeclareModel(declareModel.getAssignmentViewModel());			
								
		/*
		 * GUI 
		 */
		final JFrame frame = new JFrame("Declare execution environment for model ");
		frame.add(convert(aDec, declareModel));
		frame.pack();
		frame.setVisible(true);
		System.out.println("Visible");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Declare execution environment");		
	}	
	
	private static JComponent convert(AnnotatedDeclareModel aDec, DeclareModel model) throws IllegalTransitionException, OptionalDataException, InvalidClassException, ClassCastException, ClassNotFoundException, IOException, ConnectionCannotBeObtained, InterruptedException, ExecutionException {
		ExecutionVisualizator eV = new ExecutionVisualizator(model);
		return eV;		
	}
	
}