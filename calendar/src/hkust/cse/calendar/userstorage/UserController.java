package hkust.cse.calendar.userstorage;


import hkust.cse.calendar.apptstorage.ApptController;
import hkust.cse.calendar.diskstorage.JsonStorable;
import hkust.cse.calendar.invite.InviteController;
import hkust.cse.calendar.notification.NotificationController;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.DeleteRequest;
import hkust.cse.calendar.unit.GroupAppt;
import hkust.cse.calendar.unit.ModifyNotification;
import hkust.cse.calendar.unit.Notification;
import hkust.cse.calendar.unit.User;

import java.util.List;

public class UserController {
	/* Applying Singleton Structure */
	private static UserController instance = null;
	private static int userIDCount = 1;
	private static UserStorage mUserStorage = null;
	private User currentUser = null;

	public UserController(){

	}

	public static UserController getInstance(){
		if (instance==null){
			instance = new UserController();
		}
		return instance;
	}

	//Initialize mUserStorage. Return false if UserStorage object already exists
	public boolean initUserStorage(UserStorage storage) {
		if(mUserStorage == null) {
			mUserStorage = storage;
			if (mUserStorage instanceof UserStorageMemory && mUserStorage instanceof JsonStorable){
				mUserStorage = (UserStorageMemory) ((JsonStorable)mUserStorage).loadFromJson();
				if (mUserStorage == null) mUserStorage = storage;
			}
			return true;
		}
		return false;
	}

	public User getCurrentUser(){
		return currentUser;
	}

	public User getUser(String user){
		return mUserStorage.getUser(user);
	}

	public void setCurrentUser(User user){
		currentUser = user;
	}

	public User getUserFromCredential(String id, String pw){
		return mUserStorage.getUserFromCredential(id, pw);
	}

	public User getAdmin(){
		return currentUser;
	}

	public List<User> getUserList(){
		return mUserStorage.getUserList();
	}

	public boolean saveUser(String id, String pw, String fName, String lName, String email, Boolean admin) {
		return mUserStorage.SaveUser(id, pw, fName, lName, email, admin);
	}

	public boolean removeUser(String id){
		boolean shouldDelete = true;
		for (Appt a : ApptController.getInstance().RetrieveApptsInList(UserController.getInstance().getUser(id))){
			if (a instanceof GroupAppt){
				mUserStorage.addDeleteRequest(new DeleteRequest(id, ((GroupAppt) a).getOwner()));
				shouldDelete = false;
			}
		}
		if (shouldDelete) {
			for(Notification n: NotificationController.getInstance().retrieveNotification(UserController.getInstance().getUser(id))) {
				NotificationController.getInstance().removeNotification(UserController.getInstance().getUser(id), n);
				System.out.println("UserController/removeUser: Notification Controller Deleted Notificatoin " + n.getName() + " from Appt " + 
						n.getAppointmentTime());
			}
			for(Appt a : ApptController.getInstance().RetrieveApptsInList(UserController.getInstance().getUser(id))) {
				if (a instanceof GroupAppt){
					GroupAppt gAppt = (GroupAppt)a;
					ApptController.getInstance().removeUserFromGroupAppt(gAppt,UserController.getInstance().getUser(id));
					ApptController.getInstance().removeAppt(UserController.getInstance().getUser(id), gAppt);
				} else {
					ApptController.getInstance().removeAppt(UserController.getInstance().getUser(id), a);
					System.out.println("UserController/removeUser: User Controller Deleted Appt " + a.getTitle() + " from User " + 
							UserController.getInstance().getUser(id));
				}
			}
			if(InviteController.getInstance().removeUserFromAllInvite(UserController.getInstance().getUser(id))) {
				mUserStorage.RemoveUser(id);
			}
		}
			
		return shouldDelete;
	}

	public boolean modifyUser(User current, User before) {
		// TODO Auto-generated method stub
		return mUserStorage.ModifyUser(current, before);
	}

	public List<DeleteRequest> getDeleteRequests(User user){
		return mUserStorage.getDeleteRequests(user);
	}

	public void respondToDeleteRequest(boolean response, DeleteRequest request){
		if (response)
			request.acceptRequest();
		mUserStorage.removeDeleteRequest(request);
	}

	public List<ModifyNotification> getModifyNotifications(User user){
		return mUserStorage.getModifyNotificationList(user);
	}
	
	public void addModifyNotification(ModifyNotification noti){
		mUserStorage.addModifyNotification(noti);
	}
	
	public void removeModifyNotification(ModifyNotification noti){
		mUserStorage.removeModifyNotification(noti);
	}

}
