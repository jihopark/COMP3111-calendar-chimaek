package hkust.cse.calendar.unit;

import hkust.cse.calendar.locationstorage.LocationController;

public class Location {
	private String _name;
	private int _locationID;
	private int _appointmentCount;
	
	public Location() {
		_name = "";
		_locationID = 0;
		_appointmentCount = 0;
	}
	
	public void increaseCountForLocation() {
		_appointmentCount++;
	}
	
	public void decreaseCountForLocation() {
		_appointmentCount--;
	}
	
	public int getAppointmentCount() {
		return _appointmentCount;
	}
	
	
	public Location(Location location) {
		_name = location.getName();
		_locationID = location.getID();
		_appointmentCount = location.getAppointmentCount();
	}
	
	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}
	/*
	public void setAppt(Appt appointment) {
		_appointment = appointment;
	}
	public Appt getAppt() {
		return _appointment;
	}
	*/

	public void setID(int locationID) {
		_locationID = locationID;
	}
	public int getID() {
		return _locationID;
	}
	
	public boolean isValid() {
		if (getID() <= 0)
			return false;
		if (getName()==null || getName().equals(""))
			return false;
		return true;
	}
	
	public String toString() {
		return _name;
	}

}
