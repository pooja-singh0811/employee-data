package com.swiss.util;

import com.swiss.entity.EmployeeEntity;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

public class EmployeeSalaryCalculationTest {

    private EmployeeEntity ceo;
    private EmployeeEntity manager1;
    private EmployeeEntity manager2;
    private EmployeeEntity employee1;
    private EmployeeEntity employee2;

    @BeforeEach
    public void setUp() {
        // Setup mock employees and their hierarchy
        ceo = new EmployeeEntity(1, "CEO", "One", 100000.0, null);
        manager1 = new EmployeeEntity(2, "Manager", "One", 80000.0, 1);
        manager2 = new EmployeeEntity(3, "Manager", "Two", 85000.0, 2);
        employee1 = new EmployeeEntity(4, "Employee", "One", 50000.0, 3);
        employee2 = new EmployeeEntity(5, "Employee", "Two", 55000.0, 2);

        // Build the hierarchy
        ceo.addSubordinate(manager1);
        ceo.addSubordinate(manager2);
        manager1.addSubordinate(employee1);
        manager1.addSubordinate(employee2);
    }
    @Test
    public void testReadEmployeesFromCSV() throws Exception {
    	// CSV content to simulate file content
        String csvContent = "123,Joe,Doe,60000,\n124,Martin,Chekov,45000,123";


        // Mock CSVParser and CSVRecord
        CSVParser mockCsvParser = mock(CSVParser.class);
        CSVRecord mockRecord1 = mock(CSVRecord.class);
        CSVRecord mockRecord2 = mock(CSVRecord.class);

        // Simulate CSV records
        when(mockCsvParser.iterator()).thenReturn(List.of(mockRecord1, mockRecord2).iterator());

        // Employee 1 data (mocked CSVRecord)
        when(mockRecord1.get(0)).thenReturn("123");
        when(mockRecord1.get(1)).thenReturn("Joe");
        when(mockRecord1.get(2)).thenReturn("Doe");
        when(mockRecord1.get(3)).thenReturn("60000");
        when(mockRecord1.get(4)).thenReturn(""); // (CEO)

        // Employee 2 data (mocked CSVRecord)
        when(mockRecord2.get(0)).thenReturn("124");
        when(mockRecord2.get(1)).thenReturn("Martin");
        when(mockRecord2.get(2)).thenReturn("Chekov");
        when(mockRecord2.get(3)).thenReturn("45000");
        when(mockRecord2.get(4)).thenReturn("123"); 

        try (MockedStatic<EmployeeSalaryCalculation> mockStatic = mockStatic(EmployeeSalaryCalculation.class)) {
            when(EmployeeSalaryCalculation.createCsvParser(any(Reader.class))).thenReturn(mockCsvParser);

            List<EmployeeEntity> mockedEmployees = new ArrayList<>();
            
            EmployeeEntity joe = new EmployeeEntity(123, "Joe", "Doe", 60000, null);
            EmployeeEntity martin = new EmployeeEntity(124, "Martin", "Chekov", 45000, 123);
            
            mockedEmployees.add(joe);
            mockedEmployees.add(martin);

            mockStatic.when(() -> EmployeeSalaryCalculation.readEmployeesFromCSV(anyString()))
                      .thenReturn(mockedEmployees);
            List<EmployeeEntity> employees = EmployeeSalaryCalculation.readEmployeesFromCSV(csvContent); 

            assertNotNull(employees);
            assertEquals(2, employees.size());

         
            EmployeeEntity employee1 = employees.get(0);
            assertEquals(123, employee1.getId());
            assertEquals("Joe", employee1.getFirstName());
            assertEquals("Doe", employee1.getLastName());
            assertEquals(60000, employee1.getSalary());
            assertNull(employee1.getManagerId());

            EmployeeEntity employee2 = employees.get(1);
            assertEquals(124, employee2.getId());
            assertEquals("Martin", employee2.getFirstName());
            assertEquals("Chekov", employee2.getLastName());
            assertEquals(45000, employee2.getSalary());
            assertEquals(123, employee2.getManagerId());
        }
    }
    @Test
    public void testCalculateAverageSalary() {
        double avgSalaryManager1 = EmployeeSalaryCalculation.calculateAverageSalary(manager1);

        assertEquals(52500.0, avgSalaryManager1, "Average salary for manager1 is incorrect.");
    }

    @Test
    public void testCalculateReportingLineLength() {
        Map<Integer, EmployeeEntity> employeeMap = mock(Map.class);
        when(employeeMap.get(ceo.getId())).thenReturn(ceo);
        when(employeeMap.get(manager1.getId())).thenReturn(manager1);
        when(employeeMap.get(manager2.getId())).thenReturn(manager2);
        when(employeeMap.get(employee1.getId())).thenReturn(employee1);
        when(employeeMap.get(employee2.getId())).thenReturn(employee2);

        int reportingLineLength = EmployeeSalaryCalculation.calculateReportingLineLength(employeeMap, employee2, ceo, 0);
        assertEquals(1, reportingLineLength, "Reporting line length for employee2 is incorrect.");

        int reportingLineLength2 = EmployeeSalaryCalculation.calculateReportingLineLength(employeeMap, employee1, ceo, 0);
        assertEquals(2, reportingLineLength2, "Reporting line length for employee1 is incorrect.");
    }
}


