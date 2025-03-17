package org.processmining.plugins.declare.visualizing;

import java.awt.Graphics;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.PortRenderer;
import org.jgraph.graph.PortView;

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
public class TransparentPortView extends PortView {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8359300852198338515L;
	public static transient TransparentRenderer renderer = new TransparentRenderer();

	public TransparentPortView() {
		super();
	}

	public TransparentPortView(Object object) {
		super(object);
	}

	public CellViewRenderer getRenderer() {
		return renderer;
	}

	public static class TransparentRenderer extends PortRenderer {

		/**
	 * 
	 */
		private static final long serialVersionUID = -7309495960446672754L;

		public void paint(Graphics g) {
		}
	}

}
