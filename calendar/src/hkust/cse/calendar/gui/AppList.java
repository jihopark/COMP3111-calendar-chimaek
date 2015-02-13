package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.unit.Appt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;


class AppCellRenderer extends DefaultTableCellRenderer {
	private int r;
	private int c;

	// public final static int EARLIEST_TIME = 480;
	//
	// public final static int LATEST_TIME = 1050;
	//
	// public final static int[] monthDays = { 31, 28, 31, 30, 31, 30, 31, 31,
	// 30,
	// 31, 30, 31 };

	public AppCellRenderer(Object value, boolean override, int row, int col,
			int colorCMD, Color currColor) {

		Font f1 = new Font("Helvetica", Font.ITALIC, 11);
		if (override) {
			if (row % 2 == 0)
				setBackground(new Color(153, 204, 255));
			else
				setBackground(new Color(204, 204, 255));
			setForeground(Color.black);

		}
		if (col == 2 || col == 5)
			setFont(f1);
		if (col != 0 && col != 3)
			setHorizontalAlignment(SwingConstants.LEFT);
		else
			setHorizontalAlignment(SwingConstants.RIGHT);
		if (col == 1 || col == 4) {
			if (colorCMD == AppList.COLORED_TITLE) {
				setBackground(currColor);
				setForeground(Color.black);
			} else if (colorCMD == AppList.COLORED) {
				setBackground(currColor);
				setForeground(currColor);
			}

		}
		setVerticalAlignment(SwingConstants.TOP);
	}

	public int row() {
		return r;
	}

	public int col() {
		return c;
	}
}

