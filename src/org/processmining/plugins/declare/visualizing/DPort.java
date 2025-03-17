package org.processmining.plugins.declare.visualizing;

import org.jgraph.graph.DefaultPort;

public class DPort extends DefaultPort {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4577240013297646609L;
	private final DVertex vertex;

	public DPort(DVertex aVertex, Parameter parameter) {
		super(parameter);
		vertex = aVertex;
	}

	public DPort(DVertex aVertex) {
		super();
		vertex = aVertex;
	}

	protected DVertex getVertex() {
		return vertex;
	}
}
