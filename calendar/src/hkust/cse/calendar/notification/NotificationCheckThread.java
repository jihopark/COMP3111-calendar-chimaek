package hkust.cse.calendar.notification;

import hkust.cse.calendar.gui.CalGrid;
import hkust.cse.calendar.time.TimeController;
import hkust.cse.calendar.userstorage.UserController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

public class NotificationCheckThread extends Thread {

	public static Date currentTime = new Date();
	public CalGrid calGrid;
	private List<NotificationTime> deliveredNotification = new ArrayList<NotificationTime>();
	
	public NotificationCheckThread(CalGrid cal)
	{
		calGrid = cal;
	}
	
	
	public void run() {
		while(true) {
			try {
				Thread.sleep(1000);
				currentTime = TimeController.getInstance().getCurrentTimeInDate();
				calGrid.updateCalGridTitleClock(currentTime);
				//System.out.println("Current Time is: " + currentTime);

				List<NotificationTime> notifications = NotificationController.getInstance().retrieveNotification(UserController.getInstance().getAdmin(), currentTime);
				if(notifications.size() == 0)
				{
					//System.out.println("There is no notification!");
				}
				if (notifications.size()!=0){
					for (NotificationTime time : notifications){
						if (!deliveredNotification.contains(time) && time.getParent().isPending() != true){
							System.out.println((time == null)+"");
							System.out.println((time.getParent()==null)+"");
							System.out.println((time.getParent().getAppt()==null)+"");				
							
							JOptionPane.showMessageDialog(null, "You have an appointment!\n" + time.getParent().getName() + "at " + time.getParent().getAppt().TimeSpan());
							deliveredNotification.add(time);
						}
					}
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			
		}
	}
}
