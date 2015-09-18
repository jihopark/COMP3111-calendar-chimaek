package hkust.cse.calendar.unit;

import java.io.Serializable;

public class User implements Serializable {

	private String mID;						// User id
	private String mPassword;				// User password
	private String mFirstName;
	private String mLastName;
	private String mEmailAddress;
	private Boolean mAdmin;
	
	// Getter of the user id
	public String ID() {		
		return mID;
	}
	
	//Getter and Setter for Additional Functions
	public String getFirstName() {
		return mFirstName;
	}
	public String getLastName() {
		return mLastName;
	}
	
	public String getFullName() {
		return (mFirstName+" "+mLastName); 
	}
	public String getEmailAddress(){
		return mEmailAddress;
	}
	
	public void setFirstName(String firstName){
		mFirstName = firstName;
	}
	public void setLastName(String lastName){
		mLastName = lastName;
	}
	public void setEmailAddress(String email){
		mEmailAddress = email;
	}
	

	// Constructor of class 'User' which set up the user id and password
	public User(String id, String pass, String fName, String lName, String email, Boolean isAdmin) {
		mID = id;
		mPassword = pass;
		mFirstName = fName;
		mLastName = lName;
		mEmailAddress = email;
		mAdmin = isAdmin;
	}
	
	
	// Another getter of the user id
	public String getID() {
		return ID();
	}

	// Getter of the user password
	public String getPW() {
		return mPassword;
	}
	
	public Boolean isAdmin() {
		return mAdmin;
	}

	// Setter of the user password
	public void Password(String pass) {
		mPassword = pass;
	}
	
	public void setAdmin() {
		mAdmin = true;
	}
	
	public boolean checkCredentials(String id, char[] pw){
		return mID.equals(id) && checkPassword(pw);
	}
	
	private boolean checkPassword(char[] pw){
		char[] correctPW = mPassword.toCharArray();
		int n = mPassword.length();
		
		if (n!= pw.length)
			return false;
		
		for (int i = 0; i < n ; i++){
			if (pw[i]!= correctPW[i])
				return false;
		}
		
		return true;
	}
	
	public String toString(){
		return mID;
	}
}
