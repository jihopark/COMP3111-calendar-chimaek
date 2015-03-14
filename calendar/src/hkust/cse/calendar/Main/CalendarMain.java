//main method, code starts here
package hkust.cse.calendar.Main;


import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.UIManager;

import hkust.cse.calendar.apptstorage.ApptController;
import hkust.cse.calendar.apptstorage.ApptStorageMemory;
import hkust.cse.calendar.gui.LoginDialog;
import hkust.cse.calendar.locationstorage.LocationController;
import hkust.cse.calendar.locationstorage.LocationStorageNullImpl;
import hkust.cse.calendar.unit.Notification;
import hkust.cse.calendar.unit.User;
import hkust.cse.calender.notificationstorage.NotificationController;
import hkust.cse.calender.notificationstorage.NotificationStorageNullImpl;


public class CalendarMain {
	public static boolean logOut = false;
	public static Date currentTime = new Date();
	public static User user = new User( "noname", "nopass");
	public static void main(String[] args) {
		while(true){
			logOut = false;
			//testForNotification();
			try{
		//	UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			}catch(Exception e){
				
			}
			LoginDialog loginDialog = new LoginDialog();
			while(logOut == false){
				try {
					Thread.sleep(300);
					currentTime = Calendar.getInstance().getTime();
					
					//System.out.println(currentTime);
					
					//////TEMPORARY STATEMENTS//////
					ApptController.getInstance().initApptStorage(new ApptStorageMemory(new User("HELLO","1234")));
					LocationController.getInstance().initLocationStorage(new LocationStorageNullImpl(ApptController.getInstance().getDefaultUser()));
					NotificationController.getInstance().initNotificationStorage(new NotificationStorageNullImpl(ApptController.getInstance().getDefaultUser()));
					////////////////////////////////
					
					if(NotificationController.getInstance().checkForNotificationTime(currentTime)) {
						Notification notification = NotificationController.getInstance().retrieveNotification(currentTime);
						System.out.println("Yes we have an notification at: " + currentTime);
						System.out.println("Notification name is: " + notification.getName());
						
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	
	private static void testForNotification() {
		// only for testing
		NotificationController.getInstance().initNotificationStorage(new NotificationStorageNullImpl(user));
		Notification notification = new Notification();
		notification.setName("Test");
		notification.setID(0);

		//future notification for testing
		Calendar cal = Calendar.getInstance();
		cal.setTime(Calendar.getInstance().getTime());
		cal.add(Calendar.SECOND, 5);
		cal.add(Calendar.HOUR, 1);
		Date Future = cal.getTime();
		
		System.out.println("Future Time is : " + Future);
		
		notification.setAppointmentTime(Future);
		notification.setFlags(true,true,false,false);
		notification.setAlarms();
		NotificationController.getInstance().saveNewNotification(notification);
		for(Date d : NotificationController.getInstance().retrieveAllNotificationTimes(notification)) {
			System.out.println("Saved Notification Times are : " + d);
		}
		LinkedList<Date> alarms = notification.getAlarms();
		for(Date d : alarms) {
			System.out.println("Alarm is set at: " + d);
		}
	}

}
		