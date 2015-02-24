package hkust.cse.calendar.tests;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.sql.Timestamp;
import org.junit.Test;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.TimeSpan;
/*
 * This Test Case is for Unit Testing of Appt class
 * */
public class ApptTest {
	private static final long FIFTEEN_MINS = 15*60*1000;
	/*
	 * Appt must have Title, ID, Timespan
	 * */
	@Test
	public void apptShouldHaveRequiredFields() {
		Appt appt = new Appt();
		Calendar calendar = Calendar.getInstance();
		Timestamp start = new Timestamp(calendar.getTimeInMillis());
		Timestamp end = new Timestamp(calendar.getTimeInMillis() + FIFTEEN_MINS);
		TimeSpan span = new TimeSpan(start, end);

		appt.setTitle("abcd");
		appt.setID(123);
		appt.setTimeSpan(span);
		
		assertTrue("Has all required fields", appt.isValid());
		
		appt.setTitle(null);
		
		assertFalse("Has No Title", appt.isValid());
		
		appt.setTitle("abcd");
		appt.setID(0);
		
		assertFalse("Has No ID", appt.isValid());
		
		appt.setID(123);
		appt.setTimeSpan(null);
		
		assertFalse("Has No Timespan", appt.isValid());
	}
	
	@Test
	public void apptShouldHaveValidStartTimeAndEndTime(){
		Appt appt = new Appt();
		Calendar calendar = Calendar.getInstance();
		Timestamp start = new Timestamp(calendar.getTimeInMillis());
		Timestamp end = new Timestamp(calendar.getTimeInMillis() + FIFTEEN_MINS);
		
		assertTrue("Has Valid Timespan", appt.setTimeSpan(new TimeSpan(start, end)));
		assertFalse("Has Invalid Timespan. EndTime is earlier than StartTime", 
				appt.setTimeSpan(new TimeSpan(end, start)));
	}
	
	@Test
	public void apptShouldHavePredefinedValidLocation(){
		//fail("Not yet implemented");
	}
	
	@Test
	public void apptReminderShouldBePredefinedTimeFrame(){
		//fail("Not yet implemented");
	}
	
	@Test
	public void apptFrequencyShouldBePredefinedTimeFrame(){
		//fail("Not yet implemented");
	}
}
