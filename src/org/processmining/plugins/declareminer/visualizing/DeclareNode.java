package org.processmining.plugins.declareminer.visualizing;

import java.awt.Dimension;

import org.processmining.models.graphbased.AttributeMap;
import org.processmining.models.graphbased.directed.AbstractDirectedGraph;
import org.processmining.models.graphbased.directed.AbstractDirectedGraphNode;

public class DeclareNode extends AbstractDirectedGraphNode {

	private final AbstractDirectedGraph<?, ?> graph;

	public DeclareNode(final ActivityDefinitonCell activity, final AbstractDirectedGraph<?, ?> graph) {
		this.graph = graph;
		getAttributeMap().put(AttributeMap.LABEL, activity.getActivityDefinition().getName());
		final Dimension size = new Dimension();
		size.setSize(activity.getWidth(), activity.getHeight());
		getAttributeMap().put(AttributeMap.SIZE, size);
	}

	@Override
	public AbstractDirectedGraph<?, ?> getGraph() {
		return graph;
	}

}
