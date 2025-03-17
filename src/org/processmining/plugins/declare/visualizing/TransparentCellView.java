package org.processmining.plugins.declare.visualizing;

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

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphContext;
import org.jgraph.graph.VertexRenderer;
import org.jgraph.graph.VertexView;

import com.fluxicon.slickerbox.factory.SlickerDecorator;

public class TransparentCellView extends VertexView {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6460594714818908835L;
	/**
   */
	public static transient JGraphTransparentRenderer renderer = new JGraphTransparentRenderer();

	/**
   */
	public TransparentCellView() {
		super();
	}

	/**
   */
	public TransparentCellView(Object cell) {
		super(cell);
	}

	/**
	 * Returns the intersection of the bounding rectangle and the straight line
	 * between the source and the specified point p. The specified point is
	 * expected not to intersect the bounds.
	 */
	public Point2D getPerimeterPoint(EdgeView edge, Point2D source, Point2D p) {
		return getCenterPoint(this);
	}

	/**
   */
	public CellViewRenderer getRenderer() {
		return renderer;
	}

	/**
	 * The Vertex Renderer inherits from JLabel. Do not paint anything.
	 */
	public static class JGraphTransparentRenderer extends VertexRenderer {
		/**
	 * 
	 */
		private static final long serialVersionUID = 961934859392648500L;

		/**
		 * Paints the Ellipse.
		 */
		public void paint(Graphics g) {
		}
	}
	
	
	
	public static class DVertexHandle extends VertexView.SizeHandle {

		/**
	 * 
	 */
		private static final long serialVersionUID = -395505643022569913L;
        private VertexView edge;
		/**
		 * 
		 * @param edge
		 *            EdgeView
		 * @param ctx
		 *            GraphContext
		 */
		public DVertexHandle(VertexView edge, GraphContext ctx) {
			super(edge, ctx);
			this.edge = edge;
		}
		
		public void mousePressed(MouseEvent e){
			ConstraintDefinitionEdge cde = (ConstraintDefinitionEdge)edge.getCell();
	        JPanel jp = cde.getMetricsPanel();
	        jp.removeAll();
	        Vector labels = cde.getLabels();
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
	        JPanel main = cde.getMainPanel();
	        main.revalidate();
	        main.repaint();
		}
	
	
	}

}
