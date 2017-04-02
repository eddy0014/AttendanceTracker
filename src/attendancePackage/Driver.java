package attendancePackage;

import java.sql.*;

public class Driver {
	
	Connection dbConnection;

	public void createConnection() {
		try{
			// Get a connection to database
			dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_tracker", "root", "data-manage55");
			
			// Create a statement
			//Statement myStatement = dbConnection.createStatement();
			
			// Execute SQL query
			//ResultSet myResult = myStatement.executeQuery("select * from city limit 0, 10");
			
			// Process result set
			/*while(myResult.next()) {
				System.out.println(myResult.getString("city") + ", " + myResult.getString("city_id")); 
			}*/
			
		} catch(Exception e) {
			e.printStackTrace();
		}		
	}
	
	//TODO add code to create attendance table for the course
	public void addCourse(String courseNum, String courseName) {
		try {
			// Create a statement
			Statement theStatement = dbConnection.createStatement();
			
			// Insert course information into courses table
			String insertCourse = "insert into courses (course_num, course_name) values "
					+ "(" + "\"" + courseNum + "\"" + "," + "\"" + courseName + "\"" + ")";
			
			String courseNumLower = courseNum.toLowerCase();
			
			// Create the table for the course
			String createTable = "create table " + courseNumLower + " (student_id int, first_name tinytext, last_name tinytext)";
			
			// Create the attendance table for the course
			String createAttendTable = "create table attendance_" + courseNumLower + " (student_id int, first_name tinytext, last_name tinytext, status tinytext)";
			
			// Execute queries
			int rowsAffected = theStatement.executeUpdate(insertCourse); 
			theStatement.executeUpdate(createTable);
			theStatement.executeUpdate(createAttendTable);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public ResultSet getAllCourses() {
		ResultSet allCourses = null;
		
		try{
			Statement theStatement = dbConnection.createStatement();
			
			String query = "select * from courses";
			
			allCourses = theStatement.executeQuery(query);
		} catch(Exception e) {
			e.printStackTrace(); 
		}
		
		return allCourses;
	}
	
	public ResultSet selectStudentsInCourse(String courseNum) {
		ResultSet results = null;
		
		try{
			Statement theStatement = dbConnection.createStatement();
			
			String courseNumLower = courseNum.toLowerCase();
			String query = "select * from " + courseNumLower + " order by last_name";
			
			results = theStatement.executeQuery(query);
		} catch(Exception e) {
			e.printStackTrace(); 
		}
		
		return results;
	}
	
	public int countStudentsInCourse(String courseNum) {
		int numOfRows = 0;
		
		try {
			Statement theStatement = dbConnection.createStatement();
			
			String courseNumLower = courseNum.toLowerCase();
			String query = "select count(*) as total from " + courseNumLower;
			ResultSet results = theStatement.executeQuery(query);
			while(results.next()) {
				numOfRows = results.getInt("total"); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return numOfRows;
	}
	
	public void addStudent(String courseNum, int id, String firstName, String lastName) {
		try {
			Statement theStatement = dbConnection.createStatement();
			
			String courseNumLower = courseNum.toLowerCase();
			String query = "insert into " + courseNumLower + "(student_id, first_name, last_name) values "
					+ "(" + id + "," + "\"" + firstName + "\"" + "," + "\"" +  lastName + "\"" + ")";
			
			int rowsAffected = theStatement.executeUpdate(query); 
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void insertIntoAttend(String courseNum, int id, String firstName, String lastName, String statusLabel) {
		try {
			Statement theStatement = dbConnection.createStatement();
			
			String courseNumLower = courseNum.toLowerCase();
			String query = "insert into attendance_" + courseNumLower + " (student_id, first_name, last_name, status) values "
					+ "(" + id + "," + "\"" + firstName + "\"" + "," + "\"" +  lastName + "\"" + "," + "\"" + statusLabel + "\"" + ")";
			
			int rowsAffected = theStatement.executeUpdate(query); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
