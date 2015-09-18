package hkust.cse.calendar.notification;

import hkust.cse.calendar.gui.CalGrid;
import hkust.cse.calendar.time.TimeController;
import hkust.cse.calendar.userstorage.UserController;

import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

public class NotificationCheckThread extends Thread {

	public static Date currentTime = new Date();
	public CalGrid calGrid;
	
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

				List<NotificationTime> notifications = NotificationController.getInstance().retrieveNotification(UserController.getInstance().getCurrentUser(), currentTime);
				if (notifications.size()!=0){
					for (NotificationTime time : notifications){
						if ((!time.getParent().isDelivered()) && (!time.getParent().isPending())){
							NotificationController.getInstance().setDelivered(time.getParent());
							JOptionPane.showMessageDialog(null, "You have an appointment!\n" + time.getParent().getName() + " at " + time.getParent().getAppt().TimeSpan());
						}
					}
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			
		}
	}
}
