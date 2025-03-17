/* Generated by Together */

package org.processmining.plugins.declare.visualizing;

public class Assignment extends AssignmentModel {

	private int id = 0;

	private State state;

	/**
	 * Assignment
	 * 
	 * @param anCase
	 *            Case
	 * @param anAssignmentModel
	 *            AssignmentModel
	 */
	public Assignment(int ID, AssignmentModel anAssignmentModel) {
		super(anAssignmentModel);
		id = ID;

	}

	public Assignment clone(int ID) {
		Assignment clone = new Assignment(ID, this);

		return clone;
	}

	public boolean equals(Object obj) {
		boolean equals = false;
		if (obj instanceof Assignment) {
			Assignment a = (Assignment) obj;
			equals = id == a.getId();
		}
		return equals;
	}

	/**
	 * addactivityDefinition
	 * 
	 * @return activityDefinition
	 * @param activityDefinition
	 *            ActivityDefinition
	 */
	protected boolean addActivityDefinition(ActivityDefinition activityDefinition) {
		return super.addActivityDefinition(new Activity((ActivityDefinition) activityDefinition.clone(), this));
	}

	public boolean addConstraintDefiniton(ConstraintDefinition constraintDefinition) {
		return super.addConstraintDefiniton(new Constraint(constraintDefinition, this));
	}

	/**
	 * jobExists
	 * 
	 * @param activity
	 *            Job
	 * @return boolean
	 */
	public boolean activityExists(Activity activity) {
		return super.ActivityDefinitionExists(activity);
	}

	/**
	 * activityDefinitionAt
	 * 
	 * @param anIndex
	 *            int
	 * @return Job
	 */
	public Activity activityAt(int anIndex) {
		ActivityDefinition definition = super.activityDefinitionAt(anIndex);
		return toActivity(definition);
	}

	private Activity toActivity(ActivityDefinition definition) {
		Activity activity = null;
		if (definition != null) {
			activity = (Activity) definition;
		}
		return activity;
	}

	/**
	 * 
	 * @param name
	 *            int
	 * @return Activity
	 */
	public Activity activityWithName(String name) {
		ActivityDefinition definition = super.activityDefinitionWithName(name);
		return toActivity(definition);
	}

	/**
	 * jobsCount
	 * 
	 * @return int
	 */
	public int activitiesCount() {
		return super.activityDefinitionsCount();
	}

	/**
	 * add
	 * 
	 * @param element
	 *            DataElement
	 * @return boolean
	 */

	public String toString() {
		return Integer.toString(id) + ": " + getName();
	}

	public int getId() {
		return id;
	}

	/**
	 * activityDefinitionAt
	 * 
	 * @param anIndex
	 *            int
	 * @return Job
	 */
	public Constraint constraintAt(int anIndex) {
		return (Constraint) super.constraintDefinitionAt(anIndex);
	}

	/**
	 * 
	 * @param id
	 *            int
	 * @return Constraint
	 */
	public Constraint constraintWithId(int id) {
		return (Constraint) super.constraintWithId(id);
	}

	/**
	 * isTrue()
	 * 
	 * @return boolean
	 */
	public boolean isTrue() {
		boolean ok = true;
		int i = 0;
		while ((i < constraintDefinitionsCount()) && ok) {
			Constraint constraint = (Constraint) super.constraintDefinitionAt(i++);
			ok = constraint.getState().equals(State.SATISFIED);
		}
		return ok;
	}

	/**
	 * isTrue()
	 * 
	 * @return DefaultState
	 */
	public State getState() {
		return state;
	}

	public void setState(State state) {
		if (state != null) {
			this.state = state;
		}
	}

	/**
	 * 
	 * @return int
	 */
	public int hashCode() {
		int hash = 7;
		int var_code = getId();
		hash = (31 * hash) + var_code;
		return hash;
	}

	/**
	 * 
	 * @param definition
	 *            ActivityDefinition
	 * @return Activity
	 */
	public Activity getActivityForDefinition(ActivityDefinition definition) {
		return toActivity(definition);
	}

	/**
	 * 
	 * @param activity
	 *            Activity
	 * @param user
	 *            User
	 * @return boolean
	 */
}
