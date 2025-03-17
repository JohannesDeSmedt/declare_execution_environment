package org.processmining.plugins.declareminer.visualizing;

/**
 * <p>
 * Title: DECLARE
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: TU/e
 * </p>
 * 
 * @author Maja Pesic
 * @version 1.0
 */
import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jgraph.graph.DefaultGraphCell;
import org.processmining.framework.util.ui.widgets.ProMSplitPane;

public class AssignmentModelView extends ModelView implements AssignmentModelListener {

	AssignmentModel model;
	transient DGraphSelectionListener graphListener;

	public AssignmentModelView(final AssignmentModel aModel) {
		super();
		model = aModel;
		model.addListener(this);
		graphListener = new DGraphSelectionListener(graph);
		// graph.getGraphLayoutCache().setFactory(new LTLCellViewFactory());
		// two last lines are added on 04-07-2006
		refreshCells();
		clearSelection();
		graph.updateUI();
		graph.repaint();

	}

	/**
	 * addActivityDefinition
	 * 
	 * @param activityDefinition
	 *            ActivityDefinition
	 * @param point
	 *            Point2D
	 */
	public void addActivityDefinition(final ActivityDefinition activityDefinition, final Point2D point) {
		final Double dx = new Double(point.getX());
		final Double dy = new Double(point.getY());
		final ActivityDefinitonCell view = createActivityCell(activityDefinition, dx.intValue(), dy.intValue());
		addVertex(view);
	}

	protected ActivityDefinitonCell createActivityCell(final ActivityDefinition activityDefinition, final int x,
			final int y) {
		final ActivityDefinitonCell view = new ActivityDefinitonCell(activityDefinition, x, y);
		return view;
	}

	/**
	 * addConstraintDefinition
	 * 
	 * @param object
	 *            ConstraintDefinition
	 */
	private void removeObject(final Object object) {
		final List<Object> list = new ArrayList<Object>();
		list.add(object);
		removeObjects(list);
	}

	/**
	 * removeCell
	 * 
	 * @param objects
	 *            List
	 */
	private void removeObjects(final List<Object> objects) {
		// a list where all DefaulGraphCell objects will be stored
		final List<Object> toRemove = new ArrayList<Object>();

		// loop through the list of user objects to find the DefaulGraphCell objects
		// that contain them as user objects
		for (int i = 0; i < objects.size(); i++) {
			final Object object = objects.get(i);
			final List<DefaultGraphCell> cells = getCells(object);
			if (cells != null) {
				toRemove.addAll(cells);
			}
		}
		removeCells(toRemove.toArray());
	}

	/**
	 * editConstraintDefinition
	 * 
	 * @param constraintDefinition
	 *            ConstraintDefinition
	 */
	public void editConstraintDefinition(final ConstraintDefinition constraintDefinition) {
		if (constraintDefinition != null) {
			final List<DefaultGraphCell> cells = this.getCells(constraintDefinition);
			if (cells == null) {
				return;
			}
			final Iterator<DefaultGraphCell> iterator = cells.iterator();
			while (iterator.hasNext()) {
				final DefaultGraphCell cell = iterator.next();
				if (cell instanceof ConstraintDefinitionEdge) {
					((ConstraintDefinitionEdge) cell).update();
					updateUI(cell);
				}
			}
		}
	}


	//	public void setConstraintDefinitionInvisible(final ConstraintDefinition constraintDefinition) {
	//		if (constraintDefinition != null) {
	//			final List<DefaultGraphCell> cells = this.getCells(constraintDefinition);
	//			if (cells == null) {
	//				return;
	//			}
	//			final Iterator<DefaultGraphCell> iterator = cells.iterator();
	//			while (iterator.hasNext()) {
	//				final DefaultGraphCell cell = iterator.next();
	//				if (cell instanceof ConstraintDefinitionEdge) {
	//					Color linecol = new Color(((ConstraintDefinitionEdge) cell).getLineColor().getRed(),((ConstraintDefinitionEdge) cell).getLineColor().getGreen(),((ConstraintDefinitionEdge) cell).getLineColor().getBlue(),0);
	//					Color forecol = new Color(((ConstraintDefinitionEdge) cell).getForeground().getRed(),((ConstraintDefinitionEdge) cell).getForeground().getGreen(),((ConstraintDefinitionEdge) cell).getForeground().getBlue(),0);
	//					Color backcol = new Color(((ConstraintDefinitionEdge) cell).getBackground().getRed(),((ConstraintDefinitionEdge) cell).getBackground().getGreen(),((ConstraintDefinitionEdge) cell).getBackground().getBlue(),0);
	//					((ConstraintDefinitionEdge) cell).setColor(linecol, forecol, backcol);
	//					((ConstraintDefinitionEdge) cell).setLabel(false);
	//					((ConstraintDefinitionEdge) cell).setVisible(false);
	//					((ConstraintDefinitionEdge) cell).setSelectable(false);
	//					((ConstraintDefinitionEdge) cell).setOpaque(false);
	////					constraintDefinition.setVisible(false);
	//					//((ConstraintDefinitionEdge) cell).setForeground(new Color(0,0,0,0));
	//					//((ConstraintDefinitionEdge) cell).setBackground(new Color(0,0,0,0));
	//					//((ConstraintDefinitionEdge) cell).setLine(new Color(0,0,0,0));
	//					//((ConstraintDefinitionEdge) cell).setLineColor(new Color(0,0,0,0));
	//
	//				}
	//			}
	//		}
	//	}



