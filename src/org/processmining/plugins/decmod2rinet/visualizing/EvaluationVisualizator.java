package org.processmining.plugins.decmod2rinet.visualizing;

/** Author: Johannes De Smedt
 *  E-mail: johannes.desmedt@kuleuven.be
 *  October 2015
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphModel;
import org.processmining.framework.util.Pair;
import org.processmining.models.graphbased.directed.DirectedGraphEdge;
import org.processmining.models.graphbased.directed.DirectedGraphNode;
import org.processmining.models.graphbased.directed.petrinet.ResetInhibitorNet;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.graphbased.directed.petrinet.impl.ResetInhibitorNetImpl;
import org.processmining.models.semantics.IllegalTransitionException;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.models.semantics.petrinet.impl.PetrinetSemanticsFactory;
import org.processmining.plugins.declare.visualizing.ActivityDefinitonCell;
import org.processmining.plugins.declare.visualizing.ConstraintDefinitionEdge;
import org.processmining.plugins.declare.visualizing.Parameter;
import org.processmining.plugins.declareminer.visualizing.ConstraintDefinition;
import org.processmining.plugins.declare.visualizing.DGraph;
import org.processmining.plugins.declareminer.importing.DeclareModelVisualizerSecond;
import org.processmining.plugins.declareminer.visualizing.DeclareMap;
import org.processmining.plugins.decmod2rinet.DependencyStructure;
import org.processmining.plugins.decmod2rinet.PetrinetBuilder;


import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;

import be.kuleuven.liris.declareexecutionenvironment.semantics.DeclareSemanticsImpl;
//import be.kuleuven.liris.declareexecutionenvironment.semantics.PetrinetSemanticsFactory;

public class EvaluationVisualizator extends JPanel {

	private static final long serialVersionUID = -4828826958007119206L;	
	protected PetrinetPane petriPane;
	protected ButtonPane buttonPane;	
	protected DynTracePane tracePane;
	private HelpPane helpPane;
	
	protected ResetInhibitorNetImpl riNet;
	protected static DeclareMap decMap;
	private Marking m;
	private static Marking iniM;	
	protected DeclareSemanticsImpl semantics;
	
	private static HashMap<ConstraintDefinition, Pair<HashSet<DirectedGraphNode>,HashSet<DirectedGraphEdge>>> constraintConstructs;
	private DGraph graph;
	
	private static Transition end;
	private HashSet<Transition> permDisable;

	public ArrayList<DependencyStructure> dp;
	private HashSet<DependencyStructure> iniDp;
	protected HashSet<String> ex;
	protected HashMap<DirectedGraphNode, String> nodeLabels;
	private HashMap<Transition, Object> transToNode;

	public HashMap<DependencyStructure,JFrame> dsFrames;
	public HashMap<DependencyStructure,Boolean> dsVisible;
	public HashSet<Place> visualized;
	
	private String movements;
	
	public boolean guessed;
	protected Integer option;
	
	public static HashMap<Place, Pair<Transition,Transition>> violCon;
	public Collection<Transition> enabledTransitions;
	private PetrinetBuilder pn;
	
	protected ArrayList<Collection<Transition>> transitionHistory;
	protected ArrayList<HashSet<Transition>> permdisableHistory;
	protected ArrayList<Marking> stateHistory;
	protected Marking currentState;
	
	public EvaluationVisualizator(PetrinetBuilder pn, DeclareMap map2, String option) throws IllegalTransitionException{
		this.pn = pn;
		this.option = Integer.parseInt(option);
		
		this.decMap = pn.map;
		System.out.println("Petrinetbuilder: "+pn.resetnet.getNodes().toString());
		this.riNet = pn.resetnet;
		this.m = pn.iniM;
		this.iniM = new Marking();
		for(Place p: pn.iniM)
			this.iniM.add(p);
		this.constraintConstructs = pn.constraintConstructs;
		this.dp = new ArrayList<DependencyStructure>(pn.dpS);			
		this.iniDp = pn.dpS;
		this.violCon = pn.violCon;
		this.guessed = false;
		this.enabledTransitions = new HashSet<Transition>();
		this.movements= "";
		this.dsFrames = new HashMap<DependencyStructure,JFrame>();
		this.dsVisible = new HashMap<DependencyStructure,Boolean>();

		transitionHistory = new ArrayList<Collection<Transition>>();
		permdisableHistory = new ArrayList<HashSet<Transition>>();
		stateHistory = new ArrayList<Marking>();
		currentState = new Marking();
		
		permDisable = new HashSet<Transition>();
		nodeLabels = new HashMap<DirectedGraphNode, String>();
				
		System.out.println("Violcon: "+violCon.toString());
		
		System.out.println("\n\n---- Petrinet places and transitions:");
		for(DirectedGraphNode n: riNet.getNodes())
			nodeLabels.put(n, n.getLabel());
				
		
		semantics = PetrinetSemanticsFactory.riNetDeclareSemantics(ResetInhibitorNet.class);		
		semantics.initialize(riNet.getTransitions(), iniM, pn);
						
		graph = decMap.getViewCh().getGraph();
	}
	
	public void extendMovements(String move){
		movements+= move+System.currentTimeMillis()+"\n";
	}
	
	public String printMovements(){
		return movements;
	}
	
	public DGraph getGraph(){
		return graph;
	}
	
	public void showHelpPane(){
		//helpPane = new HelpPane(pn.descriptions, false);
	}
		
	
	public void visualizeDependencyGraph(final int index, boolean refresh){			
		JFrame popUp = dsFrames.get(dp.get(index));

		JPanel panel = toPanel(dp.get(index));
		JSplitPane main = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panel, makeLegend(dp.get(index)));
		popUp.add(main);	
		if(refresh){
			popUp.setVisible(true);
			//popUp.repaint();
		}
		
		popUp.pack();
		dsFrames.put(dp.get(index), popUp);
	}
	
	
	public void execute(String transition) throws IllegalTransitionException{		
		if(transition.equals("empty")){
			tracePane.extendTrace("Begin");
		}else{
//			for(Transition t: riNet.getTransitions())
//				if(t.isInvisible())
//					if(semantics.getExecutableTransitions().contains(t))
//						semantics.executeExecutableTransition(t);
			tracePane.extendTrace(transition);
			semantics.executeExecutableTransition(getTransitionsByLabel(riNet, transition).iterator().next());
		}
				
		
		
		System.out.println("\n@@@@@@@@@@@@@\nFIRING "+transition+"\n@@@@@@@@@@@@@\n");
		//System.out.println("Unlucky places :");
		for(Place p: semantics.getExPlaces())
			System.out.println("-"+nodeLabels.get(p));
		

		
		for(int i=0; i<dp.size();i++){
			//System.out.println("Index in execution is "+i);
			if(dsFrames.get(dp.get(i)).isVisible()){
				visualizeDependencyGraph(i,true);
			}else{
				visualizeDependencyGraph(i,false);	
			}
		}
		
		currentState = semantics.getCurrentState();

		ex = new HashSet<String>();		
		enabledTransitions = semantics.getExecutableTransitions();
		
		Collection<Transition> transfer = new HashSet<Transition>();
		transfer.addAll(enabledTransitions);
		
		transitionHistory.add(transfer);
		stateHistory.add(semantics.getCurrentState());
		
		System.out.println("Enabled activities: "+enabledTransitions.toString());
		
		ex = semantics.getExplanations();
		colorDecMap(null);
	}

	
	public DependencyStructure getDSofTrans(String trans){
		for(DependencyStructure d: iniDp)
			if(d.getT().getLabel().equals(trans))
				return d;
		return null;		
	}
	
	public void displayPast(Integer index){
		//System.out.println("Asked "+index+" for "+ transitionHistory.toString());
		enabledTransitions = transitionHistory.get(index);
		
		currentState = stateHistory.get(index);
		colorDecMap(null);
	}
	
	public void reset() throws IllegalTransitionException{
		permDisable = new HashSet<Transition>();
		tracePane.reset();		
		
		semantics.initialize(riNet.getTransitions(), iniM, pn);		
		execute("empty");	
	}
	
		
	
	public void colorConstraint(org.processmining.plugins.declare.visualizing.ConstraintDefinition conDec, Color color){
		for(ConstraintDefinition cD: constraintConstructs.keySet())
			if(cD.getCaption().equals(conDec.getCaption()))
				petriPane.colorCon(constraintConstructs.get(cD), color, semantics.getCurrentState());			
	}
	
	
	public void colorDecMap(org.processmining.plugins.declare.visualizing.ConstraintDefinition conDec){		
		if(option>0){
		Collection<Transition> hM = enabledTransitions;
		HashSet<String> hMS = new HashSet<String>();
				
		HashSet<Transition> hFinal = new HashSet<Transition>();
		for(Transition t: hM)
			if(t.isInvisible()){
				hFinal.add(t);
				//System.out.println("Removing: "+t.getLabel());
			}
				
		
		hM.removeAll(hFinal);
		for(Transition t: hM)
			hMS.add(t.getLabel());
		
		for(Transition t: permDisable)
			if(hMS.contains(t.getLabel()))
				hMS.remove(t.getLabel());
		
		buttonPane.updateScrollArea(hMS);
		
		System.out.println("Enabled is: "+enabledTransitions.toString()+" vs HM"+hM.toString());
		//System.out.println("\n\nEx is:\n"+ex.toString());
		
		if(!ex.isEmpty())
			buttonPane.updateExScrollArea(ex);
		else
			buttonPane.updateExScrollArea(new HashSet<String>());

		List<Object> cellsAll = new ArrayList<Object>();		
		List<Object> colored = new ArrayList<Object>();
		
				
		GraphModel model = graph.getModel();
		for (int i = 0; i < model.getRootCount(); i++) {
			Object root = model.getRootAt(i);
			Object rootUserObject = getUserObject(root);
			if (rootUserObject != null) {
					cellsAll.add(root);						
			}
		}


		HashSet<ConstraintDefinitionEdge> toColor = new HashSet<ConstraintDefinitionEdge>();
		
		for(Object cell: cellsAll){
			if(cell instanceof ActivityDefinitonCell){
					if(hMS.contains(((ActivityDefinitonCell)cell).getActivityDefinition().toString())){
						((ActivityDefinitonCell)cell).setBackground(Color.green);
						decMap.getViewCh().updateUI((DefaultGraphCell) cell);
						colorT(((ActivityDefinitonCell)cell).getActivityDefinition().toString(),Color.green);
						colored.add(cell);
					}
			}else if(cell instanceof ConstraintDefinitionEdge){			
				//System.out.println("Reviewing "+((ConstraintDefinitionEdge) cell).getConstraintDefinition().getCaption());
					for(Place pp: violCon.keySet()){
						if(currentState.contains(pp)){
							Iterator<Parameter> i = ((ConstraintDefinitionEdge) cell).getConstraintDefinition().getParameters().iterator();
							String A = ((ConstraintDefinitionEdge) cell).getConstraintDefinition().getBranches(i.next()).iterator().next().getName();	
							if(((ConstraintDefinitionEdge) cell).getConstraintDefinition().isBinary()){			
								String B = ((ConstraintDefinitionEdge) cell).getConstraintDefinition().getBranches(i.next()).iterator().next().getName();
								if(violCon.get(pp).getFirst().getLabel().equals(A) && violCon.get(pp).getSecond().getLabel().equals(B)){
									toColor.add((ConstraintDefinitionEdge) cell);		
								}
							}else{
								if(pn.existencePlaces.containsKey(pp) && violCon.get(pp).getFirst().getLabel().equals(A)){
									if(currentState.occurrences(pp)<pn.existencePlaces.get(pp))	
										toColor.add((ConstraintDefinitionEdge) cell);	
								}else if(pn.exactlyPlaces.contains(pp)){
									if(violCon.get(pp).getFirst().getLabel().equals(A)){
										toColor.add((ConstraintDefinitionEdge) cell);
									}
								}
							}
						}else{
							if(((ConstraintDefinitionEdge) cell).getConstraintDefinition().isUnary()){	
								Iterator<Parameter> i = ((ConstraintDefinitionEdge) cell).getConstraintDefinition().getParameters().iterator();
								String A = ((ConstraintDefinitionEdge) cell).getConstraintDefinition().getBranches(i.next()).iterator().next().getName();	
								if(pn.existencePlaces.containsKey(pp) && violCon.get(pp).getFirst().getLabel().equals(A)){
										toColor.add((ConstraintDefinitionEdge) cell);										
								}
							}
						}
					}
			}
		}
		
		
		boolean coloredCondec=false;
		for(Object cell: cellsAll){
			if(cell instanceof ConstraintDefinitionEdge){
				if(toColor.contains(cell)){
					((ConstraintDefinitionEdge) cell).setColor(Color.red, Color.red, Color.red);
					colorConstraint(((ConstraintDefinitionEdge) cell).getConstraintDefinition(), Color.red);
					if(((ConstraintDefinitionEdge) cell).getConstraintDefinition().equals(conDec)){
						for(ConstraintDefinition cD: constraintConstructs.keySet())
							if(cD.getCaption().equals(conDec.getCaption())){
								petriPane.colorCon(constraintConstructs.get(cD), Color.decode("#F5840C"), currentState);			
								coloredCondec=true;
							}
					}
				}else{
					((ConstraintDefinitionEdge) cell).setColor(Color.black, Color.black, Color.black);
					colorConstraint(((ConstraintDefinitionEdge) cell).getConstraintDefinition(), Color.black);
				}
			}
		}

	
		for(Object o: cellsAll)
			if(!colored.contains(o))
				if(o instanceof ActivityDefinitonCell){
					((ActivityDefinitonCell)o).setBackground(Color.white);	
					colorT(((ActivityDefinitonCell)o).getActivityDefinition().toString(),Color.white);
					decMap.getViewCh().updateUI((DefaultGraphCell) o);
				}
		
		
		if(!(conDec==null) && !coloredCondec)
			for(ConstraintDefinition cD: constraintConstructs.keySet())
				if(cD.getCaption().equals(conDec.getCaption()))
					petriPane.colorCon(constraintConstructs.get(cD), Color.orange, currentState);		
		

		
		if(semantics.isAccepting())
			petriPane.colorT("END", Color.GREEN);
		else
			petriPane.colorT("END", Color.WHITE);
		
		graph.refresh();
		}
	}
	

	public JPanel makeLegend(DependencyStructure d){

		JPanel out = new JPanel();
		JLabel legendText = new JLabel("Legend");
		
		out.add(legendText);
		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();
		
		graph.getModel().beginUpdate();
		try
		{				
			Object node1 = graph.insertVertex(parent, null, "", 0, 5, 30, 30);
			Object node2 = graph.insertVertex(parent, null, "", 200, 5, 30, 30);
			graph.insertEdge(parent, null, "Not violated constraint", node1, node2, "strokeColor=#000000;fontSize=9;labelBackgroundColor=#FFFFFF");	
			
			
			Object node3 = graph.insertVertex(parent, null, "", 0, 50, 30, 30);
			Object node4 = graph.insertVertex(parent, null, "", 200, 50, 30, 30);
			graph.insertEdge(parent, null, "Direct violated constraint", node3, node4, "strokeColor=#F0000F;fontSize=9;labelBackgroundColor=#FFFFFF");	
			
			Object node5 = graph.insertVertex(parent, null, "", 0, 95, 30, 30);
			Object node6 = graph.insertVertex(parent, null, "", 200, 95, 30, 30);
			graph.insertEdge(parent, null, "Indirectly violated constraint", node5, node6, "strokeColor=#FF8000;fontSize=9;labelBackgroundColor=#FFFFFF");	
	
			Object node7 = graph.insertVertex(parent, null, "", 250, 5, 30, 30);
			Object node8 = graph.insertVertex(parent, null, "", 450, 5, 30, 30);
			graph.insertEdge(parent, null, "Satisfied forward\n-dependent constraint", node7, node8, "strokeColor=#00FFBF;fontSize=9;labelBackgroundColor=#FFFFFF");	
			
			Object node9 = graph.insertVertex(parent, null, "", 250, 50, 30, 30);
			Object node10 = graph.insertVertex(parent, null, "", 450, 50, 30, 30);
			graph.insertEdge(parent, null, "Not fulfilled forward\n-dependent constraint", node9, node10, "strokeColor=#000FF0;fontSize=9;labelBackgroundColor=#FFFFFF");				
			
			Object node11 = graph.insertVertex(parent, null, "", 250, 95, 30, 30);
			Object node12 = graph.insertVertex(parent, null, "", 450, 95, 30, 30);
			graph.insertEdge(parent, null, "Violated forward-\ndependent constraint", node11, node12, "strokeColor=#8904B1;fontSize=9;labelBackgroundColor=#FFFFFF");		
			
			if(d.isNS()){
				Object node13 = graph.insertVertex(parent, null, "", 500, 5, 30, 30);
				Object node14 = graph.insertVertex(parent, null, "", 700, 5, 30, 30);			
				graph.insertEdge(parent, null, "Main constraint satisfiable/satisfied", node13, node14, "strokeColor=#09FF00;fontSize=9;labelBackgroundColor=#FFFFFF");	
			}
		}
		finally
		{
			graph.getModel().endUpdate();
		}

		mxGraphComponent graphUIComponent = new mxGraphComponent(graph);		
		out.add(graphUIComponent);
		return out;					
	}
	
	
	public JPanel toPanel(DependencyStructure d){
		//System.out.println("\n##### Creating panel for "+d.getCaption());
		//System.out.println("Enabled activities: "+semantics.getExecutableTransitions().toString());
		JPanel out = new JPanel();
		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();
		transToNode = new HashMap<Transition,Object>();
		
		mxGraphView view = new mxGraphView(graph);
		view.setScale(1.4);
		graph.setAutoSizeCells(true);
		graph.setView(view);
		
		visualized = new HashSet<Place>();
		
		graph.getModel().beginUpdate();
		try
		{		
			Transition t=null;
			if(d.isNS()){	
				t=d.getTs();
				if(semantics.getExecutableTransitions().contains(d.getT()))	
					graph.insertEdge(parent, null, nodeLabels.get(d.getP()), insertNode(parent, graph, d.getTs()), insertNode(parent, graph,d.getT()), "strokeColor=#09FF00;fontSize=8;labelBackgroundColor=#FFFFFF");
				else
					graph.insertEdge(parent, null, nodeLabels.get(d.getP()), insertNode(parent, graph, d.getTs()), insertNode(parent, graph,d.getT()), "strokeColor=#FF0000;fontSize=8;labelBackgroundColor=#FFFFFF");
			}else if(d.isUn()){
				insertNode(parent, graph, d.getT());
				
				t= d.getT();
			}else{
				insertNode(parent, graph, d.getT());
				t= d.getT();
			}
			
			//System.out.println("What about "+nodeLabels.get(d.getUP()));
			for(Pair<Place,Transition> p: d.getPtt()){
				//System.out.println("Looping pairs "+nodeLabels.get(p.getFirst())+" and "+nodeLabels.get(p.getSecond()));
				boolean violation=false;
				
				for(DependencyStructure dd: d.getDP()){
					//System.out.println("Looking for "+dd.getT());
					if(dd.getT().equals(p.getSecond()) && !pn.choiceP.containsKey(p.getFirst()) && !visualized.contains(dd.getP())){
						violation = lowerHierarchy(dd, parent,graph, dd.getTforP(dd.getP()));
						if(semantics.getCurrentState().contains(dd.getP())){
							graph.insertEdge(parent, null, nodeLabels.get(dd.getP()), insertNode(parent, graph,  dd.getTforP(dd.getP())), insertNode(parent, graph, p.getSecond()), "strokeColor=#00FFBF;fontSize=8;labelBackgroundColor=#FFFFFF");
							visualized.add(dd.getP());
						}else if(!violation){
							graph.insertEdge(parent, null, nodeLabels.get(dd.getP()), insertNode(parent, graph,  dd.getTforP(dd.getP())), insertNode(parent, graph, p.getSecond()), "strokeColor=#000FF0;fontSize=8;labelBackgroundColor=#FFFFFF");
							visualized.add(dd.getP());
						}else{
							graph.insertEdge(parent, null, nodeLabels.get(dd.getP()), insertNode(parent, graph,  dd.getTforP(dd.getP())), insertNode(parent, graph, p.getSecond()), "strokeColor=#8904B1;fontSize=8;labelBackgroundColor=#FFFFFF");
							visualized.add(dd.getP());
						}
					}
				}
				
				if(!visualized.contains(p.getFirst())){
					if(semantics.getExPlaces().contains(p.getFirst())){
						graph.insertEdge(parent, null, nodeLabels.get(p.getFirst()), insertNode(parent, graph, p.getSecond()), insertNode(parent, graph, t), "strokeColor=#F0000F;fontSize=8;labelBackgroundColor=#FFFFFF");
						visualized.add(p.getFirst());
					}else if(!violation){
						graph.insertEdge(parent, null, nodeLabels.get(p.getFirst()), insertNode(parent, graph, p.getSecond()), insertNode(parent, graph, t), "strokeColor=#000000;fontSize=8;labelBackgroundColor=#FFFFFF");
						visualized.add(p.getFirst());
					}else if(semantics.getCurrentState().contains(p.getFirst())){
						//System.out.println("In here? "+nodeLabels.get(p.getFirst()));
						graph.insertEdge(parent, null, nodeLabels.get(p.getFirst()), insertNode(parent, graph, p.getSecond()), insertNode(parent, graph, t), "strokeColor=#F7FE2E;fontSize=8;labelBackgroundColor=#FFFFFF");					
						visualized.add(p.getFirst());
					}else{
						graph.insertEdge(parent, null, nodeLabels.get(p.getFirst()), insertNode(parent, graph, p.getSecond()), insertNode(parent, graph, t), "strokeColor=#FF8000;fontSize=8;labelBackgroundColor=#FFFFFF");
						visualized.add(p.getFirst());
					}
				}
			}			
		}
		finally
		{
			graph.getModel().endUpdate();
		}
		mxHierarchicalLayout h = new mxHierarchicalLayout(graph);
		h.execute(graph.getDefaultParent());		
		mxGraphComponent graphUIComponent = new mxGraphComponent(graph);		
		out.add(graphUIComponent);					
		return out;		
	}
	
	
	private boolean lowerHierarchy(DependencyStructure dLower, Object parent, mxGraph graph, Transition v1){		
		boolean violation=false;
		//System.out.println("Lower hierarchy for "+nodeLabels.get(dLower.getP()));
		for(Pair<Place,Transition> p: dLower.getPtt()){	
			if(p.getFirst()!=dLower.getP() && !visualized.contains(p.getFirst())){
				if(semantics.getExPlaces().contains(p.getFirst())){
					graph.insertEdge(parent, null, nodeLabels.get(p.getFirst()), insertNode(parent, graph, p.getSecond()), insertNode(parent, graph, v1), "strokeColor=#F0000F;fontSize=8;labelBackgroundColor=#FFFFFF");
					violation=true;
					visualized.add(p.getFirst());
				}else{
					graph.insertEdge(parent, null, nodeLabels.get(p.getFirst()), insertNode(parent, graph, p.getSecond()), insertNode(parent, graph, v1), "strokeColor=#000000;fontSize=8;labelBackgroundColor=#FFFFFF");				
					visualized.add(p.getFirst());
				}
			}
			for(DependencyStructure d: dLower.getDP())
				if(d.getT().equals(p.getSecond()) && !visualized.contains(d.getP())){
					violation = lowerHierarchy(d, parent,graph, d.getTforP(d.getP()));		
					if(semantics.getCurrentState().contains(d.getP()) && !pn.choiceP.containsKey(p.getFirst())){
						graph.insertEdge(parent, null, nodeLabels.get(d.getP()), insertNode(parent, graph, d.getTforP(d.getP())), insertNode(parent, graph, p.getSecond()), "strokeColor=#00FFBF;fontSize=8;labelBackgroundColor=#FFFFFF");
						visualized.add(d.getP());
					}else if(!violation){
						graph.insertEdge(parent, null, nodeLabels.get(d.getP()), insertNode(parent, graph, d.getTforP(d.getP())), insertNode(parent, graph, p.getSecond()), "strokeColor=#000FF0;fontSize=8;labelBackgroundColor=#FFFFFF");
						visualized.add(d.getP());
					}else{
						graph.insertEdge(parent, null, nodeLabels.get(d.getP()), insertNode(parent, graph, d.getTforP(d.getP())), insertNode(parent, graph, p.getSecond()), "strokeColor=#8904B1;fontSize=8;labelBackgroundColor=#FFFFFF");
						visualized.add(d.getP());
					}
				}
		}	
		for(DependencyStructure d: dLower.getDP())
			if(d.getT().equals(dLower.getP())){
				//graph.insertEdge(parent, null, nodeLabels.get(d.getP()), insertNode(parent, graph, d.getT()), insertNode(parent, graph, v1), "strokeColor=#000FF0;fontSize=8;labelBackgroundColor=#FFFFFF");
				lowerHierarchy(d, parent,graph, d.getT());		
			}

		return violation;		
	}
	
	
	private Object insertNode(Object parent, mxGraph graph, Transition t){
		Object node=null;
		if(!transToNode.containsKey(t)){
			if(semantics.getExecutableTransitions().contains(t))
				node = graph.insertVertex(parent, null, t.getLabel(), 20, 130, 150, 30, "fillColor=#00FF00");
			else
				node = graph.insertVertex(parent, null, t.getLabel(), 20, 130, 150, 30, "fillColor=#FFFFFF");
			transToNode.put(t, node);
		}else
			node = transToNode.get(t);
		return node;		
	}
	
	
	private Object getUserObject(Object cell) {
		if (cell != null) {
			if (cell instanceof DefaultGraphCell) {
				return ((DefaultGraphCell) cell).getUserObject();
			}
			return null;
		}
		return null;
	}
	
	public HashSet<String> getTransitions(){
		HashSet<String> trans = new HashSet<String>();
		for(Transition t: riNet.getTransitions())
			if(t.isInvisible()==false)
				trans.add(t.getLabel());
		return trans;
	}
	 
	
	public ResetInhibitorNetImpl getNet(){
		return riNet;
	}

	public Marking getMarking(){
		return semantics.getCurrentState();
	}
	
	
	public static Set<Transition> getTransitionsByLabel(ResetInhibitorNet petriNet, String label) {
		Set<Transition> transitions = new HashSet<Transition>();
		for (Transition t : petriNet.getTransitions()) {
			if (t.getLabel().equals(label))
				transitions.add(t);
		}
		return transitions;
	}
	
	
	public void colorT(String act, Color color){
		this.petriPane.colorT(act, color);
	}
	

}
