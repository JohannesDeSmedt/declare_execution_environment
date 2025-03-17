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
public class ConstraintGroup extends Base {

	private String name; // a short name for this group of optional violations.
	private String description; // description of this group of optional violations.

	public ConstraintGroup(int id) {
		super(id);
		setName("");
		setDescription("");
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String toString() {
		return name;
	}

	public Object clone() {
		ConstraintGroup clone = new ConstraintGroup(getId());
		clone.setName(name);
		clone.setDescription(getDescription());
		return clone;
	}
}
