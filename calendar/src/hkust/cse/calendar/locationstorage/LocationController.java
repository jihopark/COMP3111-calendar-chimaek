package hkust.cse.calendar.locationstorage;

import java.util.ArrayList;
import java.util.List;

import hkust.cse.calendar.apptstorage.ApptController;
import hkust.cse.calendar.apptstorage.ApptStorage;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

public class LocationController {
	
	/* Applying Singleton Structure */
	private static LocationController instance = null;

	private static int locationIDCount = 1;
		
	/* The Location storage */
	private static LocationStorage mLocationStorage;

	/* Empty Constructor, since in singleton getInstance() is used instead*/
	public LocationController() {
		
	}
	
	public static LocationController getInstance()
	{
		if (instance == null){
			instance = new LocationController();
		}
		return instance;
	}
	
	public boolean initLocationStorage(LocationStorage storage){
		if (mLocationStorage == null){
			mLocationStorage = storage;
			return true;
		}
		return false;
	}
	
	public Location RetrieveLocations(int LocationID) {
		return mLocationStorage.RetrieveLocations(LocationID);
	}
	
	public Location RetrieveLocations(String locationString)
	{
		return mLocationStorage.RetrieveLocations(locationString);
	}
	
	//save new location
	public boolean saveNewLocation(Location location){
		location.setID(locationIDCount++);
		return mLocationStorage.SaveLocation(location);
	}
	
	//Modify location Return true if successfully modified
	public boolean modifyLocation(Location location){
		return mLocationStorage.UpdateLocation(location);
	}
	
	//Remove location. Return true if successfully removed
	public boolean removeLocation(Location location){
		return mLocationStorage.RemoveLocation(location);
	}
	
	public ArrayList<Location> getLocationList(){
		return mLocationStorage.getLocationList();
	}
}
