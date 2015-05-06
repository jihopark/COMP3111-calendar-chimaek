package hkust.cse.calendar.unit;

import java.util.ArrayList;
import java.util.LinkedList;

public class GroupAppt extends Appt {
	private LinkedList<String> attend;			// The Attendant list
	private LinkedList<String> waiting;			// The waiting list
	private String owner_id =null;
	private boolean isVote=false;
	private ArrayList<TimeSpan> voteTimeList=null;
	
	public GroupAppt(){
		mApptID = 0;
		mTimeSpan = null;
		mTitle = "Untitled";
		mInfo = "";
		isVote=false;
		voteTimeList=null;
		
		attend=null;
		waiting=null;
		owner_id=null;
	}
	
	public GroupAppt(Appt appt, LinkedList<String> attendList, String ownerID){
		mApptID=appt.getID();
		mTimeSpan=appt.getTimeSpan();
		mTitle = appt.getTitle();
		mInfo= appt.getInfo();
		location_id = appt.getLocationID();
		notification_id = appt.getNotificationID();
		attend = attendList;
		waiting = new LinkedList<String>(attendList);
		waiting.remove(ownerID);
		owner_id=ownerID;
		isVote=false;
		
	}
	
	public GroupAppt(int repeatType, TimeSpan timespan, String title, String info, int ApptID, int nextRepeatedApptID, int prevRepeatedApptID, int locationID, int notificationID, LinkedList<String> attendList, String ownerID){
		
		this.repeatType = repeatType;
		this.mTimeSpan=timespan;					// Include day, start time and end time of the appointments

		this.mTitle=title;						// The Title of the appointments

		this.mInfo=info;						// Store the content of the appointments description

		this.mApptID=ApptID;						// The appointment id	
		
		this.nextRepeatedAppt_id = nextRepeatedApptID;						
		this.previousRepeatedAppt_id = prevRepeatedApptID;
		
		this.location_id = locationID;
		this.notification_id = notificationID;
		
		this.attend = attendList;			// The Attendant list
		this.waiting = new LinkedList<String>(attendList);			// The waiting list
		this.waiting.remove(ownerID);
		this.owner_id = ownerID;
		this.isVote = false;
	}

	public GroupAppt(GroupAppt gAppt){
		mApptID=gAppt.getID();
		mTimeSpan=gAppt.getTimeSpan();
		mTitle = gAppt.getTitle();
		mInfo= gAppt.getInfo();
		location_id = gAppt.getLocationID();
		notification_id = gAppt.getNotificationID();
		attend = new LinkedList<String>(gAppt.getAttendList());
		waiting = new LinkedList<String>(gAppt.getAttendList());
		waiting.remove(gAppt.getOwner());
		owner_id=gAppt.getOwner();
		isVote = false;
	}
	
	public GroupAppt(Appt appt, LinkedList<String> attendList, String ownerID, ArrayList<TimeSpan> timeList){
		mApptID=appt.getID();
		mTimeSpan=appt.getTimeSpan();
		mTitle = appt.getTitle();
		mInfo= appt.getInfo();
		location_id = appt.getLocationID();
		notification_id = appt.getNotificationID();
		attend = attendList;
		waiting = new LinkedList<String>(attendList);
		waiting.remove(ownerID);
		owner_id=ownerID;
		
		isVote=true;
		voteTimeList = timeList;
	}
	
	public boolean getisVote(){
		return isVote;
	}
	
	public void setisVote(boolean val){
		isVote=val;
	}
	
	public ArrayList<TimeSpan> getvoteTimeList(){
		return voteTimeList;
	}
	public void setvoteTimeList(ArrayList<TimeSpan> list){
		voteTimeList = list;
	}
	
	public String getOwner(){
		return owner_id;
	}
	
	public void setOwner(String id){
		owner_id = id;
	}
	
	public LinkedList<String> getAttendList(){
		return attend;
	}

	public LinkedList<String> getWaitingList(){
		return waiting;
	}

	public void addAttendant(String addID){
		if (attend == null)
			attend = new LinkedList<String>();
		attend.add(addID);
	}
	
	public void removeAttendant(String removeID){
		if (attend !=null)
			attend.remove(removeID);
	}

	public void addWaiting(String addID){
		if (waiting == null)
			waiting = new LinkedList<String>();
		waiting.add(addID);
	}
	
	public void removeWaiting(String removeID){
		if (waiting !=null)
			waiting.remove(removeID);
	}

	public void setWaitingList(LinkedList<String> waitingList){
		waiting = waitingList;
	}

	public void setAttendList(LinkedList<String> attendLinkedList) {
		attend = attendLinkedList;
	}
	
	public boolean checkAllConfirmed(){
		if(waiting.isEmpty()){
			return true;
		}
		return false;
	}
	
	public void copyChangedInfoFrom(GroupAppt gAppt){
		mApptID=gAppt.getID();
		mTimeSpan=gAppt.getTimeSpan();
		mTitle = gAppt.getTitle();
		mInfo= gAppt.getInfo();
		location_id = gAppt.getLocationID();
	}
}
