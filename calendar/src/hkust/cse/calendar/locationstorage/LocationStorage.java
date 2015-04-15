package hkust.cse.calendar.locationstorage;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.unit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultListModel;


public abstract class LocationStorage {

	public HashMap mLocations;		//a hashmap to save every thing to it, write to memory by the memory based storage implementation	
	public User defaultUser;	//a user object, now is single user mode without login
	public int mAssignedLocationID;	//a global appointment ID for each appointment record

	public LocationStorage() {	//default constructor
	}
	
	public abstract DefaultListModel<Location> getLocationList();

	public abstract boolean UpdateLocation(Location location);

	public abstract boolean RemoveLocation(int index);

	public abstract boolean SaveLocation(Location location);

	public abstract Location RetrieveLocations(int LocationID);

	public abstract Location RetrieveLocations(String locationString);

	public abstract int getListSize();
	
	public abstract int getIDCount();

	/*
	 * Add other methods if necessary
	 */

}
