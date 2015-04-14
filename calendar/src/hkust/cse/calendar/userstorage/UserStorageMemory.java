package hkust.cse.calendar.userstorage;

import java.util.LinkedList;
import java.util.List;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.User;

public class UserStorageMemory extends UserStorage{

	
	private LinkedList<User> list;
 	private int userNumber = 1;
	private User admin= new User("huamin","comp3111");	
	
	public UserStorageMemory() {
		list = new LinkedList<User>();
		list.add(admin);
	}
	
	public User getUserFromCredential(String id, String pw) {
		for(User u : list) {
			if( (u.getID().equals(id)) && (u.getPW().equals(pw)) )
				return u;
		}
		return null;
	}

	
	public boolean SaveUser(String id, String pw){
		if(!isDuplicate(id)){
			User user = new User(id, pw);
			list.add(user);
			userNumber++;
			return true;
		}
		return false;
		
	}
	
	private boolean isDuplicate(String id) {
		for(User u : list){
			if(u.getID().equals(id))
				return true;
		}
		return false;
	}
	
	@Override
	public List<User> getUserList() {
		return list;
	}
	
	public boolean RemoveUser(String ID){	
		if(ID!=null){
			for(User u : list){
				if(u.getID().equals(ID)) {
					userNumber--;
					return list.remove(u);
				}
			}
		}
		System.out.println("RemoveUser: user is null");
		return false;
	}
	

	@Override
	public User getAdmin() {
		return admin;
	}

}
