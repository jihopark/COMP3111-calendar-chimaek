package hkust.cse.calendar.userstorage;

import hkust.cse.calendar.unit.DeleteRequest;
import hkust.cse.calendar.unit.ModifyNotification;
import hkust.cse.calendar.unit.User;

import java.util.HashMap;
import java.util.List;

public abstract class UserStorage {

		public HashMap mAppts;		//a hashmap to save every thing to it, write to memory by the memory based storage implementation	
		public int mAssignedUserID;	//a global appointment ID for each appointment record

		public abstract User getUserFromCredential(String id, String pw);
		
		public abstract boolean SaveUser(String id, String pw, String fName, String lName, String email, Boolean admin);	//abstract method to save an appointment record

		public abstract List<User> getUserList();

		public abstract boolean RemoveUser(String id);	//abstract method to remove an appointment record

		public abstract boolean ModifyUser(User current, User before);

		public abstract User getUser(String user);
		
		public abstract boolean addDeleteRequest(DeleteRequest request);
		
		public abstract boolean removeDeleteRequest(DeleteRequest request);
		
		public abstract List<DeleteRequest> getDeleteRequests(User user);
		
		public abstract List<ModifyNotification> getModifyNotificationList(User user);
		
		public abstract void addModifyNotification(ModifyNotification noti);
		
		public abstract void removeModifyNotification(ModifyNotification noti);
}
