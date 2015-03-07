package hkust.cse.calendar.time;

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
		timeMachine = Calendar.getInstance();
		timeMachine.setTime(d);
	}
	
	public boolean isOnTimeMachineMode(){
		return onTimeMachineMode;
	}
	
	public void disableTimeMachineMode(){		
		onTimeMachineMode = false;
	}
	
	public long getCurrentTimeInMillis(){
		if (onTimeMachineMode)
			return timeMachine.getTimeInMillis();
		return Calendar.getInstance().getTimeInMillis();
	}
	
	public Timestamp getCurrentTimeInTimestamp(){
		if (onTimeMachineMode)
			return new Timestamp(timeMachine.getTimeInMillis());
		return new Timestamp(Calendar.getInstance().getTimeInMillis());
	}
	
	public Date getCurrentTimeInDate(){
		if (onTimeMachineMode)
			return timeMachine.getTime();
		return Calendar.getInstance().getTime();
	}
}
