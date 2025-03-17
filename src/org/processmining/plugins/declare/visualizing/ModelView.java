package org.processmining.plugins.declare.visualizing;

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

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.TreeNode;

import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.Port;

public abstract class ModelView {

	private static final Color BACKGROUND = new Color(235, 235, 235);
	protected transient List<DefaultGraphCell> cells = new ArrayList<DefaultGraphCell>();
	protected transient DGraph graph = new DGraph();

	public ModelView() {
		super();
		graph.setBackground(BACKGROUND);
	}

	public ModelView(final ModelView view) {
		super();
		graph.setBackground(BACKGROUND);
	}

	/**
	 * addVertex
	 * 
	 * @param cell
	 *            DefaultGraphCell
	 */
	public void addVertex(final DefaultGraphCell cell) {
		addCell(cell);
	}

	/**
	 * addEdge
	 * 
	 * @param cell
	 *            DefaultGraphCell
	 * @param source
	 *            DefaultGraphCell
	 * @param target
	 *            DefaultGraphCell
	 */
	public void addEdge(final DefaultGraphCell cell, final DefaultGraphCell source, final DefaultGraphCell target) {
		if (cell instanceof DefaultEdge) {
			if (cells.contains(source) && cells.contains(target)) {
				if (source.getChildCount() == 0) {
					source.addPort();
				}
				if (target.getChildCount() == 0) {
					target.addPort();
				}
				final DefaultEdge edge = (DefaultEdge) cell;
				edge.setSource(source.getChildAt(0));
				edge.setTarget(target.getChildAt(0));
				addCell(edge);
			}
		}
	}

	/**
	 * addEdge
	 * 
	 * @param cell
	 *            DefaultGraphCell
	 * @param source
	 *            DefaultGraphCell
	 * @param target
	 *            DefaultGraphCell
	 */
	public void addEdge(final DefaultGraphCell cell, final TreeNode source, final TreeNode target) {
		if (cell instanceof DefaultEdge) {
			final DefaultEdge edge = (DefaultEdge) cell;
			edge.setSource(source);
			edge.setTarget(target);
			addCell(edge);
		}
	}

	/**
	 * addCell
	 * 
	 * @param cell
	 *            DefaultGraphCell
	 */
	protected void addCell(final DefaultGraphCell cell) {
		if (!cells.contains(cell)) {
			cells.add(cell);
			graph.getGraphLayoutCache().insert(cell);
		}
	}

	/**
	 * getGraph
	 * 
	 * @return DGraph
	 */
	public DGraph getGraph() {
		return graph;
	}

	/**
	 * getModel
	 * 
	 * @return DGraphModel
	 */
	public GraphModel getModel() {
		// return model;
		return graph.getModel();
	}

	/**
	 * removeCell
	 * 
	 * @param cells
	 *            Object
	 */
	protected void removeCells(Object[] cells) {
		cells = graph.getDescendants(cells);
		getModel().remove(cells);
	}

	/**
	 * getCell
	 * 
	 * @param object
	 *            Object
	 * @return DefaultGraphCell
	 */
	protected List<DefaultGraphCell> getCells(final Object object) {
		final List<DefaultGraphCell> list = new ArrayList<DefaultGraphCell>();
		DefaultGraphCell cell = null;
		for (int i = 0; i < getModel().getRootCount(); i++) {
			final Object cellObject = getModel().getRootAt(i);
			if (cellObject instanceof DefaultGraphCell) {
				cell = (DefaultGraphCell) cellObject;
				final Object userObject = cell.getUserObject();
				if (userObject.equals(object)) {
					list.add(cell);
				}
			}
		}
		return list;
	}

	/**
	 * getCells
	 * 
	 * @return DefaultGraphCell
	 */
	protected List<DefaultGraphCell> getCells() {
		final List<DefaultGraphCell> list = new ArrayList<DefaultGraphCell>();
		DefaultGraphCell cell = null;
		for (int i = 0; i < getModel().getRootCount(); i++) {
			final Object cellObject = getModel().getRootAt(i);
			if (cellObject instanceof DefaultGraphCell) {
				cell = (DefaultGraphCell) cellObject;
				list.add(cell);
			}
		}
		return list;
	}

	/**
	 * clear
	 */
	protected void clear() {
		getModel().remove(getCells().toArray());
	}

	/**
	 * updateUI
	 */
	public void updateUI() {
		graph.invalidate();
		graph.validate();
		graph.refresh();
		graph.repaint();
	}

	/**
	 * updateUI
	 * 
	 * @param cell
	 *            DefaultGraphCell
	 */
	public void updateUI(final DefaultGraphCell cell) {
		final CellView view = graph.getGraphLayoutCache().getMapping(cell, true);
		graph.getGraphLayoutCache().refresh(view, true);
		graph.validate();
		graph.repaint();
	}

	/**
	 * getVertex
	 * 
	 * @param port
	 *            Port
	 * @return DVertex
	 */
	protected DVertex getVertex(final Port port) {
		DVertex vertex = null;
		final Object vertexObject = getModel().getParent(port);
		if (vertexObject != null) {
			if (vertexObject instanceof DVertex) {
				vertex = (DVertex) vertexObject;
			}
		}
		return vertex;
	}

	/**
	 * getVertexObject
	 * 
	 * @param port
	 *            Port
	 * @return ActivityDefinition
	 */
	public Object getVertexObject(final Port port) {
		Object object = null;
		final DVertex vertex = getVertex(port);
		if (vertex != null) {
			object = vertex.getUserObject();
		}
		return object;
	}

	/**
	 * vertexCells
	 * 
	 * @return List
	 */
	protected List<DVertex> vertexCells() {
		final List<DVertex> cells = new ArrayList<DVertex>();
		for (int i = 0; i < getModel().getRootCount(); i++) {
			final Object cell = getModel().getRootAt(i);
			if (cell instanceof DVertex) {
				cells.add((DVertex) cell);
			}
		}
		return cells;
	}

	/**
   *
   */
	public void clearSelection() {
		graph.getSelectionModel().clearSelection();
	}
}
