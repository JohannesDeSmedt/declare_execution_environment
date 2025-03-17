package org.processmining.plugins.declareminer.visualizing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.processmining.models.graphbased.directed.AbstractDirectedGraph;
import org.processmining.models.graphbased.directed.DirectedGraph;
import org.processmining.models.graphbased.directed.DirectedGraphEdge;
import org.processmining.models.graphbased.directed.DirectedGraphElement;
import org.processmining.models.graphbased.directed.DirectedGraphNode;

public class DeclareDirectedGraph extends
		AbstractDirectedGraph<DirectedGraphNode, DirectedGraphEdge<DirectedGraphNode, DirectedGraphNode>> {

	private final DeclareModel model;
	private Map<ActivityDefinition, DirectedGraphNode> nodes;
	private HashSet<DirectedGraphEdge<DirectedGraphNode, DirectedGraphNode>> edges;

	public DeclareDirectedGraph(final DeclareModel model) {
		this.model = model;
	}

	// This is needed as the bound in the graph model is too strict
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public synchronized Set<DirectedGraphEdge<DirectedGraphNode, DirectedGraphNode>> getEdges() {
		if (edges == null) {
			getNodes();
			edges = new HashSet<DirectedGraphEdge<DirectedGraphNode, DirectedGraphNode>>();
			for (final ConstraintDefinition constraint : model.getModel().getConstraintDefinitions()) {
				boolean branches = constraint.getParameters().size() != 2;
				for (final Parameter parameter : constraint.getParameters()) {
					if (constraint.getBranches(parameter).size() != 1) {
						branches = true;
					}
				}
				if (branches) {
					// FIXME
				} else {
					final Iterator<Parameter> parameters = constraint.getParameters().iterator();
					final DirectedGraphEdge edge = new DeclareEdge((DeclareNode) nodes.get(constraint
							.getBranches(parameters.next()).iterator().next()), (DeclareNode) nodes.get(constraint
							.getBranches(parameters.next()).iterator().next()), constraint);
					edges.add(edge);
				}
			}
		}
		return edges;
	}

	@Override
	public synchronized Set<DirectedGraphNode> getNodes() {
		if (nodes == null) {
			nodes = new HashMap<ActivityDefinition, DirectedGraphNode>();
			for (final ActivityDefinitonCell activity : model.getView().activityDefinitionCells()) {
				nodes.put(activity.getActivityDefinition(), new DeclareNode(activity, this));
			}
		}
		return new HashSet<DirectedGraphNode>(nodes.values());
	}

	@Override
	public void removeEdge(final DirectedGraphEdge edge) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeNode(final DirectedGraphNode cell) {
		throw new UnsupportedOperationException();

	}

	@Override
	protected Map<? extends DirectedGraphElement, ? extends DirectedGraphElement> cloneFrom(
			final DirectedGraph<DirectedGraphNode, DirectedGraphEdge<DirectedGraphNode, DirectedGraphNode>> graph) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected AbstractDirectedGraph<DirectedGraphNode, DirectedGraphEdge<DirectedGraphNode, DirectedGraphNode>> getEmptyClone() {
		throw new UnsupportedOperationException();
	}

}
