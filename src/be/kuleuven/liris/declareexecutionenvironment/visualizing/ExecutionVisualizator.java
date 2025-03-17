package be.kuleuven.liris.declareexecutionenvironment.visualizing;

/** Author: Johannes De Smedt
 *  E-mail: johannes.desmedt@kuleuven.be
 *  October 2015
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphModel;
import org.processmining.models.graphbased.directed.petrinet.ResetInhibitorNet;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.graphbased.directed.petrinet.impl.ResetInhibitorNetImpl;
import org.processmining.models.semantics.IllegalTransitionException;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.declare.visualizing.ActivityDefinitonCell;
import org.processmining.plugins.declare.visualizing.ConstraintDefinitionEdge;
import org.processmining.plugins.declare.visualizing.DGraph;
import org.processmining.plugins.decmod2rinet.visualizing.HelpPane;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;

import be.kuleuven.liris.declareexecutionenvironment.activity.Activity;
import be.kuleuven.liris.declareexecutionenvironment.constraint.Constraint;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.Binary;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.Choice;
import be.kuleuven.liris.declareexecutionenvironment.constraint.nary.negative.NotSuccession;
import be.kuleuven.liris.declareexecutionenvironment.constraint.unary.Unary;
import be.kuleuven.liris.declareexecutionenvironment.dependencystructure.ConstraintDependencyStructure;
import be.kuleuven.liris.declareexecutionenvironment.dependencystructure.DependencyStructure;
import be.kuleuven.liris.declareexecutionenvironment.model.AnnotatedDeclareModel;
import be.kuleuven.liris.declareexecutionenvironment.model.DeclareModel;
import be.kuleuven.liris.declareexecutionenvironment.model.StateSpaceDecMod;
import be.kuleuven.liris.declareexecutionenvironment.semantics.DeclareMapSemantics;

public class ExecutionVisualizator extends JPanel {

	private static final long serialVersionUID = -4828826958007119206L;	
	private final boolean largeScreen = 1440<Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	protected PetrinetPane petriPane;
	protected ButtonPane buttonPane;	
	protected DynTracePane tracePane;
	private HelpPane helpPane;
	private JTable activityTable;
	private DefaultTableModel model;
	
	private AnnotatedDeclareModel aMod;
	protected DeclareModel declareModel;
	
	protected DeclareMapSemantics semantics;
	
	protected ResetInhibitorNetImpl riNet;

	private DGraph graph;
	
	public ArrayList<ConstraintDependencyStructure> dp;
	protected HashSet<String> ex;
	private HashMap<Activity, Object> transToNode;

	public HashMap<ConstraintDependencyStructure,JFrame> dsFrames;
	public HashMap<ConstraintDependencyStructure,Boolean> dsVisible;
	public HashSet<Constraint> visualized;
	
	private String movements;
	
	public boolean guessed;
	protected Integer option;
	
	public Collection<Activity> enabledActivities;
	
	protected ArrayList<Collection<Activity>> activityHistory;
	protected ArrayList<HashSet<Transition>> permdisableHistory;
	protected ArrayList<Marking> stateHistory;
	protected Marking currentState;
	
	
	public ExecutionVisualizator(DeclareModel declareModel) throws IllegalTransitionException{
		this.aMod = new AnnotatedDeclareModel(declareModel.getAssignmentViewModel());
		this.declareModel = declareModel;
		this.riNet = aMod.getRiNet();
		this.semantics = new DeclareMapSemantics(aMod);
		dp = new ArrayList<ConstraintDependencyStructure>();
		for(ConstraintDependencyStructure d: aMod.getDependencyStructures())
			dp.add(d);
		
		
		StateSpaceDecMod state_space = new StateSpaceDecMod(aMod);
				
		this.enabledActivities = new HashSet<Activity>();
		this.movements= "";
		this.dsFrames = new HashMap<ConstraintDependencyStructure,JFrame>();
		this.dsVisible = new HashMap<ConstraintDependencyStructure,Boolean>();

		activityHistory = new ArrayList<Collection<Activity>>();
		permdisableHistory = new ArrayList<HashSet<Transition>>();
		stateHistory = new ArrayList<Marking>();
		currentState = new Marking();
		
		//permDisable = new HashSet<Transition>();				
		
		// I want dragons in the north
		graph = declareModel.getAssignmentModelView().getGraph();		
		graph.setAntiAliased(true);
		if(largeScreen)
			graph.setScale(2.0);
		
	    model = new DefaultTableModel();
	    activityTable = new JTable(model);
		JScrollPane pane = new JScrollPane(activityTable);
		pane.setPreferredSize(new Dimension(170,800));
		
		this.buttonPane = new ButtonPane(this, largeScreen);
		this.tracePane = new DynTracePane(this, largeScreen);
		try{
			this.petriPane = new PetrinetPane(this, largeScreen);
			if(largeScreen)
				petriPane.enlarge();
		} catch (IllegalArgumentException iaE){
			System.out.println("Too many arcs in Petri net");
		}
				
		BorderLayout layout = new BorderLayout();
		layout.setVgap(20);
		this.setLayout(layout);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		//panel.add(pane, BorderLayout.WEST);
		panel.add(buttonPane, BorderLayout.EAST);	
		
		this.add(graph, BorderLayout.NORTH);
		this.add(panel, BorderLayout.CENTER);
		this.add(tracePane,BorderLayout.SOUTH);
		
		//JSplitPane splitMainAgain = new JSplitPane(JSplitPane.VERTICAL_SPLIT, /*splitMain*/j, buttonPane);		
		//JSplitPane main = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitMainAgain, tracePane);
		//this.add(main,BorderLayout.CENTER);
		
		for(int i=0;i<dp.size();i++){		
			dsFrames.put(dp.get(i), new JFrame(dp.get(i).getCaption()));
			dsVisible.put(dp.get(i), false);
		}
		
		execute(null);			
		this.buttonPane.updateDSScrollArea(dp);
		if(largeScreen)
			this.setPreferredSize(new Dimension(2560,1440));	
		else	
			this.setPreferredSize(new Dimension(1400,800));		
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
	
