package hkust.cse.calendar.invite;

import java.util.LinkedList;
import java.util.List;

import hkust.cse.calendar.apptstorage.ApptController;
import hkust.cse.calendar.apptstorage.ApptStorage;
import hkust.cse.calendar.diskstorage.JsonStorable;
import hkust.cse.calendar.locationstorage.LocationController;
import hkust.cse.calendar.locationstorage.LocationStorage;
import hkust.cse.calendar.locationstorage.LocationStorageMemory;
import hkust.cse.calendar.notification.NotificationController;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.GroupAppt;
import hkust.cse.calendar.unit.Notification;
import hkust.cse.calendar.unit.TimeSpan;
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
		else{//save to the invite storage
			mInviteStorage.addGroupAppt(tempAppt);
			updateDiskStorage();
			return true;
		}
	}
	
	public boolean saveNewGroupAppt(Appt appt, LinkedList<String> attendList, String ownerID, int hoursBefore, int minutesBefore){
		GroupAppt tempAppt= mInviteStorage.createGroupApptInvite(appt, attendList, ownerID);
		if(tempAppt == null){
			return false;
		}
		else{//save to the invite storage
			mInviteStorage.addGroupAppt(tempAppt);
			//the initiator will have the pending group appt notification temporarily.
			ApptController.getInstance().setNotificationForAppt(tempAppt, 
					UserController.getInstance().getCurrentUser(), hoursBefore, minutesBefore, true);
			updateDiskStorage();
			return true;
		}
	}
	
	public LinkedList<GroupAppt> checkIfUserHasInvite(User user){
		return mInviteStorage.checkIfUserHasInvite(user);
	}
	
	public boolean removeUserFromAllInvite(User user){
		if(mInviteStorage.removeUserFromAllInvite(user)){
			updateDiskStorage();
			return true;
		} else {
			return false;
		}
	}
	public void setResponse(User user, GroupAppt gAppt, boolean response){
		if(response == true){		//when the user accepts
			gAppt.removeWaiting(user.getID());
			if(gAppt.checkAllConfirmed()){		//check if all attendees accepted.
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
		Notification notification = gAppt.getNotification();
		for(String attendee : gAppt.getAttendList()){
			User attendingUser = UserController.getInstance().getUser(attendee);
			GroupAppt tempGroupAppt = new GroupAppt(gAppt);
			if(notification != null){
				ApptController.getInstance().saveNewAppt(attendingUser, tempGroupAppt, true,
						notification.getHoursBefore(), notification.getMinutesBefore());
			}
			else{
				ApptController.getInstance().saveNewAppt(attendingUser, tempGroupAppt);
			}
		}
		//erase notification for pending groupAppt
		if(notification != null)
			NotificationController.getInstance().removeNotification(UserController.getInstance().getUser(gAppt.getOwner()), notification);
		updateDiskStorage();
	}
	
	public boolean checkOverlaps(User user, Appt appt){
		for(GroupAppt gAppt : mInviteStorage.RetrieveGroupApptsInList(user)){
			if(appt.getTimeSpan().Overlap(gAppt.getTimeSpan())){
				System.out.println("Time clash with "+(user.getFullName())
					+gAppt.getTimeSpan());
					return true;
				}
			}
		return false;
	}
			
	public boolean checkOverlaps(GroupAppt gAppt){
		for(String userID : gAppt.getAttendList()){
			User attendee = UserController.getInstance().getUser(userID);
			for(GroupAppt tempGroupAppt : mInviteStorage.RetrieveGroupApptsInList(attendee)){
				if(gAppt.getTimeSpan().Overlap(tempGroupAppt.getTimeSpan())){
					System.out.println("Time clash with "+(attendee.getFullName())
							+gAppt.getTimeSpan());
					return true;
				}
			}
		}
		return false;
	}
	
	
	
	private void updateDiskStorage(){
		if (mInviteStorage instanceof JsonStorable)
			((JsonStorable) mInviteStorage).saveToJson();
	}
}
