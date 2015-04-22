package hkust.cse.calendar.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;

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
	final boolean ACCEPT = true;
	final boolean DECLINE = false;
	
	private Container contentPane;
	private JPanel topPanel;
	private JPanel bottomPanel;
	private JLabel apptTimeLabel;
	private JLabel organizerLabel;
	private JLabel locationLabel;
	private JButton acceptButton;
	private JButton declineButton;
	private JButton pendingButton;
	
	public InvitationPopUpDialog(GroupAppt groupAppt){
		_groupAppt = groupAppt;
		_currentUser = UserController.getInstance().getCurrentUser();
		
		//initialize each components
		contentPane = getContentPane();
		topPanel = initializeTopPanel();
		bottomPanel = initializeBottomPanel();
		apptTimeLabel = initializeApptTimeLabel();
		organizerLabel = initializeOrganizerLabel();
		locationLabel = initializeLocationLabel();
		acceptButton = initializeAcceptButton();
		declineButton = initializeDeclineButton();
		pendingButton = initializePendingButton();
	
		//add components to corresponding panels
		topPanel.add(apptTimeLabel);
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
		final int ROW = 3;
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
		
		tempTimeLabel.setText("Appointment Time: " + startYear + "-" + startMonth + "-" + startDate + " "
				+ startHour + ":" + startMinute + "-" + endHour + ":" + endMinute);
		
		return tempTimeLabel;
	}
	
	private JLabel initializeOrganizerLabel(){
		JLabel tempOrganiserLabel = new JLabel();
		tempOrganiserLabel.setText("Organizer: "+ _groupAppt.getOwner());
		return tempOrganiserLabel;
	}
	
	private JLabel initializeLocationLabel(){	
		JLabel tempLocationLabel = new JLabel();
		tempLocationLabel.setText("Location: " ); //+ _groupAppt.getLocation().getName());
		return tempLocationLabel;
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
