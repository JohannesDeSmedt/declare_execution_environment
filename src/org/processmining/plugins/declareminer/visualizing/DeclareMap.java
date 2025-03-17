package org.processmining.plugins.declareminer.visualizing;

import org.processmining.plugins.decmod2rinet.AnnotatedDeclareMapGraph;
import org.processmining.plugins.decmod2rinet.visualizing.EvaluationVisualizator;

/**
 * @author michael
 * 
 */
public class DeclareMap {

	private final transient AssignmentModel model;
	private final transient AssignmentModelView view;
	private final transient AssignmentViewBroker broker;
	private final transient org.processmining.plugins.declare.visualizing.AssignmentModel modelCh;
	private final transient org.processmining.plugins.declare.visualizing.AssignmentModelView viewCh;
	private final transient org.processmining.plugins.declare.visualizing.AssignmentViewBroker brokerCh;
	

	/**
	 * @param model
	 * @param view
	 */
	public DeclareMap(final AssignmentModel model, final org.processmining.plugins.declare.visualizing.AssignmentModel modelCh,  final AssignmentModelView view, final org.processmining.plugins.declare.visualizing.AssignmentModelView viewCh, final AssignmentViewBroker broker,final org.processmining.plugins.declare.visualizing.AssignmentViewBroker brokerCh) {
		this.model = model;
		this.view = view;
		this.broker = broker;
		this.modelCh = modelCh;
		this.viewCh = viewCh;
		this.brokerCh = brokerCh;
	}

	/**
	 * @return

	 */
	
	public void setaDmg(AnnotatedDeclareMapGraph aDmgIn){
		viewCh.getGraph().setAdg(aDmgIn);
	}
	
	public AnnotatedDeclareMapGraph getaDmg(){
		return viewCh.getGraph().getAdg();
	}
	
	public void setEV(EvaluationVisualizator evIn){
		viewCh.getGraph().setEv(evIn);
	}
	
	public EvaluationVisualizator getEV(){
		return viewCh.getGraph().getEv();
	}
	
	public AssignmentModel getModel() {
		return model;
	}

	public AssignmentViewBroker getBroker() {
		return broker;
	}

	/**
	 * @return
	 */
	public AssignmentModelView getView() {
		return view;
	}

	public org.processmining.plugins.declare.visualizing.AssignmentModel getModelCh() {
		return modelCh;
	}

	public org.processmining.plugins.declare.visualizing.AssignmentModelView getViewCh() {
		return viewCh;
	}

	public org.processmining.plugins.declare.visualizing.AssignmentViewBroker getBrokerCh() {
		return brokerCh;
	}

	

}
