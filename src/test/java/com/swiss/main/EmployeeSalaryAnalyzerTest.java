package com.swiss.main;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.swiss.entity.EmployeeEntity;
import com.swiss.util.EmployeeSalaryCalculation;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

public class EmployeeSalaryAnalyzerTest {

    private Map<Integer, EmployeeEntity> employeeMap;
    private EmployeeEntity ceo;
    private EmployeeEntity manager1;
    private EmployeeEntity manager2;
    private EmployeeEntity employee1;
    private EmployeeEntity employee2;
    private EmployeeEntity employee3;
    private EmployeeEntity employee4;
    private EmployeeEntity employee5;
    private EmployeeEntity employee6;
    private EmployeeEntity employee7;
    private EmployeeEntity employee8;
    private EmployeeEntity employee9;


    @Mock
    private EmployeeSalaryCalculation employeeSalaryCalculation;
    @InjectMocks
    private EmployeeSalaryAnalyzerTest employeeSalaryAnalyzerTest;
    @Mock
    private EmployeeEntity manager;
    
    @Mock
    private Map<Integer, EmployeeEntity> employeeMap1;

    @BeforeEach
    public void setUp() {
        // Initialize the employee data
    	MockitoAnnotations.openMocks(this);
        employeeMap = new HashMap<>();
        ceo = new EmployeeEntity(123, "Joe", "Doe", 70000, null);
        
        manager1 = new EmployeeEntity(124, "Martin", "Chekov", 64000, 123);
         manager2 = new EmployeeEntity(125, "Bob", "Ronstad", 47000, 123);
        
         employee1 = new EmployeeEntity(300, "Alice", "Hasacat", 50000, 124);
         employee2 = new EmployeeEntity(305, "Brett", "Hardleaf", 34000, 124);
         employee3 = new EmployeeEntity(309, "Alice", "Hasacat", 50000, 301);
         employee4 = new EmployeeEntity(301, "Brett", "Hardleaf", 34000, 302);
         employee5 = new EmployeeEntity(302, "David", "Hasacat", 30000, 303);
         employee6 = new EmployeeEntity(303, "Marry", "Hardleaf", 34000, 304);
         employee7 = new EmployeeEntity(304, "Emma", "Hardleaf", 26000, 306);
         employee8 = new EmployeeEntity(306, "Lory", "Hardleaf", 19000, 307);
         employee9 = new EmployeeEntity(307, "Lory", "Hardleaf", 49000, 125);


        
        // Map the employees to their manager
        employeeMap.put(123, ceo);
        employeeMap.put(124, manager1);
        employeeMap.put(125, manager2);
        employeeMap.put(300, employee1);
        employeeMap.put(305, employee2);
        employeeMap.put(301, employee4);
        employeeMap.put(302, employee5);
        employeeMap.put(303, employee6);
        employeeMap.put(304, employee7);
        employeeMap.put(306, employee8);
        employeeMap.put(307, employee9);




        manager1.addSubordinate(employee1);
        manager1.addSubordinate(employee2);
        manager2.addSubordinate(employee9);
    }

	@Test
    public void testSalaryValidationForManagers_LessThanRequired() {
    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
     
    	
        EmployeeSalaryAnalyzer.salaryValidationForManagers(employeeMap);

        // Verify: Capture the output and check if the message was printed
        String output = outputStream.toString();
        assertTrue(output.contains("earns less than required by"));
      
    }

    @Test
    public void testSalaryValidationForManagers_MoreThanRequired() {
    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        // Assuming manager2 earns more than required
        EmployeeSalaryAnalyzer.salaryValidationForManagers(employeeMap);
        // Similar validation to check if manager2's salary exceeds the upper limit
        String output = outputStream.toString();
        assertTrue(output.contains("earns more than required by"));
    }

    @Test
    public void testLongReportingLine() {
    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        EmployeeSalaryAnalyzer.validateLongReportingLine(employeeMap, ceo);
        String output = outputStream.toString();
        assertTrue(output.contains("has a reporting line too long by"));
    }

    @Test
    public void testCalculateReportingLineLength() {
        int reportingLineLength = EmployeeSalaryCalculation.calculateReportingLineLength(employeeMap, employeeMap.get(300), ceo, 0);
        assertEquals(1, reportingLineLength);
    }
    
    @Test
    public void testCalculateAverageSalary() {
        double avgSalary = EmployeeSalaryCalculation.calculateAverageSalary(employeeMap.get(124)); // For manager1
        double expectedAvgSalary = (50000 + 34000) / 2.0;
        assertEquals(expectedAvgSalary, avgSalary);
    }


}
