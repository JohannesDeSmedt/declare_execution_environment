package org.processmining.plugins.declare.visualizing;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

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
public class ActivityDefinitonCell extends DVertex {

	/**
	 * 
	 */
	private static final long serialVersionUID = -319493519832010439L;
    private JPanel metricsPanel;
    private JPanel mainPanel;
    public JPanel getMainPanel() {
		return mainPanel;
	}

	public void setMainPanel(JPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	private Vector labels;

    /**
	 * 
	 * @param anActivityDefinition
	 *            ActivityDefinition
	 */
	public ActivityDefinitonCell(ActivityDefinition anActivityDefinition, int x, int y) {
		super(anActivityDefinition, x, y, null, false, false);
		addPort();
		if (anActivityDefinition.isExternal()) {
			Border border = GraphConstants.getBorder(getAttributes());
			Font old = GraphConstants.getFont(getAttributes());
			Font font = new Font(old.getName(), Font.BOLD, old.getSize() - 2);
			GraphConstants.setBorder(getAttributes(),
					BorderFactory.createTitledBorder(border, "YAWL", TitledBorder.LEADING, TitledBorder.TOP, font));
		}
	}

	public void setBackground(Color color) {
		GraphConstants.setGradientColor(getAttributes(), color);
	}
	
	public void setForeground(Color color) {
		GraphConstants.setForeground(getAttributes(), color);
	}

	public void setLabel(String label) {
		Border border = GraphConstants.getBorder(getAttributes());
		System.out.println("Changing label "+label);
		//LineBorder bord = new LineBorder (Color.black, 7);
		for(Object o: getAttributes().keySet()){
			System.out.println("o "+o+" is "+getAttributes().get(o));
		}
		GraphConstants.setBorder(getAttributes(), BorderFactory.createTitledBorder(border, label,
				TitledBorder.ABOVE_BOTTOM, TitledBorder.ABOVE_BOTTOM, GraphConstants.getFont(getAttributes())));
	}
	
	public void setMetricsPanel(JPanel metricsPanel) {
		this.metricsPanel = metricsPanel;
	}
	
	public void setLabels(Vector labels) {
		this.labels = labels;
	}
	
	public Vector getLabels() {
		return labels;
	}
	
	public JPanel getMetricsPanel() {
		return metricsPanel;
	}

	/**
	 * 
	 * @param anActivityDefinition
	 *            ActivityDefinition
	 */
	protected ActivityDefinitonCell(DVertex vertex) {
		super(vertex);
		addPort();
	}

	/**
	 * 
	 * @return ActivityDefinition
	 */
	public ActivityDefinition getActivityDefinition() {
		ActivityDefinition activityDefinition = null;
		Object userObject = getUserObject();
		if (userObject != null) {
			if (userObject instanceof ActivityDefinition) {
				activityDefinition = (ActivityDefinition) userObject;
			}
		}
		return activityDefinition;
	}

	/**
	 * addPort
	 */
	public Object addPort() {
		ActivityDefinitionPort activityDefinitionPort = new ActivityDefinitionPort(this);
		add(activityDefinitionPort);
		return activityDefinitionPort;
	}

	public double getDiameter() {
		double diameter = Math.sqrt(Math.pow(super.getHeight(), 2) + Math.pow(super.getWidth(), 2)) + 20;
		return diameter;
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseClicked(MouseEvent e) {
        JPanel jp = ((ActivityDefinitonCell)e.getSource()).getMetricsPanel();
        jp.removeAll();
        Vector labels = ((ActivityDefinitonCell)e.getSource()).getLabels();
        for(int i =0; i< labels.size(); i ++){
        	jp.add((JLabel)labels.get(i));
        }
        jp.repaint();
        JDialog jd = new JDialog();
		jd.setVisible(true);
		
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
}
