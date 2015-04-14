package hkust.cse.calendar.apptstorage;

import hkust.cse.calendar.diskstorage.JsonStorable;
import hkust.cse.calendar.notification.NotificationController;
import hkust.cse.calendar.time.TimeController;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Notification;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;

public class ApptStorageMemory extends ApptStorage implements JsonStorable {

	private User defaultUser = null;
	
	//private LinkedList<Appt> list;
 	private int apptNumber = 0;
	
	public ApptStorageMemory( User user )
	{
		defaultUser = user;
		mAppts = new HashMap<User, LinkedList<Appt>>();
		//list = new LinkedList<Appt>();
	}
	
	@Override
	public boolean checkOverlaps(User user, Appt appt){
		if (mAppts.get(user)==null)
			return false;
		for (Appt a : mAppts.get(user)){
			if (!a.equals(appt) && a.TimeSpan().Overlap(appt.TimeSpan())){
				System.out.println("\nApptStorageMemory/checkOverlaps: Overlaps!");
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public boolean checkOverlaps(User user, List<Appt> appts){
		for (Appt a : appts){
			if (checkOverlaps(user, a))
				return true;
		}
		return false;
	}
		
	@Override
	public boolean SaveAppt(User user, Appt appt) {
		if (appt!=null && appt.isValid()){
			if (!checkOverlaps(user, appt) && TimeController.getInstance().isNotPast(appt)){
				if (mAppts.get(user)==null){
					mAppts.put(user, new LinkedList<Appt>());
				}
				mAppts.get(user).add(appt);
				apptNumber++;
				System.out.println("ApptStorageMemory/SaveAppt : Saved Appt #"+appt.getID());
				return true;
			}
		}
		System.out.println("ApptStorageMemory/SaveAppt : Failed to Save Appt #"+appt.getID());
		return false;
	}
	
	//Only For Testing Purpose. Don't Use for Real Use
	public boolean SavePastAppt(User user, Appt appt) {
		if (appt!=null && appt.isValid()){
			if (!checkOverlaps(user, appt)){
				if (mAppts.get(user)==null){
					mAppts.put(user, new LinkedList<Appt>());
				}
				mAppts.get(user).add(appt);
				apptNumber++;
				System.out.println("ApptStorageMemory/SavePastAppt : Saved Past Appt #"+appt.getID());
				return true;
			}
		}
		return false;
	}

	@Override
	@Deprecated
	public Appt[] RetrieveAppts(TimeSpan d) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Appt> RetrieveApptsInList(User user, TimeSpan d) {
		ArrayList<Appt> retrieveList = new ArrayList<Appt>();
		
		if (mAppts.get(user) != null){
			for (Appt a : mAppts.get(user)){
				if (a.TimeSpan().Overlap(d)){
					retrieveList.add(a);
	//				System.out.println("ApptStorageMemory/RetrieveApptsInList : Retrive Appt #"+a.getID());
				}
			}
		}
		
		return retrieveList;
	}

	@Override
	@Deprecated	
	public Appt[] RetrieveAppts(User entity, TimeSpan time) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Appt RetrieveAppts(int apptID) {
		System.out.println("ApptStorageMemory/RetrieveAppts Retrieve #" + apptID);
		for (User user : mAppts.keySet()){
			for (Appt a : mAppts.get(user)){
				if (a.getID() == apptID)
					return a;
			}
		}
		System.out.println("ApptStorageMemory/RetrieveAppts Not Found");
		
		return null;
	}

	@Override
	public boolean RemoveAppt(User user, Appt appt) {
		if (mAppts.get(user) == null)
			return false;
		for (Appt a : mAppts.get(user)){
			if (a.equals(appt) && TimeController.getInstance().isNotPast(appt)){
				mAppts.get(user).remove(a);
				apptNumber--;
				System.out.println("ApptStorageMemory/RemoveAppt : Removed Appt #"+appt.getID());
				removeNotification(a.getNotification());
				return true;
			}
		}
		System.out.println("ApptStorageMemory/RemoveAppt : Fail to remove Appt #"+appt.getID());
		return false;
	}
	
	@Override
	public void removeNotification(Notification noti){
		if (noti!=null){
			NotificationController.getInstance().removeNotification(noti);
		}
	}
	
	@Override
	public void saveNotification(Notification noti){
		if (noti!=null){
			NotificationController.getInstance().saveNewNotification(noti);
		}
	}

	@Override
	public User getDefaultUser() {
		return defaultUser;
	}

	public int getTotalApptCount(){
		return apptNumber;
	}
	
	/*
	 * For Disk Storage
	 * */
	
	public String getFileName(){
		return "Appt.txt";
	}
	
	public void loadFromJson(){
		
	}
	
	public void saveToJson(){
		Gson gson = new Gson();
		System.out.println("ApptStorageMemory/saveToJson " +gson.toJson(this));
	}
}
