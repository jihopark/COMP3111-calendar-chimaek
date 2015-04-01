package hkust.cse.calendar.time;

import hkust.cse.calendar.unit.Appt;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TimeController {
	
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public static final long FIFTEEN_MINS = 15*60*1000;
	public static final long ONE_HOUR = 60*60*1000;
	
	
	/* Applying Singleton Structure */
	private static TimeController instance = null;
	private boolean onTimeMachineMode = false;
	private Calendar timeMachine = null;
	private long timeMachineDifference;
	
	/* Empty Constructor, since in singleton getInstance() is used instead*/
	public TimeController() {
		
	}
	
	public static TimeController getInstance(){
		if (instance == null){
			instance = new TimeController();
		}
		return instance;
	}
	
	/* Set Time Machine Mode */
	public void setTimeMachine(Date d){
		onTimeMachineMode = true;
		timeMachineDifference = Calendar.getInstance().getTimeInMillis() - d.getTime();
	}
	
	public boolean isOnTimeMachineMode(){
		return onTimeMachineMode;
	}
	
	public void disableTimeMachineMode(){		
		onTimeMachineMode = false;
	}
	public void enableTimeMachineMode(){		
		onTimeMachineMode = true;
	}
	
	public long getCurrentTimeInMillis(){
		if (onTimeMachineMode)
			return Calendar.getInstance().getTimeInMillis() - timeMachineDifference;
		return Calendar.getInstance().getTimeInMillis();
	}
	
	public Timestamp getCurrentTimeInTimestamp(){
		return new Timestamp(getCurrentTimeInMillis());
	}
	
	public Date getCurrentTimeInDate(){
		return new Date(getCurrentTimeInMillis());
	}
	
	public boolean isNotPast(Appt appt){
		long currentTime = getCurrentTimeInMillis(); 
		if (currentTime <= appt.TimeSpan().StartTime().getTime())
			return true;
		System.out.println("\nTimeController/isNotPast: Past Time!");
		return false;
	}

	public boolean isNotPast(Date date)
	{
		long currentTime = getCurrentTimeInMillis();
		System.out.println("Current Time: " + currentTime);
		System.out.println("EndAt Time: " + date.getTime());
		if(currentTime <= date.getTime())
			return true;
		System.out.println("\nTimeController/isNotPast: Past Time!");
		return false;
	}
	
	public boolean isLeapYear(int year)
	{
		if ((year % 400 == 0) || ((year % 4 == 0) && (year % 100 != 0))) 
		{
			return true;
		}
		else 
		{
			return false;
		}
	}
	
	public int numOfDaysInMonth()
	{
		int[] monthDays = {31,28,31,30,31,30,31,31,30,31,30,31};
		int currentMonth;
		int currentYear;
		
		
		if(onTimeMachineMode)
		{
			currentMonth = timeMachine.getTime().getMonth()+1;
			currentYear = timeMachine.getTime().getYear()+1900;
		}
		else
		{
			currentMonth = Calendar.getInstance().getTime().getMonth()+1;
			currentYear = Calendar.getInstance().getTime().getYear()+1900;
		}
		
		//System.out.println("Function numOfDaysInMonth(): currentMonth: " + currentMonth);
		//System.out.println("Function numOfDaysInMonth(): currentYear: " + currentYear);
		//System.out.println("Function numOfDaysInMonth(): currentMonthDays: "+monthDays[currentMonth-1]);
		if(this.isLeapYear(currentYear))
		{
			if(currentMonth == 2)
			{	
				return 29;
			}
			else
			{
				return monthDays[currentMonth-1];
			}
		}
		else
		{
			return monthDays[currentMonth-1];
		}
		
	}
	
}
