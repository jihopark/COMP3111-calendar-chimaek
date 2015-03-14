package hkust.cse.calendar.unit;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

public class Notification {
	private String _name;
	private LinkedList<Date> _alarmTime;
	private LinkedList<Boolean> _alarmFlag;
	
	private Date _savedTime;
	private int _notificationID;
	
	// A default constructor
	public Notification() {
		_name = "";
		_notificationID = 0;
		_alarmTime = new LinkedList<Date>();
		_alarmFlag = new LinkedList<Boolean>();
		_savedTime = Calendar.getInstance().getTime();

	}
	
	public Notification(Notification notification ) {
		_name = notification.getName();
		_notificationID = 0;
		_alarmFlag = notification.getFlags();
		_alarmTime = notification.getAlarms();
	}


	private LinkedList<Date> getAlarms() {
		return _alarmTime;
	}

	private LinkedList<Boolean> getFlags() {
		return _alarmFlag;
	}

	public String getName() {
		return _name;
	}
	
	public int getID() {
		return _notificationID;
	}
	
	public void setName(String name) {
		_name = name;
	}
		
	public void setID(int id) {
		_notificationID = id;
	}
	
	private void setAlarms(LinkedList<Boolean> flags) {
		//check flag1, set alarm1 repeat 4 times
	}
	
	private void setFlags(boolean firstFlag, boolean secondFlag, boolean thirdFlag, boolean fourthFlag) {
		//if flag1 2 3 4.. set _alarmFlag
	}
	
	
	
	public String toString() {
		return _name;
	}
	
	public boolean isValid() {
		if (getID() <= 0)
			return false;
		if (getName()==null || getName().equals(""))
			return false;
		return true;
	}
}
