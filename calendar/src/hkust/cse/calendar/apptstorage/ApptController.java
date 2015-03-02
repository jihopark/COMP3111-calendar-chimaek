package hkust.cse.calendar.apptstorage;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

public class ApptController {
	
	/* Applying Singleton Structure */
	private static ApptController instance = null;
	
	@Deprecated
	public final static int REMOVE = 1;
	@Deprecated	
	public final static int MODIFY = 2;
	@Deprecated	
	public final static int NEW = 3;

	/* The Appt storage */
	private static ApptStorage mApptStorage = null;

	/* Empty Constructor, since in singleton getInstance() is used instead*/
	public ApptController() {
		
	}
	
	public static ApptController getInstance(){
		if (instance == null){
			instance = new ApptController();
		}
		return instance;
	}
	
	//Initialize mApptStorage. Returns false if ApptStorage object already exists
	public boolean initApptStorage(ApptStorage storage){
		if (mApptStorage == null){
			mApptStorage = storage;
			return true;
		}
		return false;
	}
	

	/* Retrieve the Appt's in the storage for a specific user within the specific time span */
	public Appt[] RetrieveAppts(User entity, TimeSpan time) {
		return mApptStorage.RetrieveAppts(entity, time);
	}

	// overload method to retrieve appointment with the given joint appointment id
	public Appt RetrieveAppts(int joinApptID) {
		return mApptStorage.RetrieveAppts(joinApptID);
	}
	
	/* Manage the Appt in the storage
	 * parameters: the Appt involved, the action to take on the Appt */
	@Deprecated
	public void ManageAppt(Appt appt, int action) {

		if (action == NEW) {				// Save the Appt into the storage if it is new and non-null
			if (appt == null)
				return;
			mApptStorage.SaveAppt(appt);
		} else if (action == MODIFY) {		// Update the Appt in the storage if it is modified and non-null
			if (appt == null)
				return;
			mApptStorage.UpdateAppt(appt);
		} else if (action == REMOVE) {		// Remove the Appt from the storage if it should be removed
			mApptStorage.RemoveAppt(appt);
		} 
	}
	
	//Register appt as New Appt of user. Return true if successfully registered
	public boolean registerNewAppt(User user, Appt appt){
		return true;
	}
	
	//Modify appt of user. Return true if successfully modified
	public boolean modifyAppt(User user, Appt appt){
		return true;
	}
	
	//Remove appt of user. Return true if successfully removed
	public boolean removeAppt(User user, Appt appt){
		return true;
	}
	
	/* Get the defaultUser of mApptStorage */
	public User getDefaultUser() {
		return mApptStorage.getDefaultUser();
	}

	// method used to load appointment from xml record into hash map
	public void LoadApptFromXml(){
		mApptStorage.LoadApptFromXml();
	}
}
