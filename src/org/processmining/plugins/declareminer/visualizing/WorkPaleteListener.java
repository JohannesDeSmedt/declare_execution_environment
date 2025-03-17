package org.processmining.plugins.declareminer.visualizing;

import java.awt.geom.Point2D;

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
public interface WorkPaleteListener {
	/**
	 * addActivityDefinition
	 */
	public void insert(Point2D point);

	/**
	 * addConstraint
	 */
	public void addConstraint();
}
