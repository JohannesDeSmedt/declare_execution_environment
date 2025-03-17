package org.processmining.plugins.declare.visualizing;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
public class AssignmentElementFactory extends XMLElementFactory {

	private static final String TAG_ASSIGNMENT = "assignment";
	private static final String TAG_ASSIGNMENT_NAME = "name";
	private static final String TAG_ASSIGNMENT_LANGUAGE = "language";

	private static final String TAG_ACTIVITY_DEFINITIONS = "activitydefinitions";
	private static final String TAG_ACTIVITY_DEFINITION = "activity";
	private static final String TAG_ACTIVITY_DEFINITION_NAME = "name";
	private static final String TAG_ACTIVITY_DEFINITION_AUTHORIZATIONS = "authorization";

	private static final String TAG_ACTIVITY_DEFINITION_DATA_MODEL = "datamodel";
	private static final String TAG_ACTIVITY_DEFINITION_DATA_DEFINITION = "data";
	private static final String TAG_ACTIVITY_DEFINITION_DATA_ELEMENT = "element";
	private static final String TAG_ACTIVITY_DEFINITION_DATA_TYPE = "type";
	private static final String TAG_CONSTRAINT_DEFINITIONS = "constraintdefinitions";
	private static final String TAG_CONSTRAINT_DEFINITION = "constraint";
	private static final String TAG_CONSTRAINT_DEFINITION_NAME = "name";
	private static final String TAG_CONSTRAINT_DEFINITION_MANDATORY = "mandatory";
	private static final String TAG_CONSTRAINT_DEFINITION_CONDITION = "condition";
	private static final String TAG_CONSTRAINT_DEFINITION_TEMPLATE = "template";
	private static final String TAG_CONSTRAINT_DEFINITION_PARAMETERS = "constraintparameters";
	private static final String TAG_CONSTRAINT_DEFINITION_PARAMETER = "parameter";
	private static final String TAG_CONSTRAINT_DEFINITION_PARAMETER_LTL = "templateparameter";
	private static final String TAG_CONSTRAINT_DEFINITION_PARAMETER_BRANCHES = "branches";
	private static final String TAG_CONSTRAINT_DEFINITION_PARAMETER_BRANCH = "branch";
	private static final String TAG_CONSTRAINT_DEFINITION_PARAMETER_BRANCH_NAME = "name";

	private static final String TAG_CONSTRAINT_DEFINITION_LEVEL = "level";
	private static final String TAG_CONSTRAINT_DEFINITION_LEVEL_GROUP = "group";
	private static final String TAG_CONSTRAINT_DEFINITION_LEVEL_GROUP_NAME = "name";
	private static final String TAG_CONSTRAINT_DEFINITION_LEVEL_GROUP_DESCRIPTION = "description";
	private static final String TAG_CONSTRAINT_DEFINITION_LEVEL_PRIORITY = "priority";
	private static final String TAG_CONSTRAINT_DEFINITION_LEVEL_MESSAGE = "message";

	private static final String TAG_DATA = "data";
	private static final String TAG_DATA_ELEMENT = "dataelement";
	private static final String TAG_DATA_ELEMENT_NAME = "name";
	private static final String TAG_DATA_ELEMENT_TYPE = "type";
	private static final String TAG_DATA_ELEMENT_INITIAL = "initial";

	private static final String TAG_TEAM = "team";
	private static final String TAG_TEAM_ROLE = "teamrole";
	private static final String TAG_TEAM_ROLE_NAME = "name";
	private static final String TAG_TEAM_ROLE_ROLE = "role";

	/**
	 * ElementFactory
	 * 
	 * @param aBroker
	 *            XMLBroker
	 */
	public AssignmentElementFactory(XMLBroker aBroker) {
		super(aBroker);
	}

	/**
	 * elementToAssignmentGraphical
	 * 
	 * @param view
	 *            model
	 * @param model
	 *            AssignmentModel
	 * @param element
	 *            Element
	 */
	protected Element getAssignmentElement(Element element) {
		return getFirstElement(element, TAG_ASSIGNMENT);
	}

