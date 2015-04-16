package hkust.cse.calendar.gui;

import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.userstorage.UserController;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class GroupInvitationDialog extends JFrame implements ActionListener {

	private JList leftUserBox;
	private JList rightUserBox;
	private JButton cancelButton;
	private JButton okButton;
	private JButton leftButton;
	private JButton rightButton;
	private DefaultListModel leftListModel;
	private DefaultListModel rightListModel;
	private List<User> userList;
	private JList displayList;
	
	public GroupInvitationDialog(){
		setTitle("Group Event Invitation Dialog");
		this.setAlwaysOnTop(true);
		
		Container contentPane;
		contentPane = getContentPane();

		leftListModel = new DefaultListModel();
		// get user data from user controller currently temp data 
		userList = UserController.getInstance().getUserList();
		for(User a : userList) {
			leftListModel.addElement(a.getID());
		}
		//end
			
			
		//top part
		JPanel top = new JPanel();
		top.setLayout(new FlowLayout());
		leftUserBox = new JList( leftListModel );
		leftUserBox.setFixedCellWidth(50);
		leftUserBox.setFixedCellHeight(20);
		
		JScrollPane leftUserBoxScrollPane = new JScrollPane(leftUserBox);
			top.add(leftUserBoxScrollPane);
		
		rightListModel = new DefaultListModel();
		rightUserBox = new JList( rightListModel );
		rightUserBox.setFixedCellWidth(50);
		rightUserBox.setFixedCellHeight(20);
		JScrollPane rightUserBoxScrollPane = new JScrollPane(rightUserBox);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		leftButton = new JButton("<<");
		leftButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		leftButton.addActionListener(this);
		leftButton.setEnabled(false);
			buttonPanel.add(leftButton);
			
		rightButton = new JButton(">>");
		rightButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		rightButton.addActionListener(this);
			buttonPanel.add(rightButton);
		
			top.add(buttonPanel);
			top.add(rightUserBoxScrollPane);
			
				contentPane.add("North", top);
		//bottom part
		JPanel bottom = new JPanel();
		bottom.setLayout(new FlowLayout());
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
			bottom.add(cancelButton);
		
		okButton = new JButton("Ok");
		okButton.addActionListener(this);
			bottom.add(okButton);
		
				contentPane.add("South", bottom);
		
		//visualization part
		pack();
		setLocationRelativeTo(null);
		setVisible(true);	
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == leftButton)
		{
			if(rightUserBox.getSelectedValue() == null){
				return;
			}
			String selected = rightUserBox.getSelectedValue().toString();
			int selectedIndex = rightUserBox.getSelectedIndex();
			rightListModel.remove(selectedIndex);
			leftListModel.addElement(selected);
			checkButtonActivity();
			
		}else if(e.getSource() == rightButton){
			if(leftUserBox.getSelectedValue() == null){
				return;
			}
			String selected = leftUserBox.getSelectedValue().toString();
			int selectedIndex = leftUserBox.getSelectedIndex();
			leftListModel.remove(selectedIndex);
			rightListModel.addElement(selected);
			checkButtonActivity();
			
		}else if(e.getSource() == okButton){
			//get data from rightUserBox and add it to userController
			if(rightListModel.getSize() <= 0){
				JOptionPane.showMessageDialog(this, "Please choose at least one user before sending the invitation",
						"Input Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			for(int i = 0; i<rightListModel.getSize();i++){
				sendGroupInvitation(rightListModel.get(i).toString());
			}
			setVisible(false);
			dispose();
		}else if(e.getSource() == cancelButton){
			setVisible(false);
			dispose();
		}
		
	}

	private void sendGroupInvitation(String user) {
		// TODO Auto-generated method stub
		System.out.println("GroupInvitationDialog/sendGroupInvitation: Invitation sent to " + user);
		//need to connect to groupInvitation send logic
	}

	private void checkButtonActivity() {
		// TODO Auto-generated method stub
		if(rightListModel.isEmpty()){
			leftButton.setEnabled(false);
			rightButton.setEnabled(true);
		} else if(leftListModel.isEmpty()){
			rightButton.setEnabled(false);
			leftButton.setEnabled(true);
		} else{
			rightButton.setEnabled(true);
			leftButton.setEnabled(true);
		}
	}
}
