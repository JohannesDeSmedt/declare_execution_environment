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
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;

public abstract class DVertex extends DefaultGraphCell implements MouseListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4451359435095622492L;
	protected static final int WIDTH = 90;
	protected static final int HEIGHT = 50;

	private static final boolean CHILDREN_SELECTABLE = true;

	public DVertex(Base anObject, int x, int y, Color bg, boolean raised, boolean editable) {
		this(anObject, new Rectangle2D.Double(x, y, WIDTH, HEIGHT), bg, raised, editable);

	}

	protected DVertex(Base object, Rectangle2D bounds, Color bg, boolean raised, boolean editable) {
		super(object);

		// set default line width
		//GraphConstants.setLineWidth(getAttributes(), this.getLineWidth());

		GraphConstants.setChildrenSelectable(getAttributes(), CHILDREN_SELECTABLE);
		// set bounds
		GraphConstants.setBounds(getAttributes(), bounds);
		// Set fill color
		if (bg != null) {
			GraphConstants.setGradientColor(getAttributes(), bg);
			GraphConstants.setOpaque(getAttributes(), true);
		}

		// Set raised border
		if (raised) {
			GraphConstants.setBorder(getAttributes(), BorderFactory.createRaisedBevelBorder());
		} else {
			// Set black border
			//GraphConstants.setBorderColor(getAttributes(), Color.black);
			//GraphConstants.setBorder(getAttributes(), BorderFactory.createLineBorder(Color.o, getBorderWidth()));
			//GraphConstants.setLineWidth(getAttributes(), 2);
		}

		GraphConstants.setEditable(getAttributes(), editable);
		GraphConstants.setOpaque(getAttributes(), true);
	}

	protected int getBorderWidth() {
		return 1;
	}

	public DVertex(DVertex vertex) {
		super(null);

		GraphConstants.setChildrenSelectable(getAttributes(), CHILDREN_SELECTABLE);
		// set bounds
		Rectangle2D bounds = GraphConstants.getBounds(vertex.getAttributes());
		GraphConstants.setBounds(getAttributes(), bounds);
		// Set fill color
		Color bg = GraphConstants.getGradientColor(vertex.getAttributes());
		if (bg != null) {
			GraphConstants.setGradientColor(getAttributes(), bg);
			GraphConstants.setOpaque(getAttributes(), true);
		}

		// Set raised border
		Border border = GraphConstants.getBorder(vertex.getAttributes());

		if (border != null) {
			GraphConstants.setBorder(getAttributes(), border);
		} else {
			// Set black border
			GraphConstants.setBorderColor(getAttributes(), Color.black);
			//GraphConstants.setLineWidth(getAttributes(), 2);
		}

		boolean editable = GraphConstants.isEditable(vertex.getAttributes());
		GraphConstants.setEditable(getAttributes(), editable);
	}

	/**
	 * setEditable
	 * 
	 * @param editable
	 *            boolean
	 */
	public void setEditable(boolean editable) {
	}

	/**
	 * getPort
	 * 
	 * @param edge
	 *            DEdge
	 * @return Port
	 */
	//  public abstract Port getPort(DEdge edge);
	/**
	 * 
	 * @param edge
	 *            DEdge
	 * @return Port
	 */
	/*
	 * public Port getPort(DEdge edge) { return (Port) getChildAt(0); }
	 */

	/**
	 * addPort
	 */
	public Object addPort() {
		DPort dPort = new DPort(this);
		add(dPort);
		return dPort;
	}

	/**
	 * setPosition
	 * 
	 * @param point
	 *            Point2D
	 */
	public void setPosition(Point2D point) {
		Rectangle2D oldBounds = GraphConstants.getBounds(getAttributes());
		Rectangle2D newBounds = new Rectangle2D.Double(point.getX(), point.getY(), oldBounds.getWidth(),
				oldBounds.getHeight());
		GraphConstants.setBounds(getAttributes(), newBounds);
	}

	/**
	 * setPosition
	 * 
	 * @param point
	 *            Point2D
	 */
	public void setSize(Point2D point) {
		Rectangle2D oldBounds = GraphConstants.getBounds(getAttributes());
		Rectangle2D newBounds = new Rectangle2D.Double(oldBounds.getX(), oldBounds.getY(), point.getX(), point.getY());
		GraphConstants.setBounds(getAttributes(), newBounds);
	}

	/**
	 * setPosition
	 * 
	 * @param bounds
	 *            Point2D
	 */
	public void setBounds(Rectangle2D bounds) {
		GraphConstants.setBounds(getAttributes(), bounds);
	}

	/**
	 * resize
	 */
	public void resize() {
		GraphConstants.setResize(getAttributes(), true);
	}

	/**
	 * getBounds
	 * 
	 * @return Rectangle2D
	 */
	public Rectangle2D getBounds() {
		return GraphConstants.getBounds(getAttributes());
	}

	public double getWidth() {
		return GraphConstants.getBounds(getAttributes()).getWidth();
	}

	public double getHeight() {
		return GraphConstants.getBounds(getAttributes()).getHeight();
	}

	public Base getBase() {
		return (Base) getUserObject();
	}
	
	public void mouseClicked(MouseEvent e) {
        JPanel jp = ((ActivityDefinitonCell)e.getSource()).getMetricsPanel();
        //SlickerTabbedPane tabbed = (SlickerTabbedPane) jp.getComponent(1);
        jp.removeAll();
        Vector labels = ((ActivityDefinitonCell)e.getSource()).getLabels();
        for(int i =0; i< labels.size(); i ++){
        	jp.add((JLabel)labels.get(i));
        }
        //jp.add(tabbed, "0,1");
        jp.repaint();
        JDialog jd = new JDialog();
		jd.setVisible(true);
		
	}

}
