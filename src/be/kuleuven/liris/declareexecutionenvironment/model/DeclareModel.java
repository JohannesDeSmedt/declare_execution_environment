package be.kuleuven.liris.declareexecutionenvironment.model;

import org.processmining.plugins.declare.visualizing.AssignmentModel;
import org.processmining.plugins.declare.visualizing.AssignmentModelView;
import org.processmining.plugins.declare.visualizing.AssignmentViewBroker;

import be.kuleuven.liris.ioutilities.IOHelp;

public class DeclareModel {

	private AssignmentViewBroker broker;
	private AssignmentModel model;
	private AssignmentModelView view;
	private String path;
	
	public DeclareModel(AssignmentViewBroker broker, AssignmentModel model, AssignmentModelView view, String path){
		this.broker = broker;
		this.model = model;
		this.view = view;
		this.path = path;
	}
	
	public AssignmentViewBroker getAssignmentViewBroker(){
		return broker;
	}
	
	public AssignmentModel getAssignmentViewModel(){
		return model;
	}
	
	public AssignmentModelView getAssignmentModelView(){
		return view;
	}
	
	public DeclareModel reload() throws Exception{
		return IOHelp.loadDeclareModel(path);
	}
	
}
