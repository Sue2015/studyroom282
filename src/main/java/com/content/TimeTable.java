package com.content;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "TimeTable")
public class TimeTable {
	@Id
	String date;
	List<ResvInfo> timesheet;
	public TimeTable(String date, String sid, String rid, int slot) {
		this.date = date;
		timesheet = new ArrayList<ResvInfo>();
		timesheet.add(new ResvInfo(sid,rid,slot));
	}
	public TimeTable() {
		
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<ResvInfo> getTimesheet() {
		return timesheet;
	}
	public void setTimesheet(List<ResvInfo> timesheet) {
		this.timesheet = timesheet;
	}
	
}
