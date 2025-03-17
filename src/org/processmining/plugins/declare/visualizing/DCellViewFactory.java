package org.processmining.plugins.declare.visualizing;

import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.PortView;
import org.jgraph.graph.VertexView;

/**
 * <p>
 * Title: DECLARE
 * </p>
 * 
 * <p>
 * Description:
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
public class DCellViewFactory extends DefaultCellViewFactory {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8064103123888186409L;

	/**
	 * Constructs an EdgeView view for the specified object.
	 * 
	 * @param cell
	 *            Object
	 * @return EdgeView
	 */
	protected EdgeView createEdgeView(Object cell) {
		if (cell instanceof ConstraintDefinitionEdge) {
			return new ConstraintView(cell);
		} else {
			return new DEdgeView(cell);
		}
	}

	protected VertexView createVertexView(Object object) {
		if (object instanceof TransparentCell) {
			return new TransparentCellView(object);
		}
		return super.createVertexView(object);
	}

	/**
	 * Constructs a PortView view for the specified object.
	 * 
	 * @param cell
	 *            Object
	 * @return PortView
	 */
	protected PortView createPortView(Object cell) {
		if (cell instanceof TransparentPort) {
			return new TransparentPortView(cell);
		} else {
			return new PortView(cell);
		}
	}
}
