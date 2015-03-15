package hkust.cse.calendar.notification;

import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;

import hkust.cse.calendar.apptstorage.ApptController;
import hkust.cse.calendar.apptstorage.ApptStorageMemory;
import hkust.cse.calendar.locationstorage.LocationController;
import hkust.cse.calendar.locationstorage.LocationStorageNullImpl;
import hkust.cse.calendar.time.TimeController;
import hkust.cse.calendar.unit.Notification;
import hkust.cse.calendar.unit.User;

public class NotificationCheckThread extends Thread {

	public static Date currentTime = new Date();
	public void run() {
		while(true) {
			try {
				Thread.sleep(1000);
				currentTime = TimeController.getInstance().getCurrentTimeInDate();
				
				System.out.println("Current Time is: " + currentTime);
				//System.out.println("Current User Name is: " + ApptController.getInstance().gettUser());
				
				if(NotificationController.getInstance().checkForNotificationTime(currentTime)) {
					Notification notification = NotificationController.getInstance().retrieveNotification(currentTime);
					//System.out.println("Yes we have an notification at: " + currentTime);
					//System.out.println("Notification name is: " + notification.getName());
					if (!notification.isDelivered()){
						JOptionPane.showMessageDialog(null, "You have an appointment!\n" + notification.getName() + " at " + notification.getAppt().TimeSpan());
						notification.hasDelivered();
					}
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			
		}
	}
}
