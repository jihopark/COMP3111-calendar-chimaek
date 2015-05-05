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
		private static NotificationStorage mNotificationStorag = null;
		
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
			if (mNotificationStorag == null){
				mNotificationStorag = storage;
				if (mNotificationStorag instanceof JsonStorable){
					mNotificationStorag = (NotificationStorage) ((JsonStorable)mNotificationStorag).loadFromJson();
					if (mNotificationStorag == null) mNotificationStorag = storage; 
				}
					
				return true;
			}
			return false;
		}
		
		
		//Retrieve
		public Notification retrieveNotification(int notificationID) {
			return mNotificationStorag.RetrieveNotification(notificationID);
		}
		
		public List<Notification> retrieveNotification(User user) {
			return mNotificationStorag.RetrieveNotification(user);
		}
		
		//Retrieve Notification with currentTime. returns empty arraylist if no notification

		public List<NotificationTime> retrieveNotification(User user, Date currentTime) {
			return mNotificationStorag.RetrieveNotificationAtCurrentTime(user, currentTime);
		}
		
		/*
		@Deprecated
		public List<NotificationTime> retrieveAllNotificationTimes(Notification notification){
			return mNotificationStorag.RetrieveAllNotificationTimes(notification);
		}*/
		
		public NotificationTime retrieveNotificationTime(Notification notification){
			return mNotificationStorag.RetrieveNotificationTime(notification);
		}
		
		//Update
		public boolean updateNotification(User user, Notification notification){
			boolean temp = mNotificationStorag.UpdateNotification(user, notification);
			if(temp)
				updateDiskStorage();
			return temp;
		}
		
		//Save New
		public boolean saveNewNotification(User user, Notification notification){
			notification.setID(mNotificationStorag.getIDCount());
			System.out.println("NotificationController/saveNewNotification Saved. ID is " + notification.getID());
			boolean temp = mNotificationStorag.SaveNotification(user, notification);
			if(temp)
				updateDiskStorage();
			return temp;
		}
		
		//remove
		public boolean removeNotification(User user, Notification notification){
			System.out.println("NotificationController/removeNewNotification Removed: " + notification.getID());
			boolean temp = mNotificationStorag.RemoveNotification(user, notification);
			if(temp)
				updateDiskStorage();
			return temp;
		}
		
		public Notification getNotificationByID(int id){
			return mNotificationStorag.RetrieveNotification(id);
		}
		
		public void setDelivered(Notification noti){
			noti.hasDelivered();
			updateDiskStorage();
		}
		
		private void updateDiskStorage(){
			if (mNotificationStorag instanceof JsonStorable)
				((JsonStorable) mNotificationStorag).saveToJson();
		}
}
