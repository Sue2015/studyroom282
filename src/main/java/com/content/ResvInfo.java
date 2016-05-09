package com.content;

import java.util.ArrayList;
import java.util.List;

public class ResvInfo {
	String roomNumber;
	List<TimeSlot> timeSlots;
	public ResvInfo(String sid, String rid, int slot) {
		this.roomNumber = rid;
		timeSlots = new ArrayList<TimeSlot>();
		timeSlots.add(new TimeSlot(sid, slot));
	}
	public ResvInfo() {
		
	}
	public String getRoomNumber() {
		return roomNumber;
	}
	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}
	public List<TimeSlot> getTimeSlots() {
		return timeSlots;
	}
	public void setTimeSlots(List<TimeSlot> timeSlots) {
		this.timeSlots = timeSlots;
	}
	
}
