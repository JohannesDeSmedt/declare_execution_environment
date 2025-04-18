package org.processmining.plugins.declareminer.visualizing;

/**
 * <p>
 * Title: DECLARE
 * </p>
 * 
 * <p>
 * Description: A template broker for xml data warehouse. Implements the
 * TemplateBroker interface.
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
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLTemplateBroker extends XMLBroker implements TemplateBroker {

	TemplateElementFactory factory;

	public XMLTemplateBroker(String aConnectionString, String aName) {
		super(aConnectionString, aName);
		factory = new TemplateElementFactory(this);
		readDocument();
	}

	/**
	 * Adds a template to a xml file.
	 * 
	 * @param template
	 *            template that should be added.
	 */
	public void addTemplate(LanguageGroup parent, ConstraintTemplate template) {
		/*
		 * Element languageElement =
		 * this.getLanguageElement(template.getLanguage()); Element newTemplate
		 * = this.factory.createTemplateElement(template);
		 * languageElement.appendChild(newTemplate); writeDocument();
		 */
		Element parentElement = getParentElement(parent, template.getLanguage());

		Element newTemplate = factory.createTemplateElement(template);
		parentElement.appendChild(newTemplate);
		writeDocument();
	}

	private Element getLanguageElement(Language lang) {
		Element languageElement = factory.getLanguageElement(getLanguagesElement(), lang);
		if (languageElement == null) {
			languageElement = createLanguage(lang);
		}
		return languageElement;
	}

	/**
	 * Adds a template to a xml file.
	 * 
	 * @param language
	 *            template that should be added.
	 * @return Element
	 */
	private Element createLanguage(Language language) {
		Element newLanguage = factory.createLanguageElement(language);
		Element languages = getLanguagesElement();
		languages.appendChild(newLanguage);
		writeDocument();
		return newLanguage;
	}

	/**
	 * Adds a template to a xml file.
	 * 
	 * @param language
	 *            template that should be added.
	 */
	public void addLanguage(Language language) {
		createLanguage(language);
	}

	/**
	 * Edits/alters a template in a xml file.
	 * 
	 * @param template
	 *            template that should be edited
	 * @return true if the template was succesfuly edited
	 */
	public boolean editTemplate(ConstraintTemplate template, LanguageGroup parent) {
		/*
		 * Element languageElement =
		 * this.getLanguageElement(template.getLanguage()); Element element =
		 * factory.getTemplateElement(template, languageElement); if (element !=
		 * null) { factory.templateToElement(template, element);
		 * writeDocument(); return true; } return false;
		 */
		Element parentElement = getParentElement(parent, template.getLanguage());
		Element element = factory.getTemplateElement(template, parentElement);
		if (element != null) {
			factory.templateToElement(template, element);
			writeDocument();
			return true;
		}
		return false;
	}

	/**
	 * Deletes a template from a xml file.
	 * 
	 * @param template
	 *            template that should be deleted
	 * @return true if the template was succesfuly deleted
	 */
	public boolean deleteTemplate(ConstraintTemplate template, LanguageGroup parent) {
		Element parentElement = getParentElement(parent, template.getLanguage());

		Element element = factory.getTemplateElement(template, parentElement);
		if (element != null) {
			deleteElement(element, parentElement);
			//factory.deleteElement(element, parentElement);
			writeDocument();
			return true;
		}
		return false;
	}

	private Element getParentElement(LanguageGroup parent, Language language) {
		Element languageElement = getLanguageElement(language);
		Element parentElement = null;
		if (parent == language) {
			parentElement = languageElement;
		} else {
			parentElement = factory.getGroupElement(languageElement, parent);
		}

		return parentElement;
	}

	public List<Language> readLanguages() {
		List<Language> list = new ArrayList<Language>();
		Element element;
		Language lang;

		NodeList languages = factory.getListLanguages(getLanguagesElement());
		for (int i = 0; i < languages.getLength(); i++) {
			element = (Element) languages.item(i);
			lang = factory.elementToLanguage(element);
			list.add(lang);
		}
		return list;
	}

	/**
	 * getTemplatesElement
	 * 
	 * @return Element
	 */
	public Element getLanguagesElement() {
		return getDocumentRoot();
	}

	public boolean deleteLanguage(Language language) {
		Element element = getLanguageElement(language);
		if (element != null) {
			deleteElement(element, getLanguagesElement());
			writeDocument();
			return true;
		}
		return false;
	}

	public void addGroup(LanguageGroup group, LanguageGroup parent, Language language) {
		Element parentElement = getParentElement(parent, language);

		Element newGroup = factory.createGroupElement(group);
		parentElement.appendChild(newGroup);
		writeDocument();
	}

	public boolean editGroup(LanguageGroup group, Language language) {
		Element languageElement = getLanguageElement(language);
		Element element = factory.getGroupElement(languageElement, group);
		if (element != null) {
			factory.groupToElement(group, element);
			writeDocument();
			return true;
		}
		return false;
	}

	public void deleteGroup(LanguageGroup group, LanguageGroup parent, Language language) {
		Element languageElement = getLanguageElement(language);
		Element parentElement = getParentElement(parent, language);
		Element groupElement = factory.getGroupElement(languageElement, group);
		if ((parentElement != null) && (groupElement != null)) {
			parentElement.removeChild(groupElement);
			writeDocument();
		}
	}

}

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
