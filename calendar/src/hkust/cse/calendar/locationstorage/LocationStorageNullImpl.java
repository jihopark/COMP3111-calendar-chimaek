package hkust.cse.calendar.locationstorage;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

public class LocationStorageNullImpl extends LocationStorage {

	private User defaultUser = null;
	private ArrayList<Location> list;
	private int locationNumber = 0;
	private Location initialLocation = new Location();
	
	public LocationStorageNullImpl( User user )
	{
		defaultUser = user;
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
			list.add(location);
			locationNumber++;
			System.out.println("Saved Location ID is: " + location.getID());
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
			locationNumber--;
			return true;
		}
		return false;
	}
	
	
	//Additional Functions for Checking
	private boolean checkForSameLocation(Location location) {
	// need to implement if Location with same name exists
		return true;
	}



}


