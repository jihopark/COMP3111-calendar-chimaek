package hkust.cse.calendar.locationstorage;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.unit.Location;
import java.util.HashMap;


public abstract class LocationStorage {

	public HashMap mLocations;		//a hashmap to save every thing to it, write to memory by the memory based storage implementation	
	public User defaultUser;	//a user object, now is single user mode without login
	public int mAssignedLocationID;	//a global appointment ID for each appointment record

	public LocationStorage() {	//default constructor
	}
	
	public abstract Location[] getLocationList();
	
	public abstract void setLocationList(Location[] locations);

	/*
	 * Add other methods if necessary
	 */

}