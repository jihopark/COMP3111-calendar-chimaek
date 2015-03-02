package hkust.cse.calendar.apptstorage;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

import java.util.List;

public class ApptStorageMemory extends ApptStorage {

	private User defaultUser = null;
	
	public ApptStorageMemory( User user )
	{
		defaultUser = user;
	}
	
	@Override
	public boolean SaveAppt(Appt appt) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	@Deprecated
	public Appt[] RetrieveAppts(TimeSpan d) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Appt> RetrieveApptsInList(TimeSpan d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Deprecated	
	public Appt[] RetrieveAppts(User entity, TimeSpan time) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Appt RetrieveAppts(int joinApptID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean UpdateAppt(Appt appt) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean RemoveAppt(Appt appt) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public User getDefaultUser() {
		// TODO Auto-generated method stub
		return defaultUser;
	}

	@Override
	public void LoadApptFromXml() {
		// TODO Auto-generated method stub

	}

}
