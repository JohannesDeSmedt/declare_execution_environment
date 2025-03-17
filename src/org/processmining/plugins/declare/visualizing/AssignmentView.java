package org.processmining.plugins.declare.visualizing;

import java.util.Iterator;

import org.jgraph.graph.DefaultGraphCell;

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
public class AssignmentView extends AssignmentModelView implements IAssignmentMarqueeListener {

	public AssignmentView(AssignmentModel aModel) {
		super(aModel);
		// TODO Auto-generated constructor stub
	}

	// AssignmentMarqueeHandler marqueeHandler = null;

	/**
	 * @param state
	 *            Assignment
	 */

	@Override
	protected ActivityDefinitonCell createActivityCell(final ActivityDefinition activityDefinition, final int x,
			final int y) {
		final ActivityCell view = new ActivityCell((Activity) activityDefinition, x, y);
		return view;
	}

	@Override
	protected ConstraintDefinitionEdge createConstraintCell(final ConstraintDefinition constraint,
			final ActivityDefinition branch, final Parameter formal) {
		return new ConstraintEdge(constraint, branch, formal);
	}

	/**
	 * @param listener
	 *            IAssignmentViewListener
	 */

	/**
	 * @return Color
	 */

	/**
   *
   */

	/**
	 * @param constraint
	 *            Constraint
	 */
	private void constraintState(final Constraint constraint) {
		if (constraint != null) {
			final Iterator<DefaultGraphCell> iterator = this.getCells(constraint).iterator(); // get all cells that are
			// assigned to teh constrint
			// this is important when constraint is branched, because
			// there are three edsed and a vertex for that constraint.
			while (iterator.hasNext()) { // for all cells set the new color
				final DefaultGraphCell cell = iterator.next();
				if (cell instanceof DEdge) {
					((DEdge) cell).refresh();
				}
			}
		}
	}

	/**
	 * @return AssignmentState
	 */

	/**
	 * @param activity
	 *            Activity
	 */
	private void enableActivity(final Activity activity) {
		if (activity != null) {
			final Iterator<DefaultGraphCell> iterator = this.getCells(activity).iterator();
			while (iterator.hasNext()) {
				final DefaultGraphCell cell = iterator.next();
				if (cell instanceof ActivityCell) {
					((ActivityCell) cell).setEnabled(true);
				}
			}
		}
	}

	/**
	 * @param activity
	 *            Activity
	 */
	private void disableActivity(final Activity activity) {
		if (activity != null) {
			final Iterator<DefaultGraphCell> iterator = this.getCells(activity).iterator();
			while (iterator.hasNext()) {
				final DefaultGraphCell cell = iterator.next();
				if (cell instanceof ActivityCell) {
					((ActivityCell) cell).setEnabled(false);
				}
			}
		}
	}

	/**
   *
   */

	/**
	 * @param activity
	 *            Activity
	 */
	private void stateActivity(final Activity activity, final boolean start, final boolean complete) {
		if (activity != null) {
			final Iterator<DefaultGraphCell> iterator = this.getCells(activity).iterator();
			while (iterator.hasNext()) {
				final DefaultGraphCell cell = iterator.next();
				if (cell instanceof ActivityCell) {
					((ActivityCell) cell).setState(start, complete);
				}
			}
		}
	}

	public void cellDoubleClicked(DefaultGraphCell cell) {
		// TODO Auto-generated method stub

	}
}
