package org.processmining.plugins.declare.visualizing;

//import info.clearthought.layout.TableLayout;
//import info.clearthought.layout.TableLayoutConstants;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphModel;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.plugins.decmod2rinet.AnnotatedDeclareMapGraph;
import org.processmining.plugins.decmod2rinet.DependencyStructure;

import com.fluxicon.slickerbox.factory.SlickerDecorator;

/**
 * <p>
 * Title: DECLARE
 * </p>
 * 
 * <p>
 * Description: Makes sure that all graph cells with the same userObject are
 * either selected or disselected at one moment.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: TU/e
 * </p>
 * 
 * @author Maja Pesic
 * @version 1.0
 */
public class DGraphSelectionListener implements GraphSelectionListener {

	DGraph graph;	

	/**
	 * Cretes a new instance for the specified graph. Assignes self as a
	 * listener for the graph.
	 * 
	 * @param aGraph
	 *            The graph that is listened to.
	 */
	public DGraphSelectionListener(DGraph aGraph) {
		graph = aGraph;
		graph.getSelectionModel().addGraphSelectionListener(this);
	}
	

	/**
	 * Selects all graph cells that have the same userObject as specified in the
	 * parameter.
	 * 
	 * @param root
	 *            The graph cell whose userObject will be considered.
	 */
	private void addAll(Object root) {
		if (root != null) {
			if (root instanceof DefaultGraphCell) {
				// the cell is a DefaultGraphCell
				DefaultGraphCell rootCell = (DefaultGraphCell) root;
				// get all graph cells that have the same userObject like the rootCell
				List<Object> list = getCellsWithUserObject(rootCell);
				if (list != null) {
					// select all the graph cells with the same userObject
					graph.getSelectionModel().addSelectionCells(list.toArray());
				}
			}
		}
	}

	/**
	 * Deselects all graph cells that have the same userObject as specified in
	 * the parameter.
	 * 
	 * @param root
	 *            The graph cell whose userObject will be considered.
	 */
	private void removeAll(Object root) {
		if (root != null) {
			if (root instanceof DefaultGraphCell) {
				// the cell is a DefaultGraphCell
				DefaultGraphCell rootCell = (DefaultGraphCell) root;
				// get all graph cells that have the same userObject like the rootCell
				List<Object> list = getCellsWithUserObject(rootCell);
				if (list != null) {
					// deselect all the graph cells with the same userObject
					graph.getSelectionModel().removeSelectionCells(list.toArray());
				}
			}
		}
	}

	/**
	 * Invoked whenever the selection of the graph changes.
	 * 
	 * @param graphSelectionEvent
	 *            GraphSelectionEvent
	 * @todo Implement this org.jgraph.event.GraphSelectionListener method
	 */
	public void valueChanged(GraphSelectionEvent graphSelectionEvent) {
		Object[] cells = graphSelectionEvent.getCells();
		if(cells.length>0){
			Object cell = cells[0];
			
			List<Object> cellsAll = new ArrayList<Object>();

			GraphModel model = graph.getModel();
			for (int i = 0; i < model.getRootCount(); i++) {
				Object root = model.getRootAt(i);
				Object rootUserObject = getUserObject(root);
				if (rootUserObject != null) {
						cellsAll.add(root);						
				}
			}
			//List<Object> colored = new ArrayList<Object>();

//			if(cell instanceof ActivityDefinitonCell){
//				System.out.println("Coloring "+((ActivityDefinitonCell) cell).getActivityDefinition().toString());
//				
////				for(String s: graph.getEv().getTransitions())
////					System.out.println("\nS: "+graph.getEv().getDSofTrans(s));				
//				
//				if(graph.getEv().getDSofTrans(((ActivityDefinitonCell) cell).getActivityDefinition().toString())!=null){
//					DependencyStructure ds = graph.getEv().getDSofTrans(((ActivityDefinitonCell) cell).getActivityDefinition().toString());
//					colored.addAll(colorDS(ds, cellsAll,0));					
//				}
//			}else{		
//				if(cell instanceof ConstraintDefinitionEdge){
				//System.out.println("Testing :"+graph.getAdg().getDepAct(((ActivityDefinitonCell) cell).getActivityDefinition().toString()).getFirst().toString());
					//graph.getEv().colorConstraint(((ConstraintDefinitionEdge) cell).getConstraintDefinition(), Color.ORANGE);
//					graph.getEv().colorDecMap(((ConstraintDefinitionEdge) cell).getConstraintDefinition());
//				colored.add(cell);
//				for(String s: graph.getAdg().getDepAct(((ActivityDefinitonCell) cell).getActivityDefinition().toString()).getFirst()){
//					for(Object c: cellsAll){
//						if(c instanceof ActivityDefinitonCell){
//							if(((ActivityDefinitonCell)c).getActivityDefinition().toString().equals(s)){
//								colored.add(c);
//								((ActivityDefinitonCell)c).setForeground(Color.magenta);
//								graph.getEv().colorT(s,Color.green);
//							}
//						}
//					}
//				}
//				for(String s: graph.getAdg().getDepAct(((ActivityDefinitonCell) cell).getActivityDefinition().toString()).getSecond()){
//					for(Object c: cellsAll){
//						if(c instanceof ActivityDefinitonCell){
//							if(((ActivityDefinitonCell)c).getActivityDefinition().toString().equals(s)){
//								colored.add(c);
//								((ActivityDefinitonCell) c).setForeground(Color.red);
//								graph.getEv().colorT(s,Color.red);
//							}
//						}
//					}
//				}
//				}
//			}
				
			//((ActivityDefinitonCell) cell).setForeground(Color.blue);
			//graph.getEv().colorT(((ActivityDefinitonCell) cell).getActivityDefinition().toString(),Color.blue);
			
			//Change names of activities
//			for(Object o: cellsAll)
//				if(o instanceof ActivityDefinitonCell){
//					((ActivityDefinitonCell) o).setLabel("Stomme activiteit!");
//				}
				
			// Color the activities
//			for(Object o: cellsAll)
//				//if(!colored.contains(o))
//					if(o instanceof ActivityDefinitonCell){
//						((ActivityDefinitonCell) o).setLabel("Stomme activiteit!");
//						//((ActivityDefinitonCell)o).setForeground(Color.black);	
//						//graph.getEv().colorT(((ActivityDefinitonCell)o).getActivityDefinition().toString(),Color.white);
//				//		graph.getEv().colorDecMap(((ConstraintDefinitionEdge) cell).getConstraintDefinition());
//				//	}

//			// Color the edges
//			if(cell instanceof ConstraintDefinitionEdge){
//				System.out.println(((ConstraintDefinitionEdge)cell).getConstraintDefinition().getCaption());
//				//graph.getEv().colorConstraint(((ConstraintDefinitionEdge)cell).getConstraintDefinition(), Color.ORANGE);
//				graph.getEv().colorDecMap(((ConstraintDefinitionEdge) cell).getConstraintDefinition());
//			}
//		

		}


//		for (int i = 0; i < cells.length; i++) {
//			handleSelection(graphSelectionEvent, cells[i]);
//		}
	}