	//	public void setActivityDefinitionVisible(final ActivityDefinition activityDefinition) {
	//		if (activityDefinition != null) {
	//			//activityDefinition.setName(activityDefinition.getName().replace("<html><body text=EBEBEB>","<html>").replace("</body></html>", "</html>"));
	//			activityDefinition.setName(activityDefinition.getName().replace("<html><body text=404040>","<html>").replace("</body></html>", "</html>"));
	//			final List<DefaultGraphCell> cells = this.getCells(activityDefinition);
	//			if (cells == null) {
	//				return;
	//			}
	//			final Iterator<DefaultGraphCell> iterator = cells.iterator();
	//			while (iterator.hasNext()) {
	//				final DefaultGraphCell cell = iterator.next();
	//				if (cell instanceof ActivityDefinitonCell) {
	//					((ActivityDefinitonCell) cell).setForeground(new Color(((ActivityDefinitonCell) cell).getForeground().getRed(),((ActivityDefinitonCell) cell).getForeground().getGreen(),((ActivityDefinitonCell) cell).getForeground().getBlue()));
	//					((ActivityDefinitonCell) cell).setBackground(new Color(((ActivityDefinitonCell) cell).getBackground().getRed(),((ActivityDefinitonCell) cell).getBackground().getGreen(),((ActivityDefinitonCell) cell).getBackground().getBlue()));
	//					if(((ActivityDefinitonCell) cell).getOldSize()!= null){
	//						((ActivityDefinitonCell) cell).setSize(((ActivityDefinitonCell) cell).getOldSize());
	//					}
	//					((ActivityDefinitonCell) cell).setSelectable(true);
	//					((ActivityDefinitonCell) cell).setOpaque(true);
	//				activityDefinition.setVisible(true);
	//					updateUI(cell);
	//				}
	//			}
	//		}
	//	}


	//	public void setActivityDefinitionInvisible(final ActivityDefinition activityDefinition) {
	//		if (activityDefinition != null) {
	//			//			if(!activityDefinition.getName().contains("<html><body text=EBEBEB>")){
	//			if(!activityDefinition.getName().contains("<html><body text=404040>")){
	//				//activityDefinition.setName(activityDefinition.getName().replace("<html>", "<html><body text=EBEBEB>").replace("</html>","</body></html>"));
	//				activityDefinition.setName(activityDefinition.getName().replace("<html>", "<html><body text=404040>").replace("</html>","</body></html>"));
	//			}
	//			final List<DefaultGraphCell> cells = this.getCells(activityDefinition);
	//			if (cells == null) {
	//				return;
	//			}
	//			final Iterator<DefaultGraphCell> iterator = cells.iterator();
	//			while (iterator.hasNext()) {
	//				final DefaultGraphCell cell = iterator.next();
	//				if (cell instanceof ActivityDefinitonCell) {
	//					//((ActivityDefinitonCell) cell).setLabel("");
	//					((ActivityDefinitonCell) cell).setForeground(new Color(((ActivityDefinitonCell) cell).getForeground().getRed(),((ActivityDefinitonCell) cell).getForeground().getGreen(),((ActivityDefinitonCell) cell).getForeground().getBlue(),0));
	//					((ActivityDefinitonCell) cell).setBackground(new Color(((ActivityDefinitonCell) cell).getBackground().getRed(),((ActivityDefinitonCell) cell).getBackground().getGreen(),((ActivityDefinitonCell) cell).getBackground().getBlue(),0));
	//					updateUI(cell);
	//					//	((ActivityDefinitonCell) cell).setOldSize(new Point2D.Double(((ActivityDefinitonCell) cell).getWidth(),((ActivityDefinitonCell) cell).getHeight()));
	//					//	((ActivityDefinitonCell) cell).setSize(new Point2D.Double(0,0));
	//					((ActivityDefinitonCell) cell).setSelectable(false);
	//					((ActivityDefinitonCell) cell).setOpaque(false);
	//					activityDefinition.setVisible(false);
	//				}
	//			}
	//		}
	//	}

