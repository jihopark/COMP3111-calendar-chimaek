package hkust.cse.calendar.gui;

import hkust.cse.calendar.Main.CalendarMain;
import hkust.cse.calendar.apptstorage.ApptController;
import hkust.cse.calendar.apptstorage.ApptStorageMemory;
import hkust.cse.calendar.notification.NotificationCheckThread;
import hkust.cse.calendar.time.TimeController;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.metal.MetalBorders.Flush3DBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


public class CalGrid extends JFrame implements ActionListener {

	// private User mNewUser;
	private static final long serialVersionUID = 1L;
	public User mCurrUser;
	private String mCurrTitle = "Desktop Calendar - No User - ";
	private Date todayDate;
	public int currentDay;
	public int currentMonth;
	public int currentYear;
	public int previousRow;
	public int previousCol;
	public int currentRow;
	public int currentCol;
	private BasicArrowButton monthRightArrowButton;
	private BasicArrowButton monthLeftArrowButton;
	private JLabel yearLabel;
	private JComboBox monthComboBox;
	private NotificationCheckThread notificationCheckThread = new NotificationCheckThread();
	
	private final Object[][] data = new Object[6][7];
	private final int[] apptMarker = new int[31];
	private final String[] dayNames = { "Sunday", "Monday", "Tuesday",
			"Wednesday", "Thursday", "Friday", "Saturday" };
	private final String[] months = { "January", "February", "March", "April",
			"May", "June", "July", "August", "September", "October",
			"November", "December" };
	private JTable tableView;
	private AppList applistPanel;
	public static final int[] monthDays = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
			31, 30, 31 };
	private JTextPane textPaneImportantDays;

	private JSplitPane panelUpper;
	private JSplitPane panelWhole;
	private JScrollPane scrollPaneMonthView;
	private StyledDocument styledDocImportantDays = null;
	private SimpleAttributeSet sabImportantDays = null;
	// private boolean isLogin = false;
	private JMenu Appmenu = new JMenu("Appointment");
	
	
	private final String[] holidays = {
			"New Years Day\nSpring Festival\n",
			"President's Day (US)\n",
			"",
			"Ching Ming Festival\nGood Friday\nThe day following Good Friday\nEaster Monday\n",
			"Labour Day\nThe Buddha's Birthday\nTuen Ng Festival\n",
			"",
			"Hong Kong Special Administrative Region Establishment Day\n",
			"Civic Holiday(CAN)\n",
			"",
			"National Day\nChinese Mid-Autumn Festival\nChung Yeung Festival\nThanksgiving Day\n",
			"Veterans Day(US)\nThanksgiving Day(US)\n", "Christmas\n" };
	
	private AppScheduler setAppDial;

	public CalGrid() {
		super();
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		mCurrUser = null;

		previousRow = 0;
		previousCol = 0;
		currentRow = 0;
		currentCol = 0;

		setJMenuBar(createMenuBar());
		
		//Change respective to time controller
		todayDate = TimeController.getInstance().getCurrentTimeInDate();
		currentYear = todayDate.getYear()+1900;
		currentDay = todayDate.getDate();
		//int temp = todayDate.getMonth()+1;
		//currentMonth = 12;
		currentMonth = todayDate.getMonth()+1;

		applistPanel = new AppList(currentYear,currentMonth,currentDay);
		applistPanel.setParent(this);
		
		
		getDateArray(data);

		JPanel panelMonthYearAndImportantDays = new JPanel();
		panelMonthYearAndImportantDays.setLayout(new BorderLayout());
		panelMonthYearAndImportantDays.setPreferredSize(new Dimension(500, 300));

		JLabel labelImportantDays = new JLabel("Important Days");
		labelImportantDays.setForeground(Color.red);

		textPaneImportantDays = new JTextPane();
		textPaneImportantDays.setEditable(false);
		textPaneImportantDays.setBorder(new Flush3DBorder());
		styledDocImportantDays = textPaneImportantDays.getStyledDocument();
		sabImportantDays = new SimpleAttributeSet();
		StyleConstants.setBold(sabImportantDays, true);
		StyleConstants.setFontSize(sabImportantDays, 30);

		JPanel panelImportantDays = new JPanel();
		panelImportantDays.setLayout(new BorderLayout());
		panelImportantDays.add(labelImportantDays, BorderLayout.NORTH);
		panelImportantDays.add(textPaneImportantDays, BorderLayout.CENTER);

		panelMonthYearAndImportantDays.add(panelImportantDays, BorderLayout.CENTER);

		monthRightArrowButton = new BasicArrowButton(SwingConstants.EAST);
		monthRightArrowButton.setEnabled(true);
		monthRightArrowButton.addActionListener(this);
		monthLeftArrowButton = new BasicArrowButton(SwingConstants.WEST);
		monthLeftArrowButton.setEnabled(true);
		monthLeftArrowButton.addActionListener(this);

		yearLabel = new JLabel(new Integer(currentYear).toString());
		monthComboBox = new JComboBox();
		monthComboBox.addActionListener(this);
		monthComboBox.setPreferredSize(new Dimension(200, 30));
		for (int cnt = 0; cnt < 12; cnt++)
			monthComboBox.addItem(months[cnt]);
		monthComboBox.setSelectedIndex(currentMonth+1);

		JPanel yearGroup = new JPanel();
		yearGroup.setLayout(new FlowLayout());
		yearGroup.setBorder(new Flush3DBorder());
		yearGroup.add(monthLeftArrowButton);
		yearGroup.add(yearLabel);
		yearGroup.add(monthRightArrowButton);
		yearGroup.add(monthComboBox);

		panelMonthYearAndImportantDays.add(yearGroup, BorderLayout.NORTH);

		TableModel dataModel = prepareTableModel();
		
		tableView = new JTable(dataModel) {
			public TableCellRenderer getCellRenderer(int row, int col) {
				getDateArray(data);
				String tem = (String) data[row][col];
				if (tem.equals("") == false) {
					try {
						//System.out.println("todayDate: "+(todayDate.getYear()+1900)+" "+(todayDate.getMonth()+1)+" "+todayDate.getDate());
						//System.out.println("currentXx: "+currentYear+" "+currentMonth+" "+extract_only_date_string(tem));
						
						if ((todayDate.getYear()+1900) == currentYear
								&& (todayDate.getMonth()+1) == currentMonth
								&& todayDate.getDate() == extract_only_date_string(tem)) {
							return new CalCellRenderer(1); //1 if today
						}
						else if(tem.length()>2)
							return new CalCellRenderer(2); //2 if not today but has appointments
					} catch (Throwable e) {
						System.exit(1);
					}

				}
				return new CalCellRenderer(0); //0 if empty slot, and not today
			}
		};

		tableView.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tableView.setRowHeight(40);
		tableView.setRowSelectionAllowed(false);
		tableView.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				mousePressResponse();
			}

			public void mouseReleased(MouseEvent e) {
				mouseResponse();
			}
		});

		JTableHeader head = tableView.getTableHeader();
		head.setReorderingAllowed(false);
		head.setResizingAllowed(true);

		scrollPaneMonthView = new JScrollPane(tableView);
		scrollPaneMonthView.setBorder(new BevelBorder(BevelBorder.RAISED));
		scrollPaneMonthView.setPreferredSize(new Dimension(536, 260));

		panelUpper = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelMonthYearAndImportantDays, scrollPaneMonthView);

		panelWhole = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelUpper, applistPanel);
		getContentPane().add(panelWhole);

		
		initializeSystem(); // for you to add.
		//mCurrUser = getCurrUser(); // totally meaningless code
		Appmenu.setEnabled(true);

		UpdateCal();
		pack();				// sized the window to a preferred size
		setVisible(true);	//set the window to be visible
	}

	public TableModel prepareTableModel() {

		TableModel dataModel = new AbstractTableModel() {

			public int getColumnCount() {
				return dayNames.length;
			}

			public int getRowCount() {
				return 6;
			}

			public Object getValueAt(int row, int col) {
				return data[row][col];
			}

			public String getColumnName(int column) {
				return dayNames[column];
			}

			public Class getColumnClass(int c) {
				return getValueAt(0, c).getClass();
			}

			public boolean isCellEditable(int row, int col) {
				return false;
			}

			public void setValueAt(Object aValue, int row, int column) {
				System.out.println("Setting value to: " + aValue);
				data[row][column] = aValue;
			}
		};
		return dataModel;
	}

	public void getDateArray(Object[][] data) {
		
		GregorianCalendar c = new GregorianCalendar(currentYear, currentMonth - 1, 1);
		int day;
		int date;
		Date d = c.getTime();
		c.setTime(d);
		day = d.getDay();
		date = d.getDate();

		//System.out.println("Day is: " + day);
		//System.out.println("Date is: " + date);
		
		
		if (c.isLeapYear(currentYear)) {
			monthDays[1] = 29;
		} else
			monthDays[1] = 28;

		int temp = day - date % 7;
		if (temp > 0)
			day = temp + 1;
		else if (temp < 0)
			day = (temp + 1) + 7;
		else
			day = date % 7;
		
		day %= 7;
		countApptsInMonth(GetMonthAppts());
		////////////////////////////
		paintApptsInCal(day);
	}

	JMenuBar createMenuBar() {

		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("Manual Scheduling")) {
					AppScheduler defaultAppt = new AppScheduler("New", CalGrid.this);
					defaultAppt.updateSettingAppt(hkust.cse.calendar.gui.Utility
							.createDefaultAppt(currentYear, currentMonth, currentDay,
								mCurrUser));
					defaultAppt.setLocationRelativeTo(null);
					defaultAppt.show();
					TableModel t = prepareTableModel();
					tableView.setModel(t);
					tableView.repaint();
				}

			} 
		};
		JMenuBar menuBar = new JMenuBar();
		menuBar.getAccessibleContext().setAccessibleName("Calendar Choices");
		JMenuItem mi;

		JMenu Access = (JMenu) menuBar.add(new JMenu("Access"));
		Access.setMnemonic('A');
		Access.getAccessibleContext().setAccessibleDescription(
				"Account Access Management");

		mi = (JMenuItem) Access.add(new JMenuItem("Logout"));	//adding a Logout menu button for user to logout
		mi.setMnemonic('L');
		mi.getAccessibleContext().setAccessibleDescription("For user logout");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showConfirmDialog(null, "Logout?",
						"Confirm", JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION){
					//ApptController.getInstance().dumpStorageToFile();
					//System.out.println("closed");
					dispose();
					CalendarMain.logOut = true;
					return;	//return to CalendarMain()
				}
			}
		});
		
		mi = (JMenuItem) Access.add(new JMenuItem("Exit"));
		mi.setMnemonic('E');
		mi.getAccessibleContext().setAccessibleDescription("Exit Program");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showConfirmDialog(null, "Exit Program ?",
						"Confirm", JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION)
					System.exit(0);

			}
		});

		menuBar.add(Appmenu);
		Appmenu.setEnabled(false);
		Appmenu.setMnemonic('p');
		Appmenu.getAccessibleContext().setAccessibleDescription(
				"Appointment Management");
		mi = new JMenuItem("Manual Scheduling");
		mi.addActionListener(listener);
		Appmenu.add(mi);
		
		//Add manage location to CalGrid
		mi = new JMenuItem("Manage Locations");	
		mi.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent arg0) {
				//CalGrid grid = new CalGrid(new ApptStorageControllerImpl(new ApptStorageNullImpl(user)));
				//ApptController.getInstance().initApptStorage(new ApptStorageMemory(user));
				//CalGrid grid = new CalGrid();
				
				ManageLocationDialog dlg = new ManageLocationDialog();
				dlg.createAndShowManageLocationDialogGUI();
				//ManageLocationDialog.createAndShowManageLocationDialogGUI();
			}	
		});	
		Appmenu.add(mi);
		
		
		JMenu TimeMachine = (JMenu) menuBar.add(new JMenu("TimeMachine"));
		TimeMachine.setMnemonic('T');
		TimeMachine.getAccessibleContext().setAccessibleDescription(
				"Time Travel");
		mi = (JMenuItem) TimeMachine.add(new JMenuItem("Time Controller"));
		mi.setMnemonic('T');
		mi.getAccessibleContext().setAccessibleDescription("For Time Travel");	
		mi.addActionListener(listener);
		mi.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent arg0) {
				TimeMachineDialog TimeMachineTest = new TimeMachineDialog();
				TimeMachineTest.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		});
		
		return menuBar;
	}

	private void initializeSystem() {

		mCurrUser = ApptController.getInstance().getDefaultUser();	//get User from ApptController.getInstance().
		ApptController.getInstance().LoadApptFromXml();
		notificationCheckThread.start();
		// Fix Me !
		// Load the saved appointments from disk
		checkUpdateJoinAppt();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == monthRightArrowButton) {
			if (yearLabel == null)
				return;
			currentYear = currentYear + 1;
			yearLabel.setText(new Integer(currentYear).toString());
			CalGrid.this.setTitle("Desktop Calendar - No User - (" + currentYear
					+ "-" + currentMonth + "-" + currentDay + ")");
			getDateArray(data);
			if (tableView != null) {
				TableModel t = prepareTableModel();
				tableView.setModel(t);
				tableView.repaint();

			}
			UpdateCal();
		} else if (e.getSource() == monthLeftArrowButton) {
			if (yearLabel == null)
				return;
			currentYear = currentYear - 1;
			yearLabel.setText(new Integer(currentYear).toString());
			CalGrid.this.setTitle("Desktop Calendar - No User - (" + currentYear
					+ "-" + currentMonth + "-" + currentDay + ")");
			getDateArray(data);
			if (tableView != null) {
				TableModel t = prepareTableModel();
				tableView.setModel(t);
				tableView.repaint();
			}
			UpdateCal();
		} else if (e.getSource() == monthComboBox) {
			if (monthComboBox.getSelectedItem() != null) {
				currentMonth = monthComboBox.getSelectedIndex() + 1;
				try {
					styledDocImportantDays.remove(0, styledDocImportantDays.getLength());
					styledDocImportantDays.insertString(0, holidays[currentMonth - 1], sabImportantDays);
				} catch (BadLocationException e1) {

					e1.printStackTrace();
				}

				CalGrid.this.setTitle("Desktop Calendar - No User - ("
						+ currentYear + "-" + currentMonth + "-" + currentDay + ")");
				getDateArray(data);
				if (tableView != null) {
					TableModel t = prepareTableModel();
					tableView.setModel(t);
					tableView.repaint();
				}
				UpdateCal();
			}
		}
	}

	// update the appointment list on gui
	public void updateAppList() {
		applistPanel.clear();
		applistPanel.repaint();
		applistPanel.setTodayAppt(GetTodayAppt());
		applistPanel.updateTitleBorderForAppList(currentYear,currentMonth,currentDay);
	}

	public void UpdateCal() {
		if (mCurrUser != null) {
			mCurrTitle = "Desktop Calendar - " + mCurrUser.ID() + " - ";
			this.setTitle(mCurrTitle + "(" + currentYear + "-" + currentMonth + "-"
					+ currentDay + ")");
			
			getDateArray(data);
			TableModel t = prepareTableModel();
			this.tableView.setModel(t);
			this.tableView.repaint();
			
			
			updateAppList();
		}
	}

