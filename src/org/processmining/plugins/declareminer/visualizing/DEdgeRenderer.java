package org.processmining.plugins.declareminer.visualizing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Area;
import java.awt.geom.Point2D;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.CellView;
import org.jgraph.graph.EdgeRenderer;
import org.jgraph.graph.GraphConstants;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class DEdgeRenderer extends EdgeRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2245131380109364539L;

	protected transient Color middleColor;

	/** Painting attributes of the current edgeview */
	protected transient int lineNumber;
	protected transient float width;

	/**
	 * Constructs a renderer that may be used to render edges.
	 */
	public DEdgeRenderer() {
		super();
	}

	/**
	 * Paint the current view's direction. Sets tmpPoint as a side-effect such
	 * that the invoking method can use it to determine the connection point to
	 * this decoration.
	 * 
	 * @param size
	 *            int
	 * @param style
	 *            int
	 * @param src
	 *            Point2D
	 * @param dst
	 *            Point2D
	 * @return Shape
	 */
	@Override
	protected Shape createLineEnd(final int size, final int style, final Point2D src, final Point2D dst) {
		if ((src == null) || (dst == null)) {
			return null;
		}
		if (style != DGraphConstants.ARROW_TECHNICAL_CIRCLE) {
			return super.createLineEnd(size, style, src, dst);
		} else {
			final Area areaCircle = new Area(super.createLineEnd(size, GraphConstants.ARROW_CIRCLE, src, dst));
			final Shape arrow = super.createLineEnd(size, GraphConstants.ARROW_TECHNICAL, src, dst);
			final Area areaPoly = new Area(arrow);
			areaCircle.add(areaPoly);
			return areaCircle;
		}
	}

	/**
	 * Installs the attributes of specified cell in this renderer instance. This
	 * means, retrieve every published key from the cells hashtable and set
	 * global variables or superclass properties accordingly.
	 * 
	 * @param view
	 *            the cell view to retrieve the attribute values from.
	 */

	@Override
	protected void installAttributes(final CellView view) {
		super.installAttributes(view);
		final AttributeMap map = view.getAllAttributes();
		lineNumber = DGraphConstants.getLineNumber(map);

		width = lineWidth;
		lineWidth = switchWidth(lineNumber);

	}

	/**
	 * getDEdgeView
	 * 
	 * @return DEdgeView
	 */
	public DEdgeView getDEdgeView() {
		if (view instanceof DEdgeView) {
			return (DEdgeView) view;
		}
		return null;
	}

	/*
	 * rewritten method paint from EdgeRenderer. Added extra edge in them middle
	 * of fifferent color.
	 */
	@Override
	public void paint(final Graphics g) {
		final Graphics2D g2 = (Graphics2D) g;
		super.paint(g);
		drawLines(g2);
	}

	/**
	 * @param g2
	 *            Graphics2D
	 */
	private void drawLines(final Graphics2D g2) {
		if (lineNumber > 1) {
			Color color = getForeground();
			for (int i = lineNumber - 1; i > 0; i--) {
				final float w = switchWidth(i);
				color = switchColor(color);
				drawLine(g2, w, color);
			}
		}
	}

	private float switchWidth(final int number) {
		return width * ((2 * number) - 1);
	}

	private Color switchColor(final Color color) {
		Color c = getForeground();
		if (color != null) {
			if (color.equals(this.getBackground())) {
				c = this.getForeground();
			} else {
				c = this.getBackground();
			}
		}
		return c;
	}

	/**
	 * @param g2
	 *            Graphics2D
	 * @param width
	 *            int
	 * @param color
	 *            Color
	 */
	private void drawLine(final Graphics2D g2, final float width, final Color color) {
		if (view.lineShape != null) {
			g2.setColor(color);
			g2.setStroke(getStroke(width));
			g2.draw(view.lineShape);
		}
	}

	private Stroke getStroke(final float width) {
		Stroke s = null;
		if (lineDash != null) { // Dash For Line Only
			s = new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, lineDash, dashOffset);
		} else {
			s = new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
		}

		if (selected) { // Paint Selected
			s = GraphConstants.SELECTION_STROKE;
		}
		return s;
	}
}