	//	public void setConstraintDefinitionVisible(final ConstraintDefinition constraintDefinition) {
	//		if (constraintDefinition != null) {
	//
	//			final List<DefaultGraphCell> cells = this.getCells(constraintDefinition);
	//			if (cells == null) {
	//				return;
	//			}
	//			boolean done = false;
	//			final Iterator<DefaultGraphCell> iterator = cells.iterator();
	//			while (iterator.hasNext()) {
	//				final DefaultGraphCell cell = iterator.next();
	//				if (cell instanceof ConstraintDefinitionEdge) {
	//					((ConstraintDefinitionEdge) cell).setColor(new Color(((ConstraintDefinitionEdge) cell).getLine().getRed(),((ConstraintDefinitionEdge) cell).getLine().getGreen(),((ConstraintDefinitionEdge) cell).getLine().getBlue()),new Color(((ConstraintDefinitionEdge) cell).getForeground().getRed(),((ConstraintDefinitionEdge) cell).getForeground().getGreen(),((ConstraintDefinitionEdge) cell).getForeground().getBlue()),new Color(((ConstraintDefinitionEdge) cell).getBackground().getRed(),((ConstraintDefinitionEdge) cell).getBackground().getGreen(),((ConstraintDefinitionEdge) cell).getBackground().getBlue()));
	//					if(!((ConstraintDefinitionEdge) cell).isVisible() && ! done){
	//						done = true;
	//						((ConstraintDefinitionEdge) cell).setLabel(true);
	//					}
	//					((ConstraintDefinitionEdge) cell).setSelectable(true);
	//					((ConstraintDefinitionEdge) cell).setOpaque(true);
	//					((ConstraintDefinitionEdge) cell).setVisible(true);
	////					constraintDefinition.setVisible(true);
	//					//	updateUI(cell);
	//				}
	//			}
	//		}
	//	}

	public void setConstraintDefinitionColor(final ConstraintDefinition constraintDefinition, Color linecolor,
			Color forecolor, Color backcolor) {
		if (constraintDefinition != null) {
			final List<DefaultGraphCell> cells = this.getCells(constraintDefinition);
			if (cells == null) {
				return;
			}
			final Iterator<DefaultGraphCell> iterator = cells.iterator();
			while (iterator.hasNext()) {
				final DefaultGraphCell cell = iterator.next();
				if (cell instanceof ConstraintDefinitionEdge) {
					((ConstraintDefinitionEdge) cell).setLineDash(new float[] { 2, 3 });
					((ConstraintDefinitionEdge) cell).setColor(linecolor, forecolor, backcolor);
					((ConstraintDefinitionEdge) cell).update();
					updateUI(cell);
				}
			}
		}
	}


	public void setConstraintDefinitionLabel(final ConstraintDefinition constraintDefinition, String label) {
		if (constraintDefinition != null) {
			final List<DefaultGraphCell> cells = this.getCells(constraintDefinition);
			if (cells == null) {
				return;
			}
			final Iterator<DefaultGraphCell> iterator = cells.iterator();
			while (iterator.hasNext()) {
				final DefaultGraphCell cell = iterator.next();
				if (cell instanceof ConstraintDefinitionEdge) {
					((ConstraintDefinitionEdge) cell).setLabel(true);
					((ConstraintDefinitionEdge) cell).update();
					updateUI(cell);
				}
			}
		}
	}

	public void setActivityDefinitionBackground(final ActivityDefinition activityDefinition, Color backcolor) {
		if (activityDefinition != null) {
			final List<DefaultGraphCell> cells = this.getCells(activityDefinition);
			if (cells == null) {
				return;
			}
			final Iterator<DefaultGraphCell> iterator = cells.iterator();
			while (iterator.hasNext()) {
				final DefaultGraphCell cell = iterator.next();
				if (cell instanceof ActivityDefinitonCell) {
					((ActivityDefinitonCell) cell).setBackground(backcolor);
					updateUI(cell);
				}
			}
		}
	}

