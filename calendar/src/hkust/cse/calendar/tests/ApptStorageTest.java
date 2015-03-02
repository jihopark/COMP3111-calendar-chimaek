package hkust.cse.calendar.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import hkust.cse.calendar.apptstorage.ApptStorageMemory;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ApptStorageTest {
	private static long currentTime = Calendar.getInstance().getTimeInMillis(); 
	private static final long ONE_HOUR = 60*60*1000;
	private static final long FIFTEEN_MINS = 15*60*1000;

	private static ApptStorageMemory memory = null;


	@Before
	public void initializeApptStorageMemory(){
		if (memory==null)
			memory = new ApptStorageMemory(new User("default","default"));
	}

	@Test
	public void shouldNotSaveOverlappingAppt() {
		//Will not Test here, because overlapping has been throughly tested in TimeSpanTest
	}

	@Test
	public void shouldNotSavePastAppt(){
		assertTrue("Should Save Future Appt", memory.SaveAppt(createRandomFutureAppt()));
		assertFalse("Should Not Save Past Appt", memory.SaveAppt(createRandomPastAppt()));
	}

	@Test
	public void shouldNotModifyPastAppt(){
		assertTrue("Should Modify Future Appt", memory.UpdateAppt(createRandomFutureAppt()));
		assertFalse("Should Not Modify Past Appt", memory.UpdateAppt(createRandomPastAppt()));
	}

	@Test
	public void shouldNotRemovePastAppt(){
		assertTrue("Should Remove Future Appt", memory.RemoveAppt(createRandomFutureAppt()));
		assertFalse("Should Not Remove Past Appt", memory.RemoveAppt(createRandomPastAppt()));
	}

	@Test
	public void shouldRetrieveCorrectAppt(){
		TimeSpan span = createRandom6HourLongTimeSpan();
		List<Appt> apptTestCases = createRandomApptTestCases(span);
		for (Appt appt : apptTestCases)
			memory.SaveAppt(appt);
		
		assertTrue("Should Retrieve Correct Appt", hasSameAppts(apptTestCases, memory.RetrieveApptsInList(span)));
		
		long randomLength = generateRandomLengthLessThanThreeHour();
		TimeSpan front = new TimeSpan(span.EndTime().getTime() + randomLength,span.EndTime().getTime() + 2*randomLength);
		System.out.println("\nshouldRetrieveCorrectAppt : " + "Generated Front" + front);
		
		Appt randomFrontTime = new Appt();
		randomFrontTime.setTimeSpan(front);
		memory.SaveAppt(randomFrontTime);
		
		assertTrue("Should Retrieve Correct Appt Even After Adding Other Appt Outside of Scope", 
				hasSameAppts(apptTestCases, memory.RetrieveApptsInList(span)));		
	}
	
	public boolean hasSameAppts(List<Appt> aList, List<Appt> bList){
		if (aList == null || bList==null)
			return false;
		
		boolean flag = false;
		for (Appt a : aList){
			for (Appt b : bList){
				a.equals(b);
				flag = true;
				break;
			}
			if (!flag)
				return false;
			flag = false;
		}
		return true;
	}
	
	public List<Appt> createRandomApptTestCases(TimeSpan span){
		ArrayList<Appt> list = new ArrayList<Appt>();
		int n = (int) (Math.random()*9)+1;
		ArrayList<Timestamp> timestamps = new ArrayList<Timestamp>();
		
		System.out.println("\ncreateRandomApptTestCases: " + "Creating " + n + " Cases");
		
		//Make n*2 timestamps inside TimeSpan span and sort in ascending order
		for (int i=0; i<n*2; i++)
			timestamps.add(new Timestamp(span.StartTime().getTime() + (long)(span.TimeLength()*Math.random())));
		Collections.sort(timestamps);
		
		for (int i=0; i<n*2; i+=2){
			TimeSpan s = new TimeSpan(timestamps.get(i),timestamps.get(i+1));
			System.out.println("\ncreateRandomApptTestCases: " + "Created " + s );
			Appt a = new Appt();
			a.setTimeSpan(s);
			list.add(a);
		}
		return list;
	}
	
	public TimeSpan createRandom6HourLongTimeSpan(){
		Long randomStartTime = currentTime + (long)(6*ONE_HOUR*Math.random());
		TimeSpan span = new TimeSpan(new Timestamp(randomStartTime),
				new Timestamp(randomStartTime + 6*ONE_HOUR));
		System.out.println("\ncreateRandom6HourLongTimeSpan: " + span);
		return span;
	}

	public Appt createRandomPastAppt(){
		Appt randomPastAppt = new Appt();

		Timestamp randomStartTime = new Timestamp(currentTime - 3*ONE_HOUR + (long)(3*ONE_HOUR*Math.random()));

		TimeSpan randomTimeSpan = new TimeSpan(randomStartTime, 
				new Timestamp(randomStartTime.getTime() + generateRandomLengthLessThanThreeHour()));

		System.out.println("Past randomTimeSpan is " + randomTimeSpan);

		randomPastAppt.setTimeSpan(randomTimeSpan);
		randomPastAppt.setID(1);

		return randomPastAppt;
	}

	public Appt createRandomFutureAppt(){
		Appt randomFutureAppt = new Appt();

		Timestamp randomStartTime = new Timestamp(currentTime + (long)(3*ONE_HOUR*Math.random()));

		TimeSpan randomTimeSpan = new TimeSpan(randomStartTime, 
				new Timestamp(randomStartTime.getTime() + generateRandomLengthLessThanThreeHour()));

		System.out.println("Future randomTimeSpan is " + randomTimeSpan);

		randomFutureAppt.setTimeSpan(randomTimeSpan);
		randomFutureAppt.setID(1);

		return randomFutureAppt;
	}

	public long generateRandomLengthLessThanThreeHour(){
		long randomLength = FIFTEEN_MINS + (long)((3*ONE_HOUR-FIFTEEN_MINS)*Math.random());
		System.out.println("\ngenerateRandomLengthLessThanThreeHour: randomLength is " + randomLength/1000/60 + " in mins");
		return randomLength;
	}

	public Appt createRandomOverlappingEvent(){
		return null;
	}
}	
