package org.processmining.plugins.declare.visualizing;

import javax.swing.table.DefaultTableModel;

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
public class TTableModel extends DefaultTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5763578148531632285L;
	// one row represents one object
	// objectColumn is the column to which the related object assigned
	// used to get the selected object/row
	private int objectColumn = 0;

	//private final int iniRowCount = 0;

	public TTableModel(int objectColumn, Object[] columnNames) {
		super(columnNames, 0);
		this.objectColumn = objectColumn;
	}

	public Object getObject(int row) {
		if ((0 <= row) && (row < getRowCount())) {
			return getValueAt(row, objectColumn);
		} else {
			return null;
		}
	}

	public void clear() {
		//getDataVector().clear();
		setRowCount(0);
	}

	public int getIndexOf(Object object) {
		boolean found = false;
		int i = 0;
		while (!found && (i < getRowCount())) {
			Object current = getObject(i++);
			found = current == object;
		}
		return found ? i - 1 : -1;
	}

	public boolean isCellEditable(int row, int col) {
		return false;
	}

	public void remove(Object object) {
		if (object != null) {
			int row = getIndexOf(object);
			if (row >= 0) {
				removeRow(row);
			}
		}
	}
}