	/**
	 * createAssignmentElement
	 * 
	 * @param model
	 *            model
	 * @return Element
	 */
	public Element createAssignmentElement(AssignmentModel model) {

		// *** create a new element for the template
		Element element = getDocument().createElement(TAG_ASSIGNMENT);

		// *** fill the element with template attributes
		assignmentToElement(model, element);

		// *** return the template element
		return element;
	}

	/**
	 * assignmentToElement
	 * 
	 * @param model
	 *            model
	 * @param element
	 *            Element
	 */
	public void assignmentToElement(AssignmentModel model, Element element) {
		// *** update name and text attributes
		setAttribute(element, TAG_ASSIGNMENT_NAME, model.getName());
		setAttribute(element, TAG_ASSIGNMENT_LANGUAGE, model.getLanguage().getName());

		attributesToElement(model.getAttributes(), element);

		// handle job descriptions
		activitiesToElement(model, element);
		// handle constraint defitions
		constraintsToElement(model, element);

	}

	/**
	 * 
	 * @param model
	 *            AssignmentModel
	 * @param element
	 *            Element
	 */
	private void activitiesToElement(AssignmentModel model, Element element) {
		// get the element containing the list of activities
		Element activities = getFirstElement(element, TAG_ACTIVITY_DEFINITIONS);
		// clear all activities because we will write them again
		removeChildren(activities);
		// loop through the all jobs in the model
		for (int i = 0; i < model.activityDefinitionsCount(); i++) {
			// create the element for every activity
			Element job = activityDefinitionToElement(model.activityDefinitionAt(i));
			// add the element to the list of activities
			activities.appendChild(job);
		}
	}

	/**
	 * 
	 * @param model
	 *            AssignmentModel
	 * @param element
	 *            Element
	 */
	private void constraintsToElement(AssignmentModel model, Element element) {
		// get the element containing the list of constraint definitons
		Element constraints = getFirstElement(element, TAG_CONSTRAINT_DEFINITIONS);
		// clear all constraint definitonsbecause we will write them again
		removeChildren(constraints);
		// loop through the all constraint definitons in the model
		for (int i = 0; i < model.constraintDefinitionsCount(); i++) {
			// create the element for every constraint definiton
			Element constraint = constraintDefintionToElement(model.constraintDefinitionAt(i));
			// add the element to the list of constraint definitons
			constraints.appendChild(constraint);
		}
	}

	/**
	 * 
	 * @param activity
	 *            ActivityDefinition
	 * @return Element
	 */
	private Element activityDefinitionToElement(ActivityDefinition activity) {
		Element element = baseToElement(activity, TAG_ACTIVITY_DEFINITION);
		setAttribute(element, TAG_ACTIVITY_DEFINITION_NAME, activity.getName());

		return element;
	}

	/**
	 * 
	 * @param constraint
	 *            ConstraintDefinition
	 * @return Element
	 */
	private Element constraintDefintionToElement(ConstraintDefinition constraint) {
		Element element = baseToElement(constraint, TAG_CONSTRAINT_DEFINITION);
		Boolean mandatory = constraint.getMandatory();
		setAttribute(element, TAG_CONSTRAINT_DEFINITION_MANDATORY, mandatory.toString());
		Element levelElement = constraintLevetToElement(constraint.getLevel());
		if (levelElement != null) {
			element.appendChild(levelElement);
		}

		updateObjectAttribute(element, TAG_CONSTRAINT_DEFINITION_CONDITION, constraint.getCondition().getText());
		updateObjectAttribute(element, TAG_CONSTRAINT_DEFINITION_NAME, constraint.getName());

		TemplateElementFactory templateFactory = new TemplateElementFactory(this);

		Element template = getFirstElement(element, TAG_CONSTRAINT_DEFINITION_TEMPLATE);
		templateFactory.templateToElement(constraint, template);

		parametersToElement(constraint, element);

		// *** return the constraint defintion element
		return element;
	}

