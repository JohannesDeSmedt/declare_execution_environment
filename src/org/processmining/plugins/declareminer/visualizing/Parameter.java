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

public class Parameter extends Base {

	private String name;
	private boolean branchable;
	private LineStyle line;

	/**
	 * 
	 * @param aConstraintDefintion
	 *            ConstraintDefinition
	 * @param aFormalParameter
	 *            FormalParameter
	 */
	public Parameter(int aId, String name) {

		super(aId);
		setName(name);
		line = new LineStyle();
	}

	public int hasCode() {
		return name.hashCode();
	}

	/**
	 * getName
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}

	public LineStyle getStyle() {
		return line;
	}

	/**
	 * toString
	 * 
	 * @return String
	 */
	public String toString() {
		return getName();
	}

	/**
	 * equals
	 * 
	 * @param anObject
	 *            Object
	 * @return boolean
	 */
	public boolean equals(Object anObject) {
		boolean eql = false;
		if (anObject != null) {
			if (anObject instanceof Parameter) {
				Parameter attr = (Parameter) anObject;
				eql = (attr.getName().equals(getName()));
			}
		}
		return eql;
	}

	public boolean isBranchable() {
		return branchable;
	}

	public void setBranchable(boolean branchable) {
		this.branchable = branchable;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * clone
	 * 
	 * @return Object
	 */
	public Object clone() {
		Parameter myClone = new Parameter(getId(), new String(name));
		myClone.setBranchable(branchable);
		myClone.line = (LineStyle) line.clone();
		return myClone;
	}
}
