package org.processmining.plugins.declare.visualizing;

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
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class DesignerCoordinator implements AssignmentCoordinatorListener {

	private static final int KEY_QUIT = KeyEvent.VK_Q;
	private static final int KEY_OPEN_MODEL = KeyEvent.VK_O;
	private static final int KEY_SAVE_MODEL = KeyEvent.VK_S;
	private static final int KEY_MODEL_PROPERTIES = KeyEvent.VK_P;
	private static final int KEY_MODEL_EXPORT_IMAGE = KeyEvent.VK_I;
	private static final int KEY_MODEL_VERIFY = KeyEvent.VK_V;
	private static final int KEY_ORGANIZATION = KeyEvent.VK_R;
	private static final int KEY_TEMPLATES = KeyEvent.VK_C;

	// this coordinator handles all events with all assignment models
	private final AssignmentCoordinator assignmentCoordinator;

	/**
	 * mainFarme is the main frame for the Designer. Within mainFrame internal
	 * frames are open.
	 */
	private MainFrame mainFrame = null;

	private JDesktopPane desktop;

	private JMenuItem save, saveAs, properties, export, verify;

	final Control control;

	private final WindowMenu frames;

	public DesignerCoordinator() {
		control = Control.singleton();
		mainFrame = new MainFrame();
		frames = new WindowMenu();
		frames.addListener(new WindowMenuListener() {
			public void itemSelected(final JInternalFrame frame, final boolean active) {
				if (active) {
					maximize(frame);
				}
			}
		});
		assignmentCoordinator = new AssignmentCoordinator(mainFrame);
		assignmentCoordinator.addListener(this);
		start();
		assignmentCoordinator.init();
	}

	/**
	 * start
	 */
	private void start() {

		final boolean packFrame = false;

		// Set up the GUI.
		desktop = new JDesktopPane(); // a specialized layered pane
		mainFrame.setContentPane(desktop);
		mainFrame.setJMenuBar(createMenuBar());

		// Make dragging a little faster but perhaps uglier.
		desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

		// Validate frames that have preset sizes
		// Pack frames that have useful preferred size info, e.g. from their layout
		if (packFrame) {
			mainFrame.pack();
		} else {
			mainFrame.validate();
		}

		// Center the window
		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		final Dimension frameSize = mainFrame.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		mainFrame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		mainFrame.setVisible(true);
	}

	/**
	 * organization
	 */
	public void organization() {
	}

	/**
	 * maximize
	 * 
	 * @param aFrame
	 *            aFrame Maximize an internal frame aFrame.
	 */
	private void maximize(final JInternalFrame aFrame) {
		try {
			aFrame.setVisible(true);
			aFrame.setSelected(true);
			aFrame.setMaximum(true);
		} catch (final Exception e) {
		}
	}

	/**
	 * jMenuOrganizationRoles_actionPerformed
	 * 
	 * @param actionEvent
	 *            actionEvent actionEvent Execute this method when user selects
	 *            to work with "organization" Create the form for Organization
	 *            and start the organizationCoordinator
	 */

	/**
	 * jMenuAssignment_actionPerformed
	 * 
	 * @param actionEvent
	 *            actionEvent actionEvent Execute this method when user selects
	 *            to work with "assignment" Create the form for Organization and
	 *            start the organizationCoordinator
	 */

	/**
	 * frameClosed / /* public void frameClosed() { internalFrame = null; }
	 * 
	 * @param actionEvent
	 *            ActionEvent
	 */
	void childClose_actionPerformed(final ActionEvent actionEvent) {
	}

	private void setMenuKey(final JMenuItem menuItem, final int key) {
		menuItem.setMnemonic(key);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(key, ActionEvent.ALT_MASK));
	}

	protected JMenuBar createMenuBar() {
		final JMenuBar menuBar = new JMenuBar();

		// Set up the lone menu.
		final JMenu jMenuAssignment = new JMenu("Model");
		jMenuAssignment.setMnemonic(KeyEvent.VK_P);
		menuBar.add(jMenuAssignment);

		// Set up the menu item for new model

		// Set up the menu item for open model
		JMenuItem menuItem = new JMenuItem("Open");
		setMenuKey(menuItem, KEY_OPEN_MODEL);

		jMenuAssignment.add(menuItem);

		// Set up the menu item for save model
		save = new JMenuItem("Save");
		setMenuKey(save, KEY_SAVE_MODEL);
		save.setActionCommand("save");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				// jMenuSaveModel_actionPerformed(e);
				if (assignmentCoordinator != null) {
					assignmentCoordinator.save();
					frames.frameChanged(assignmentCoordinator.getFrame());
				}
			}
		});

		jMenuAssignment.add(save);

		// Set up the menu item for saveAs model
		saveAs = new JMenuItem("Save as");
		saveAs.setActionCommand("saveAs");
		saveAs.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				if (assignmentCoordinator != null) {
					assignmentCoordinator.saveAs();
					frames.frameChanged(assignmentCoordinator.getFrame());
				}
			}
		});

		jMenuAssignment.add(saveAs);

		// Set up the menu item for properties of the model
		properties = new JMenuItem("Properties");
		setMenuKey(properties, KEY_MODEL_PROPERTIES);
		properties.setActionCommand("properties");
		properties.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				if (assignmentCoordinator != null) {

					frames.frameChanged(assignmentCoordinator.getFrame());
				}
			}
		});

		jMenuAssignment.add(properties);

		// Set up the menu item for properties of the model

		jMenuAssignment.add(verify);

		// Set up the menu item image exporting
		export = exportItem();
		jMenuAssignment.add(export);

		jMenuAssignment.addSeparator();

		// Set up the second menu item.
		menuItem = new JMenuItem("Quit");
		setMenuKey(menuItem, KEY_QUIT);
		menuItem.setActionCommand("quit");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				System.exit(0);
			}
		});
		jMenuAssignment.add(menuItem);

		// setup the Design menu item
		final JMenu jMenuDesign = new JMenu();
		jMenuDesign.setText("System");
		menuBar.add(jMenuDesign);

		// setup the Organization menu item
		final JMenuItem jMenuOrganization = new JMenuItem();
		setMenuKey(jMenuOrganization, KEY_ORGANIZATION);
		jMenuOrganization.setText("Organization");
		jMenuOrganization.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {

			}
		});

		jMenuDesign.add(jMenuOrganization);

		// setup the ConstraintTemplate menu item
		final JMenuItem jMenuConstraintTemplate = new JMenuItem();
		setMenuKey(jMenuConstraintTemplate, KEY_TEMPLATES);
		jMenuConstraintTemplate.setText("Constraint templates");
		jMenuConstraintTemplate.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {

			}
		});

		jMenuDesign.add(jMenuConstraintTemplate);

		// setop window menu item
		menuBar.add(frames);

		// setup the Help menu item
		final JMenu jMenuHelp = new JMenu();
		jMenuHelp.setText("Help");
		menuBar.add(jMenuHelp);

		// setup the About menu item
		final JMenuItem jMenuHelpAbout = new JMenuItem();
		jMenuHelpAbout.setText("About");
		jMenuHelpAbout.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				jMenuHelpAbout_actionPerformed(e);
			}
		});

		jMenuHelp.add(jMenuHelpAbout);

		return menuBar;
	}

	private JMenu exportItem() {
		// Set up the menu item for new model
		final JMenu menu = new JMenu("Export");

		// Set up the menu item image exporting
		final JMenuItem exportLTL = new JMenuItem("As native LTL formula...");
		exportLTL.setActionCommand("export_ltl");

		// Set up the menu item image exporting
		final JMenuItem exportDOT = new JMenuItem("As automaton in DOT...");
		exportDOT.setActionCommand("export_dot");

		menu.add(exportDOT);

		// Set up the menu item image exporting
		final JMenuItem exportImage = new JMenuItem("As image...");
		setMenuKey(exportImage, KEY_MODEL_EXPORT_IMAGE);
		exportImage.setActionCommand("export_image");
		exportImage.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				if (assignmentCoordinator != null) {

				}
			}
		});
		menu.add(exportImage);

		return menu;
	}

	void jMenuHelpAbout_actionPerformed(final ActionEvent actionEvent) {
		final MainFrame_AboutBox dlg = new MainFrame_AboutBox(mainFrame);
		final Dimension dlgSize = dlg.getPreferredSize();
		final Dimension frmSize = mainFrame.getSize();
		final Point loc = mainFrame.getLocation();
		dlg.setLocation(((frmSize.width - dlgSize.width) / 2) + loc.x, ((frmSize.height - dlgSize.height) / 2) + loc.y);
		dlg.setModal(true);
		dlg.pack();
		dlg.setVisible(true);
	}

	private void setModelMenuItems(final boolean enabled) {
		save.setEnabled(enabled);
		saveAs.setEnabled(enabled);
		properties.setEnabled(enabled);
		verify.setEnabled(enabled);
		export.setEnabled(enabled);
	}

	public void deactivated(final JInternalFrame frame) {
		setModelMenuItems(false);
		frames.activate(false, frame);
	}

	public void activated(final JInternalFrame frame) {
		setModelMenuItems(true);
		frames.activate(true, frame);
	}

	public void closed(final JInternalFrame frame) {
		frames.remove(frame);
	}
}
