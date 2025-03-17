package org.processmining.plugins.declareminer.visualizing;

import java.awt.Component;
import java.awt.event.ActionEvent;

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

public class FrmAssignmentModel extends DesignInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1207580453413544345L;
	private AssignmentPanel panel = null;

	protected FrmAssignmentModel(String title, AssignmentPanel panel) {
		super(title);
		this.panel = panel;
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param e
	 *            ActionEvent
	 * @todo Implement this java.awt.event.ActionListener method
	 */
	public void actionPerformed(ActionEvent e) {
	}

	/**
	 * newFrame
	 * 
	 * @param aCoordinator
	 *            ModelCoordinator
	 */
	public FrmAssignmentModel(AssignmentPanel panel) {
		this("", panel);
	}

	/**
	 * SetModelName
	 * 
	 * @param aName
	 *            String
	 */
	public void SetModelName(String aName) {
		setTitle(aName);
	}

	/**
	 * 
	 * @throws Exception
	 */
	protected void jbInit() throws Exception {
		setContentPane(panel);
	}

	/**
	 * preview
	 * 
	 * @param com
	 *            Component
	 */
	public void preview(Component com) {
		panel.preview(com);
	}

	public AssignmentPanel getPanel() {
		return panel;
	}
}
