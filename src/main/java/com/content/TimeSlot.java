package com.content;

public class TimeSlot {
	int timeSlot;
	String studentId;
	public TimeSlot(String sid, int slot) {
		timeSlot = slot;
		studentId = sid;
	}
	public TimeSlot() {
		
	}
	public int getTimeSlot() {
		return timeSlot;
	}
	public void setTimeSlot(int timeSlot) {
		this.timeSlot = timeSlot;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	
}
