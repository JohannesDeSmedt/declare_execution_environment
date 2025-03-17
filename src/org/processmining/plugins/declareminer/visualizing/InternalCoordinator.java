package org.processmining.plugins.declareminer.visualizing;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;

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
public abstract class InternalCoordinator {

	protected JFrame mainFrame = null;

	protected InternalCoordinator(JFrame aMainFrame) {
		mainFrame = aMainFrame;
	}

	public abstract JInternalFrame getInternalFrame();

	public abstract void start();

	public abstract void end();

	protected Control getControl() {
		return Control.singleton();
	}

}
