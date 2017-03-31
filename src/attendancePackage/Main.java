package attendancePackage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.sql.*;

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
			displayCourseHomePanel(courseNum);
		} else {
			// Do nothing
		}
	}
	
	public void displayCourseHomePanel(String courseNum) {
		parentPanel.setVisible(false);
		
		// The panel that will hold the left and right panels
		JPanel parentCoursePanel = new JPanel();
		parentCoursePanel.setLayout(new BoxLayout(parentCoursePanel, BoxLayout.LINE_AXIS));
		parentCoursePanel.setSize(500, 500);
				
		// The panel displaying the buttons for options
		JPanel leftCoursePanel = new JPanel();
		leftCoursePanel.setPreferredSize(new Dimension(25, 500));
		leftCoursePanel.setBackground(Color.orange);
		leftCoursePanel.add(new JLabel(courseNum));
		
		// Set the buttons
		JButton takeButton = new JButton("Take Attendance");
		leftCoursePanel.add(takeButton);
		JButton manageButton = new JButton("Manage Students");
		leftCoursePanel.add(manageButton);
		
		parentCoursePanel.add(leftCoursePanel);
	
		// The panel displaying the content
		JPanel rightCoursePanel = new JPanel();
		rightCoursePanel.setPreferredSize(new Dimension(250, 500));
		rightCoursePanel.setBackground(Color.red);
		parentCoursePanel.add(rightCoursePanel);
		
		manageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JPanel managePanel = new JPanel();
				managePanel.setBackground(Color.cyan);
				managePanel.setPreferredSize(new Dimension(rightCoursePanel.getWidth(), rightCoursePanel.getHeight())); 
				
				ResultSet courseStudents = db.selectStudentsInCourse(courseNum); 
				
				try { 
				while(courseStudents.next()) {
					// TODO fix this so it matches database table in columns
					managePanel.add(new JLabel(courseStudents.getString("first_name") + " " + courseStudents.getString("last_name")));
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
				
				rightCoursePanel.add(managePanel);
				rightCoursePanel.revalidate();
				rightCoursePanel.repaint();
			}
		});
		
		takeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				displayTakeAttPanel(courseNum, rightCoursePanel);
			}
		});
		
		// Add the new course to the menu
		menu.add(new JMenuItem(courseNum));
		
		add(parentCoursePanel);
	}
	
	public void displayManagePanel(String courseNum) {
		
	}
	
	public void displayTakeAttPanel(String courseNum, JPanel rightCoursePanel) {
		// Set up panel layout
		JPanel takePanel = new JPanel();
		takePanel.setLayout(new BoxLayout(takePanel, BoxLayout.PAGE_AXIS));
		takePanel.setBackground(Color.cyan);
		takePanel.setPreferredSize(new Dimension(rightCoursePanel.getWidth(), rightCoursePanel.getHeight())); 
		
		int numOfStudents = db.countStudentsInCourse(courseNum);
		
		JComboBox[] comboArray = new JComboBox[numOfStudents];
		String[] labels = {"Present", "Absent", "Excused"};
		int i = 0;
		
		ResultSet studentsInCourse = db.selectStudentsInCourse(courseNum);
		
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
						
						db.insertIntoAttend(courseNum, studentID, firstName, lastName, statusLabel);  
						
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
		} else {
			// Do nothing
		}
	}

	public static void main(String[] args) {		
		Main theApp = new Main();
		theApp.setupEverything();
	}

}
