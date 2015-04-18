package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptController;
import hkust.cse.calendar.locationstorage.LocationController;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.userstorage.UserController;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

public class UserAppointmentViewDialog extends JFrame implements ActionListener {

	private JButton okButton;
	private JPanel listPanel;
	private JPanel topPanel;
	private JPanel bottomPanel;
	
	private JList userListBox;
	private DefaultListModel userListModel;
	private List<User> userList;
	private List<Appt> appointmentList;
	private JList displayList;
	
	private JLabel repeatedAppts;
	private JLabel nonRepeatedAppts;
	
	private User user;
	
	public UserAppointmentViewDialog(){
		setTitle("View Public Appointments");
		this.setAlwaysOnTop(true);
		repeatedAppts = new JLabel("");
		nonRepeatedAppts = new JLabel("");
		
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
        			appointmentList = ApptController.getInstance().RetrieveApptsInList(user);

    				//System.out.println("Non-Repeated Appointments are: ");
        			String nonRepeat = "Non-Repeated Appointments are: \n";
        			for(Appt a : appointmentList){
        				if(!a.isRepeated()){
        					//System.out.println(a.getTitle() + " " + a.getTimeSpan().toString());
        					nonRepeat += a.getTitle() + " " + a.getTimeSpan().toString() + "\n";
        				}
        			}
    				//System.out.println("Repeated Appointments are: ");
        			String repeat = "Repeated Appointments are: \n";
        			int counter = 0;
        			for(Appt a : appointmentList){
        				if(counter < 20 && a.isRepeated()){
        					//System.out.println(a.getTitle() + " " + a.getTimeSpan().toString());
        					repeat += a.getTitle() + " " + a.getTimeSpan().toString() + "\n";
        					counter++;
        				}
        			}
        			
        			//repeatedAppts.setText(repeat);
        			//nonRepeatedAppts.setText(nonRepeat);
        			System.out.println(nonRepeat);
        			System.out.println(repeat);
        			
        		}
        	}
        };
        userListBox.addMouseListener(mouseListener);
		JScrollPane topUserBox = new JScrollPane(userListBox);
		listPanel.add(topUserBox);
		
		contentPane.add(listPanel);
		
		//top part
		topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.add(repeatedAppts);
		topPanel.add(nonRepeatedAppts);
		
		contentPane.add(topPanel);
				
		//bottom part
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout());
		okButton = new JButton("Ok");
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
			setVisible(false);
			dispose();			
		}
		
	}

}
