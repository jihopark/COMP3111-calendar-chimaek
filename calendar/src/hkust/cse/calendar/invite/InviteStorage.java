package hkust.cse.calendar.invite;

import hkust.cse.calendar.unit.GroupAppt;
import hkust.cse.calendar.unit.User;

import java.util.LinkedList;

public class InviteStorage {
	
	public LinkedList<GroupAppt> invites;
	
	public InviteStorage(){
		//need to implement
	}
	
	public void createGroupApptInvite(){
		//time clash check 
		//location capacity check
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
}
