package hkust.cse.calendar.unit;

import java.util.LinkedList;

public class GroupAppt extends Appt {
	private LinkedList<String> attend;			// The Attendant list
	private LinkedList<String> waiting;			// The waiting list
	private String owner_id =null;
	
	
	public GroupAppt(){

	}
	
	public String getOwner(){
		//need to implement
		return "";
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
}