	public void setActivityDefinitionLabels(final ActivityDefinition activityDefinition, JPanel main, ProMSplitPane metrics, HashMap<Object,Float> actSupp, HashMap<Object,HashMap> blnc) {
		if (activityDefinition != null) {
			final List<DefaultGraphCell> cells = this.getCells(activityDefinition);
			if (cells == null) {
				return;
			}
			Vector labels = new Vector();
			JLabel movesLog = new JLabel("Activity Support");
			//	movesLog.setForeground(Color.orange);
			//	movesLog.setSize(10,10);
			labels.add(movesLog);
			for(Object key : actSupp.keySet()){
				if(((Integer)key).equals(activityDefinition.getId())){
					labels.add(new JLabel(""+((Float)actSupp.get(key)).floatValue()));
				}
			}

			JLabel balance = new JLabel("Balanced Traces");
			//	movesLog.setForeground(Color.orange);
			//	movesLog.setSize(10,10);
			labels.add(balance);
			for(Object key : blnc.keySet()){
				if(((Integer)key).equals(activityDefinition.getId())){
					labels.add(new JLabel(""+((Float)((HashMap)blnc.get(key)).get("tracesBalanced")).floatValue()));
				}
			}

			JLabel ms = new JLabel("Traces with Missing Start");
			//	movesLog.setForeground(Color.orange);
			//	movesLog.setSize(10,10);
			labels.add(ms);
			for(Object key : blnc.keySet()){
				if(((Integer)key).equals(activityDefinition.getId())){
					labels.add(new JLabel(""+((Float)((HashMap)blnc.get(key)).get("tracesStart")).floatValue()));
				}
			}

			JLabel uds = new JLabel("Unbalancing Degree (Missing Start)");
			//	movesLog.setForeground(Color.orange);
			//	movesLog.setSize(10,10);
			labels.add(uds);
			for(Object key : blnc.keySet()){
				if(((Integer)key).equals(activityDefinition.getId())){
					labels.add(new JLabel(""+((Float)((HashMap)blnc.get(key)).get("unbalancingDegreeStart")).floatValue()));
				}
			}

			JLabel mc = new JLabel("Traces with Missing Complete");
			//	movesLog.setForeground(Color.orange);
			//	movesLog.setSize(10,10);
			labels.add(mc);
			for(Object key : blnc.keySet()){
				if(((Integer)key).equals(activityDefinition.getId())){
					labels.add(new JLabel(""+((Float)((HashMap)blnc.get(key)).get("tracesComplete")).floatValue()));
				}
			}


			JLabel udc = new JLabel("Unbalancing Degree (Missing Complete)");
			//	movesLog.setForeground(Color.orange);
			//	movesLog.setSize(10,10);

			labels.add(udc);
			for(Object key : blnc.keySet()){
				if(((Integer)key).equals(activityDefinition.getId())){
					labels.add(new JLabel(""+((Float)((HashMap)blnc.get(key)).get("unbalancingDegreeCompl")).floatValue()));
				}
			}

			final Iterator<DefaultGraphCell> iterator = cells.iterator();
			while (iterator.hasNext()) {
				final DefaultGraphCell cell = iterator.next();
				if (cell instanceof ActivityDefinitonCell) {
					((ActivityDefinitonCell) cell).setLabels(labels);
					((ActivityDefinitonCell) cell).setMetricsPanel(metrics);
					((ActivityDefinitonCell) cell).setMainPanel(main);
					updateUI(cell);
				}
			}
		}
	}


