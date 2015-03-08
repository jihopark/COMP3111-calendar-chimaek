package hkust.cse.calendar.tests;

import java.util.ArrayList;

import javax.swing.DefaultListModel;

import hkust.cse.calendar.gui.ManageLocationDialog;
import hkust.cse.calendar.unit.Location;

public class GUITest {

	public static DefaultListModel<Location> getLocationListTest(){
		
		//if location is already registered
		
		Location location1= new Location("RM2464",0);
		Location location2= new Location("RM3002", 1);
		Location location3= new Location("LSK1026", 2);
		DefaultListModel<Location> locationlist = new DefaultListModel<Location>();
		
		locationlist.addElement(location1);
		locationlist.addElement(location2);
		locationlist.addElement(location3);
		
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
	            }	
	        });

	}

}
