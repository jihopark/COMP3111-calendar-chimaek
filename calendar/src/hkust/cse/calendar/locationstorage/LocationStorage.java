package hkust.cse.calendar.locationstorage;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.unit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public abstract class LocationStorage {

	public HashMap mLocations;		//a hashmap to save every thing to it, write to memory by the memory based storage implementation	
	public User defaultUser;	//a user object, now is single user mode without login
	public int mAssignedLocationID;	//a global appointment ID for each appointment record

	public LocationStorage() {	//default constructor
	}
	
	public abstract ArrayList<Location> getLocationList();

	public abstract boolean UpdateLocation(Location location);

	public abstract boolean RemoveLocation(Location location);

	public abstract boolean SaveLocation(Location location);

	public abstract Location RetrieveLocations(int LocationID);

	public abstract Location RetrieveLocations(String locationString);
	/*
	 * Add other methods if necessary
	 */

}