	public void setConstraintDefinitionLabels(boolean dataaware,final ConstraintDefinition constraintDefinition,JPanel mainPanel, ProMSplitPane metrics, HashMap cpir, HashMap sr, HashMap sa, HashMap sc,HashMap conf,HashMap i,HashMap acts,HashMap viols,HashMap confls,HashMap fulfills, HashMap maxTD, HashMap minTD, HashMap avgTD,HashMap stdDevTD, double[] histogrData) {
		if (constraintDefinition != null) {
			if(histogrData==null){
				avgTD = null;
			}else if(histogrData[0]==-1){
				avgTD = null;
			}
			final List<DefaultGraphCell> cells = this.getCells(constraintDefinition);
			if (cells == null) {
				return;
			}
			Vector labels = new Vector();
			JLabel movesProc = new JLabel("Support");
			//movesProc.setForeground(Color.BLUE);
			//	movesProc.setSize(10,10);
			labels.add(movesProc);
			for(Object key : sr.keySet()){
				if(((Integer)key).equals(constraintDefinition.getId())){
					labels.add(new JLabel(""+((Float)sr.get(key)).floatValue()));
				}
			}

			JLabel movesLog = new JLabel("CPIR");
			//	movesLog.setForeground(Color.orange);
			//	movesLog.setSize(10,10);
			labels.add(movesLog);
			for(Object key : cpir.keySet()){
				if(((Integer)key).equals(constraintDefinition.getId())){
					if(!(cpir.get(key) instanceof String)){
						labels.add(new JLabel(""+((Float)cpir.get(key)).floatValue()));
					}else{
						labels.add(new JLabel((String)cpir.get(key)));
					}
				}
			}

			//	JLabel suppAnt = new JLabel("Support Antecedent");

			//	labels.add(suppAnt);
			//	for(Object key : sa.keySet()){
			//		if(((Integer)key).equals(constraintDefinition.getId())){
			//			if(!(sa.get(key) instanceof String)){
			//				labels.add(new JLabel(""+((Float)sa.get(key)).floatValue()));
			//			}else{
			//				labels.add(new JLabel((String)sa.get(key)));
			//			}
			//		}
			//	}

			//	JLabel suppConseq = new JLabel("Support Consequent");
			//	labels.add(suppConseq);
			//	for(Object key : sc.keySet()){
			//		if(((Integer)key).equals(constraintDefinition.getId())){
			//			if(!(sc.get(key) instanceof String)){
			//				labels.add(new JLabel(""+((Float)sc.get(key)).floatValue()));
			//			}else{
			//				labels.add(new JLabel((String)sc.get(key)));
			//			}
			//		}
			//	}

			JLabel confidence = new JLabel("Confidence");
			//movesProc.setForeground(Color.BLUE);
			//	movesProc.setSize(10,10);
			labels.add(confidence);
			for(Object key : conf.keySet()){
				if(((Integer)key).equals(constraintDefinition.getId())){
					if(!(conf.get(key) instanceof String)){
						labels.add(new JLabel(""+((Float)conf.get(key)).floatValue()));
					}else{
						labels.add(new JLabel((String)conf.get(key)));
					}
				}
			}

			JLabel  intFact= new JLabel("Interest Factor");
			//movesProc.setForeground(Color.BLUE);
			//	movesProc.setSize(10,10);
			labels.add(intFact);
			for(Object key : i.keySet()){
				if(((Integer)key).equals(constraintDefinition.getId())){
					if(!(i.get(key) instanceof String)){
						labels.add(new JLabel(""+((Float)i.get(key)).floatValue()));
					}else{
						labels.add(new JLabel((String)i.get(key)));
					}
				}
			}

			/*	JLabel  actsl= new JLabel("Activations");

			labels.add(actsl);
			for(Object key : acts.keySet()){
				if(((Integer)key).equals(constraintDefinition.getId())){
					if(!(acts.get(key) instanceof String)){
						labels.add(new JLabel(""+((Integer)acts.get(key)).intValue()));
					}else{
						labels.add(new JLabel((String)acts.get(key)));
					}
				}
			}

			JLabel  fulfillsl= new JLabel("Fulfilments");

			labels.add(fulfillsl);
			for(Object key : fulfills.keySet()){
				if(((Integer)key).equals(constraintDefinition.getId())){
					if(!(fulfills.get(key) instanceof String)){
						labels.add(new JLabel(""+((Integer)fulfills.get(key)).intValue()));
					}else{
						labels.add(new JLabel((String)fulfills.get(key)));
					}
				}
			}

			JLabel  violsl= new JLabel("Violations");

			labels.add(violsl);
			for(Object key : viols.keySet()){
				if(((Integer)key).equals(constraintDefinition.getId())){
					if(!(viols.get(key) instanceof String)){
						labels.add(new JLabel(""+((Integer)viols.get(key)).intValue()));
					}else{
						labels.add(new JLabel((String)viols.get(key)));
					}
				}
			}

			JLabel  conflsl= new JLabel("Conflicts");

			labels.add(conflsl);
			for(Object key : confls.keySet()){
				if(((Integer)key).equals(constraintDefinition.getId())){
					if(!(confls.get(key) instanceof String)){
						labels.add(new JLabel(""+((Integer)confls.get(key)).intValue()));
					}else{
						labels.add(new JLabel((String)confls.get(key)));
					}
				}
			}
			 */


			Vector withoutData = new Vector();
			Vector withData = new Vector();
			if(dataaware){
				withoutData.add(cpir.get(constraintDefinition.getId()));
				withoutData.add(sr.get(constraintDefinition.getId()));
				withoutData.add(sa.get(constraintDefinition.getId()));
				withoutData.add(sc.get(constraintDefinition.getId()));

				withData.add(conf.get(constraintDefinition.getId()));
				withData.add(i.get(constraintDefinition.getId()));
				withData.add(acts.get(constraintDefinition.getId()));
				withData.add(viols.get(constraintDefinition.getId()));
			}

			Vector dataM = new Vector();
			Vector dataT = new Vector();
			if(avgTD!=null){
				dataM.add(acts.get(constraintDefinition.getId()));
				dataM.add(fulfills.get(constraintDefinition.getId()));
				dataM.add(viols.get(constraintDefinition.getId()));
				dataM.add(confls.get(constraintDefinition.getId()));


				dataT.add(maxTD.get(constraintDefinition.getId()));
				dataT.add(minTD.get(constraintDefinition.getId()));
				dataT.add(avgTD.get(constraintDefinition.getId()));
				dataT.add(stdDevTD.get(constraintDefinition.getId()));
			}else{
				dataM.add("-");
				dataT.add("-");
			}
			final Iterator<DefaultGraphCell> iterator = cells.iterator();
			while (iterator.hasNext()) {
				final DefaultGraphCell cell = iterator.next();
				if (cell instanceof ConstraintDefinitionEdge) {
					((ConstraintDefinitionEdge) cell).setWith(withData);
					((ConstraintDefinitionEdge) cell).setWithout(withoutData);
					((ConstraintDefinitionEdge) cell).setLabels(labels);
					((ConstraintDefinitionEdge) cell).setMetricsPanel(metrics);
					((ConstraintDefinitionEdge) cell).setMainPanel(mainPanel);
					((ConstraintDefinitionEdge) cell).setMetrics(dataM);
					((ConstraintDefinitionEdge) cell).setTimeDistances(dataT);
					((ConstraintDefinitionEdge) cell).setHistogrData(histogrData);
					updateUI(cell);
				}
			}
		}
	}

