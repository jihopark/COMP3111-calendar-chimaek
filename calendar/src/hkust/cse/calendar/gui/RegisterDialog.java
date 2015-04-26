package hkust.cse.calendar.gui;

import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.userstorage.UserController;
import hkust.cse.calendar.userstorage.UserStorageMemory;
import hkust.cse.calendar.apptstorage.ApptController;
import hkust.cse.calendar.apptstorage.ApptStorageMemory;
import hkust.cse.calendar.locationstorage.LocationController;
import hkust.cse.calendar.tests.GUITest;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.*;

public class RegisterDialog extends JFrame implements ActionListener {
	
	private JTextField userName;
	private JTextField firstName;
	private JTextField lastName;
	private JPasswordField firstPassword;
	private JPasswordField secondPassword;
	private JTextField emailAddress;
	private JButton backButton;
	private JButton registerButton;
	private JCheckBox adminCheckBox;
	
	public RegisterDialog() {
		setTitle("Register");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				LoginDialog loginDialog = new LoginDialog();
				setVisible(false);
				dispose();
			}
		});
		Container contentPane;
		contentPane = getContentPane();
		
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
		
		JPanel messagePanel = new JPanel();
		messagePanel.add(new JLabel("Register"));
		top.add(messagePanel);
		
		JPanel userNamePanel = new JPanel();
		userNamePanel.add(new JLabel("       User Name:       "));
		userName = new JTextField(15);
		userNamePanel.add(userName);
		top.add(userNamePanel);
		
		JPanel firstNamePanel = new JPanel();
		firstNamePanel.add(new JLabel("      First Name:        "));
		firstName = new JTextField(15);
		firstNamePanel.add(firstName);
		top.add(firstNamePanel);
		
		JPanel lastNamePanel = new JPanel();
		lastNamePanel.add(new JLabel("      Last Name:        "));
		lastName = new JTextField(15);
		lastNamePanel.add(lastName);
		top.add(lastNamePanel);
		
		JPanel firstPasswordPanel = new JPanel();
		firstPasswordPanel.add(new JLabel("      Password:         "));
		firstPassword = new JPasswordField(15);
		firstPasswordPanel.add(firstPassword);
		top.add(firstPasswordPanel);
		
		JPanel secondPasswordPanel = new JPanel();
		secondPasswordPanel.add(new JLabel("Password Check:  "));
		secondPassword = new JPasswordField(15);
		secondPasswordPanel.add(secondPassword);
		top.add(secondPasswordPanel);
		
		JPanel emailPanel = new JPanel();
		emailPanel.add(new JLabel("   Email Address:     "));
		emailAddress = new JTextField(15);
		emailPanel.add(emailAddress);
		top.add(emailPanel);
		
		contentPane.add("North", top);
		
		JPanel bottom = new JPanel();
		bottom.setLayout(new FlowLayout());
		
		backButton = new JButton("Back to Login");
		backButton.addActionListener(this);
		bottom.add(backButton);
		
		registerButton = new JButton("Register");
		registerButton.addActionListener(this);
		bottom.add(registerButton);
		
		adminCheckBox = new JCheckBox("Admin Register");
		bottom.add(adminCheckBox);
		
		
		contentPane.add("South", bottom);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);	
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == backButton)
		{
			LoginDialog loginDialog = new LoginDialog();
			setVisible(false);
			dispose();
		} else if(e.getSource() == registerButton)
		{
			//register after checking for input errors
			if(checkForWrongUserName(userName.getText()) || checkForWrongPassword(firstPassword.getText(), secondPassword.getText())) {
				System.out.println("RegisterDialog/actionPerformed: Error inside username or Password");
				JOptionPane.showMessageDialog(this, "Please Check the input data for validation",
						"Input Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			System.out.println("RegisterDialog/actionPerformed: No Error and called UserController");
			// call user controller and close
			UserController.getInstance().initUserStorage(new UserStorageMemory());
			if(adminCheckBox.isSelected()){
				System.out.println("RegisterDialog/actionPerformed: Admin Checkbox is Checked and User is Saved");
				if(UserController.getInstance().saveUser(userName.getText(), firstPassword.getText(), firstName.getText(), lastName.getText(), emailAddress.getText(), true)) {				
					JOptionPane.showMessageDialog(this, "Admin Registration Complete. Please Login using your credentials",
							"Register Success", JOptionPane.PLAIN_MESSAGE);
					LoginDialog loginDialog = new LoginDialog();
					setVisible(false);
					dispose();
				} else {
					System.out.println("RegisterDialog/actionPerformed: UserController calls error. Data can not be saved");
				}
			} else {
				if(UserController.getInstance().saveUser(userName.getText(), firstPassword.getText(), firstName.getText(), lastName.getText(), emailAddress.getText(), false)) {
					JOptionPane.showMessageDialog(this, "Normal Registration Complete. Please Login using your credentials",
							"Register Success", JOptionPane.PLAIN_MESSAGE);
					LoginDialog loginDialog = new LoginDialog();
					setVisible(false);
					dispose();
				} else {
					System.out.println("RegisterDialog/actionPerformed: UserController calls error. Data can not be saved");
				}
			}
		}
	}

	private boolean checkForWrongUserName(String userName) {
		// TODO Auto-generated method stub

		if(userName.isEmpty() || checkUserName(userName)){
			return true;
		}
		return false;
	}

	private boolean checkUserName(String text) {
		boolean check = false;
		if(text != null && !text.isEmpty()) {
			for(char c : text.toCharArray()) {
				if(check = Character.isDigit(c)){
					System.out.println("RegisterDialog/checkUserName: userName contains Digits at: " + c);
					break;
				}
				if(check = Character.isUpperCase(c)){
					System.out.println("RegisterDialog/checkUserName: userName contains an Uppercase at at: " + c);
					break;
				}if(check = Character.isSpaceChar(c)){
					System.out.println("RegisterDialog/checkUserName: userName contains a Space");
					break;
				}if(check = !Character.isLetter(c)){
					System.out.println("RegisterDialog/checkUserName: userName is not a letter at: " + c);
					break;
				}
				
			}
		}
		return check;
	}

	private boolean checkForWrongPassword(String firstPassword, String secondPassword) {
		if(firstPassword.isEmpty() || secondPassword.isEmpty()){
			return true;
		}
		if(firstPassword.equals(secondPassword)) {
			return false;
		} else {
			System.out.println("RegisterDialog/checkForWrongPassword: two passwords are not equal: " + firstPassword + " compared with " + secondPassword);
			return true;
		}
	}


}
