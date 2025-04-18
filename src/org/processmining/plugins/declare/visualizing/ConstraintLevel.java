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

public class ConstraintLevel {

	private ConstraintGroup group; // the group of constraints. e.g. "Billing policy"
	private int level; // the priority of the constraint.
	private String message; // special message to be displayed to the user when warning about breaking the constraint

	/**
	 * 
	 * @param group
	 *            ConstraintGroup
	 */
	public ConstraintLevel(ConstraintGroup group) {
		super();
		setGroup(group);
		setLevel(0);
		setMessage("");
	}

	/**
	 * 
	 * @param priority
	 *            int
	 */
	public void setLevel(int priority) {
		level = priority;
	}

	/**
	 * 
	 * @param message
	 *            String
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 
	 * @return int
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * 
	 * @return String
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * 
	 * @param group
	 *            ConstraintGroup
	 */
	public void setGroup(ConstraintGroup group) {
		this.group = (ConstraintGroup) group.clone();
	}

	/**
	 * 
	 * @return ConstraintGroup
	 */
	public ConstraintGroup getGroup() {
		return group;
	}
}
