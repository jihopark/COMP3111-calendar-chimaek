package hkust.cse.calendar.apptstorage;

import hkust.cse.calendar.diskstorage.JsonStorable;
import hkust.cse.calendar.notification.NotificationController;
import hkust.cse.calendar.time.TimeController;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.GroupAppt;
import hkust.cse.calendar.unit.Notification;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.userstorage.UserController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ApptController {

	/* Applying Singleton Structure */
	private static ApptController instance = null;
	//private static int apptIDCount = 1;
	public static final int DAILY = 1, WEEKLY = 2, MONTHLY = 3;
	@Deprecated
	public final static int REMOVE = 1;
	@Deprecated	
	public final static int MODIFY = 2;
	@Deprecated	
	public final static int NEW = 3;

	/* The Appt storage */
	private static ApptStorage mApptStorage = null;
	private static boolean shouldSave = true;

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
			rollback();
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
	
	public static List<Appt> RetrieveApptsInList(User user) {
		return mApptStorage.RetrieveApptsInList(user);
	}

	public Appt RetrieveAppt(int apptID) {
		return mApptStorage.RetrieveAppts(apptID);
	}
	
	public List<Appt> RetrievePublicApptsInList(User user){
		return mApptStorage.RetrievePublicApptsInList(user);
	}

	/* Manage the Appt in the storage
	 * parameters: the Appt involved, the action to take on the Appt */
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

	/*
	//Save Appt with Notification
	@Deprecated
	public boolean saveNewAppt(User user, Appt appt, 
			boolean flagOne, boolean flagTwo, boolean flagThree, boolean flagFour){
		appt.setID(mApptStorage.getIDCount());
		if (flagOne || flagTwo || flagThree || flagFour)
			setNotificationForAppt(appt, flagOne, flagTwo, flagThree, flagFour);
		boolean tmp = mApptStorage.SaveAppt(user, appt);
		
		
		if (tmp) updateDiskStorage();
		
		return tmp;
	}*/
	
	public boolean saveNewAppt(User user,Appt appt, boolean notificationEnabled,
			int notificationHoursBefore, int notificationMinutesBefore){
		appt.setID(mApptStorage.getIDCount());
		if(notificationEnabled)
			setNotificationForAppt(appt, user, notificationHoursBefore,notificationMinutesBefore);
		boolean tmp = mApptStorage.SaveAppt(user, appt);
		
		if(tmp) updateDiskStorage();
		return tmp;
	}

	//Register appt as New Appt of user. Return true if successfully registered
	public boolean saveNewAppt(User user, Appt appt){
		appt.setID(mApptStorage.getIDCount());
		boolean tmp = mApptStorage.SaveAppt(user, appt);
		
		if (tmp) updateDiskStorage();
		return tmp;
	}
	
	/*
	//Save Repeated Appt with Notification
	@Deprecated
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
	}*/
	
	
	public boolean saveRepeatedNewAppt(User user, Appt appt, Date repeatEndDate, 
			boolean notificationEnabled, int notificationHoursBefore, int notificationMinutesBefore){
		List<Appt> tmpList;
		tmpList = getRepeatedApptList(appt, repeatEndDate);
		if (mApptStorage.checkOverlaps(user,tmpList))
			return false;
		for (Appt a : tmpList){
			if (!saveNewAppt(user, a))
				return false;
			if(notificationEnabled)
				setNotificationForAppt(a, user, notificationHoursBefore,notificationMinutesBefore);
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

	/*
	@Deprecated
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
	}*/
	
	
	public boolean modifyAppt(User user, Appt appt, 
			boolean notificationEnabled, int notificationHoursBefore, int notificationMinutesBefore){
		
		//check if it is group appt
		
		if (!TimeController.getInstance().isNotPast(appt) || mApptStorage.checkOverlaps(user, appt)){
			rollback();
			return false;
		}
		//Remove Appt First
		if (removeAppt(user, appt)){
			if (notificationEnabled)
				return saveNewAppt(user, appt, notificationEnabled,notificationHoursBefore, notificationMinutesBefore);
			else 
				return saveNewAppt(user, appt);
		}

		return false;
	}
	
	public boolean modifyGroupApptWithNotification(GroupAppt gAppt, 
			TimeSpan timeSpanBeforeModified, int hoursBefore, int minutesBefore){
		
		final boolean NOTIFICATION_ON = true;
		
		//FOR ALL ATTENDEE
		for(String attendeeString: gAppt.getAttendList()){
			System.out.println("Changing GroupAppt in: " + attendeeString);
			
			//Get the group appt to be modified from each user.
			User attendee = UserController.getInstance().getUser(attendeeString);
			List<Appt> listContainingGroupApptToBeModified = ApptController.getInstance().RetrieveApptsInList(attendee,
					timeSpanBeforeModified);
			GroupAppt eachUserGroupAppt;
			if(listContainingGroupApptToBeModified.size() <= 1 && listContainingGroupApptToBeModified.size() != 0){
				eachUserGroupAppt = (GroupAppt)listContainingGroupApptToBeModified.get(0);
				//should change eachGroupAppt's attribute to same as gGroupAppt and then save the changes.
				eachUserGroupAppt.copyChangedInfoFrom(gAppt);
				System.out.println("AppScheduler/groupEventButtonResponse: "+ eachUserGroupAppt.getID());
			}
			else{
				System.out.println("Something wrong in modifying group appt !");
				return false;
			}
			
			//modify the group appt.
			if(eachUserGroupAppt != null){
				boolean modifySuccess = ApptController.getInstance().modifyAppt(attendee, eachUserGroupAppt, 
						NOTIFICATION_ON, hoursBefore, minutesBefore);
				if(!modifySuccess){
					return false;
				}
			}
		}//end of for-loop.
		
		return true;
	}
	
	
	public boolean modifyGroupApptWithoutNotification(GroupAppt gAppt, TimeSpan timeSpanBeforeModified){
		
		boolean NOTIFICATION_OFF = false;
		
		for(String attendeeString: gAppt.getAttendList()){
			System.out.println("Changing GroupAppt in: " + attendeeString);
			
			//Get the group appt to be modified from each user.
			User attendee = UserController.getInstance().getUser(attendeeString);
			List<Appt> listContainingGroupApptToBeModified = ApptController.getInstance().RetrieveApptsInList(attendee,
					timeSpanBeforeModified);
			GroupAppt eachUserGroupAppt;
			if(listContainingGroupApptToBeModified.size() <= 1 && listContainingGroupApptToBeModified.size() != 0){
				eachUserGroupAppt = (GroupAppt)listContainingGroupApptToBeModified.get(0);
				//should change eachGroupAppt's attribute to same as currentGroupAppt and then save the changes.
				eachUserGroupAppt.copyChangedInfoFrom(gAppt);
				System.out.println("AppScheduler/groupEventButtonResponse: "+ eachUserGroupAppt.getID());
			}
			else{
				System.out.println("Something wrong in modifying group appt !");
				return false;
			}
			
			//modify the group appt.
			if(eachUserGroupAppt != null){
				boolean modifySuccess = ApptController.getInstance().modifyAppt(attendee, eachUserGroupAppt, 
						NOTIFICATION_OFF, 0, 0);
				if(!modifySuccess){
					return false;
				}
			}
		}//end of for-loop.
	
		return true;
	}
	
	public boolean checkOverlapsForGroupAppt(List<String> attendList, TimeSpan timeSpanBeforeModify,
			TimeSpan timeSpanAfterModify){
		
		for(String userString: attendList){
			System.out.println("CURRENT USER: " + userString);
			TimeSpan tempTimeSpan;		//used to save original time span.
			User attendee = UserController.getInstance().getUser(userString);
			List<Appt> listContainingApptToBeModified = ApptController.getInstance().RetrieveApptsInList(attendee,
					timeSpanBeforeModify);
			GroupAppt eachUserGroupAppt;
			if(!listContainingApptToBeModified.isEmpty()){
				eachUserGroupAppt = (GroupAppt)listContainingApptToBeModified.get(0);
				tempTimeSpan = eachUserGroupAppt.getTimeSpan();
				System.out.println("CURRENT TIME SPAN: " + tempTimeSpan);
				eachUserGroupAppt.setTimeSpan(timeSpanAfterModify);		//change time span just for checking purpose.
				System.out.println("CHANGED TIME SPAN: " + eachUserGroupAppt.TimeSpan());
				boolean temp = mApptStorage.checkOverlaps(attendee, eachUserGroupAppt);
				System.out.println("TEMP: " + temp);
				eachUserGroupAppt.setTimeSpan(tempTimeSpan);	//set the time span back to orignal.
				System.out.println("RETURNED TIME SPAN: " + eachUserGroupAppt.TimeSpan());
				if(temp){	//if overlaps.
					return true;
				}
			}else{
				return true;
			}
		}
		//if no overlap
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

	/*
	@Deprecated
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
	}*/

	public boolean modifyRepeatedNewAppt(User user, Appt appt, Date repeatEndDate,
			boolean notificationEnabled, int notificationHoursBefore, int notificationMinutesBefore){
		//If repeated, then modify all repeated appts. However, past appts will not be modified
		if (!TimeController.getInstance().isNotPast(appt)){
			return false;
		}
		
		//Get the Start of the repeat
		Appt start = appt.getRepeatStartAppt();
		if (start==null) start = appt;
		start.getTimeSpan().setTimeWithoutChangingDay(appt.getTimeSpan());
		start.setLocation(appt.getLocation());
		start.setTitle(appt.getTitle());
		start.setInfo(appt.getInfo());
		start.setRepeatType(appt.getRepeatType());
		
		shouldSave = false;
		//Remove Appts First
		removeAppt(user, appt);
		
		boolean tmp;
		//Save Modified Appt
		if (notificationEnabled)
			tmp = saveRepeatedNewAppt(user, start, repeatEndDate, notificationEnabled,
					notificationHoursBefore, notificationMinutesBefore);
		tmp = saveRepeatedNewAppt(user, start, repeatEndDate);
		shouldSave = true;
		if (!tmp) rollback();
		else updateDiskStorage();
		
		return tmp;
	}
	

	//Remove appt of user. Return true if successfully removed
	public boolean removeAppt(User user, Appt appt){
		//If repeated, then remove all repeated appts. However, past appts will not be removed
		
		// check if group appt
		if (!TimeController.getInstance().isNotPast(appt)){
			return false;
		}

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
	
	//Return non-overlapping timeslot in one day
	public ArrayList<TimeSpan> getSchedulableTimeSpan(List<User> users, Date scheduleDay){
		ArrayList<TimeSpan> timeSlots = new ArrayList<TimeSpan>();
		
		//create timeslots in 15min interval of the whole day
		Date startTime = new Date(scheduleDay.getTime());
		TimeController.getInstance().setHour(startTime, 0);
		TimeController.getInstance().setMinute(startTime, 0);
		TimeController.getInstance().setSecond(startTime, 0);

		Date endTime;
		
		do{
			endTime = new Date(startTime.getTime()+TimeController.FIFTEEN_MINS);
			TimeSpan span = new TimeSpan(startTime.getTime(), endTime.getTime());
			if (TimeController.getInstance().isNotPast(span))
				timeSlots.add(span);
			startTime = new Date(endTime.getTime());
		}while(TimeController.getInstance().getDateFrom(endTime)
				==TimeController.getInstance().getDateFrom(scheduleDay));
		
		//remove all timeslots overlaps with the users existing schedule
		startTime = new Date(scheduleDay.getTime());
		TimeController.getInstance().setHour(startTime, 0);
		TimeController.getInstance().setMinute(startTime, 0);
		TimeController.getInstance().setSecond(startTime, 0);

		endTime = new Date(startTime.getTime() + TimeController.ONE_HOUR*24);
		TimeSpan oneDay = new TimeSpan(startTime.getTime(), endTime.getTime());
		
		for (User user : users){
			//System.out.println("ApptController/getSchedualableTimeSpan Iterating user " + user.toString());
			for (Appt appt : mApptStorage.RetrieveApptsInList(user, oneDay)){
				TimeSpan slot = timeSlots.get(0);
				for (Iterator<TimeSpan> it = timeSlots.iterator(); it.hasNext() ; 
						slot = it.next()){
					if (appt.getTimeSpan().Overlap(slot)){
						//System.out.println("ApptController/getSchedualableTimeSpan Removed " + slot);
						it.remove();						
					}
				}
			}
		}
		//System.out.println("ApptController/getSchedualableTimeSpan Removed " + timeSlots);
		return timeSlots;
	}
	
	
	private void updateDiskStorage(){
		if (mApptStorage instanceof JsonStorable && shouldSave)
			((JsonStorable) mApptStorage).saveToJson();
	}

	/*
	@Deprecated
	public boolean setNotificationForAppt(Appt appt, 
			boolean flagOne, boolean flagTwo, boolean flagThree, boolean flagFour){
		System.out.println("ApptController/setNotificationForAppt Notification For " + appt.TimeSpan());
		Notification noti = new Notification(appt, appt.getTitle(), appt.getTimeSpan().StartTime(),
				flagOne, flagTwo, flagThree, flagFour);
		boolean tmp = NotificationController.getInstance().saveNewNotification(UserController.getInstance().getCurrentUser(), noti);
		if (tmp)
			appt.setNotification(noti);
		return tmp;
	}*/
	
	public boolean setNotificationForAppt(Appt appt, User user, int notificationHoursBefore, int notificationMinutesBefore)
	{
		Notification notification = new Notification(appt,notificationHoursBefore, notificationMinutesBefore);
		boolean tmp = NotificationController.getInstance().saveNewNotification(user, notification);
		if(tmp)
			appt.setNotification(notification);
		return tmp;
	}

	public boolean setNotificationForAppt(Appt appt, User user, int notificationHoursBefore, int notificationMinutesBefore, boolean pending)
	{
		Notification notification = new Notification(appt,notificationHoursBefore, notificationMinutesBefore);
		notification.setPending(pending);
		boolean tmp = NotificationController.getInstance().saveNewNotification(user, notification);
		if(tmp)
			appt.setNotification(notification);
		return tmp;
	}

	
	/* Get the defaultUser of mApptStorage */
	public User getDefaultUser() {
		return UserController.getInstance().getCurrentUser();
	}
	
	public void createNewGroupAppt(){
		//create new group appt
	}
	
	/*
	 * Load ApptStorage Again back from Disk
	 * */
	
	public void rollback(){
		if (mApptStorage instanceof JsonStorable && mApptStorage instanceof ApptStorageMemory){
			ApptStorageMemory tmp = (ApptStorageMemory) ((JsonStorable)mApptStorage).loadFromJson();
			if (tmp != null) mApptStorage = tmp;
		}
	}

}
