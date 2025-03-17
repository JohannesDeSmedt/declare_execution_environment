package org.processmining.plugins.declare.visualizing;

import java.util.Vector;

import javax.swing.AbstractListModel;

/**
 * @author Fabrizio M. Maggi
 * 
 */
public class CellModel extends AbstractListModel {

	/**
		 * 
		 */
	private static final long serialVersionUID = 4735900378220460350L;
	protected Vector list;

	public CellModel(Vector list) {
		this.list = list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.ListModel#getElementAt(int)
	 */
	public Object getElementAt(int index) {
		try {
			return list.get(index);
		} catch (IndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.ListModel#getSize()
	 */
	public int getSize() {
		return list.size();
	}

}
