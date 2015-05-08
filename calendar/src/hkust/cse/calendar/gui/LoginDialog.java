	package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptController;
import hkust.cse.calendar.apptstorage.ApptStorage;
import hkust.cse.calendar.invite.InviteController;
import hkust.cse.calendar.invite.InviteStorage;
import hkust.cse.calendar.locationstorage.LocationController;
import hkust.cse.calendar.locationstorage.LocationStorage;
import hkust.cse.calendar.notification.NotificationController;
import hkust.cse.calendar.notification.NotificationStorage;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.userstorage.UserController;
import hkust.cse.calendar.userstorage.UserStorage;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class LoginDialog extends JFrame implements ActionListener
{
	private JTextField userName;
	private JPasswordField password;
	private JButton button;
	private JButton closeButton;
	private JButton signupButton;
	
	public LoginDialog()		// Create a dialog to log in
	{
		
		setTitle("Log in");
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		
		Container contentPane;
		contentPane = getContentPane();
		
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
		
		JPanel messPanel = new JPanel();
		messPanel.add(new JLabel("Please input your user name and password to log in."));
		top.add(messPanel);
		
		JPanel namePanel = new JPanel();
		namePanel.add(new JLabel("User Name:"));
		userName = new JTextField(15);
		namePanel.add(userName);
		top.add(namePanel);
		
		JPanel pwPanel = new JPanel();
		pwPanel.add(new JLabel("Password:  "));
		password = new JPasswordField(15);
		pwPanel.add(password);
		top.add(pwPanel);

		
		JPanel signupPanel = new JPanel();
		signupPanel.add(new JLabel("If you don't have an account, please sign up:"));
		signupButton = new JButton("Sign up now");
		signupButton.addActionListener(this);
		signupPanel.add(signupButton);
		top.add(signupPanel);
		
		contentPane.add("North", top);
		
		JPanel butPanel = new JPanel();
		butPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		button = new JButton("Log in");
		button.addActionListener(this);
		butPanel.add(button);
		
		//Need to press Alt+Enter//
		button.setMnemonic(KeyEvent.VK_ENTER);
		
		closeButton = new JButton("Close program");
		closeButton.addActionListener(this);
		butPanel.add(closeButton);
		
		contentPane.add("South", butPanel);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);	
		
	}
	

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == button)
		{
			// When the button is clicked, check the user name and password, and try to log the user in
			
			if (!attemptLogin(userName.getText(), String.valueOf(password.getPassword()))){
				JOptionPane.showMessageDialog(this, "Incorrect Credential",
						"Input Error", JOptionPane.ERROR_MESSAGE);
			}
			
		}
		else if(e.getSource() == signupButton)
		{
			// Create a new account
			RegisterDialog registerDialog = new RegisterDialog();
			setVisible(false);
			
		}
		else if(e.getSource() == closeButton)
		{
			int n = JOptionPane.showConfirmDialog(null, "Exit Program ?",
					"Confirm", JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION)
				System.exit(0);			
		}
	}
	
	private boolean attemptLogin(String id, String pw){
		UserController.getInstance().initUserStorage(new UserStorage());
		User user = UserController.getInstance().getUserFromCredential(id, pw);
		if (user == null)
			return false;
		
		UserController.getInstance().setCurrentUser(user);
		ApptController.getInstance().initApptStorage(new ApptStorage());
		LocationController.getInstance().initLocationStorage(new LocationStorage(user));
		NotificationController.getInstance().initNotificationStorage(new NotificationStorage(user));
		InviteController.getInstance().initInviteStorage(new InviteStorage());
		CalGrid grid = new CalGrid();
		setVisible( false );
		return true;
	}
	
	// This method checks whether a string is a valid user name or password, as they can contains only letters and numbers
	public static boolean ValidString(String s)
	{
		char[] sChar = s.toCharArray();
		for(int i = 0; i < sChar.length; i++)
		{
			int sInt = (int)sChar[i];
			if(sInt < 48 || sInt > 122)
				return false;
			if(sInt > 57 && sInt < 65)
				return false;
			if(sInt > 90 && sInt < 97)
				return false;
		}
		return true;
	}
}
