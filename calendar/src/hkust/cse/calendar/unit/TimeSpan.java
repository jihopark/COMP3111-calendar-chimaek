package hkust.cse.calendar.unit;

import hkust.cse.calendar.time.TimeController;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/* This class represents the time span between two points of time */
public class TimeSpan implements Serializable {
	
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public static SimpleDateFormat onlyDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat onlyTimeFormat = new SimpleDateFormat("HH:mm");
	/* The starting time of the time span */
	private Timestamp mStartTime;
	/* The ending time of the time span */
	private Timestamp mEndTime;

	/* Create a new TimeSpan object with the specific starting time and ending time */
	public TimeSpan(Timestamp start, Timestamp end) {
		int startYear = TimeController.getInstance().getYearFrom(start);
		TimeController.getInstance().setYear(start, startYear);
		
		int endYear = TimeController.getInstance().getYearFrom(end);
		TimeController.getInstance().setYear(end,endYear);
		
		mStartTime = start;
		mEndTime = end;
	}
	
	public TimeSpan(long start, long end){
		mStartTime = new Timestamp(start);
		mEndTime = new Timestamp(end);
	}

	/* Get the starting time */
	public Timestamp StartTime() {
		return mStartTime;
	}

	/* Get the ending time */
	public Timestamp EndTime() {
		return mEndTime;
	}

	/* Check whether a time span overlaps with this time span */
	public boolean Overlap(TimeSpan d) {
		if (d.EndTime().before(mStartTime) || d.EndTime().getTime()/1000 == mStartTime.getTime()/1000)	// If the time span ends before or at the starting time of this time span then these two time spans do not overlap
			return false;
		if (d.StartTime().getTime()/1000 == mEndTime.getTime()/1000 || mEndTime.before(d.StartTime()))	// If the time span starts after or at the ending time of this time span then these two time spans do not overlap
			return false;
		return true;		// Else, the time span overlaps with this time span

	}

	/* Calculate the length of the time span in milliseconds */
	public long TimeLength() {				
		return mEndTime.getTime() - mStartTime.getTime();
	}

	/* Set the starting time */
	public void StartTime(Timestamp s) {
		mStartTime = s;
	}

	/* Set the ending time */
	public void EndTime(Timestamp e) {
		mEndTime = e;
	}
	
	public String toString(){
		return "TimeSpan From " + dateFormat.format(new Date(mStartTime.getTime())) + " to " + dateFormat.format(new Date(mEndTime.getTime()));
	}
	
	public String OnlyTimetoString(){
		return onlyTimeFormat.format(new Date(mStartTime.getTime())) +" ~ "+ onlyTimeFormat.format(new Date(mEndTime.getTime()));
	}
	
	public String OnlyDatetoString(){
		return onlyDateFormat.format(new Date(mStartTime.getTime()));
	}
	
	public void setTimeWithoutChangingDay(TimeSpan timespan){
		Date start = new Date(StartTime().getTime());
		Date newStart = new Date(timespan.StartTime().getTime());
		Date end = new Date(EndTime().getTime());
		Date newEnd = new Date(timespan.EndTime().getTime());
		StartTime(TimeController.getInstance().dateInputToTimestamp(
				TimeController.getInstance().getYearFrom(start), 
				TimeController.getInstance().getMonthFrom(start), 
				TimeController.getInstance().getDateFrom(start), 
				TimeController.getInstance().getHourFrom(newStart), 
				TimeController.getInstance().getMinuteFrom(newStart), 
				TimeController.getInstance().getSecondFrom(newStart)));
		EndTime(TimeController.getInstance().dateInputToTimestamp(
				TimeController.getInstance().getYearFrom(end), 
				TimeController.getInstance().getMonthFrom(end), 
				TimeController.getInstance().getDateFrom(end), 
				TimeController.getInstance().getHourFrom(newEnd), 
				TimeController.getInstance().getMinuteFrom(newEnd), 
				TimeController.getInstance().getSecondFrom(newEnd)));
	}
	
	public int compareTo(TimeSpan thatTimeSpan) {
	    Timestamp thatStartTime = thatTimeSpan.StartTime();
	    if(this.StartTime().after(thatStartTime)) {
	    	return 1;
	    } else if(this.StartTime().before(thatStartTime)) {
	    	return -1;
	    } else {
	    	return 0;
	    }
	}
}
