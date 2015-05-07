package hkust.cse.calendar.gui;


import hkust.cse.calendar.apptstorage.ApptController;
import hkust.cse.calendar.invite.InviteController;
import hkust.cse.calendar.time.TimeController;
import hkust.cse.calendar.unit.GroupAppt;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.userstorage.UserController;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

public class AvailableTimeSlot_TextWindow extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
    private CalGrid parent;
	private JScrollPane scrollPaneView;
	private Container contentPane;
	private JButton pendingButton;
	private JButton declineButton;
	private JButton okButton;
	private User _currentUser;
	private TimeSpan _selectedTimeSlot;
	private ArrayList<TimeSpan> _voteTimeSpan;
	
    public AvailableTimeSlot_TextWindow(ArrayList<User> userList, ArrayList<Date> dateList) {
    	
    	
    	for(int i=0; i<dateList.size();i++)
    	{
    		System.out.println(dateList.get(i).toString());
    	}
    	
		JPanel mainPanel = new JPanel();

		this.setAlwaysOnTop(true);

		//contentPane = getContentPane();
		//contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
		
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
    	setTitle("View Available TimeSlot");
    	//this.getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
    	setSize(500, 300);
        // create JTextField
    	

    	ArrayList<TimeSpan> slotList = new ArrayList<TimeSpan>();
    	
    	for(int i =0; i<dateList.size(); i++) {
    		
    		Date tempDate = dateList.get(i);
    		JLabel dateLabel= new JLabel("");
			dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    		dateLabel.setText( TimeController.getInstance().getYearFrom(tempDate)+"-"+TimeController.getInstance().getMonthFrom(tempDate)+"-"+TimeController.getInstance().getDateFrom(tempDate));
			
    		mainPanel.add(dateLabel);
			slotList = ApptController.getInstance().getSchedulableTimeSpan(userList, dateList.get(i));
    		

			for(int j=0; j<slotList.size();j++){
				JLabel timeslot = new JLabel("");
				timeslot.setAlignmentX(Component.CENTER_ALIGNMENT);
				timeslot.setText("("+slotList.get(j).OnlyTimetoString()+")");
				
				mainPanel.add(timeslot);
    		}
			JLabel bufferLabel1 = new JLabel("\n");
			
			mainPanel.add(bufferLabel1);
    		
    		
    	}
    	
    	scrollPaneView = new JScrollPane(mainPanel);
		scrollPaneView.setBorder(new BevelBorder(BevelBorder.RAISED));
		add(scrollPaneView);
    }
    
    public AvailableTimeSlot_TextWindow(GroupAppt groupAppt) {
    	_currentUser = UserController.getInstance().getCurrentUser();
    	ArrayList<TimeSpan> slotList = groupAppt.getvoteTimeList();
    	_voteTimeSpan = new ArrayList<TimeSpan>();
		JPanel mainPanel = new JPanel();

		this.setAlwaysOnTop(true);
		this.setVisible(true);
		
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
    	setTitle("View Available TimeSlot");
    	//this.getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
    	setSize(500, 300);
        // create JTextField
    	

		JLabel dateLabel= new JLabel("");
		dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		dateLabel.setText("=="+slotList.get(0).OnlyDatetoString()+"==");
		
		mainPanel.add(dateLabel);

		

		for(int j=0; j<slotList.size();j++){
			JLabel timeslot = new JLabel("");
			timeslot.setAlignmentX(Component.CENTER_ALIGNMENT);
			timeslot.setText("("+slotList.get(j).OnlyTimetoString()+")");
						
			JButton temp = new JButton("Vote");
			temp.setText("Vote for: "+ slotList.get(j));
			temp.setAlignmentX(Component.CENTER_ALIGNMENT);
    		temp.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent e) {
    				String tempVoteDate = temp.getText();
    				String voteTime = tempVoteDate.substring(9);
    				voteTime = voteTime.replaceAll("\\D+","");
    				//TimeSpan From 2015-05-15 00:30 to 2015-05-15 00:45
    				//201505150000
    				//201505150015
    				/**
    				System.out.println("The Start Year is : " +voteTime.substring(0, 4));
    				System.out.println("The Start Month is : " +voteTime.substring(4, 6));
    				System.out.println("The Start Date is : " +voteTime.substring(6, 8));
    				System.out.println("The Start Time is : " +voteTime.substring(8, 12));
    				
    				System.out.println("The End Year is : " +voteTime.substring(12, 16));
    				System.out.println("The End Month is : " +voteTime.substring(16, 18));
    				System.out.println("The End Date is : " +voteTime.substring(18, 20));
    				System.out.println("The End Time is : " +voteTime.substring(20, 24));
    				 **/
    				
    				int startYear = Integer.parseInt(voteTime.substring(0, 4));
    				int startMonth = Integer.parseInt(voteTime.substring(4, 6));
    				int startDate = Integer.parseInt(voteTime.substring(6, 8));
    				int startHour = Integer.parseInt(voteTime.substring(8, 10));
    				int startMin = Integer.parseInt(voteTime.substring(10,12));
    				
    				int endYear = Integer.parseInt(voteTime.substring(12, 16));
    				int endMonth = Integer.parseInt(voteTime.substring(16, 18));
    				int endDate = Integer.parseInt(voteTime.substring(18, 20));
    				int endHour = Integer.parseInt(voteTime.substring(20, 22));
    				int endMin = Integer.parseInt(voteTime.substring(22,24));
    				    				
    				Timestamp startTime = TimeController.getInstance().dateInputToTimestamp(startYear, startMonth, startDate, startHour, startMin, 0);
    				Timestamp endTime = TimeController.getInstance().dateInputToTimestamp(endYear, endMonth, endDate, endHour, endMin, 0);
    				
    				TimeSpan returnTimeSpan = new TimeSpan(startTime, endTime);
    				_voteTimeSpan.add(returnTimeSpan);
    				System.out.println(tempVoteDate);
    				temp.setEnabled(false);
    			}
    		});
    		
    		//mainPanel.add(timeslot);
    		mainPanel.add(temp);
		}
		JLabel bufferLabel1 = new JLabel("\n");
		mainPanel.add(bufferLabel1);
		pendingButton = initializeMainButton();
		declineButton = new JButton("Decline");
		declineButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InviteController.getInstance().setVoteResponse(_currentUser, groupAppt , false, _voteTimeSpan);
				dispose();
			}
		});
		okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InviteController.getInstance().setVoteResponse(_currentUser, groupAppt , true, _voteTimeSpan);
				dispose();
			}
		});
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(declineButton);
		buttonPanel.add(okButton);
		buttonPanel.add(pendingButton);
		
		mainPanel.add(buttonPanel);
    	
    	scrollPaneView = new JScrollPane(mainPanel);
		scrollPaneView.setBorder(new BevelBorder(BevelBorder.RAISED));
		add(scrollPaneView);
    }
    


	private JButton initializeMainButton() {
		JButton test = new JButton("Pending"); 
		test.addActionListener(this);
		return test;
	}
	

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == pendingButton) {
			dispose();
		}
		
	}

}
