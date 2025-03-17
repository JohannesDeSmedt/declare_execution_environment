package org.processmining.plugins.decmod2rinet.visualizing;

/** Author: Johannes De Smedt
 *  E-mail: johannes.desmedt@kuleuven.be
 *  October 2015
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

import org.processmining.models.semantics.IllegalTransitionException;
import org.processmining.plugins.decmod2rinet.DependencyStructure;

public class ButtonPane extends JPanel {

	private static final long serialVersionUID = 7296759288208140407L;

	private JScrollPane scrollPane;
	private JScrollPane scrollPane2;
	private JScrollPane scrollPane3;
	private JList tList3;
	private JList tList;
	private DefaultListModel<String> model;
	private DefaultListModel<String> model2;
	private DefaultListModel<String> model3;
	private EvaluationVisualizator vis;
	
	public ButtonPane(EvaluationVisualizator evaluationVisualizator, Integer option) {
		this.vis = evaluationVisualizator;
		
		 // List of dependency structures
        model3 = new DefaultListModel<String>();
        tList3 = new JList(model3);
                
        tList3.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane3 = new JScrollPane(tList3);
        scrollPane3.setPreferredSize(new Dimension(500,170));

		
        // List of explanations
        model2 = new DefaultListModel<String>();
        final JList tList2 = new JList(model2);
                
        tList2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane2 = new JScrollPane(tList2);
        scrollPane2.setPreferredSize(new Dimension(500,170));
        
		
		// List of enabled activities
		model = new DefaultListModel<String>();
        final JList tList = new JList(model);
        tList.setPreferredSize(new Dimension(100,170));
        
        tList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane = new JScrollPane(tList);
        
        
        JSplitPane main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane3, scrollPane);        
        JSplitPane main2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane2, main);
        
        if(option>1)
        	this.add(main2, BorderLayout.NORTH);
        else if(option>0)
        	this.add(scrollPane);
        
        // Buttons
		JButton button = new JButton("Execute");
		
		button.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	           	try {
	           		String move = (String) tList.getSelectedValue();
					vis.extendMovements("execute("+move+");");
					vis.execute(move);
					//vis.colorDecMap();
				} catch (IllegalTransitionException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	         }          
	      });
		
		JButton reset = new JButton("Reset");
		
		reset.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	           	try {
					vis.reset();
					vis.extendMovements("reset;");
				} catch (IllegalTransitionException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	         }          
	      });
		
		JButton help = new JButton("Show help");
		
		help.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	           	vis.extendMovements("showHelp;");
	        	vis.showHelpPane();
	         }          
	      });
		
		JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
		if(option>0 && option<3){
			panel.add(button);
			panel.add(reset);
		}
		if(option<3)
			panel.add(help);
		this.add(panel);		
	}


	public void updateDSScrollArea(ArrayList<DependencyStructure> ds){
		System.out.println("Let's display some");		
		model3.removeAllElements();
		for(DependencyStructure d: ds){
			model3.addElement(d.getCaption());			
		}
		
		tList3.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent evt) {
		        JList list = (JList)evt.getSource();
		        int index = list.locationToIndex(evt.getPoint());
		        if (evt.getClickCount()==1) {
		            // Double-click detected		            
		            vis.extendMovements("visualize_ds--"+model3.get(index)+"--;");
		    		vis.visualizeDependencyGraph(index,true);	
		        } 
//		        else if (evt.getClickCount() == 3) {
//		            // Triple-click detected
//		            int index = list.locationToIndex(evt.getPoint());
//		        }
		    }
		});
	}
	
	public void updateExScrollArea(HashSet<String> trans){
		model2.removeAllElements();
		for(String s: trans){
			model2.addElement(s);
		}
	}
	
	public void updateScrollArea(HashSet<String> trans){
		model.removeAllElements();
		for(String s: trans){
			model.addElement(s);
		}
	}
	
}
