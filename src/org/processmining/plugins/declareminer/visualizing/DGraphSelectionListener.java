package org.processmining.plugins.declareminer.visualizing;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphModel;
import org.processmining.framework.util.ui.widgets.ProMSplitPane;
import org.processmining.framework.util.ui.widgets.ProMTable;
import org.processmining.plugins.declareminer.gui.ListRendererResult;

import com.fluxicon.slickerbox.factory.SlickerDecorator;
import com.fluxicon.slickerbox.factory.SlickerFactory;

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
		for (int i = 0; i < cells.length; i++) {
			handleSelection(graphSelectionEvent, cells[i]);
		}
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
				if (cell instanceof ActivityDefinitonCell) {
					

					ActivityDefinitonCell cde = (ActivityDefinitonCell)cell;
					
					
					
					
					Object splitPane = cde.getMetricsPanel();
					if(splitPane instanceof ProMSplitPane){
				//	if(splitPane!=null){
					JSplitPane split = (JSplitPane)((ProMSplitPane) splitPane).getComponent(0);
					//SlickerTabbedPane tabbed = (SlickerTabbedPane) jp.getComponent(3);
					JPanel jpan = (JPanel)split.getTopComponent();
					//JScrollPane jpScroll = (JScrollPane) jpan.getComponent(0);
					//JPanel jp = (JPanel) jpScroll.getViewport().getView();
					JScrollPane jpScroll = (JScrollPane) jpan.getComponent(0);
					JPanel jp = (JPanel) jpScroll.getViewport().getView();
					JPanel radioPanel = (JPanel) jp.getComponent(1);
					jp.removeAll();
					
					Vector labels = cde.getLabels();
					JList labelsList = new JList();
					ListRendererResult renderer = new ListRendererResult();
					labelsList.setCellRenderer(renderer);
					Vector cellList = new Vector();
					for (int i = 0; i < labels.size(); i++) {
							Cell cellL = new Cell();
							cellL.setLabel(((JLabel)labels.get(i)).getText());
							cellL.setSelected(false);
							cellList.add(cellL);
						//}
					}
					
					labelsList.setModel(new CellModel(cellList));
					labelsList.setSelectionBackground(new Color(0, 100, 0));
					labelsList.setSelectedIndex(0); //It's easier to see the focus change
					//if an item is selected.
					labelsList.setBackground(new Color(160, 160, 160));
					labelsList.setSelectionForeground(Color.white);
					labelsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					//final Vector tempTems = templs;
					//	SlickerFactory sf = SlickerFactory.instance();
					//	SlickerTabbedPane tabbed = sf.createTabbedPane("filtering");
					//	tabbed.addTab("templates", new JPanel());


					JScrollPane scroll = new JScrollPane(labelsList);
					scroll.setOpaque(false);
					scroll.getViewport().setOpaque(false);
					scroll.setBorder(BorderFactory.createEmptyBorder());
					scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
					SlickerDecorator.instance().decorate(scroll.getVerticalScrollBar(), new Color(0, 0, 0, 0),
							new Color(140, 140, 140), new Color(80, 80, 80));
					scroll.getVerticalScrollBar().setOpaque(false);

					SlickerDecorator.instance().decorate(scroll.getHorizontalScrollBar(), new Color(0, 0, 0, 0),
							new Color(140, 140, 140), new Color(80, 80, 80));
					scroll.getHorizontalScrollBar().setOpaque(false);
					jp.setLayout(new TableLayout(new double[][] { { TableLayoutConstants.FILL}, {200, 150 , 75, 75, TableLayout.FILL}}));
					jp.add(scroll, "0,0");
					jp.add(radioPanel,"0,1");
					Vector colNams = new Vector();
					colNams.add("Activations");
					colNams.add("Fulfilments");
					colNams.add("Violations");
					colNams.add("Conflicts");
					Vector data = new Vector();
					data.add(new Vector());
					
					TableModel dtm = new DefaultTableModel(data,colNams);
					ProMTable pt = new ProMTable(dtm);
					jp.add(pt,"0,2");
					colNams = new Vector();
					colNams.add("Max delay");
					colNams.add("Min delay");
					colNams.add("Avg delay");
					colNams.add("StdDev delay");
					data = new Vector();
					Vector row = new Vector();
					data.add(row);
					dtm = new DefaultTableModel(data,colNams);
					pt = new ProMTable(dtm);
					jp.add(pt,"0,3");
					
					SlickerFactory sf = SlickerFactory.instance();
					
					jp.add(sf.createRoundedPanel(), "0,4");
					//jp.add(radioPanel,"0,1");
					jp.repaint();

					
					
					JScrollPane rightTopPanelScroll = new JScrollPane(jp);  
					rightTopPanelScroll.setOpaque(false);
					rightTopPanelScroll.getViewport().setOpaque(false);
					rightTopPanelScroll.setBorder(BorderFactory.createEmptyBorder());
					rightTopPanelScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					rightTopPanelScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
					SlickerDecorator.instance().decorate(rightTopPanelScroll.getVerticalScrollBar(), new Color(0, 0, 0, 0),
							new Color(140, 140, 140), new Color(80, 80, 80));
					rightTopPanelScroll.getVerticalScrollBar().setOpaque(false);

					SlickerDecorator.instance().decorate(rightTopPanelScroll.getHorizontalScrollBar(), new Color(0, 0, 0, 0),
							new Color(140, 140, 140), new Color(80, 80, 80));
					rightTopPanelScroll.getHorizontalScrollBar().setOpaque(false);

					
					rightTopPanelScroll.setSize(new Dimension(300, 300));
					rightTopPanelScroll.setPreferredSize(new Dimension(300, 300));
					rightTopPanelScroll.setMinimumSize(new Dimension(300, 300));
					rightTopPanelScroll.setMaximumSize(new Dimension(300, 300));
					
					
					((ProMSplitPane) splitPane).setTopComponent(rightTopPanelScroll);
					((ProMSplitPane) splitPane).setBottomComponent(sf.createRoundedPanel());
					((ProMSplitPane) splitPane).repaint();
					JPanel main = cde.getMainPanel();
					main.revalidate();
					main.repaint();
					
					
					
					
					
					
					}else{
						
					}
					
					
					
				}
				addAll(cell);
		//		}
			} else {
				removeAll(cell);
			}
			}
		
	}
}
