package org.processmining.plugins.declareminer.visualizing;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.net.URL;

import javax.swing.ImageIcon;

import org.jgraph.graph.GraphConstants;

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
public class ActivityCell extends ActivityDefinitonCell {

	private static final long serialVersionUID = -8623688566984682674L;

	private static final int DEFAULT_BORDER_WIDTH = 2;
	private static final Color ENABLED_COLOR = Color.BLACK;
	private static final Color DISABLED_COLOR = Color.GRAY;

	private static final String NONE = "none.gif";
	private static final String START = "start.gif";
	private static final String STOP = "stop.gif";
	private static final String START_STOP = "start_stop.gif";

	public ActivityCell(final Activity activity, final int x, final int y) {
		super(activity, x, y);
		if (activity != null) {
			if (activity.isExternal()) {
				final Rectangle2D bounds = new Rectangle2D.Double(x, y, WIDTH, 100);
				GraphConstants.setBounds(getAttributes(), bounds);
			}
		}
		
	}

	@Override
	protected int getBorderWidth() {
		return DEFAULT_BORDER_WIDTH;
	}

	void setEnabled(final boolean enabled) {
		final Font cellFont = GraphConstants.getFont(getAttributes()); // keep the same font
		Font font = null;
		Color color = null;
		if (enabled) { // settings for enabled activities
			color = ENABLED_COLOR;
			font = enableFont(cellFont);
		} else { // settings for disabled activities
			color = DISABLED_COLOR;
			font = disableFont(cellFont);
		}
		// now apply settings
		GraphConstants.setBorderColor(getAttributes(), color);
		GraphConstants.setFont(getAttributes(), font);
		GraphConstants.setForeground(getAttributes(), color);
	}

	/**
	 * @param old
	 *            Font
	 * @return Font
	 */
	public static Font enableFont(final Font old) {
		return new Font(old.getName(), Font.BOLD, old.getSize());
	}

	/**
	 * @param old
	 *            Font
	 * @return Font
	 */
	public static Font disableFont(final Font old) {
		return new Font(old.getName(), Font.PLAIN, old.getSize());
	}

	private ImageIcon getIcon(final String icon) {
		final URL insertUrl = URLLoader.loadResource(icon);
		return new ImageIcon(insertUrl);
	}

	public void setState(final boolean start, final boolean complete) {
		String image = NONE;
		if (start && complete) {
			image = START_STOP;
		}
		if (start && !complete) {
			image = START;
		}
		if (!start && complete) {
			image = STOP;
		}

		final ImageIcon icon = getIcon(image);
		if (icon != null) {
			GraphConstants.setIcon(getAttributes(), icon);
		}
	}
}
