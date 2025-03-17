package org.processmining.plugins.declareminer.visualizing;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;

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
public class ModelCoordinator extends AbstractModelCoordinator {

	protected FrmAssignmentModel frame;

	protected ModelCoordinator(final JFrame aMainFrame, final AssignmentModel aModel) {
		super(aMainFrame, aModel, true, true, true);
		frame = new FrmAssignmentModel(panel);
		frame.SetModelName(aModel.getName());
	}

	/**
	 * end
	 * 
	 * @todo Implement this
	 *       nl.tue.declare.appl.design.coordinate.InternalCoordinator method
	 */
	public void end() {
	}

	/**
	 * getInternalFrame
	 * 
	 * @return JInternalFrame
	 */
	public JInternalFrame getInternalFrame() {
		return frame;
	}

	/**
	 * start
	 * 
	 * @todo Implement this
	 *       nl.tue.declare.appl.design.coordinate.InternalCoordinator method
	 */
	public void start() {
		frame.maximize();
	}

	/**
	 * active
	 * 
	 * @param aFrame
	 *            FrmAssignmentModel
	 * @return boolean
	 */
	public boolean isActive(final JInternalFrame aFrame) {
		return aFrame == frame;
	}

	/**
	 * propertiesChanged
	 */
	public void propertiesChanged() {
		writeModelNameOnFrame();
	}

	private void writeModelNameOnFrame() {
		String path = getFilePath();
		if (path == null) {
			path = "UNSAVED";
		}
		frame.SetModelName(path + " - " + model.getName());
	}

	/**
	 * setFilePath
	 * 
	 * @param path
	 *            String
	 */
	@Override
	public void setFilePath(final String path) {
		super.setFilePath(path);
		writeModelNameOnFrame();
	}

}
