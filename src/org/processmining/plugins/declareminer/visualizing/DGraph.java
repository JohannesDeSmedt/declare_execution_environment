package org.processmining.plugins.declareminer.visualizing;

import java.awt.event.MouseEvent;

import javax.swing.ToolTipManager;

import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultGraphModel;

/**
 * <p>
 * Title: DECLARE
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: TU/e
 * </p>
 * 
 * @author Maja Pesic
 * @version 1.0
 */
public class DGraph extends JGraph {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5250029168745239406L;

	public DGraph() {
		super(new DefaultGraphModel());
		setGraph();
	}

	/**
	 * setGraph
	 */
	private void setGraph() {

		// Enable edit without final RETURN keystroke
		setInvokesStopCellEditing(true);

		getGraphLayoutCache().setFactory(new DCellViewFactory());

		// When over a cell, jump to its default port (we only have one, anyway)
		setJumpToDefaultPort(true);

		ToolTipManager.sharedInstance().registerComponent(this); // enable tool tips
		ToolTipManager.sharedInstance().setDismissDelay(40000); // let tooltips be displayd for 40 seconds
	}

	@Override
	public void refresh() {
		final CellView[] views = getGraphLayoutCache().getCellViews();
		getGraphLayoutCache().refresh(views, true);
		super.refresh();
	}

	@Override
	public String getToolTipText(final MouseEvent event) {
		final Object cell = getFirstCellForLocation(event.getX(), event.getY());
		if (cell instanceof ConstraintEdge) {
			return ((ConstraintEdge) cell).getToolTipString();
		}
		return null;
	}

}
