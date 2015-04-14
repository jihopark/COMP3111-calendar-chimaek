package hkust.cse.calendar.userstorage;

import java.util.HashMap;
import java.util.List;

import hkust.cse.calendar.unit.User;

public abstract class UserStorage {

		public HashMap mAppts;		//a hashmap to save every thing to it, write to memory by the memory based storage implementation	
		public int mAssignedUserID;	//a global appointment ID for each appointment record

		public abstract User getUserFromCredential(String id, String pw);
		
		public abstract boolean SaveUser(String id, String pw);	//abstract method to save an appointment record

		public abstract List<User> getUserList();

		public abstract boolean RemoveUser(String id);	//abstract method to remove an appointment record
		
		public abstract User getAdmin();		//abstract method to return the current user object
}
