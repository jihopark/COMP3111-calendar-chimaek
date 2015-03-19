package hkust.cse.calendar.unit;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;

public class Appt implements Serializable {

	private TimeSpan mTimeSpan;					// Include day, start time and end time of the appointments

	private String mTitle;						// The Title of the appointments

	private String mInfo;						// Store the content of the appointments description

	private int mApptID;						// The appointment id	
	
	private Appt nextRepeatedAppt = null;						
	private Appt previousRepeatedAppt = null;

	private Location location;
	private Notification notification;
	
	private LinkedList<String> attend;			// The Attendant list
	
	private LinkedList<String> reject;			// The reject list
	
	private LinkedList<String> waiting;			// The waiting list
	
	public Appt() {								// A default constructor used to set all the attribute to default values
		mApptID = 0;
		mTimeSpan = null;
		mTitle = "Untitled";
		mInfo = "";
		attend = new LinkedList<String>();
		reject = new LinkedList<String>();
		waiting = new LinkedList<String>();
	}
	
	public Appt(Appt appt){
		mApptID = 0;
		mTimeSpan = appt.getTimeSpan();
		mTitle = appt.getTitle();
		mInfo = appt.getInfo();
		attend = appt.getAttendList();
		reject = appt.getRejectList();
		waiting = appt.getWaitingList();
	}
	
	public void setNotification(Notification notification){
		this.notification = notification;
	}
	
	public void setLocation(Location location){
		this.location = location;
	}
	
	public Location getLocation(){
		return location;
	}
	
	public Notification getNotification(){
		return notification;
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
	
	//Returns Repeat End Date
	public Timestamp getRepeateEndDate(){
		if (isRepeated()){
			Appt temp = this;
			while (temp.nextRepeatedAppt!=null)
				temp = temp.nextRepeatedAppt;
			return temp.getTimeSpan().EndTime();
		}
		return null;
	}
	
	public Appt getNextRepeatedAppt(){
		return nextRepeatedAppt;
	}
	
	public Appt getPreviousRepeatedAppt(){
		return previousRepeatedAppt;
	}
	
	public void setPreviousRepeatedAppt(Appt appt){
		previousRepeatedAppt = appt;
	}
	public void setNextRepeatedAppt(Appt appt){
		nextRepeatedAppt = appt;
	}
	
	public boolean isRepeated(){
		return !(previousRepeatedAppt == null && nextRepeatedAppt == null);
	}


	// Getter of the attend LinkedList<String>
	public LinkedList<String> getAttendList(){
		return attend;
	}
	
	// Getter of the reject LinkedList<String>
	public LinkedList<String> getRejectList(){
		return reject;
	}
	
	// Getter of the waiting LinkedList<String>
	public LinkedList<String> getWaitingList(){
		return waiting;
	}
	
	public LinkedList<String> getAllPeople(){
		LinkedList<String> allList = new LinkedList<String>();
		allList.addAll(attend);
		allList.addAll(reject);
		allList.addAll(waiting);
		return allList;
	}
	
	public void addAttendant(String addID){
		if (attend == null)
			attend = new LinkedList<String>();
		attend.add(addID);
	}
	
	public void addReject(String addID){
		if (reject == null)
			reject = new LinkedList<String>();
		reject.add(addID);
	}
	
	public void addWaiting(String addID){
		if (waiting == null)
			waiting = new LinkedList<String>();
		waiting.add(addID);
	}
	
	public void setWaitingList(LinkedList<String> waitingList){
		waiting = waitingList;
	}
	
	public void setWaitingList(String[] waitingList){
		LinkedList<String> tempLinkedList = new LinkedList<String>();
		if (waitingList !=null){
			for (int a=0; a<waitingList.length; a++){
				tempLinkedList.add(waitingList[a].trim());
			}
		}
		waiting = tempLinkedList;
	}
	
	public void setRejectList(LinkedList<String> rejectLinkedList) {
		reject = rejectLinkedList;
	}
	
	public void setRejectList(String[] rejectList){
		LinkedList<String> tempLinkedList = new LinkedList<String>();
		if (rejectList !=null){
			for (int a=0; a<rejectList.length; a++){
				tempLinkedList.add(rejectList[a].trim());
			}
		}
		reject = tempLinkedList;
	}
	
	public void setAttendList(LinkedList<String> attendLinkedList) {
		attend = attendLinkedList;
	}
	
	public void setAttendList(String[] attendList){
		LinkedList<String> tempLinkedList = new LinkedList<String>();
		if (attendList !=null){
			for (int a=0; a<attendList.length; a++){
				tempLinkedList.add(attendList[a].trim());
			}
		}
		attend = tempLinkedList;
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
