/* Generated by Together */

package org.processmining.plugins.declare.visualizing;

import java.util.Iterator;
import java.util.List;

public class Base implements Cloneable {

	private int id = 0;

	protected void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	/**
	 * Base
	 * 
	 * @param aId
	 *            int
	 */
	public Base(int aId) {
		id = aId;
	}

	/**
	 * 
	 * @param aBase
	 *            Base
	 */
	public Base(Base aBase) {
		this(aBase.getId());
	}

	/**
	 * getIdString
	 * 
	 * @return String
	 */
	public String getIdString() {
		return Integer.toString(id);
	}

	/**
	 * equals
	 * 
	 * @param anObject
	 *            Object
	 * @return boolean
	 */
	public boolean equals(Object anObject) {
		boolean eql = false;
		if (anObject != null) {
			if (anObject instanceof Base) {
				eql = (((Base) anObject).getId() == getId());
			}
		}
		return eql;
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
	 * @return Base
	 */
	protected Base newInstance() {
		return new Base(id);
	}

	/**
   *
   */
	protected void cloneAttributes() {

	}

	/**
	 * clone
	 */
	public Object clone() {
		Base myClone = newInstance();
		cloneAttributes();
		return myClone;
	}

	public static int nextId(List<Base> list) {
		if (list == null) {
			return -1;
		}
		int id = 0;
		Iterator<Base> it = list.iterator();
		while (it.hasNext()) {
			Base base = it.next();
			if (id < base.getId()) {
				id = base.getId();
			}
		}
		return ++id;
	}

}
