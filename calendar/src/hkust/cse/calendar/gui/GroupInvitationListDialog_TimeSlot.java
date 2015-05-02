package hkust.cse.calendar.gui;

import hkust.cse.calendar.time.TimeController;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.userstorage.UserController;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;


public class GroupInvitationListDialog_TimeSlot extends JFrame implements ActionListener {

	private JList leftUserBox;
	private JList rightUserBox;
	private JButton cancelButton;
	private JButton viewTimeSlotButton; 
	private JButton leftButton;
	private JButton rightButton;
	private DefaultListModel leftListModel;
	private DefaultListModel rightListModel;
	private List<User> userList;
	private JList displayList;
	
	private JLabel startyearLabel;
	private JTextField startyearField;
	private JLabel startmonthLabel;
	private JTextField startmonthField;
	private JLabel startdayLabel;
	private JTextField startdayField;
	
	private JLabel endyearLabel;
	private JTextField endyearField;
	private JLabel endmonthLabel;
	private JTextField endmonthField;
	private JLabel enddayLabel;
	private JTextField enddayField;
	
	private CalGrid parent;
	
	public GroupInvitationListDialog_TimeSlot(CalGrid cal){
		
		parent = cal;
		setTitle("Group Event Invitation Dialog");
		this.setAlwaysOnTop(true);
		
		Container contentPane;
		contentPane = getContentPane();
		contentPane.setSize(500, 1300);

		leftListModel = new DefaultListModel();
		// get user data from user controller currently temp data 
		userList = UserController.getInstance().getUserList();
		for(User a : userList) {
			if(!a.equals(UserController.getInstance().getCurrentUser()))
				leftListModel.addElement(a.getID());
		}
		//end
		JPanel top = new JPanel();
		top.setLayout(new BorderLayout());
		
		JPanel panelStartDate = initializeStartDatePanel();
		JPanel panelEndDate = initializeEndDatePanel();
		top.add("North", panelStartDate);
		top.add("South", panelEndDate);
		
		contentPane.add("North", top);
		
		JPanel center = new JPanel();
		center.setLayout(new FlowLayout());
		leftUserBox = new JList( leftListModel );
		leftUserBox.setFixedCellWidth(50);
		leftUserBox.setFixedCellHeight(20);
		
		JScrollPane leftUserBoxScrollPane = new JScrollPane(leftUserBox);
		center.add(leftUserBoxScrollPane);
		
		rightListModel = new DefaultListModel();
		rightUserBox = new JList( rightListModel );
		rightUserBox.setFixedCellWidth(50);
		rightUserBox.setFixedCellHeight(20);
		JScrollPane rightUserBoxScrollPane = new JScrollPane(rightUserBox);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		leftButton = new JButton("<<");
		leftButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		leftButton.addActionListener(this);
		leftButton.setEnabled(false);
			buttonPanel.add(leftButton);
			
		rightButton = new JButton(">>");
		rightButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		rightButton.addActionListener(this);
			buttonPanel.add(rightButton);
		
			center.add(buttonPanel);
			center.add(rightUserBoxScrollPane);
			
				contentPane.add("Center", center);
		//bottom part
		JPanel bottom = new JPanel();
		bottom.setLayout(new FlowLayout());
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		bottom.add(cancelButton);
		
		viewTimeSlotButton = new JButton("View Timeslot");
		viewTimeSlotButton.addActionListener(this);
		bottom.add(viewTimeSlotButton);
		
		contentPane.add("South", bottom);
		
		//visualization part
		pack();
		setLocationRelativeTo(null);
		setVisible(true);	
	}
	
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == leftButton)
		{
			if(rightUserBox.getSelectedValue() == null){
				return;
			}
			String selected = rightUserBox.getSelectedValue().toString();
			int selectedIndex = rightUserBox.getSelectedIndex();
			rightListModel.remove(selectedIndex);
			leftListModel.addElement(selected);
			checkButtonActivity();
			
		}else if(e.getSource() == rightButton){
			if(leftUserBox.getSelectedValue() == null){
				return;
			}
			String selected = leftUserBox.getSelectedValue().toString();
			int selectedIndex = leftUserBox.getSelectedIndex();
			leftListModel.remove(selectedIndex);
			rightListModel.addElement(selected);
			checkButtonActivity();
			
		}else if(e.getSource() == cancelButton){
			setVisible(false);
			dispose();
		}else if(e.getSource() == viewTimeSlotButton){
			//get data from rightUserBox and add it to userController
			if(rightListModel.getSize() <= 0){
				JOptionPane.showMessageDialog(this, "Please choose at least one user before sending the invitation",
						"Input Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			////need to implement view available timeslot.
	
			/*


			
			Timestamp timestampForStartTime = CreateTimeStamp(validDate1, validTimeInterval[0]);
			Timestamp timestampForEndTime = CreateTimeStamp(validDate2, validTimeInterval[1]);
			
			TimeSpan timeSpanForSlot = new TimeSpan(timestampForStartTime,timestampForEndTime);
			*/
			
			
			ArrayList<User> inviteeList = new ArrayList<User>();
			

			for(int i = 0; i<rightListModel.getSize();i++){
				inviteeList.add(UserController.getInstance().getUser(rightListModel.get(i).toString()));
			}
			
			ArrayList<Date> dateList = new ArrayList<Date>();
			
			Date d2 = new Date();
			Date d1 = new Date();
			
			int[] validDate1 = getValidDate(startyearField,startmonthField,startdayField);
			int[] validDate2 = getValidDate(endyearField,endmonthField,enddayField);
			int[] validTimeInterval = setTimeInterval();
			
			if(validDate1 == null || validDate2 == null || validTimeInterval == null)
			{
				System.out.println("not Valid date || TimeInterval");
				return;
			}
			
			//System.out.println("d2: "+ Utility.getNumber(endyearField.getText()) +" - "+ Utility.getNumber(endmonthField.getText())+" - "+Utility.getNumber(enddayField.getText()));
			d1 = TimeController.dateInputToDate(validDate1[0],validDate1[1], validDate1[2], 0,0,0);
			d2 = TimeController.dateInputToDate(validDate2[0],validDate2[1], validDate2[2], 0,0,0);
			if(d2.getTime()<=d1.getTime()){
				System.out.println("end date should be later than the start date");
				return;
			}
			dateList.add(d1);
			Date tempdate = new Date();
			for(int dayCount=1; dayCount<(d2.getTime()-d1.getTime())/(1000*60*60*24); dayCount++){
				tempdate = d1;
				tempdate.setDate(tempdate.getDate()+dayCount);
				dateList.add(tempdate);
			}
			dateList.add(d2);
			
			  //Display the window.
	    	AppScheduler groupApptScheduler = new AppScheduler("TimeSlot GroupAppt", parent, inviteeList, dateList);
			groupApptScheduler.setLocationRelativeTo(null);
			groupApptScheduler.show();
			
			JFrame frame = new AvailableTimeSlot_TextWindow(inviteeList, dateList);
			//frame.pack();
			frame.setSize(300, 500);
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(false);
			dispose();
		}
		
	}
	
	private int getTimeInput(int hour, int min) 
	{

		if (hour == -1)
			return -1;
		if (min == -1)
			return -1;

		return (hour * 60 + min);

	}
	private int[] setTimeInterval() {

		int[] result = new int[2];
		result[0] = getTimeInput(0, 0);
		result[1] = getTimeInput(24, 0);

		return result;
	}
	
	private int[] getValidDate(JTextField year, JTextField month, JTextField day) {

		int[] date = new int[3];
		date[0] = Utility.getNumber(year.getText());
		date[1] = Utility.getNumber(month.getText());

		if (date[0] < 1980 || date[0] > 2100) 
		{
			JOptionPane.showMessageDialog(this, "Please input proper year",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		if (date[1] <= 0 || date[1] > 12) 
		{
			JOptionPane.showMessageDialog(this, "Please input proper month",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		date[2] = Utility.getNumber(day.getText());
		int monthDay = CalGrid.monthDays[date[1] - 1];

		if (date[1] == 2) 
		{
			GregorianCalendar c = new GregorianCalendar();
			if (c.isLeapYear(date[0]))
				monthDay = 29;
		}

		if (date[2] <= 0 || date[2] > monthDay) 
		{
			JOptionPane.showMessageDialog(this,
					"Please input proper month day", "Input Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
		return date;
	}
	
	
	private Timestamp CreateTimeStamp(int[] date, int time) {
		Timestamp stamp = new Timestamp(0);
		TimeController.getInstance().setYear(stamp, date[0]);
		TimeController.getInstance().setMonth(stamp, date[1]);
		if(time/60 == 24)
		{
			TimeController.getInstance().setDate(stamp, date[2]+1);
			TimeController.getInstance().setHour(stamp, 0);
			TimeController.getInstance().setMinute(stamp, time % 60);
		}
		else
		{
			TimeController.getInstance().setDate(stamp,date[2]);
			TimeController.getInstance().setHour(stamp, time/60);
			TimeController.getInstance().setMinute(stamp, time % 60);
		}
		return stamp;
	}
	
	private void checkButtonActivity() {
		// TODO Auto-generated method stub
		if(rightListModel.isEmpty()){
			leftButton.setEnabled(false);
			rightButton.setEnabled(true);
		} else if(leftListModel.isEmpty()){
			rightButton.setEnabled(false);
			leftButton.setEnabled(true);
		} else{
			rightButton.setEnabled(true);
			leftButton.setEnabled(true);
		}
	}
	
	private JPanel initializeStartDatePanel()
	{
		JPanel panelDate = new JPanel();
		Border dateBorder = new TitledBorder(null, "START DATE");
		panelDate.setBorder(dateBorder);
		
		startyearLabel = new JLabel("YEAR: ");
		panelDate.add(startyearLabel);
		startyearField = new JTextField(6);
		panelDate.add(startyearField);

		startmonthLabel = new JLabel("MONTH: ");
		panelDate.add(startmonthLabel);
		startmonthField = new JTextField(4);
		panelDate.add(startmonthField);

		startdayLabel = new JLabel("DAY: ");
		panelDate.add(startdayLabel);
		startdayField = new JTextField(4);
		panelDate.add(startdayField);
		
		return panelDate;
	}
	
	private JPanel initializeEndDatePanel()
	{
		JPanel panelDate = new JPanel();
		Border dateBorder = new TitledBorder(null, "END DATE");
		panelDate.setBorder(dateBorder);
		
		endyearLabel = new JLabel("YEAR: ");
		panelDate.add(endyearLabel);
		endyearField = new JTextField(6);
		panelDate.add(endyearField);

		endmonthLabel = new JLabel("MONTH: ");
		panelDate.add(endmonthLabel);
		endmonthField = new JTextField(4);
		panelDate.add(endmonthField);

		enddayLabel = new JLabel("DAY: ");
		panelDate.add(enddayLabel);
		enddayField = new JTextField(4);
		panelDate.add(enddayField);
		
		return panelDate;
	}
}