//	public void clear() {
//		for (int i = 0; i < 6; i++)
//			for (int j = 0; j < 7; j++)
//				apptMarker[i][j] = new Vector(10, 1);
//		TableModel t = prepareTableModel();
//		tableView.setModel(t);
//		tableView.repaint();
//		applist.clear();
//	}

	private List<Appt> GetMonthAppts() {
		Timestamp start = new Timestamp(0);
		start.setYear(currentYear);
		start.setMonth(currentMonth - 1);
		start.setDate(1);
		start.setHours(0);
		Timestamp end = new Timestamp(0);
		end.setYear(currentYear);
		end.setMonth(currentMonth - 1);
		end.setDate(TimeController.getInstance().numOfDaysInMonth());
		//System.out.println("end date: "+TimeController.getInstance().numOfDaysInMonth());
		end.setHours(23);
		TimeSpan period = new TimeSpan(start, end);
		return ApptController.getInstance().RetrieveApptsInList(mCurrUser, period);
	}
	

	private void mousePressResponse() {
		previousRow = tableView.getSelectedRow();
		previousCol = tableView.getSelectedColumn();
	}
	
	private void mouseResponse() {
		int[] selectedRows = tableView.getSelectedRows();
		int[] selectedCols = tableView.getSelectedColumns();
		if(tableView.getSelectedRow() == previousRow && tableView.getSelectedColumn() == previousCol){
			currentRow = selectedRows[selectedRows.length - 1];
			currentCol = selectedCols[selectedCols.length - 1];
		}
		else if(tableView.getSelectedRow() != previousRow && tableView.getSelectedColumn() == previousCol){
			currentRow = tableView.getSelectedRow();
			currentCol = selectedCols[selectedCols.length - 1];
		}
		else if(tableView.getSelectedRow() == previousRow && tableView.getSelectedColumn() != previousCol){
			currentRow = selectedRows[selectedRows.length - 1];
			currentCol = tableView.getSelectedColumn();
		}
		else{
			currentRow = tableView.getSelectedRow();
			currentCol = tableView.getSelectedColumn();
		}
		
		if (currentRow > 5 || currentRow < 0 || currentCol < 0
				|| currentCol > 6)
			return;

		
		
		if (tableView.getModel().getValueAt(currentRow, currentCol) != "") {
			try {
				currentDay = extract_only_date_string((String) tableView.getModel().getValueAt(currentRow, currentCol));
			} 
			catch (NumberFormatException n) {
				return;
			}
		}
		CalGrid.this.setTitle(mCurrTitle + "(" + currentYear + "-" + currentMonth
				+ "-" + currentDay + ")");
		
		updateAppList();
	}
	public int extract_only_date_string(String cellString) {
		String only_date_substring = cellString;
		int stringLength = only_date_substring.length();
		if(stringLength>2) {
			only_date_substring = only_date_substring.substring(stringLength-2, stringLength); 
			if(only_date_substring.substring(0,1).equals(" "))
				only_date_substring.substring(1,2);
		}
			return new Integer(only_date_substring).intValue();
	} 	
	
	public boolean IsTodayAppt(Appt appt) {
		if (appt.TimeSpan().StartTime().getYear() + 1900 != currentYear)
			return false;
		if ((appt.TimeSpan().StartTime().getMonth() + 1) != currentMonth)
			return false;
		if (appt.TimeSpan().StartTime().getDate() != currentDay)
			return false;
		return true;
	}

	public boolean IsMonthAppts(Appt appt) {

		if (appt.TimeSpan().StartTime().getYear() + 1900 != currentYear)
			return false;

		if ((appt.TimeSpan().StartTime().getMonth() + 1) != currentMonth)
			return false;
		return true;
	}

	public List<Appt> GetTodayAppt() {
		Integer temp;
		temp = new Integer(currentDay);
		Timestamp start = new Timestamp(0);
		start.setYear(currentYear);
		start.setMonth(currentMonth-1);
		start.setDate(currentDay);
		start.setHours(0);
		start.setMinutes(0);
		start.setSeconds(0);
		
		Timestamp end = new Timestamp(0);
		end.setYear(currentYear);
		end.setMonth(currentMonth-1);
		end.setDate(currentDay);
		end.setHours(23);
		end.setMinutes(59);
		end.setSeconds(59);
		
		TimeSpan period = new TimeSpan(start, end);
		return ApptController.getInstance().RetrieveApptsInList(mCurrUser, period);
	}

	public void countApptsInMonth(List<Appt> list) 
	{
		for(int i=0; i<31;i++)
			apptMarker[i]=0;
		
		int date;
		for(int i=0; i<list.size(); i++){
			date = list.get(i).getTimeSpan().StartTime().getDate();
			//System.out.println("apptdate: "+date);
			apptMarker[date-1]++;
		}
		//System.out.println("countApptsInMonth: ");
		//for(int i=0; i<apptMarker.length;i++)
		//	System.out.println(i+": "+apptMarker[i]);
	}
	
	public void paintApptsInCal(int today_day) {
		
		for (int i = 0; i < 6; i++){
			for (int j = 0; j < 7; j++) {
				int temp1 = i * 7 + j - today_day + 1;
				
				if (temp1 > 0 && temp1 <= monthDays[currentMonth - 1]) {
					String date_string = new Integer(temp1).toString();
					if(apptMarker[temp1-1]!=0)
						data[i][j] = "[[ "+apptMarker[temp1-1]+" ]]    "+date_string;
					else 
						data[i][j] = date_string;
				}
				else
					data[i][j] = new String("");
			}
		}
	}
	public AppList getAppListPanel() {
		return applistPanel;
	}

	public User getCurrUser() {
		return mCurrUser;
	}
	
	public int[] getCurrentDateFromCalGrid()
	{
		int[] tempDateArray = new int[3];
		tempDateArray[0] = currentYear;
		tempDateArray[1] = currentMonth;
		tempDateArray[2] = currentDay;
		
		return tempDateArray;
	}
	
	// check for any invite or update from join appointment
	public void checkUpdateJoinAppt(){
		// Fix Me!
	}

}
