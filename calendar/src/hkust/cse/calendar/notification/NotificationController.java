package hkust.cse.calendar.notification;

import hkust.cse.calendar.diskstorage.JsonStorable;
import hkust.cse.calendar.unit.Notification;
import hkust.cse.calendar.unit.User;

import java.util.Date;
import java.util.List;

public class NotificationController {

		//Singleton Structure
		private static NotificationController instance = null;
		
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
				if (mNotificationStorage instanceof JsonStorable && mNotificationStorage instanceof NotificationStorageMemory){
					mNotificationStorage = (NotificationStorageMemory) ((JsonStorable)mNotificationStorage).loadFromJson();
					if (mNotificationStorage == null) mNotificationStorage = storage; 
				}
					
				return true;
			}
			return false;
		}
		
		
		//Retrieve
		public Notification retrieveNotification(int notificationID) {
			return mNotificationStorage.RetrieveNotification(notificationID);
		}
		
		//Retrieve Notification with currentTime. returns empty arraylist if no notification

		public List<NotificationTime> retrieveNotification(User user, Date currentTime) {
			return mNotificationStorage.RetrieveNotificationAtCurrentTime(user, currentTime);
		}
		
		/*
		@Deprecated
		public List<NotificationTime> retrieveAllNotificationTimes(Notification notification){
			return mNotificationStorage.RetrieveAllNotificationTimes(notification);
		}*/
		
		public NotificationTime retrieveNotificationTime(Notification notification){
			return mNotificationStorage.RetrieveNotificationTime(notification);
		}
		
		//Update
		public boolean updateNotification(User user, Notification notification){
			return mNotificationStorage.UpdateNotification(user, notification);
		}
		
		//Save New
		public boolean saveNewNotification(User user, Notification notification){
			notification.setID(mNotificationStorage.getIDCount());
			System.out.println("NotificationController/saveNewNotification Saved. ID is " + notification.getID());
			return mNotificationStorage.SaveNotification(user, notification);
		}
		
		//remove
		public boolean removeNotification(User user, Notification notification){
			System.out.println("NotificationController/removeNewNotification Removed");
			return mNotificationStorage.RemoveNotification(user, notification);
		}
		
		public Notification getNotificationByID(int id){
			return mNotificationStorage.RetrieveNotification(id);
		}
}
