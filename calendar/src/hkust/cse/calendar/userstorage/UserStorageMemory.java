package hkust.cse.calendar.userstorage;

import hkust.cse.calendar.apptstorage.ApptController;
import hkust.cse.calendar.diskstorage.FileManager;
import hkust.cse.calendar.diskstorage.JsonStorable;
import hkust.cse.calendar.notification.NotificationController;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.DeleteRequest;
import hkust.cse.calendar.unit.GroupAppt;
import hkust.cse.calendar.unit.ModifyNotification;
import hkust.cse.calendar.unit.Notification;
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
	private LinkedList<ModifyNotification> modifyNotificationList;

	public UserStorageMemory() {
		list = new LinkedList<User>();
		modifyNotificationList = new LinkedList<ModifyNotification>();
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

		//Check for same request
		for (DeleteRequest r : deleteRequestList){
			if (r.equals(request))
				return false;
		}
		System.out.println("UserStorageMemory/addDeleteRequest Added DeleteRequest to delete " + 
				request.getDeleteUser() + " for " + request.getRequestUser());

		deleteRequestList.add(request);
		saveToJson();
		return true;
	}

	public boolean removeDeleteRequest(DeleteRequest request){
		boolean deleteUser = false;

		if (deleteRequestList==null) return false;
		if (request.getResponse()){
			//If it is the last true Response
			deleteUser = true;
			for (DeleteRequest r : deleteRequestList){
				if (r.getDeleteUser().equals(request.getDeleteUser()) && !r.getRequestUser().equals(request.getRequestUser())){
					deleteUser = false;
					System.out.println("UserStorageMemory/removeDeleteRequest Another User's response needed");
					break;
				}
			}
		}
		Iterator<DeleteRequest> itr = deleteRequestList.iterator();
		while (true){
			DeleteRequest r = itr.next();
			if (r.equals(request)){
				itr.remove();
				System.out.println("UserStorageMemory/removeDeleteRequest Removed DeleteRequest");
			}
			else if (request.getResponse()==false && r.getDeleteUser().equals(request.getDeleteUser())){
				itr.remove();
				System.out.println("UserStorageMemory/removeDeleteRequest Removed DeleteRequest with Same DeleteUser");
			}

			if (!itr.hasNext())
				break;
		}
		System.out.println("UserStorageMemory/removeDeleteRequest " + deleteRequestList);
		if (deleteUser) {
			
			for(Notification n: NotificationController.getInstance().retrieveNotification(UserController.getInstance().getUser(request.getDeleteUser()))) {
				NotificationController.getInstance().removeNotification(UserController.getInstance().getUser(request.getDeleteUser()), n);
				System.out.println("UserStorageMemory/removeUser: Notification Controller Deleted Notificatoin " + n.getName() + " from Appt " + 
						n.getAppointmentTime());
			}
			
			for(Appt a : ApptController.getInstance().RetrieveApptsInList(UserController.getInstance().getUser(request.getDeleteUser()))) {
				if (a instanceof GroupAppt){
					GroupAppt gAppt = (GroupAppt)a;
					//Delete ALL request.getDeleteUser from all related GroupAppts
					ApptController.getInstance().removeUserFromGroupAppt(gAppt,UserController.getInstance().getUser(request.getDeleteUser()));
					ApptController.getInstance().removeAppt(UserController.getInstance().getUser(request.getDeleteUser()), gAppt);
				} else {
					ApptController.getInstance().removeAppt(UserController.getInstance().getUser(request.getDeleteUser()), a);
					System.out.println("UserStorageMemory/removeUser: User Controller Deleted Appt " + a.getTitle() + " from User " + 
							UserController.getInstance().getUser(request.getDeleteUser()));
				}
			}
			
			RemoveUser(request.getDeleteUser());
		}
			
		saveToJson();
		return true;
	}
	
	public List<ModifyNotification> getModifyNotificationList(User user){
		ArrayList<ModifyNotification> list = new ArrayList<ModifyNotification>();
		for (ModifyNotification noti : modifyNotificationList){
			if (noti.getModifyUserID().equals(user.getID()))
				list.add(noti);
		}
		return list;
	}
	
	public void addModifyNotification(ModifyNotification noti){
		System.out.println("UserStorageMemory/addModifyNotification " + noti);
		modifyNotificationList.add(noti);
		saveToJson();
	}
	
	public void removeModifyNotification(ModifyNotification noti){
		System.out.println("UserStorageMemory/removeModifyNotification " + noti);
		modifyNotificationList.remove(noti);
		saveToJson();
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
