package org.processmining.plugins.declare.visualizing;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JDialog;

import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;

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

public class TransparentCell extends DVertex {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2453468667806401105L;

	/**
	 * Creates a custom graph cell with a given user object
	 * 
	 * @param userObject
	 */
	public TransparentCell(Base userObject) {
		//super(userObject);
		super(userObject, new Rectangle2D.Double(0, 0, 100, 100), null, false, false);
		initialize(new Rectangle2D.Double(0, 0, 100, 100));
	}

	/**
	 * Initializes the graph cell
	 * 
	 */
	private void initialize(Rectangle2D bounds) {
		GraphConstants.setSizeable(getAttributes(), false);
		setBounds(bounds);
	}

	/**
	 * Initializes the graph cell
	 * 
	 */
	/*
	 * public void setBounds(Rectangle2D bounds) {
	 * GraphConstants.setBounds(this.getAttributes(), bounds); }
	 */

	public void setPosition(double x, double y) {
		Rectangle2D old = GraphConstants.getBounds(getAttributes());
		GraphConstants.setBounds(getAttributes(), new Rectangle2D.Double(x, y, old.getWidth(), old.getHeight()));
	}

	public Object addPort(Point2D offset, Object userObject) {
		DefaultPort port = new TransparentPort(userObject);
		if (offset == null) {
			add(port);
		} else {
			GraphConstants.setOffset(port.getAttributes(), offset);
			add(port);
		}
		return port;
	}

	public void mouseClicked(MouseEvent e) {
		JDialog jd = new JDialog();
		jd.setVisible(true);
		// TODO Auto-generated method stub
		
	}

	/*public void SelectionChanged(SelectionChangeEvent event) {
	JPanel jp = this.getMetricsPanel();
    jp.removeAll();
    Vector labels = this.getLabels();
	JList labelsList = new JList();
	ListRenderer renderer = new ListRenderer();
	labelsList.setCellRenderer(renderer);

	Vector cellList = new Vector();
	for (int i = 0; i < labels.size(); i++) {
		Cell cellL = new Cell();
		if (i == 0) {
			cellL.setCurrentSelected(true);
		}
		cellL.setLabel(((JLabel)labels.get(i)).getText());
		cellL.setSelected(false);
		cellList.add(cellL);
	}
	labelsList.setModel(new CellModel(cellList));
	labelsList.setSelectionBackground(new Color(0, 100, 0));
	labelsList.setSelectedIndex(0); //It's easier to see the focus change
	//if an item is selected.
	labelsList.setBackground(new Color(160, 160, 160));
	labelsList.setSelectionForeground(Color.white);
	labelsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	//final Vector tempTems = templs;
	
	
	
	
	JScrollPane scroll = new JScrollPane(labelsList);
	scroll.setOpaque(false);
	scroll.getViewport().setOpaque(false);
	scroll.setBorder(BorderFactory.createEmptyBorder());
	scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
	SlickerDecorator.instance().decorate(scroll.getVerticalScrollBar(), new Color(0, 0, 0, 0),
			new Color(140, 140, 140), new Color(80, 80, 80));
	scroll.getVerticalScrollBar().setOpaque(false);

	SlickerDecorator.instance().decorate(scroll.getHorizontalScrollBar(), new Color(0, 0, 0, 0),
			new Color(140, 140, 140), new Color(80, 80, 80));
	scroll.getHorizontalScrollBar().setOpaque(false);
	jp.setLayout(new TableLayout(new double[][] { { TableLayoutConstants.FILL}, {TableLayoutConstants.FILL } }));
	jp.add(scroll, "0,0");
	

    jp.repaint();
    JPanel main = this.getMainPanel();
    main.revalidate();
    main.repaint();
}*/
	
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		JDialog jd = new JDialog();
		jd.setVisible(true);
		
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
