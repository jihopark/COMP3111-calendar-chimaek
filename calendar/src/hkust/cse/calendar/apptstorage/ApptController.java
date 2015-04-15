package hkust.cse.calendar.apptstorage;

import hkust.cse.calendar.diskstorage.JsonStorable;
import hkust.cse.calendar.notification.NotificationController;
import hkust.cse.calendar.time.TimeController;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Notification;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ApptController {

	/* Applying Singleton Structure */
	private static ApptController instance = null;
	private static int apptIDCount = 1;
	public static final int DAILY = 1, WEEKLY = 2, MONTHLY = 3;
	@Deprecated
	public final static int REMOVE = 1;
	@Deprecated	
	public final static int MODIFY = 2;
	@Deprecated	
	public final static int NEW = 3;

	/* The Appt storage */
	private static ApptStorage mApptStorage = null;

	/* Empty Constructor, since in singleton getInstance() is used instead*/
	public ApptController() {

	}

	public static ApptController getInstance(){
		if (instance == null){
			instance = new ApptController();
		}
		return instance;
	}

	//Initialize mApptStorage. Returns false if ApptStorage object already exists
	public boolean initApptStorage(ApptStorage storage){
		if (mApptStorage == null){
			mApptStorage = storage;
			if (mApptStorage instanceof JsonStorable && mApptStorage instanceof ApptStorageMemory){
				mApptStorage = (ApptStorageMemory) ((JsonStorable)mApptStorage).loadFromJson();
				if (mApptStorage == null) mApptStorage = storage;
			}
			return true;
		}
		return false;
	}


	/* Retrieve the Appt's in the storage for a specific user within the specific time span */
	@Deprecated
	public Appt[] RetrieveAppts(User entity, TimeSpan time) {
		return mApptStorage.RetrieveAppts(entity, time);
	}

	public List<Appt> RetrieveApptsInList(User user, TimeSpan time){
		return mApptStorage.RetrieveApptsInList(user, time);
	}

	public Appt RetrieveAppt(int apptID) {
		return mApptStorage.RetrieveAppts(apptID);
	}

	/* Manage the Appt in the storage
	 * parameters: the Appt involved, the action to take on the Appt */
	@Deprecated
	/*public void ManageAppt(Appt appt, int action) {

		if (action == NEW) {				// Save the Appt into the storage if it is new and non-null
			if (appt == null)
				return;
			mApptStorage.SaveAppt(appt);
		} else if (action == MODIFY) {		// Update the Appt in the storage if it is modified and non-null
			if (appt == null)
				return;
			//		mApptStorage.UpdateAppt(appt);
		} else if (action == REMOVE) {		// Remove the Appt from the storage if it should be removed
			mApptStorage.RemoveAppt(appt);
		} 
	}*/

	//Save Appt with Notification
	public boolean saveNewAppt(User user, Appt appt, 
			boolean flagOne, boolean flagTwo, boolean flagThree, boolean flagFour){
		appt.setID(apptIDCount++);
		if (flagOne || flagTwo || flagThree || flagFour)
			setNotificationForAppt(appt, flagOne, flagTwo, flagThree, flagFour);
		boolean tmp = mApptStorage.SaveAppt(user, appt);
		
		
		if (tmp) updateDiskStorage();
		
		return tmp;
	}

	//Register appt as New Appt of user. Return true if successfully registered
	public boolean saveNewAppt(User user, Appt appt){
		appt.setID(apptIDCount++);
		boolean tmp = mApptStorage.SaveAppt(user, appt);

		if (tmp) updateDiskStorage();
		return tmp;
	}
	
	//Save Repeated Appt with Notification
	public boolean saveRepeatedNewAppt(User user, Appt appt, Date repeatEndDate, 
			boolean flagOne, boolean flagTwo, boolean flagThree, boolean flagFour){
		List<Appt> tmpList;
		tmpList = getRepeatedApptList(appt, repeatEndDate);
		if (mApptStorage.checkOverlaps(user, tmpList))
			return false;
		for (Appt a : tmpList){
			if (!saveNewAppt(user, a))
				return false;
			if (flagOne || flagTwo || flagThree || flagFour)
				setNotificationForAppt(a, flagOne, flagTwo, flagThree, flagFour);
		}
		linkRepeatedAppt(tmpList);
		updateDiskStorage();
		return true;
	}
	
	public boolean saveRepeatedNewAppt(User user, Appt appt, Date repeatEndDate){
		List<Appt> tmpList;
		tmpList = getRepeatedApptList(appt, repeatEndDate);
		if (mApptStorage.checkOverlaps(user, tmpList))
			return false;
		Appt tmp = null;
		for (Appt a : tmpList){
			if (!saveNewAppt(user, a))
				return false;
		}
		
		linkRepeatedAppt(tmpList);
		updateDiskStorage();
		return true;
	}
	
	private void linkRepeatedAppt(List<Appt> list){
		Appt tmp = null;
		for (Appt a : list){
			if (tmp!=null){
				a.setPreviousRepeatedAppt(tmp);
				tmp.setNextRepeatedAppt(a);
			}
			tmp = a;
		}
	}


	public List<Appt> getRepeatedApptList(Appt appt, Date repeatEndDate){
		ArrayList<Appt> list = new ArrayList<Appt>();
		Calendar startTime = Calendar.getInstance();
		Calendar endTime = Calendar.getInstance();
		startTime.setTime(new Date(appt.getTimeSpan().StartTime().getTime()));
		endTime.setTime(new Date(appt.getTimeSpan().EndTime().getTime()));

		Appt i = new Appt(appt);
		list.add(i);

		Appt j = new Appt(appt);
		while(true){
			if (appt.getRepeatType() == DAILY){
				startTime.add(Calendar.DATE, 1);
				endTime.add(Calendar.DATE, 1);
			}
			else if (appt.getRepeatType() == WEEKLY){
				startTime.add(Calendar.DATE, 7);
				endTime.add(Calendar.DATE, 7);
			}
			else if (appt.getRepeatType() == MONTHLY){
				startTime.add(Calendar.MONTH, 1);
				endTime.add(Calendar.MONTH, 1);
			}
			if (endTime.getTime().after(repeatEndDate))
				break;
			j.setTimeSpan(new TimeSpan(startTime.getTimeInMillis(), endTime.getTimeInMillis()));
			list.add(j);
			i = j;
			j = new Appt(i);
		}
		return list;
	}

	//Modify appt of user. Return true if successfully modified
	/*
	public boolean modifyAppt(User user, Appt appt){
		if (!TimeController.getInstance().isNotPast(appt)){
			return false;
		}
		//Remove Appt First
		List<Boolean> tmp = appt.getNotification().getFlags();
		if (removeAppt(user, appt)){
			return saveNewAppt(user, appt);
		}

		return false;
	}*/
	
	public boolean modifyAppt(User user, Appt appt, 
			boolean flagOne, boolean flagTwo, boolean flagThree, boolean flagFour){
		if (!TimeController.getInstance().isNotPast(appt)){
			return false;
		}
		//Remove Appt First
		if (removeAppt(user, appt)){
			if (flagOne || flagTwo || flagThree || flagFour)
				return saveNewAppt(user, appt,flagOne, flagTwo, flagThree, flagFour);
			else 
				return saveNewAppt(user, appt);
		}

		return false;
	}
	
	/*public boolean modifyRepeatedNewAppt(User user, Appt appt, Date repeatEndDate,
			boolean flagOne, boolean flagTwo, boolean flagThree, boolean flagFour){
		//If repeated, then modify all repeated appts. However, past appts will not be modified
		if (!TimeController.getInstance().isNotPast(appt)){
			return false;
		}
		//Remove Appt First
		removeAppt(user, appt);

		//Save Modified Appt
		if (flagOne || flagTwo || flagThree || flagFour)
			return saveRepeatedNewAppt(user, appt, repeatEndDate, flagOne, flagTwo, flagThree, flagFour);
		return saveRepeatedNewAppt(user, appt, repeatEndDate);		
	}*/

	public boolean modifyRepeatedNewAppt(User user, Appt appt, Date repeatEndDate,
			boolean flagOne, boolean flagTwo, boolean flagThree, boolean flagFour){
		//If repeated, then modify all repeated appts. However, past appts will not be modified
		if (!TimeController.getInstance().isNotPast(appt)){
			return false;
		}
		//Remove Appt First
		removeAppt(user, appt);

		//Save Modified Appt
		if (flagOne || flagTwo || flagThree || flagFour)
			return saveRepeatedNewAppt(user, appt, repeatEndDate, flagOne, flagTwo, flagThree, flagFour);
		return saveRepeatedNewAppt(user, appt, repeatEndDate);		
	}

	//Remove appt of user. Return true if successfully removed
	public boolean removeAppt(User user, Appt appt){
		//If repeated, then remove all repeated appts. However, past appts will not be removed
		if (!TimeController.getInstance().isNotPast(appt)){
			return false;
		}
		appt.getLocation().subtractCountForLocation();
		if (appt.isRepeated()){
			System.out.println("ApptController/removeAppt Remove Repeated!");
			Appt iterator = appt.getNextRepeatedAppt();
			while (iterator!=null){
				System.out.println("ApptController/removeAppt Remove #" +iterator.getID());
				mApptStorage.RemoveAppt(user, iterator);
				iterator = iterator.getNextRepeatedAppt();
			}
			iterator = appt.getPreviousRepeatedAppt();
			while (iterator!=null){
				if (!TimeController.getInstance().isNotPast(iterator))
					break;
				System.out.println("ApptController/removeAppt Remove #" +iterator.getID());
				mApptStorage.RemoveAppt(user, iterator);
				iterator = iterator.getPreviousRepeatedAppt();
			}
			mApptStorage.RemoveAppt(user, appt);
			updateDiskStorage();
			return true;
		}
		boolean tmp = mApptStorage.RemoveAppt(user, appt);
		updateDiskStorage();
		return tmp;
	}
	
	private void updateDiskStorage(){
		if (mApptStorage instanceof JsonStorable)
			((JsonStorable) mApptStorage).saveToJson();
	}

	public boolean setNotificationForAppt(Appt appt, 
			boolean flagOne, boolean flagTwo, boolean flagThree, boolean flagFour){
		System.out.println("ApptController/setNotificationForAppt Notification For " + appt.TimeSpan());
		Notification noti = new Notification(appt, appt.getTitle(), appt.getTimeSpan().StartTime(),
				flagOne, flagTwo, flagThree, flagFour);
		boolean tmp = NotificationController.getInstance().saveNewNotification(noti);
		if (tmp)
			appt.setNotification(noti);
		return tmp;
	}

	/* Get the defaultUser of mApptStorage */
	public User getDefaultUser() {
		return mApptStorage.getDefaultUser();
	}
}
