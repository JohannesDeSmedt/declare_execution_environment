package org.processmining.plugins.declareminer.visualizing;

import java.util.ArrayList;

import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

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
public class TList extends JList {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2054230024326728712L;

	public TList(ListModel dataModel) {
		super(dataModel);
		prepare();
	}

	public TList(Object[] listData) {
		super(listData);
		prepare();
	}

	public TList(ArrayList<Object> listData) {
		super(listData.toArray());
		prepare();
	}

	public TList() {
		super();
		prepare();
	}

	private void prepare() {
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
}
