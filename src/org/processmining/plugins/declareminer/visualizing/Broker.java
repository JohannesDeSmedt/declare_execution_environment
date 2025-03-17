package org.processmining.plugins.declareminer.visualizing;

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
public abstract class Broker {

	private final String connectionString;

	public Broker(String aConnectionString) {
		super();
		connectionString = aConnectionString;
		connect();
	}

	protected String getConnectionString() {
		return connectionString;
	}

	/**
	 * connect
	 */
	protected abstract void connect();
}
