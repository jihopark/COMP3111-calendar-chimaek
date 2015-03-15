package hkust.cse.calendar.tests;

import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;

import hkust.cse.calendar.gui.ManageLocationDialog;
import hkust.cse.calendar.locationstorage.LocationController;
import hkust.cse.calendar.unit.Location;

public class GUITest {

	public static DefaultListModel <Location> getLocationListTest(){
		
		//if location is already registered
		
		Location location1= new Location();
		Location location2= new Location();
		Location location3= new Location();
		
		//location1.setID(0);
		//location2.setID(1);
		//location3.setID(2);
		location1.setName("test1");
		location2.setName("test2");
		location3.setName("test3");
		
		//System.out.println(LocationController.getInstance().saveNewLocation(location1));
		LocationController.getInstance().saveNewLocation(location1);
		LocationController.getInstance().saveNewLocation(location2);
		LocationController.getInstance().saveNewLocation(location3);
		
		LocationController.getInstance().printList();
		
		
		return LocationController.getInstance().getLocationList();
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
