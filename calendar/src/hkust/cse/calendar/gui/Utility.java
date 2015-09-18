package hkust.cse.calendar.gui;

import hkust.cse.calendar.time.TimeController;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

import java.sql.Timestamp;

import javax.swing.JOptionPane;


public class Utility {

	public static int getNumber(String s) {
		if (s == null)
			return -1;
		if (s.trim().indexOf(" ") != -1) {
			JOptionPane.showMessageDialog(null,
					"Can't Contain Whitespace In Number !");
			return -1;
		}
		int result = 0;
		try {
			result = Integer.parseInt(s);
		} catch (NumberFormatException n) {
			return -1;
		}
		return result;
	}

	public static Appt createDefaultAppt(int currentYear, int currentMonth,
			int currentDate, User me) {
		Appt newAppt = new Appt();
		newAppt.setID(0);
		Timestamp start = new Timestamp(0);
		TimeController.getInstance().setYear(start, currentYear);
		TimeController.getInstance().setMonth(start, currentMonth);
		TimeController.getInstance().setDate(start, currentDate);
		TimeController.getInstance().setHour(start, 9);
		TimeController.getInstance().setMinute(start, 0);

		Timestamp end = new Timestamp(0);
		TimeController.getInstance().setYear(end, currentYear);
		TimeController.getInstance().setMonth(end, currentMonth);
		TimeController.getInstance().setDate(end, currentDate);
		TimeController.getInstance().setHour(end, 9);
		TimeController.getInstance().setMinute(end, 30);

		newAppt.setTimeSpan(new TimeSpan(start, end));
		User[] temp = new User[1];
		temp[0] = me;
		// newAppt.setParticipants(temp);

		newAppt.setTitle("Untitled");
		newAppt.setInfo("Input description of this appointment");
		return newAppt;
	}

	public static Appt createDefaultAppt(int currentYear, int currentMonth,
			int currentDate, User me, int startTime) {
		Appt newAppt = new Appt();
		newAppt.setID(0);
		Timestamp start = new Timestamp(0);
		TimeController.getInstance().setYear(start, currentYear);
		TimeController.getInstance().setMonth(start, currentMonth);
		TimeController.getInstance().setDate(start, currentDate);
		TimeController.getInstance().setHour(start, startTime / 60);
		TimeController.getInstance().setMinute(start, startTime % 60);

		int dur = startTime + 60;
		Timestamp end = new Timestamp(0);
		TimeController.getInstance().setYear(end, currentYear);
		TimeController.getInstance().setMonth(end, currentMonth);
		TimeController.getInstance().setDate(end, currentDate);
		TimeController.getInstance().setHour(end, dur / 60);
		TimeController.getInstance().setMinute(end, dur % 60);

		newAppt.setTimeSpan(new TimeSpan(start, end));
		User[] temp = new User[1];
		temp[0] = me;

		newAppt.setTitle("Untitled");
		newAppt.setInfo("Input description of this appointment");
		return newAppt;
	}
}
