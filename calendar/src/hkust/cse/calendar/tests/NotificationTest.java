package hkust.cse.calendar.tests;

import static org.junit.Assert.*;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.Notification;

import org.junit.Test;

public class NotificationTest {

	@Test
	public void notificationShouldHaveRequiredFields() {
		Notification notification = new Notification("notification",15);
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
