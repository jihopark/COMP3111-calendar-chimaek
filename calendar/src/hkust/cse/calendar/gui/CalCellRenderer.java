package hkust.cse.calendar.gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

class CalCellRenderer extends DefaultTableCellRenderer

{

	private int r;

	private int c;

	public CalCellRenderer(Object value) {
		if (value != null) {
			setForeground(Color.blue);
			setBackground(Color.black);
		} else
			setForeground(Color.black);
		setBackground(Color.white);
		setHorizontalAlignment(SwingConstants.RIGHT);
		setVerticalAlignment(SwingConstants.TOP);
	}

	public int row() {
		return r;
	}

	public int col() {
		return c;
	}

}
