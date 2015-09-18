package hkust.cse.calendar.time;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.TimeSpan;

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
	
	public boolean isNotPast(TimeSpan span){
		long currentTime = getCurrentTimeInMillis(); 
		if (currentTime <= span.StartTime().getTime())
			return true;
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
		
		currentMonth = getCurrentTimeInDate().getMonth()+1;
		currentYear = getCurrentTimeInDate().getYear()+1900;
		
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
	
	public int numOfDaysInMonth(int year, int month)
	{
		int[] monthDays = {31,28,31,30,31,30,31,31,30,31,30,31};
		int currentMonth;
		int currentYear;
		
		currentMonth = month;
		currentYear = year;
		
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
	
	
	/*TimeController as standard time source*/
	
	public Timestamp dateInputToTimestamp(int year, int month, int date, int hour, int min, int sec)
	{
		Timestamp tempTimestamp = new Timestamp(0);
		long dateInputInMillisFromEpoch = dateInputToMillisFromEpoch(year,month,date,hour,min,sec);
		tempTimestamp.setTime(dateInputInMillisFromEpoch);
		return tempTimestamp;
	}
	
	public static Date dateInputToDate(int year, int month, int date, int hour, int min, int sec)
	{
		Date tempDate = new Date(0);
		long dateInputInMillisFromEpoch = dateInputToMillisFromEpoch(year,month,date,hour,min,sec);
		tempDate.setTime(dateInputInMillisFromEpoch);
		return tempDate;
	}
	
	public static long dateInputToMillisFromEpoch(int year, int month, int date, int hour, int min, int sec)
	{
		Calendar tempCalendar = Calendar.getInstance();
		tempCalendar.set(year, month-1, date, hour, min, sec);
		return tempCalendar.getTimeInMillis();
	}
	
	public Date millisFromEpochToDate(long millis)
	{
		Date tempDate = new Date(millis);
		return tempDate;
	}

	public Date millisFromEpochToTimestamp(long millis)
	{
		Timestamp tempTimestamp = new Timestamp(millis);
		return tempTimestamp;
	}
	
	public void setYear(Date date, int year)
	{
		Calendar tempCalendar = Calendar.getInstance();
		tempCalendar.setTime(date);
		tempCalendar.set(Calendar.YEAR, year);
		date.setTime(tempCalendar.getTimeInMillis());		
	}
	
	public void setMonth(Date date, int month)
	{
		Calendar tempCalendar = Calendar.getInstance();
		tempCalendar.setTime(date);
		tempCalendar.set(Calendar.MONTH, month-1);
		date.setTime(tempCalendar.getTimeInMillis());	
	}
	
	public void setDate(Date date, int day)
	{
		Calendar tempCalendar = Calendar.getInstance();
		tempCalendar.setTime(date);
		tempCalendar.set(Calendar.DATE, day);
		date.setTime(tempCalendar.getTimeInMillis());	
	}
	
	public void setHour(Date date, int hour)
	{
		Calendar tempCalendar = Calendar.getInstance();
		tempCalendar.setTime(date);
		tempCalendar.set(Calendar.HOUR_OF_DAY, hour);
		date.setTime(tempCalendar.getTimeInMillis());	
	}
	
	public void setMinute(Date date, int minute)
	{
		Calendar tempCalendar = Calendar.getInstance();
		tempCalendar.setTime(date);
		tempCalendar.set(Calendar.MINUTE, minute);
		date.setTime(tempCalendar.getTimeInMillis());	
	}
	
	public void setSecond(Date date, int second)
	{
		Calendar tempCalendar = Calendar.getInstance();
		tempCalendar.setTime(date);
		tempCalendar.set(Calendar.SECOND, second);
		date.setTime(tempCalendar.getTimeInMillis());	
	}
	
	
	public int getYearFrom(Date date)
	{
		Calendar tempCalendar = Calendar.getInstance();
		tempCalendar.setTime(date);
		return tempCalendar.get(Calendar.YEAR);
	}
	
	public int getMonthFrom(Date date)
	{
		Calendar tempCalendar = Calendar.getInstance();
		tempCalendar.setTime(date);
		return tempCalendar.get(Calendar.MONTH)+1;
	}
	
	public int getDateFrom(Date date)
	{
		Calendar tempCalendar = Calendar.getInstance();
		tempCalendar.setTime(date);
		return tempCalendar.get(Calendar.DATE);
	}
	
	public int getHourFrom(Date date)
	{
		Calendar tempCalendar = Calendar.getInstance();
		tempCalendar.setTime(date);
		return tempCalendar.get(Calendar.HOUR_OF_DAY);
	}
	
	public int getMinuteFrom(Date date)
	{
		Calendar tempCalendar = Calendar.getInstance();
		tempCalendar.setTime(date);
		return tempCalendar.get(Calendar.MINUTE);
	}
	
	public int getSecondFrom(Date date)
	{
		Calendar tempCalendar = Calendar.getInstance();
		tempCalendar.setTime(date);
		return tempCalendar.get(Calendar.SECOND);
	}
	
	public long getTimeInMillisFromEpoch(Date date)
	{
		Calendar tempCalendar = Calendar.getInstance();
		tempCalendar.setTime(date);
		return tempCalendar.getTimeInMillis();
	}
	
	public boolean checkValidYear(int year){
		if(year < 1 || year > 9999){
			return false;
		}
		return true;
	}
	
	public boolean checkValidMonth(int month){
		if(month < 1 || month > 12){
			return false;
		}
		return true;
	}
	
	public boolean checkValidDate(int year, int month, int day){
		int daysInMonth = numOfDaysInMonth(year,month);
		if(day < 1 || day > daysInMonth){
			return false;
		}
		return true;
	}
	
	public boolean checkValidTime(int hour, int minute){
		if(hour < 0 || hour > 23){
			return false;
		}
		if(minute < 0 || minute > 59){
			return false;
		}
		return true;
	}
	
	public boolean checkValidInput(int year, int month, int day, int hour, int minutes){
		TimeController time = TimeController.getInstance();
		return (time.checkValidYear(year)&&time.checkValidMonth(month)&&time.checkValidDate(year, month, day)
				&&time.checkValidTime(hour, minutes));

	}
	
}