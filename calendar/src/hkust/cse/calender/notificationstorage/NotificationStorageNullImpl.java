package hkust.cse.calender.notificationstorage;

import java.util.LinkedList;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.Notification;
import hkust.cse.calendar.unit.User;

public class NotificationStorageNullImpl extends NotificationStorage {
	
	private User defaultUser = null;
	private LinkedList<Notification> list;
	private int notificationNumber = 0;
	
	public NotificationStorageNullImpl(User user)
	{
		defaultUser = user;
		list = new LinkedList<Notification>();
	}

	@Override
	public boolean SaveNotification(Notification notification) { 
		if(notificationIsValid(notification)) {
			list.add(notification);
			notificationNumber++;
			return false;
		}
		else {
			return false;
		}
	}


	@Override
	public boolean RemoveNotification(Notification notification) {
		for (Notification a : list){
			if (a.equals(notification) && notificationIsValid(notification)) {
				list.remove(a);
				notificationNumber--;
				return true;
			}
		}
		return false;
	}

	@Override
	public Notification RetrieveNotification(int notificationID) {
		for (Notification a : list){
			if (a.getID() == notificationID){
				return a;
			}
		}
		//if can't find location in the list
		return null;
	}
	
	@Override
	public boolean UpdateNotification(Notification notification) {
		for (Notification a : list){
			if (a.equals(notification)){
				list.remove(a);
				list.add(notification);
				return true;
			}
		}
		return false;
	}
		
	
	
	//temp functions
	private boolean notificationIsValid(Notification notification) {
		// TODO Auto-generated method stub
		return true;
	}

}

