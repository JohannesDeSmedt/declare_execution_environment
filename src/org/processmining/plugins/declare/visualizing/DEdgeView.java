package org.processmining.plugins.declare.visualizing;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import org.jgraph.graph.CellHandle;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphContext;

import com.fluxicon.slickerbox.factory.SlickerDecorator;

/**
 * <p>
 * Title:
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
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class DEdgeView extends EdgeView {

	/** Drawing attributes that are created on the fly */

	/**
	 * 
	 */
	private static final long serialVersionUID = -5599481405889850931L;

	/**
	 * Constructs an edge view for the specified model object.
	 * 
	 * @param cell
	 *            reference to the model object
	 */
	public DEdgeView(Object cell) {
		super(cell);
		renderer = new DEdgeRenderer();

	}

	/**
	 * Returns a cell handle for the view.
	 * 
	 * @param context
	 *            GraphContext
	 * @return CellHandle
	 */
	public CellHandle getHandle(GraphContext context) {
		return new DEdgeHandle(this, context);
	}

	//
	// Custom Edge Handle
	//

	// Defines a EdgeHandle that uses the Shift-Button (Instead of the Right
	// Mouse Button, which is Default) to add/remove point to/from an edge.
	public static class DEdgeHandle extends EdgeView.EdgeHandle {

		/**
	 * 
	 */
		private static final long serialVersionUID = -395505643022569913L;
        private EdgeView edge;
		/**
		 * 
		 * @param edge
		 *            EdgeView
		 * @param ctx
		 *            GraphContext
		 */
		public DEdgeHandle(EdgeView edge, GraphContext ctx) {
			super(edge, ctx);
			this.edge = edge;
		}
		
		public void mousePressed(MouseEvent e){
			ConstraintDefinitionEdge cde = (ConstraintDefinitionEdge)edge.getCell();
	        JPanel jp = cde.getMetricsPanel();
	        if(jp!=null){
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

		// Override Superclass Method
		public boolean isAddPointEvent(MouseEvent event) {
			// Points are Added using Shift-Click
			return event.isShiftDown();
		}

		// Override Superclass Method
		public boolean isRemovePointEvent(MouseEvent event) {
			// Points are Removed using Shift-Click
			return event.isShiftDown();
		}
	}
}
