package hkust.cse.calendar.locationstorage;

import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.User;

public class LocationStorageNullImpl extends LocationStorage {

	private User defaultUser = null;
	Location[] _locations;
	
	public LocationStorageNullImpl( User user )
	{
		defaultUser = user;
	}
		
	@Override
	public Location[] getLocationList() {
		return _locations;
	}
	
	@Override
	public void setLocationList(Location[] locations) {
		_locations = locations;
	}

}
