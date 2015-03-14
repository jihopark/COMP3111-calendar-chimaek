package hkust.cse.calendar.unit;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

public class Notification {
	private String _name;
	private LinkedList<Date> _alarmTime;
	private LinkedList<Boolean> _alarmFlag;
	
	private Date _appointmentTime;
	private int _notificationID;
	
	// A default constructor
	public Notification() {
		_name = "";
		_notificationID = 0;
		_alarmTime = new LinkedList<Date>();
		_alarmFlag = new LinkedList<Boolean>();
		_appointmentTime = Calendar.getInstance().getTime();
	}
	
	public Notification(Notification notification ) {
		_name = notification.getName();
		_notificationID = 0;
		_alarmFlag = notification.getFlags();
		_alarmTime = notification.getAlarms();
	}

	public Date getAppointmentTime() {
		return _appointmentTime;
	}

	public LinkedList<Date> getAlarms() {
		return _alarmTime;
	}

	public LinkedList<Boolean> getFlags() {
		return _alarmFlag;
	}

	public String getName() {
		return _name;
	}
	
	public int getID() {
		return _notificationID;
	}
	public void setAppointmentTime(Date date) {
		_appointmentTime = date;
	}
	public void setName(String name) {
		_name = name;
	}
		
	public void setID(int id) {
		_notificationID = id;
	}
	
	public void setFlags(boolean firstFlag, boolean secondFlag, boolean thirdFlag, boolean fourthFlag) {
		//if flag1 2 3 4.. set _alarmFlag
		if(firstFlag == true) {
			_alarmFlag.add(0, true);
		} else {
			_alarmFlag.add(0, false);
		} 
		if(secondFlag == true) {
			_alarmFlag.add(1, true);
		} else {
			_alarmFlag.add(1, false);
		}
		if(thirdFlag == true) {
			_alarmFlag.add(2, true);
		} else {
			_alarmFlag.add(2, false);
		}
		if(fourthFlag == true) {
			_alarmFlag.add(3, true);
		} else {
			_alarmFlag.add(3, false);
		}
	}
	
	public void setAlarms() {
		//check flag1, set alarm1 repeat 4 times
		Calendar cal = Calendar.getInstance();
		
		if(_alarmFlag.get(0) == true) {
			cal.setTime(_appointmentTime);
			cal.add(Calendar.HOUR, -1);
			Date oneHourBack = cal.getTime();
			_alarmTime.add(0,oneHourBack);
		} else {
			cal.clear();
			_alarmTime.add(0,cal.getTime());
		}
		if(_alarmFlag.get(1) == true) {
			cal.setTime(_appointmentTime);
			cal.add(Calendar.HOUR, -3);
			Date threeHourBack = cal.getTime();
			_alarmTime.add(1,threeHourBack);
		} else {
			cal.clear();
			_alarmTime.add(1,cal.getTime());
		}
		if(_alarmFlag.get(2) == true) {
			cal.setTime(_appointmentTime);
			cal.add(Calendar.HOUR, -12);
			Date twelveHourBack = cal.getTime();
			_alarmTime.add(2,twelveHourBack);
		} else {
			cal.clear();
			_alarmTime.add(2,cal.getTime());
		}
		if(_alarmFlag.get(3) == true) {
			cal.setTime(_appointmentTime);
			cal.add(Calendar.HOUR, -24);
			Date twentyFourHourBack = cal.getTime();
			_alarmTime.add(3,twentyFourHourBack);
		} else {
			cal.clear();
			_alarmTime.add(3,cal.getTime());
		}
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
