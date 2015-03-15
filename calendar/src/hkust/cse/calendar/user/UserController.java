package hkust.cse.calendar.user;

import hkust.cse.calendar.unit.User;

import java.util.ArrayList;
import java.util.List;

public class UserController {
	/* Applying Singleton Structure */
	private static UserController instance = null;
	
	private List<User> list = null; // not in use for single-user mode 
	private User defaultUser = new User("huamin","comp3111");
	
	public UserController(){
		list = new ArrayList<User>();
		list.add(defaultUser);
	}
	
	public static UserController getInstance(){
		if (instance==null){
			instance = new UserController();
		}
		return instance;
	}
	
	public User getUserFromCredential(String id, char[] pw){
		for (User u : list){
			if (u.checkCredentials(id, pw))
				return u;
			
		}
		return null;
	}
	
	public User getDefaultUser(){
		return defaultUser;
	}
	
	public List<User> getUserList(){
		return list;
	}
}
