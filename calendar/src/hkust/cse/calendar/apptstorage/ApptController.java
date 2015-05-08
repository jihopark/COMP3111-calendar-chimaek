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

	//Initialize mApptStorageMemory. Returns false if ApptStorageMemory object already exists
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
	
	public List<Appt> RetrieveApptsInList(User user) {
		return mApptStorage.RetrieveApptsInList(user);
	}

	public Appt RetrieveAppt(int apptID) {
		return mApptStorage.RetrieveAppts(apptID);
	}
	
	public List<Appt> RetrievePublicApptsInList(User user){
		return mApptStorage.RetrievePublicApptsInList(user);
	}

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
	
	public boolean removeUserFromGroupAppt(GroupAppt gAppt, User user){
		LinkedList<String> attendList = new LinkedList<String>(gAppt.getAttendList());
		LinkedList<String> waitingList = new LinkedList<String>(gAppt.getWaitingList());
		// Case 1) When Deleted user is the owner of the Group Event
		if(user.toString().equals(gAppt.getOwner())) {
			// Delete The Group Event for Individuals
			for(String attendee: attendList) {
				System.out.println("ApptController/removeUserFromGroupAppt: Deleting Group Event " +gAppt.getTitle()+ " from " +attendee+"'s Group Event");
				for(Appt a: ApptController.getInstance().RetrieveApptsInList(UserController.getInstance().getUser(attendee))) {
					if(a instanceof GroupAppt) {
						//System.out.println("Owner is: " +((GroupAppt) a).getOwner()+ " and User is: " +user.toString());						
						if(user.toString().equals(((GroupAppt) a).getOwner())) {
							ApptController.getInstance().removeAppt(UserController.getInstance().getUser(attendee), a);
							System.out.println("ApptController/removeUserFromGroupAppt: Deleted " +a.getTitle()+ " from " +attendee+" Appt Controller");
						}
					}
				}
			}
		// Case 2) When Deleted user is only a participant of the Group Event	
		} else {
		// Delete User from individual participant's group event	
			for(String attendee: attendList) {
				System.out.println("ApptController/removeUserFromGroupAppt: Deleting Particiapant " +user+ " from " +attendee+"'s Group Event");
				for(Appt a: ApptController.getInstance().RetrieveApptsInList(UserController.getInstance().getUser(attendee))) {
					if(a instanceof GroupAppt) {
						((GroupAppt) a).removeAttendant(user.toString());
						((GroupAppt) a).removeWaiting(user.toString());
						System.out.println("ApptController/removeUserFromGroupAppt: Removed " +user.toString() + " from " + a.getTitle()+" Group Event");
					}
				}
			}
		}
		
		
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
		NotificationController.getInstance().setShouldSave(false);
		//Remove Appts First
		removeAppt(user, appt);
		
		boolean tmp;
		//Save Modified Appt
		if (notificationEnabled)
			tmp = saveRepeatedNewAppt(user, start, repeatEndDate, notificationEnabled,
					notificationHoursBefore, notificationMinutesBefore);
		else
			tmp = saveRepeatedNewAppt(user, start, repeatEndDate);
		shouldSave = true;
		NotificationController.getInstance().setShouldSave(true);
		if (!tmp){
			rollback();
			NotificationController.getInstance().rollback();
		}
		else {
			updateDiskStorage();
			NotificationController.getInstance().updateDiskStorage();
		}
		
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
				if (!TimeController.getInstance().isNotPast(iterator)){
					iterator.setNextRepeatedAppt(null);
					break;
				}
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
				for (Iterator<TimeSpan> it = timeSlots.iterator();; 
						slot = it.next()){
					if (appt.getTimeSpan().Overlap(slot)){
						//System.out.println("ApptController/getSchedualableTimeSpan Removed " + slot);
						it.remove();						
					}
					if (!it.hasNext()) break;
				}
			}
		}
		//System.out.println("ApptController/getSchedualableTimeSpan Removed " + timeSlots);
		return timeSlots;
	}

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

	
	/* Get the defaultUser of mApptStorageMemory */
	public User getDefaultUser() {
		return UserController.getInstance().getCurrentUser();
	}
	
	/*
	 * Load ApptStorageMemory Again back from Disk
	 * */
	
	public void updateDiskStorage(){
		if (mApptStorage instanceof JsonStorable && shouldSave)
			((JsonStorable) mApptStorage).saveToJson();
	}
	
	public void rollback(){
		if (mApptStorage instanceof JsonStorable){
			ApptStorage tmp = (ApptStorage) ((JsonStorable)mApptStorage).loadFromJson();
			if (tmp != null) mApptStorage = tmp;
		}
	}


}
