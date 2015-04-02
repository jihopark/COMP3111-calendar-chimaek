package hkust.cse.calendar.notification;

import hkust.cse.calendar.time.TimeController;
import hkust.cse.calendar.unit.Notification;
import hkust.cse.calendar.unit.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
			System.out.println("NotificationStorageNullImpl/SaveNotification Notification added. Total " + list.size());
			notificationNumber++;
			return true;
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
	public List<NotificationTime> RetrieveNotification(Date currentTime) {
		ArrayList<NotificationTime> notis = new ArrayList<NotificationTime>();
		for (Notification a : list){ //for each notification
			for(NotificationTime time : a.getTimes()) { //check each alarm
				if (time.getNotificationTime().getTime()/1000 == currentTime.getTime()/1000){
					notis.add(time);
					break;
				}
			}
		}
		return notis;
	}

	@Override
	public LinkedList<NotificationTime> RetrieveAllNotificationTimes(Notification notification) {
		LinkedList<NotificationTime> valueToReturn = new LinkedList<NotificationTime>();
		for (NotificationTime d : notification.getTimes()){
			valueToReturn.add(d);
		}
		return valueToReturn;
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

