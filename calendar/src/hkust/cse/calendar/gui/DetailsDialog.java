package hkust.cse.calendar.gui;

import hkust.cse.calendar.time.TimeController;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.GroupAppt;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;


public class DetailsDialog extends JFrame implements ActionListener {
	private JButton exitBut;
	private JTextArea area;

	public DetailsDialog(String msg, String title) {
		paintContent(title);
		Display(msg);
		this.setSize(500, 350);
		pack();
	}

	public DetailsDialog(Appt appt, String title) {
		paintContent(title);
		this.setSize(500, 350);
		Display(appt);
		pack();

	}

	public void paintContent(String title) {

		Container content = getContentPane();
		setTitle(title);

		JScrollPane panel = new JScrollPane();
		Border border = new TitledBorder(null, "Information");
		panel.setBorder(border);

		area = new JTextArea(25, 40);
		//		area.setPreferredSize(new Dimension(400, 300));

		panel.getViewport().add(area);

		exitBut = new JButton("Exit");
		exitBut.addActionListener(this);

		JPanel p2 = new JPanel();
		p2.setLayout(new FlowLayout(FlowLayout.CENTER));

		p2.add(exitBut);

		content.add("Center", panel);
		content.add("South", p2);

	}

	public void Display(String msg) {
		area.setFont(new Font("bold", Font.BOLD, 14));

		if (msg.length() == 0)
			msg = new String("No Information Inputed");
		area.setText(msg);
		area.setEditable(false);
	}

	public void Display(Appt appt) {

		Timestamp startTime = appt.TimeSpan().StartTime();
		Timestamp endTime = appt.TimeSpan().EndTime();
		String time = TimeController.getInstance().getHourFrom(startTime) + ":";
		if (TimeController.getInstance().getMinuteFrom(startTime) == 0)
			time = time + "00" + " - " + TimeController.getInstance().getHourFrom(endTime) + ":";
		else
			time = time + TimeController.getInstance().getMinuteFrom(startTime) + " - " + TimeController.getInstance().getHourFrom(endTime) + ":";
		if (TimeController.getInstance().getMinuteFrom(endTime) == 0)
			time = time + "00";
		else
			time = time + TimeController.getInstance().getMinuteFrom(endTime);

		area.setText("Appointment Information \n");
		area.append("Title: " + appt.getTitle() + "\n");
		area.append("Time: " + time + "\n");

		area.append("Location: " + appt.getLocation() + "\n");

		//REPEAT DETAILS
		area.append("Repeated: ");
		switch(appt.getRepeatType())
		{
		case 0:area.append("One-Time \n"); break;
		case 1:area.append("Daily \n"); break;
		case 2:area.append("Weekly \n"); break;
		case 3:area.append("Monthly \n"); break;
		}

		//NOTIFICATION DETAILS
		if(appt.getNotification() != null)
		{
			area.append("Notifications at: " + appt.getNotification().getNotificationTimeObj().getNotificationTime());
		}
		else
		{
			area.append("Notifications at: -");
		}
		
		if(appt.getisPublic()){
			area.append("\nisPublic: YES");
		}
		else{
			area.append("\nisPublic: NO");
		}
		if (appt instanceof GroupAppt){
			area.append("\nParticipants:\n");
			area.append("  Owner:");
			area.append("  "+((GroupAppt)appt).getOwner());
			area.append("\n  Attend:");
			LinkedList<String> attendList = ((GroupAppt)appt).getAttendList();
			if(attendList != null)
			{
				for(int i = 0; i < attendList.size(); i++)
				{
					area.append("  " + attendList.get(i));
				}
			}
		}

		area.append("\n\nDescription: \n" + appt.getInfo());
		area.setEditable(false);
	}

	public void Display(Vector[] vs, User[] entities) {
		if (vs == null || entities == null)
			return;
		String temp = ((TimeSpan) vs[0].elementAt(0)).StartTime().toString();
		area.setText("Available Time For Selected participants and room ("
				+ temp.substring(0, temp.lastIndexOf(" ")) + ")\n\n\n");
		String temp1 = null;
		String temp2 = null;

		for (int i = 0; i < entities.length; i++) {
			area.append((i + 1) + ". " + entities[i].ID() + " :\n\n");
			for (int j = 0; j < vs[i].size(); j++) {
				temp1 = ((TimeSpan) vs[i].elementAt(j)).StartTime().toString();
				temp2 = ((TimeSpan) vs[i].elementAt(j)).EndTime().toString();
				area.append("   > From: "
						+ temp1.substring(0, temp1.lastIndexOf(':')) + "  To: "
						+ temp2.substring(0, temp2.lastIndexOf(':')) + "\n");

			}
			area.append("\n");

		}
		area.setEditable(false);

	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == exitBut) {
			dispose();
		}
	}

}
