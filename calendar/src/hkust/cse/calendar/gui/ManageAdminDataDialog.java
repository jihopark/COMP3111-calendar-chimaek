package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptController;
import hkust.cse.calendar.unit.DeleteRequest;
import hkust.cse.calendar.unit.ModifyNotification;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.userstorage.UserController;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class ManageAdminDataDialog extends JFrame implements ActionListener {

	private JButton cancelButton;
	private JButton removeButton;
	private JButton okButton;
	private JPanel listPanel;
	private JPanel topPanel;
	private JPanel bottomPanel;
	
	private JList userListBox;
	private DefaultListModel userListModel;
	private List<User> userList;
	private JList displayList;
	
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
	
	public ManageAdminDataDialog(){
		setTitle("Manage All Users Information");
		this.setAlwaysOnTop(true);
		user = UserController.getInstance().getCurrentUser();
				
		Container contentPane;
		contentPane = getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
		
		userListModel = new DefaultListModel();
		userList = UserController.getInstance().getUserList();
		for(User a : userList) {
			userListModel.addElement(a.getID());
		}
		
	
		listPanel = new JPanel();
		listPanel.setLayout(new FlowLayout());
		userListBox = new JList( userListModel );
		userListBox.setFixedCellWidth(300);
		userListBox.setFixedCellHeight(20);
        
        MouseListener mouseListener = new MouseAdapter() {
        	public void mouseClicked(MouseEvent e) {
        		if(e.getClickCount() == 2 ) {
        			//Location selectedItem = (Location) displayList.getSelectedValue();
                    //capacityLabel.setText("Capacity is: " + selectedItem.getCapacity());
        			String ID = (String) userListBox.getSelectedValue();
        			user = UserController.getInstance().getUser(ID);
        			userNameBox.setText(" " + user.getID());
        			firstNameBox.setText(user.getFirstName());
        			lastNameBox.setText(user.getLastName());
        			emailAddressBox.setText(user.getEmailAddress());
        		}
        	}
        };
        userListBox.addMouseListener(mouseListener);
		JScrollPane topUserBox = new JScrollPane(userListBox);
		listPanel.add(topUserBox);

		
		contentPane.add(listPanel);
		
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
		
		contentPane.add(topPanel);
				
		//bottom part
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout());
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		bottomPanel.add(cancelButton);
		
		removeButton = new JButton("Remove");
		removeButton.addActionListener(this);
		bottomPanel.add(removeButton);
		
		okButton = new JButton("Modify Data");
		okButton.addActionListener(this);
		bottomPanel.add(okButton);
		contentPane.add(bottomPanel);
		
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
				User newDataUser = user;
				newDataUser.Password(firstPasswordBox.getText());
				newDataUser.setEmailAddress(emailAddressBox.getText());
				newDataUser.setFirstName(firstNameBox.getText());
				newDataUser.setLastName(lastNameBox.getText());
				UserController.getInstance().modifyUser(user, newDataUser);
				UserController.getInstance().addModifyNotification(new ModifyNotification(newDataUser.getID(), UserController.getInstance().getCurrentUser().getID()));
				setVisible(false);
				dispose();
			}
			
			
		}else if(e.getSource() == removeButton){
			User newDataUser = user;
			if(newDataUser == UserController.getInstance().getCurrentUser()){
				JOptionPane.showMessageDialog(this, "Could not Delete Yourself!",
						"Remove UnSuccessful", JOptionPane.OK_CANCEL_OPTION);
				return;
			}
			UserController.getInstance().removeUser(user.toString());
			//DeleteRequest newDeleteRequest = new DeleteRequest(user.toString(), UserController.getInstance().getCurrentUser().toString());
			JOptionPane.showMessageDialog(this, "You have Requested to Delete User: " + newDataUser.getID() + "!",
					"Remove Requsted Successful", JOptionPane.OK_CANCEL_OPTION);
			userListModel.clear();
			userList = UserController.getInstance().getUserList();
			for(User a : userList) {
				userListModel.addElement(a.getID());
			}
			

			
			/** old code: delete on user on request
			if(UserController.getInstance().removeUser(newDataUser.getID())){
				JOptionPane.showMessageDialog(this, "You have Deleted User: " + newDataUser.getID() + "!",
						"Remove Successful", JOptionPane.OK_CANCEL_OPTION);
				userListModel.clear();
				userList = UserController.getInstance().getUserList();
				for(User a : userList) {
					userListModel.addElement(a.getID());
				}
			} else {
				JOptionPane.showMessageDialog(this, "Could Not Delete the Specific User.",
						"Remove UnSuccessful", JOptionPane.OK_CANCEL_OPTION);
			}
			**/
			
		} else if(e.getSource() == cancelButton){
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
