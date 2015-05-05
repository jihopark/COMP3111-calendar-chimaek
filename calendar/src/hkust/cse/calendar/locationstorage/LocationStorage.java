package hkust.cse.calendar.locationstorage;

import hkust.cse.calendar.unit.Location;

import javax.swing.DefaultListModel;


public abstract class LocationStorage {

	public LocationStorage() {	//default constructor
	}
	
	public abstract DefaultListModel<Location> getLocationList();

	public abstract boolean UpdateLocation(Location location);

	public abstract boolean RemoveLocation(int index);

	public abstract boolean SaveLocation(Location location);

	public abstract Location RetrieveLocations(int LocationID);

	public abstract Location RetrieveLocations(String locationString);

	public abstract int getLocationCapacity(String name);
	
	public abstract boolean setLocationCapacity(String name, int num);
	
	public abstract int getListSize();
	
	public abstract int getIDCount();

	public abstract int getLocationApptCount(Location location);

}
