package org.processmining.plugins.declare.visualizing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.fluxicon.slickerbox.factory.SlickerFactory;

public class ListRenderer implements ListCellRenderer {

	private final JLabel label;
	//private final JPanel panel;

	public ListRenderer() {
		SlickerFactory sf = SlickerFactory.instance();
		//panel = new JPanel();
		//	panel.setBackground(new Color(160, 160, 160));
		label = sf.createLabel("");
		//	panel.setLayout(new BorderLayout());
		//	panel.add(label, BorderLayout.WEST);
		//	panel.setBorder(new EmptyBorder(3, 2, 3, 0));
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		// TODO Auto-generated method stub
		Cell c = (Cell) value;
		if(c.getLabel().equals("Moves in Log")){
			label.setForeground(Color.orange);
			label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, label.getFont().getSize()));
		}
		if(c.getLabel().equals("Moves in Model")){
			label.setForeground(Color.blue);
			label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, label.getFont().getSize() ));
		}
		if(c.getLabel().equals("Moves in Both")){
			label.setForeground(Color.GREEN);
			label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, label.getFont().getSize() ));
		}
		label.setText(c.getLabel());
		//return panel;
		return label;
	}

}
