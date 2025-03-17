package org.processmining.plugins.declareminer.visualizing;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ListRenderer implements ListCellRenderer {

	private final JCheckBox box;

	public ListRenderer() {
		box = new JCheckBox();
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		// TODO Auto-generated method stub
		Cell c = (Cell) value;
		if (c.isSelected()) {
			box.setSelected(true);
		} else {
			box.setSelected(false);
		}
		box.setText(c.getLabel());
		return box;
	}

}
