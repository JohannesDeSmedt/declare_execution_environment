package org.processmining.plugins.declareminer.visualizing;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

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
public class AssignmentPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9079802798769750273L;

	private final JTabbedPane tp = new JTabbedPane();

	private final ModelPanel work = new WorkPanel();

	public AssignmentPanel() {
		super();
		setLayout(new BorderLayout());
		add(tp, BorderLayout.CENTER);
	}

	public void set(boolean work, boolean team, boolean data) {
		if (work) {
			tp.add("work", this.work);
		}

		if (data) {

		}
	}

	/**
	 * preview
	 * 
	 * @param com
	 *            Component
	 */
	public void preview(Component com) {
		if (work instanceof WorkPanel) {
			((WorkPanel) work).preview(com);
		}
	}

}
