package hkust.cse.calendar.notification;

import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.Notification;
import hkust.cse.calendar.unit.User;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public abstract class NotificationStorage {
	public HashMap mNotification;
	public int mAssignedNotificationID;
	
	public NotificationStorage() {	//default constructor
	}
	
	public abstract boolean SaveNotification(Notification notification);
	
	public abstract boolean RemoveNotification(Notification notification);
	
	public abstract boolean UpdateNotification(Notification notification);

	public abstract Notification RetrieveNotification(int notificationID);
	
	public abstract List<NotificationTime> RetrieveNotification(Date currentTime);

	public abstract List<NotificationTime> RetrieveAllNotificationTimes(Notification notification);

}
