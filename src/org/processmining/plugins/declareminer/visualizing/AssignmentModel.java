/**
 *
 */

package org.processmining.plugins.declareminer.visualizing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class AssignmentModel {

	private final Language language;
	private final List<ActivityDefinition> activities;
	private final List<ConstraintDefinition> constraints;
	private AssignmentModelListener listener;

	private String name;

	private final HashMap<String, String> attributes;

	/**
	 * AssignmentModel
	 * 
	 * @param lang
	 *            Language
	 */
	public AssignmentModel(final Language lang) {
		super();
		language = lang;
		name = "test";
		activities = Collections.synchronizedList(new ArrayList<ActivityDefinition>());
		constraints = Collections.synchronizedList(new ArrayList<ConstraintDefinition>());

		attributes = new HashMap<String, String>();
		listener = null;
	}

	public AssignmentModel(final AssignmentModel model) {
		super();
		language = model.language;
		activities = Collections.synchronizedList(new ArrayList<ActivityDefinition>());
		constraints = Collections.synchronizedList(new ArrayList<ConstraintDefinition>());

		listener = null;
		name = model.name;
		attributes = new HashMap<String, String>();
		attributes.putAll(model.attributes);
		createActivities(model);
		createConstraints(model);
	}

	@Override
	public Object clone() {
		final AssignmentModel clone = new AssignmentModel(language);
		clone.name = name;
		synchronized (activities) {
			final Iterator<ActivityDefinition> ia = activities.iterator();
			while (ia.hasNext()) {
				clone.addActivityDefinition(ia.next());
			}
		}
		synchronized (constraints) {
			final Iterator<ConstraintDefinition> ic = constraints.iterator();
			while (ic.hasNext()) {
				try {
					clone.addConstraintDefiniton(ic.next());
				} catch (final Exception ex) {
				}
			}
		}

		clone.attributes.putAll(attributes);
		return clone;
	}

	public Collection<ConstraintDefinition> getConstraintDefinitions() {
		return constraints;
	}

	/**
	 * addactivityDefinition
	 * 
	 * @return activityDefinition
	 */
	public ActivityDefinition addActivityDefinition() {
		final ActivityDefinition activityDefinition = new ActivityDefinition(nextActivityDefinitionId(), this);
		activityDefinition.setName("activity " + activityDefinition.getId());
		if (this.addActivityDefinition(activityDefinition)) {
			return activityDefinition;
		}
		return null;
	}

	private void createActivities(final AssignmentModel assignmentModel) {
		for (int i = 0; i < assignmentModel.activityDefinitionsCount(); i++) {
			final ActivityDefinition activityDefinition = assignmentModel.activityDefinitionAt(i);
			this.addActivityDefinition(activityDefinition);
		}
	}

	/**
	 * addactivityDefinition
	 * 
	 * @return activityDefinition
	 * @param activityDefinition
	 *            ActivityDefinition
	 */
	protected boolean addActivityDefinition(final ActivityDefinition activityDefinition) {
		if (activities.add(activityDefinition)) {
			if (listener != null) {
				listener.addActivityDefinition(activityDefinition);
			}
			return true;
		}
		return false;
	}

	/**
	 * addactivityDefinition
	 * 
	 * @return ActivityDefinition
	 * @param id
	 *            int
	 */
	public ActivityDefinition addActivityDefinition(final int id) {
		final ActivityDefinition anActivityDefinition = new ActivityDefinition(id, this);
		anActivityDefinition.setName("job " + anActivityDefinition.getId());
		if (activities.add(anActivityDefinition)) {
			if (listener != null) {
				listener.addActivityDefinition(anActivityDefinition);
			}
			return anActivityDefinition;
		}
		return null;
	}

	/**
	 * ActivityDefinitionExists
	 * 
	 * @param anActivityDefinition
	 *            ActivityDefinition
	 * @return boolean
	 */
	public boolean ActivityDefinitionExists(final ActivityDefinition anActivityDefinition) {
		return activities.contains(anActivityDefinition);
	}

	/**
	 * deleteActivityDefinition
	 * 
	 * @param anActivityDefinition
	 *            ActivityDefinition
	 * @return boolean
	 */
	public List<Object> deleteActivityDefinition(final ActivityDefinition anActivityDefinition) {
		final List<Object> list = new ArrayList<Object>();
		if (ActivityDefinitionExists(anActivityDefinition)) {
			if (activities.remove(anActivityDefinition)) {
				if (listener != null) {
					listener.deleteActivityDefinition(anActivityDefinition);
				}
				list.add(anActivityDefinition);
				list.addAll(deleteConstraintDefinitions(anActivityDefinition));
			}
		}
		return list;
	}

	/**
	 * constraintExists
	 * 
	 * @param constraint
	 *            LTLConstraintDefinition
	 * @return boolean
	 */
	public boolean constraintDefinitionExists(final ConstraintDefinition constraint) {
		return constraints.contains(constraint);
	}

	/**
	 * deleteConstraint
	 * 
	 * @param constraint
	 *            LTLConstraintDefinition
	 * @return boolean
	 */
	public List<Object> deleteConstraintDefinition(final ConstraintDefinition constraint) {
		final List<Object> list = new ArrayList<Object>();
		if (constraintDefinitionExists(constraint)) {
			if (constraints.remove(constraint)) {
				if (listener != null) {
					listener.deleteConstraintDefiniton(constraint);
				}
				list.add(constraint);
			}
		}
		return list;
	}

	/**
	 * jobsCount
	 * 
	 * @return int
	 */
	public int activityDefinitionsCount() {
		return activities.size();
	}

	/**
	 * activityDefinitionAt
	 * 
	 * @param anIndex
	 *            int
	 * @return ActivityDefinition
	 */
	public ActivityDefinition activityDefinitionAt(final int anIndex) {
		ActivityDefinition activityDefinition = null;
		if (anIndex < activityDefinitionsCount()) {
			activityDefinition = activities.get(anIndex);
		}
		return activityDefinition;
	}

	public Iterable<ActivityDefinition> getActivityDefinitions() {
		return activities;
	}

	/**
	 * nextTemplateId
	 * 
	 * @return int
	 */
	private int nextActivityDefinitionId() {
		int id = 0;
		ActivityDefinition activityDefinition = null;
		synchronized (activities) {
			final Iterator<ActivityDefinition> it = activities.iterator();
			while (it.hasNext()) {
				activityDefinition = it.next();
				if (id < activityDefinition.getId()) {
					id = activityDefinition.getId();
				}
			}
		}
		return ++id;
	}

	/**
	 * nextTemplateId
	 * 
	 * @return int
	 */
	private int nextConstraintDefinitionId() {
		int id = 0;
		ConstraintDefinition constraintDefinition = null;
		synchronized (constraints) {
			final Iterator<ConstraintDefinition> it = constraints.iterator();
			while (it.hasNext()) {
				constraintDefinition = it.next();
				if (id < constraintDefinition.getId()) {
					id = constraintDefinition.getId();
				}
			}
		}
		return ++id;
	}

	/**
	 * constraintsCount
	 * 
	 * @return int
	 */
	public int constraintDefinitionsCount() {
		return constraints.size();
	}

	/**
	 * constraintAt
	 * 
	 * @param anIndex
	 *            int
	 * @return ConstraintDefinition
	 */
	public ConstraintDefinition constraintDefinitionAt(final int anIndex) {
		ConstraintDefinition constraintDefinition = null;
		if (anIndex < constraintDefinitionsCount()) {
			constraintDefinition = constraints.get(anIndex);
		}
		return constraintDefinition;
	}

	/**
	 * @param id
	 *            int
	 * @return Constraint
	 */
	public ConstraintDefinition constraintWithId(final int id) {
		ConstraintDefinition constraint = null;
		boolean found = false;
		synchronized (constraints) {
			final Iterator<ConstraintDefinition> iterator = constraints.iterator();
			while (iterator.hasNext() && !found) {
				constraint = iterator.next();
				found = constraint.getId() == id;
			}
		}
		return found ? constraint : null;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	/**
	 * Adds a binary constraint defintion.
	 * 
	 * @param template
	 *            ConstraintTemplate
	 * @return ConstraintDefinition
	 */
	public ConstraintDefinition createConstraintDefinition(final ConstraintTemplate template) {
		try {
			return new ConstraintDefinition(nextConstraintDefinitionId(), this, template);
		} catch (final Throwable t) {
			t.printStackTrace();
			return null;
		}
	}

	/**
	 * Adds a unary constraint defintion.
	 * 
	 * @param constraint
	 *            int
	 * @return ConstraintDefinition / /* public ConstraintDefinition
	 *         addConstraintDefinition(int id, ConstraintTemplate template,
	 *         ActivityDefinition param) { ConstraintDefinition constraint =
	 *         null; if (template != null && param != null) { if
	 *         (template.isUnary() && param != null) { constraint = new
	 *         ConstraintDefinition(id, this, template);
	 *         constraint.addParameter(param); } } if (constraint != null) if
	 *         (this.addConstraintDefiniton(constraint)) { return constraint; }
	 *         return null; }
	 */
	public boolean addConstraintDefiniton(final ConstraintDefinition constraint) {
		if (constraint != null) {
			if (!constraintDefinitionExists(constraint)) {
				constraints.add(constraint);
				if (listener != null) {
					listener.addConstraintDefinition(constraint);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * createActivities
	 * 
	 * @param assignmentModel
	 *            AssignmentModel
	 */
	private void createConstraints(final AssignmentModel assignmentModel) {
		for (int i = 0; i < assignmentModel.constraintDefinitionsCount(); i++) {
			final ConstraintDefinition constraintDefinition = assignmentModel.constraintDefinitionAt(i);
			addConstraintDefiniton(constraintDefinition);
		}
	}

	/**
	 * deleteConstraintDefinitions
	 * 
	 * @param descripton
	 *            ActivityDefinition
	 * @return List
	 */
	private List<Object> deleteConstraintDefinitions(final ActivityDefinition descripton) {
		final List<Object> list = new ArrayList<Object>();
		final List<ConstraintDefinition> temp = new ArrayList<ConstraintDefinition>();
		for (int i = 0; i < constraintDefinitionsCount(); i++) {
			final ConstraintDefinition constraint = constraintDefinitionAt(i);
			if (constraint.dependsOn(descripton)) {
				temp.add(constraint);
			}
		}
		for (int i = 0; i < temp.size(); i++) {
			list.addAll(deleteConstraintDefinition(temp.get(i)));
		}
		return list;
	}

	/**
	 * addListener
	 * 
	 * @param aListener
	 *            AssignmentModelListener
	 */
	public void addListener(final AssignmentModelListener aListener) {
		listener = aListener;
	}

	/**
	 * editActivityDefinition
	 * 
	 * @param activityDefinition
	 *            activityDefinition
	 * @return boolean
	 */
	public boolean editActivityDefinition(final ActivityDefinition activityDefinition) {
		boolean ok = true;
		final int count = activityDefinitionsCount();
		int i = 0;
		ActivityDefinition curr = null;
		// *** search for all job descriptions that have the user-name like the copy object
		while ((i < count) && ok) {
			curr = activityDefinitionAt(i++);
			if (activityDefinition.getName().equals(curr.getName())) {
				// *** allow for the update only if the new job description-name is not alreday
				// assigned to another job description (a job description with different id)
				ok = activityDefinition.getId() == curr.getId();
			}
		}
		if (ok && (listener != null)) {
			listener.updateActivityDefinition(activityDefinition);
		}
		return ok;
	}

	/**
	 * editConstraintDefinition
	 * 
	 * @param constraint
	 *            ConstraintDefinition
	 * @return boolean
	 */
	public boolean editConstraintDefinition(final ConstraintDefinition constraint) {
		if (listener != null) {
			listener.updateConstraintDefinition(constraint);
		}
		return true;

	}

	/**
	 * @param name
	 *            String
	 * @return ActivityDefinition
	 */
	public ActivityDefinition activityDefinitionWithName(final String name) {
		boolean found = false;
		ActivityDefinition job = null;
		synchronized (activities) {
			final Iterator<ActivityDefinition> iterator = activities.listIterator();
			while (iterator.hasNext() && !found) {
				job = iterator.next();
				found = job.getName().equals(name);
			}
		}
		return found ? job : null;
	}

	/**
	 * @param id
	 *            int
	 * @return ActivityDefinition
	 */
	public ActivityDefinition activityDefinitionWithId(final int id) {
		boolean found = false;
		ActivityDefinition job = null;
		synchronized (activities) {
			final Iterator<ActivityDefinition> iterator = activities.listIterator();
			while (iterator.hasNext() && !found) {
				job = iterator.next();
				found = job.getId() == id;
			}
		}
		return found ? job : null;
	}

	/**
	 * @return TeamModel
	 */

	/**
	 * add
	 * 
	 * @param element
	 *            DataElement
	 * @return boolean
	 */

	public Language getLanguage() {
		return language;
	}

	public HashMap<String, String> getAttributes() {
		return attributes;
	}

	/**
	 * add
	 * 
	 * @param Id
	 *            DataElement
	 * @param name
	 *            String
	 * @param type
	 *            Type
	 * @param initial
	 *            String
	 * @return boolean
	 * 
	 * @return Iterator
	 */
	public Iterator<ActivityDefinition> getConstrainedActivities() {
		final List<ActivityDefinition> result = new ArrayList<ActivityDefinition>();
		final Iterator<ActivityDefinition> act = activities.iterator();
		while (act.hasNext()) {
			final ActivityDefinition activity = act.next();
			final Iterator<ConstraintDefinition> constr = constraints.iterator();
			boolean found = false;
			while (constr.hasNext() && !found) {
				final ConstraintDefinition constraint = constr.next();
				found = constraint.dependsOn(activity);
			}
			if (found) {
				result.add(activity);
			}
		}
		return result.iterator();
	}

	@Override
	public String toString() {
		return name;

	}
}
