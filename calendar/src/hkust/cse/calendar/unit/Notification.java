package hkust.cse.calendar.unit;

public class Notification {
	private String _name;
	private int _reminderHour;
	private int _notificationID;
	
	public Notification(String name, int reminderHour ) {
		_name = name;
		_reminderHour = reminderHour;
		_notificationID = 0;
	}
	
	public String getName() {
		return _name;
	}
	
	public int getReminderHour() {
		return _reminderHour;
	}
	public int getID() {
		return _notificationID;
	}
	
	public void setName(String name) {
		_name = name;
	}
		
	public void setReminderHour(int reminderHour) {
		_reminderHour = reminderHour;
	}

	public void setID(int id) {
		_notificationID = id;
	}

}
