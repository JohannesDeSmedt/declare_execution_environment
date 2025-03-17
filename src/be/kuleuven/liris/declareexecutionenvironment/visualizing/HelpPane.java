package be.kuleuven.liris.declareexecutionenvironment.visualizing;

/** Author: Johannes De Smedt
 *  E-mail: johannes.desmedt@kuleuven.be
 *  October 2015
 */

import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class HelpPane extends JPanel {

	private static final long serialVersionUID = 7296759288208140407L;

//	private JScrollPane scrollPane;
//	private JScrollPane scrollPane2;
//	private JScrollPane scrollPane3;
//	private JList tList3;
//	private JList tList;
//	private DefaultListModel<String> model;
//	private DefaultListModel<String> model2;
//	private DefaultListModel<String> model3;
//	private EvaluationVisualizator vis;
	
	public HelpPane(HashMap<String,String> descriptions) {
		JFrame popUp = new JFrame("Constraint descriptions");
		popUp.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));

			
		for(String s: descriptions.keySet()){
			JLabel label = new JLabel("<html>"+s+" : "+descriptions.get(s)+"\n</html>");
			labelPanel.add(label);
			JLabel space = new JLabel("");
			labelPanel.add(space);
		}
		
		popUp.add(labelPanel);		
		popUp.setVisible(true);
		popUp.pack();		
	}		
}
