package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptController;
import hkust.cse.calendar.locationstorage.LocationController;
import hkust.cse.calendar.locationstorage.LocationStorageMemory;
import hkust.cse.calendar.time.TimeController;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.userstorage.UserController;

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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.BorderFactory;
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

	private JCheckBox notificationEnableBox;
	private JTextField notificationHourField;
	private JTextField notificationMinuteField;

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
	private JButton groupEventButton;

	private Appt currentAppt;
	private CalGrid parent;
	private boolean isNew = true;
	private boolean isChanged = true;
	private boolean isJoint = false;

	private JTextArea detailArea;

	private JSplitPane panelApptDescription;
	private JPanel panelDetail;

	private JComboBox locationField;
	private String[] locationStringArray;

	//	private JTextField attendField;
	//	private JTextField rejectField;
	//	private JTextField waitingField;
	private boolean isModifying = false;
	private final String MODIFY = "Modify";
	
	private void commonConstructor(String title, CalGrid cal) {

		parent = cal;
		this.setAlwaysOnTop(true);
		setTitle(title);
		setModal(false);

		if (title.equals(MODIFY)) 
			isModifying = true;

		Container contentPane;
		contentPane = getContentPane();

		//Initializing individual panels.
		JPanel panelTop = initializeTopPanel();
		JPanel panelDate = initializeDatePanel();
		JPanel panelStartTime = initializeStartTimePanel();
		JPanel panelEndTime = initializeEndTimePanel();
		JPanel panelBothTime = initializeBothTimePanel();
		JPanel panelDateAndTime = initializeDateAndTimePanel();
		JPanel panelFreq = initializeFreqPanel();
		JPanel panelNotification = initializeNotificationPanel();
		JPanel panelFreqAndNotification = initiatializeFreqAndNotificationPanel();
		JPanel panelFreqEndAt = initializeFreqEndAtPanel();
		JPanel panelEndAtFreqAndNotification = initializeEndAtFreqAndNotificationPanel();

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
		JPanel panelTitleAndText = initializeTitleAndTextPanel();
		panelDetail = initializeDetailPanel();
		panelApptDescription = initializeApptDescriptionPanel(panelTitleAndText, panelDetail);
		panelTop.add(panelApptDescription, BorderLayout.SOUTH);
		contentPane.add("North", panelTop);

		//currentAppt = new Appt();

		JPanel panelBottom = new JPanel();
		panelBottom.setLayout(new FlowLayout(FlowLayout.RIGHT));

		//initiate buttons.
		initiateSaveButton();
		initiateRejectButton();
		initiateCancelButton();
		initiateGroupEventButton();
		
		//add them to the panel
		panelBottom.add(groupEventButton);
		panelBottom.add(saveBut);
		panelBottom.add(rejectBut);
		rejectBut.show(false);
		panelBottom.add(CancelBut);
		contentPane.add("South", panelBottom);

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


	public JPanel initializeDatePanel()
	{
		JPanel panelDate = new JPanel();
		Border dateBorder = new TitledBorder(null, "DATE");
		panelDate.setBorder(dateBorder);

		yearLabel = new JLabel("YEAR: ");
		panelDate.add(yearLabel);
		yearField = new JTextField(6);
		panelDate.add(yearField);

		monthLabel = new JLabel("MONTH: ");
		panelDate.add(monthLabel);
		monthField = new JTextField(4);
		panelDate.add(monthField);

		dayLabel = new JLabel("DAY: ");
		panelDate.add(dayLabel);
		dayField = new JTextField(4);
		panelDate.add(dayField);

		return panelDate;
	}

	/*public void initializeDefaultDateSetting()
	{
		String defaultYearString = Integer.toString(TimeController.getInstance().getCurrentTimeInDate().getYear()+1900);
		yearField.setText(defaultYearString);

		String defaultMonthString = Integer.toString(TimeController.getInstance().getCurrentTimeInDate().getMonth()+1);
		monthField.setText(defaultMonthString);

		String defaultDayString = Integer.toString(TimeController.getInstance().getCurrentTimeInDate().getDate());
		dayField.setText(defaultDayString);

	}*/

	public JPanel initializeStartTimePanel()
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

	public JPanel initializeEndTimePanel()
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

	public JPanel initializeBothTimePanel()
	{
		final int ROWS = 1;
		final int COLUMNS = 2;
		JPanel panelBothTime = new JPanel();
		panelBothTime.setLayout(new GridLayout(ROWS,COLUMNS));
		return panelBothTime;

	}

	public JPanel initializeDateAndTimePanel()
	{
		JPanel panelDateAndTime = new JPanel();
		panelDateAndTime.setLayout(new BorderLayout());
		return panelDateAndTime;
	}

	public JPanel initializeFreqPanel()
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

	public JPanel initializeNotificationPanel()
	{
		JPanel panelNotification = new JPanel();
		Border notificationBorder = new TitledBorder(null, "Notification");
		panelNotification.setBorder(notificationBorder);
		panelNotification.setLayout(new BorderLayout());
		
		JPanel notificationhHourPanel = new JPanel();
		notificationhHourPanel.setLayout(new FlowLayout());
		JLabel hourLabel = new JLabel("Hour(s)");
		notificationHourField = new JTextField(4);
		notificationHourField.setEditable(false);
		notificationhHourPanel.add(notificationHourField);
		notificationhHourPanel.add(hourLabel);
		
		JPanel notificationMinutePanel = new JPanel();
		notificationMinutePanel.setLayout(new FlowLayout());
		JLabel minuteLabel = new JLabel("Minutes");
		notificationMinuteField = new JTextField(4);
		notificationMinuteField.setEditable(false);
		notificationMinutePanel.add(notificationMinuteField);
		notificationMinutePanel.add(minuteLabel);
		
		
		JPanel descriptionPanel = new JPanel();
		descriptionPanel.setLayout(new FlowLayout());
		notificationEnableBox = new JCheckBox("Turn on/off notification");
		descriptionPanel.add(notificationEnableBox);
		notificationEnableBox.addActionListener(this);
		
		panelNotification.add("North", descriptionPanel);
		panelNotification.add("Center", notificationhHourPanel);
		panelNotification.add("South", notificationMinutePanel);
		
		
		/*oneHourCheckBox = new JCheckBox("1 hour");
		threeHourCheckBox = new JCheckBox("3 hours");
		twelveHourCheckBox = new JCheckBox("12 hours");
		twentyfourHourCheckBox = new JCheckBox("24 hours");

		panelNotification.add(oneHourCheckBox);
		panelNotification.add(threeHourCheckBox);
		panelNotification.add(twelveHourCheckBox);
		panelNotification.add(twentyfourHourCheckBox);
		 */
		return panelNotification;

	}

	public JPanel initiatializeFreqAndNotificationPanel()
	{
		final int ROWS = 1;
		final int COLUMNS = 2;
		JPanel panelFreqAndNotification = new JPanel();
		panelFreqAndNotification.setLayout(new GridLayout(ROWS,COLUMNS));

		return panelFreqAndNotification;
	}

	public JPanel initializeFreqEndAtPanel()
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

	public JPanel initializeEndAtFreqAndNotificationPanel()
	{

		JPanel panelEndAtFreqAndNotification = new JPanel();
		panelEndAtFreqAndNotification.setLayout(new BorderLayout());

		return panelEndAtFreqAndNotification;
	}

	public JPanel initializeTopPanel()
	{
		JPanel panelTop = new JPanel();
		panelTop.setLayout(new BorderLayout());
		panelTop.setBorder(new BevelBorder(BevelBorder.RAISED));

		return panelTop;
	}

	public JPanel initializeTitleAndTextPanel()
	{
		JPanel panelTitleAndText = new JPanel();
		JLabel titleL = new JLabel("TITLE");
		titleField = new JTextField(15);
		panelTitleAndText.add(titleL);
		panelTitleAndText.add(titleField);

		//GUI for location list.
		//test for location combobox.

		LocationController.getInstance().initLocationStorage(new LocationStorageMemory(UserController.getInstance().getAdmin()));
		ArrayList<Location> locationList = new ArrayList<Location>();
		for(int i=0; i<LocationController.getInstance().getLocationList().getSize(); i++){
			locationList.add(LocationController.getInstance().getLocationList().getElementAt(i));
		}

		locationStringArray = new String[locationList.size()];
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

	public JPanel initializeDetailPanel()
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

	public JSplitPane initializeApptDescriptionPanel(JPanel panelTitleAndText, JPanel tempPanelDetail)
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
	private void initiateGroupEventButton() {
		groupEventButton = new JButton("Save as Group Event");
		groupEventButton.addActionListener(this);
	}

	//Constructor	
	AppScheduler(String title, CalGrid cal, Appt appt) {
		this.currentAppt = appt;
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
		else if (e.getSource() == groupEventButton) 
		{
			groupEventButtonResponse();

		}
		else if(e.getSource() == oneTimeButton)
		{
			enableEndAtFields(false);
			enableGroupEventButton(true);
			
		}
		else if(e.getSource() == dailyButton || e.getSource() == weeklyButton || e.getSource() == monthlyButton)
		{
			enableEndAtFields(true);
			enableGroupEventButton(false);
		}
		else if(e.getSource() == notificationEnableBox)
		{
			modifyNotificationField();
		}
		parent.getAppListPanel().clear();
		parent.getAppListPanel().setTodayAppt(parent.GetTodayAppt());
		parent.repaint();
	}
	
	private void groupEventButtonResponse() {
		
		if(saveInfoToAppt() == false){//when the input for the appt is invalid.
			return;
		}
		if(checkGroupEventConditions()){
			GroupInvitationDialog groupInvitationDialog = new GroupInvitationDialog(currentAppt,this);
		}
	}
	
	private boolean checkGroupEventConditions(){
		if(!oneTimeButton.isSelected()){//check if event is repeated.
			JOptionPane.showMessageDialog(this, "Group Event can only be ONE-TIME!",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if(currentAppt.getLocation().getName().equals("-")){
			JOptionPane.showMessageDialog(this, "Group Event must have a location!",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	private void modifyNotificationField()
	{
		if(notificationEnableBox.isSelected())
		{
			notificationHourField.setEditable(true);
			notificationMinuteField.setEditable(true);
		}
		else
		{
			notificationHourField.setEditable(false);
			notificationMinuteField.setEditable(false);
		}
	}
	
	
	private void enableEndAtFields(boolean b){
		endAtYearField.setEditable(b);
		endAtMonthField.setEditable(b);
		endAtDayField.setEditable(b);
	}
	
	private void enableGroupEventButton(boolean b){
		groupEventButton.setEnabled(b);
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
	private int getTimeInput(JTextField h, JTextField min) 
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
		result[0] = getTimeInput(startTimeHourField, startTimeMinuteField);
		result[1] = getTimeInput(endTimeHourField, endTimeMinuteField);
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
		if(saveInfoToAppt() == false){
			return;
		}
		
		//SAVE REPEATED APPOINTMENT
		//CASE: DAILY, WEEKLY, MONTHLY
		if(!(oneTimeButton.isSelected()))
		{
		
			int[] validEndAtDate = getValidDate(endAtYearField, endAtMonthField, endAtDayField);
			Date endAtDate;
			
			if(validEndAtDate != null)
			{
				endAtDate = intArrayToDate(validEndAtDate);
		
				if(saveApptWithEndAt(endAtDate))
				{
					//SAVE NOTIFICATION
					JOptionPane.showMessageDialog(this, "Saved appointment successfully!");				
					dispose();
				}
				else
				{
					JOptionPane.showMessageDialog(this, "Failed to save appointment!");
				}
			}
		}
		//CASE: ONE-TIME
		else
		{
			if (saveApptWithoutEndAt()){
				JOptionPane.showMessageDialog(this, "Saved appointment successfully");
				dispose();
			}
			else{
				JOptionPane.showMessageDialog(this, "Failed to save appointment!");
			}
		}
	}
	
	/*
	private void saveResponseFromNotification()
	{
		boolean flagOne = false;
		boolean flagTwo = false;
		boolean flagThree = false;
		boolean flagFour = false;
		
		if(oneHourCheckBox.isSelected())
		{
			flagOne= true;
		}
		if(threeHourCheckBox.isSelected())
		{
			flagTwo= true;
		}
		if(twelveHourCheckBox.isSelected())
		{
			flagThree= true;
		}
		if(twentyfourHourCheckBox.isSelected())
		{
			flagFour= true;
		}
		
		
		if(ApptController.getInstance().setNotificationForAppt(currentAppt, flagOne, flagTwo, flagThree, flagFour))
		{
			JOptionPane.showMessageDialog(this, "Saved notification successfully");
		}
		else
		{
			JOptionPane.showMessageDialog(this, "Failed to save notification");
		}
	}*/

	private boolean saveInfoToAppt(){
		int[] validDate = getValidDate(yearField,monthField,dayField);
		int[] validTimeInterval = getValidTimeInterval();
		boolean validNotificationTime = checkValidNotificationTime();
		
		if(validDate == null || validTimeInterval == null || validNotificationTime == false)
		{
			return false;
		}
		
		Timestamp timestampForStartTime = CreateTimeStamp(validDate, validTimeInterval[0]);
		Timestamp timestampForEndTime = CreateTimeStamp(validDate, validTimeInterval[1]);
		
		TimeSpan timeSpanForAppt = new TimeSpan(timestampForStartTime,timestampForEndTime);
		
		if(!TimeController.getInstance().isNotPast(timeSpanForAppt)){
			JOptionPane.showMessageDialog(this, "Input date is past date! ");
			return false;
		}
		
		currentAppt.setTimeSpan(timeSpanForAppt);
		currentAppt.setTitle(titleField.getText());
		currentAppt.setInfo(detailArea.getText());


		//SAVE LOCATION
		String locationString = (String) locationField.getSelectedItem();
		Location locationObject = LocationController.getInstance().RetrieveLocations(locationString);
		if(currentAppt.getLocation()!=null && !(currentAppt.getLocation().getName().equals(locationObject.getName()))){
			currentAppt.getLocation().decreaseCountForLocation();
		}
		currentAppt.setLocation(locationObject);

		return true;
	}
	
	private boolean checkForNotification()
	{
		if(notificationEnableBox.isSelected())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private boolean checkValidNotificationTime()
	{
		if(notificationEnableBox.isSelected())
		{
			int notificationHour = Utility.getNumber(notificationHourField.getText());
			int notificationMinute = Utility.getNumber(notificationMinuteField.getText());
			if(notificationHour < 0 || notificationHour > 24)
			{
				JOptionPane.showMessageDialog(this, "Notification hour range from 0-24!", "Input Error",JOptionPane.ERROR_MESSAGE);
				return false;
			}
			if(notificationMinute < 0 || notificationMinute > 59)
			{
				JOptionPane.showMessageDialog(this, "Notification minute range from 0-59!", "Input Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			return true;
		}
		else
		{
			return true;
		}
	}
	
	
	private boolean saveApptWithEndAt(Date endAtDate)
	{
		System.out.println("Current Appt Time: " + currentAppt.getTimeSpan().StartTime().getTime());
		
		if((!(TimeController.getInstance().isNotPast(endAtDate))) 
				|| (currentAppt.getTimeSpan().StartTime().getTime() > endAtDate.getTime())){
			return false;
		}
		
		int notificationHoursBefore = Utility.getNumber(notificationHourField.getText());
		int notificationMinutesBefore = Utility.getNumber(notificationMinuteField.getText());
		
		int repeatType;
		if(dailyButton.isSelected())
			repeatType = ApptController.DAILY;
		else if(weeklyButton.isSelected())
			repeatType = ApptController.WEEKLY;
		else if(monthlyButton.isSelected())
			repeatType = ApptController.MONTHLY;
		else
			return false;

		currentAppt.setRepeatType(repeatType);
		
		if (checkForNotification()){
			if (isModifying){
				return ApptController.getInstance().modifyRepeatedNewAppt(UserController.getInstance().getAdmin(), currentAppt, endAtDate,
						checkForNotification(),notificationHoursBefore,notificationMinutesBefore);
			}
			return ApptController.getInstance().saveRepeatedNewAppt(UserController.getInstance().getAdmin(), currentAppt, endAtDate,
					checkForNotification(),notificationHoursBefore,notificationMinutesBefore);	
		}
		if (isModifying){
			return ApptController.getInstance().modifyRepeatedNewAppt(UserController.getInstance().getAdmin(), currentAppt, endAtDate,
					checkForNotification(),notificationHoursBefore,notificationMinutesBefore);
		}
		return ApptController.getInstance().saveRepeatedNewAppt(UserController.getInstance().getAdmin(), currentAppt, endAtDate);	
	}

	private boolean saveApptWithoutEndAt()
	{

		int notificationHoursBefore = Utility.getNumber(notificationHourField.getText());
		int notificationMinutesBefore = Utility.getNumber(notificationMinuteField.getText());
		
		if (checkForNotification()){
			
			if (isModifying){
				return ApptController.getInstance().modifyAppt(UserController.getInstance().getAdmin(), currentAppt,
						checkForNotification(),notificationHoursBefore,notificationMinutesBefore);
			}
			return ApptController.getInstance().saveNewAppt(UserController.getInstance().getAdmin(), currentAppt,
					checkForNotification(),notificationHoursBefore,notificationMinutesBefore);
		}
		if (isModifying){
			return ApptController.getInstance().modifyAppt(UserController.getInstance().getAdmin(), currentAppt,
					checkForNotification(),notificationHoursBefore,notificationMinutesBefore);
		}	
		return ApptController.getInstance().saveNewAppt(UserController.getInstance().getAdmin(), currentAppt);
	}



	//This method can be used in conjunction with getValidDate() and getValidTimeInterval()
	//to create a Timestamp object.
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

	
	//OPTIMIZED FOR DATE OBJECT
	private Date intArrayToDate(int[] intArray)
	{
		Date temp = new Date();
		TimeController.getInstance().setYear(temp, intArray[0]);
		TimeController.getInstance().setMonth(temp,intArray[1]);
		TimeController.getInstance().setDate(temp, intArray[2]);
		TimeController.getInstance().setHour(temp, 23);
		TimeController.getInstance().setMinute(temp, 59);
		return temp;
	}
	
	public void updateSettingAppt(Appt appt) {
		currentAppt = appt;
		Timestamp startTimestamp = currentAppt.getTimeSpan().StartTime();
		Timestamp endTimestamp = currentAppt.getTimeSpan().EndTime(); 
		//Load data on date and time field.
		yearField.setText(Integer.toString(TimeController.getInstance().getYearFrom(startTimestamp)));
		monthField.setText(Integer.toString(TimeController.getInstance().getMonthFrom(startTimestamp)));
		dayField.setText(Integer.toString(TimeController.getInstance().getDateFrom(startTimestamp)));
		startTimeHourField.setText(Integer.toString(TimeController.getInstance().getHourFrom(startTimestamp)));
		startTimeMinuteField.setText(Integer.toString(TimeController.getInstance().getMinuteFrom(startTimestamp)));
		endTimeHourField.setText(Integer.toString(TimeController.getInstance().getHourFrom(endTimestamp)));
		endTimeMinuteField.setText(Integer.toString(TimeController.getInstance().getMinuteFrom(endTimestamp)));

		//Load data on location.
		if(currentAppt.getLocation() != null)
		{	
			String currentLocation = currentAppt.getLocation().getName();
			String locationStringItem = getStringItemFromLocationStringArray(currentLocation);
			if(locationStringItem != null)
				locationField.setSelectedItem(locationStringItem);
		}

		//If Current Appt is Repeated
		if (currentAppt.isRepeated()){
			Timestamp endAtTime = currentAppt.getRepeatedEndDate();
			endAtYearField.setText(""+(TimeController.getInstance().getYearFrom(endAtTime)));
			endAtMonthField.setText(""+(TimeController.getInstance().getMonthFrom(endAtTime)));
			endAtDayField.setText(""+(TimeController.getInstance().getDateFrom(endAtTime)));

			enableEndAtFields(true);
			switch(currentAppt.getRepeatType()){
				case ApptController.DAILY:
					dailyButton.setSelected(true);
					break;
				case ApptController.WEEKLY:
					weeklyButton.setSelected(true);
					break;
				case ApptController.MONTHLY:
					monthlyButton.setSelected(true);
					break;
				default:
					enableEndAtFields(false);
			}

		}
		
		//Load data on notification.
		if(currentAppt.getNotification() != null)
		{
			notificationEnableBox.setSelected(true);
			notificationHourField.setText(Integer.toString(currentAppt.getNotification().getHoursBefore()));
			notificationMinuteField.setText(Integer.toString(currentAppt.getNotification().getMinutesBefore()));
		}
		
		titleField.setText(currentAppt.getTitle());
		detailArea.setText(currentAppt.getInfo());	
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

	public void setAppt(Appt appt)
	{
		currentAppt = appt;
	}

	public String getStringItemFromLocationStringArray(String targetString)
	{
		for(int i=0; i<locationStringArray.length; i++)
		{
			if(locationStringArray[i].compareTo(targetString) == 0)
			{
				return locationStringArray[i];
			}
		}
		return null;
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