	private Element constraintLevetToElement(ConstraintLevel level) {
		Element element = null;
		if (level != null) {
			element = createElement(TAG_CONSTRAINT_DEFINITION_LEVEL);

			Element group = constraintGroupToElement(level.getGroup());
			element.appendChild(group);

			updateObjectAttribute(element, TAG_CONSTRAINT_DEFINITION_LEVEL_PRIORITY, Integer.toString(level.getLevel()));
			updateObjectAttribute(element, TAG_CONSTRAINT_DEFINITION_LEVEL_MESSAGE, level.getMessage());
		}
		return element;
	}

	private Element constraintGroupToElement(ConstraintGroup group) {
		Element element = baseToElement(group, TAG_CONSTRAINT_DEFINITION_LEVEL_GROUP);
		updateObjectAttribute(element, TAG_CONSTRAINT_DEFINITION_LEVEL_GROUP_NAME, group.getName());
		updateObjectAttribute(element, TAG_CONSTRAINT_DEFINITION_LEVEL_GROUP_DESCRIPTION, group.getDescription());
		return element;
	}

	/**
	 * parametersToElement
	 * 
	 * @param constraint
	 *            ConstraintDefinition
	 * @param element
	 *            Element
	 */
	private void parametersToElement(ConstraintDefinition constraint, Element element) {
		// get the element containing the list of constraint definitons
		Element parameters = getFirstElement(element, TAG_CONSTRAINT_DEFINITION_PARAMETERS);
		// clear all constraint definitons because we will write them again
		removeChildren(parameters);
		// loop through the all constraint definitons in the model
		for (Parameter p : constraint.getParameters()) {
			// create the element for every constraint definiton
			Element parameter = parameterToElement(constraint, p);
			// add the element to the list of constraint definitons
			parameters.appendChild(parameter);
		}
	}

	private Element parameterToElement(ConstraintDefinition constraintDefinition, Parameter parameter) {
		Element element = getDocument().createElement(TAG_CONSTRAINT_DEFINITION_PARAMETER);

		setAttribute(element, TAG_CONSTRAINT_DEFINITION_PARAMETER_LTL, parameter.getIdString());

		branchesToElement(constraintDefinition, parameter, element);

		return element;
	}

	/**
	 * 
	 * @param parameter
	 *            Parameter
	 * @param element
	 *            Element
	 */
	private void branchesToElement(ConstraintDefinition constraintDefinition, Parameter parameter, Element element) {
		// get the element containing the list of parameter branches
		Element branches = getFirstElement(element, TAG_CONSTRAINT_DEFINITION_PARAMETER_BRANCHES);
		removeChildren(branches); // clear all parameter branches because we will write them again  

		for (ActivityDefinition real : constraintDefinition.getBranches(parameter)) { // loop through the all parameter branches in the model
			// create the element for every branch
			Element branch = branchToElement(real);
			// add the element to the list of parameter branches
			branches.appendChild(branch);

		}
	}

	/**
	 * 
	 * @param branch
	 *            ParameterBranch
	 * @return Element
	 */
	private Element branchToElement(ActivityDefinition branch) {
		Element element = createElement(TAG_CONSTRAINT_DEFINITION_PARAMETER_BRANCH);
		setAttribute(element, TAG_CONSTRAINT_DEFINITION_PARAMETER_BRANCH_NAME, branch.getName());

		// *** return the template element
		return element;
	}

