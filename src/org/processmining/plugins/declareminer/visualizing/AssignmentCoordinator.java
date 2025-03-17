package org.processmining.plugins.declareminer.visualizing;

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

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

public class AssignmentCoordinator implements InternalFrameListener {

	AssignmentCoordinatorListener listener;

	private final JFrame mainFrame;
	JInternalFrame frame;

	List<ModelCoordinator> coordinators;
	ModelCoordinator active;

	// dialog for add/edit Role

	public AssignmentCoordinator(final JFrame aMainFrame) {
		super();
		mainFrame = aMainFrame;
		coordinators = new ArrayList<ModelCoordinator>();
		listener = null;
		active = null;

	}

	public JInternalFrame getFrame() {
		return frame;
	}

	public void init() {
		internalFrameDeactivated(null);
	}

	public void addListener(final AssignmentCoordinatorListener list) {
		listener = list;
	}

	/**
	 * newModel
	 * 
	 * @return JInternalFrame
	 */

	private JInternalFrame startCoordinator(final ModelCoordinator coordinator) {
		coordinators.add(coordinator);
		frame = coordinator.getInternalFrame();
		frame.addInternalFrameListener(this);
		return frame;
	}

	/**
	 * getModelCoordinator
	 * 
	 * @param frame
	 *            JInternalFrame
	 * @return ModelCoordinator
	 */
	public ModelCoordinator getModelCoordinator(final JInternalFrame frame) {
		ModelCoordinator coordinator = null;
		int i = 0;
		boolean found = false;
		while (!found && (i < coordinators.size())) {
			coordinator = coordinators.get(i++);
			found = coordinator.isActive(frame);
		}
		return found ? coordinator : null;
	}

	/**
	 * activateModel
	 * 
	 * @param frame
	 *            JInternalFrame
	 */
	public void activateModel(final JInternalFrame frame) {
		final ModelCoordinator coordinator = getModelCoordinator(frame);
		coordinator.start();
	}

	public void internalFrameDeactivated(final InternalFrameEvent e) {
		if (listener != null) {
			if (e == null) {
				listener.deactivated(null);
			} else {
				listener.deactivated(e.getInternalFrame());
			}
		}
	}

	public void internalFrameActivated(final InternalFrameEvent e) {
		if (e != null) {
			if (listener != null) {
				listener.activated(e.getInternalFrame());
			}
			// get the activated model coordinator
			active = getModelCoordinator(e.getInternalFrame());
		}
	}

	public void internalFrameDeiconified(final InternalFrameEvent e) {
	}

	public void internalFrameIconified(final InternalFrameEvent e) {
	}

	public void internalFrameClosed(final InternalFrameEvent e) {
		final Object source = e.getSource();
		if (source instanceof JInternalFrame) {
			final JInternalFrame frame = (JInternalFrame) source;
			final ModelCoordinator coordinator = getModelCoordinator(frame);
			if (coordinator != null) {
				// removed 04-07-2006 coordinator.onClosed();
				coordinators.remove(coordinator);
				if (listener != null) {
					listener.closed(frame);
				}
			}
		}
	}

	public void internalFrameClosing(final InternalFrameEvent e) {
	}

	public void internalFrameOpened(final InternalFrameEvent e) {
	}

	/**
	 * save
	 */
	public void save() {
		// XMLFileDialog dialog = new XMLFileDialog();
		// String file = dialog.saveFile(this.frame);
		final String file = active.getFilePath();
		if (file != null) {
			saveAssignmentModel(file);
			/*
			 * if (active != null)
			 * this.getControl().getAssignmentModel().addAssignmentModelAndView
			 * (active.model, active.workCoordinator.modelView, file);
			 */
		} else {
			saveAs();
		}
	}

	/**
	 * saveAs
	 */
	public void saveAs() {
		// create a dilaog for XML files
		final XMLFileDialog dialog = new XMLFileDialog();
		// open a dilaog for saving XML files with this frame as a parent frame
		// store the path of the seelcted file
		final String file = dialog.saveFile(frame);
		// if the path is selected
		if (file != null) {
			saveAssignmentModel(file);
			/*
			 * if (active != null) {
			 * this.getControl().getAssignmentModel().addAssignmentModelAndView
			 * (active. model, active.workCoordinator.modelView, file); }
			 */
		}
	}

	private void saveAssignmentModel(final String file) {
	}
	/**
  *
  */

	/**
	 * verify
	 */

}
