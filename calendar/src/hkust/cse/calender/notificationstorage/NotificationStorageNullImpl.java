package hkust.cse.calender.notificationstorage;

import java.util.Calendar;
import java.util.Date;
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
	public Notification RetrieveNotification(Date currentTime) {
		for (Notification a : list){ //for each notification
			for(Date date : a.getAlarms()) { //check each alarm
				Calendar cal1 = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				cal1.setTime(date);
				cal2.setTime(currentTime);
				if(	cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
						cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
						cal1.get(Calendar.HOUR_OF_DAY) == cal2.get(Calendar.HOUR_OF_DAY) &&
						cal1.get(Calendar.MINUTE) == cal2.get(Calendar.MINUTE) &&
						cal1.get(Calendar.SECOND) == cal2.get(Calendar.SECOND))
					{
					return a;
				}
			}
		}
		return null;
	}

	@Override
	public LinkedList<Date> RetrieveAllNotificationTimes(Notification notification) {
		LinkedList<Date> valueToReturn = new LinkedList<Date>();
		for (Date d : notification.getAlarms()){
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
	
	@Override
	public Boolean CheckForNotificationTime(Date currentTime) {
		for (Notification a : list){ //for each notification
			//System.out.println(a);
			for(Date date : a.getAlarms()) { //check each alarm
				//System.out.println(date);
				//System.out.println("current Time is: " + currentTime);
				//System.out.println("difference are:  " + date.compareTo(currentTime));
				Calendar cal1 = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				cal1.setTime(date);
				cal2.setTime(currentTime);
				if(	cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
					cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
					cal1.get(Calendar.HOUR_OF_DAY) == cal2.get(Calendar.HOUR_OF_DAY) &&
					cal1.get(Calendar.MINUTE) == cal2.get(Calendar.MINUTE) &&
					cal1.get(Calendar.SECOND) == cal2.get(Calendar.SECOND))
				{
					//System.out.println("date and Current time equals and returns true at: " + currentTime);
					return true;
				}
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

