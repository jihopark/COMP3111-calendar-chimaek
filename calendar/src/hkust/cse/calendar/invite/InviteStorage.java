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
		//need to implement
		
		return null;
	}
	
	public void setResponse(User user, GroupAppt appt, boolean response){
		//record users response
	}
	
	public void setConfirmedGroupAppt(GroupAppt appt){
		//when everyone accepts evolve to group appt
	}
}
