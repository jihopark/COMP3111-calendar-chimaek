package hkust.cse.calendar.apptstorage;

import hkust.cse.calendar.diskstorage.FileManager;
import hkust.cse.calendar.diskstorage.JsonStorable;
import hkust.cse.calendar.invite.InviteController;
import hkust.cse.calendar.locationstorage.LocationController;
import hkust.cse.calendar.notification.NotificationController;
import hkust.cse.calendar.time.TimeController;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.GroupAppt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.Notification;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.userstorage.UserController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;

public class ApptStorage implements JsonStorable {

	public HashMap<String, LinkedList<Appt>> mAppts;		//a hashmap to save every thing to it, write to memory by the memory based storage implementation	
	public HashMap<String, LinkedList<GroupAppt>> mGroupAppts;

	//private LinkedList<Appt> list;
	private int apptNumber = 0;
	private int apptIDCount = 1;

	public ApptStorage()
	{
		mAppts = new HashMap<String, LinkedList<Appt>>();
		mGroupAppts = new HashMap<String, LinkedList<GroupAppt>>();
	}

	private boolean hasNoAppts(User user){
		return (mAppts.get(user.toString())==null || mAppts.get(user.toString()).size()==0)
				&& (mGroupAppts.get(user.toString())==null || mGroupAppts.get(user.toString()).size()==0);	 
	}

	private List<Appt> getAllAppts(User user){
		LinkedList<Appt> appts = new LinkedList<Appt>();
		if (mAppts.get(user.toString())!=null)
			appts.addAll(mAppts.get(user.toString()));
		if (mGroupAppts.get(user.toString())!=null)
			appts.addAll(mGroupAppts.get(user.toString()));
		return appts;
	}

	public boolean checkOverlaps(User user, Appt appt){
		//CHECK THE APPT STORAGE
		if (hasNoAppts(user))
			return false;
		for (Appt a : getAllAppts(user)){
			if (!a.equals(appt) && a.TimeSpan().Overlap(appt.TimeSpan())){
				System.out.println("\nApptStorage/checkOverlaps: Overlaps! ");
				return true;
			}
		}
		
		//CHECK THE INVITE STORAGE
		if(InviteController.getInstance().checkOverlaps(user,appt)){
			return true;
		}
		
		return false;
	}
	
	public boolean hasOverlapsInLocation(Appt appt, TimeSpan t, Location loc){
		
		if (loc.getID() == 0)
			return false;
		
		if(loc.getAppointmentCount()!=0){
			LinkedList<User> users = new LinkedList<User>();
			users=(LinkedList<User>) UserController.getInstance().getUserList();
			if(users!=null)
			{
				for(User u : users)
				{
					for(Appt a : getAllAppts(u)){
						if(!appt.equals(a) && a.getLocation().getID()==loc.getID() && a.TimeSpan().Overlap(t)){
							if 	(appt instanceof GroupAppt && a instanceof GroupAppt 
									&& ((GroupAppt)appt).isSameAttendList(((GroupAppt)a).getAttendList())){
								System.out.println("ApptStorage/hasOverlapsInLocation Same GroupAppt Not Overlap");
							}
							else{
								System.out.println("ApptStorage/hasOverlapsInLocation Location Overlaps");
								return true;
							}
						}
					}
				}
			}
		}
		return false;
		
	}

	public boolean checkOverlaps(User user, List<Appt> appts){
		for (Appt a : appts){
			if (checkOverlaps(user, a))
				return true;
		}
		return false;
	}

	
	public boolean SaveAppt(User user, Appt appt) {
		if (appt!=null && appt.isValid()){
			if (!checkOverlaps(user, appt) && TimeController.getInstance().isNotPast(appt) && !hasOverlapsInLocation(appt, appt.getTimeSpan(),appt.getLocation())){
				if (appt instanceof GroupAppt){
					if (mGroupAppts.get(user.toString())==null){
						mGroupAppts.put(user.toString(), new LinkedList<GroupAppt>());
					}
					mGroupAppts.get(user.toString()).add((GroupAppt) appt);
				}
				else{
					if (mAppts.get(user.toString())==null){
						mAppts.put(user.toString(), new LinkedList<Appt>());
					}
					mAppts.get(user.toString()).add(appt);
				}
				apptNumber++;
				System.out.println("ApptStorage/SaveAppt : Saved Appt #"+appt.getID());
				if(!appt.getLocation().getName().equals("-")){
					LocationController.getInstance().increaseLocationCount(appt.getLocation());
				}

				return true;

			}
		}
		System.out.println("ApptStorage/SaveAppt : Failed to Save Appt #"+appt.getID());
		return false;
	}

