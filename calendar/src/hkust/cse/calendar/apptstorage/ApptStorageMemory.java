package hkust.cse.calendar.apptstorage;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Notification;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;
import hkust.cse.calender.notificationstorage.NotificationController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class ApptStorageMemory extends ApptStorage {

	private User defaultUser = null;
	
	private LinkedList<Appt> list;
 	private int apptNumber = 0;
	
	public ApptStorageMemory( User user )
	{
		defaultUser = user;
		list = new LinkedList<Appt>();
	}
	
	@Override
	public boolean checkOverlaps(Appt appt){
		for (Appt a : list){
			if (!a.equals(appt) && a.TimeSpan().Overlap(appt.TimeSpan())){
				System.out.println("\nApptStorageMemory/checkOverlaps: Overlaps!");
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public boolean checkOverlaps(List<Appt> appts){
		for (Appt a : appts){
			if (checkOverlaps(a))
				return true;
		}
		return false;
	}
	
	private boolean isNotPast(Appt appt){
		long currentTime = Calendar.getInstance().getTimeInMillis(); 
		if (currentTime <= appt.TimeSpan().StartTime().getTime())
			return true;
		System.out.println("\nApptStorageMemory/isNotPast: Past Time!");
		return false;
	}
	
	@Override
	public boolean SaveAppt(Appt appt) {
		if (appt!=null && appt.isValid()){
			if (!checkOverlaps(appt) && isNotPast(appt)){
				list.add(appt);
				apptNumber++;
				System.out.println("ApptStorageMemory/SaveAppt : Saved Appt #"+appt.getID());
				saveNotification(appt.getNotification());
				//Save Notification if any
				if (appt.getNotification()!=null)
					NotificationController.getInstance().saveNewNotification(appt.getNotification());
				return true;
			}
		}
		System.out.println("ApptStorageMemory/SaveAppt : Failed to Save Appt #"+appt.getID());
		return false;
	}
	
	//Only For Testing Purpose. Don't Use for Real Use
	public boolean SavePastAppt(Appt appt) {
		if (appt!=null && appt.isValid()){
			if (!checkOverlaps(appt)){
				list.add(appt);
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
	public List<Appt> RetrieveApptsInList(TimeSpan d) {
		ArrayList<Appt> retrieveList = new ArrayList<Appt>();
		
		for (Appt a : list){
			if (a.TimeSpan().Overlap(d)){
				retrieveList.add(a);
				System.out.println("ApptStorageMemory/RetrieveApptsInList : Retrive Appt #"+a.getID());
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
	public Appt RetrieveAppts(int joinApptID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean UpdateAppt(Appt appt) {
		for (Appt a : list){
			if (a.equals(appt) && appt.isValid() && isNotPast(appt) && !checkOverlaps(appt)){
				list.remove(a);
				removeNotification(a.getNotification());
				list.add(appt);
				saveNotification(appt.getNotification());
				System.out.println("ApptStorageMemory/UpdateAppt : Updated Appt #"+appt.getID());
				return true;
			}
		}
		System.out.println("ApptStorageMemory/UpdateAppt : Fail to Update Appt #"+appt.getID());
		return false;
	}

	@Override
	public boolean RemoveAppt(Appt appt) {
		for (Appt a : list){
			if (a.equals(appt) && isNotPast(appt)){
				list.remove(a);
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

	@Override
	public void LoadApptFromXml() {
		// TODO Auto-generated method stub
	}
	
	public int getTotalApptCount(){
		return apptNumber;
	}

}