	public void setActivityDefinitionForeground(final ActivityDefinition activityDefinition, Color backcolor) {
		if (activityDefinition != null) {
			final List<DefaultGraphCell> cells = this.getCells(activityDefinition);
			if (cells == null) {
				return;
			}
			final Iterator<DefaultGraphCell> iterator = cells.iterator();
			while (iterator.hasNext()) {
				final DefaultGraphCell cell = iterator.next();
				if (cell instanceof ActivityDefinitonCell) {
					((ActivityDefinitonCell) cell).setForeground(backcolor);
					updateUI(cell);
				}
			}
		}
	}

	public void setActivityDefinitionLabel(final ActivityDefinition activityDefinition, String label) {
		if (activityDefinition != null) {
			final List<DefaultGraphCell> cells = this.getCells(activityDefinition);
			if (cells == null) {
				return;
			}
			final Iterator<DefaultGraphCell> iterator = cells.iterator();
			while (iterator.hasNext()) {
				final DefaultGraphCell cell = iterator.next();
				if (cell instanceof ActivityDefinitonCell) {
					((ActivityDefinitonCell) cell).setLabel(label);
					updateUI(cell);
				}
			}
		}
	}

	public void addConstraintDefinition(final ConstraintDefinition constraintDefinition) {
		if (constraintDefinition != null) {

			if (constraintDefinition.isUnary()) { // unary constraint
				final Parameter parameter = constraintDefinition.getFirstParameter();
				final ActivityDefinition branch = constraintDefinition.getFirstBranch(parameter);
				final ActivityDefinitonCell sourceCell = getView(branch);
				final ConstraintDefinitionEdge edge = createConstraintCell(constraintDefinition, branch, parameter);
				edge.setLabel(true);
				addEdge(edge, sourceCell, sourceCell);
			} else { // binary constraint
				final ConstraintConnector connector = new ConstraintConnector(constraintDefinition);
				addCell(connector);
				int x = 0;
				int y = 0;
				// int count = 0;
				boolean firstParameter = true;
				for (final Parameter parameter : constraintDefinition.getParameters()) {
					ConstraintDefinitionEdge first = null;
					boolean firstBranch = true;
					for (final ActivityDefinition branch : constraintDefinition.getBranches(parameter)) {
						final ActivityDefinitonCell source = getView(branch);

						x += source.getBounds().getCenterX();
						y += source.getBounds().getCenterY();
						final ConstraintDefinitionEdge edge = createConstraintCell(constraintDefinition, branch,
								parameter);

						DefaultGraphCell target = null;
						if (firstBranch) { // first branch
							first = edge;
							target = connector;
							if (firstParameter) { // only first branch of the first parameter has label
								edge.setLabel(true);
							}
						} else {
							target = first;
						}

						addEdge(edge, source, target);
						firstBranch = false;
						// count++;
						// f
					}
					firstParameter = false;
				}
				x /= constraintDefinition.parameterCount();
				y /= constraintDefinition.parameterCount();
				final Rectangle2D bounds = new Rectangle2D.Double(x, y, 1, 1);

				connector.setBounds(bounds);
			}

		}
		super.updateUI();
	}

	protected ConstraintDefinitionEdge createConstraintCell(final ConstraintDefinition constraint,
			final ActivityDefinition parameter, final Parameter formal) {
		return new ConstraintDefinitionEdge(constraint, parameter, formal);
	}

