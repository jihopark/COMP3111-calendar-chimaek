package hkust.cse.calendar.userstorage;


import hkust.cse.calendar.diskstorage.JsonStorable;
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
		return mUserStorage.RemoveUser(id);
	}

	public boolean modifyUser(User current, User before) {
		// TODO Auto-generated method stub
		return mUserStorage.ModifyUser(current, before);
	}

}
