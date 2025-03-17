package org.processmining.plugins.declareminer.visualizing;

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
public interface AssignmentBroker {
	/**
	 * addAssignment
	 * 
	 * @param model
	 *            AssignmentModel
	 * @param path
	 *            String
	 */
	public void addAssignment(AssignmentModel model);

	/**
	 * readAssignment
	 * 
	 * @return AssignmentModel
	 * @param path
	 *            String
	 */
	public AssignmentModel readAssignment();

	public boolean writeDocument();

}
