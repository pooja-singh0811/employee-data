package com.swiss.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.swiss.entity.EmployeeEntity;
import com.swiss.util.EmployeeSalaryCalculation;

public class EmployeeSalaryAnalyzer {

	public static void main(String[] args) {
		String filePath = "C:\\Users\\pooja.am.singh\\Downloads\\employeeDetails.csv"; // CSV file location

		// Read the CSV file and process EmployeeEntity data
		List<EmployeeEntity> employees = EmployeeSalaryCalculation.readEmployeesFromCSV(filePath);
		System.out.println("size"+employees.size());
		// Build the EmployeeEntity hierarchy
		Map<Integer, EmployeeEntity> employeeMap = new HashMap<>();

		EmployeeEntity ceo = null;
		for (EmployeeEntity employeeEntity : employees) {
			employeeMap.put(employeeEntity.getId(), employeeEntity);
			if (employeeEntity.getManagerId() == null) {
				ceo = employeeEntity; 
			} else {
				EmployeeEntity manager = employeeMap.get(employeeEntity.getManagerId());
				if (manager != null) {
					manager.addSubordinate(employeeEntity); // Associate subordinates with managers
				}
			}
		}

		// Check salary validity for managers
		salaryValidationForManagers(employeeMap);

		// employees have a reporting line which is too long, and by how much
		validateLongReportingLine(employeeMap, ceo);
	}

	/**
	 * validates if employee reporting length is abpve 4 levels
	 * 
	 * @param employeeMap
	 * @param ceo
	 */
	public static void validateLongReportingLine(Map<Integer, EmployeeEntity> employeeMap, EmployeeEntity ceo) {
		for (EmployeeEntity employeeEntity : employeeMap.values()) {
			int reportingLineLength = EmployeeSalaryCalculation.calculateReportingLineLength(employeeMap,employeeEntity, ceo, 0);
			if (reportingLineLength > 4) {
				System.out.println(employeeEntity.getFirstName() + " " + employeeEntity.getLastName() + " has a reporting line too long by " +
			(reportingLineLength - 4) + " managers.");
			}
		}
	}

	/**
	 * to validate if managers salary is too high or low
	 * @param employeeMap
	 */
	public static void salaryValidationForManagers(Map<Integer, EmployeeEntity> employeeMap) {
		for (EmployeeEntity manager : employeeMap.values()) {
			if (manager.getManagerId()!= null) {
				double avgSalary = EmployeeSalaryCalculation.calculateAverageSalary(manager);
				if(avgSalary == 0.0) {
					continue;
				}
				//managers earn less than they should, and by how much
				if (manager.getSalary() < 1.2 * avgSalary) {
					System.out.println(manager.getFirstName() + " " + manager.getLastName() + " earns less than required by " 
				+ (1.2 * avgSalary - manager.getSalary()));
				}
				//managers earn more than they should, and by how much
				if (manager.getSalary() > 1.5 * avgSalary) {
					System.out.println(manager.getFirstName() + " " + manager.getLastName() + " earns more than required by " 
				+ (manager.getSalary() - 1.5 * avgSalary));
				}
			}
		}
	}

}