	//Only For Testing Purpose. Don't Use for Real Use
	public boolean SavePastAppt(User user, Appt appt) {
		if (appt!=null && appt.isValid()){
			if (!checkOverlaps(user, appt)){
				if (mAppts.get(user.toString())==null){
					mAppts.put(user.toString(), new LinkedList<Appt>());
				}
				mAppts.get(user.toString()).add(appt);
				apptNumber++;
				System.out.println("ApptStorage/SavePastAppt : Saved Past Appt #"+appt.getID());
				return true;
			}
		}
		return false;
	}

	
	@Deprecated
	public Appt[] RetrieveAppts(TimeSpan d) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public List<Appt> RetrieveApptsInList(User user, TimeSpan d) {
		ArrayList<Appt> retrieveList = new ArrayList<Appt>();
		if (!hasNoAppts(user)){
			for (Appt a : getAllAppts(user)){
				if (a.TimeSpan().Overlap(d)){
					retrieveList.add(a);
				}
			}
		}

		return retrieveList;
	}



	
	public List<Appt> RetrieveApptsInList(User user) {
		if (!hasNoAppts(user))
			return getAllAppts(user);
		return new ArrayList<Appt>();
	}

	public List<Appt> RetrievePublicApptsInList(User user) {
		ArrayList<Appt> retrievePublicList = new ArrayList<Appt>();
		if(!hasNoAppts(user)){
			for (Appt a: getAllAppts(user)){
				if(a.getisPublic())
					retrievePublicList.add(a);
			}
		}
		return retrievePublicList;
	}

	
	@Deprecated	
	public Appt[] RetrieveAppts(User entity, TimeSpan time) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Appt RetrieveAppts(int apptID) {
		System.out.println("ApptStorage/RetrieveAppts Retrieve #" + apptID);
		for (String userID : mAppts.keySet()){
			for (Appt a : mAppts.get(userID)){
				if (a.getID() == apptID)
					return a;
			}
		}
		for (String userID : mGroupAppts.keySet()){
			for (Appt a : mGroupAppts.get(userID)){
				if (a.getID() == apptID)
					return a;
			}
		}
		System.out.println("ApptStorage/RetrieveAppts Not Found");

		return null;
	}

	
	public boolean RemoveAppt(User user, Appt appt) {
		if (hasNoAppts(user))
			return false;
		for (Appt a : getAllAppts(user)){
			if (a.equals(appt) && TimeController.getInstance().isNotPast(appt)){
				if (appt instanceof GroupAppt)
					mGroupAppts.get(user.toString()).remove(a);
				else
					mAppts.get(user.toString()).remove(a);
				
				apptNumber--;
				System.out.println("ApptStorage/RemoveAppt : Removed Appt #"+appt.getID());
				if(!a.getLocation().getName().equals("-"))
					LocationController.getInstance().decreaseLocationCount(a.getLocation());

				removeNotification(user,a.getNotification());
				return true;
			}
		}
		System.out.println("ApptStorage/RemoveAppt : Fail to remove Appt #"+appt.getID());
		return false;
	}

	
	public void removeNotification(User user,Notification noti){
		if (noti!=null){
			NotificationController.getInstance().removeNotification(user, noti);
		}
	}

	
	public void saveNotification(User user,Notification noti){
		if (noti!=null){
			NotificationController.getInstance().saveNewNotification(user, noti);
		}
	}
	public int getTotalApptCount(){
		return apptNumber;
	}

	
	public int getIDCount(){ return apptIDCount++; }

	/*
	 * For Disk Storage
	 * */

	public String getFileName(){
		return "DISK_APPT.txt";
	}

	public Object loadFromJson(){
		Gson gson = new Gson();
		String json = FileManager.getInstance().loadFromFile(getFileName());
		if (json.equals("")) return null;
		return gson.fromJson(json, ApptStorage.class);
	}

	public void saveToJson(){
		Gson gson = new Gson();
		FileManager.getInstance().writeToFile(gson.toJson(this), getFileName());
	}


}
