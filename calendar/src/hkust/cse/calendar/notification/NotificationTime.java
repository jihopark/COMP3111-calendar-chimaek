package hkust.cse.calendar.notification;

import hkust.cse.calendar.unit.Notification;

import java.util.Date;

public class NotificationTime {		
	
	private int _flag;
	private Date _notificationTime;
	private Notification _parent;
	
	public NotificationTime(Notification parent, int flag, Date notificationTime){
		_parent = parent;
		_flag = flag;
		_notificationTime = notificationTime;
	}
	
	public Notification getParent(){ return _parent; }
	public Date getNotificationTime(){ return _notificationTime; }
	public int getFlag(){ return _flag; }
}
