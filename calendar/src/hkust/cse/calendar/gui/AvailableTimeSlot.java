package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptController;
import hkust.cse.calendar.time.TimeController;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.TimeSpan;
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


class TimeSlotCellRenderer extends DefaultTableCellRenderer 
{
	private int r;
	private int c;



	public TimeSlotCellRenderer(Object value, boolean availability, int row, int col,
			int colorCMD) 
	{

		Font f1 = new Font("Helvetica", Font.ITALIC, 11);
		setForeground(Color.black);
		
		if (availability) { //available
			setBackground(Color.green);

		}
		else{ //unavailable
			setBackground(Color.red);
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

public class AvailableTimeSlot extends JPanel implements ActionListener 
{
	private int startYear;
	private int startMonth;
	private int startDay;
	
	private int endYear;
	private int endMonth;
	private int endDay;
	
	public static int SMALLEST_DURATION = 15;
	private static final long serialVersionUID = 1L;
	public static int OFFSET = 0;
	public static int ROWNUM = 48;
	
	private String[] names;

	private JTable tableView;
	//private final Object[][] apptContentData = new Object[ROWNUM][6];
	//private int currentRow;
	//private int currentCol;
	//private int pressRow;
	//private int pressCol;
	//private int releaseRow;
	//private int releaseCol;
	
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

	public void updateTitleBorderForAppList()
	{
		appListTitle.setTitle("Available Timeslot");
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





	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == tableView) 
		{
			pop.show(tableView, currentRow * 20, currentRow * 20);

		}

	}

}
