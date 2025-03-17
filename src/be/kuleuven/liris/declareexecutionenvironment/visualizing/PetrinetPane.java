package be.kuleuven.liris.declareexecutionenvironment.visualizing;

/** Authors: Seppe K.L.M. vanden Broucke, Johannes De Smedt
 *  E-mail: seppe.vandenbroucke@kuleuven.be, johannes.desmedt@kuleuven.be
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.HashSet;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.processmining.framework.util.Pair;
import org.processmining.models.connections.GraphLayoutConnection;
import org.processmining.models.graphbased.AttributeMap;
import org.processmining.models.graphbased.ViewSpecificAttributeMap;
import org.processmining.models.graphbased.directed.DirectedGraphEdge;
import org.processmining.models.graphbased.directed.DirectedGraphNode;
import org.processmining.models.graphbased.directed.petrinet.elements.Arc;
import org.processmining.models.graphbased.directed.petrinet.elements.InhibitorArc;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.jgraph.ProMGraphModel;
import org.processmining.models.jgraph.ProMJGraph;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.kutoolbox.visualizators.GraphViewPanel;

import com.jgraph.layout.JGraphFacade;
import com.jgraph.layout.hierarchical.JGraphHierarchicalLayout;

import be.kuleuven.liris.declareexecutionenvironment.visualizing.ExecutionVisualizator;

public class PetrinetPane extends JPanel {
	private static final long serialVersionUID = 10027791212343506L;
	private ProMJGraph graph;
	private ExecutionVisualizator evaluationVisualizator;
	private GraphViewPanel graphViewPanel;
	
	public PetrinetPane(ExecutionVisualizator evaluationVisualizator, boolean larger) {
		this.evaluationVisualizator = evaluationVisualizator;
		
		this.setLayout(new BorderLayout());
		
		this.initializeGraph();	
		if(larger)
			this.setPreferredSize(new Dimension(800,800));
		else
			this.setPreferredSize(new Dimension(400,400));
	}
	
	public void enlarge(){
		graph.setScale(2.0);
	}
	
	private void initializeGraph(){
		System.out.println("EV "+evaluationVisualizator);
		GraphLayoutConnection layoutConnection = new GraphLayoutConnection(evaluationVisualizator.riNet);
		ProMGraphModel model = new ProMGraphModel(evaluationVisualizator.riNet);
		ProMJGraph jGraph = new ProMJGraph(model, new ViewSpecificAttributeMap(), layoutConnection);
		
		for (DirectedGraphNode node : jGraph.getModel().getGraph().getNodes()) {
			node.getAttributeMap().put(AttributeMap.SHOWLABEL, true);
			if (node instanceof Place) {
				node.getAttributeMap().put(AttributeMap.SIZE, new Dimension(30,30));
			} else if (node instanceof Transition) {
				if (!((Transition) node).isInvisible())
					node.getAttributeMap().put(AttributeMap.SIZE, new Dimension(80,60));
			}
		}
				
		JGraphHierarchicalLayout layout = new JGraphHierarchicalLayout();
		layout.setDeterministic(false);
		layout.setCompactLayout(false);
		layout.setFineTuning(true);
		layout.setParallelEdgeSpacing(15);
		layout.setFixRoots(false);
		layout.setOrientation(SwingConstants.WEST);
	
		if(!layoutConnection.isLayedOut()){
			JGraphFacade facade = new JGraphFacade(jGraph);
			facade.setOrdered(false);
			facade.setEdgePromotion(true);
			facade.setIgnoresCellsInGroups(false);
			facade.setIgnoresHiddenCells(false);
			facade.setIgnoresUnconnectedCells(false);
			facade.setDirected(true);
			facade.resetControlPoints();
			facade.run(layout, true);
	
			java.util.Map<?, ?> nested = facade.createNestedMap(true, true);
	
			jGraph.getGraphLayoutCache().edit(nested);
			layoutConnection.setLayedOut(true);
		}
		
		jGraph.setUpdateLayout(layout);
				
		this.graph = jGraph;
		
		if (graphViewPanel != null)
			this.remove(graphViewPanel);
		
		graphViewPanel = new GraphViewPanel(graph);
		this.add(graphViewPanel, BorderLayout.CENTER);
		
		this.resetGraph();
	}
	
	private void resetGraph() {
		for (DirectedGraphEdge<? extends DirectedGraphNode, ? extends DirectedGraphNode> edge : 
			graph.getModel().getGraph().getEdges()) {
			edge.getAttributeMap().put(AttributeMap.EDGECOLOR, Color.BLACK);
			edge.getAttributeMap().put(AttributeMap.LINEWIDTH, 1.0F);
			if(edge instanceof InhibitorArc)
				edge.getAttributeMap().put(AttributeMap.EDGECOLOR, Color.GRAY);
		}
		for (DirectedGraphNode node : graph.getModel().getGraph().getNodes()) {
			node.getAttributeMap().put(AttributeMap.STROKECOLOR, Color.BLACK);
			node.getAttributeMap().put(AttributeMap.LINEWIDTH, 1.5F);
			node.getAttributeMap().put(AttributeMap.FILLCOLOR, Color.WHITE);
			if (node instanceof Place){
				node.getAttributeMap().put(AttributeMap.LABEL, "");
			}
			if (node instanceof Transition && ((Transition) node).isInvisible()){
				node.getAttributeMap().put(AttributeMap.SHOWLABEL, false);
				node.getAttributeMap().put(AttributeMap.FILLCOLOR, Color.BLACK);
			}
		}
				
		graph.refresh();		
	}

	public void colorT(String act, Color color){
		for(DirectedGraphNode n: graph.getModel().getGraph().getNodes()){
			if(n instanceof Transition){
				if(n.getLabel().equals(act)){
					if(color.equals(Color.blue))
						n.getAttributeMap().put(AttributeMap.LABELCOLOR, Color.white);
					n.getAttributeMap().put(AttributeMap.FILLCOLOR, color);
				}
			}
		}
		graph.refresh();
	}
	
	public void colorCon(Pair<HashSet<DirectedGraphNode>,HashSet<DirectedGraphEdge<?,?>>> hS, Color color, Marking m){		
		for(DirectedGraphNode n: hS.getFirst()){
			n.getAttributeMap().put(AttributeMap.STROKECOLOR, color);
			if(n instanceof Place){
				n.getAttributeMap().put(AttributeMap.LABEL, Integer.toString(m.occurrences(n)));
				n.getAttributeMap().put(AttributeMap.SHOWLABEL, true);
			}
		}
		for(DirectedGraphEdge<?, ?> e: hS.getSecond()){
		//	if(!e.getAttributeMap().get(AttributeMap.EDGECOLOR).equals(Color.RED))
			e.getAttributeMap().put(AttributeMap.EDGECOLOR,  color);	
		}
	}
	
	
	
	public void displayMarking(Marking m, Transition end){
		HashSet<Place> colored = new HashSet<Place>();
		HashSet<DirectedGraphEdge<?,?>> coloredE = new HashSet<DirectedGraphEdge<?,?>>();
		//HashSet<String> violCon = new HashSet<String>();
		
		for(Place p: m)
			for(DirectedGraphNode n: graph.getModel().getGraph().getNodes())
				if(p.equals(n)){
					n.getAttributeMap().put(AttributeMap.FILLCOLOR, Color.GREEN);
					colored.add(p);
					
					for(DirectedGraphEdge<? extends DirectedGraphNode, ? extends DirectedGraphNode> e: graph.getModel().getGraph().getOutEdges(p)){
						if(e.getTarget().getLabel().equals("END")){
							coloredE.add(e);
							e.getAttributeMap().put(AttributeMap.EDGECOLOR, Color.RED);
							e.getSource().getAttributeMap().put(AttributeMap.STROKECOLOR, Color.RED);
						}
					}							
				}
				
		for(DirectedGraphNode n: graph.getModel().getGraph().getNodes())
			if(n instanceof Place){
				if(!colored.contains(n)){
					n.getAttributeMap().put(AttributeMap.FILLCOLOR, Color.WHITE);
					n.getAttributeMap().put(AttributeMap.SHOWLABEL, true);
				}
				n.getAttributeMap().put(AttributeMap.LABEL, Integer.toString(m.occurrences(n)));
				n.getAttributeMap().put(AttributeMap.SHOWLABEL, true);
			}
		for(DirectedGraphEdge<?,?> e: graph.getModel().getGraph().getEdges())
				if(!coloredE.contains(e)){
					if(e.getTarget().getLabel().equals("END") && e instanceof Arc){
						e.getAttributeMap().put(AttributeMap.EDGECOLOR, Color.RED);
						e.getSource().getAttributeMap().put(AttributeMap.STROKECOLOR, Color.RED);
					}else{
						e.getAttributeMap().put(AttributeMap.EDGECOLOR, Color.BLACK);
						e.getSource().getAttributeMap().put(AttributeMap.STROKECOLOR, Color.BLACK);
					}
				}
		

		graph.refresh();
	}
	
		
	public void notifyGlobalView() {
		resetGraph();
		
//		Map<Transition, Integer> normalTransitions = new HashMap<Transition, Integer>();
//		Map<Transition, Integer> forcedTransitions = new HashMap<Transition, Integer>();
//		Map<Transition, Integer> unobservableTransitions = new HashMap<Transition, Integer>();
//		Map<Transition, Integer> skippedTransitions = new HashMap<Transition, Integer>();
//		Map<Place, Integer> logmovedPlaces = new HashMap<Place, Integer>();
//		Map<Transition, Integer> allowedNegativeEvents = new HashMap<Transition, Integer>();
//		Map<Transition, Integer> allowedUnmappedNegativeEvents = new HashMap<Transition, Integer>();
//		Map<Transition, Integer> disallowedGeneralizedEvents = new HashMap<Transition, Integer>();
//		Map<Transition, Integer> disallowedNegativeEvents = new HashMap<Transition, Integer>();
//		Map<Transition, Integer> allowedGeneralizedEvents = new HashMap<Transition, Integer>();
		
	}

//	
//	private int getTracePositionForReplayStep(ProcessReplayModel<Transition, XEventClass, Marking> replayModel, int replayStep) {
//		int positionInTrace = -1;
//		for (int step = 0; step <= replayStep; step++) {
//			ReplayMove type = replayModel.getReplayMove(step);
//			if ( type.equals(ReplayMove.BOTH_SYNCHRONOUS) 
//					|| type.equals(ReplayMove.BOTH_FORCED) 
//					|| type.equals(ReplayMove.LOGONLY_INSERTED) ) {
//				positionInTrace++;
//			}
//		}
//		return positionInTrace;
//	}
	

//	private void fixGraphTooltips() {
//		for (DirectedGraphNode node : graph.getModel().getGraph().getNodes()) {
//			if (node.getAttributeMap().containsKey(AttributeMap.TOOLTIP)) {
//				String tip = node.getAttributeMap().get(AttributeMap.TOOLTIP).toString();
//				tip = tip.replaceAll("<html>", "");
//				tip = tip.replaceAll("</html>", "");
//				tip = tip.replaceAll("<br>", "");
//				tip = tip.replaceAll("\\n", "<br>\n");
//				tip = "<html>"+tip+"</html>";
//				node.getAttributeMap().put(AttributeMap.TOOLTIP, tip);
//			}
//		}
//		
//	}

}
