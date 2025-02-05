package com.swiss.entity;

import java.util.ArrayList;
import java.util.List;

public class EmployeeEntity {


	private int id;
	private   String firstName;
	private  String lastName;
	private double salary;
	private Integer managerId;
	List<EmployeeEntity> subordinates = new ArrayList<>();

	public EmployeeEntity(int id, String firstName, String lastName, double salary, Integer managerId) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.salary = salary;
		this.managerId = managerId;
	}

	public void addSubordinate(EmployeeEntity subordinate) {
		this.subordinates.add(subordinate);
	}

	public List<EmployeeEntity> getSubordinates() {
		return subordinates;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public Integer getManagerId() {
		return managerId;
	}

	public void setManagerId(Integer managerId) {
		this.managerId = managerId;
	}

	public void setSubordinates(List<EmployeeEntity> subordinates) {
		this.subordinates = subordinates;
	}
}
