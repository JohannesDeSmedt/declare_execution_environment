package org.processmining.plugins.declareminer.visualizing;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jgraph.graph.CellHandle;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphContext;
import org.processmining.framework.util.ui.widgets.ProMSplitPane;
import org.processmining.framework.util.ui.widgets.ProMTable;
import org.processmining.plugins.declareminer.gui.ListRendererResult;

import com.fluxicon.slickerbox.factory.SlickerDecorator;
import com.fluxicon.slickerbox.factory.SlickerFactory;

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
			Object splitPaneObj= cde.getMainPanel();
			if(splitPaneObj instanceof ProMSplitPane){
				ProMSplitPane splitPane= cde.getMetricsPanel();
				JSplitPane split = (JSplitPane) splitPane.getComponent(0);
				//SlickerTabbedPane tabbed = (SlickerTabbedPane) jp.getComponent(3);
				JPanel jpan = (JPanel)split.getTopComponent();
				JScrollPane jpScroll = (JScrollPane) jpan.getComponent(0);
				JPanel jp = (JPanel) jpScroll.getViewport().getView();
				JPanel radioPanel = (JPanel) jp.getComponent(1);
				jp.removeAll();
				Vector labels = cde.getLabels();
				JList labelsList = new JList();
				ListRendererResult renderer = new ListRendererResult();
				labelsList.setCellRenderer(renderer);
				Vector cellList = new Vector();
				for (int i = 0; i < labels.size(); i++) {
					if(!((JLabel)(labels.get(i))).getText().equals("Support Antecedent")&&!((JLabel)labels.get(i)).getText().equals("Support Consequent")){
						Cell cellL = new Cell();
						if (i == 0) {
							cellL.setCurrentSelected(true);
						}
						cellL.setLabel(((JLabel)labels.get(i)).getText());
						cellL.setSelected(false);
						cellList.add(cellL);
					}
				}
				labelsList.setModel(new CellModel(cellList));
				labelsList.setSelectionBackground(new Color(0, 100, 0));
				labelsList.setSelectedIndex(0); //It's easier to see the focus change
				//if an item is selected.
				labelsList.setBackground(new Color(160, 160, 160));
				labelsList.setSelectionForeground(Color.white);
				labelsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				//final Vector tempTems = templs;
				//	SlickerFactory sf = SlickerFactory.instance();
				//	SlickerTabbedPane tabbed = sf.createTabbedPane("filtering");
				//	tabbed.addTab("templates", new JPanel());


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
				jp.setLayout(new TableLayout(new double[][] { { TableLayoutConstants.FILL}, {200, 150 , 75, 75, TableLayout.FILL}}));
				jp.add(scroll, "0,0");
				jp.add(radioPanel,"0,1");
				Vector colNams = new Vector();
				colNams.add("Activations");
				colNams.add("Fulfilments");
				colNams.add("Violations");
				colNams.add("Conflicts");
				Vector data = new Vector();
				if(cde.getMetrics().contains("-")){
					Vector row = new Vector();
					row.add("-");
					row.add("-");
					row.add("-");
					row.add("-");
					data.add(row);
				}else{
					data.add(cde.getMetrics());
				}
				TableModel dtm = new DefaultTableModel(data,colNams);
				ProMTable pt = new ProMTable(dtm);
				jp.add(pt, "0,2");
				colNams = new Vector();
				colNams.add("Max delay");
				colNams.add("Min delay");
				colNams.add("Avg delay");
				colNams.add("StdDev delay");
				data = new Vector();
				Vector row = new Vector();
				boolean histogram = false;
				if(cde.getTimeDistances().get(0) instanceof String){
					row.add("-");
					row.add("-");
					row.add("-");
					row.add("-");
				}else{
					histogram = true;
					if(((Long)cde.getTimeDistances().get(0)) !=-1){
						long timeDiff = (Long)cde.getTimeDistances().get(0);
						long noDays = timeDiff/(1000*60*60*24);
						long noHours =  (timeDiff-(noDays*1000*60*60*24))/(1000*60*60);
						long noMinutes = ((timeDiff-(noDays*1000*60*60*24)-(noHours*1000*60*60)))/(1000*60);
						long noSecs = (timeDiff-(noDays*1000*60*60*24)-(noHours*1000*60*60)-(noMinutes*1000*60))/1000;
						row.add(noDays+"d "+noHours+"h "+noMinutes+"m "+noSecs+"s");
						timeDiff = (Long)cde.getTimeDistances().get(1);
						noDays = timeDiff/(1000*60*60*24);
						noHours =  (timeDiff-(noDays*1000*60*60*24))/(1000*60*60);
						noMinutes = ((timeDiff-(noDays*1000*60*60*24)-(noHours*1000*60*60)))/(1000*60);
						noSecs = (timeDiff-(noDays*1000*60*60*24)-(noHours*1000*60*60)-(noMinutes*1000*60))/1000;
						row.add(noDays+"d "+noHours+"h "+noMinutes+"m "+noSecs+"s");
						timeDiff = (Long)cde.getTimeDistances().get(2);
						noDays = timeDiff/(1000*60*60*24);
						noHours =  (timeDiff-(noDays*1000*60*60*24))/(1000*60*60);
						noMinutes = ((timeDiff-(noDays*1000*60*60*24)-(noHours*1000*60*60)))/(1000*60);
						noSecs = (timeDiff-(noDays*1000*60*60*24)-(noHours*1000*60*60)-(noMinutes*1000*60))/1000;
						row.add(noDays+"d "+noHours+"h "+noMinutes+"m "+noSecs+"s");
						timeDiff = (Long)cde.getTimeDistances().get(3);
						noDays = timeDiff/(1000*60*60*24);
						noHours =  (timeDiff-(noDays*1000*60*60*24))/(1000*60*60);
						noMinutes = ((timeDiff-(noDays*1000*60*60*24)-(noHours*1000*60*60)))/(1000*60);
						noSecs = (timeDiff-(noDays*1000*60*60*24)-(noHours*1000*60*60)-(noMinutes*1000*60))/1000;
						row.add(noDays+"d "+noHours+"h "+noMinutes+"m "+noSecs+"s");
					}else{
						if(cde.getConstraintDefinition().getName().contains("not")||cde.getConstraintDefinition().getName().contains("absence")){
							row.add("always satisfied");
							row.add("always satisfied");
							row.add("always satisfied");
							row.add("always satisfied");
						}else{
							row.add("always violated");
							row.add("always violated");
							row.add("always violated");
							row.add("always violated");
						}
					}
				}
				data.add(row);
				dtm = new DefaultTableModel(data,colNams);
				pt = new ProMTable(dtm);
				jp.add(pt, "0,3");
				if(histogram){
					splitPane.setBottomComponent(generateHistogram(cde.getHistogrData()));
				}else{
					SlickerFactory sf = SlickerFactory.instance();
					splitPane.setBottomComponent(sf.createRoundedPanel());
				}
				//jp.add(tabbed, "0,5");

				jp.repaint();



				JScrollPane rightTopPanelScroll = new JScrollPane(jp);  
				rightTopPanelScroll.setOpaque(false);
				rightTopPanelScroll.getViewport().setOpaque(false);
				rightTopPanelScroll.setBorder(BorderFactory.createEmptyBorder());
				rightTopPanelScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				rightTopPanelScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
				SlickerDecorator.instance().decorate(rightTopPanelScroll.getVerticalScrollBar(), new Color(0, 0, 0, 0),
						new Color(140, 140, 140), new Color(80, 80, 80));
				rightTopPanelScroll.getVerticalScrollBar().setOpaque(false);

				SlickerDecorator.instance().decorate(rightTopPanelScroll.getHorizontalScrollBar(), new Color(0, 0, 0, 0),
						new Color(140, 140, 140), new Color(80, 80, 80));
				rightTopPanelScroll.getHorizontalScrollBar().setOpaque(false);


				rightTopPanelScroll.setSize(new Dimension(300, 300));
				rightTopPanelScroll.setPreferredSize(new Dimension(300, 300));
				rightTopPanelScroll.setMinimumSize(new Dimension(300, 300));
				rightTopPanelScroll.setMaximumSize(new Dimension(300, 300));


				splitPane.setTopComponent(rightTopPanelScroll);
				splitPane.repaint();
				JPanel main = cde.getMainPanel();
				main.revalidate();
				main.repaint();
			}else{
				ProMSplitPane main = cde.getMetricsPanel();
				JPanel rightPanel = cde.getMainPanel(); 
				rightPanel.removeAll();
				rightPanel.setLayout(new TableLayout(new double[][] { {700}, {30, 100, 30, 100}}));
				SlickerFactory sf = SlickerFactory.instance();
				JLabel labelWithout = sf.createLabel("WITHOUT DATA CONDITION");
				JLabel labelWith = sf.createLabel("WITH DATA CONDITION");
				rightPanel.add(labelWithout,"0,0");
				Vector<String> colNams = new Vector<String>();
				colNams.add("Violations");
				colNams.add("Fulfillments");
				colNams.add("Violated Traces");
				colNams.add("Satisfied Traces");
				Vector data = new Vector();
				data.add(cde.getWithout());
				TableModel dtm = new DefaultTableModel(data,colNams);
				ProMTable pt = new ProMTable(dtm);
				rightPanel.add(pt,"0,1");
				rightPanel.add(labelWith,"0,2");
				colNams = new Vector();
				colNams.add("Violations");
				colNams.add("Fulfillments");
				colNams.add("Violated Traces");
				colNams.add("Satisfied Traces");
				data = new Vector();
				data.add(cde.getWith());
				dtm = new DefaultTableModel(data,colNams);
				pt = new ProMTable(dtm);
				rightPanel.add(pt,"0,3");
				
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

	private static ChartPanel generateHistogram(double[] data){
		HistogramDataset dataset = new HistogramDataset();
		dataset.setType(HistogramType.FREQUENCY);
		dataset.addSeries("Time Invocations Histogram", data, 20);

		JFreeChart chart =ChartFactory.createHistogram(
				"", 
				"Time (ms)", 
				"No. Invocations", 
				dataset, 
				PlotOrientation.VERTICAL, 
				true, 
				false, 
				false
				);
		chart.getXYPlot().setForegroundAlpha(0.75f);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		chartPanel.setMouseZoomable(true, false);
		// JFrame frame = new JFrame();
		//frame.setSize(chartPanel.getPreferredSize());
		//frame.getContentPane().add(chartPanel);
		//frame.setVisible(true);
		return chartPanel;
	}


}
