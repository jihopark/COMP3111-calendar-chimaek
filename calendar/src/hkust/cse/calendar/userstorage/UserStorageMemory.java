package hkust.cse.calendar.userstorage;

import hkust.cse.calendar.diskstorage.FileManager;
import hkust.cse.calendar.diskstorage.JsonStorable;
import hkust.cse.calendar.notification.NotificationStorageMemory;
import hkust.cse.calendar.unit.User;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;

public class UserStorageMemory extends UserStorage implements JsonStorable{

	
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
			saveToJson();
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
					boolean tmp = list.remove(u);
					if (tmp) saveToJson();
					return tmp;
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
	
	/*
	 * For Disk Storage
	 * */
	
	public String getFileName(){
		return "DISK_USER.txt";
	}
	
	public Object loadFromJson(){
		Gson gson = new Gson();
		String json = FileManager.getInstance().loadFromFile(getFileName());
		if (json.equals("")) return null;
		return gson.fromJson(json, UserStorageMemory.class);
	}
	
	public void saveToJson(){
		Gson gson = new Gson();
		FileManager.getInstance().writeToFile(gson.toJson(this), getFileName());
	}

}
