package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptController;
import hkust.cse.calendar.time.TimeController;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.GroupAppt;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.userstorage.UserController;

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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

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


class AppCellRenderer extends DefaultTableCellRenderer 
{
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
			int colorCMD, Color currColor) 
	{

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
			if (colorCMD == AppList.COLORED_TITLE) 
			{
				setBackground(currColor);
				setForeground(Color.black);
			}
			else if (colorCMD == AppList.COLORED) 
			{
				setBackground(currColor);
				setForeground(currColor);
			}

		}
		setVerticalAlignment(SwingConstants.TOP);
	}

	public int row() 
	{
		return r;
	}

	public int col() 
	{
		return c;
	}
}

public class AppList extends JPanel implements ActionListener 
{
	private int currentYear;
	private int currentMonth;
	private int currentDay;
	
	public static int SMALLEST_DURATION = 15;
	private static final long serialVersionUID = 1L;
	public static int OFFSET = 0;
	public static int ROWNUM = 48;
	public Appt[] todaylist;
	private final String[] names = { "Time", "Appointments", "Status", "Time",
			"Appointments", "Status" };
	private final int[] monthDays = { 31, 28, 31, 30, 31, 30, 31, 30, 31, 30,
			31, 30, 31 };
	private JTable tableView;
	private final Object[][] apptContentData = new Object[ROWNUM][6];
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
	private int[][] cellCMD = new int[ROWNUM][2];
	private Color[][] cellColor = new Color[ROWNUM][2];
	public Appt selectedAppt=null;
	private MouseEvent tempe;
	private Random random = new Random();
	private TitledBorder appListTitle;
	
	JMenuItem popupMenuList_NEW;
	JMenuItem popupMenuList_DELETE;
	JMenuItem popupMenuList_MODIFY;
	JMenuItem popupMenuList_DETAILS;
	
	//CONSTRUCTOR
	public AppList(int currentYear, int currentMonth, int currentDay) 
	{
		appListTitle = BorderFactory.createTitledBorder(currentYear +"-"+currentMonth + "-"+currentDay);
		appListTitle.setTitleColor(new Color(102, 0, 51));
		Font f = new Font("Helvetica", Font.BOLD + Font.ITALIC, 13);
		appListTitle.setTitleFont(f);
		setBorder(appListTitle);
	
		//initialize the Applist panel layout
		setLayout(new BorderLayout());
		currentRow = 0;
		currentCol = 0;
		
		//initialize the PopUpMenu
		pop = initializePopupMenu();

		//Initialize the table model
		getDataArray(apptContentData);
		TableModel dataModel = prepareTableModel();
		tableView = initializeTableView(dataModel);

		//Initialize the scrollpane
		JScrollPane scrollpane = initializeScrollPane(tableView); 
		add(scrollpane, BorderLayout.CENTER);


		setVisible(true);
		setSize(600, 300);	
		//this.
	}

	public void updateTitleBorderForAppList(int currentYear, int currentMonth, int currentDay)
	{
		appListTitle.setTitle(currentYear +"-"+currentMonth + "-"+currentDay);
	}
	
	
	
