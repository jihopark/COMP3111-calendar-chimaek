package hkust.cse.calender.notificationstorage;

import java.util.Date;
import java.util.LinkedList;

import hkust.cse.calendar.locationstorage.LocationStorage;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.Notification;
import hkust.cse.calendar.unit.User;

public class NotificationController {

		//Singleton Structure
		private static NotificationController instance = null;
		private static int notificationIDCount = 1;
		
		//Notification Storage
		private static NotificationStorage mNotificationStorage = null;
		
		//Empty Constructor with getInstasnce
		public NotificationController() {
			
		}
		public static NotificationController getInstance() {
			if (instance == null){
				instance = new NotificationController();
			}
			return instance;
		}
		
		//initialize
		public boolean initNotificationStorage(NotificationStorage storage){
			if (mNotificationStorage == null){
				mNotificationStorage = storage;
				return true;
			}
			return false;
		}
		
		
		//Retrieve
		public Notification retrieveNotification(int notificationID) {
			return mNotificationStorage.RetrieveNotification(notificationID);
		}
		
		//Retrieve Notification with currentTime
		public Notification retrieveNotification(Date currentTime) {
			return mNotificationStorage.RetrieveNotification(currentTime);
		}
		
		public LinkedList<Date> retrieveAllNotificationTimes(Notification notification){
			return mNotificationStorage.RetrieveAllNotificationTimes(notification);
		}
		
		//Update
		public boolean updateNotification(User user, Notification notification){
			return mNotificationStorage.UpdateNotification(notification);
		}
		
		//Save New
		public boolean saveNewNotification(Notification notification){
			notification.setID(notificationIDCount++);
			return mNotificationStorage.SaveNotification(notification);
		}
		
		//remove
		public boolean removeNotification(Notification notification){
			return mNotificationStorage.RemoveNotification(notification);
		}
		//check For notification with given time
		public Boolean checkForNotificationTime(Date currentTime) {
			return mNotificationStorage.CheckForNotificationTime(currentTime);
		}

		
}
