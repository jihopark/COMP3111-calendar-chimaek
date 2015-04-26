package hkust.cse.calendar.invite;

import hkust.cse.calendar.apptstorage.ApptController;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.apptstorage.ApptStorageMemory;
import hkust.cse.calendar.diskstorage.FileManager;
import hkust.cse.calendar.diskstorage.JsonStorable;
import hkust.cse.calendar.unit.GroupAppt;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.userstorage.UserController;

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
		for(String userID : attendList){
			for(Appt a : ApptController.RetrieveApptsInList(UserController.getInstance().getUser(userID))){
				if(appt.getTimeSpan().Overlap(a.getTimeSpan())){
					System.out.println("Time clash with "+(UserController.getInstance().getUser(userID).getFullName()));
					return false;
				}
			}	
		}
		return true;
	}
}
