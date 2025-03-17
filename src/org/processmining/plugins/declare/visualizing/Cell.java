package org.processmining.plugins.declare.visualizing;

public class Cell {

	private String label;
	private boolean currentSelected;

	public boolean isCurrentSelected() {
		return currentSelected;
	}

	public void setCurrentSelected(boolean currentSelected) {
		this.currentSelected = currentSelected;
	}

	private boolean selected;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
