package hkust.cse.calendar.unit;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/* This class represents the time span between two points of time */
public class TimeSpan implements Serializable {
	
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	/* The starting time of the time span */
	private Timestamp mStartTime;
	/* The ending time of the time span */
	private Timestamp mEndTime;

	/* Create a new TimeSpan object with the specific starting time and ending time */
	public TimeSpan(Timestamp start, Timestamp end) {
		int startYear = start.getYear() - 1900;
		start.setYear(startYear);
		
		int endYear = end.getYear() - 1900;
		end.setYear(endYear);
		
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
		if (d.EndTime().before(mStartTime) || d.EndTime().equals(mStartTime))	// If the time span ends before or at the starting time of this time span then these two time spans do not overlap
			return false;
		if (d.StartTime().equals(mEndTime) || mEndTime.before(d.StartTime()))	// If the time span starts after or at the ending time of this time span then these two time spans do not overlap
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
}
