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

import java.awt.GridLayout;

public abstract class Palete extends DefaultPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -851774136448016779L;

	public Palete(String title) {
		super(null);
		setLayout(new GridLayout(3, 1));
	}

	/**
	 * buildInterface
	 * 
	 * @todo Implement this nl.tue.declare.appl.design.assignment.gui.Panel
	 *       method
	 */
	protected abstract void buildInterface();
}
