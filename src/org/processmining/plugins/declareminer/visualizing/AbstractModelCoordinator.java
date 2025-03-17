package org.processmining.plugins.declareminer.visualizing;

import java.io.File;

import javax.swing.JFrame;

/**
 * <p>
 * Title: DECLARE
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: TU/e
 * </p>
 * 
 * @author Maja Pesic
 * @version 1.0
 */
public class AbstractModelCoordinator {

	protected JFrame mainFrame = null;

	protected AssignmentPanel panel;

	protected AssignmentModel model;

	private File file; // store the path of the file where the model is saved

	public AbstractModelCoordinator(final JFrame aMainFrame, final AssignmentModel aModel, final boolean work,
			final boolean team, final boolean data) {
		this(aModel);
		setUp(aMainFrame, work, team, data);
	}

	public AbstractModelCoordinator(final AssignmentModel aModel) {
		super();
		panel = new AssignmentPanel();
		model = aModel;
		file = null;
	}

	public AssignmentPanel getPanel() {
		return panel;
	}

	protected void setUp(final JFrame aMainFrame, final boolean work, final boolean team, final boolean data) {
		mainFrame = aMainFrame;
		panel.set(work, team, data);

	}

	/**
	 * setFilePath
	 * 
	 * @param path
	 *            String
	 */
	public void setFilePath(final String path) {
		file = new File(path);
	}

	/**
	 * @return String
	 */
	public String getFilePath() {
		if (file == null) {
			return null;
		}
		return file.getAbsolutePath();
	}

	/**
	 * verify
	 * 
	 * @return boolean
	 */

}
