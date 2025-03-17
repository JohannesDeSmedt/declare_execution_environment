package org.processmining.plugins.declareminer.visualizing;

/**
 * @author michael
 * 
 */
public class DeclareModel {

	private final transient AssignmentModel model;
	private final transient AssignmentModelView view;

	/**
	 * @param model
	 * @param view
	 */
	public DeclareModel(final AssignmentModel model, final AssignmentModelView view) {
		this.model = model;
		this.view = view;
	}

	/**
	 * @return
	 */
	public AssignmentModel getModel() {
		return model;
	}

	/**
	 * @return
	 */
	public AssignmentModelView getView() {
		return view;
	}

}
