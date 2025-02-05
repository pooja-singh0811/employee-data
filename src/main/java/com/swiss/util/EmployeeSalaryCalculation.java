package com.swiss.util;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.swiss.entity.EmployeeEntity;

public class EmployeeSalaryCalculation {
	
	
	 /**
	 * Reads data from excel sheet and does mapping from csv column to java
	 * @param filePath
	 * @return
	 */
	public static List<EmployeeEntity> readEmployeesFromCSV(String filePath) {
	        List<EmployeeEntity> employees = new ArrayList<>();
	        try (Reader reader = new FileReader(filePath);
	             CSVParser csvParser = createCsvParser(reader)) {

	            for (CSVRecord record : csvParser) {
	                int id = Integer.parseInt(record.get(0));
	                String firstName = record.get(1);
	                String lastName = record.get(2);
	                double salary = Double.parseDouble(record.get(3));
	                Integer managerId = record.get(4).isEmpty() ? null : Integer.parseInt(record.get(4));

	                EmployeeEntity EmployeeEntity = new EmployeeEntity(id, firstName, lastName, salary, managerId);
	                employees.add(EmployeeEntity);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return employees;
	    }
	
	
	 public static CSVParser createCsvParser(Reader reader) throws IOException {
	        return new CSVParser(reader, CSVFormat.DEFAULT);
	    }
    /**
     * calculates the average salary of a manager's subordinates
     * @param manager
     * @return
     */
    public static double calculateAverageSalary(EmployeeEntity manager) {
        if (manager.getSubordinates().isEmpty()) return 0;
         double averageSalary = manager.getSubordinates().stream()
                .mapToDouble(EmployeeEntity::getSalary)
                .average().orElse(0.0);
         return averageSalary;
    }


    /**
     * method to calculate the reporting line length for an EmployeeEntity
     * @param employeeMap
     * @param employeeEntity
     * @param ceo
     * @param depth
     * @return
     */
    public static int calculateReportingLineLength(Map<Integer, EmployeeEntity> employeeMap,EmployeeEntity employeeEntity, EmployeeEntity ceo, int depth) {
        if (employeeEntity.getManagerId() == null || employeeEntity.getManagerId() == ceo.getId()) {
            return depth;
        }
        EmployeeEntity manager = employeeMap.get(employeeEntity.getManagerId()); // Get manager of the EmployeeEntity
        if (manager != null) {
            return calculateReportingLineLength(employeeMap,manager, ceo, depth + 1);
        }
        return depth;
    }
}
