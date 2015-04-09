package hkust.cse.calendar.notification;

import hkust.cse.calendar.unit.Notification;
import hkust.cse.calendar.unit.User;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
		
		//Retrieve Notification with currentTime. returns empty arraylist if no notification
		public List<NotificationTime> retrieveNotification(Date currentTime) {
			return mNotificationStorage.RetrieveNotification(currentTime);
		}
		
		public List<NotificationTime> retrieveAllNotificationTimes(Notification notification){
			return mNotificationStorage.RetrieveAllNotificationTimes(notification);
		}
		
		//Update
		public boolean updateNotification(User user, Notification notification){
			return mNotificationStorage.UpdateNotification(notification);
		}
		
		//Save New
		public boolean saveNewNotification(Notification notification){
			notification.setID(notificationIDCount++);
			System.out.println("NotificationController/saveNewNotification Saved. ID is " + notification.getID());
			return mNotificationStorage.SaveNotification(notification);
		}
		
		//remove
		public boolean removeNotification(Notification notification){
			System.out.println("NotificationController/removeNewNotification Removed");
			return mNotificationStorage.RemoveNotification(notification);
		}
		
		public Notification getNotificationByID(int id){
			return mNotificationStorage.RetrieveNotification(id);
		}
}
