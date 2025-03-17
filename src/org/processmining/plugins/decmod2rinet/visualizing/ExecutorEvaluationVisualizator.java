package org.processmining.plugins.decmod2rinet.visualizing;

/** Author: Johannes De Smedt
 *  E-mail: johannes.desmedt@kuleuven.be
 *  October 2015
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.semantics.IllegalTransitionException;
import org.processmining.plugins.declareminer.importing.DeclareModelVisualizerSecond;
import org.processmining.plugins.declareminer.visualizing.DeclareMap;
import org.processmining.plugins.decmod2rinet.PetrinetBuilder;

public class ExecutorEvaluationVisualizator extends EvaluationVisualizator{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ExecutorEvaluationVisualizator(PetrinetBuilder pn, DeclareMap map2, String option) throws IllegalTransitionException{
		super(pn, map2, option);
		
		this.petriPane = new PetrinetPane(this);
		this.buttonPane = new ButtonPane(this, this.option);
		this.tracePane = new DynTracePane(this, this.option);
		
		decMap.setEV(this);
		
		DeclareModelVisualizerSecond j = new DeclareModelVisualizerSecond();
		j.showLogVis(decMap);
		
		//this.setLayout(new BorderLayout());
		
		JSplitPane splitMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				j, petriPane);		
		splitMain.setPreferredSize(new Dimension(1024,600));
		
		//this.add(j,BorderLayout.NORTH);
		JSplitPane splitMainAgain = new JSplitPane(JSplitPane.VERTICAL_SPLIT, /*splitMain*/j, buttonPane);		
		//this.add(buttonPane,BorderLayout.CENTER);
		JSplitPane main = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitMainAgain, tracePane);
		//this.add(tracePane,BorderLayout.SOUTH);
		this.add(main,BorderLayout.SOUTH);
		
		
		
		for(int i=0;i<dp.size();i++){
			if(dp.get(i).isNS())
				dp.get(i).setCaption("Dependency structure for Not Succession "+dp.get(i).getT().getLabel()+" -> "+dp.get(i).getTs().getLabel());
			else if(dp.get(i).isUn()){
				dp.get(i).setCaption("Dependency structure for "+nodeLabels.get(dp.get(i).getUP()));
			}else if(dp.get(i).isEx())
				dp.get(i).setCaption("Dependency structure for exclusive choice, side of "+dp.get(i).getT());
		dsFrames.put(dp.get(i), new JFrame(dp.get(i).getCaption()));
		dsVisible.put(dp.get(i), false);
		}
		
		execute("empty");			
		this.buttonPane.updateDSScrollArea(dp);
		this.setPreferredSize(new Dimension(1400,800));		
	}	
}
