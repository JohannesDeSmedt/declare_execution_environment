package org.processmining.plugins.declare.visualizing;

import org.w3c.dom.Element;

/**
 * <p>
 * Title: DECLARE
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: TU/e
 * </p>
 * 
 * @author Maja Pesic
 * @version 1.0
 */
public class XMLAssignmentViewBroker extends XMLAssignmentBroker implements AssignmentViewBroker {

	private final AssignmentViewElementFactory2 factory;

	public XMLAssignmentViewBroker(String aConnectionString, String name) {
		super(aConnectionString, name);
		factory = new AssignmentViewElementFactory2(this);
	}

	public void addAssignmentAndView(AssignmentModel model, AssignmentModelView view) {
		Element newAssignment = factory.createAssignmentElement(model, view);
		Element root = getAssignmentElement();
		root.appendChild(newAssignment);
		writeDocument();
	}

	public Element assignmentAndViewElement(AssignmentModel model, AssignmentModelView view) {
		Element newAssignment = factory.createAssignmentElement(model, view);
		Element root = getAssignmentElement();
		root.appendChild(newAssignment);
		return root;
	}

	public Element getAssignmentAndView() {
		readDocument();
		Element root = getDocumentRoot();
		return root;
	}

	public void readAssignmentGraphical(AssignmentModel model, AssignmentModelView view) {
		readDocument();
		Element root = getDocumentRoot();
		try {
			factory.elementToAssignmentGraphical(view, model, root);
		} catch (Exception e) { // older versions of models must be read by old classes
			AssignmentViewElementFactory temp = new AssignmentViewElementFactory(this);
			temp.elementToAssignmentGraphical(view, model, root);
		}
	}

	public void readAssignmentGraphicalFromString(AssignmentModel model, AssignmentModelView view, String documentString) {
		readDocumentString(documentString);
		Element root = getDocumentRoot();
		try {
			factory.elementToAssignmentGraphical(view, model, root);
		} catch (Exception e) { // older versions of models must be read by old classes
			AssignmentViewElementFactory temp = new AssignmentViewElementFactory(this);
			temp.elementToAssignmentGraphical(view, model, root);
		}
	}
}
