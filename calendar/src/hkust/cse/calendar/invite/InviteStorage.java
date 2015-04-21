package hkust.cse.calendar.invite;

import hkust.cse.calendar.apptstorage.ApptController;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.GroupAppt;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.userstorage.UserController;

import java.util.LinkedList;
import java.util.List;

public class InviteStorage {
	
	public LinkedList<GroupAppt> invites;
	
	public InviteStorage(){
		//need to implement
	}
	
	//make groupappt and put in storage
	public GroupAppt createGroupApptInvite(Appt appt, LinkedList<String> attendList, String ownerID){
		
		//location capacity check
		if(capacityCheck(appt, attendList) && timeclashCheck(appt, attendList)){
			GroupAppt groupappt = new GroupAppt(appt, attendList, ownerID);
			return groupappt;
		}
		return null;
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
	
	public void setResponse(User user, GroupAppt appt, boolean response){
		//record users response
	}
	
	public void setConfirmedGroupAppt(GroupAppt appt){
		//when everyone accepts evolve to group appt
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
