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

import java.io.File;

public class XMLBrokerFactory /* extends BrokerFactory */{

	private static final String APPLICATION_PATH = "user.dir";

	private static final String XML_FOLDER = "xml";

	private static final String ORG_NAME = "organization";
	private static final String ORG_FILE = "organization.xml";

	private static final String TEMPL_NAME = "languages";
	private static final String TEMPL_FILE = "template.xml";

	private static final String ASSIGNMENT_NAME = "model";

	private static final String CONSGROUPS_NAME = "constraintgroups";
	private static final String CONSGROUPS_FILE = "constraintgroups.xml";

	/**
	 * 
	 * @return OrganizationBroker
	 */

	/**
	 * 
	 * @return TemplateBroker
	 */
	public static TemplateBroker newTemplateBroker() {
		return new XMLTemplateBroker(getTemplatePath(), TEMPL_NAME);
	}

	/**
	 * 
	 * @return TemplateBroker
	 */
	public static TemplateBroker newTemplateBroker(String path) {
		return new XMLTemplateBroker(path, TEMPL_NAME);
	}

	/**
	 * 
	 * @return AssignmentBroker
	 * @param path
	 *            String
	 */
	public static AssignmentViewBroker newAssignmentBroker(String path) {
		return new XMLAssignmentViewBroker(path, ASSIGNMENT_NAME);
	}

	/**
	 * 
	 * @return AssignmentBroker
	 */
	public static AssignmentViewBroker newAssignmentBroker() {
		return new XMLAssignmentViewBroker("", ASSIGNMENT_NAME);
	}

	/**
	 * 
	 * @return ConstraintGroupBroker
	 */

	/**
	 * 
	 * @return String
	 */
	private static String getApplicationPath() {
		return System.getProperty(APPLICATION_PATH);
	}

	/**
	 * 
	 * @return String
	 */
	private static String getOrganizationPath() {
		return getDataPath() + File.separator + ORG_FILE;
	}

	/**
	 * 
	 * @return String
	 */
	private static String getTemplatePath() {
		return getDataPath() + File.separator + TEMPL_FILE;
	}

	/**
	 * 
	 * @return String
	 */
	private static String getConstraintGroupPath() {
		return getDataPath() + File.separator + CONSGROUPS_FILE;
	}

	/**
	 * getDataPath
	 * 
	 * @return String
	 */
	private static String getDataPath() {
		return getApplicationPath() + File.separator + XML_FOLDER;
	}

}
