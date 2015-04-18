package hkust.cse.calendar.notification;

import hkust.cse.calendar.unit.Notification;

import java.util.Date;

public class NotificationTime {		
	
	private int _flag;
	private Date _notificationTime;
	private int notification_parent_id;
	
	public NotificationTime(Notification parent, int flag, Date notificationTime){
		notification_parent_id = parent.getID();
		_flag = flag;
		_notificationTime = notificationTime;
	}
	
	public Notification getParent(){ return NotificationController.getInstance().getNotificationByID(notification_parent_id); }
	public Date getNotificationTime(){ return _notificationTime; }
	public int getFlag(){ return _flag; }
	public void setNotificationParentID(int id){
		notification_parent_id = id;
	}
}
