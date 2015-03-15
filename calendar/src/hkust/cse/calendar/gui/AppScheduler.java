package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptController;
import hkust.cse.calendar.locationstorage.LocationController;
import hkust.cse.calendar.locationstorage.LocationStorage;
import hkust.cse.calendar.locationstorage.LocationStorageNullImpl;
import hkust.cse.calendar.notification.NotificationController;
import hkust.cse.calendar.notification.NotificationStorageNullImpl;
import hkust.cse.calendar.time.TimeController;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.Notification;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.user.UserController;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;


public class AppScheduler extends JDialog implements ActionListener,
		ComponentListener {

	private JLabel yearLabel;
	private JTextField yearField;
	private JLabel monthLabel;
	private JTextField monthField;
	private JLabel dayLabel;
	private JTextField dayField;
	private JLabel startTimeHourLabel;
	private JTextField startTimeHourField;
	private JLabel startTimeMinuteLabel;
	private JTextField startTimeMinuteField;
	private JLabel endTimeHourLabel;
	private JTextField endTimeHourField;
	private JLabel endTimeMinuteLabel;
	private JTextField endTimeMinuteField;

	private JLabel endAtYearLabel;
	private JTextField endAtYearField;
	private JLabel endAtMonthLabel;
	private JTextField endAtMonthField;
	private JLabel endAtDayLabel;
	private JTextField endAtDayField;

	private ButtonGroup freqButton;
	private JRadioButton oneTimeButton;
	private JRadioButton dailyButton;
	private JRadioButton weeklyButton;
	private JRadioButton monthlyButton;
	
	private JCheckBox oneHourCheckBox;
	private JCheckBox threeHourCheckBox;
	private JCheckBox twelveHourCheckBox;
	private JCheckBox twentyfourHourCheckBox;
	
	
	private DefaultListModel model;
	private JTextField titleField;

	private JButton saveBut;
	private JButton CancelBut;
	private JButton inviteBut;
	private JButton rejectBut;
	
	private Appt NewAppt;
	private CalGrid parent;
	private boolean isNew = true;
	private boolean isChanged = true;
	private boolean isJoint = false;

	private JTextArea detailArea;

	private JSplitPane panelApptDescription;
	private JPanel panelDetail;

	private JComboBox locationField;
	
	
//	private JTextField attendField;
//	private JTextField rejectField;
//	private JTextField waitingField;
	private int selectedApptId = -1;
	

	private void commonConstructor(String title, CalGrid cal) {
		
		parent = cal;
		this.setAlwaysOnTop(true);
		setTitle(title);
		setModal(false);

		Container contentPane;
		contentPane = getContentPane();
		
		//Initializing individual panels.
		JPanel panelTop = initiateTopPanel();
		JPanel panelDate = initiateDatePanel();
		JPanel panelStartTime = initiateStartTimePanel();
		JPanel panelEndTime = initiateEndTimePanel();
		JPanel panelBothTime = initiateBothTimePanel();
		JPanel panelDateAndTime = initiateDateAndTimePanel();
		JPanel panelFreq = initiateFreqPanel();
		JPanel panelNotification = initiateNotificationPanel();
		JPanel panelFreqAndNotification = initiateFreqAndNotificationPanel();
		JPanel panelFreqEndAt = initiateFreqEndAtPanel();
		JPanel panelEndAtFreqAndNotification = initiateEndAtFreqAndNotificationPanel();
		
		//Adding the panels to respective root panels.
		panelBothTime.add(panelStartTime);
		panelBothTime.add(panelEndTime);
		panelDateAndTime.add("North", panelDate);
		panelDateAndTime.add("South", panelBothTime);
		panelTop.add(panelDateAndTime, BorderLayout.NORTH);
		
		panelFreqAndNotification.add(panelFreq);
		panelFreqAndNotification.add(panelNotification);
		panelEndAtFreqAndNotification.add("North",panelFreqAndNotification);
		panelEndAtFreqAndNotification.add("South",panelFreqEndAt);
		panelTop.add(panelEndAtFreqAndNotification, BorderLayout.CENTER);
		
		JPanel panelTitleAndText = initiateTitleAndTextPanel();
		panelDetail = initiateDetailPanel();
		panelApptDescription = initiateApptDescriptionPanel(panelTitleAndText, panelDetail);

		panelTop.add(panelApptDescription, BorderLayout.SOUTH);

		contentPane.add("North", panelTop);
		
		if (NewAppt != null) {
			detailArea.setText(NewAppt.getInfo());

		}
		
		JPanel panelBottom = new JPanel();
		panelBottom.setLayout(new FlowLayout(FlowLayout.RIGHT));

//		inviteBut = new JButton("Invite");
//		inviteBut.addActionListener(this);
//		panelBottom.add(inviteBut);
		
		//initiate buttons.
		initiateSaveButton();
		initiateRejectButton();
		initiateCancelButton();
		
		//add them to the panel
		panelBottom.add(saveBut);
		panelBottom.add(rejectBut);
		rejectBut.show(false);
		panelBottom.add(CancelBut);
		contentPane.add("South", panelBottom);
		
		NewAppt = new Appt();

		if (this.getTitle().equals("Join Appointment Content Change") || this.getTitle().equals("Join Appointment Invitation")){
			inviteBut.show(false);
			rejectBut.show(true);
			CancelBut.setText("Consider Later");
			saveBut.setText("Accept");
		}
		if (this.getTitle().equals("Someone has responded to your Joint Appointment invitation") ){
			inviteBut.show(false);
			rejectBut.show(false);
			CancelBut.show(false);
			saveBut.setText("confirmed");
		}
		if (this.getTitle().equals("Join Appointment Invitation") || this.getTitle().equals("Someone has responded to your Joint Appointment invitation") || this.getTitle().equals("Join Appointment Content Change")){
			allDisableEdit();
		}
		pack();

	}
	
	
	public JPanel initiateDatePanel()
	{
		JPanel panelDate = new JPanel();
		Border dateBorder = new TitledBorder(null, "DATE");
		panelDate.setBorder(dateBorder);

		yearLabel = new JLabel("YEAR: ");
		panelDate.add(yearLabel);
		yearField = new JTextField(6);
		String defaultYearString = Integer.toString(TimeController.getInstance().getCurrentTimeInDate().getYear()+1900);
		yearField.setText(defaultYearString);
		panelDate.add(yearField);
		
		monthLabel = new JLabel("MONTH: ");
		panelDate.add(monthLabel);
		monthField = new JTextField(4);
		String defaultMonthString = Integer.toString(TimeController.getInstance().getCurrentTimeInDate().getMonth()+1);
		monthField.setText(defaultMonthString);
		panelDate.add(monthField);
		
		dayLabel = new JLabel("DAY: ");
		panelDate.add(dayLabel);
		dayField = new JTextField(4);
		String defaultDayString = Integer.toString(TimeController.getInstance().getCurrentTimeInDate().getDate());
		dayField.setText(defaultDayString);
		panelDate.add(dayField);
		
		return panelDate;
	}
	
	public JPanel initiateStartTimePanel()
	{
	
		JPanel panelStartTime = new JPanel();
		Border startTimeBorder = new TitledBorder(null, "START TIME");
		panelStartTime.setBorder(startTimeBorder);
		startTimeHourLabel = new JLabel("Hour");
		panelStartTime.add(startTimeHourLabel);
		startTimeHourField = new JTextField(4);
		panelStartTime.add(startTimeHourField);
		startTimeMinuteLabel = new JLabel("Minute");
		panelStartTime.add(startTimeMinuteLabel);
		startTimeMinuteField = new JTextField(4);
		panelStartTime.add(startTimeMinuteField);

		return panelStartTime;

	}
	
	public JPanel initiateEndTimePanel()
	{
		JPanel panelEndTime = new JPanel();
		Border etimeBorder = new TitledBorder(null, "END TIME");
		panelEndTime.setBorder(etimeBorder);
		endTimeHourLabel = new JLabel("Hour");
		panelEndTime.add(endTimeHourLabel);
		endTimeHourField = new JTextField(4);
		panelEndTime.add(endTimeHourField);
		endTimeMinuteLabel = new JLabel("Minute");
		panelEndTime.add(endTimeMinuteLabel);
		endTimeMinuteField = new JTextField(4);
		panelEndTime.add(endTimeMinuteField);
		
		return panelEndTime;
		
	}
	
	public JPanel initiateBothTimePanel()
	{
		final int ROWS = 1;
		final int COLUMNS = 2;
		JPanel panelBothTime = new JPanel();
		panelBothTime.setLayout(new GridLayout(ROWS,COLUMNS));
		return panelBothTime;
		
	}
	
	public JPanel initiateDateAndTimePanel()
	{
		JPanel panelDateAndTime = new JPanel();
		panelDateAndTime.setLayout(new BorderLayout());
		return panelDateAndTime;
	}
	
	public JPanel initiateFreqPanel()
	{
		JPanel panelFreq = new JPanel();
		Border freqBorder = new TitledBorder(null, "Frequency");
		panelFreq.setBorder(freqBorder);
		BoxLayout boxLayout = new BoxLayout(panelFreq, BoxLayout.Y_AXIS);
		panelFreq.setLayout(boxLayout);
		
		freqButton = new ButtonGroup();
		oneTimeButton = new JRadioButton("One-Time");
		dailyButton = new JRadioButton("Daily");
		weeklyButton = new JRadioButton("Weekly");
		monthlyButton = new JRadioButton("Monthly");
		freqButton.add(oneTimeButton);
		freqButton.add(dailyButton);
		freqButton.add(weeklyButton);
		freqButton.add(monthlyButton);
		oneTimeButton.setSelected(true);
		
		oneTimeButton.addActionListener(this);
		dailyButton.addActionListener(this);
		weeklyButton.addActionListener(this);
		monthlyButton.addActionListener(this);
		
		
		panelFreq.add(oneTimeButton);
		panelFreq.add(dailyButton);
		panelFreq.add(weeklyButton);
		panelFreq.add(monthlyButton);
		
		return panelFreq;
		
	}
	
	public JPanel initiateNotificationPanel()
	{
		JPanel panelNotification = new JPanel();
		Border notificationBorder = new TitledBorder(null, "Notification");
		panelNotification.setBorder(notificationBorder);
		BoxLayout boxLayout = new BoxLayout(panelNotification, BoxLayout.Y_AXIS);
		panelNotification.setLayout(boxLayout);
		
		oneHourCheckBox = new JCheckBox("1 hour");
		threeHourCheckBox = new JCheckBox("3 hours");
		twelveHourCheckBox = new JCheckBox("12 hours");
		twentyfourHourCheckBox = new JCheckBox("24 hours");
		
		panelNotification.add(oneHourCheckBox);
		panelNotification.add(threeHourCheckBox);
		panelNotification.add(twelveHourCheckBox);
		panelNotification.add(twentyfourHourCheckBox);
		
		return panelNotification;
		
	}
	
	public JPanel initiateFreqAndNotificationPanel()
	{
		final int ROWS = 1;
		final int COLUMNS = 2;
		JPanel panelFreqAndNotification = new JPanel();
		panelFreqAndNotification.setLayout(new GridLayout(ROWS,COLUMNS));
		
		return panelFreqAndNotification;
	}
	
	public JPanel initiateFreqEndAtPanel()
	{
		JPanel panelFreqEndAt = new JPanel();
		Border freqBorder = new TitledBorder(null, "Appointment Ends At");
		panelFreqEndAt.setBorder(freqBorder);

		endAtYearLabel = new JLabel("YEAR: ");
		panelFreqEndAt.add(endAtYearLabel);
		endAtYearField = new JTextField(6);
		panelFreqEndAt.add(endAtYearField);
		endAtMonthLabel = new JLabel("MONTH: ");
		panelFreqEndAt.add(endAtMonthLabel);
		endAtMonthField = new JTextField(4);
		panelFreqEndAt.add(endAtMonthField);
		endAtDayLabel = new JLabel("DAY: ");
		panelFreqEndAt.add(endAtDayLabel);
		endAtDayField = new JTextField(4);
		panelFreqEndAt.add(endAtDayField);
		
		endAtYearField.setEditable(false);
		endAtMonthField.setEditable(false);
		endAtDayField.setEditable(false);
		
		
		
		return panelFreqEndAt;
	}
	
	public JPanel initiateEndAtFreqAndNotificationPanel()
	{
	
		JPanel panelEndAtFreqAndNotification = new JPanel();
		panelEndAtFreqAndNotification.setLayout(new BorderLayout());
		
		return panelEndAtFreqAndNotification;
	}
	
	public JPanel initiateTopPanel()
	{
		JPanel panelTop = new JPanel();
		panelTop.setLayout(new BorderLayout());
		panelTop.setBorder(new BevelBorder(BevelBorder.RAISED));
		
		return panelTop;
	}
	
	public JPanel initiateTitleAndTextPanel()
	{
		JPanel panelTitleAndText = new JPanel();
		JLabel titleL = new JLabel("TITLE");
		titleField = new JTextField(15);
		panelTitleAndText.add(titleL);
		panelTitleAndText.add(titleField);
		
		//GUI for location list.
		//test for location combobox.

		LocationController.getInstance().initLocationStorage(new LocationStorageNullImpl(UserController.getInstance().getDefaultUser()));
		ArrayList<Location> locationList = new ArrayList<Location>();
		for(int i=0; i<LocationController.getInstance().getLocationList().getSize(); i++){
			locationList.add(LocationController.getInstance().getLocationList().getElementAt(i));
		}

		String[] locationStringArray = new String[locationList.size()];
		int i = 0;
		for(Location l: locationList)
		{
			locationStringArray[i] = l.getName();
			i++;
		}
		
		JLabel locationLabel = new JLabel("LOCATION");
		
		//need to change the parameter in the constructor
		//to load the list of locations.
		locationField = new JComboBox(locationStringArray);
		panelTitleAndText.add(locationLabel);
		panelTitleAndText.add(locationField);
		
		return panelTitleAndText;
	}
	
	public JPanel initiateDetailPanel()
	{
		JPanel tempPanelDetail = new JPanel();
		tempPanelDetail.setLayout(new BorderLayout());
		Border detailBorder = new TitledBorder(null, "Appointment Description");
		tempPanelDetail.setBorder(detailBorder);
		detailArea = new JTextArea(15, 30);

		detailArea.setEditable(true);
		JScrollPane detailScroll = new JScrollPane(detailArea);
		tempPanelDetail.add(detailScroll);
		
		return tempPanelDetail;
	}
	
	public JSplitPane initiateApptDescriptionPanel(JPanel panelTitleAndText, JPanel tempPanelDetail)
	{
		JSplitPane tempPanelApptDescription = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelTitleAndText,
				tempPanelDetail);
		
		return tempPanelApptDescription;
		
	}
	
	public void initiateSaveButton()
	{
		saveBut = new JButton("Save");
		saveBut.addActionListener(this);
		
	}
	
	public void initiateRejectButton()
	{
		rejectBut = new JButton("Reject");
		rejectBut.addActionListener(this);
		
	}
	
	public void initiateCancelButton()
	{
		CancelBut = new JButton("Cancel");
		CancelBut.addActionListener(this);
	}
	
	//Constructor	
	AppScheduler(String title, CalGrid cal, int selectedApptId) {
		this.selectedApptId = selectedApptId;
		commonConstructor(title, cal);
	}

	
	//Constructor
	AppScheduler(String title, CalGrid cal) {
		commonConstructor(title, cal);
	}
	
	
	public void actionPerformed(ActionEvent e) {

		// distinguish which button is clicked and continue with require function
		if (e.getSource() == CancelBut) 
		{

			setVisible(false);
			dispose();
		}
		else if (e.getSource() == saveBut) 
		{
			saveButtonResponse();

		}
		else if (e.getSource() == rejectBut)
		{
			if (JOptionPane.showConfirmDialog(this, "Reject this joint appointment?", "Confirmation", JOptionPane.YES_NO_OPTION) == 0)
			{
				NewAppt.addReject(getCurrentUser());
				NewAppt.getAttendList().remove(getCurrentUser());
				NewAppt.getWaitingList().remove(getCurrentUser());
				this.setVisible(false);
				dispose();
			}
		}
		else if(e.getSource() == oneTimeButton)
		{
			endAtYearField.setEditable(false);
			endAtMonthField.setEditable(false);
			endAtDayField.setEditable(false);
			
		}
		else if(e.getSource() == dailyButton)
		{
			endAtYearField.setEditable(true);
			endAtMonthField.setEditable(true);
			endAtDayField.setEditable(true);
			
		}
		else if(e.getSource() == weeklyButton)
		{
			endAtYearField.setEditable(true);
			endAtMonthField.setEditable(true);
			endAtDayField.setEditable(true);
			
		}
		else if(e.getSource() == monthlyButton)
		{
			endAtYearField.setEditable(true);
			endAtMonthField.setEditable(true);
			endAtDayField.setEditable(true);
			
		}
		
		parent.getAppListPanel().clear();
		parent.getAppListPanel().setTodayAppt(parent.GetTodayAppt());
		parent.repaint();
	}

	private JPanel createPartOperaPane() {
		JPanel POperaPane = new JPanel();
		JPanel browsePane = new JPanel();
		JPanel controPane = new JPanel();

		POperaPane.setLayout(new BorderLayout());
		TitledBorder titledBorder1 = new TitledBorder(BorderFactory
				.createEtchedBorder(Color.white, new Color(178, 178, 178)),
				"Add Participant:");
		browsePane.setBorder(titledBorder1);

		POperaPane.add(controPane, BorderLayout.SOUTH);
		POperaPane.add(browsePane, BorderLayout.CENTER);
		POperaPane.setBorder(new BevelBorder(BevelBorder.LOWERED));
		return POperaPane;

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

	
	//helper function
	private int getTime(JTextField h, JTextField min) 
	{

		int hour = Utility.getNumber(h.getText());
		if (hour == -1)
			return -1;
		int minute = Utility.getNumber(min.getText());
		if (minute == -1)
			return -1;

		return (hour * 60 + minute);

	}

	private int[] getValidTimeInterval() {

		int[] result = new int[2];
		result[0] = getTime(startTimeHourField, startTimeMinuteField);
		result[1] = getTime(endTimeHourField, endTimeMinuteField);
		if ((result[0] % 15) != 0 || (result[1] % 15) != 0) {
			JOptionPane.showMessageDialog(this,
					"Minute Must be 0, 15, 30, or 45 !", "Input Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		if (!startTimeMinuteField.getText().equals("0") && !startTimeMinuteField.getText().equals("15") && !startTimeMinuteField.getText().equals("30") && !startTimeMinuteField.getText().equals("45") 
			|| !endTimeMinuteField.getText().equals("0") && !endTimeMinuteField.getText().equals("15") && !endTimeMinuteField.getText().equals("30") && !endTimeMinuteField.getText().equals("45")){
			JOptionPane.showMessageDialog(this,
					"Minute Must be 0, 15, 30, or 45 !", "Input Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		if (result[1] == -1 || result[0] == -1) {
			JOptionPane.showMessageDialog(this, "Please check time",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if (result[1] <= result[0]) {
			JOptionPane.showMessageDialog(this,
					"End time should be bigger than \nstart time",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if ((result[0] < AppList.OFFSET * 60)
				|| (result[1] > (AppList.OFFSET * 60 + AppList.ROWNUM * 2 * 15))) {
			JOptionPane.showMessageDialog(this, "Out of Appointment Range !",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		return result;
	}

	//This method executed when the save button is clicked 
	//Must check for valid input
	//If the input is valid, save the responses.
	private void saveButtonResponse() {
		
		//SAVE ALL THE RELEVENT INFORMATION TO THE NEWAPPT.
		int[] validDate = getValidDate(yearField,monthField,dayField);
		int[] validTimeInterval = getValidTimeInterval();
		
		if(validDate == null || validTimeInterval == null)
		{
			return;
		}
		
		Timestamp timestampForStartTime = CreateTimeStamp(validDate, validTimeInterval[0]);
		Timestamp timestampForEndTime = CreateTimeStamp(validDate, validTimeInterval[1]);
		TimeSpan timeSpanForAppt = new TimeSpan(timestampForStartTime,timestampForEndTime);
		NewAppt.setTimeSpan(timeSpanForAppt);
		NewAppt.setTitle(titleField.getText());
		NewAppt.setInfo(detailArea.getText());

		
		//SAVE LOCATION
		String locationString = (String) locationField.getSelectedItem();
		
		//Temporary Location for Testing
		Location locationObject = LocationController.getInstance().RetrieveLocations(locationString);
		NewAppt.setLocation(locationObject);
		
		//SAVE REPEATED APPOINTMENT
		//CASE: DAILY, WEEKLY, MONTHLY
		if(!(oneTimeButton.isSelected()))
		{
			int[] validEndAtDate = getValidDate(endAtYearField, endAtMonthField, endAtDayField);
			
			Date endAtDate = intArrayToDate(validEndAtDate);
			
			if(saveFrequencyWithEndAt(endAtDate))
			{
				//SAVE NOTIFICATION
				if(checkForNotification())
				{
					saveResponseFromNotification();
				}
				JOptionPane.showMessageDialog(this, "Saved appointment successfully!");
				dispose();
			}
			else
			{
				JOptionPane.showMessageDialog(this, "Failed to save appointment!");
			}
			
		}
		//CASE: ONE-TIME
		else
		{
			if(saveFrequencyWithoutEndAt())
			{
				//SAVE NOTIFICATION
				if(checkForNotification())
				{
					saveResponseFromNotification();
				}
				JOptionPane.showMessageDialog(this, "Saved appointment successfully");
				dispose();
			}
			else
			{
				JOptionPane.showMessageDialog(this, "Failed to save appointment!");
			}
		}
		
	}

	private Date intArrayToDate(int[] intArray)
	{
		Date temp = new Date();
		temp.setYear(intArray[0]);
		temp.setMonth(intArray[1]);
		temp.setDate(intArray[2]);
		
		return temp;
	}
	
	private void saveResponseFromNotification()
	{
				
		boolean flagOne = false;
		boolean flagTwo = false;
		boolean flagThree = false;
		boolean flagFour = false;
		
		if(oneHourCheckBox.isSelected())
		{
			flagOne = true;
		}
		
		if(threeHourCheckBox.isSelected())
		{
			flagTwo = true;
		}
		
		if(twelveHourCheckBox.isSelected())
		{
			flagThree = true;
		}
		
		if(twentyfourHourCheckBox.isSelected())
		{
			flagFour = true;
		}
		
		
		if(ApptController.getInstance().setNotificationForAppt(NewAppt, flagOne, flagTwo, flagThree, flagFour))
		{
			JOptionPane.showMessageDialog(this, "Saved notification successfully");
		}
		else
		{
			JOptionPane.showMessageDialog(this, "Failed to save notification");
		}
	}
	
	
	private boolean checkForNotification()
	{
		if(oneHourCheckBox.isSelected() || threeHourCheckBox.isSelected() || 
				twelveHourCheckBox.isSelected() || twentyfourHourCheckBox.isSelected())
		{
			return true;
		}
		
		return false;
	}
	
	private boolean saveFrequencyWithEndAt(Date endAtDate)
	{
		
		 UserController tempUserController = UserController.getInstance();
		 ApptController tempApptController = ApptController.getInstance();
		 
		 if(dailyButton.isSelected())
		 {
			 return tempApptController.saveRepeatedNewAppt(tempUserController.getDefaultUser(),NewAppt,1, endAtDate);
		 }
		 else if(weeklyButton.isSelected())
		 {
			 return tempApptController.saveRepeatedNewAppt(tempUserController.getDefaultUser(), NewAppt,2, endAtDate);
		 }
		 else if(monthlyButton.isSelected())
		 {
			 return tempApptController.saveRepeatedNewAppt(tempUserController.getDefaultUser(), NewAppt,3, endAtDate);
		 }
		 
		 return false;
	}
	
	private boolean saveFrequencyWithoutEndAt()
	{
		return ApptController.getInstance().saveNewAppt(UserController.getInstance().getDefaultUser(), NewAppt);
	}
	
	
	
	//This method can be used in conjunction with getValidDate() and getValidTimeInterval()
	//to create a Timestamp object.
	private Timestamp CreateTimeStamp(int[] date, int time) {
		Timestamp stamp = new Timestamp(0);
		stamp.setYear(date[0]);
		stamp.setMonth(date[1] - 1);
		stamp.setDate(date[2]);
		stamp.setHours(time / 60);
		stamp.setMinutes(time % 60);
		return stamp;
	}

	public void updateSettingAppt(Appt appt) {
		// Fix Me!
	}

	public void componentHidden(ComponentEvent e) {

	}

	public void componentMoved(ComponentEvent e) {

	}

	public void componentResized(ComponentEvent e) {

		Dimension dm = panelApptDescription.getSize();
		double width = dm.width * 0.93;
		double height = dm.getHeight() * 0.6;
		panelDetail.setSize((int) width, (int) height);

	}

	public void componentShown(ComponentEvent e) {

	}
	
	public String getCurrentUser()		// get the id of the current user
	{
		return this.parent.mCurrUser.ID();
	}
	
	private void allDisableEdit(){
		yearField.setEditable(false);
		monthField.setEditable(false);
		dayField.setEditable(false);
		startTimeHourField.setEditable(false);
		startTimeMinuteField.setEditable(false);
		endTimeHourField.setEditable(false);
		endTimeMinuteField.setEditable(false);
		titleField.setEditable(false);
		detailArea.setEditable(false);
	}
}
