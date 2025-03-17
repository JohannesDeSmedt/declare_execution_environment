package org.processmining.plugins.decmod2rinet.visualizing;

/** Author: Johannes De Smedt
 *  E-mail: johannes.desmedt@kuleuven.be
 *  October 2015
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class DynTracePane extends JPanel {

	private static final long serialVersionUID = 4552315260112714479L;
	private EvaluationVisualizator evaluationVisualizator;
	private int selectedTraceIndex = -1;
	
	private ArrayList<String> steps;
	private final JList<String> stepList;
	
	public DynTracePane(final EvaluationVisualizator evaluationVisualizator, Integer option) {
		this.setLayout(new BorderLayout());
		
		this.evaluationVisualizator = evaluationVisualizator;
		this.stepList = new JList<String>();
		this.stepList.setLayoutOrientation(JList.HORIZONTAL_WRAP);  
		this.stepList.setVisibleRowCount(1);
		this.setPreferredSize(new Dimension(1200,68));
		this.steps = new ArrayList<String>();
		JScrollPane scroll = new JScrollPane(this.stepList,  
		                                JScrollPane.VERTICAL_SCROLLBAR_NEVER,  
		                                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		if(option>0)
			this.add(scroll);
		
		stepList.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = -2745441247826070962L;

			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {  
	            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);  
	            
	            c.setPreferredSize(new Dimension(50,50));
	            String text = steps.get(index);
				c.setBackground(Color.cyan);			
	            setText(text);
	            return c;  
	        }  
		});
		stepList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				System.out.println("Clicketyclick on number "+stepList.getSelectedIndex());
			}
			
		});
	}
	
	public void reset(){
		steps.clear();
		stepList.setListData(steps.toArray(new String[]{}));
	}
	
	public void extendTrace(String executedActivity) {
		steps.add(executedActivity);
		stepList.setListData(steps.toArray(new String[]{}));
	}

}
