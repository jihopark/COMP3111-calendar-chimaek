package hkust.cse.calendar.gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

class CalCellRenderer extends DefaultTableCellRenderer

{

	private int r;

	private int c;

	public CalCellRenderer(int value) {
		if (value != 0) {
			if(value ==1) {
				setForeground(Color.white);
				setBackground(Color.green);
			}
			else if(value==2) {
				setForeground(Color.blue);
				setBackground(Color.white);
			}
		} else {
			setForeground(Color.black);
			setBackground(Color.white);
		}
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