	/**
	 * elementToAssignmentModel
	 * 
	 * @param element
	 *            Element
	 * @return AssignmentModel
	 */
	public AssignmentModel elementToAssignmentModel(Element element) {
		AssignmentModel model = null;
		Element modelElement = getFirstElement(element, TAG_ASSIGNMENT);
		if (modelElement != null) {
			Language lang = Control.singleton().getConstraintTemplate()
					.getLanguage(modelElement.getAttribute(TAG_ASSIGNMENT_LANGUAGE));
			if (lang != null) {
				model = new AssignmentModel(lang);

				// get attributes for nam eand external
				String name = modelElement.getAttribute(TAG_ASSIGNMENT_NAME);

				// set attributes for name and external
				model.setName(name);

				elementToAttributes(modelElement, model.getAttributes());

				// get team roles
				Element rolesTag = getFirstElement(modelElement, TAG_TEAM);
				NodeList roleElements = rolesTag.getElementsByTagName(TAG_TEAM_ROLE);
				for (int i = 0; i < roleElements.getLength(); i++) {
					roleElements.item(i);

				}

				// get data elements
				Element dataTag = getFirstElement(modelElement, TAG_DATA);
				NodeList dataElements = dataTag.getElementsByTagName(TAG_DATA_ELEMENT);
				for (int i = 0; i < dataElements.getLength(); i++) {
					dataElements.item(i);

				}

				// get activities
				Element jobsTag = getFirstElement(modelElement, TAG_ACTIVITY_DEFINITIONS);
				NodeList jobs = jobsTag.getElementsByTagName(TAG_ACTIVITY_DEFINITION);
				for (int i = 0; i < jobs.getLength(); i++) {
					Element job = (Element) jobs.item(i);
					elementToActivityDefinition(model, job);
				}

				// get constraint definitions
				Element constraintsTag = getFirstElement(modelElement, TAG_CONSTRAINT_DEFINITIONS);
				NodeList constraints = constraintsTag.getElementsByTagName(TAG_CONSTRAINT_DEFINITION);
				for (int i = 0; i < constraints.getLength(); i++) {
					Element constraint = (Element) constraints.item(i);
					elementToConstraintDeintion(model, constraint);
				}
			}
		}
		return model;
	}

	/**
	 * 
	 * @param element
	 *            Element
	 * @return TeamRole
	 */

	/**
	 * 
	 * @param model
	 *            AssignmentModel
	 * @param element
	 *            Element
	 * @return ConstraintDefinition
	 */
	private ConstraintDefinition elementToConstraintDeintion(AssignmentModel model, Element element) {
		// get base for the ID
		Base base = elementToBase(element);

		String conditionText = getSimpleElementText(element, TAG_CONSTRAINT_DEFINITION_CONDITION);
		String mandatoryText = element.getAttribute(TAG_CONSTRAINT_DEFINITION_MANDATORY);
		Boolean mandatory = new Boolean(mandatoryText);

		String name = getSimpleElementText(element, TAG_CONSTRAINT_DEFINITION_NAME);

		ConstraintLevel level = elementToConstraintLevel(element);

		TemplateElementFactory templateFactory = new TemplateElementFactory(this);
		// get template
		Element templateElement = getFirstElement(element, TAG_CONSTRAINT_DEFINITION_TEMPLATE);
		// *** ConstraintTemplate template = templateFactory.elementToTemplate(element);
		ConstraintTemplate template = templateFactory.elementToTemplate(model.getLanguage(), templateElement);

		ConstraintDefinition constraint = new ConstraintDefinition(base.getId(), model, template);
		model.addConstraintDefiniton(constraint);

		// get constraint parameters
		// first get the parameters tag
		Element parametersTag = getFirstElement(element, TAG_CONSTRAINT_DEFINITION_PARAMETERS);
		// get all parameters
		NodeList parameters = parametersTag.getElementsByTagName(TAG_CONSTRAINT_DEFINITION_PARAMETER);

		// a temp list for parameters
		List<Parameter> params = new ArrayList<Parameter>();
		for (int i = 0; i < parameters.getLength(); i++) {
			Element parameterElement = (Element) parameters.item(i);
			Parameter parameter = elementToParameter(model, constraint, parameterElement);
			params.add(parameter);
		}

		for (int i = 0; i < params.size(); i++) {
			Parameter parameter = params.get(i);
			for (ActivityDefinition real : constraint.getBranches(parameter)) {
				constraint.addBranch(parameter, real);
			}
		}

		if (constraint != null) {
			constraint.getCondition().setText(conditionText);
			constraint.setName(name);
			constraint.setMandatory(mandatory);
			if (!mandatory) {
				constraint.setLevel(level);
			}
		}

		return constraint;
	}