	public void updateActivityDefinition(final ActivityDefinition activityDefinition) {
		updateUI();
	}

	public void addActivityDefinition(final ActivityDefinition activityDefinition) {
		this.addActivityDefinition(activityDefinition, new Point(10, 10));
	}

	public void updateConstraintDefinition(final ConstraintDefinition constraintDefinition) {
		editConstraintDefinition(constraintDefinition);
	}

	public void deleteActivityDefinition(final ActivityDefinition activityDefinition) {
		removeObject(activityDefinition);
	}

	public void deleteConstraintDefiniton(final ConstraintDefinition constraintDefinition) {
		removeObject(constraintDefinition);
	}

	/**
	 * @param constraintDefinition
	 *            ConstraintDefinition
	 * @param activityDefinition
	 *            ActivityDefinition
	 */
	/*
	 * public void addBranch(ConstraintDefinition constraintDefinition,
	 * ActivityDefinition activityDefinition) { DefaultGraphCell cell =
	 * this.getCells(constraintDefinition).get(0); ParameterBranch branch =
	 * constraintDefinition.getBranchable().getBranch(activityDefinition);
	 * ConstraintDefinitionEdge constraintEdge = this.createConstraintCell(
	 * constraintDefinition, branch); ActivityDefinitonCell branchCell =
	 * this.getView(activityDefinition); // add the edge for the source and
	 * target cells. boolean forward = constraintDefinition.isForwardBranch();
	 * if (forward) { addEdge(constraintEdge, cell, branchCell); } else {
	 * addEdge(constraintEdge, branchCell, cell); } }
	 */

	/**
	 * @param constraintDefinition
	 *            ConstraintDefinition
	 * @param activityDefinition
	 *            ActivityDefinition
	 */
	public void deleteBranch(final ConstraintDefinition constraintDefinition,
			final ActivityDefinition activityDefinition) {
		Collection<DefaultGraphCell> cells = this.getCells(constraintDefinition);
		final Iterator<DefaultGraphCell> iterator = cells.iterator();
		ConstraintDefinitionEdge remove = null;
		while (iterator.hasNext()) {
			final DefaultGraphCell cell = iterator.next();
			if (cell instanceof ConstraintDefinitionEdge) {
				final ConstraintDefinitionEdge edge = (ConstraintDefinitionEdge) cell;
				final Object source = edge.getSource();
				final Object target = edge.getTarget();
				ActivityDefinitionPort port = null;
				// the edge to remove is fout if source or/and target ports are to activity definitions
				if (source instanceof ActivityDefinitionPort) {
					port = (ActivityDefinitionPort) source;
					if (port.ActivityDefinitionView().getActivityDefinition() == activityDefinition) {
						remove = edge;
					}
				}
				if (target instanceof ActivityDefinitionPort) {
					port = (ActivityDefinitionPort) target;
					if (port.ActivityDefinitionView().getActivityDefinition() == activityDefinition) {
						remove = edge;
					}
				}
			}
		}
		// if we found the edge to remove
		if (remove != null) {
			final Object source = remove.getSource();
			final Object target = remove.getTarget();
			if ((source instanceof ActivityDefinitionPort) && (target instanceof ActivityDefinitionPort)) {
				// removing the main edge

				cells = this.getCells(constraintDefinition); // get the remaining branches
				// first remove the one to be removed
				cells.remove(remove);
				// get the first one. we will make this one the main edge now
				final ConstraintDefinitionEdge edge = (ConstraintDefinitionEdge) cells.iterator().next();

				if (!(edge.getSource() instanceof ActivityDefinitionPort)) { // if the source is lost
					edge.setSource(remove.getSource());
				}
				if (!(edge.getTarget() instanceof ActivityDefinitionPort)) { // if the target is lost
					edge.setTarget(remove.getTarget());
				}
			}
			removeCells(new Object[] { remove });
		}
	}

	/**
	 * @param activityDefinition
	 *            ActivityDefinition
	 * @return ActivityDefinitionView
	 */
	private ActivityDefinitonCell getView(final ActivityDefinition activityDefinition) {
		ActivityDefinitonCell view = null;
		final List<DefaultGraphCell> cells = this.getCells(activityDefinition);
		if (cells.size() > 0) {
			final DefaultGraphCell cell = cells.get(0);
			if (cell instanceof ActivityDefinitonCell) {
				view = (ActivityDefinitonCell) cell;
			}
		}
		return view;
	}

