package hkust.cse.calendar.userstorage;

import hkust.cse.calendar.diskstorage.FileManager;
import hkust.cse.calendar.diskstorage.JsonStorable;
import hkust.cse.calendar.unit.DeleteRequest;
import hkust.cse.calendar.unit.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;

public class UserStorageMemory extends UserStorage implements JsonStorable{

	
	private LinkedList<User> list;
 	private int userNumber = 1;
 	private LinkedList<DeleteRequest> deleteRequestList;
	
	public UserStorageMemory() {
		list = new LinkedList<User>();
	}
	
	public User getUserFromCredential(String id, String pw) {
		for(User u : list) {
			if( (u.getID().equals(id)) && (u.getPW().equals(pw)) )
				return u;
		}
		return null;
	}
	
	@Override
	public User getUser(String id) {
		for(User u : list) {
			if( (u.getID().equals(id)) )
				return u;
		}
		return null;
	}

	
	public boolean SaveUser(String id, String pw, String fName, String lName, String email, Boolean admin){
		if(!isDuplicate(id)){
			User user = new User(id, pw, fName, lName, email, admin);
			list.add(user);
			userNumber++;
			saveToJson();
			return true;
		}
		return false;	
	}	
	
	@Override
	public boolean ModifyUser(User current, User before) {
		// TODO Auto-generated method stub
		for(User a: list){
			if(a.equals(current)){
				a.setEmailAddress(before.getEmailAddress());
				a.setFirstName(before.getFirstName());
				a.setLastName(before.getLastName());
				a.Password(before.getPW());
				saveToJson();
				return true;
			}
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
			System.out.println("UserStorageMemory/RemoveUser Removed " + ID);
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
	
	public List<DeleteRequest> getDeleteRequests(User user){
		ArrayList<DeleteRequest> list = new ArrayList<DeleteRequest>();
		if (deleteRequestList!=null){
			for (DeleteRequest r : deleteRequestList){
				if (r.getRequestUser().equals(user.ID()))
						list.add(r);
			}
		}
		return list;
	}
	
	public boolean addDeleteRequest(DeleteRequest request){
		if (deleteRequestList==null) deleteRequestList = new LinkedList<DeleteRequest>();
		
		return deleteRequestList.add(request);
	}
	
	public boolean removeDeleteRequest(DeleteRequest request){
		boolean deleteUser;
		
		if (deleteRequestList==null) return false;
		if (request.getResponse()){
			//If it is the last true Response
			deleteUser = true;
			for (DeleteRequest r : deleteRequestList){
				if (r.getDeleteUser().equals(request.getDeleteUser())){
					deleteUser = false;
					break;
				}
			}
		}
		else{
			deleteUser = false;
			Iterator<DeleteRequest> itr = deleteRequestList.iterator();
			while (true){
				DeleteRequest r = itr.next();
				if (r.getDeleteUser().equals(request.getDeleteUser()))
					itr.remove();
				
				if (!itr.hasNext())
					break;
			}
		}
		if (deleteUser)
			RemoveUser(request.getDeleteUser());
		return true;
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
