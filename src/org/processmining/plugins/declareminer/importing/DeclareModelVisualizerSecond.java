/*
 * Copyright (c) 2007 Christian W. Guenther (christian@deckfour.org)
 * 
 * LICENSE:
 * 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
 * 
 * EXEMPTION:
 * 
 * License to link and use is also granted to open source programs which are not
 * licensed under the terms of the GPL, given that they satisfy one or more of
 * the following conditions: 1) Explicit license is granted to the ProM and
 * ProMimport programs for usage, linking, and derivative work. 2) Carte blance
 * license is granted to all programs developed at Eindhoven Technical
 * University, The Netherlands, or under the umbrella of STW Technology
 * Foundation, The Netherlands. For further exemptions not covered by the above
 * conditions, please contact the author of this code.
 */
package org.processmining.plugins.declareminer.importing;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import org.processmining.plugins.declare.visualizing.Cell;
import org.processmining.plugins.declare.visualizing.CellModel;
import org.processmining.plugins.declare.visualizing.DGraph;
import org.processmining.plugins.declare.visualizing.ListRenderer;
import org.processmining.plugins.declareminer.visualizing.DeclareMap;

import com.fluxicon.slickerbox.factory.SlickerDecorator;
import com.fluxicon.slickerbox.factory.SlickerFactory;

/**
 * @author Fabrizio M. Maggi
 * 
 */
public class DeclareModelVisualizerSecond extends JPanel implements MouseListener {
	/**
	 * 
	 */
	//private static final long serialVersionUID = 215385982744090270L;
	protected DGraph graph;
    protected JPanel mainPanel;
	@Override
	public void mouseClicked(final MouseEvent arg0) {
		if (graph != null) {
			graph.refresh();
			graph.repaint();
		}
		if (mainPanel != null) {
			mainPanel.repaint();
		}
	}

	@Override
	public void mouseEntered(final MouseEvent arg0) {
		// TODO Auto-generated method stub
		if (mainPanel != null) {
			mainPanel.repaint();
		}
	}

	@Override
	public void mouseExited(final MouseEvent arg0) {
		// TODO Auto-generated method stub
		if (mainPanel != null) {
			mainPanel.repaint();
		}
	}

	@Override
	public void mousePressed(final MouseEvent arg0) {
		if (graph != null) {
			graph.refresh();
			graph.repaint();
		}
		if (mainPanel != null) {
			mainPanel.repaint();
		}

	}

	@Override
	public void mouseReleased(final MouseEvent arg0) {
		if (graph != null) {
			graph.refresh();
			graph.repaint();
		}
		if (mainPanel != null) {
			mainPanel.repaint();
		}
	}

	public JComponent showLogVis(final DeclareMap model) {
		initialize();
		completeGui(model);
		return this;
	}

	protected void completeGui(final DeclareMap viz) {
		DeclareMap decModel = viz;
		
	
		SlickerFactory sf = SlickerFactory.instance();
		//mainPanel=new ProMSplitPane(ProMSplitPane.HORIZONTAL_SPLIT);
		
		mainPanel = sf.createRoundedPanel();
		JPanel leftPanel = sf.createRoundedPanel();
		JPanel rightPanel = sf.createRoundedPanel();
		Vector labels = new Vector();
		
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
		rightPanel.setLayout(new TableLayout(new double[][] { { TableLayoutConstants.FILL}, {TableLayoutConstants.FILL } }));
		rightPanel.add(scroll, "0,0");
		
		
		
		//rightPanel.add(label, "0,0");
		graph = null;
		if ((decModel != null) && (decModel.getViewCh() != null)) {
			graph = decModel.getViewCh().getGraph();

			//			JGraphLayoutAlgorithm.applyLayout(graph,new TreeLayoutAlgorithm(),graph.getComponents());
			//final JScrollPane scroll = new JScrollPane(graph);
			graph.addMouseListener(this);
			//scroll.addMouseListener(this);
			graph.refresh();
			graph.repaint();
			//	scroll.repaint();
			JScrollPane scrollpane = new JScrollPane(graph);
			scrollpane.setOpaque(false);
			scrollpane.getViewport().setOpaque(false);
			scrollpane.setBorder(BorderFactory.createEmptyBorder());
			scrollpane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			//SlickerDecorator.instance().decorate(scrollpane.getVerticalScrollBar(), new Color(0, 0, 0, 0),
			//		new Color(140, 140, 140), new Color(80, 80, 80));
			scrollpane.getVerticalScrollBar().setOpaque(false);

			//SlickerDecorator.instance().decorate(scrollpane.getHorizontalScrollBar(), new Color(0, 0, 0, 0),
			//		new Color(140, 140, 140), new Color(80, 80, 80));
			scrollpane.getHorizontalScrollBar().setOpaque(false);
			leftPanel.setLayout(new TableLayout(new double[][] { { TableLayoutConstants.FILL}, {TableLayoutConstants.FILL } }));
			leftPanel.add(scrollpane, "0,0");
		}
		
//		for(ActivityDefinition ad: decModel.getModelCh().getActivityDefinitions()){
//			decModel.getViewCh().setActivityDefinitionLabels(ad,mainPanel,rightPanel, viz.getAdML(), viz.getAdMP(), viz.getAdMLP());
//		}
//		
//		for(ConstraintDefinition cd: decModel.getModelCh().getConstraintDefinitions()){
//			decModel.getViewCh().setConstraintDefinitionLabels(cd,mainPanel, rightPanel,viz.getCdML(), viz.getCdMP());
//		}
		
		//mainPanel.add(leftPanel, "0,0");
		//mainPanel.add(rightPanel, "1,0");
		
		//mainPanel.setLeftComponent(leftPanel);
		//mainPanel.setResizeWeight(1.0);
		//mainPanel.setOneTouchExpandable(true);
		//mainPanel.setRightComponent(rightPanel);
		add(leftPanel);
		revalidate();
		repaint();
	}

	/**
	 * loads the log and initializes this component
	 */
	protected void initialize() {
		String promDir = System.getProperty("user.dir");
		if (promDir.endsWith(".")) {
			promDir = promDir.substring(0, promDir.length() - 1);
		}
		if (promDir.endsWith(System.getProperty("file.separator")) == false) {
			promDir += System.getProperty("file.separator");
		}
		setBackground(new Color(40, 40, 40));

		setBorder(BorderFactory.createEmptyBorder());
		setLayout(new BorderLayout());

	}
}