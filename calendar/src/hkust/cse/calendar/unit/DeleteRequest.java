package hkust.cse.calendar.unit;

import hkust.cse.calendar.userstorage.UserController;

public class DeleteRequest {
	private String deleteUserID;
	private String requestUserID;
	private boolean response = false;
	
	public DeleteRequest(String delete, String request){
		deleteUserID = delete;
		requestUserID = request;
	}
	
	public String getDeleteUser(){
		return deleteUserID;
	}
	
	public String getRequestUser(){
		return requestUserID;
	}
	
	public void acceptRequest(){
		response = true;
	}
	
	public boolean getResponse(){
		return response;
	}
	
}
