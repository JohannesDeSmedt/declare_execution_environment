package org.processmining.plugins.declareminer.importing;

import javax.swing.JComponent;

import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.plugins.declareminer.visualizing.DeclareMap;


public class DeclareModelVisualization {
	@PluginVariant(requiredParameterLabels = { 0 })
	public JComponent visualize(final PluginContext context, final DeclareMap model) {
		final JComponent frame = new DeclareModelVisualizer().showLogVis(model);
		return frame;
	}
	
	public static JComponent visualize(final DeclareMap model, final DeclareMap model2) {
		final JComponent frame = new DeclareModelVisualizer().showLogVis(model);
		return frame;
	}
}
