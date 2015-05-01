package hkust.cse.calendar.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import hkust.cse.calendar.invite.InviteController;
import hkust.cse.calendar.time.TimeController;
import hkust.cse.calendar.unit.DeleteRequest;
import hkust.cse.calendar.unit.GroupAppt;
import hkust.cse.calendar.unit.ModifyNotification;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.userstorage.UserController;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ModifyUserPopUpDialog extends JFrame implements ActionListener{
	
	private ModifyNotification _request;
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
	
	public ModifyUserPopUpDialog(ModifyNotification notification, CalGrid parent){
		_request = notification;
		_currentUser = UserController.getInstance().getCurrentUser();
		_parent = parent;
		//initialize each components
		contentPane = getContentPane();
		topPanel = initializeTopPanel();
		bottomPanel = initializeBottomPanel();
		apptTimeLabel = initializeApptTimeLabel();
		pendingButton = initializePendingButton();
	
		//add components to corresponding panels
		topPanel.add(apptTimeLabel);
		//topPanel.add(notificationTimeLabel);
		//topPanel.add(organizerLabel);
		//topPanel.add(locationLabel);
		//bottomPanel.add(acceptButton);
		//bottomPanel.add(declineButton);
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
		final int ROW = 1;
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
				
		tempTimeLabel.setText("Admin modified Your Data, " + _request.getModifyUserID());
		
		return tempTimeLabel;
	}
	
	private JButton initializePendingButton(){
		JButton tempPendingButton = new JButton("Ok");
		tempPendingButton.addActionListener(this);
		return tempPendingButton;
	}
	
	public ModifyNotification getDeleteRequest()
	{
		return _request;
	}
	
	private void pendingUserDeleteRequest(){
		
		//do nothing.
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == pendingButton){
			pendingUserDeleteRequest();
			dispose();
		}
	}
	
}
