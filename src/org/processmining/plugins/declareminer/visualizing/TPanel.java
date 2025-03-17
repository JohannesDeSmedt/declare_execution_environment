package org.processmining.plugins.declareminer.visualizing;

import java.awt.LayoutManager;

import javax.swing.BorderFactory;

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
public class TPanel extends DefaultPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7809059177888585871L;

	public TPanel(LayoutManager manager) {
		super(manager);
	}

	public TPanel(LayoutManager manager, String title) {
		super(manager);
		setBorder(BorderFactory.createTitledBorder(title));
	}

	public void setTitle(String title) {
		setBorder(BorderFactory.createTitledBorder(title));
	}

	/**
	 * buildInterface
	 * 
	 * @todo Implement this nl.tue.declare.appl.util.swing.Panel method
	 */
	protected void buildInterface() {
		setBorder(BorderFactory.createTitledBorder(""));
	}
}
