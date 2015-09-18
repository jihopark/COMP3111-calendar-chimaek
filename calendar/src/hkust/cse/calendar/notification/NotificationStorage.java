package hkust.cse.calendar.notification;

import hkust.cse.calendar.diskstorage.FileManager;
import hkust.cse.calendar.diskstorage.JsonStorable;
import hkust.cse.calendar.unit.Notification;
import hkust.cse.calendar.unit.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;

public class NotificationStorage implements JsonStorable {
	
	//private LinkedList<Notification> list;
	private int notificationNumber = 0;
	private int notificationIDCount = 1;
	private HashMap<String, LinkedList<Notification>> notifications;
	
	public NotificationStorage(User user)
	{
		notifications = new HashMap<String, LinkedList<Notification>>();
		//list = new LinkedList<Notification>();
	}

	
	public boolean SaveNotification(User user, Notification notification) { 
		if(notificationIsValid(notification)){
			if (notifications.get(user.toString()) == null)
				notifications.put(user.toString(), new LinkedList<Notification>());
			notifications.get(user.toString()).add(notification);
			System.out.println("NotificationStorage/SaveNotification Notification added.");
			notificationNumber++;
			notification.resetNotificationID();
			return true;
		}
		else {
			return false;
		}
	}
	
	
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

	
	public Notification RetrieveNotification(int notificationID) {
		for (String key : notifications.keySet()){
			for (Notification a : notifications.get(key)){
				if (a.getID() == notificationID){
					System.out.println("NotificationStorage/RetriveNotification ID is " + a.getID());
					return a;
				}
			}
		}
		//if can't find location in the list
		System.out.println("Cannot find #" +notificationID);
		return null;
	}
	
	
	public List<NotificationTime> RetrieveNotificationAtCurrentTime(User user, Date currentTime) {
		ArrayList<NotificationTime> notis = new ArrayList<NotificationTime>();
		if (notifications.get(user.toString()) == null)
			return notis;
		for (Notification a : notifications.get(user.toString())){ //for each notification
			NotificationTime tempNotificationObj = a.getNotificationTimeObj();	
			if(tempNotificationObj != null && (tempNotificationObj.getNotificationTime().getTime()/1000 == currentTime.getTime()/1000))
			{	
				System.out.println("Notification Time: " + tempNotificationObj.getNotificationTime().getTime());
				System.out.println("Current Time: " + currentTime.getTime());
				notis.add(tempNotificationObj);
			}
		}
		return notis;
	}

	
	public List<Notification> RetrieveNotification(User user) {
		ArrayList<Notification> notis = new ArrayList<Notification>();
		if (notifications.get(user.toString()) == null)
			return notis;
		for (Notification a : notifications.get(user.toString())){ //for each notification
			notis.add(a);
			System.out.println("NotificationStorageMemory/RetrieveNotification:  Notification Retrieved " + a.getAppointmentTime());
		}
		return notis;
	}

	/*
	@Deprecated
	
	public LinkedList<NotificationTime> RetrieveAllNotificationTimes(Notification notification) {
		LinkedList<NotificationTime> valueToReturn = new LinkedList<NotificationTime>();
		for (NotificationTime d : notification.getTimes()){
			valueToReturn.add(d);
		}
		return valueToReturn;
	}
	*/
	
	
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
	
	
	public int getIDCount(){
		return notificationIDCount++;
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
		return "DISK_NOTIFICATION.txt";
	}
	
	public Object loadFromJson(){
		Gson gson = new Gson();
		String json = FileManager.getInstance().loadFromFile(getFileName());
		if (json.equals("")) return null;
		return gson.fromJson(json, NotificationStorage.class);
	}
	
	public void saveToJson(){
		Gson gson = new Gson();
		FileManager.getInstance().writeToFile(gson.toJson(this), getFileName());
	}

	
	public NotificationTime RetrieveNotificationTime(Notification notification) {
		// TODO Auto-generated method stub
		return null;
	}

}

