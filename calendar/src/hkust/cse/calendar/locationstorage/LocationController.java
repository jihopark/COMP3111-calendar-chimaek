package hkust.cse.calendar.locationstorage;

import hkust.cse.calendar.diskstorage.JsonStorable;
import hkust.cse.calendar.unit.Location;

import javax.swing.DefaultListModel;

public class LocationController {
	
	/* Applying Singleton Structure */
	private static LocationController instance = null;

	//private static int locationIDCount = 0;
		
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
			if (mLocationStorage instanceof JsonStorable){
				mLocationStorage = (LocationStorage) ((JsonStorable)mLocationStorage).loadFromJson();
				if (mLocationStorage == null) mLocationStorage = storage;
			}
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
	public void increaseLocationCount(Location location){
		location.increaseCountForLocation();
		System.out.println("LocationController/increaseLocationCount");
		updateDiskStorage();
	}
	
	public void decreaseLocationCount(Location location){
		location.decreaseCountForLocation();
		System.out.println("LocationController/decreaseLocationCount");
		updateDiskStorage();
	}
	
	//save new location
	public boolean saveNewLocation(Location location){
		location.setID(mLocationStorage.getIDCount());
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
	
	public boolean checkValidCapacity(int capacity){
		if(capacity < 1){
			return false;
		}
		return true;
	}

	public void updateDiskStorage(){
		if (mLocationStorage instanceof JsonStorable)
			((JsonStorable) mLocationStorage).saveToJson();
	}
	
	public Location getLocationByID(int id){
		return mLocationStorage.RetrieveLocations(id);
	}
	public boolean canDeleteLocation(Location location){
		int count = mLocationStorage.getLocationApptCount(location);
		if(count<0) {
			System.out.println("getLocationApptCount ERROR");
			return false;
		}
		else if(count==0)
			return true;
		else
			return false;
	}
	public int getLocationApptCount(Location location){
		return mLocationStorage.getLocationApptCount(location);
	}
}
