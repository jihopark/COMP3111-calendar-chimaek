package hkust.cse.calendar.locationstorage;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

public class LocationStorageNullImpl extends LocationStorage {

	private User defaultUser = null;
	private LinkedList<Location> list;
	private int locationNumber = 0;
	
	public LocationStorageNullImpl( User user )
	{
		defaultUser = user;
		list = new LinkedList<Location>();
	}
	
	@Override
	public Location RetrieveLocations(int joinLocationID) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean SaveLocation(Location location) {
		// TODO Auto-generated method stub
		if (!checkForSameLocation(location)) {
			list.add(location);
			locationNumber++;
			return true;
		}
		return false;

	}

	@Override
	public List<Location> getLocationList() {
		ArrayList<Location> retrieveList = new ArrayList<Location>();
		
		for (Location a : list) {
			retrieveList.add(a);
		}
		return retrieveList;
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
	public boolean RemoveLocation(Location location) {
		// TODO Auto-generated method stub
		for (Location a : list){
			if (a.equals(location)){
				list.remove(a);
				locationNumber--;
				return true;
			}
		}
		return false;
	}
	
	
	//Additional Functions for Checking
	private boolean checkForSameLocation(Location location) {
	// need to implement if Location with same name exsists
		return true;
	}

}


