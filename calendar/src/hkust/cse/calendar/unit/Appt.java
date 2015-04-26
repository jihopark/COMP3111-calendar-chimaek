package hkust.cse.calendar.unit;

import hkust.cse.calendar.apptstorage.ApptController;
import hkust.cse.calendar.locationstorage.LocationController;
import hkust.cse.calendar.notification.NotificationController;
import hkust.cse.calendar.time.TimeController;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;

public class Appt implements Serializable {
	
	protected boolean isPublic = false;
	
	protected int repeatType = 0;
	protected TimeSpan mTimeSpan;					// Include day, start time and end time of the appointments

	protected String mTitle;						// The Title of the appointments

	protected String mInfo;						// Store the content of the appointments description

	protected int mApptID;						// The appointment id	
	
	protected int nextRepeatedAppt_id = -1;						
	protected int previousRepeatedAppt_id = -1;
	
	protected int location_id = -1, notification_id = -1;
	
	public Appt() {								// A default constructor used to set all the attribute to default values
		mApptID = 0;
		mTimeSpan = null;
		mTitle = "Untitled";
		mInfo = "";
	}
	
	public Appt(Appt appt){
		mApptID = 0;
		mTimeSpan = appt.getTimeSpan();
		mTitle = appt.getTitle();
		mInfo = appt.getInfo();
		repeatType = appt.getRepeatType();
		location_id = appt.getLocationID();
	}
	
	public boolean getisPublic(){
		return isPublic;
	}
	
	public void setIsPublic(boolean privacy_setting){
		isPublic=privacy_setting;
	}
	
	public int getRepeatType(){ return repeatType; }
	public void setRepeatType(int type){
		if (repeatType <= 3)
			repeatType = type;
	}
	
	public void setNotification(Notification notification){
		notification_id = notification.getID();
	}
	
	public void setLocation(Location location){
		location_id= location.getID();
		System.out.println("Appt/setLocation " + "Set Location " + location_id);
	}
	
	public int getLocationID(){ return location_id; }
	public int getNotificationID(){ return notification_id; }
	
	
	public Location getLocation(){
		System.out.println("Appt/getLocation " + "Get Location " + location_id);
		if (location_id == -1)
			return null;
		
		return LocationController.getInstance().getLocationByID(location_id);
	}
	
	public Notification getNotification(){
		System.out.println("Appt/getNotification " + "Get Notification " + notification_id );
		return NotificationController.getInstance().getNotificationByID(notification_id);
	}

	// Getter of the mTimeSpan
	public TimeSpan TimeSpan() {
		return mTimeSpan;
	}
	
	// Getter of the appointment title
	public String getTitle() {
		return mTitle;
	}

	// Getter of appointment description
	public String getInfo() {
		return mInfo;
	}

	// Getter of the appointment id
	public int getID() {
		return mApptID;
	}
	
	//Returns Repeat Start Date
	public Appt getRepeatStartAppt(){
		if (isRepeated()){
			Appt temp = this;
			while (temp.previousRepeatedAppt_id!=-1){				
				temp = ApptController.getInstance().RetrieveAppt(temp.previousRepeatedAppt_id);
				if (!TimeController.getInstance().isNotPast(temp)){
					temp = ApptController.getInstance().RetrieveAppt(temp.nextRepeatedAppt_id);
					break;
				}
			}
			return temp;
		}
		return null;

	}
	
	//Returns Repeat End Date
	public Timestamp getRepeatedEndDate(){
		if (isRepeated()){
			Appt temp = this;
			while (temp.nextRepeatedAppt_id!=-1)
				temp = ApptController.getInstance().RetrieveAppt(temp.nextRepeatedAppt_id);
			return temp.getTimeSpan().EndTime();
		}
		return null;
	}
	
	public Appt getNextRepeatedAppt(){
		return ApptController.getInstance().RetrieveAppt(nextRepeatedAppt_id);
	}
	
	public Appt getPreviousRepeatedAppt(){
		return ApptController.getInstance().RetrieveAppt(previousRepeatedAppt_id);
	}
	
	public void setPreviousRepeatedAppt(Appt appt){
		if (appt == null)
			previousRepeatedAppt_id = -1;
		else{
			previousRepeatedAppt_id = appt.getID();
			System.out.println("Appt/setPreviousRepeatedAppt " + previousRepeatedAppt_id + " set to previous repeated appt for " + getID());
		}
	}
	public void setNextRepeatedAppt(Appt appt){
		if (appt == null)
			nextRepeatedAppt_id = -1;
		else{
			nextRepeatedAppt_id = appt.getID();
			System.out.println("Appt/setNextRepeatedAppt " + nextRepeatedAppt_id + " set to next repeated appt for " + getID());
		}
	}
	
	public boolean isRepeated(){
		return !(previousRepeatedAppt_id == -1 && nextRepeatedAppt_id == -1);
	}


	// Getter of the appointment title
	public String toString() {
		return mTitle;
	}

	// Setter of the appointment title
	public void setTitle(String t) {
		mTitle = t;
	}

	// Setter of the appointment description
	public void setInfo(String in) {
		mInfo = in;
	}

	// Setter of the mTimeSpan, Returns true for valid timespan, false for invalid timespan
	public boolean setTimeSpan(TimeSpan timespan) {
		long FIFTEEN_MINS = 15*60*1000;
		
		if (timespan==null)
			return false;
		
		Timestamp start = timespan.StartTime(), end = timespan.EndTime();
		if (end.getTime() - start.getTime() >= FIFTEEN_MINS){
			mTimeSpan = timespan;
			return true;
		}
		return false;
	}
	
	public TimeSpan getTimeSpan(){
		return mTimeSpan;
	}

	// Setter if the appointment id
	public void setID(int id) {
		mApptID = id;
	}
	
	
	public boolean equals(Appt a){
		return this.getID() == a.getID();
	}
	
	public boolean isValid(){
		if (getID() <= 0)
			return false;
		if (TimeSpan()==null)
			return false;
		if (getTitle()==null || getTitle().equals(""))
			return false;
		return true;
	}

}