//	public void showHelpPane(){
//		helpPane = new HelpPane(pn.descriptions);
//	}
		
	
	public void visualizeDependencyGraph(final int index, boolean refresh){			
		JFrame popUp = dsFrames.get(dp.get(index));

		JPanel panel = toPanel(dp.get(index));
		JSplitPane main = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panel, makeLegend(dp.get(index)));
		popUp.add(main);	
		if(refresh)
			popUp.setVisible(true);
				
		popUp.pack();
		dsFrames.put(dp.get(index), popUp);
	}
	
	
	public void execute(Activity firedActivity) throws IllegalTransitionException{		
		if(firedActivity==null){
			tracePane.extendTrace("Begin");
			System.out.println("\n@@@@@@@@@@@@@\nResetting\n@@@@@@@@@@@@@\n");
		}else{
			tracePane.extendTrace(firedActivity.getName());
			semantics.fire(firedActivity);
			System.out.println("\n@@@@@@@@@@@@@\nFIRING "+firedActivity.getName()+"\n@@@@@@@@@@@@@\n");
		}
		
		//Update table
		updateActivityTable();
					
		System.out.println("Semantics "+semantics.getEnabledActivities());
		enabledActivities = semantics.getEnabledActivities();
		
		colorDecMap(null);
	}

	public void updateActivityTable(){
		model = new DefaultTableModel();
		activityTable.setModel(model);
		model.addColumn("Activity");
		model.addColumn("Lower bound");
		model.addColumn("Upper bound");		
		for(Activity a: aMod.getActivities(true)){
			Vector<String> row = new Vector<String>();
			row.add(a.getName());
			row.add(a.getLowerBound()+"");
			if(a.getUpperBound()<Integer.MAX_VALUE/2)
				row.add(a.getUpperBound()+"");
			else
				row.add("<html>&infin</html>");
			model.addRow(row);
		}
		model.fireTableDataChanged();
	}
		
	public void displayPast(Integer index){
		//System.out.println("Asked "+index+" for "+ transitionHistory.toString());
		//enabledActivities = transitionHistory.get(index);
		
		currentState = stateHistory.get(index);
		colorDecMap(null);
	}
	
	public void reset() throws IllegalTransitionException{
		tracePane.reset();				
		semantics.reset();		
		execute(null);	
	}
			
		
	public void colorDecMap(org.processmining.plugins.declare.visualizing.ConstraintDefinition conDec){		
		HashSet<String> hMS = new HashSet<String>();
		HashSet<String> explanations = new HashSet<String>();
		
		for(Activity a: aMod.getActivities(true)){
			if(a.isEnabled())
				hMS.add(a.getName());
			explanations.addAll(a.getExplanations());
		}
		
		buttonPane.updateScrollArea(aMod.getActivities(true));
		buttonPane.updateExScrollArea(explanations);
		
		//System.out.println("\n\nEx is:\n"+ex.toString());		
//		if(!ex.isEmpty())
//			buttonPane.updateExScrollArea(ex);
//		else
//			buttonPane.updateExScrollArea(new HashSet<String>());

		List<Object> cellsAll = new ArrayList<Object>();				
				
		GraphModel model = graph.getModel();
		for (int i = 0; i < model.getRootCount(); i++) {
			Object root = model.getRootAt(i);
			Object rootUserObject = getUserObject(root);
			if (rootUserObject != null) {
					cellsAll.add(root);						
			}
		}
		
		for(Object cell: cellsAll){
			if(cell instanceof ActivityDefinitonCell){
				ActivityDefinitonCell c = (ActivityDefinitonCell) cell;
				org.jgraph.graph.AttributeMap map = c.getAttributes();
				GraphConstants.setGradientColor(map, Color.white);
				if(hMS.contains(((ActivityDefinitonCell)cell).getActivityDefinition().toString())){
					GraphConstants.setGradientColor(map, Color.green);
					//decMap.getViewCh().updateUI((DefaultGraphCell) cell);
				}
				System.out.println("Activity: "+((ActivityDefinitonCell)cell).getActivityDefinition().toString());
			}else if(cell instanceof ConstraintDefinitionEdge){	
				ConstraintDefinitionEdge ce = (ConstraintDefinitionEdge) cell;
				org.jgraph.graph.AttributeMap map = ce.getAttributes();
				GraphConstants.setLineColor(map, Color.black);
				for(Constraint c: aMod.getConstraints()){
					if(c instanceof Binary)
						if(((Binary) c).isActivated())
							if(c.getCD().getCaption().equals(((ConstraintDefinitionEdge) cell).getConstraintDefinition().getCaption()))
								GraphConstants.setLineColor(map, Color.blue);
					if(!c.isAccepting())
						if(c.getCD().getCaption().equals(((ConstraintDefinitionEdge) cell).getConstraintDefinition().getCaption()))
							GraphConstants.setLineColor(map, Color.red);										
				}
			}
		}	
		graph.refresh();
	}
	

	public JPanel makeLegend(ConstraintDependencyStructure d){

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
			
			if(d.getMainConstraint() instanceof NotSuccession){
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
	
	
	public JPanel toPanel(ConstraintDependencyStructure d){
		System.out.println("\n##### Creating panel for "+d.getCaption());
		JPanel out = new JPanel();
		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();
		graph.setHtmlLabels(true);
		transToNode = new HashMap<Activity,Object>();
		
		mxGraphView view = new mxGraphView(graph);
		if(largeScreen)
			view.setScale(2);
		graph.setAutoSizeCells(true);
		graph.setView(view);
		
		visualized = new HashSet<Constraint>();
		
		graph.getModel().beginUpdate();
		try
		{		
			Activity a = null;
			if(d.getMainConstraint() instanceof NotSuccession){
				NotSuccession ns = (NotSuccession) d.getMainConstraint();
				a = ns.getConsequent();
				if(enabledActivities.contains(ns.getConsequent())){
					graph.insertEdge(parent, null, ns.toString(),
							insertNode(parent, graph, ns.getConsequent()), 
								insertNode(parent, graph, ns.getAntecedent()), "strokeColor=#09FF00;fontSize=8;labelBackgroundColor=#FFFFFF");
				}else{
					graph.insertEdge(parent, null, ns.toString(),
							insertNode(parent, graph, ns.getConsequent()), 
								insertNode(parent, graph, ns.getAntecedent()), "strokeColor=#FF0000;fontSize=8;labelBackgroundColor=#FFFFFF");
				}
			}else if(d.getMainConstraint() instanceof Unary){
				insertNode(parent, graph, ((Unary) d.getMainConstraint()).getAntecedent());
				a = ((Unary) d.getMainConstraint()).getAntecedent();
			}else{
				insertNode(parent, graph, ((Binary) d.getMainConstraint()).getAntecedent());
				a = ((Binary) d.getMainConstraint()).getAntecedent();
			}
			
			for(Constraint c: d.getDependentConstraints()){
				if(c instanceof Unary){
					graph.insertEdge(parent, null, c, insertNode(parent, graph,  ((Unary) c).getAntecedent()), insertNode(parent, graph, ((Unary) c).getAntecedent()), "strokeColor=#00FFBF;fontSize=8;labelBackgroundColor=#FFFFFF");
				}else if (c instanceof Choice){
					Choice ch = (Choice) c;
					if(d.getActivities().contains(ch.getAntecedent()))
						graph.insertEdge(parent, null, c, insertNode(parent, graph,  ch.getAntecedent()), insertNode(parent, graph, ch.getAntecedent()), "strokeColor=#00FFBF;fontSize=8;labelBackgroundColor=#FFFFFF");
					else
						graph.insertEdge(parent, null, c, insertNode(parent, graph,  ch.getConsequent()), insertNode(parent, graph, ch.getConsequent()), "strokeColor=#00FFBF;fontSize=8;labelBackgroundColor=#FFFFFF");
				}else if(c instanceof Binary){
					Binary b = (Binary) c;
					graph.insertEdge(parent, null, b, insertNode(parent, graph,  b.getAntecedent()), insertNode(parent, graph, b.getConsequent()), "strokeColor=#00FFBF;fontSize=8;labelBackgroundColor=#FFFFFF");
				}
			}
			for(DependencyStructure cds: d.getDependentStructures()){
				lowerHierarchy((ConstraintDependencyStructure) cds, parent, graph, a);
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
	
	private boolean lowerHierarchy(ConstraintDependencyStructure dLower, Object parent, mxGraph graph, Activity v1){
		boolean violation = false;
		
		Binary mainConstraint = (Binary) dLower.getMainConstraint();
		Activity a = mainConstraint.getAntecedent();
		
		if(a.getLowerBound()<1)
			graph.insertEdge(parent, null, mainConstraint, insertNode(parent, graph, mainConstraint.getConsequent()), insertNode(parent, graph, a), "strokeColor=#F0000F;fontSize=8;labelBackgroundColor=#FFFFFF");
		else
			graph.insertEdge(parent, null, mainConstraint, insertNode(parent, graph, mainConstraint.getConsequent()), insertNode(parent, graph, a), "strokeColor=#000000;fontSize=8;labelBackgroundColor=#FFFFFF");
				
		for(Constraint c: dLower.getDependentConstraints()){
			if(c instanceof Unary){
				graph.insertEdge(parent, null, c, insertNode(parent, graph,  ((Unary) c).getAntecedent()), insertNode(parent, graph, ((Unary) c).getAntecedent()), "strokeColor=#00FFBF;fontSize=8;labelBackgroundColor=#FFFFFF");
			}else if (c instanceof Choice){
				Choice ch = (Choice) c;
				if(dLower.getActivities().contains(ch.getAntecedent()))
					graph.insertEdge(parent, null, c, insertNode(parent, graph,  ch.getAntecedent()), insertNode(parent, graph, ch.getAntecedent()), "strokeColor=#00FFBF;fontSize=8;labelBackgroundColor=#FFFFFF");
				else
					graph.insertEdge(parent, null, c, insertNode(parent, graph,  ch.getConsequent()), insertNode(parent, graph, ch.getConsequent()), "strokeColor=#00FFBF;fontSize=8;labelBackgroundColor=#FFFFFF");
			}else if(c instanceof Binary){
				Binary b = (Binary) c;
				graph.insertEdge(parent, null, b, insertNode(parent, graph,  b.getAntecedent()), insertNode(parent, graph, b.getConsequent()), "strokeColor=#00FFBF;fontSize=8;labelBackgroundColor=#FFFFFF");
			}
		}
		for(DependencyStructure cds: dLower.getDependentStructures())
			lowerHierarchy((ConstraintDependencyStructure) cds, parent, graph, a);
		
		return violation;		
	}
		
	private Object insertNode(Object parent, mxGraph graph, Activity a){
		Object node=null;
		if(!transToNode.containsKey(a)){
			String ub = "";
			if(a.getUpperBound()<Integer.MAX_VALUE/2)
				ub = a.getUpperBound()+"";
			else
				ub = "<html>&infin)</html>";
			if(enabledActivities.contains(a))
				node = graph.insertVertex(parent, null, a+" (LB: "+a.getLowerBound()+", UB: "+ub+")", 20, 130, 150, 30, "fillColor=#00FF00");
			else
				node = graph.insertVertex(parent, null, a+" (LB: "+a.getLowerBound()+", UB: "+ub+")", 20, 130, 150, 30, "fillColor=#FFFFFF");
			transToNode.put(a, node);
		}else
			node = transToNode.get(a);
		return node;		
	}
	
	public void showHelpPane(){
		//helpPane = new HelpPane(aMod, largeScreen);
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
	 	
	public static Set<Transition> getTransitionsByLabel(ResetInhibitorNet petriNet, String label) {
		Set<Transition> transitions = new HashSet<Transition>();
		for (Transition t : petriNet.getTransitions()) {
			if (t.getLabel().equals(label))
				transitions.add(t);
		}
		return transitions;
	}

}
