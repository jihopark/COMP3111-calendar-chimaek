package hkust.cse.calendar.locationstorage;

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
		//public LocationController(LocationStorage storage) {
		//	mLocationStorage = storage;
		//}
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
	
	public Location RetrieveLocations(int joinLocationID) {
		return mLocationStorage.RetrieveLocations(joinLocationID);
	}
	
	//Modify location of user. Return true if successfully modified
	public boolean modifyLocation(User user, Location location){
		return mLocationStorage.UpdateLocation(location);
	}
	
	//Remove appt of user. Return true if successfully removed
	public boolean removeLocation(User user, Location location){
		return mLocationStorage.RemoveLocation(location);
	}
	
	
}
