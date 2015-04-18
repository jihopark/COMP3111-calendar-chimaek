package hkust.cse.calendar.notification;

import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.Notification;
import hkust.cse.calendar.unit.User;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public abstract class NotificationStorage {
	
	public NotificationStorage() {	//default constructor
	}
	
	public abstract boolean SaveNotification(User user, Notification notification);
	
	public abstract boolean RemoveNotification(User user, Notification notification);
	
	public abstract boolean UpdateNotification(User user, Notification notification);

	public abstract Notification RetrieveNotification(int notificationID);

	//public abstract List<NotificationTime> RetrieveAllNotificationTimes(Notification notification);
	
	public abstract int getIDCount();
	
	public abstract List<NotificationTime> RetrieveNotificationAtCurrentTime(User user, Date currentTime);

	public abstract NotificationTime RetrieveNotificationTime(Notification notification);

}