	private JPopupMenu initializePopupMenu()
	{
		JPopupMenu tempPopupMenu = new JPopupMenu();
		Font f1 = new Font("Helvetica", Font.ITALIC, 11);
		tempPopupMenu.setFont(f1);

		popupMenuList_NEW = (JMenuItem) tempPopupMenu.add(new JMenuItem("New"));
		
		popupMenuList_NEW.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{				
				NewAppt();
			}
		});

		popupMenuList_DELETE = (JMenuItem) tempPopupMenu.add(new JMenuItem("Delete"));

		popupMenuList_DELETE.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				delete();
			}
		});

		popupMenuList_MODIFY = (JMenuItem) tempPopupMenu.add(new JMenuItem("Modify"));
		popupMenuList_MODIFY.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				modify();
			}
		});

		tempPopupMenu.add(new JPopupMenu.Separator());
		popupMenuList_DETAILS = new JMenuItem("Details");
		
		Font f2 = new Font("Helvetica", Font.BOLD + Font.ITALIC, 11);
		popupMenuList_DETAILS.setFont(f2);
		tempPopupMenu.add(popupMenuList_DETAILS);

		popupMenuList_DETAILS.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				getDetail();
			}
		});

		return tempPopupMenu;
	}

	private JTable initializeTableView(TableModel dataModel)
	{
		JTable tempTableView = new JTable(dataModel) 
		{
			public TableCellRenderer getCellRenderer(int row, int col) 
			{
				if (col == 0 || col == 3)
					return new AppCellRenderer(new Object(), true, row, col, 1,
							null);
				else if (col == 1) 
				{

					return new AppCellRenderer(new Object(), false, row, col,
							cellCMD[row][0], cellColor[row][0]);

				} else if (col == 4) 
				{
					return new AppCellRenderer(new Object(), false, row, col,
							cellCMD[row][1], cellColor[row][1]);
				} else
					return new AppCellRenderer(new Object(), false, row, col,
							1, null);

			}
		};

		tempTableView.setAutoResizeMode(tempTableView.AUTO_RESIZE_ALL_COLUMNS);
		tempTableView.setRowHeight(20);
		tempTableView.setRowSelectionAllowed(false);
		TableColumn c = null;
		c = tempTableView.getColumnModel().getColumn(0);
		c.setPreferredWidth(60);
		c = tempTableView.getColumnModel().getColumn(3);
		c.setPreferredWidth(60);
		c = tempTableView.getColumnModel().getColumn(1);
		c.setPreferredWidth(215);
		c = tempTableView.getColumnModel().getColumn(4);
		c.setPreferredWidth(215);
		c = tempTableView.getColumnModel().getColumn(2);
		c.setPreferredWidth(60);
		c = tempTableView.getColumnModel().getColumn(5);
		c.setPreferredWidth(60);
		JTableHeader tableViewHeader = tempTableView.getTableHeader();
		tableViewHeader.setResizingAllowed(true);
		tableViewHeader.setReorderingAllowed(false);

		tempTableView.addMouseListener(new MouseAdapter() 
		{
			public void mousePressed(MouseEvent e) 
			{
				pressResponse(e);
			}

			public void mouseReleased(MouseEvent e) 
			{
				releaseResponse(e);
				if(e.getButton()==1)
					calculateDrag(e);
			}
		});

		return tempTableView;
	}

	public JScrollPane initializeScrollPane(JTable tableView)
	{
		JScrollPane tempScrollPane = new JScrollPane(tableView);
		tempScrollPane.setBorder(new BevelBorder(BevelBorder.LOWERED));
		tempScrollPane.setPreferredSize(new Dimension(695, 300));
		return tempScrollPane;
	}

	public TableModel prepareTableModel() 
	{

		TableModel dataModel = new AbstractTableModel() 
		{

			public int getColumnCount() 
			{
				return names.length;
			}

			public int getRowCount() 
			{
				return ROWNUM;
			}

			public Object getValueAt(int row, int col) 
			{
				return apptContentData[row][col];
			}

			public String getColumnName(int column) 
			{
				return names[column];
			}

			public Class getColumnClass(int c) 
			{
				return getValueAt(0, c).getClass();
			}

			public boolean isCellEditable(int row, int col) 
			{
				return false;
			}

			public void setValueAt(Object aValue, int row, int column) 
			{
				apptContentData[row][column] = aValue;
			}
		};
		return dataModel;
	}

	public void getDataArray(Object[][] data) 
	{
		int startingTimeAM = 0;
		int startingTimePM = 0;
		String s;
		String stringAM = new String("AM");
		String stringPM = new String("PM");

		int i;
		for (i = 0; i < ROWNUM; i++) 
		{
			if (startingTimeAM % 60 == 0)
				data[i][0] = (startingTimeAM / 60) + ":" + "00" + stringAM;
			else
				data[i][0] = (startingTimeAM / 60) + ":" + (startingTimeAM % 60) + stringAM;
			startingTimeAM = startingTimeAM + SMALLEST_DURATION;
			cellCMD[i][0] = NOT_COLORED;
			cellCMD[i][1] = NOT_COLORED;
			cellColor[i][0] = Color.white;
			cellColor[i][1] = Color.white;
		}
		for (i = 0; i < ROWNUM; i++) 
		{
			if (startingTimePM % 60 == 0)
			{
				if(startingTimePM == 0)
				{
					data[i][3] = "12" + ":" + "00" + stringPM;
				}
				else
				{
					data[i][3] = (startingTimePM / 60) + ":" + "00" + stringPM;
				}
			}
			else
			{
				if(startingTimePM / 60 == 0)
				{
					data[i][3] = "12" + ":" + (startingTimePM % 60) + stringPM;
				}
				else
				{
					data[i][3] = (startingTimePM / 60) + ":" + (startingTimePM % 60) + stringPM;
				}
			}
			startingTimePM = startingTimePM + SMALLEST_DURATION;
		}

	}

	// clear the appointment list on the gui
	public void clear() 
	{
		for (int i = 0; i < ROWNUM; i++) 
		{
			setTextAt(" ", i, 1);

			setTextAt(" ", i, 4);

			cellCMD[i][0] = NOT_COLORED;
			cellCMD[i][1] = NOT_COLORED;
			cellColor[i][0] = Color.white;
			cellColor[i][1] = Color.white;
		}
	}

	public void setTextAt(String text, int row, int col) 
	{
		TableModel t = tableView.getModel();
		t.setValueAt(text, row, col);
	}

	public String getTextAt(int row, int col) 
	{
		TableModel t = tableView.getModel();
		return (String) t.getValueAt(row, col);
	}

	public String getCurrentText() 
	{
		TableModel t = tableView.getModel();
		return (String) t.getValueAt(currentRow, currentCol);
	}

	public void setTodayAppt(List<Appt> list) 
	{
		if (list == null)
		{
			return;
		}
		
		for (Appt a : list)
		{
			addAppt(a);
		}
		
		repaint();

	}

	// colouring the appointment list
	public void addAppt(Appt appt) 
	{
		Color color;
		//currColor = new Color(0,240-(appt.TimeSpan().StartTime().getHours())*10,255-(appt.TimeSpan().StartTime().getMinutes()*3));
		//currColorForJoint = new Color(255-(appt.TimeSpan().StartTime().getHours())*10,0,190-(appt.TimeSpan().StartTime().getMinutes()*3));

		final float hue = random.nextFloat();
		final float saturation = 0.9f;//1.0 for brilliant, 0.0 for dull
		final float luminance = 1.0f; //1.0 for brighter, 0.0 for black
		currColor = Color.getHSBColor(hue, saturation, luminance);	
		currColorForJoint = Color.getHSBColor(hue, saturation, luminance);

		if(!appt.isRepeated())
		{
			color = currColor;
		}
		else
		{
			color = currColorForJoint;
		}
		
		if(appt == null)
		{
			return;
		}
			
		Timestamp tempStartTimeStamp = appt.TimeSpan().StartTime();
		int startDay = TimeController.getInstance().getDateFrom(tempStartTimeStamp);
		int startMin = TimeController.getInstance().getHourFrom(tempStartTimeStamp) * 60 + TimeController.getInstance().getMinuteFrom(tempStartTimeStamp);

		Timestamp tempEndTimeStamp = appt.TimeSpan().EndTime();
		int endDay = TimeController.getInstance().getDateFrom(tempEndTimeStamp);
		int endMin = TimeController.getInstance().getHourFrom(tempEndTimeStamp)* 60 + TimeController.getInstance().getMinuteFrom(tempEndTimeStamp);
		
		if(endMin == 0 && (endDay == (startDay+1)))
			endMin= endMin + 24*60;
		
		int[] pos = new int[2];
		for (int i = startMin; i < endMin; i = i + SMALLEST_DURATION) 
		{
			pos = calculateRowColNum(i);
			if (i == startMin) 
			{
				tableView.getModel().setValueAt(appt, pos[0], pos[1]);

				if (pos[1] == 1) 
				{
					cellCMD[pos[0]][0] = COLORED_TITLE;
					cellColor[pos[0]][0] = color;
				} 
				else 
				{
					cellCMD[pos[0]][1] = COLORED_TITLE;
					cellColor[pos[0]][1] = color;
				}
			} 
			else 
			{
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

	public int[] calculateRowColNum(int startTime) 
	{
		int[] position = new int[2];
		position[0] = startTime / SMALLEST_DURATION;

		if (position[0] > (ROWNUM - 1)) 
		{
			position[0] = position[0] - ROWNUM;
			position[1] = 4;
		} 
		else
		{
			position[1] = 1;
		}

		/*if (position[1] == 4 && position[0] > ROWNUM - 1)
		{
			position[0] = ROWNUM - 1;
		}*/

		return position;
	}

	private void getDetail() 
	{

		Appt apptTitle = getSelectedApptTitle();
		if (apptTitle == null)
			return;

		DetailsDialog info = new DetailsDialog(apptTitle, "Appointment Details");

		info.setVisible(true);

	}

	private void delete() 
	{
		Appt selectedAppt = getSelectedApptTitle();

		if(selectedAppt == null)
		{
			return;
		}
		else
		{
			if(selectedAppt instanceof GroupAppt){
				GroupAppt selectedGroupAppt = (GroupAppt) selectedAppt;
				String ownerString = selectedGroupAppt.getOwner();
				User owner = UserController.getInstance().getUser(ownerString);
				
				if(UserController.getInstance().getCurrentUser() == owner){		//if the current user is the owner
					int reply = JOptionPane.showConfirmDialog(parent, "This will delete the group event from all the attending users");
					if (reply != JOptionPane.YES_OPTION)
						return;
					for(String attendeeString :selectedGroupAppt.getAttendList()){
						User attendee = UserController.getInstance().getUser(attendeeString);
						List<Appt> eachGroupApptList = ApptController.getInstance().RetrieveApptsInList(attendee,
								selectedGroupAppt.getTimeSpan());
						GroupAppt eachGroupAppt;
						if(eachGroupApptList.size() <= 1 && eachGroupApptList.size() != 0){
							eachGroupAppt = (GroupAppt)eachGroupApptList.get(0);
						}
						else{
							System.out.println("Something wrong with deleting group appts!");
							return;
						}
						if(eachGroupAppt != null){
							ApptController.getInstance().removeAppt(attendee, eachGroupAppt);
						}
					}
					parent.getAppListPanel().clear();
					parent.getAppListPanel().setTodayAppt(parent.GetTodayAppt());
					parent.repaint();
					JOptionPane.showMessageDialog(parent, "Deleted successfully!");
				}
				else{		//if the current user is NOT the owner.
					JOptionPane.showMessageDialog(this, "Only the owner of the group appointment can delete/modify!");	
					return;
				}
			}
			else{
				if (selectedAppt.isRepeated()){
					int reply = JOptionPane.showConfirmDialog(parent, "This will delete all repeated schedule");
					if (reply != JOptionPane.YES_OPTION)
						return ;
				}
				if (ApptController.getInstance().removeAppt(UserController.getInstance().getCurrentUser(), selectedAppt)){
					parent.getAppListPanel().clear();
					parent.getAppListPanel().setTodayAppt(parent.GetTodayAppt());
					parent.repaint();
					JOptionPane.showMessageDialog(parent, "Deleted successfully!");
				}
				else{
					JOptionPane.showMessageDialog(parent, "Failed to Delete",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}


	}

	private void modify() 
	{
		Appt selectedAppt = getSelectedApptTitle();

		if (selectedAppt == null)
		{
			return;
		}
		
		if(selectedAppt instanceof GroupAppt){
			GroupAppt selectedGroupAppt = (GroupAppt) selectedAppt;
			String ownerString = selectedGroupAppt.getOwner();
			User owner = UserController.getInstance().getUser(ownerString);
			
			if(UserController.getInstance().getCurrentUser() == owner){		//if the current user is the owner
				int reply = JOptionPane.showConfirmDialog(parent, "This will modify the group event for all the attending users");
				if (reply != JOptionPane.YES_OPTION)
					return;
			}
			else{		//if the current user is NOT the owner.
				JOptionPane.showMessageDialog(this, "Only the owner of the group appointment can delete/modify!");	
				return;
			}
			AppScheduler setAppDial = new AppScheduler("Modify", "Group", parent, selectedAppt);
			setAppDial.updateSettingAppt(selectedAppt);
			setAppDial.show();
			setAppDial.setResizable(false);
			
		}
		else{
			if (selectedAppt.isRepeated()){
				int reply = JOptionPane.showConfirmDialog(parent, "This will modify all repeated schedule");
				if (reply != JOptionPane.YES_OPTION)
					return ;
			}
			AppScheduler setAppDial = new AppScheduler("Modify", "Single", parent, selectedAppt);
			setAppDial.updateSettingAppt(selectedAppt);
			setAppDial.show();
			setAppDial.setResizable(false);
		}
	}

	public Appt getSelectedApptTitle() 
	{

		Object apptTitle;
		if (currentRow < 0 || currentRow > ROWNUM - 1) 
		{
			JOptionPane.showMessageDialog(parent, "Please Select Again !",
					"Error", JOptionPane.ERROR_MESSAGE);

			selectedAppt=null;
			return selectedAppt;
		}
		if (currentCol < 3) 
		{
			apptTitle = tableView.getModel().getValueAt(currentRow, 1);
		}
		else
		{
			apptTitle = tableView.getModel().getValueAt(currentRow, 4);
		}


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

	private void NewAppt() 
	{

		if (parent.mCurrUser == null)
			return;
		if (currentRow < 0 || currentRow > ROWNUM - 1) 
		{
			JOptionPane.showMessageDialog(parent, "Please Select Again !",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		int startTime;

		if (currentCol < 3)
			startTime = currentRow * 15;
		else
			startTime = (currentRow + ROWNUM) * 15;
		AppScheduler a = new AppScheduler("New","Single",parent);
		a.updateSettingAppt(hkust.cse.calendar.gui.Utility.createDefaultAppt(
				parent.getCurrentlySelectedYear(), parent.getCurrentlySelectedMonth(), parent.getCurrentlySelectedDay(),
				parent.mCurrUser, startTime));
		a.setLocationRelativeTo(null);
		a.show();

	}

	private void pressResponse(MouseEvent e) 
	{
		tempe = e;
		pressRow = tableView.getSelectedRow();
		pressCol = tableView.getSelectedColumn();

		if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0)
		{
			if(getSelectedApptTitle() == null)
			{
				popupMenuList_NEW.setEnabled(true);
				popupMenuList_MODIFY.setEnabled(false);
				popupMenuList_DELETE.setEnabled(false);
				popupMenuList_DETAILS.setEnabled(false);
			}
			else
			{
				popupMenuList_NEW.setEnabled(false);
				popupMenuList_MODIFY.setEnabled(true);
				popupMenuList_DELETE.setEnabled(true);
				popupMenuList_DETAILS.setEnabled(true);
			}
			
			pop.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	private void releaseResponse(MouseEvent e) 
	{

		releaseRow = tableView.getSelectedRow();
		releaseCol = tableView.getSelectedColumn();

		if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0)
		{
			if(getSelectedApptTitle() == null)
			{
				popupMenuList_NEW.setEnabled(true);
				popupMenuList_MODIFY.setEnabled(false);
				popupMenuList_DELETE.setEnabled(false);
				popupMenuList_DETAILS.setEnabled(false);
			}
			else
			{
				popupMenuList_NEW.setEnabled(false);
				popupMenuList_MODIFY.setEnabled(true);
				popupMenuList_DELETE.setEnabled(true);
				popupMenuList_DETAILS.setEnabled(true);
			}
			
			pop.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	private void calculateDrag(MouseEvent e)
	{

		if(releaseRow==pressRow)
		{		
			currentRow = tableView.getSelectedRow()+tableView.getSelectedRowCount()-1;			
		}
		else
		{
			currentRow = releaseRow;

		}

		if(releaseCol==pressCol)
		{			
			currentCol = tableView.getSelectedColumn()+tableView.getSelectedColumnCount()-1;
		}
		else
		{
			currentCol = releaseCol;
		}

	}
	public void setParent(CalGrid grid) 
	{
		parent = grid;
	}

	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == tableView) 
		{
			pop.show(tableView, currentRow * 20, currentRow * 20);

		}

	}

}
