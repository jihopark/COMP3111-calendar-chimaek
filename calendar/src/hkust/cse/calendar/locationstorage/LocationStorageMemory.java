package hkust.cse.calendar.locationstorage;

import hkust.cse.calendar.apptstorage.ApptStorageMemory;
import hkust.cse.calendar.diskstorage.FileManager;
import hkust.cse.calendar.diskstorage.JsonStorable;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.User;

import java.util.ArrayList;

import javax.swing.DefaultListModel;

import com.google.gson.Gson;

public class LocationStorageMemory extends LocationStorage implements JsonStorable {

	private ArrayList<Location> list;
	private int locationNumber = 0;
	private Location initialLocation = new Location();
	private int locationIDCount = 1;
	
	public LocationStorageMemory( User user )
	{
		list = new ArrayList<Location>();
		initialLocation.setName("-");
		initialLocation.setID(0);
		list.add(initialLocation);
		locationNumber++;
	}
	
	@Override
	public Location RetrieveLocations(int LocationID) {
		for (Location a : list){
			if (a.getID() == LocationID){
				return a;
			}
		}
		//if can't find location in the list
		return null;
	}
	
	@Override
	public Location RetrieveLocations(String locationString)
	{
		for (Location a : list){
			if (a.getName() == locationString){
				return a;
			}
		}
		//if can't find location in the list
		return null;
		
	}
	
	@Override
	public boolean SaveLocation(Location location) {
		// TODO Auto-generated method stub
		if (checkForSameLocation(location)) {
			location.setID(locationNumber++);
			list.add(location);
			System.out.println("LocationStorageMemory/SaveLocation Saved Location #" + location.getID());
			saveToJson();
			return true;
		}
		return false;
	}

	@Override
	public DefaultListModel<Location> getLocationList() {
		DefaultListModel<Location> retrieveList = new DefaultListModel<Location>();
		
		for (Location a : list) {
			retrieveList.addElement(a);
		}
		return retrieveList;
	}
	
	@Override
	public int getListSize() {
		return list.size();
	} 
	
	@Override
	public boolean UpdateLocation(Location location) {
		// TODO Auto-generated method stub
		for (Location a : list){
			if (a.equals(location)){
				list.remove(a);
				list.add(location);
				saveToJson();
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean RemoveLocation(int index) {
		// TODO Auto-generated method stub
		
		if(index<list.size() && index>-1) {
			list.remove(index);
			saveToJson();
			return true;
		}
		return false;
	}
	
	@Override
	public int getIDCount(){ return locationIDCount++; }
	
	
	//Additional Functions for Checking
	private boolean checkForSameLocation(Location location) {
	// need to implement if Location with same name exists
		return true;
	}
	
	/*
	 * For Disk Storage
	 * */
	
	public String getFileName(){
		return "DISK_LOCATION.txt";
	}
	
	public Object loadFromJson(){
		Gson gson = new Gson();
		String json = FileManager.getInstance().loadFromFile(getFileName());
		if (json.equals("")) return null;
		return gson.fromJson(json, LocationStorageMemory.class);
	}
	
	public void saveToJson(){
		Gson gson = new Gson();
		FileManager.getInstance().writeToFile(gson.toJson(this), getFileName());
	}

	public int getLocationCapacity(String name) {
		if(RetrieveLocations(name)!=null)
			return RetrieveLocations(name).getCapacity();
		return 0;
	}

	public boolean setLocationCapacity(String name, int num) {
		if(RetrieveLocations(name)!=null || num<=0) {
			RetrieveLocations(name).setCapacity(num);
			return true;
		}
		return false;
	}

}


