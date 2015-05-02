package hkust.cse.calendar.gui;


import hkust.cse.calendar.apptstorage.ApptController;
import hkust.cse.calendar.time.TimeController;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

import java.awt.Component;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.TableModel;

public class AvailableTimeSlot_TextWindow extends JFrame{
	private static final long serialVersionUID = 1L;
    private CalGrid parent;
    public AvailableTimeSlot_TextWindow(ArrayList<User> userList, ArrayList<Date> dateList) {
    	setTitle("View Available TimeSlot");
    	this.getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
    	setSize(500, 300);
        // create JTextField
    	JLabel dateLabel= new JLabel("");
    	JLabel timeslot = new JLabel("");

    	ArrayList<TimeSpan> slotList = new ArrayList<TimeSpan>();
    	
    	for(int i =0; i<dateList.size(); i++) {
    		Date tempDate = dateList.get(i);
    		dateLabel.setText( TimeController.getInstance().getYearFrom(tempDate)+"-"+TimeController.getInstance().getMonthFrom(tempDate)+"-"+TimeController.getInstance().getDateFrom(tempDate));
			add(dateLabel);
			slotList = ApptController.getInstance().getSchedulableTimeSpan(userList, dateList.get(i));
    		
			timeslot.setAlignmentX(Component.CENTER_ALIGNMENT);

			for(int j=0; j<slotList.size();j++){
				timeslot.setText(slotList.get(j).OnlyTimetoString());
	    		add(timeslot);
    		}
    	
    	}
    }

}