public class AppList extends JPanel implements ActionListener {
	public static int SMALLEST_DURATION = 15;
	private static final long serialVersionUID = 1L;
	public static int OFFSET = 8;
	public static int ROWNUM = 20;
	public Appt[] todaylist;
	private final String[] names = { "Time", "Appointments", "Status", "Time",
			"Appointments", "Status" };
	private final int[] monthDays = { 31, 28, 31, 30, 31, 30, 31, 30, 31, 30,
			31, 30, 31 };
	private JTable tableView;
	private final Object[][] data = new Object[20][6];
	private JPopupMenu pop;
	private int currentRow;
	private int currentCol;
	private int pressRow;
	private int pressCol;
	private int releaseRow;
	private int releaseCol;
	private CalGrid parent;
	private Color currColor = Color.green;
	private Color currColorForJoint = Color.red;
	public final static int COLORED_TITLE = 1;
	public final static int COLORED = 2;
	public final static int NOT_COLORED = 0;
	private int[][] cellCMD = new int[20][2];
	private Color[][] cellColor = new Color[20][2];
	public Appt selectedAppt=null;
	private MouseEvent tempe;
	public AppList() {
		setLayout(new BorderLayout());
		currentRow = 0;
		currentCol = 0;
		
		TitledBorder b = BorderFactory
				.createTitledBorder("Appointment Contents");
		b.setTitleColor(new Color(102, 0, 51));
		Font f = new Font("Helvetica", Font.BOLD + Font.ITALIC, 11);
		b.setTitleFont(f);
		setBorder(b);
		Font f1 = new Font("Helvetica", Font.ITALIC, 11);

		pop = new JPopupMenu();
		pop.setFont(f1);

		JMenuItem mi;
		mi = (JMenuItem) pop.add(new JMenuItem("New"));

		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				NewAppt();
			}
		});

		mi = (JMenuItem) pop.add(new JMenuItem("Delete"));

		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				delete();
			}
		});

		mi = (JMenuItem) pop.add(new JMenuItem("Modify"));
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modify();
			}
		});
		pop.add(new JPopupMenu.Separator());
		JMenuItem j = new JMenuItem("Details");
		j.setFont(f);
		pop.add(j);

		j.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getDetail();
			}
		});

		getDataArray(data);
		TableModel dataModel = prepareTableModel();
		tableView = new JTable(dataModel) {
			public TableCellRenderer getCellRenderer(int row, int col) {
				if (col == 0 || col == 3)
					return new AppCellRenderer(new Object(), true, row, col, 1,
							null);
				else if (col == 1) {

					return new AppCellRenderer(new Object(), false, row, col,
							cellCMD[row][0], cellColor[row][0]);

				} else if (col == 4) {
					return new AppCellRenderer(new Object(), false, row, col,
							cellCMD[row][1], cellColor[row][1]);
				} else
					return new AppCellRenderer(new Object(), false, row, col,
							1, null);

			}
		};

		tableView.setAutoResizeMode(tableView.AUTO_RESIZE_ALL_COLUMNS);
		tableView.setRowHeight(20);
		tableView.setRowSelectionAllowed(false);
		TableColumn c = null;
		c = tableView.getColumnModel().getColumn(0);
		c.setPreferredWidth(60);
		c = tableView.getColumnModel().getColumn(3);
		c.setPreferredWidth(60);
		c = tableView.getColumnModel().getColumn(1);
		c.setPreferredWidth(215);
		c = tableView.getColumnModel().getColumn(4);
		c.setPreferredWidth(215);
		c = tableView.getColumnModel().getColumn(2);
		c.setPreferredWidth(60);
		c = tableView.getColumnModel().getColumn(5);
		c.setPreferredWidth(60);
		JTableHeader h = tableView.getTableHeader();
		h.setResizingAllowed(true);
		h.setReorderingAllowed(false);

		tableView.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				pressResponse(e);
			}

			public void mouseReleased(MouseEvent e) {
				releaseResponse(e);
				if(e.getButton()==1)
				calculateDrag(e);
			}
		});

		JScrollPane scrollpane = new JScrollPane(tableView);
		scrollpane.setBorder(new BevelBorder(BevelBorder.LOWERED));
		scrollpane.setPreferredSize(new Dimension(695, 300));
		add(scrollpane, BorderLayout.CENTER);
		setVisible(true);
		setSize(600, 300);
		//this.
	}

	public TableModel prepareTableModel() {

		TableModel dataModel = new AbstractTableModel() {

			public int getColumnCount() {
				return names.length;
			}

			public int getRowCount() {
				return 20;
			}

			public Object getValueAt(int row, int col) {
				return data[row][col];
			}

			public String getColumnName(int column) {
				return names[column];
			}

			public Class getColumnClass(int c) {
				return getValueAt(0, c).getClass();
			}

			public boolean isCellEditable(int row, int col) {
				return false;
			}

			public void setValueAt(Object aValue, int row, int column) {
				data[row][column] = aValue;
			}
		};
		return dataModel;
	}

	public void getDataArray(Object[][] data) {
		int tam = 480;
		int tpm = 60;
		String s;
		String am = new String("AM");
		String pm = new String("PM");

		int i;
		for (i = 0; i < ROWNUM; i++) {
			if (tam % 60 == 0)
				data[i][0] = (tam / 60) + ":" + "00" + am;
			else
				data[i][0] = (tam / 60) + ":" + (tam % 60) + am;
			tam = tam + SMALLEST_DURATION;
			cellCMD[i][0] = NOT_COLORED;
			cellCMD[i][1] = NOT_COLORED;
			cellColor[i][0] = Color.white;
			cellColor[i][1] = Color.white;
		}
		for (i = 0; i < ROWNUM; i++) {
			if (tpm % 60 == 0)
				data[i][3] = (tpm / 60) + ":" + "00" + pm;
			else
				data[i][3] = (tpm / 60) + ":" + (tpm % 60) + pm;
			tpm = tpm + SMALLEST_DURATION;
		}

	}

	// clear the appointment list on the gui
	public void clear() {
		for (int i = 0; i < 20; i++) {
			setTextAt(" ", i, 1);

			setTextAt(" ", i, 4);

			cellCMD[i][0] = NOT_COLORED;
			cellCMD[i][1] = NOT_COLORED;
			cellColor[i][0] = Color.white;
			cellColor[i][1] = Color.white;
		}
	}

	public void setTextAt(String text, int row, int col) {
		TableModel t = tableView.getModel();
		t.setValueAt(text, row, col);
	}

	public String getTextAt(int row, int col) {
		TableModel t = tableView.getModel();
		return (String) t.getValueAt(row, col);
	}

	public String getCurrentText() {
		TableModel t = tableView.getModel();
		return (String) t.getValueAt(currentRow, currentCol);
	}
	
	public void setTodayAppt(Appt[] list) {
		if (list == null)
			return;
		for (int i = 0; i < list.length; i++)
			addAppt(list[i]);
		repaint();

	}

	// colouring the appointment list
	public void addAppt(Appt appt) {
		Color color;
		currColor = new Color(0,240-(appt.TimeSpan().StartTime().getHours()-8)*25,255-(appt.TimeSpan().StartTime().getMinutes()*3));
		currColorForJoint = new Color(255-(appt.TimeSpan().StartTime().getHours()-8)*25,0,190-(appt.TimeSpan().StartTime().getMinutes()*3));
		if(!appt.isJoint())
			color = currColor;
		else
			color = currColorForJoint;
		
		if (appt == null)
			return;

		Timestamp temp;

		temp = appt.TimeSpan().StartTime();
		int startMin = temp.getHours() * 60 + temp.getMinutes();
		startMin = startMin - OFFSET * 60;

		temp = appt.TimeSpan().EndTime();
		int endMin = temp.getHours() * 60 + temp.getMinutes();
		endMin = endMin - OFFSET * 60;

		int[] pos = new int[2];
		for (int i = startMin; i < endMin; i = i + SMALLEST_DURATION) {
			pos = calRowColNum(i);
			if (i == startMin) {
				tableView.getModel().setValueAt(appt, pos[0], pos[1]);

				if (pos[1] == 1) {
					cellCMD[pos[0]][0] = COLORED_TITLE;
					cellColor[pos[0]][0] = color;
				} else {
					cellCMD[pos[0]][1] = COLORED_TITLE;
					cellColor[pos[0]][1] = color;
				}
			} else {
				tableView.getModel().setValueAt(appt, pos[0], pos[1]);

				if (pos[1] == 1) {
					cellCMD[pos[0]][0] = COLORED;
					cellColor[pos[0]][0] = color;
				} else {
					cellCMD[pos[0]][1] = COLORED;
					cellColor[pos[0]][1] = color;
					
				}

			}
		}

		//if (currColor.equals(Color.yellow))
		//	currColor = Color.pink;
		//else
		//	currColor = Color.yellow;
		

	}

	public int[] calRowColNum(int total) {
		int[] position = new int[2];
		position[0] = total / SMALLEST_DURATION;

		if (position[0] > (ROWNUM - 1)) {
			position[0] = position[0] - ROWNUM;
			position[1] = 4;
		} else
			position[1] = 1;
		if (position[1] == 4 && position[0] > ROWNUM - 1)
			position[0] = ROWNUM - 1;
		return position;
	}

	private void getDetail() {

		Appt apptTitle = getSelectedAppTitle();
		if (apptTitle == null)
			return;

		DetailsDialog info = new DetailsDialog(apptTitle, "Appointment Details");

		info.setVisible(true);

	}

	private void delete() {

	}

	private void modify() {
		Appt apptTitle = getSelectedAppTitle();
		if (apptTitle == null)
			return;
		AppScheduler setAppDial = new AppScheduler("Modify", parent, apptTitle.getID());

		setAppDial.updateSetApp(apptTitle);
		setAppDial.show();
		setAppDial.setResizable(false);

	}

	public Appt getSelectedAppTitle() {
		
		Object apptTitle;
		if (currentRow < 0 || currentRow > ROWNUM - 1) {
			JOptionPane.showMessageDialog(parent, "Please Select Again !",
					"Error", JOptionPane.ERROR_MESSAGE);
			
			selectedAppt=null;
			return selectedAppt;
		}
		if (currentCol < 3) {
			apptTitle = tableView.getModel().getValueAt(currentRow, 1);
		} else
			apptTitle = tableView.getModel().getValueAt(currentRow, 4);
		

		if (apptTitle instanceof Appt)
		{
			selectedAppt=(Appt) apptTitle;
			return  selectedAppt;
		}		
		else
		{
			selectedAppt=null;
			return selectedAppt;
		}
			

	}
	
	private void NewAppt() {
		
		if (parent.mCurrUser == null)
			return;
		if (currentRow < 0 || currentRow > ROWNUM - 1) {
			JOptionPane.showMessageDialog(parent, "Please Select Again !",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		int startTime;
		
		if (currentCol < 3)
			startTime = currentRow * 15 + 480;
		else
			startTime = (currentRow + 20) * 15 + 480;
		AppScheduler a = new AppScheduler("New", parent);
		a.updateSetApp(hkust.cse.calendar.gui.Utility.createDefaultAppt(
				parent.currentY, parent.currentM, parent.currentD,
				parent.mCurrUser, startTime));
		a.setLocationRelativeTo(null);
		a.show();
		
	}

	private void pressResponse(MouseEvent e) {
		tempe = e;
		pressRow = tableView.getSelectedRow();
		pressCol = tableView.getSelectedColumn();
		if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0)
			pop.show(e.getComponent(), e.getX(), e.getY());
	}
	private void releaseResponse(MouseEvent e) {
		
		releaseRow = tableView.getSelectedRow();
		releaseCol = tableView.getSelectedColumn();
		if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0)
			pop.show(e.getComponent(), e.getX(), e.getY());
	}
	private void calculateDrag(MouseEvent e){
		
		if(releaseRow==pressRow){		
			currentRow = tableView.getSelectedRow()+tableView.getSelectedRowCount()-1;			
		}else{
			currentRow = releaseRow;
			
		}
		
		if(releaseCol==pressCol){			
			currentCol = tableView.getSelectedColumn()+tableView.getSelectedColumnCount()-1;
		}else{
			currentCol = releaseCol;
		}
		
	}
	public void setParent(CalGrid grid) {
		parent = grid;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == tableView) {
			pop.show(tableView, currentRow * 20, currentRow * 20);

		}
		
	}

}
