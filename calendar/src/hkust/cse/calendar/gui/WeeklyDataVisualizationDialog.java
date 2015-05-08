package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptController;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.userstorage.UserController;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class WeeklyDataVisualizationDialog extends JFrame implements ActionListener {
	private Container contentPane;
	
	private JButton okButton;
	private JPanel gridPanel;
	private JPanel bottomPanel;
	
	private JLabel sunday;
	private JLabel monday;
	private JLabel tuesday;
	private JLabel wednesday;
	private JLabel thursday;
	private JLabel friday;
	private JLabel saturday;
	
	private CircleComponent component;
	
	private List<User> users;
	private List<Appt> usersAppts;
	private int[] radius;
	
	public WeeklyDataVisualizationDialog(){
		setTitle("Weekly Analysis of Users Appointment");
		setSize(500, 300);
		this.setAlwaysOnTop(true);
		users = UserController.getInstance().getUserList();
		int lines = users.size();
		int length = 8;
		radius = new int[7];
		
		System.out.println("Total " +lines+ " users with " +length+" length");
		
		contentPane = getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
		

		gridPanel = new JPanel();
		gridPanel.setAlignmentY(CENTER_ALIGNMENT);
		gridPanel.setAlignmentX(CENTER_ALIGNMENT);
		gridPanel.setLayout(new GridLayout(lines + 1, length, 5,5));
		//gridPanel.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		gridPanel.add(new JLabel(" "));
		
		sunday = new JLabel("Sunday");
		//sunday.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		sunday.setHorizontalAlignment(SwingConstants.LEFT);
		sunday.setVerticalAlignment(SwingConstants.CENTER);
		gridPanel.add(sunday);
		
		monday = new JLabel("monday");
		//monday.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		monday.setHorizontalAlignment(SwingConstants.LEFT);
		monday.setVerticalAlignment(SwingConstants.CENTER);
		gridPanel.add(monday);
		
		tuesday = new JLabel("Tuesday");
		//tuesday.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		tuesday.setHorizontalAlignment(SwingConstants.LEFT);
		tuesday.setVerticalAlignment(SwingConstants.CENTER);
		gridPanel.add(tuesday);
		
		wednesday = new JLabel("Wednesday");
		//wednesday.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		wednesday.setHorizontalAlignment(SwingConstants.LEFT);
		wednesday.setVerticalAlignment(SwingConstants.CENTER);
		gridPanel.add(wednesday);
		
		thursday = new JLabel("Thursday");
		//thursday.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		thursday.setHorizontalAlignment(SwingConstants.LEFT);
		thursday.setVerticalAlignment(SwingConstants.CENTER);
		gridPanel.add(thursday);
		
		friday = new JLabel("Friday");
		//friday.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		friday.setHorizontalAlignment(SwingConstants.LEFT);
		friday.setVerticalAlignment(SwingConstants.CENTER);
		gridPanel.add(friday);
		
		saturday = new JLabel("Saturday");
		//saturday.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		saturday.setHorizontalAlignment(SwingConstants.LEFT);
		saturday.setVerticalAlignment(SwingConstants.CENTER);
		gridPanel.add(saturday);
		
	
		
		
		for(int i =0; i < lines; i++) {
			JLabel userName = new JLabel(users.get(i).toString());
			//userName.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			userName.setHorizontalAlignment(SwingConstants.CENTER);
			userName.setVerticalAlignment(SwingConstants.TOP);
			//userName.setHorizontalAlignment(SwingConstants.NORTH);
			usersAppts = ApptController.getInstance().RetrieveApptsInList(users.get(i));
			gridPanel.add(userName);
			for(int j = 0; j<7;j++) {
				//JButton temp = new JButton(users.get(i).toString() + "'s Circule " + j);
				//gridPanel.add(temp);
				for(Appt a: usersAppts) {
					if(a.getTimeSpan().StartTime().getDay()==0){
						radius[0]++; 
					} else if(a.getTimeSpan().StartTime().getDay()==1){
						radius[1]++; 
					} else if(a.getTimeSpan().StartTime().getDay()==2){
						radius[2]++; 
					} else if(a.getTimeSpan().StartTime().getDay()==3){
						radius[3]++; 
					} else if(a.getTimeSpan().StartTime().getDay()==4){
						radius[4]++; 
					} else if(a.getTimeSpan().StartTime().getDay()==5){
						radius[5]++; 
					} else if(a.getTimeSpan().StartTime().getDay()==6){
						radius[6]++; 
					} else {
						
					}
				}
				
				int totalNumberAppt = 0;
				for(int k = 0;k<7;k++){
					totalNumberAppt += radius[k];
				}
				if(totalNumberAppt == 0){
					component = new CircleComponent(5 + radius[j]);
				} else {
					component = new CircleComponent(5 + 35*radius[j]/totalNumberAppt);
				}
				
				component.setLocation(80, 80);
				component.setSize(component.getPreferredSize());
				//component.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				//component.setHorizontalAlignment(SwingConstants.CENTER);
				gridPanel.add(component);
				gridPanel.repaint();
				
				for(int k = 0;k<7;k++){
					radius[k]=0;
				}
			}
			
		}
		
		contentPane.add(gridPanel);
		
		/**
		gridPanel.setLayout(new GridLayout(1,2));
		JButton temp = new JButton(" 123");
		gridPanel.add(temp);
		contentPane.add(temp);
		
		**/
		
					
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
