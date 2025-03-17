package org.processmining.plugins.declareminer.visualizing;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

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

public class ModelPropertiesPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// components
	private final JTextField name = new JTextField("");

	public ModelPropertiesPanel() {
		super(new BorderLayout());

		add(new JLabel("name"), BorderLayout.WEST);
		add(name, BorderLayout.CENTER);

	}

	/**
	 * getRoleName
	 * 
	 * @return String
	 */
	public String getModelName() {
		return name.getText();
	}

	/**
	 * setRoleName
	 * 
	 * @param name
	 *            String
	 */
	public void setModelName(String name) {
		this.name.setText(name);
	}

}
