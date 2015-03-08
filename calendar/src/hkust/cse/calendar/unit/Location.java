package hkust.cse.calendar.unit;

public class Location {
	//private Appt _appointment;
	private String _name;
	private int _locationID;
	
	public Location(String name, int locationID) {
		//_appointment = appointment;
		_name = name;
		_locationID = locationID;
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
}
