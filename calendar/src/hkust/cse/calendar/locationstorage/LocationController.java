package hkust.cse.calendar.locationstorage;

import hkust.cse.calendar.apptstorage.ApptController;
import hkust.cse.calendar.unit.Location;

public class LocationController {
	
	/* Applying Singleton Structure */
	private static LocationController instance = null;
	
	/* Remove the Location from the storage */
	public final static int REMOVE = 1;

	/* Modify the Location the storage */
	public final static int MODIFY = 2;

	/* Add a new Location into the storage */
	public final static int NEW = 3;
	
	/*
	 * Add additional flags which you feel necessary
	 */
	
	/* The Location storage */
	private static LocationStorage mLocationStorage = null;

	/* Empty Constructor, since in singleton getInstance() is used instead*/
	public LocationController() {
		
	}
	public Location[] getLocationList() {
		return mLocationStorage.getLocationList();
		
	}
	
	public void setLocationList(Location[] locations) {
		mLocationStorage.setLocationList(locations);
	}

	//temporary function
	public static LocationController getInstance()
	{
		if (instance == null){
			instance = new LocationController();
		}
		return instance;
		
	}
	
}
