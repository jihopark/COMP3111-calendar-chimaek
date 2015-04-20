package hkust.cse.calendar.invite;

import java.util.LinkedList;

import hkust.cse.calendar.diskstorage.JsonStorable;
import hkust.cse.calendar.locationstorage.LocationController;
import hkust.cse.calendar.locationstorage.LocationStorage;
import hkust.cse.calendar.locationstorage.LocationStorageMemory;
import hkust.cse.calendar.unit.GroupAppt;
import hkust.cse.calendar.unit.User;

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
			
			return true;
		}
		return false;
	}
	
	public LinkedList<GroupAppt> checkIfUserHasInvite(User user){
		return mInviteStorage.checkIfUserHasInvite(user);
	}
	

}
