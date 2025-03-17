package org.processmining.plugins.declare.visualizing;

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
public class Control {

	private static Control instance = null;

	private final ConstraintTemplateControl constraintTemplate;
	private final AssignmentModelControl assignmentModel;

	private Control() {
		super();
		constraintTemplate = ConstraintTemplateControl.singleton();

		assignmentModel = AssignmentModelControl.singleton();
	}

	public ConstraintTemplateControl getConstraintTemplate() {
		return constraintTemplate;
	}

	public AssignmentModelControl getAssignmentModel() {
		return assignmentModel;
	}

	public static Control singleton() {
		if (instance == null) {
			instance = new Control();
		}
		return instance;
	}
}
