package org.processmining.plugins.decmod2rinet.visualizing;

import java.awt.Font;

/** Author: Johannes De Smedt
 *  E-mail: johannes.desmedt@kuleuven.be
 *  October 2015
 */

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import be.kuleuven.liris.declareexecutionenvironment.constraint.Constraint;
import be.kuleuven.liris.declareexecutionenvironment.model.AnnotatedDeclareModel;

public class HelpPane extends JPanel {

	private static final long serialVersionUID = 7296759288208140407L;

	public HelpPane(AnnotatedDeclareModel mod, boolean larger) {
		JFrame popUp = new JFrame("Constraint descriptions");
		popUp.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
			
		for(Constraint c: mod.getConstraints()){
			JLabel label = new JLabel("<html>"+c+" : "+c.getCD().getDescription()+"\n</html>");
			labelPanel.add(label);
			if(larger)
				label.setFont(new Font("Helvetica", Font.ROMAN_BASELINE, 24));
			else
				label.setFont(new Font("Helvetica", Font.ROMAN_BASELINE, 12));
			JLabel space = new JLabel("");
			labelPanel.add(space);
		}
		
		popUp.add(labelPanel);		
		popUp.setVisible(true);
		popUp.pack();		
	}		
}
