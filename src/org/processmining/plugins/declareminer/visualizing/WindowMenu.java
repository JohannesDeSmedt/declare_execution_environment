package org.processmining.plugins.declareminer.visualizing;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;

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
class WindowMenu extends JMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6935823044528907714L;
	private final Collection<WindowMenuItem> frames;
	ButtonGroup group;
	WindowMenuListener listener = null;

	/**
	 * Frames
	 */
	WindowMenu() {
		super("Window");
		frames = new HashSet<WindowMenuItem>();
		group = new BlankButtonGroup();
		setVisibility();
	}

	/**
   *
   */
	private void setVisibility() {
		setVisible(frames.size() > 0);
	}

	/**
	 * 
	 * @param active
	 *            boolean
	 * @param frame
	 *            JInternalFrame
	 */
	void add(boolean active, JInternalFrame frame) {
		WindowMenuItem item = new WindowMenuItem(frame);
		frames.add(item);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() instanceof WindowMenuItem) {
					changed((WindowMenuItem) e.getSource());
				}
			}
		});
		this.add(item);
		group.add(item);
		activate(active, frame);
		setVisibility();
	}

	/**
	 * 
	 * @param active
	 *            boolean
	 * @param frame
	 *            JInternalFrame
	 */
	void activate(boolean active, JInternalFrame frame) {
		WindowMenuItem item = get(frame);
		if (item != null) {
			group.setSelected(item.getModel(), active);
		}
	}

	/**
	 * 
	 * @param frame
	 *            JInternalFrame
	 */
	void remove(JInternalFrame frame) {
		WindowMenuItem item = get(frame);
		if (item != null) {
			frames.remove(item);
			this.remove(item);
			group.remove(item);
			setVisibility();
		}
	}

	/**
	 * 
	 * @param frame
	 *            JInternalFrame
	 * @return FrameMenuItem
	 */
	WindowMenuItem get(JInternalFrame frame) {
		WindowMenuItem item = null;
		boolean found = false;
		Iterator<WindowMenuItem> iterator = frames.iterator();
		while (iterator.hasNext() && !found) {
			item = iterator.next();
			found = item.frame(frame);
		}
		return found ? item : null;
	}

	/**
	 * 
	 * @param item
	 *            ItemEvent
	 */
	private void changed(WindowMenuItem item) {
		if (item != null) {
			if (!item.isSelected()) {
				item.setSelected(true);
			} else {
				if (listener != null) {
					listener.itemSelected(item.getFrame(), true);
				}
			}
		}
	}

	public void frameChanged(JInternalFrame frame) {
		WindowMenuItem item = get(frame);
		if (item != null) {
			item.frameChanged();
		}
	}

	public void addListener(WindowMenuListener l) {
		listener = l;
	}
}

class WindowMenuItem extends JCheckBoxMenuItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6833810674766810637L;
	private final JInternalFrame frame;

	/**
	 * JFrameManuItem
	 * 
	 * @param frame
	 *            JInternalFrame
	 */
	public WindowMenuItem(JInternalFrame frame) {
		super();
		this.frame = frame;
		frameChanged();
	}

	boolean frame(JInternalFrame frame) {
		return this.frame == frame;
	}

	void activate(boolean active) {
		int style = active ? Font.BOLD : Font.PLAIN;
		Font old = getFont();
		Font font = new Font(old.getFontName(), style, old.getSize());
		setFont(font);

	}

	void frameChanged() {
		setText(frame.getTitle());
	}

	JInternalFrame getFrame() {
		return frame;
	}
}

interface WindowMenuListener {
	void itemSelected(JInternalFrame frame, boolean active);
}
