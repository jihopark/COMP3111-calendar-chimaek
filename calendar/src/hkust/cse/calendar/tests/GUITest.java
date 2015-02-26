package hkust.cse.calendar.tests;

import hkust.cse.calendar.gui.ManageLocationDialog;

public class GUITest {

	public static void main(String[] args) {
	        //Schedule a job for the event-dispatching thread:
	        //creating and showing this application's GUI.
	        javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                ManageLocationDialog.createAndShowLocationsDialogGUI();
	            }
	        });

	}

}
