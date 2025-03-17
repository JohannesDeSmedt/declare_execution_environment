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
public class ConstraintConnector extends TransparentCell {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3706488901967995628L;

	public ConstraintConnector(ConstraintDefinition constraint) {
		super(constraint);
		addPort();
	}
}
