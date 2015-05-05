package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptController;
import hkust.cse.calendar.apptstorage.ApptStorage;
import hkust.cse.calendar.time.TimeController;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.userstorage.UserController;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Timestamp;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;


public class TimeMachineDialog extends JFrame implements ActionListener
{
    private JLabel yearLabel;
    private JTextField yearField;
    private JLabel monthLabel;
    private JTextField monthField;
    private JLabel dayLabel;
    private JTextField dayField;
	private JButton OnButton;
	private JButton OffButton;
	private JLabel startTimeHourLabel;
	private JTextField startTimeHourField;
	private JLabel startTimeMinuteLabel;
	private JTextField startTimeMinuteField;
	private TimeController timeController = new TimeController();
	public TimeMachineDialog()		// Create a dialog to log in
	{
		
		setTitle("Time Machine");
		/*
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});*/
		
		Container contentPane;
		contentPane = getContentPane();
		
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
		Border dateBorder = new TitledBorder(null, "DATE");
		top.setBorder(dateBorder);
		yearLabel = new JLabel("YEAR: ");
		top.add(yearLabel);
		yearField = new JTextField(6);
		top.add(yearField);
		monthLabel = new JLabel("MONTH: ");
		top.add(monthLabel);
		monthField = new JTextField(4);
		top.add(monthField);
		dayLabel = new JLabel("DAY: ");
		top.add(dayLabel);
		dayField = new JTextField(4);
		top.add(dayField);
		startTimeHourLabel = new JLabel("Hour");
		top.add(startTimeHourLabel);
		startTimeHourField = new JTextField(4);
		top.add(startTimeHourField);
		startTimeMinuteLabel = new JLabel("Minute");
		top.add(startTimeMinuteLabel);
		startTimeMinuteField = new JTextField(4);
		top.add(startTimeMinuteField);


		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

		OnButton = new JButton("Turn on Time Machine");
		OnButton.addActionListener(this);
		OffButton = new JButton("Turn Off Time Machine");
		OffButton.addActionListener(this);

		OnButton.setEnabled(!timeController.getInstance().isOnTimeMachineMode());
		OffButton.setEnabled(timeController.getInstance().isOnTimeMachineMode());
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(OnButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(OffButton);
				
		
		contentPane.add(top, BorderLayout.CENTER);
		contentPane.add(buttonPane, BorderLayout.PAGE_END);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);	
		
	}
	

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == OnButton)
		{
			if(	isNull(yearField.getText()) || isNull(monthField.getText()) || isNull(dayField.getText()) ||
				isNull(startTimeHourField.getText()) || isNull(startTimeMinuteField.getText())) {
				System.out.println("Time Machine can not be turned on. Please Check for Null Values");
			} else {
				int year = Integer.parseInt(yearField.getText());
				int month = Integer.parseInt(monthField.getText());
				int Day = Integer.parseInt(dayField.getText());
				int hour = Integer.parseInt(startTimeHourField.getText());
				int minute = Integer.parseInt(startTimeMinuteField.getText());
				
				if(TimeController.getInstance().checkValidInput(year,month,Day,hour,minute)){
					Timestamp changedTime = TimeController.getInstance().dateInputToTimestamp(year, month, Day, hour, minute, 0);
	
					// When button is clicked, change time		
					OnButton.setEnabled(timeController.getInstance().isOnTimeMachineMode());
					timeController.getInstance().enableTimeMachineMode();
					OffButton.setEnabled(timeController.getInstance().isOnTimeMachineMode());				Date date = new Date(changedTime.getTime());
					timeController.getInstance().setTimeMachine(date);
					
					System.out.println("Time Machine is Turned ON: " + timeController.getInstance().isOnTimeMachineMode());
				}
				else{
					JOptionPane.showMessageDialog(this,"Please input proper date/time","Error",JOptionPane.ERROR_MESSAGE);
				}
			}

		}
		else if(e.getSource() == OffButton)
		{
			OffButton.setEnabled(!timeController.getInstance().isOnTimeMachineMode());
			timeController.getInstance().disableTimeMachineMode();
			OnButton.setEnabled(!timeController.getInstance().isOnTimeMachineMode());
			System.out.println("Time Machine is Turned OFF: " + timeController.getInstance().isOnTimeMachineMode());
		}
	}


	private boolean isNull(String text) {
		// TODO Auto-generated method stub
		if(text.equals("")) {
			return true;
		} else {
			return false;
		}
	}

}
