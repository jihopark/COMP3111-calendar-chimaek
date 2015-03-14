package hkust.cse.calendar.tests;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.LinkedList;

import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.Notification;

import org.junit.Test;

public class NotificationTest {

	public LinkedList<Boolean> test = new LinkedList<Boolean>();
	@Test
	public void notificationShouldHaveRequiredFields() {
		Notification notification = new Notification();
		notification.setName("Test");
		notification.setID(0);
		notification.setAppointmentTime(Calendar.getInstance().getTime()); //must set time before alarms
		notification.setFlags(true,true,false,false);
		notification.setAlarms();
		assertFalse("notification does not have required fields", notification.isValid());
	}
	
	@Test
	public void notificationNameShouldNotBeTooLong() {
		fail("Not yet implemented");
	}
	
	@Test
	public void notificationIDShouldNotBeNegetive() {
		fail("Not yet implemented");
	}
	
	@Test
	public void notificationReminderTimeShouldBeStandard() {
		fail("Not yet implemented");
	}
	
}
