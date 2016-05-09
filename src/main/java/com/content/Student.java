package com.content;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "Student")
public class Student {
	@Id
	String id;
	int curNum;
	int maxNum;
	String studentName;
	String password;
	String role;

	List<StudentResv> reservation;
	public Student() {

	}
	public Student(String id, int curNum, int maxNum, String name, String password) {
		this.id = id;
		this.curNum = curNum;
		this.maxNum = maxNum;
		this.studentName = name;
		this.password = password;
		this.reservation = new ArrayList<StudentResv>();
	}
	public String getId() {
		return id;
	}

	public void setId(String studentId) {
		this.id = studentId;
	}
	public int getCurNum() {
		return curNum;
	}
	public void setCurNum(int curNum) {
		this.curNum = curNum;
	}
	public int getMaxNum() {
		return maxNum;
	}
	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<StudentResv> getReservation() {
		return reservation;
	}
	public void setReservation(List<StudentResv> reservation) {
		this.reservation = reservation;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

}