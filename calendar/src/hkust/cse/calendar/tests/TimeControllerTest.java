package hkust.cse.calendar.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import hkust.cse.calendar.time.TimeController;

import java.sql.Timestamp;
import java.util.Date;

import org.junit.Test;

public class TimeControllerTest {

	@Test
	public void ShouldReturnCurrentTime() {
		TimeController.getInstance().disableTimeMachineMode();
		assertFalse("Time Machine Mode is Disabled",TimeController.getInstance().isOnTimeMachineMode());
			
		assertTrue("Current Time In Millis", 
				equalsInMilliSeconds(TimeController.getInstance().getCurrentTimeInMillis(), System.currentTimeMillis()));
		assertTrue("Current Time In Date", 
				equalsInMilliSeconds(TimeController.getInstance().getCurrentTimeInDate().getTime(), new Date(System.currentTimeMillis()).getTime()));
		assertTrue("Current Time In Timestamp", 
				equalsInMilliSeconds(TimeController.getInstance().getCurrentTimeInTimestamp().getTime()
				,new Timestamp(System.currentTimeMillis()).getTime()));
	}
	
	@Test
	public void ShouldTimeMachineBeEnabled(){
		
		//Future
		long randomSeconds = (long) (Math.random()*1000000000); 
		System.out.println(TimeController.dateFormat.format(new Date(randomSeconds + System.currentTimeMillis())));
		TimeController.getInstance().setTimeMachine(new Date(System.currentTimeMillis() + randomSeconds));
		assertTrue("Time Machine Mode is Enabled",TimeController.getInstance().isOnTimeMachineMode());
		assertTrue("Current Time In Millis", 
				equalsInMilliSeconds(TimeController.getInstance().getCurrentTimeInMillis(), System.currentTimeMillis()+randomSeconds));
		assertTrue("Current Time In Date", 
				equalsInMilliSeconds(TimeController.getInstance().getCurrentTimeInDate().getTime(),  new Date(System.currentTimeMillis()+randomSeconds).getTime()));
		assertTrue("Current Time In Timestamp", 
				equalsInMilliSeconds(TimeController.getInstance().getCurrentTimeInTimestamp().getTime(), new Timestamp(System.currentTimeMillis()+randomSeconds).getTime()));
		
		//Past
		randomSeconds = (long) (Math.random()*1000000000); 
		System.out.println(TimeController.dateFormat.format(new Date(System.currentTimeMillis()-randomSeconds)));
		TimeController.getInstance().setTimeMachine(new Date(System.currentTimeMillis() - randomSeconds));
		assertTrue("Current Time In Millis", 
				equalsInMilliSeconds(TimeController.getInstance().getCurrentTimeInMillis(), System.currentTimeMillis()-randomSeconds));
		assertTrue("Current Time In Date", 
				equalsInMilliSeconds(TimeController.getInstance().getCurrentTimeInDate().getTime(),  new Date(System.currentTimeMillis()-randomSeconds).getTime()));
		assertTrue("Current Time In Timestamp", 
				equalsInMilliSeconds(TimeController.getInstance().getCurrentTimeInTimestamp().getTime(), new Timestamp(System.currentTimeMillis()-randomSeconds).getTime()));
		
		TimeController.getInstance().disableTimeMachineMode();
		assertFalse("Time Machine Mode is Disabled",TimeController.getInstance().isOnTimeMachineMode());
		assertTrue("Current Time In Millis", 
				TimeController.getInstance().getCurrentTimeInMillis() == System.currentTimeMillis());
	}
	
	private boolean equalsInMilliSeconds(long a, long b){
		if (Math.abs(a-b) < 10)
			return true;
		return false;
	}

	public static void createAndShowGUI() {
		// TODO Auto-generated method stub
		
	}

}
