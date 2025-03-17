package org.processmining.plugins.declareminer.visualizing;

import org.processmining.models.graphbased.AttributeMap;
import org.processmining.models.graphbased.directed.AbstractDirectedGraphEdge;

public class DeclareEdge extends AbstractDirectedGraphEdge<DeclareNode, DeclareNode> {

	public DeclareEdge(final DeclareNode source, final DeclareNode target, final ConstraintDefinition constraint) {
		super(source, target);
		getAttributeMap().put(AttributeMap.LABEL, constraint.getName());
	}

}
