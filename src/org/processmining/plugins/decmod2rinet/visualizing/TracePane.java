package org.processmining.plugins.decmod2rinet.visualizing;

/** Author: Johannes De Smedt
 *  E-mail: johannes.desmedt@kuleuven.be
 *  October 2015
 */

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TracePane extends JPanel {

	private static final long serialVersionUID = 7296759288208140407L;

	private JTextArea textarea;
	
	public TracePane(EvaluationVisualizator evaluationVisualizator, Integer option) {
		this.setLayout(new BorderLayout());
		textarea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(textarea);
		if(option>0)
			this.add(scrollPane, BorderLayout.CENTER);
		this.setPreferredSize(new Dimension(300,500));
	}

	public String getText() {
		return textarea.getText();
	}
	
	public void setText(String text) {
		this.textarea.setText(text);
	}
}
