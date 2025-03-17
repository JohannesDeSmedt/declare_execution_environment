package org.processmining.plugins.declare.visualizing;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

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
public class XMLBroker extends Broker {

	private Document document;
	private File file;
	private final String name;
	private String xml = null;

	public XMLBroker(final String aConnectionString, final String aName) {
		super(aConnectionString);
		if ((getConnectionString() != null) && !"".equals(getConnectionString())) {
			file = new File(getConnectionString());
		}
		name = aName;
		connect();
	}

	protected Document getDocument() {
		return document;
	}

	/**
	 * @return boolean
	 */
	private boolean createDocument() {
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (final ParserConfigurationException ex) {
			return false;
		}
		final DOMImplementation domImpl = builder.getDOMImplementation();
		document = domImpl.createDocument(null, name, null);
		return true;
	}

	/**
	 * @param name
	 *            String
	 * @return Element
	 */
	public Element createElement(final String name) {
		return document.createElement(name);
	}

	/**
	 * @param text
	 *            String
	 * @return Text
	 */
	public Text createTextNode(final String text) {
		return document.createTextNode(text);
	}

	/**
	 * connect
	 */
	@Override
	protected void connect() {
		// this.document = this.createDocument(getConnectionString());
		createDocument();
	}

	/**
	 * createDocument
	 * 
	 * @param aDocumentName
	 *            String
	 * @return Document
	 */
	/*
	 * public Document createDocument(String aDocumentName) { return null; }
	 */

	/**
	 * write
	 * 
	 * @return boolean returns TRUE if succesfull; returns FALSE when exception
	 */
	public boolean writeDocument() {

		final TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = null;
		try {
			transformer = tf.newTransformer();
		} catch (final TransformerConfigurationException ex) {
			return false;
		}
		final Source source = new DOMSource(document);
		ByteArrayOutputStream stream = null;
		final Result output;
		if (file == null) {
			output = new StreamResult(stream = new ByteArrayOutputStream());
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		} else {
			output = new StreamResult(file);
		}
		try {
			transformer.transform(source, output);
		} catch (final TransformerException ex2) {
			return false;
		}
		if (stream != null) {
			xml = stream.toString();
		}
		return true;
	}

	/**
	 * read
	 * 
	 * @return boolean
	 */
	public boolean readDocument() {
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = null;
		try {
			parser = factory.newDocumentBuilder();
		} catch (final ParserConfigurationException ex) {

			return false;
		}
		try {
			document = parser.parse(new InputSource(getConnectionString()));
		} catch (final IOException ex1) {
			return false;
		} catch (final SAXException ex1) {
			return false;
		}
		return true;
	}

	/**
	 * read
	 * 
	 * @return boolean
	 * @param path
	 *            String
	 */
	public boolean readDocument(final String path) {
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = null;
		try {
			parser = factory.newDocumentBuilder();
		} catch (final ParserConfigurationException ex) {

			return false;
		}
		try {
			document = parser.parse(new InputSource(path));
		} catch (final IOException ex1) {
			return false;
		} catch (final SAXException ex1) {
			return false;
		}
		return true;
	}

	/**
	 * read
	 * 
	 * @return boolean
	 * @param documentString
	 *            String
	 */
	public boolean readDocumentString(final String documentString) {
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = null;
		try {
			parser = factory.newDocumentBuilder();
		} catch (final ParserConfigurationException ex) {

			return false;
		}
		try {
			final byte[] myStrBytes = documentString.getBytes();
			final ByteArrayInputStream stringIS = new ByteArrayInputStream(myStrBytes);
			document = parser.parse(stringIS);
		} catch (final IOException ex1) {
			return false;
		} catch (final SAXException ex1) {
			return false;
		}
		return true;
	}

	/**
	 * @param element
	 *            Element
	 * @param name
	 *            String
	 * @return Element
	 */
	/*
	 * protected Element getFirstElement(Element element, String name) {
	 * NodeList nl = element.getChildNodes(); boolean found = false; Node node =
	 * null; int i = 0; while (i < nl.getLength() && !found) { node =
	 * nl.item(i++); found = node.getNodeName().equals(name); } if (!found) {
	 * node = this.createElement(name); element.appendChild(node); } return
	 * (Element) node; }
	 */

	/**
	 * @param element
	 *            Element
	 * @param name
	 *            String
	 * @return Element
	 */
	/*
	 * protected List<Element> getAllSubElements(Element element, String name) {
	 * NodeList nl = element.getChildNodes(); List<Element> list = new
	 * ArrayList(); // first check if the element has the name if
	 * (element.getNodeName().equals(name)) { list.add(element); } Node node =
	 * null; // then continue search in all children for (int i = 0; i <
	 * nl.getLength(); i++) { node = nl.item(i); if (node instanceof Element) {
	 * list.addAll(getAllSubElements( (Element) node, name)); } } return list; }
	 */

	/**
	 * @param element
	 *            Element
	 * @param name
	 *            String
	 * @return String
	 */
	/*
	 * String getSimpleElementText(Element element, String name) { Element
	 * nameEl = getFirstElement(element, name); Node textNode =
	 * nameEl.getFirstChild(); if (textNode instanceof Text) { return
	 * textNode.getNodeValue(); } else { //throw new
	 * RuntimeException("No text in " + name); return ""; } }
	 */

	/**
	 * @param element
	 *            Element
	 * @return String
	 */
	/*
	 * String getSimpleElementText(Element element) { Node textNode =
	 * element.getFirstChild(); if (textNode instanceof Text) { return
	 * textNode.getNodeValue(); } else { //throw new
	 * RuntimeException("No text in " + name); return ""; } }
	 */

	/**
	 * getDocumentRoot
	 * 
	 * @return Element
	 */
	protected Element getDocumentRoot() {
		return getDocument().getDocumentElement();
	}

	protected void clearDocument() {
		getDocument().removeChild(getDocumentRoot());
	}

	/**
	 * deleteElement
	 * 
	 * @param elementObject
	 *            Element
	 * @param elementList
	 *            NodeList
	 */
	protected void deleteElement(final Element elementObject, final Element elementList) {
		elementList.removeChild(elementObject);
	}

	public String getXML() {
		return xml;
	}
}
