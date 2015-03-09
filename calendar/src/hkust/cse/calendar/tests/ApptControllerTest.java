package hkust.cse.calendar.tests;

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
	User defaultUser = new User("user","user");
	
	@Before
	public void initiateApptController(){
		ApptController.getInstance().initApptStorage(new ApptStorageMemory(defaultUser));
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
		tmpList = ApptController.getInstance().getRepeatedApptList(appt, ApptController.DAILY, 
				new Date(TimeController.getInstance().getCurrentTimeInMillis()+TimeController.ONE_HOUR*24*randomRepeatNumber));
		printListByIteration(tmpList.get(0));
		assertTrue(randomRepeatNumber + " Days Daily", tmpList.size()==randomRepeatNumber);
		//Weekly
		tmpList = ApptController.getInstance().getRepeatedApptList(appt, ApptController.WEEKLY, 
				new Date(TimeController.getInstance().getCurrentTimeInMillis()+TimeController.ONE_HOUR*24*7*randomRepeatNumber));
		printListByIteration(tmpList.get(0));
		assertTrue(randomRepeatNumber + " Days Weekly", tmpList.size()==randomRepeatNumber);
		//Monthly
		tmpList = ApptController.getInstance().getRepeatedApptList(appt, ApptController.MONTHLY, 
				new Date(TimeController.getInstance().getCurrentTimeInMillis()+TimeController.ONE_HOUR*24*30*randomRepeatNumber));
		printListByIteration(tmpList.get(0));
		assertTrue(randomRepeatNumber + " Days Monthly", tmpList.size()==randomRepeatNumber);
	}
	
	private void printListByIteration(Appt appt){
		System.out.println("\n------------ Start of List ----------------------");
		while (appt!=null){
			System.out.println("\n"+appt);
			appt = appt.getNextRepeatedAppt();
		}
	}

}
