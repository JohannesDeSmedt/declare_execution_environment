package org.processmining.plugins.declare.visualizing;

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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Constraint extends ConstraintDefinition {

	private State state;

	private final Collection<IConstraintListener> listeners;
	private boolean status;

	public Constraint(ConstraintDefinition definiton, Assignment assignment) {
		super(definiton);
		setAssignmentModel(assignment);
		listeners = new ArrayList<IConstraintListener>();
		state = State.getDefault();

	}

	public Object clone() {
		Constraint clone = null;
		try {
			clone = new Constraint(this, getAssignment());
			clone.listeners.addAll(listeners);
			clone.state = state;
		} catch (Exception ex) {
		}
		return clone;
	}

	public void addListener(IConstraintListener l) {
		listeners.add(l);
	}

	private void changed() throws Throwable {
		Iterator<IConstraintListener> iterator = listeners.iterator();
		while (iterator.hasNext()) {
			iterator.next().changed( /* this */);
		}
	}

	public Assignment getAssignment() {
		return (Assignment) getAssignmentModel();
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		if (state != null) {
			this.state = state;
		}
	}

	/**
	 * checkCondition
	 * 
	 * @return boolean
	 */

	public boolean getStatus() {
		return status;
	}

	public boolean isActive() {
		// only mandatory         discard when permanetly false  when condition is true
		// return (getMandatory() && !getState().isFalsePermanet() && getStatus());
		return (getMandatory() && getStatus());
	}

	public void setStatus(boolean status) {
		boolean old = this.status;
		this.status = status;
		if (old != status) {
			try {
				changed();
			} catch (Throwable ex) {
			}
		}
	}

	public String getCurrentStateMessage() {
		return getStateMessage(state);
	}

}
