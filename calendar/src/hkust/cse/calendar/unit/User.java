package hkust.cse.calendar.unit;

import java.io.Serializable;

public class User implements Serializable {

	private String mPassword;				// User password
	private String mID;						// User id

	// Getter of the user id
	public String ID() {		
		return mID;
	}

	// Constructor of class 'User' which set up the user id and password
	public User(String id, String pass) {
		mID = id;
		mPassword = pass;
	}

	// Another getter of the user id
	public String toString() {
		return ID();
	}

	// Getter of the user password
	public String Password() {
		return mPassword;
	}

	// Setter of the user password
	public void Password(String pass) {
		mPassword = pass;
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
}
