package hkust.cse.calendar.notification;

import hkust.cse.calendar.diskstorage.JsonStorable;
import hkust.cse.calendar.unit.Notification;
import hkust.cse.calendar.unit.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class NotificationStorageNullImpl extends NotificationStorage implements JsonStorable {
	
	//private LinkedList<Notification> list;
	private int notificationNumber = 0;
	private HashMap<String, LinkedList<Notification>> notifications;
	
	public NotificationStorageNullImpl(User user)
	{
		notifications = new HashMap<String, LinkedList<Notification>>();
		//list = new LinkedList<Notification>();
	}

	@Override
	public boolean SaveNotification(User user, Notification notification) { 
		if(notificationIsValid(notification)){
			if (notifications.get(user.toString()) == null)
				notifications.put(user.toString(), new LinkedList<Notification>());
			notifications.get(user.toString()).add(notification);
			System.out.println("NotificationStorageNullImpl/SaveNotification Notification added.");
			notificationNumber++;
			return true;
		}
		else {
			return false;
		}
	}


	@Override
	public boolean RemoveNotification(User user, Notification notification) {
		for (Notification a : notifications.get(user.toString())){
			if (a.equals(notification) && notificationIsValid(notification)) {
				notifications.get(user.toString()).remove(a);
				notificationNumber--;
				return true;
			}
		}
		return false;
	}

	@Override
	public Notification RetrieveNotification(int notificationID) {
		for (String key : notifications.keySet()){
			for (Notification a : notifications.get(key)){
				if (a.getID() == notificationID){
					System.out.println("NotificationStorageNullImpl/RetriveNotification ID is " + a.getID());
					return a;
				}
			}
		}
		//if can't find location in the list
		return null;
	}
	
	@Override
	public List<NotificationTime> RetrieveNotification(User user, Date currentTime) {
		ArrayList<NotificationTime> notis = new ArrayList<NotificationTime>();
		if (notifications.get(user.toString()) == null)
			return notis;
		for (Notification a : notifications.get(user.toString())){ //for each notification
			for(NotificationTime time : a.getTimes()) { //check each alarm
				if (time!=null && time.getNotificationTime().getTime()/1000 == currentTime.getTime()/1000){
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
	public boolean UpdateNotification(User user, Notification notification) {
		for (Notification a : notifications.get(user.toString())){
			if (a.equals(notification)){
				notifications.get(user.toString()).remove(a);
				notifications.get(user.toString()).add(notification);
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
	
	/*
	 * For Disk Storage
	 * */
	
	public String getFileName(){
		return "Notification.txt";
	}
	
	public Object loadFromJson(){
		return null;
	}
	
	public void saveToJson(){
		
	}

}

