package hkust.cse.calendar.locationstorage;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

import hkust.cse.calendar.apptstorage.ApptController;
import hkust.cse.calendar.apptstorage.ApptStorage;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

public class LocationController {
	
	/* Applying Singleton Structure */
	private static LocationController instance = null;

	private static int locationIDCount = 0;
		
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
	
	public int getLocationCapacity(String name){
		return mLocationStorage.getLocationCapacity(name);
	}
	
	public boolean setLocationCapacity(String name, int num){
		return mLocationStorage.setLocationCapacity(name, num);
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
	public boolean removeLocation(int index){
		return mLocationStorage.RemoveLocation(index);
	}
	
	public DefaultListModel<Location> getLocationList(){
		return mLocationStorage.getLocationList();
	}
	
	public int getListSize(){
		return mLocationStorage.getListSize();
	}
	
	public void printList(){
		System.out.println("===Current List===: "+mLocationStorage.getListSize());
		for(int i=0; i<mLocationStorage.getListSize(); i++){
			System.out.println(mLocationStorage.getLocationList().getElementAt(i));
		}
	}
}