	/**
	 * 
	 * @param element
	 *            Element
	 * @return ConstraintLevel
	 */
	private ConstraintLevel elementToConstraintLevel(Element element) {
		ConstraintLevel level = null;
		if (element != null) {
			Element levelElement = getFirstElement(element, TAG_CONSTRAINT_DEFINITION_LEVEL);
			if (levelElement != null) {
				ConstraintGroup group = elementToConstraintGroup(levelElement);
				if (group != null) {
					level = new ConstraintLevel(group);
					String priority = getSimpleElementText(levelElement, TAG_CONSTRAINT_DEFINITION_LEVEL_PRIORITY);
					String message = getSimpleElementText(levelElement, TAG_CONSTRAINT_DEFINITION_LEVEL_MESSAGE);
					int pr = ConstraintWarningLevel.possible()[0];
					try {
						pr = Integer.parseInt(priority);
					} catch (Exception e) {
					}
					level.setLevel(pr);
					level.setMessage(message);
				}
			}
		}
		return level;
	}

	/**
	 * 
	 * @param element
	 *            Element
	 * @return ConstraintGroup
	 */
	private ConstraintGroup elementToConstraintGroup(Element element) {
		ConstraintGroup group = null;
		if (element != null) {
			Element groupElement = getFirstElement(element, TAG_CONSTRAINT_DEFINITION_LEVEL_GROUP);
			if (groupElement != null) {
				Base base = elementToBase(groupElement);
				group = new ConstraintGroup(base.getId());

				String name = getSimpleElementText(groupElement, TAG_CONSTRAINT_DEFINITION_LEVEL_GROUP_NAME);
				String description = getSimpleElementText(groupElement,
						TAG_CONSTRAINT_DEFINITION_LEVEL_GROUP_DESCRIPTION);
				group.setName(name);
				group.setDescription(description);
			}
		}
		return group;
	}

	/**
	 * 
	 * @param model
	 *            AssignmentModel
	 * @param template
	 *            ConstraintTemplate
	 * @param element
	 *            Element
	 * @return Parameter
	 */
	private Parameter elementToParameter(AssignmentModel model, ConstraintDefinition constraintDefinition,
			Element element) {
		String templateParam = element.getAttribute(TAG_CONSTRAINT_DEFINITION_PARAMETER_LTL);

		Parameter parameter = constraintDefinition.getParameterWithId(Integer.decode(templateParam));

		//Parameter parameter = new Parameter(null, FormalParameter);

		Element branchesTag = getFirstElement(element, TAG_CONSTRAINT_DEFINITION_PARAMETER_BRANCHES);
		NodeList branches = branchesTag.getElementsByTagName(TAG_CONSTRAINT_DEFINITION_PARAMETER_BRANCH);

		for (int i = 0; i < branches.getLength(); i++) {
			Element branch = (Element) branches.item(i);

			String name = branch.getAttribute(TAG_CONSTRAINT_DEFINITION_PARAMETER_BRANCH_NAME);

			ActivityDefinition activityDefinition = model.activityDefinitionWithName(name);

			constraintDefinition.addBranch(parameter, activityDefinition);
		}
		return parameter;
	}

	/**
	 * 
	 * @param model
	 *            ActivityDefinition
	 * @param element
	 *            Element
	 */
	private void elementToActivityDefinition(AssignmentModel model, Element element) {
		Base base = elementToBase(element);
		ActivityDefinition activityDefinition = model.addActivityDefinition(base.getId());
		String name = element.getAttribute(TAG_ACTIVITY_DEFINITION_NAME);
		activityDefinition.setName(name);
		// YAWL sub-process

		getFirstElement(element, TAG_ACTIVITY_DEFINITION_AUTHORIZATIONS);

		getFirstElement(element, TAG_ACTIVITY_DEFINITION_DATA_MODEL);

	}

	/**
	 * 
	 * @param authorization
	 *            Authorization
	 * @param element
	 *            Element
	 * @param team
	 *            TeamModel
	 */

	/**
	 * 
	 * @param activity
	 *            Authorization
	 * @param element
	 *            Element
	 * @param data
	 *            TeamModel
	 */

	/**
	 * dataToElement
	 * 
	 * @param model
	 *            DataModel
	 * @param element
	 *            Element
	 * @return Element
	 */

	/**
	 * 
	 * @param dataElement
	 *            DataElement
	 * @param element
	 *            Element
	 * @return Element
	 */

	/**
	 * rolesToElement
	 * 
	 * @param teamModel
	 *            DataModel
	 * @param element
	 *            Element
	 * @return Element
	 */

}