	/**
	 * getActivityDefinitionCell
	 * 
	 * @param job
	 *            ActivityDefinition
	 * @return ActivityDefinitionCell
	 */
	public ActivityDefinitonCell getActivityDefinitionCell(final ActivityDefinition job) {
		final List<DefaultGraphCell> cells = this.getCells(job);
		ActivityDefinitonCell jobCell = null;
		if (cells != null) {
			if (cells.size() > 0) {
				final DefaultGraphCell cell = cells.get(0);
				if (cell != null) {
					if (cell instanceof ActivityDefinitonCell) {
						jobCell = (ActivityDefinitonCell) cell;
					}
				}
			}
		}
		return jobCell;
	}

	/**
	 * getActivityDefinitionCell
	 * 
	 * @param job
	 *            ActivityDefinition
	 * @return ActivityDefinitionCell
	 */
	public ConstraintConnector getConnector(final ConstraintDefinition constraint) {
		final List<DefaultGraphCell> cells = this.getCells(constraint);
		ConstraintConnector connector = null;
		if (cells != null) {
			if (cells.size() > 0) {
				final DefaultGraphCell cell = cells.get(0);
				if (cell != null) {
					if (cell instanceof ConstraintConnector) {
						connector = (ConstraintConnector) cell;
					}
				}
			}
		}
		return connector;
	}

	/**
	 * activityDefinitionCells
	 * 
	 * @return List
	 */
	public List<ActivityDefinitonCell> activityDefinitionCells() {
		final List<DVertex> vertexes = vertexCells();
		final List<ActivityDefinitonCell> cells = new ArrayList<ActivityDefinitonCell>();
		for (int i = 0; i < vertexes.size(); i++) {
			final DVertex vertex = vertexes.get(i);
			if (vertex instanceof ActivityDefinitonCell) {
				cells.add((ActivityDefinitonCell) vertex);
			}
		}
		return cells;
	}

	/**
	 * activityDefinitionCells
	 * 
	 * @return List
	 */
	public List<ConstraintConnector> connectorCells() {
		final List<ConstraintConnector> cells = new ArrayList<ConstraintConnector>();
		for (int i = 0; i < getModel().getRootCount(); i++) {
			final Object cell = getModel().getRootAt(i);
			if (cell instanceof ConstraintConnector) {
				cells.add((ConstraintConnector) cell);
			}
		}
		return cells;
	}

	/**
	 * refreshCells
	 */
	protected void refreshCells() {
		clear();
		for (int j = 0; j < model.activityDefinitionsCount(); j++) {
			final ActivityDefinition job = model.activityDefinitionAt(j);
			this.addActivityDefinition(job);
		}

		for (int c = 0; c < model.constraintDefinitionsCount(); c++) {
			final ConstraintDefinition constraint = model.constraintDefinitionAt(c);
			addConstraintDefinition(constraint);

			/*
			 * for (int p = 0; p < constraint.parameterCount(); p++) { Parameter
			 * parameter = constraint.parameterAt(p); // take care that we loop
			 * from the second branch: int b = 1! // this is because the first
			 * branch was already added when the constraint was // added in the
			 * first place for (int b = 1; b < parameter.branchesCount(); b++) {
			 * ParameterBranch branch = parameter.branchAt(b);
			 * this.addBranch(constraint, (ActivityDefinition)
			 * branch.getReal()); } }
			 */
		}
		graph.updateUI();
	}

	/**
	 * setBounds
	 * 
	 * @param bounds
	 *            Rectangle2D
	 * @param activityDefinition
	 *            ActivityDefinition
	 */
	public void setBounds(final Rectangle2D bounds, final ActivityDefinition activityDefinition) {
		final ActivityDefinitonCell cell = getActivityDefinitionCell(activityDefinition);
		if (cell != null) {
			cell.setBounds(bounds);
			this.updateUI();
		}
	}

	/**
	 * @param cell
	 *            DefaultGraphCell
	 * @return boolean
	 */
	protected boolean activityDefinitionViewClass(final DefaultGraphCell cell) {
		return cell instanceof ActivityDefinitonCell;
	}

	/**
	 * @param cell
	 *            DefaultGraphCell
	 * @return boolean
	 */
	protected boolean constraintDefinitionViewClass(final DefaultGraphCell cell) {
		return cell instanceof ConstraintDefinitionEdge;
	}

	/**
	 * @param cell
	 *            DefaultGraphCell
	 * @return ActivityDefinition
	 */
	protected ActivityDefinition getActivityDefiniton(final DefaultGraphCell cell) {
		ActivityDefinition activityDefiniton = null;
		if (cell != null) {
			if (activityDefinitionViewClass(cell)) {
				activityDefiniton = ((ActivityDefinitonCell) cell).getActivityDefinition();
			}
		}
		return activityDefiniton;
	}

}
