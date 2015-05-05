package hkust.cse.calendar.invite;

import hkust.cse.calendar.apptstorage.ApptController;
import hkust.cse.calendar.diskstorage.FileManager;
import hkust.cse.calendar.diskstorage.JsonStorable;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.GroupAppt;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.userstorage.UserController;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;

public class InviteStorage implements JsonStorable {
	
	public LinkedList<GroupAppt> invites;
	
	public InviteStorage(){
		invites = new LinkedList<GroupAppt>();
	}
	
	//make groupappt and put in storage
	public GroupAppt createGroupApptInvite(Appt appt, LinkedList<String> attendList, String ownerID){
		
		//location capacity check
		if(capacityCheck(appt, attendList) && timeclashCheck(appt, attendList)){
			GroupAppt groupappt = new GroupAppt(appt, attendList, ownerID);
			System.out.println("InviteStorage/createGroupApptInvite: Invitations sent");
			return groupappt;
		}
		return null;
	}
	
	public void removeGroupAppt(GroupAppt gAppt){
		invites.remove(gAppt);
	}
	
	public void addGroupAppt(GroupAppt gAppt){
		invites.add(gAppt);
	}
	
	public boolean removeUserFromAllInvite(User user) {
		if(!invites.isEmpty()){
			for(GroupAppt gAppt : invites) {
				System.out.println("InviteStorage/removeUserFromAllInvite: removing " +user.getID()+ "from all invites" );
				gAppt.removeWaiting(user.getID());
				gAppt.removeAttendant(user.getID());
			}
		}
		return true;
		
	}
	public LinkedList<GroupAppt> checkIfUserHasInvite(User user){
		
		//List of GroupAppt that the current user is involved in.
		LinkedList<GroupAppt> tempGroupApptList = new LinkedList<GroupAppt>();
		
		if(!invites.isEmpty()){
			for(GroupAppt gAppt : invites)//check all the group appts that has invites
			{
				for(String waitingUserID: gAppt.getWaitingList()){//check if the group appt's waiting list includes user
					if(waitingUserID.equals(user.getID())){
						tempGroupApptList.add(gAppt);
						break;
					}
				}
			}
		}
		if(!(tempGroupApptList.isEmpty())){		//if there is any GroupAppt the current user is involved in
			return tempGroupApptList;
		}
		else{		//if there is no GroupAppt the current user is involved in
			return null;
		}
	}
	
	private boolean capacityCheck(Appt appt, LinkedList<String> attendList){
		
		if(appt.getLocation()!=null){
			if(appt.getLocation().getCapacity()>= attendList.size()){
				return true;
			}
			else{
				System.out.println("Location cannot hold enough people!");
				return false;
			}
		}
		else{
			System.out.println("Appt Location NULL");
			return false;
		}
	}
	
	private boolean timeclashCheck(Appt appt, LinkedList<String> attendList){
		//check confirmed appt clash
		for(String userID : attendList){
			for(Appt a : ApptController.getInstance().RetrieveApptsInList(UserController.getInstance().getUser(userID))){
				if(appt.getTimeSpan().Overlap(a.getTimeSpan())){
					System.out.println("Time clash with "+(UserController.getInstance().getUser(userID).getFullName()));
					return false;
				}
			}	
		}
		
		//check group appt clash
		for(String userID : attendList){
			for(GroupAppt gAppt : RetrieveGroupApptsInList(UserController.getInstance().getUser(userID))){
				if(appt.getTimeSpan().Overlap(gAppt.getTimeSpan())){
					System.out.println("Time clash with "+(UserController.getInstance().getUser(userID).getFullName())
							+gAppt.getTimeSpan());
					return false;
				}
			}
		}
		
		return true;
	}
	
	public List<GroupAppt> RetrieveGroupApptsInList(User user) {
		if (!hasNoAppts(user)){
			return getAllGroupAppts(user);
		}
		return new LinkedList<GroupAppt>();
	}
	
	private boolean hasNoAppts(User user){
		for(GroupAppt gAppt: invites){
			for(String userString : gAppt.getAttendList()){
				if(user == UserController.getInstance().getUser(userString)){
					return false;
				}
			}
		}
		return true;
	}
	
	private List<GroupAppt> getAllGroupAppts(User user){
		List<GroupAppt> attendingList = new ArrayList<GroupAppt>();
		
		for(GroupAppt gAppt: invites){
			for(String userString : gAppt.getAttendList()){
				if(user == UserController.getInstance().getUser(userString)){
					attendingList.add(gAppt);
				}
			}
		}
		
		return attendingList;
	}
	
	public String getFileName(){
		return "DISK_INVITE.txt";
	}
	
	public Object loadFromJson(){
		Gson gson = new Gson();
		String json = FileManager.getInstance().loadFromFile(getFileName());
		if (json.equals("")) return null;
		return gson.fromJson(json, InviteStorage.class);
	}
	
	public void saveToJson(){
		Gson gson = new Gson();
		FileManager.getInstance().writeToFile(gson.toJson(this), getFileName());
	}


	
}
