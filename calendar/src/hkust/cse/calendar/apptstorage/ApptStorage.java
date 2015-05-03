package hkust.cse.calendar.apptstorage;//

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.DeleteRequest;
import hkust.cse.calendar.unit.Notification;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

import java.util.List;


public abstract class ApptStorage {

	public int mAssignedApptID;	//a global appointment ID for each appointment record

	public ApptStorage() {	//default constructor
	}

	public abstract boolean SaveAppt(User user, Appt appt);	//abstract method to save an appointment record

	@Deprecated
	public abstract Appt[] RetrieveAppts(TimeSpan d);	//abstract method to retrieve an appointment record by a given timespan
	
	@Deprecated
	public abstract Appt[] RetrieveAppts(User entity, TimeSpan time);	//overloading abstract method to retrieve an appointment record by a given user object and timespan
	
	public abstract List<Appt> RetrieveApptsInList(User user, TimeSpan d);

	public abstract Appt RetrieveAppts(int apptID);					
	
	public abstract boolean RemoveAppt(User user, Appt appt);	//abstract method to remove an appointment record
		
	public abstract boolean checkOverlaps(User user, List<Appt> appts);
	
	public abstract boolean checkOverlaps(User user, Appt appt);
	
	public abstract void saveNotification(User user,Notification noti);
	
	public abstract void removeNotification(User user,Notification noti);
	
	public abstract int getIDCount();

	public abstract List<Appt> RetrieveApptsInList(User user);

	public abstract List<Appt> RetrievePublicApptsInList(User user);

}
