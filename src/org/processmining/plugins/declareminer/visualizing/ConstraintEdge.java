package org.processmining.plugins.declareminer.visualizing;

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

import java.awt.Color;

public class ConstraintEdge extends ConstraintDefinitionEdge {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1817124703089993470L;
	private final float DEFAULT_LINE_WIDTH = 1.75f;

	public ConstraintEdge(ConstraintDefinition constraint, ActivityDefinition real, Parameter parameter) {
		super(constraint, real, parameter);
		refresh();
	}

	protected float getLineWidth() {
		return DEFAULT_LINE_WIDTH;
	}

	private Constraint getConstraint() {
		Object object = getUserObject();
		if (object instanceof Constraint) {
			return (Constraint) object;
		}
		return null;
	}

	void setColor() {
		Constraint constraint = getConstraint();
		if (constraint != null) {
			super.setColor(getColor(constraint), getColor(constraint), getColor(constraint));
		}
	}

	/**
	 * 
	 * @param constraint
	 *            Constraint
	 * @return Color
	 */
	private Color getColor(Constraint constraint) {
		Color color = DGraphConstants.trueTemporaryColor();
		if (constraint != null) {
			if (constraint.getStatus()) {
				if (constraint.getState().equals(State.VIOLATED_TEMPORARY)) {
					color = DGraphConstants.falseTemporaryColor();
				} else if (constraint.getState().equals(State.VIOLATED)) {
					color = DGraphConstants.falsePermanentColor();
				}
			} else {
				color = DGraphConstants.unvalidColor();
			}
		}
		return color;
	}

	public void refresh() {
		this.setColor();
	}

	public String getToolTipString() {

		Constraint constraint = getConstraint();

		String tooltip = "<html>" + "<table border=\"1\" width=\"500\" style=\"border-collapse: collapse\"> " + "<tr> "
				+ "   <td width=\"102\">Description</td>" + "	<td>" + constraint.getDescription() + "</td>" + "</tr>"
				+ "<tr>" + "<td width=\"102\">State</td>" + "<td>" + constraint.getState().name() + "<p>"
				+ constraint.getState().getDescription() + "</td>" + "</tr>" + "<tr>"
				+ "<td width=\"102\">State message</td>" + "<td>" + constraint.getCurrentStateMessage() + "</td>"
				+ "</tr>" + "</table>" + "</html>";
		return tooltip;
	}

}
