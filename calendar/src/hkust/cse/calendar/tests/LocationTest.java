package hkust.cse.calendar.tests;

import static org.junit.Assert.*;
import hkust.cse.calendar.unit.Location;

import org.junit.Test;

public class LocationTest {
	
	@Test
	public void locationShouldHaveRequiredFields() {
		Location location = new Location();
		assertFalse("location does not have required fields", location.isValid());
	}
	
	@Test
	public void locationNameShouldNotBeTooLong() {
		fail("Not yet implemented");
	}
	
	@Test
	public void locationIDShouldNotBeNegetive() {
		fail("Not yet implemented");
	}
	
}
