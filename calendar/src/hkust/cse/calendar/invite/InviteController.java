package hkust.cse.calendar.invite;

import java.util.LinkedList;

import hkust.cse.calendar.apptstorage.ApptController;
import hkust.cse.calendar.apptstorage.ApptStorage;
import hkust.cse.calendar.apptstorage.ApptStorageMemory;
import hkust.cse.calendar.diskstorage.JsonStorable;
import hkust.cse.calendar.locationstorage.LocationController;
import hkust.cse.calendar.locationstorage.LocationStorage;
import hkust.cse.calendar.locationstorage.LocationStorageMemory;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.GroupAppt;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.userstorage.UserController;

public class InviteController {


	/* Applying Singleton Structure */
	private static InviteController instance = null;

	private static InviteStorage mInviteStorage;

	public InviteController() {

	}
	
	public static InviteController getInstance()
	{
		if (instance == null){
			instance = new InviteController();
		}
		return instance;
	}

	public boolean initInviteStorage(InviteStorage storage){
		
		if (mInviteStorage == null){
			mInviteStorage = storage;
			if (mInviteStorage instanceof JsonStorable && mInviteStorage instanceof InviteStorage){
				mInviteStorage = (InviteStorage) ((JsonStorable)mInviteStorage).loadFromJson();
				if (mInviteStorage == null) mInviteStorage = storage;
			}
			return true;
		}
		return false;
	}

	public boolean saveNewGroupAppt(Appt appt, LinkedList<String> attendList, String ownerID){
		GroupAppt tempAppt= mInviteStorage.createGroupApptInvite(appt, attendList, ownerID);
		if(tempAppt == null){
			return false;
		}
		else{
			mInviteStorage.addGroupAppt(tempAppt);
			updateDiskStorage();
			return true;
		}
	}
	
	public LinkedList<GroupAppt> checkIfUserHasInvite(User user){
		return mInviteStorage.checkIfUserHasInvite(user);
	}
	
	public void setResponse(User user, GroupAppt gAppt, boolean response){
		if(response == true){		//when the user accepts
			gAppt.removeWaiting(user.getID());
			if(gAppt.checkAllConfirmed()){		//check if all attendees accepted.
				System.out.println("All confirmed!!");
				setConfirmedGroupAppt(gAppt);
			}
		}
		else{		//when the user declines
			mInviteStorage.removeGroupAppt(gAppt);
		}
		updateDiskStorage();
	}
	
	private void setConfirmedGroupAppt(GroupAppt gAppt){
		mInviteStorage.removeGroupAppt(gAppt);
		System.out.println(gAppt.getAttendList().size());
		for(String attendee : gAppt.getAttendList()){
			System.out.println("Moving to ApptStorage!!");
			User attendingUser = UserController.getInstance().getUser(attendee);
			ApptController.getInstance().saveNewAppt(attendingUser, gAppt);
		}
	}
	
	
	private void updateDiskStorage(){
		if (mInviteStorage instanceof JsonStorable)
			((JsonStorable) mInviteStorage).saveToJson();
	}
}
