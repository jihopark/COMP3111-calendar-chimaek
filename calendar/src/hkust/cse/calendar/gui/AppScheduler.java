package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptController;
import hkust.cse.calendar.locationstorage.LocationController;
import hkust.cse.calendar.locationstorage.LocationStorage;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.TimeSpan;

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
		JPanel panelReminder = initiateReminderPanel();
		JPanel panelFreqAndReminder = initiateFreqAndReminderPanel();
		JPanel panelFreqEndAt = initiateFreqEndAtPanel();
		JPanel panelEndAtFreqAndReminder = initiateEndAtFreqAndReminderPanel();
		
		//Adding the panels to respective parent panels.
		panelBothTime.add(panelStartTime);
		panelBothTime.add(panelEndTime);
		panelDateAndTime.add("North", panelDate);
		panelDateAndTime.add("South", panelBothTime);
		panelTop.add(panelDateAndTime, BorderLayout.NORTH);
		
		panelFreqAndReminder.add(panelFreq);
		panelFreqAndReminder.add(panelReminder);
		panelEndAtFreqAndReminder.add("North",panelFreqAndReminder);
		panelEndAtFreqAndReminder.add("South",panelFreqEndAt);
		panelTop.add(panelEndAtFreqAndReminder, BorderLayout.CENTER);
		
		JPanel panelTitleAndText = initiateTitleAndTextPanel();
		panelDetail = initiateDetailPanel();
		panelApptDescription = initiateApptDescriptionPanel(panelTitleAndText, panelDetail);

		panelTop.add(panelApptDescription, BorderLayout.SOUTH);

		contentPane.add("North", panelTop);
		
		//NOT FOR PHASE 1
		//THIS IS FOR INVITATION PURPOSE
		if (NewAppt != null) {
			detailArea.setText(NewAppt.getInfo());

		}
		
		JPanel panelBottom = new JPanel();
		panelBottom.setLayout(new FlowLayout(FlowLayout.RIGHT));

//		inviteBut = new JButton("Invite");
//		inviteBut.addActionListener(this);
//		panelBottom.add(inviteBut);
		
		//initiate buttons.
		saveBut = initiateSaveButton();
		rejectBut = initiateRejectButton();
		CancelBut = initiateCancelButton();
		
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
		
		ButtonGroup freqButton = new ButtonGroup();
		JRadioButton oneTimeButton = new JRadioButton("One-Time");
		JRadioButton dailyButton = new JRadioButton("Daily");
		JRadioButton weeklyButton = new JRadioButton("Weekly");
		JRadioButton monthlyButton = new JRadioButton("Monthly");
		freqButton.add(oneTimeButton);
		freqButton.add(dailyButton);
		freqButton.add(weeklyButton);
		freqButton.add(monthlyButton);
		oneTimeButton.setSelected(true);
		
		panelFreq.add(oneTimeButton);
		panelFreq.add(dailyButton);
		panelFreq.add(weeklyButton);
		panelFreq.add(monthlyButton);
		
		return panelFreq;
		
	}
	
	public JPanel initiateReminderPanel()
	{
		JPanel panelReminder = new JPanel();
		Border reminderBorder = new TitledBorder(null, "Reminder");
		panelReminder.setBorder(reminderBorder);
		BoxLayout boxLayout = new BoxLayout(panelReminder, BoxLayout.Y_AXIS);
		panelReminder.setLayout(boxLayout);
		panelReminder.add(new JCheckBox("1 hour"));
		panelReminder.add(new JCheckBox("3 hours"));
		panelReminder.add(new JCheckBox("12 hours"));
		panelReminder.add(new JCheckBox("24 hours"));
		
		return panelReminder;
		
	}
	
	public JPanel initiateFreqAndReminderPanel()
	{
		final int ROWS = 1;
		final int COLUMNS = 2;
		JPanel panelFreqAndReminder = new JPanel();
		panelFreqAndReminder.setLayout(new GridLayout(ROWS,COLUMNS));
		
		return panelFreqAndReminder;
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
		
		return panelFreqEndAt;
	}
	
	public JPanel initiateEndAtFreqAndReminderPanel()
	{
	
		JPanel panelEndAtFreqAndReminder = new JPanel();
		panelEndAtFreqAndReminder.setLayout(new BorderLayout());
		
		return panelEndAtFreqAndReminder;
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
		Location[] locations = {};
		
		JLabel locationLabel = new JLabel("LOCATION");
		
		//need to change the parameter in the constructor
		//to load the list of locations.
		locationField = new JComboBox(locations);
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
		detailArea = new JTextArea(20, 30);

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
	
	public JButton initiateSaveButton()
	{
		JButton tempSaveButton = new JButton("Save");
		tempSaveButton.addActionListener(this);
		return tempSaveButton;
	}
	
	public JButton initiateRejectButton()
	{
		JButton tempRejButton = new JButton("Reject");
		tempRejButton.addActionListener(this);
		return tempRejButton;
		
	}
	
	public JButton initiateCancelButton()
	{
		JButton tempCloseButton = new JButton("Cancel");
		tempCloseButton.addActionListener(this);
		return tempCloseButton;
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
		if (e.getSource() == CancelBut) {

			setVisible(false);
			dispose();
		} else if (e.getSource() == saveBut) {
			saveButtonResponse();

		} else if (e.getSource() == rejectBut){
			if (JOptionPane.showConfirmDialog(this, "Reject this joint appointment?", "Confirmation", JOptionPane.YES_NO_OPTION) == 0){
				NewAppt.addReject(getCurrentUser());
				NewAppt.getAttendList().remove(getCurrentUser());
				NewAppt.getWaitingList().remove(getCurrentUser());
				this.setVisible(false);
				dispose();
			}
		}
		parent.getAppList().clear();
		parent.getAppList().setTodayAppt(parent.GetTodayAppt());
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

	private int[] getValidDate() {

		int[] date = new int[3];
		date[0] = Utility.getNumber(yearField.getText());
		date[1] = Utility.getNumber(monthField.getText());
		if (date[0] < 1980 || date[0] > 2100) {
			JOptionPane.showMessageDialog(this, "Please input proper year",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if (date[1] <= 0 || date[1] > 12) {
			JOptionPane.showMessageDialog(this, "Please input proper month",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		date[2] = Utility.getNumber(dayField.getText());
		int monthDay = CalGrid.monthDays[date[1] - 1];
		if (date[1] == 2) {
			GregorianCalendar c = new GregorianCalendar();
			if (c.isLeapYear(date[0]))
				monthDay = 29;
		}
		if (date[2] <= 0 || date[2] > monthDay) {
			JOptionPane.showMessageDialog(this,
			"Please input proper month day", "Input Error",
			JOptionPane.ERROR_MESSAGE);
			return null;
		}
		return date;
	}

	
	//helper function
	private int getTime(JTextField h, JTextField min) {

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

	public void updateSetApp(Appt appt) {
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
