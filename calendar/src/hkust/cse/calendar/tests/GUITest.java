package hkust.cse.calendar.tests;

import java.util.ArrayList;

import javax.swing.DefaultListModel;

import hkust.cse.calendar.gui.ManageLocationDialog;
import hkust.cse.calendar.unit.Location;

public class GUITest {

	public static ArrayList<Location> getLocationListTest(){
		
		//if location is already registered
		
		Location location1= new Location("RM2464",0);
		Location location2= new Location("RM3002", 1);
		Location location3= new Location("LSK1026", 2);
		ArrayList<Location> locationlist = new ArrayList<Location>();
		
		locationlist.add(location1);
		locationlist.add(location2);
		locationlist.add(location3);
		
		//if location list is empty
		/*
		Location[] locationlist = {};
		*/
		return locationlist;
	}
	
	public static void main(String[] args) {
	        //Schedule a job for the event-dispatching thread:
	        //creating and showing this application's GUI.
			
	        javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                ManageLocationDialog.createAndShowManageLocationDialogGUI();
	                TimeControllerTest.createAndShowGUI();
	            }
	        });
	}
}