	private List<Object> colorDS(DependencyStructure ds, List<Object> cellsAll, Integer color){
		List<Object> colored = new ArrayList<Object>();		

		for(Object cc: cellsAll){
			if(cc instanceof ActivityDefinitonCell){
				if(((ActivityDefinitonCell)cc).getActivityDefinition().toString().equals(ds.getT().getLabel())){
					colored.add(cc);
					((ActivityDefinitonCell)cc).setForeground(new Color(255,0,color));
					//graph.getEv().colorT(ds.getTforP(p).getLabel(),Color.green);
				}
			}
		}		
		
		for(Place p: ds.getPlaces()){
			System.out.println("Searching for "+ds.getTforP(p));
			for(Object c: cellsAll){
				if(c instanceof ActivityDefinitonCell){
					if(((ActivityDefinitonCell)c).getActivityDefinition().toString().equals(ds.getTforP(p).getLabel())){
						colored.add(c);
						((ActivityDefinitonCell)c).setForeground(Color.decode("#1E90FF"));
						//graph.getEv().colorT(ds.getTforP(p).getLabel(),Color.green);
					}
				}
			}
		}
		for(DependencyStructure d: ds.getDP())
			colored.addAll(colorDS(d,cellsAll,color+100));
		
		return colored;
	}
	
	/**
	 * Returns the userObject of a graph cell.
	 * 
	 * @param cell
	 *            A graph cell. Assumed to be an isntance of DefaultGraphCell.
	 * @return Returns the user object of the cell. NULL if the cell is not a
	 *         DefaultGraphCell.
	 */
	private Object getUserObject(Object cell) {
		if (cell != null) {
			if (cell instanceof DefaultGraphCell) {
				return ((DefaultGraphCell) cell).getUserObject();
			}
			return null;
		}
		return null;
	}

	/**
	 * Returns an list containing all graph cells taht have the same userObject.
	 * 
	 * @param userObject
	 *            The userObject for which all graph cels should be found.
	 * @return java.util.List
	 */
	private List<Object> getCellsWithUserObject(Object userObject) {
		List<Object> cells = new ArrayList<Object>();
		if (userObject != null) {
			// get the graph model, which contains all the cells of the graph
			GraphModel model = graph.getModel();
			// loop through all the cells of the model
			for (int i = 0; i < model.getRootCount(); i++) {
				Object root = model.getRootAt(i);
				// get the userObject for the curent graph cell
				Object rootUserObject = getUserObject(root);
				if (rootUserObject != null) {
					if (userObject == rootUserObject) {
						// the userObject of the current graph cell is teh same like the wanted userObject
						cells.add(root);
					}
				}
			}
		}
		return cells;
	}

	/**
	 * Returns an list containing all graph cells taht have the same userObject
	 * like the graph cell specified as the parameter.
	 * 
	 * @param cell
	 *            The graph cell with a userObject for which all graph cels
	 *            should be found.
	 * @return List
	 */
	private List<Object> getCellsWithUserObject(DefaultGraphCell cell) {
		List<Object> cells = new ArrayList<Object>();
		if (cell != null) {
			Object userObject = getUserObject(cell);
			cells.addAll(getCellsWithUserObject(userObject));
		}
		return cells;
	}

	/**
	 * Checks if a graph cell is currently in the selection list.
	 * 
	 * @param selection
	 *            An array of the selected cells.
	 * @param object
	 *            A graph cell that is being checked.
	 * @return boolean true if the object is in the selection. false if not.
	 */
	private boolean selected(Object[] selection, Object object) {
		// keep track if the object is foundin the array
		boolean found = false;
		int i = 0;
		// loop untill the end of array or untill the obect is foundin the array
		while ((i < selection.length) && (!found)) {
			found = selection[i++] == object;
		}
		return found;
	}

	/**
	 * 
	 * @param graphSelectionEvent
	 *            GraphSelectionEvent
	 * @param cell
	 *            Object
	 */
	private void handleSelection(GraphSelectionEvent graphSelectionEvent, Object cell) {
		if (selected(graphSelectionEvent.getCells(), cell)) {
			if (graphSelectionEvent.isAddedCell(cell)) {
				addAll(cell);
			} else {
				removeAll(cell);
			}
		}
	}
}
