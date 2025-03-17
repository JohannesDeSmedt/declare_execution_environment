package org.processmining.plugins.declare.visualizing;

/**
 * 
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
public class Activity extends ActivityDefinition {

	/**
	 * Activity
	 * 
	 * @param activityDefinition
	 *            ActivityDefinition
	 * @param anAssignment
	 *            Assignment
	 */
	public Activity(ActivityDefinition activityDefinition, Assignment anAssignment) {
		super(anAssignment, activityDefinition);

	}

	public Assignment getAssignment() {
		return (Assignment) super.getAssignmentModel();
	}

	/**
	 * clone
	 * 
	 * @return Object
	 */
	public Object clone() {
		Activity clone = new Activity(this, getAssignment());
		return clone;
	}

}
