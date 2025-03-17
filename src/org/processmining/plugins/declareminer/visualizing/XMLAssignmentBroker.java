package org.processmining.plugins.declareminer.visualizing;

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
public class XMLAssignmentBroker extends XMLBroker implements AssignmentBroker {

	private final AssignmentElementFactory factory;

	public XMLAssignmentBroker(String aConnectionString, String name) {
		super(aConnectionString, name);
		factory = new AssignmentElementFactory(this);
	}

	/**
	 * 
	 * @param model
	 *            AssignmentModel
	 * @param path
	 *            String
	 * @todo Implement this nl.tue.declare.datamanagement.AssignmentBroker
	 *       method
	 */
	public void addAssignment(AssignmentModel model) {
		Element newAssignment = factory.createAssignmentElement(model);

		Element root = getAssignmentElement();
		root.appendChild(newAssignment);
		writeDocument();
	}

	/**
	 * 
	 * @return AssignmentModel
	 * @todo Implement this nl.tue.declare.datamanagement.AssignmentBroker
	 *       method
	 * @param path
	 *            String
	 */
	public AssignmentModel readAssignment( /* String path */) {
		readDocument( /* path */);
		Element root = getDocumentRoot();
		AssignmentModel model = factory.elementToAssignmentModel(root);
		return model;
	}

	/**
	 * 
	 * @param documentString
	 *            Node
	 * @return AssignmentModel
	 */
	public AssignmentModel readAssignmentfromString(String documentString) {
		readDocumentString(documentString);
		Element root = getDocumentRoot();
		AssignmentModel model = factory.elementToAssignmentModel(root);
		return model;
	}

	/**
	 * getAssignmentElement
	 * 
	 * @return Element
	 */
	public Element getAssignmentElement() {
		return getDocumentRoot();
	}
}
