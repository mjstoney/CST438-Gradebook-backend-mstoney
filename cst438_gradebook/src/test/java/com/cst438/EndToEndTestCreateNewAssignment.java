package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentGrade;
import com.cst438.domain.AssignmentGradeRepository;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;

/*
 * This example shows how to use selenium testing using the web driver 
 * with Chrome browser.
 * 
 *  - Buttons, input, and anchor elements are located using XPATH expression.
 *  - onClick( ) method is used with buttons and anchor tags.
 *  - Input fields are located and sendKeys( ) method is used to enter test data.
 *  - Spring Boot JPA is used to initialize, verify and reset the database before
 *      and after testing.
 *      
 *  In SpringBootTest environment, the test program may use Spring repositories to 
 *  setup the database for the test and to verify the result.
 */
@SpringBootTest
public class EndToEndTestCreateNewAssignment {
	
	public static final String EDGE_DRIVER_FILE_LOCATION = "C:/edgedriver_win64/msedgedriver.exe";
	public static final String URL = "http://localhost:3000/add-assignment";
	public static final String TEST_USER_EMAIL = "test@csumb.edu";
	public static final String TEST_INSTRUCTOR_EMAIL = "dwisneski@csumb.edu";
	public static final int SLEEP_DURATION = 1000; // 1 second.
	public static final String TEST_ASSIGNMENT_NAME = "Test Assignment";
	public static final String TEST_COURSE_TITLE = "Test Course";
	public static final String TEST_STUDENT_NAME = "Test";
	
	public static final Date TEST_DUE_DATE = Date.valueOf("2022-10-31");

	@Autowired
	CourseRepository courseRepository;

	@Autowired
	AssignmentRepository assignmentRepository;
	
	@Test
	public void addAssignmentTest() throws Exception {
		
        Assignment deleteTest = null;
        do {
      	  deleteTest = assignmentRepository.findAssignmentByName(TEST_ASSIGNMENT_NAME);
            if (deleteTest != null)
                assignmentRepository.delete(deleteTest);
        } while (deleteTest != null);
		
//		Database setup:  create course		
		Course c = new Course();
		c.setCourse_id(99999);
		c.setInstructor(TEST_INSTRUCTOR_EMAIL);
		c.setSemester("Fall");
		c.setYear(2021);
		c.setTitle(TEST_COURSE_TITLE);
		courseRepository.save(c);
		
		Assignment assignment = new Assignment();
		//Set information, name and due date, course then save record
		assignment.setName(TEST_ASSIGNMENT_NAME);
		assignment.setDueDate(TEST_DUE_DATE);
		assignment.setNeedsGrading(0);
		assignment.setCourse(c);

	

		// set the driver location and start driver
				//@formatter:off
				// browser	property name 				Java Driver Class
				// edge 	webdriver.edge.driver 		EdgeDriver
				// FireFox 	webdriver.firefox.driver 	FirefoxDriver
				// IE 		webdriver.ie.driver 		InternetExplorerDriver
				//@formatter:on
				
		/*
		 * initialize the WebDriver and get the home page. 
		 */

		System.setProperty("webdriver.edge.driver", EDGE_DRIVER_FILE_LOCATION);
		WebDriver driver = new EdgeDriver();
		// Puts an Implicit wait for 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		driver.get(URL);
		Thread.sleep(SLEEP_DURATION);


		try {
			/*
			* locate input element for test assignment by assignment name
			* 
			* To select a radio button in a Datagrid display
			* 1.  find the elements in the assignmentName column of the data grid table.
			* 2.  locate the element with test assignment name and click the input tag.
			*/
			
			// Select the form elements in the page
			WebElement courseInputElement  = driver.findElement(By.xpath("//input[@name='assignmentCourse']"));
			WebElement assignmentNameInputElement  = driver.findElement(By.xpath("//input[@name='assignmentName']"));
			WebElement assignmentDueDateInputElement  = driver.findElement(By.xpath("//input[@name='assignmentDueDate']"));
			
			// Fill the form elements with test data
			courseInputElement.sendKeys("99999");
			assignmentNameInputElement.sendKeys(TEST_ASSIGNMENT_NAME);
			assignmentDueDateInputElement.sendKeys("10-31-2022");
			
			// Click the submit button on the page
			driver.findElement(By.xpath("//Button[@value='Submit']")).click();
			Thread.sleep(SLEEP_DURATION);
			
			// Check if the data shows up in the database
			Assignment testAssignment = assignmentRepository.findAssignmentByName(TEST_ASSIGNMENT_NAME);
			assertNotNull(testAssignment);
			
			

		} catch (Exception ex) {
			throw ex;
		} finally {

			/*
			 *  clean up database so the test is repeatable.
			 */
			Assignment testAssignment = assignmentRepository.findAssignmentByName(TEST_ASSIGNMENT_NAME);
			if (testAssignment != null) {
				assignmentRepository.delete(assignment);
			}

			driver.quit();
		}


		
		
	}
}


