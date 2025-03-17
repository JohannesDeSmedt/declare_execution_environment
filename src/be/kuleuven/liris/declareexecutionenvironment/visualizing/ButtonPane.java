package be.kuleuven.liris.declareexecutionenvironment.visualizing;

/** Author: Johannes De Smedt
 *  E-mail: johannes.desmedt@kuleuven.be
 *  October 2015
 */

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;

import org.processmining.models.semantics.IllegalTransitionException;

import be.kuleuven.liris.declareexecutionenvironment.activity.Activity;
import be.kuleuven.liris.declareexecutionenvironment.dependencystructure.ConstraintDependencyStructure;

public class ButtonPane extends JPanel {

	private static final long serialVersionUID = 7296759288208140407L;

	private JScrollPane scrollPane;
	private JScrollPane scrollPane2;
	private JScrollPane scrollPane3;
	private JList<String> tList3;
	private DefaultListModel<Activity> model;
	private DefaultListModel<String> model2;
	private DefaultListModel<String> model3;
	private ExecutionVisualizator vis;
	
	private final Font regularFont = new Font("Helvetica", Font.ROMAN_BASELINE, 12);
	private final Font bigFont = new Font("Helvetica", Font.ROMAN_BASELINE, 24);
	
	public ButtonPane(final ExecutionVisualizator evaluationVisualizator, boolean larger) {
		this.vis = evaluationVisualizator;
		
		 // List of dependency structures
        model3 = new DefaultListModel<String>();
        tList3 = new JList<String>(model3);
                
        tList3.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane3 = new JScrollPane(tList3);
        
        if(larger)
        	scrollPane3.setPreferredSize(new Dimension(1000,340));
        else
        	scrollPane3.setPreferredSize(new Dimension(500,170));

		
        // List of explanations
        model2 = new DefaultListModel<String>();
        final JList<String> tList2 = new JList<String>(model2);
                
        tList2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane2 = new JScrollPane(tList2);
        if(larger)
        	scrollPane2.setPreferredSize(new Dimension(1000,340));
        else
        	scrollPane2.setPreferredSize(new Dimension(500,170));
     	
		// List of enabled activities
		model = new DefaultListModel<Activity>();
        final JList<Activity> tList = new JList<Activity>(model);
        if(larger)
        	tList.setPreferredSize(new Dimension(200,340));
        else
        	tList.setPreferredSize(new Dimension(100,170));
        
        tList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane = new JScrollPane(tList);
       
        
        JSplitPane main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane3, scrollPane);        
        JSplitPane main2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane2, main);
        this.add(main2);
        
        
        // Buttons
		JButton button = new JButton("Execute");
		
		button.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	           	try {
	           		Activity move = (Activity) tList.getSelectedValue();
					//vis.extendMovements("execute("+move+");");
					vis.execute(move);
					//vis.colorDecMap();
				} catch (IllegalTransitionException e1) {
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
		
		JButton openPN = new JButton("Show Petri net");
		
		openPN.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	           	JFrame pnFrame = new JFrame();	           		
				pnFrame.add(evaluationVisualizator.petriPane);
				pnFrame.pack();
				pnFrame.setVisible(true);
	         }          
	      });
		
		if(larger){
			button.setFont(bigFont);
			reset.setFont(bigFont);
			help.setFont(bigFont);
			openPN.setFont(bigFont);
	        tList.setFont(bigFont);
	        tList2.setFont(bigFont);
	        tList3.setFont(bigFont);
		} else{
			button.setFont(regularFont);
			reset.setFont(regularFont);
			help.setFont(regularFont);
			openPN.setFont(regularFont);
			tList.setFont(regularFont);
			tList2.setFont(regularFont);
			tList.setFont(regularFont);
		}
			
		JPanel panel = new JPanel(new GridLayout(4, 1, 5, 5));
		
		panel.add(button);
		panel.add(reset);
		panel.add(help);
		panel.add(openPN);
		
		this.add(panel);		
	}


	public void updateDSScrollArea(ArrayList<ConstraintDependencyStructure> ds){
		System.out.println("Let's display some");		
		model3.removeAllElements();
		for(ConstraintDependencyStructure d: ds){
			model3.addElement(d.getCaption());			
		}
		
		tList3.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent evt) {
		        JList<?> list = (JList<?>)evt.getSource();
		        int index = list.locationToIndex(evt.getPoint());
		        if (evt.getClickCount()==1) {
		            // Double-click detected		            
		            vis.extendMovements("visualize_ds--"+model3.get(index)+"--;");
		    		vis.visualizeDependencyGraph(index,true);	
		        } 
		    }
		});
	}
	
	public void updateExScrollArea(HashSet<String> trans){
		model2.removeAllElements();
		for(String s: trans){
			model2.addElement(s);
		}
	}
	
	public void updateScrollArea(Collection<Activity> activities){
		model.removeAllElements();
		for(Activity a: vis.semantics.getEnabledActivities()){
			if(a.isEnabled())
				model.addElement(a);
		}
	}
	
}
