package hkust.cse.calendar.gui;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.Date;

import hkust.cse.calendar.invite.InviteController;
import hkust.cse.calendar.time.TimeController;
import hkust.cse.calendar.unit.GroupAppt;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.userstorage.UserController;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InvitationPopUpDialog extends JFrame implements ActionListener{
	
	private GroupAppt _groupAppt;
	private User _currentUser;
	private CalGrid _parent;
	final boolean ACCEPT = true;
	final boolean DECLINE = false;
	
	private Container contentPane;
	private JPanel topPanel;
	private JPanel bottomPanel;
	private JLabel apptTimeLabel;
	private JLabel notificationTimeLabel;
	private JLabel organizerLabel;
	private JLabel locationLabel;
	private JButton acceptButton;
	private JButton declineButton;
	private JButton pendingButton;
	
	public InvitationPopUpDialog(GroupAppt groupAppt, CalGrid parent){
		_groupAppt = groupAppt;
		_currentUser = UserController.getInstance().getCurrentUser();
		_parent = parent;
		//initialize each components
		contentPane = getContentPane();
		topPanel = initializeTopPanel();
		bottomPanel = initializeBottomPanel();
		apptTimeLabel = initializeApptTimeLabel();
		notificationTimeLabel = initializeNotificationLabel();
		organizerLabel = initializeOrganizerLabel();
		locationLabel = initializeLocationLabel();
		acceptButton = initializeAcceptButton();
		declineButton = initializeDeclineButton();
		pendingButton = initializePendingButton();
	
		//add components to corresponding panels
		topPanel.add(apptTimeLabel);
		topPanel.add(notificationTimeLabel);
		topPanel.add(organizerLabel);
		topPanel.add(locationLabel);
		bottomPanel.add(acceptButton);
		bottomPanel.add(declineButton);
		bottomPanel.add(pendingButton);
		contentPane.add("North",topPanel);
		contentPane.add("South",bottomPanel);
		
		setTitle("New Invitation!");
		setAlwaysOnTop(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private JPanel initializeTopPanel()
	{
		final int ROW = 4;
		final int COL = 1;
		JPanel tempTopPanel = new JPanel();
		tempTopPanel.setLayout(new GridLayout(ROW,COL));
		return tempTopPanel;
	}
	
	private JPanel initializeBottomPanel(){
		final int ROW = 1;
		final int COL = 3;
		JPanel tempBottomPanel = new JPanel();
		tempBottomPanel.setLayout(new GridLayout(ROW,COL));
		return tempBottomPanel;
	}
	
	private JLabel initializeApptTimeLabel(){
		
		JLabel tempTimeLabel = new JLabel(); 
		Timestamp startTimestamp = _groupAppt.getTimeSpan().StartTime();
		Timestamp endTimestamp = _groupAppt.getTimeSpan().EndTime();
		int startYear = TimeController.getInstance().getYearFrom(startTimestamp);
		int startMonth = TimeController.getInstance().getMonthFrom(startTimestamp);
		int startDate = TimeController.getInstance().getDateFrom(startTimestamp);
		int startHour = TimeController.getInstance().getHourFrom(startTimestamp);
		int startMinute = TimeController.getInstance().getMinuteFrom(startTimestamp);
		int endHour = TimeController.getInstance().getHourFrom(endTimestamp);
		int endMinute = TimeController.getInstance().getMinuteFrom(endTimestamp);
		
		String changedStartMinute;
		if(startMinute/10 < 1){
			changedStartMinute = "0"+startMinute;
		}
		else{
			changedStartMinute = ""+startMinute;
		}		
		
		String changedEndMinute;
		if(endMinute/10 < 1){
			changedEndMinute = "0"+endMinute;
		}
		else{
			changedEndMinute = ""+endMinute;
		}
		
		tempTimeLabel.setText("Appointment Time: " + startYear + "-" + startMonth + "-" + startDate + " "
				+ startHour + ":" + changedStartMinute + "-" + endHour + ":" + changedEndMinute);
		
		return tempTimeLabel;
	}
	
	private JLabel initializeOrganizerLabel(){
		JLabel tempOrganiserLabel = new JLabel();
		tempOrganiserLabel.setText("Organizer: "+ _groupAppt.getOwner());
		return tempOrganiserLabel;
	}
	
	private JLabel initializeLocationLabel(){	
		JLabel tempLocationLabel = new JLabel();
		tempLocationLabel.setText("Location: " + _groupAppt.getLocation().getName());
		return tempLocationLabel;
	}
	
	private JLabel initializeNotificationLabel(){
		JLabel tempNotificationLabel = new JLabel();
		if(_groupAppt.getNotification() != null){
			Date notificationTime = _groupAppt.getNotification().getNotificationTimeObj().getNotificationTime();
			int notiYear = TimeController.getInstance().getYearFrom(notificationTime);
			int notiMonth = TimeController.getInstance().getMonthFrom(notificationTime);
			int notiDate = TimeController.getInstance().getDateFrom(notificationTime);
			int notiHour = TimeController.getInstance().getHourFrom(notificationTime);
			int notiMinute = TimeController.getInstance().getMinuteFrom(notificationTime);
			String minute;
			if(notiMinute/10 < 1){
				minute = "0"+notiMinute;
			}
			else{
				minute = ""+notiMinute;
			}
			tempNotificationLabel.setText("Notification At: " + notiYear + "-" + notiMonth + "-" + notiDate 
						+ " "+ notiHour + ":" + minute);
		}
		else{
			tempNotificationLabel.setText("Notification At: -");
		}
		return tempNotificationLabel;
	}
	
	private JButton initializeAcceptButton(){
		JButton tempAcceptButton = new JButton("Accept");
		tempAcceptButton.addActionListener(this);
		return tempAcceptButton;
	}
	
	private JButton initializeDeclineButton(){
		JButton tempDeclineButton = new JButton("Decline");
		tempDeclineButton.addActionListener(this);
		return tempDeclineButton;
	}
	
	private JButton initializePendingButton(){
		JButton tempPendingButton = new JButton("Pending");
		tempPendingButton.addActionListener(this);
		return tempPendingButton;
	}
	
	public GroupAppt getGroupAppt()
	{
		return _groupAppt;
	}
	
	private void acceptGroupAppt(){
		InviteController.getInstance().setResponse(_currentUser, _groupAppt, ACCEPT);
	}
	
	private void declineGroupAppt(){
		InviteController.getInstance().setResponse(_currentUser, _groupAppt, DECLINE);
	}
	
	private void pendingGroupAppt(){
		//do nothing.
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == acceptButton){
			acceptGroupAppt();
			_parent.UpdateCal();
			dispose();
		}
		if(e.getSource() == declineButton){
			declineGroupAppt();
			dispose();
		}
		if(e.getSource() == pendingButton){
			pendingGroupAppt();
			dispose();
		}
	}
	
}
