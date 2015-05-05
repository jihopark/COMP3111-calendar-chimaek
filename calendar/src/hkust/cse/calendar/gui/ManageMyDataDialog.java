package hkust.cse.calendar.gui;

import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.userstorage.UserController;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ManageMyDataDialog extends JFrame implements ActionListener {

	private JButton cancelButton;
	private JButton okButton;
	private JPanel topPanel;
	private JPanel bottomPanel;
	
	private JLabel userName;
	private JLabel firstName;
	private JLabel lastName;
	private JLabel firstPassword;
	private JLabel secondPassword;
	private JLabel emailAddress;
	
	private JLabel userNameBox;
	private JTextField firstNameBox;
	private JTextField lastNameBox;
	private JPasswordField firstPasswordBox;
	private JPasswordField secondPasswordBox;
	private JTextField emailAddressBox;
	private User user;
	
	public ManageMyDataDialog(){
		setTitle("Manage My Information");
		this.setAlwaysOnTop(true);
		
		Container contentPane;
		contentPane = getContentPane();
		user = UserController.getInstance().getCurrentUser();
		
		//top part
		topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(6,2));

		userName = new JLabel("Username : ",JLabel.RIGHT);
		firstName = new JLabel("First Name: ",JLabel.RIGHT);
		lastName = new JLabel("Last Name: ",JLabel.RIGHT);
		firstPassword = new JLabel("First Password : ",JLabel.RIGHT);
		secondPassword = new JLabel("Second Password : ",JLabel.RIGHT);
		emailAddress = new JLabel("E-mail Address : ",JLabel.RIGHT);
		userName.setAlignmentX(Component.RIGHT_ALIGNMENT);
		firstName.setAlignmentX(Component.RIGHT_ALIGNMENT);
		lastName.setAlignmentX(Component.RIGHT_ALIGNMENT);
		firstPassword.setAlignmentX(Component.RIGHT_ALIGNMENT);
		secondPassword.setAlignmentX(Component.RIGHT_ALIGNMENT);
		emailAddress.setAlignmentX(Component.RIGHT_ALIGNMENT);
		userNameBox = new JLabel(" " + user.getID());
		firstNameBox = new JTextField(15);
		firstNameBox.setText(user.getFirstName());
		lastNameBox = new JTextField(15);
		lastNameBox.setText(user.getLastName());
		firstPasswordBox = new JPasswordField(15);
		secondPasswordBox = new JPasswordField(15);
		emailAddressBox = new JTextField(15);
		emailAddressBox.setText(user.getEmailAddress());
		userNameBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		firstNameBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		lastNameBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		firstPasswordBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		secondPasswordBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		emailAddressBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		
		//add components
		topPanel.add(userName);
		topPanel.add(userNameBox);
		topPanel.add(firstName);
		topPanel.add(firstNameBox);
		topPanel.add(lastName);
		topPanel.add(lastNameBox);
		topPanel.add(firstPassword);
		topPanel.add(firstPasswordBox);
		topPanel.add(secondPassword);
		topPanel.add(secondPasswordBox);
		topPanel.add(emailAddress);
		topPanel.add(emailAddressBox);
		
		contentPane.add("North", topPanel);
				
		//bottom part
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout());
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		bottomPanel.add(cancelButton);
		
		okButton = new JButton("Modify Data");
		okButton.addActionListener(this);
		bottomPanel.add(okButton);
		contentPane.add("South", bottomPanel);
		
		//visualization part
		pack();
		setLocationRelativeTo(null);
		setVisible(true);	
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == okButton){
			//check valid user data
			//1. two password checks
			//2. no null
			if(checkForWrongPassword(firstPasswordBox.getText(), secondPasswordBox.getText())){
				JOptionPane.showMessageDialog(this, "Two Passwords Needs to be Equal to modify Data",
						"Modify Failed", JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "Modified Successed! Thank you!",
						"Modify Successed", JOptionPane.PLAIN_MESSAGE);
				User newDataUser = UserController.getInstance().getCurrentUser();
				newDataUser.Password(firstPasswordBox.getText());
				newDataUser.setEmailAddress(emailAddressBox.getText());
				newDataUser.setFirstName(firstNameBox.getText());
				newDataUser.setLastName(lastNameBox.getText());
				UserController.getInstance().modifyUser(UserController.getInstance().getCurrentUser(), newDataUser);
				
				setVisible(false);
				dispose();
			}
			
			
		}else if(e.getSource() == cancelButton){
			setVisible(false);
			dispose();
		}
		
	}

	private boolean checkForWrongPassword(String firstPassword, String secondPassword) {
		// TODO Auto-generated method stub
		if(firstPassword.isEmpty() || secondPassword.isEmpty()){
			return true;
		}
		if(firstPassword.equals(secondPassword)) {
			return false;
		} else {
			System.out.println("ManageMyDataDialog/checkForWrongPassword: two passwords are not equal: " + firstPassword + " compared with " + secondPassword);
			return true;
		}
	}
}
