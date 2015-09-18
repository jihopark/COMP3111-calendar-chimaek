package hkust.cse.calendar.unit;

public class ModifyNotification {
	private String modifyUserID;
	private String adminUserID;
	
	public ModifyNotification(String modify, String admin){
		modifyUserID = modify;
		adminUserID = admin;
	}
	
	public String getModifyUserID(){
		return modifyUserID;
	}
	
	public String getAdminUserID(){
		return adminUserID;
	}
	
	public boolean equals(ModifyNotification noti){
		return noti.getAdminUserID().equals(adminUserID) && noti.getModifyUserID().equals(modifyUserID);
	}
	
	public String toString(){
		return modifyUserID+"/"+adminUserID;
	}	
}
