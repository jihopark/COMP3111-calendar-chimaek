package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptController;
import hkust.cse.calendar.invite.InviteController;
import hkust.cse.calendar.locationstorage.LocationController;
import hkust.cse.calendar.locationstorage.LocationStorageMemory;
import hkust.cse.calendar.time.TimeController;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.userstorage.UserController;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class AppScheduler_Vote extends JFrame implements ActionListener,
ComponentListener {

	private JList leftUserBox;
	private JList rightUserBox;
	private JButton cancelButton;
	
	private JButton sendVoteButton; 
	private JButton leftButton;
	private JButton rightButton;
	
	private DefaultListModel leftListModel;
	private DefaultListModel rightListModel;
	
	private List<User> userList;
	private JList displayList;
	
	private JLabel titleLabel;
	private JTextField titleField;
	private JCheckBox setPublicCheckBox;

	//private JLabel descriptionLabel;
	//private JTextField descriptionField;
	private JTextArea detailArea;

	private JComboBox locationField;
	private String[] locationStringArray;
	
	private CalGrid parent;
	private JLabel startyearLabel;
	private JTextField startyearField;
	private JLabel startmonthLabel;
	private JTextField startmonthField;
	private JLabel startdayLabel;
	private JTextField startdayField;
	
	private Appt tempAppt;
	
	public AppScheduler_Vote(CalGrid cal){
		
		parent = cal;
		setTitle("App Scheduler Vote Dialog");
		this.setAlwaysOnTop(true);
		
		Container contentPane;
		contentPane = getContentPane();
		contentPane.setSize(500, 1300);

		leftListModel = new DefaultListModel();
		// get user data from user controller currently temp data 
		userList = UserController.getInstance().getUserList();
		for(User a : userList) {
			if(!a.equals(UserController.getInstance().getCurrentUser()))
				leftListModel.addElement(a.getID());
		}
		//end
		JPanel top = new JPanel();
		top.setLayout(new BorderLayout());
		
		JPanel panelTitle = initializeTitleNLocationNPublicPanel();
		top.add("North", panelTitle);
		
		JPanel panelDate = initializeStartDatePanel();
		top.add("Center", panelDate);
		
		JPanel panelDetail = initializeDetailPanel();
		top.add("South",panelDetail);
		
		contentPane.add("North",top);
		
		
		
		JPanel center = new JPanel();
		center.setLayout(new FlowLayout());
		leftUserBox = new JList( leftListModel );
		leftUserBox.setFixedCellWidth(50);
		leftUserBox.setFixedCellHeight(20);
		
		JScrollPane leftUserBoxScrollPane = new JScrollPane(leftUserBox);
		center.add(leftUserBoxScrollPane);
		
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
		
			center.add(buttonPanel);
			center.add(rightUserBoxScrollPane);
			
				contentPane.add("Center", center);
		//bottom part
		JPanel bottom = new JPanel();
		bottom.setLayout(new FlowLayout());
		
		sendVoteButton = new JButton("Send Vote");
		sendVoteButton.addActionListener(this);
		bottom.add(sendVoteButton);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		bottom.add(cancelButton);
		

		
		contentPane.add("South", bottom);
		
		//visualization part
		pack();
		setLocationRelativeTo(null);
		setVisible(true);	
	}
	
	public JPanel initializeTitleNLocationNPublicPanel()
	{
		JPanel panelTitleNLocationNPublic = new JPanel();
		
		//title label and field.
		JLabel titleLabel = new JLabel("TITLE");
		titleField = new JTextField(12);
		panelTitleNLocationNPublic.add(titleLabel);
		panelTitleNLocationNPublic.add(titleField);

		//location list
		LocationController.getInstance().initLocationStorage(new LocationStorageMemory(UserController.getInstance().getAdmin()));
		ArrayList<Location> locationList = new ArrayList<Location>();
		for(int i=0; i<LocationController.getInstance().getLocationList().getSize(); i++){
			locationList.add(LocationController.getInstance().getLocationList().getElementAt(i));
		}

		locationStringArray = new String[locationList.size()];
		int i = 0;
		for(Location l: locationList)
		{
			locationStringArray[i] = l.getName();
			i++;
		}

		JLabel locationLabel = new JLabel("LOCATION");
		locationField = new JComboBox(locationStringArray);
		panelTitleNLocationNPublic.add(locationLabel);
		panelTitleNLocationNPublic.add(locationField);
		
		//set public checkbox
		setPublicCheckBox = new JCheckBox("isPublic");
		panelTitleNLocationNPublic.add(setPublicCheckBox);
				
		
		return panelTitleNLocationNPublic;
	}

	public JPanel initializeDetailPanel()
	{
		JPanel tempPanelDetail = new JPanel();
		tempPanelDetail.setLayout(new BorderLayout());
		Border detailBorder = new TitledBorder(null, "Appointment Description");
		tempPanelDetail.setBorder(detailBorder);
		detailArea = new JTextArea(15, 30);

		detailArea.setEditable(true);
		JScrollPane detailScroll = new JScrollPane(detailArea);
		tempPanelDetail.add(detailScroll);

		return tempPanelDetail;
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
			
		}else if(e.getSource() == cancelButton){
			setVisible(false);
			dispose();
		}else if(e.getSource() == sendVoteButton){
			//get data from rightUserBox and add it to userController
			if(rightListModel.getSize() <= 0){
				JOptionPane.showMessageDialog(this, "Please choose at least one user before sending the invitation",
						"Input Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			///send vote (
			LinkedList<String> inviteeList = new LinkedList<String>();
			

			for(int i = 0; i<rightListModel.getSize();i++){
				inviteeList.add(UserController.getInstance().getUser(rightListModel.get(i).toString()).getID());
			}
			
			ArrayList<Date> dateList = new ArrayList<Date>();
		
			Date date = new Date();
			
			int[] validDate = getValidDate(startyearField,startmonthField,startdayField);
			int[] validTimeInterval = {getTimeInput(0,0), getTimeInput(0,15)};
			
			if(validDate == null || validTimeInterval == null)
			{
				System.out.println("not Valid date || TimeInterval");
				return;
			}
			Timestamp timestampForStartTime = CreateTimeStamp(validDate, validTimeInterval[0]);
			Timestamp timestampForEndTime = CreateTimeStamp(validDate, validTimeInterval[1]);
			
			TimeSpan timeSpanForAppt = new TimeSpan(timestampForStartTime,timestampForEndTime);
			
			
			//set Appt info	
			tempAppt.setTimeSpan(timeSpanForAppt);
			tempAppt.setTitle(titleField.getText());
			tempAppt.setInfo(detailArea.getText());
			if(setPublicCheckBox.isSelected()){
				tempAppt.setIsPublic(true);
			}
			//SAVE LOCATION
			String locationString = (String) locationField.getSelectedItem();
			Location locationObject = LocationController.getInstance().RetrieveLocations(locationString);
			if(tempAppt.getLocation()!=null && !(tempAppt.getLocation().getName().equals(locationObject.getName()))){
				tempAppt.getLocation().decreaseCountForLocation();
			}
			tempAppt.setLocation(locationObject);
			
			//date = TimeController.dateInputToDate(validDate[0],validDate[1], validDate[2], 0,0,0);
			
			if(sendVote(inviteeList, tempAppt))
				JOptionPane.showMessageDialog(this,  "Sent vote invitation successfully");
			else{
				JOptionPane.showMessageDialog(this, "Failed to send invitation!");
			}
		}
	}
	
	private boolean sendVote(LinkedList<String> inviteeList, Appt curAppt){
		if(..InviteController.getInstance().saveNewGroupAppt(curAppt,inviteeList,
				UserController.getInstance().getCurrentUser().getID())){
			JOptionPane.showMessageDialog(this, "Saved group appointment_VOTE withOUT notification!");
			setVisible(false);
			dispose();
		}
		else{
			JOptionPane.showMessageDialog(this, "Failed to save group appointment!");
		}
		return false;
	}
	
	
	private int getTimeInput(int hour, int min) 
	{

		if (hour == -1)
			return -1;
		if (min == -1)
			return -1;

		return (hour * 60 + min);

	}
	private int[] setTimeInterval() {

		int[] result = new int[2];
		result[0] = getTimeInput(0, 0);
		result[1] = getTimeInput(24, 0);

		return result;
	}
	
	private int[] getValidDate(JTextField year, JTextField month, JTextField day) {

		int[] date = new int[3];
		date[0] = Utility.getNumber(year.getText());
		date[1] = Utility.getNumber(month.getText());

		if (date[0] < 1980 || date[0] > 2100) 
		{
			JOptionPane.showMessageDialog(this, "Please input proper year",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		if (date[1] <= 0 || date[1] > 12) 
		{
			JOptionPane.showMessageDialog(this, "Please input proper month",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		date[2] = Utility.getNumber(day.getText());
		int monthDay = CalGrid.monthDays[date[1] - 1];

		if (date[1] == 2) 
		{
			GregorianCalendar c = new GregorianCalendar();
			if (c.isLeapYear(date[0]))
				monthDay = 29;
		}

		if (date[2] <= 0 || date[2] > monthDay) 
		{
			JOptionPane.showMessageDialog(this,
					"Please input proper month day", "Input Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
		System.out.println(year.getText()+" "+month.getText()+" "+day.getText());
		return date;
	}
	
	
	private Timestamp CreateTimeStamp(int[] date, int time) {
		Timestamp stamp = new Timestamp(0);
		TimeController.getInstance().setYear(stamp, date[0]);
		TimeController.getInstance().setMonth(stamp, date[1]);
		if(time/60 == 24)
		{
			TimeController.getInstance().setDate(stamp, date[2]+1);
			TimeController.getInstance().setHour(stamp, 0);
			TimeController.getInstance().setMinute(stamp, time % 60);
		}
		else
		{
			TimeController.getInstance().setDate(stamp,date[2]);
			TimeController.getInstance().setHour(stamp, time/60);
			TimeController.getInstance().setMinute(stamp, time % 60);
		}
		return stamp;
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
	
	private JPanel initializeStartDatePanel()
	{
		JPanel panelDate = new JPanel();
		Border dateBorder = new TitledBorder(null, "START DATE");
		panelDate.setBorder(dateBorder);
		
		startyearLabel = new JLabel("YEAR: ");
		panelDate.add(startyearLabel);
		startyearField = new JTextField(6);
		panelDate.add(startyearField);

		startmonthLabel = new JLabel("MONTH: ");
		panelDate.add(startmonthLabel);
		startmonthField = new JTextField(4);
		panelDate.add(startmonthField);

		startdayLabel = new JLabel("DAY: ");
		panelDate.add(startdayLabel);
		startdayField = new JTextField(4);
		panelDate.add(startdayField);
		
		return panelDate;
	}
	
	

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
	

}
