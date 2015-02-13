//main method, code starts here
package hkust.cse.calendar.Main;


import javax.swing.UIManager;

import hkust.cse.calendar.gui.LoginDialog;


public class CalendarMain {
	public static boolean logOut = false;
	
	public static void main(String[] args) {
		while(true){
			logOut = false;
			try{
		//	UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			}catch(Exception e){
				
			}
			LoginDialog loginDialog = new LoginDialog();
			while(logOut == false){
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
		