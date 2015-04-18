//main method, code starts here
package hkust.cse.calendar.Main;


import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.UIManager;

import hkust.cse.calendar.apptstorage.ApptController;
import hkust.cse.calendar.apptstorage.ApptStorageMemory;
import hkust.cse.calendar.gui.LoginDialog;
import hkust.cse.calendar.gui.TimeMachineDialog;
import hkust.cse.calendar.locationstorage.LocationController;
import hkust.cse.calendar.locationstorage.LocationStorageMemory;
import hkust.cse.calendar.notification.NotificationController;
import hkust.cse.calendar.notification.NotificationStorageMemory;
import hkust.cse.calendar.unit.Notification;
import hkust.cse.calendar.unit.User;


public class CalendarMain {
	public static boolean logOut = false;
	public static void main(String[] args) {
		LoginDialog loginDialog = new LoginDialog();
	}
}
