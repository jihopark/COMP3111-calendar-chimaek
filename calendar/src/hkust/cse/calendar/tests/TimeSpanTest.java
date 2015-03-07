package hkust.cse.calendar.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import hkust.cse.calendar.time.TimeController;
import hkust.cse.calendar.unit.TimeSpan;

import java.sql.Timestamp;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

public class TimeSpanTest {

	private TimeSpan randomTimeSpan;
	private long randomLength;


	@Before
	public void initizlizeRandomTimeSpan(){
		Timestamp randomStartTime = new Timestamp(TimeController.getInstance().getCurrentTimeInMillis() - 6*TimeController.ONE_HOUR + (long)(12*TimeController.ONE_HOUR*Math.random()));

		randomLength = TimeController.FIFTEEN_MINS + (long)((6*TimeController.ONE_HOUR-TimeController.FIFTEEN_MINS)*Math.random());
		System.out.println("\nrandomLength is " + randomLength/1000/60 + " in mins");

		randomTimeSpan = new TimeSpan(randomStartTime, 
				new Timestamp(randomStartTime.getTime() + randomLength));

		System.out.println("randomTimeSpan is " + randomTimeSpan);

	}

	/*
	 * Five Timespan Test Cases
	 * 1. Second Timespan inside First Timespan => Should Return true
	 * 2. Second Timespan EndTime inside First Appt Timespan => Should Return true
	 * 3. Second Timespan StartTime inside First Appt Timespan => Should Return true
	 * 4. Second Timespan before First Timespan => Should return false
	 * 5. Second Timespan after First Timespan => Should return false
	 * 
	 * */
	@Test
	public void shouldCheckWhetherTimespanOverlap() {
		TimeSpan secondTimeSpan = getOverlapFirstTestCase();
		System.out.println("1. Second Timespan is " + secondTimeSpan);	
		assertTrue("Second Timespan inside First Timespan", randomTimeSpan.Overlap(secondTimeSpan));
		
		secondTimeSpan = getOverlapSecondTestCase();		
		System.out.println("2. Second Timespan is " + secondTimeSpan);	
		assertTrue("Second Timespan EndTime inside First Appt Timespan", randomTimeSpan.Overlap(secondTimeSpan));
		
		secondTimeSpan = getOverlapThirdTestCase();		
		System.out.println("3. Second Timespan is " + secondTimeSpan);	
		assertTrue("Second Timespan StartTime inside First Appt Timespan", randomTimeSpan.Overlap(secondTimeSpan));
		
		secondTimeSpan = getOverlapFourthTestCase();		
		System.out.println("4. Second Timespan is " + secondTimeSpan);	
		assertFalse("Second Timespan before First Timespan", randomTimeSpan.Overlap(secondTimeSpan));
		
		secondTimeSpan = getOverlapFifthTestCase();		
		System.out.println("5. Second Timespan is " + secondTimeSpan);	
		assertFalse("Second Timespan after First Timespan", randomTimeSpan.Overlap(secondTimeSpan));
	}

	private TimeSpan getOverlapFirstTestCase(){
		//Randomly assign two timestamp inside first timespan
		Timestamp randomStartTime = new Timestamp(randomTimeSpan.StartTime().getTime() + (long)(randomLength*Math.random()));
		Timestamp randomEndTime = new Timestamp(randomTimeSpan.StartTime().getTime() + (long)(randomLength*Math.random()));

		//Swap Timestamp if randomStartTime is later than randomEndTime
		if (randomStartTime.getTime() > randomEndTime.getTime()){
			Timestamp tmp = randomStartTime;
			randomStartTime = randomEndTime;
			randomEndTime = tmp;
		}
		return new TimeSpan(randomStartTime, randomEndTime);
	}
	
	private TimeSpan getOverlapSecondTestCase(){
		//Randomly assign end timestamp inside first timespan
		Timestamp randomEndTime = new Timestamp(randomTimeSpan.StartTime().getTime() + (long)(randomLength*Math.random()));
		//Randomly assign start timestamp outside first timespan
		Timestamp randomStartTime = new Timestamp(randomTimeSpan.StartTime().getTime() - (long)(randomLength*Math.random()));
		
		return new TimeSpan(randomStartTime, randomEndTime);
	}
	
	private TimeSpan getOverlapThirdTestCase(){
		//Randomly assign start timestamp inside first timespan
		Timestamp randomStartTime = new Timestamp(randomTimeSpan.StartTime().getTime() + (long)(randomLength*Math.random()));
		//Randomly assign end timestamp outside first timespan
		Timestamp randomEndTime = new Timestamp(randomTimeSpan.StartTime().getTime() + randomLength + (long)(randomLength*Math.random()));
		
		return new TimeSpan(randomStartTime, randomEndTime);
	}
	
	private TimeSpan getOverlapFourthTestCase(){
		//Get Timespan inside the first timespan and then move it earlier than the first timespan
		TimeSpan tmp = getOverlapFirstTestCase();
		return new TimeSpan(new Timestamp(tmp.StartTime().getTime()-randomLength),
								new Timestamp(tmp.EndTime().getTime()-randomLength));
		
	}
	
	private TimeSpan getOverlapFifthTestCase(){
		//Get Timespan inside the first timespan and then move it later than the first timespan
		TimeSpan tmp = getOverlapFirstTestCase();
		return new TimeSpan(new Timestamp(tmp.StartTime().getTime() + randomLength),
								new Timestamp(tmp.EndTime().getTime() + randomLength));
		
	}

	@Test
	public void shouldCalculateTimeSpanLength(){
		assertTrue("randomLength " + randomLength + " Should be Equal to " + randomTimeSpan.TimeLength(), 
					randomLength == randomTimeSpan.TimeLength());
	}
}
