package hkust.cse.calendar.unit;

import hkust.cse.calendar.apptstorage.ApptController;
import hkust.cse.calendar.notification.NotificationTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Notification {
	private static final int ONE_HOUR = 0;
	private static final int THREE_HOUR = 1;
	private static final int TWELVE_HOUR = 2;
	private static final int TWENTY_FOUR_HOUR = 3;
	
	
	private String _name;
	//private ArrayList<NotificationTime> _times = new ArrayList<NotificationTime>();
	
	private Date _appointmentTime;
	private int appt_id = -1;
	//private Appt _appt;
	private NotificationTime _notificationTimeObj;
	private int hoursBefore;
	private int minutesBefore;
	private int _notificationID; 
	private boolean delivered = false;
	
	// A default constructor
	public Notification() {
		_name = "";
		_notificationID = 0;
		_appointmentTime = Calendar.getInstance().getTime();
		_notificationTimeObj = null;
		hoursBefore = 0;
		minutesBefore = 0;
	}
	
	/*
	@Deprecated
	public Notification(Appt appt, String name, Date time, 
			boolean flagOne, boolean flagTwo, boolean flagThree, boolean flagFour){
		appt_id = appt.getID();
		_name = name;
		_appointmentTime = time;

		setAlarms(flagOne, flagTwo, flagThree, flagFour);
	}
	*/
	
	public Notification(Appt appt, String name, Date time, int notificationHoursBefore, int notificationMinutesBefore){
		appt_id = appt.getID();
		_name = name;
		_appointmentTime = time;
		hoursBefore = notificationHoursBefore;
		minutesBefore = notificationMinutesBefore;
		
		setAlarms(notificationHoursBefore,notificationMinutesBefore);
	}

	public Notification(Notification notification ) {
		_name = notification.getName();
		_notificationID = 0;
		_notificationTimeObj = new NotificationTime(this,notification.getNotificationTimeObj().getNotificationTime());
		_appointmentTime = notification.getAppointmentTime();
		hoursBefore = notification.getHoursBefore();
		minutesBefore = notification.getMinutesBefore();
	}
	
	public void resetNotificationID(){
		_notificationTimeObj.setNotificationParentID(getID());
	}
	
	public Appt getAppt(){
		return ApptController.getInstance().RetrieveAppt(appt_id);
	}

	public Date getAppointmentTime() {
		return _appointmentTime;
	}

	public NotificationTime getNotificationTimeObj()
	{
		return _notificationTimeObj;
	}
	
	public int getHoursBefore()
	{
		return hoursBefore;
	}
	
	public int getMinutesBefore()
	{
		return minutesBefore;
	}
	
	public String getName() {
		return _name;
	}
	
	public int getID() {
		return _notificationID;
	}
	public void setAppointmentTime(Date date) {
		_appointmentTime = date;
	}
	public void setName(String name) {
		_name = name;
	}
	
	public void hasDelivered(){
		delivered = true;
	}
	
	public boolean isDelivered(){
		return delivered;
	}
		
	public void setID(int id) {
		_notificationID = id;
	}
	
	/*
	@Deprecated
	public void setAlarms(boolean flagOne, boolean flagTwo, boolean flagThree, boolean flagFour) {
		//check flag1, set alarm1 repeat 4 times
		Calendar cal = Calendar.getInstance();
		for (int i=0;i<4;i++)
			_times.add(null);
		
		if(flagOne) {
			cal.setTime(_appointmentTime);
			cal.add(Calendar.HOUR, -1);
			Date oneHourBack = cal.getTime();
			_times.set(ONE_HOUR,new NotificationTime(this, ONE_HOUR,oneHourBack));
		}
		if(flagTwo) {
			cal.setTime(_appointmentTime);
			cal.add(Calendar.HOUR, -3);
			Date threeHourBack = cal.getTime();
			_times.set(THREE_HOUR,new NotificationTime(this, THREE_HOUR,threeHourBack));
		}
		if(flagThree) {
			cal.setTime(_appointmentTime);
			cal.add(Calendar.HOUR, -12);
			Date twelveHourBack = cal.getTime();
			_times.set(TWELVE_HOUR,new NotificationTime(this, TWELVE_HOUR,twelveHourBack));
		}
		if(flagFour) {
			cal.setTime(_appointmentTime);
			cal.add(Calendar.HOUR, -24);
			Date twentyFourHourBack = cal.getTime();
			_times.set(TWENTY_FOUR_HOUR,new NotificationTime(this, TWENTY_FOUR_HOUR,twentyFourHourBack));
		}
	}
	*/
	
	public void setAlarms(int notificationHoursBefore, int notificationMinutesBefore){
		
		Calendar newCalendar = Calendar.getInstance();
		newCalendar.setTime(_appointmentTime);
		newCalendar.add(Calendar.HOUR, (-1)*notificationHoursBefore);
		newCalendar.add(Calendar.MINUTE, (-1)*notificationMinutesBefore);
		_notificationTimeObj = new NotificationTime(this,newCalendar.getTime());
	}
	
	/*
	@Deprecated
	public List<Boolean> getFlags(){
		ArrayList<Boolean> flags = new ArrayList<Boolean>();
		for (int i=0; i<4; i++)
			flags.add(false);
		
		for (int i=0; i<4; i++){
			if (_times.get(i)==null) flags.set(i, false);
			else flags.set(i, true);
			System.out.println("Notification/getFlags " + flags.get(i));
		}
		return flags;
	}
	
	*/
	
	public boolean equals(Notification a){
		return a.getID() == getID();
	}
	
	public String toString() {
		return _name;
	}
	
	public boolean isValid() {
		if (getID() <= 0)
			return false;
		if (getName()==null || getName().equals(""))
			return false;
		return true;
	}
	
}
