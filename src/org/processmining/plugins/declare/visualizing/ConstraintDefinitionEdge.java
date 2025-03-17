package org.processmining.plugins.declare.visualizing;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;

import org.jgraph.graph.AbstractCellView;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphConstants;

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
public class ConstraintDefinitionEdge extends DEdge {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6482878316795733721L;
	private static final float[] LINE_MANDATORY = null; //{ 10, 0};
	private static final float[] LINE_OPTIONAL = { 5, 5 };

	private final ActivityDefinition parameter;

	private boolean label = false;
	
	private JPanel metricsPanel;
	private Vector labels;
	private JPanel mainPanel;
	
	public JPanel getMainPanel() {
		return mainPanel;
	}

	public void setMainPanel(JPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	public ConstraintDefinitionEdge(ConstraintDefinition constraint, ActivityDefinition parameter, Parameter formal) {
		super(constraint, formal.getStyle());
		this.parameter = parameter;
		update();
		GraphConstants.setDisconnectable(getAttributes(), false);
		if (constraint.isUnary()) {
			GraphConstants.setRouting(getAttributes(), new UnaryRouting());
		}
		//setDisplay();
	}

	public void setLabel(boolean l) {
		label = l;
		setDisplay();
	}

	/**
	 * updateMandatory
	 */
	public void updateMandatory() {
		Object object = getUserObject();
		if (object instanceof ConstraintDefinition) {
			float[] line = LINE_MANDATORY;
			if (!((ConstraintDefinition) object).getMandatory()) {
				line = LINE_OPTIONAL;
			}
			super.setLineDash(line);
		}
	}

	public void update() {
		updateMandatory();
		setDisplay(); // *** change the display
	}

	/**
	 * getConstraintDefinition
	 * 
	 * @return ConstraintDefinition
	 */
	public ConstraintDefinition getConstraintDefinition() {
		ConstraintDefinition constraintDefiniton = null;
		Object userObject = getUserObject();
		if (userObject != null) {
			if (userObject instanceof ConstraintDefinition) {
				constraintDefiniton = (ConstraintDefinition) userObject;
			}
		}
		return constraintDefiniton;
	}
	
	public void setMetricsPanel(JPanel metricsPanel) {
		this.metricsPanel = metricsPanel;
	}
	
	public JPanel getMetricsPanel() {
		return metricsPanel;
	}
	
	public void setLabels(Vector labels) {
		this.labels = labels;
	}
	
	public Vector getLabels() {
		return labels;
	}

	public ActivityDefinition getParameter() {
		return parameter;
	}

	public void setColor(Color linecolor, Color forecolor, Color backcolor) {
		if (linecolor != null) {
			GraphConstants.setLineColor(getAttributes(), linecolor);
			GraphConstants.setForeground(getAttributes(), forecolor);
			GraphConstants.setBackground(getAttributes(), backcolor);
		}
	}

	/**
	 * setDisplay
	 */
	private void setDisplay() {
		ConstraintDefinition constraint = getConstraintDefinition();
		if ((constraint != null) && label) {
			Object[] labels = { new String(constraint.getDisplay()) };
			if(constraint.getDisplay().contains("@")){
				 labels = constraint.getDisplay().split("@");
			}
			double x = GraphConstants.PERMILLE / 2;
			double y = 10;

			if (getConstraintDefinition().isUnary()) {
				x = GraphConstants.PERMILLE / 4;
				y = 12;
			}
			;

			Point2D[] labelPositions = new Point2D[]{ new Point2D.Double(x, y) };
			if(constraint.getDisplay().contains("@")){
				labelPositions = new Point2D[]{ new Point2D.Double(x, y), new Point2D.Double(x, y+20)};
			}
			GraphConstants.setExtraLabelPositions(getAttributes(), labelPositions);
			GraphConstants.setExtraLabels(getAttributes(), labels);
		} else {
			GraphConstants.setExtraLabelPositions(getAttributes(), new Point2D[] {});
			GraphConstants.setExtraLabels(getAttributes(), new Object[] {});
		}
	}

	public static class UnaryRouting extends DefaultEdge.LoopRouting {

		/**
	 * 
	 */
		private static final long serialVersionUID = 6071223636488430899L;

		protected List<Object> routeLoop(EdgeView edge) {
			List<Object> newPoints = new ArrayList<Object>();
			newPoints.add(edge.getSource());
			CellView sourceParent = (edge.getSource() != null) ? edge.getSource().getParentView() : edge
					.getSourceParentView();
			if (sourceParent != null) {
				Point2D from = AbstractCellView.getCenterPoint(sourceParent);
				Rectangle2D rect = sourceParent.getBounds();
				double width = rect.getWidth();
				double height2 = rect.getHeight() / 2;
				double loopWidth = Math.min(20, Math.max(10, width / 4));
				double loopHeight = Math.min(30, Math.max(12, Math.max(loopWidth, height2 / 2)));
				Point2D pointLeft = edge.getAttributes().createPoint(from.getX() - loopWidth,
						from.getY() - height2 - (loopHeight * 1.2));
				Point2D pointRight = edge.getAttributes().createPoint(from.getX() + loopWidth,
						from.getY() - height2 - (loopHeight * 1.2));
				newPoints.add(pointLeft);
				newPoints.add(pointRight);
				newPoints.add(edge.getTarget());
				return newPoints;
			}
			return null;
		}

		public int getPreferredLineStyle(EdgeView edge) {
			if (edge.isLoop()) {
				return getUnaryStyle();
			}
			return getEdgeStyle();
		}

		protected int getUnaryStyle() {
			return GraphConstants.STYLE_ORTHOGONAL;
		}
	}

}
