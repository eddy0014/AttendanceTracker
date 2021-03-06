package attendancePackage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.sql.*;
import java.util.*;

public class Main extends JFrame {
	
	private JPanel parentPanel;
	private JMenuBar appMenuBar;
	private JMenu menu;
	Driver db;
	
	public Main() {
		// Connect to the database
		db = new Driver();
		db.createConnection();
	}
	
	public void setupEverything() {
		setLayout(null);
		
		setTitle("Attendance Tracking");
		setSize(500, 500);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Center the frame on screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int w = getSize().width;
		int h = getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;
		setLocation(x, y);

		// This is the toolbar that will contain the menus
		appMenuBar = new JMenuBar();
		setJMenuBar(appMenuBar);
		
		menu = new JMenu("Menu");
		appMenuBar.add(menu);
		
		// Create the menu items
		JMenuItem addCourseMenuItem = new JMenuItem("Add Course");
		menu.add(addCourseMenuItem);
		
		// Add listeners to the menu items
		addCourseMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addCourse();
			}
		});
		
		// Create the menu items for existing courses
		getCourses();
		
		// Add the panel that will hold everything
		parentPanel = new JPanel();
		parentPanel.setSize(500, 500); 
		parentPanel.setBackground(Color.magenta);
		add(parentPanel);

		JLabel appLabel = new JLabel("Welcome to Attendance Tracking");
		parentPanel.add(appLabel);
		appLabel.setBounds(10,10,200,20); //setBounds(x, y, width, height)

		// This can be used to make the menu look cleaner
		//menu.addSeparator();
		
		setVisible(true);
	}
	
	public void getCourses() {
		ResultSet allCourses = db.getAllCourses();
		
		try {
			while(allCourses.next()) {
				String courseNumber = allCourses.getString("course_num");
				JMenuItem courseMenuItem = new JMenuItem(courseNumber);
				menu.add(courseMenuItem);
				
				courseMenuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						System.out.println("Clicked on courseMenuItem with " + courseNumber);
						displayCourseHomePanel(courseNumber);
					}
				});				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addCourse() {
		// Set layout for panel asking for course information
		JPanel addCoursePanel = new JPanel();
		addCoursePanel.setLayout(new BoxLayout(addCoursePanel, BoxLayout.PAGE_AXIS)); 
		JPanel courseNumPanel = new JPanel();
		JPanel courseNamePanel = new JPanel();
		courseNumPanel.add(new JLabel("Enter course number:"));
		JTextField courseNumField = new JTextField(7);
		courseNumPanel.add(courseNumField);
		courseNamePanel.add(new JLabel("Enter course name:"));
		JTextField courseNameField = new JTextField(12);
		courseNamePanel.add(courseNameField); 
		addCoursePanel.add(courseNumPanel);
		addCoursePanel.add(courseNamePanel);
		
		// Show panel and save the option the user selects
		int userButtonChoice = JOptionPane.showConfirmDialog(null, addCoursePanel, 
				"Add Course", JOptionPane.OK_CANCEL_OPTION);
		
		String courseNum = courseNumField.getText();
		//String courseNum = "CSC_150";
		String courseName = courseNameField.getText();
		
		// Check to see what the user selected
		if(userButtonChoice == JOptionPane.OK_OPTION) {
			// Add the course to the database
			db.addCourse(courseNum, courseName);
			
			// Add the new course to the menu and add ActionListener to it
			JMenuItem newCourseItem = new JMenuItem(courseNum);
			menu.add(newCourseItem);
			newCourseItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					displayCourseHomePanel(courseNum);
				}
			});	
			
			// User goes directly to course home after creating course
			displayCourseHomePanel(courseNum);
		} else {
			// Do nothing
		}
	}
	
	JPanel parentCoursePanel = new JPanel();
	JPanel leftCoursePanel = new JPanel();
	JPanel rightCoursePanel = new JPanel();
	JPanel managePanel = new JPanel();
	JPanel takePanel = new JPanel();
	
	public void displayCourseHomePanel(String courseNum) {
		parentPanel.setVisible(false);
		
		// Remove any components in the panels
		parentCoursePanel.removeAll();
		parentCoursePanel.repaint();
		leftCoursePanel.removeAll();
		leftCoursePanel.repaint();
		rightCoursePanel.removeAll();
		rightCoursePanel.repaint();
		
		
		// Set up layout for course home 
		parentCoursePanel.setLayout(new BoxLayout(parentCoursePanel, BoxLayout.LINE_AXIS));
		parentCoursePanel.setSize(500, 500);
		leftCoursePanel.setPreferredSize(new Dimension(25, 500));
		leftCoursePanel.setBackground(Color.orange);
		leftCoursePanel.add(new JLabel(courseNum));
		JButton takeButton = new JButton("Take Attendance");
		JButton manageButton = new JButton("Manage Students");
		leftCoursePanel.add(takeButton);
		leftCoursePanel.add(manageButton);
		parentCoursePanel.add(leftCoursePanel);
		rightCoursePanel.setPreferredSize(new Dimension(250, 500));
		rightCoursePanel.setBackground(Color.red);
		parentCoursePanel.add(rightCoursePanel);
		
		// Apply changes to panel
		parentCoursePanel.revalidate();
		parentCoursePanel.repaint();
		leftCoursePanel.revalidate();
		leftCoursePanel.repaint();
		rightCoursePanel.revalidate();
		rightCoursePanel.repaint();
		
		// Add the parent panel to the frame
		add(parentCoursePanel);
		
		// Add ActionListeners
		manageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Clicked on manageButton with " + courseNum);
				displayManagePanel(courseNum);
			}
		});
		takeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Clicked on takeButton with " + courseNum);
				displayTakeAttPanel(courseNum);
			}
		});		
	}
	
	public void displayManagePanel(String courseNum) {
		// Remove any components in the panels
		rightCoursePanel.removeAll();
		rightCoursePanel.repaint();
		managePanel.removeAll();
		managePanel.repaint();
		
		// Set up layout for manage panel
		managePanel.setLayout(new BoxLayout(managePanel, BoxLayout.PAGE_AXIS));
		managePanel.setBackground(Color.cyan);
		managePanel.setPreferredSize(new Dimension(rightCoursePanel.getWidth(), rightCoursePanel.getHeight())); 
		
		ResultSet courseStudents = db.selectStudentsInCourse(courseNum); 
		
		try { 
		while(courseStudents.next()) {
			managePanel.add(new JLabel(courseStudents.getString("student_id") + " " + courseStudents.getString("first_name") + " " + courseStudents.getString("last_name")));
		}
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		
		JButton addStudentButton = new JButton("Add student");
		managePanel.add(addStudentButton);
		addStudentButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addStudent(courseNum);
			}
		});
		
		managePanel.revalidate();
		managePanel.repaint();
		rightCoursePanel.add(managePanel);
		rightCoursePanel.revalidate();
		rightCoursePanel.repaint();
	}
	
	public void displayTakeAttPanel(String courseNum) {
		rightCoursePanel.removeAll();
		rightCoursePanel.repaint();
		takePanel.removeAll();
		takePanel.repaint();
		
		takePanel.setLayout(new BoxLayout(takePanel, BoxLayout.PAGE_AXIS));
		takePanel.setBackground(Color.cyan);
		takePanel.setPreferredSize(new Dimension(rightCoursePanel.getWidth(), rightCoursePanel.getHeight())); 
		
		int numOfStudents = db.countStudentsInCourse(courseNum);
		
		JComboBox[] comboArray = new JComboBox[numOfStudents];
		String[] labels = {"Present", "Absent", "Excused"};
		int i = 0;
		
		ResultSet studentsInCourse = db.selectStudentsInCourse(courseNum);
		
		// Set up combo boxes for the date
		String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		String[] days = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
		String[] years = {"2017", "2018", "2019", "2020", "2021", "2022"};
		JComboBox monthBox = new JComboBox(months);
		JComboBox dayBox = new JComboBox(days);
		JComboBox yearBox = new JComboBox(years);
		JPanel comboBoxPanel = new JPanel(new FlowLayout());
		comboBoxPanel.add(monthBox);
		comboBoxPanel.add(dayBox);
		comboBoxPanel.add(yearBox);
		takePanel.add(comboBoxPanel);
		
		try {
			while(studentsInCourse.next()) {
				JPanel pane = new JPanel();
				
				JLabel name = new JLabel(studentsInCourse.getString("first_name") + " " + studentsInCourse.getString("last_name"));
				
				comboArray[i] = new JComboBox(labels);
				
				pane.add(name);
				pane.add(comboArray[i]);
				takePanel.add(pane);
				
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JButton saveButton = new JButton("Save");
		takePanel.add(saveButton); 
		
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int j = 0;
				
				ResultSet studentsInCourse2 = db.selectStudentsInCourse(courseNum);
				
				try {
					while(studentsInCourse2.next()) {												
						int studentID = studentsInCourse2.getInt("student_id"); 
						String firstName = studentsInCourse2.getString("first_name");
						String lastName = studentsInCourse2.getString("last_name");
						String statusLabel = (String) comboArray[j].getSelectedItem();
						int monthSelected = monthBox.getSelectedIndex() + 1;
						int daySelected = Integer.parseInt((String) dayBox.getSelectedItem());
						int yearSelected = Integer.parseInt((String) yearBox.getSelectedItem());
						
						db.insertIntoAttend(courseNum, studentID, firstName, lastName, statusLabel, monthSelected, daySelected, yearSelected);  
						
						j++;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		rightCoursePanel.add(takePanel);
		rightCoursePanel.revalidate();
		rightCoursePanel.repaint();
	}
	
	public void addStudent(String courseNum) {
		JPanel addStudentPanel = new JPanel();
		addStudentPanel.setLayout(new BoxLayout(addStudentPanel, BoxLayout.PAGE_AXIS)); 
		JPanel idPanel = new JPanel();
		JPanel firstnamePanel = new JPanel();
		JPanel lastNamePanel = new JPanel();
		idPanel.add(new JLabel("Student ID:"));
		JTextField idField = new JTextField(10);
		idPanel.add(idField);
		firstnamePanel.add(new JLabel("First Name:"));
		JTextField firstNameField = new JTextField(7);
		firstnamePanel.add(firstNameField);
		lastNamePanel.add(new JLabel("Last Name:"));
		JTextField lastNameField = new JTextField(12);
		lastNamePanel.add(lastNameField); 
		addStudentPanel.add(idPanel);
		addStudentPanel.add(firstnamePanel);
		addStudentPanel.add(lastNamePanel);
		
		int userButtonChoice = JOptionPane.showConfirmDialog(null, addStudentPanel, 
				"Add Student", JOptionPane.OK_CANCEL_OPTION);
		
		if(userButtonChoice == JOptionPane.OK_OPTION) {
			int id = Integer.parseInt(idField.getText());
			String firstName = firstNameField.getText();
			String lastName = lastNameField.getText();
			
			db.addStudent(courseNum, id, firstName, lastName); 
			
			displayManagePanel(courseNum);
		} else {
			// Do nothing
		}
	}

	public static void main(String[] args) {		
		Main theApp = new Main();
		theApp.setupEverything();
	}

}
