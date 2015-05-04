package hkust.cse.calendar.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import hkust.cse.calendar.Main.CalendarMain;
import hkust.cse.calendar.invite.InviteController;
import hkust.cse.calendar.time.TimeController;
import hkust.cse.calendar.unit.DeleteRequest;
import hkust.cse.calendar.unit.GroupAppt;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.userstorage.UserController;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DeleteUserPopUpDialog extends JFrame implements ActionListener{
	
	private DeleteRequest _request;
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
	
	public DeleteUserPopUpDialog(DeleteRequest deleteRequest, CalGrid parent){
		_request = deleteRequest;
		_currentUser = UserController.getInstance().getCurrentUser();
		_parent = parent;
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
		//topPanel.add(notificationTimeLabel);
		topPanel.add(organizerLabel);
		topPanel.add(locationLabel);
		bottomPanel.add(acceptButton);
		bottomPanel.add(declineButton);
		bottomPanel.add(pendingButton);
		contentPane.add("North",topPanel);
		contentPane.add("South",bottomPanel);
		
		setTitle("New Delete User Request!");
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
				
		tempTimeLabel.setText("Admin requested to Delete " + _request.getDeleteUser());
		
		return tempTimeLabel;
	}
	
	private JLabel initializeOrganizerLabel(){
		JLabel tempRequestUserLabel = new JLabel();
		tempRequestUserLabel.setText("Authenticatable user: "+ _request.getRequestUser());
		return tempRequestUserLabel;
	}
	
	private JLabel initializeLocationLabel(){	
		JLabel tempDeletedUserLabel = new JLabel();
		tempDeletedUserLabel.setText("Deleted User: " + _request.getDeleteUser());
		return tempDeletedUserLabel;
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
	
	public DeleteRequest getDeleteRequest()
	{
		return _request;
	}
	
	private void acceptUserDeleteRequest(){
		//System.out.println("Current User: "+ _currentUser);
		//System.out.println("Get Delted User: "+ _request.getDeleteUser());
		UserController.getInstance().respondToDeleteRequest(true, _request);

		
		if(_currentUser.getID().equals(_request.getDeleteUser().toString())) {
			dispose();
			CalendarMain.logOut = true;
			_parent.dispose();
			LoginDialog loginDialog = new LoginDialog();
		}
		return;
	}
	
	private void declineUserDeleteRequest(){
		UserController.getInstance().respondToDeleteRequest(false, _request);
	}
	
	private void pendingUserDeleteRequest(){
		//do nothing.
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == acceptButton){
			acceptUserDeleteRequest();
			_parent.UpdateCal();
			dispose();
		}
		if(e.getSource() == declineButton){
			declineUserDeleteRequest();
			dispose();
		}
		if(e.getSource() == pendingButton){
			pendingUserDeleteRequest();
			dispose();
		}
	}
	
}
