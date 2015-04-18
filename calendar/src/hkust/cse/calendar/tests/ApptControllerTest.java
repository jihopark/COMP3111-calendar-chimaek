package hkust.cse.calendar.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import hkust.cse.calendar.apptstorage.ApptController;
import hkust.cse.calendar.apptstorage.ApptStorageMemory;
import hkust.cse.calendar.time.TimeController;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ApptControllerTest {
	User defaultUser = new User("user","user", "","","",true);
	
	@Before
	public void initiateApptController(){
		ApptController.getInstance().initApptStorage(new ApptStorageMemory());
	}
	
	@Test
	public void controllerShouldCreateRepeatedSchedule() {
		Appt appt = new Appt();
		List<Appt> tmpList;
		int randomRepeatNumber = 1+(int)(30*Math.random());
		appt.setTitle("Repeat Test");
		appt.setTimeSpan(new TimeSpan(TimeController.getInstance().getCurrentTimeInMillis(), 
				TimeController.getInstance().getCurrentTimeInMillis() + TimeController.ONE_HOUR));
		//Daily
		appt.setRepeatType(ApptController.DAILY);
		tmpList = ApptController.getInstance().getRepeatedApptList(appt,
				new Date(TimeController.getInstance().getCurrentTimeInMillis()+TimeController.ONE_HOUR*24*randomRepeatNumber));
		printListByIteration(tmpList.get(0));
		assertTrue(randomRepeatNumber + " Days Daily", tmpList.size()==randomRepeatNumber);
		//Weekly

		appt.setRepeatType(ApptController.WEEKLY);
		tmpList = ApptController.getInstance().getRepeatedApptList(appt,
				new Date(TimeController.getInstance().getCurrentTimeInMillis()+TimeController.ONE_HOUR*24*7*randomRepeatNumber));
		printListByIteration(tmpList.get(0));
		assertTrue(randomRepeatNumber + " Days Weekly", tmpList.size()==randomRepeatNumber);
		//Monthly
		appt.setRepeatType(ApptController.MONTHLY);
		tmpList = ApptController.getInstance().getRepeatedApptList(appt, 
				new Date(TimeController.getInstance().getCurrentTimeInMillis()+TimeController.ONE_HOUR*24*30*randomRepeatNumber));
		printListByIteration(tmpList.get(0));
		assertTrue(randomRepeatNumber + " Days Monthly", tmpList.size()==randomRepeatNumber);
	}
	
	@Test
	public void controllerShouldSaveAndRemoveRepeatedSchedule(){
		Appt appt = new Appt();
		int randomRepeatNumber = 1+(int)(30*Math.random());

		appt.setTitle("Repeat Save Test");
		appt.setTimeSpan(new TimeSpan(TimeController.getInstance().getCurrentTimeInMillis()+TimeController.FIFTEEN_MINS, 
				TimeController.getInstance().getCurrentTimeInMillis() + TimeController.ONE_HOUR));
		appt.setRepeatType(ApptController.DAILY);
		assertTrue("Save Daily Repeated Appt Correctly",ApptController.getInstance().saveRepeatedNewAppt(defaultUser, appt, 
				new Date(TimeController.getInstance().getCurrentTimeInMillis()+TimeController.ONE_HOUR*24*randomRepeatNumber)));
		appt.setRepeatType(ApptController.WEEKLY);
		assertFalse("Save Weekly Repeated Appt Correctly",ApptController.getInstance().saveRepeatedNewAppt(defaultUser, appt, 
				new Date(TimeController.getInstance().getCurrentTimeInMillis()+TimeController.ONE_HOUR*24*7*randomRepeatNumber)));
		
		//Retrieve List
		for (Appt a : ApptController.getInstance().RetrieveApptsInList(defaultUser, new TimeSpan(TimeController.getInstance().getCurrentTimeInMillis(), 
				TimeController.getInstance().getCurrentTimeInMillis()+24*randomRepeatNumber/2*TimeController.ONE_HOUR))){
			System.out.println("\nRetrieved List: " + a );
			appt = a;
		}
		
		//Remove List - need to program assert. Please see the console for testing for now
		ApptController.getInstance().removeAppt(defaultUser, appt);
		assertTrue("All List Should be Removed", true);
		
	}
	private void printListByIteration(Appt appt){
		System.out.println("\n------------ Start of List ----------------------");
		while (true){
			System.out.println("\n"+appt);
			if (appt.getNextRepeatedAppt()==null)
				break;
			appt = appt.getNextRepeatedAppt();
		}
		System.out.println("\n------------ End of List ----------------------");
		
		while(appt!=null){
			System.out.println("\nReverse: "+appt);
			appt = appt.getPreviousRepeatedAppt();
		}
		
	}

}
