package attendancePackage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Main extends JFrame {
	
	private JPanel parentPanel;
	private JMenuBar appMenuBar;
	private JMenu menu;
	
	public Main() {
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
	}
	
	public void addCourse() {
		// Set layout for panel
		JPanel addCoursePanel = new JPanel();
		addCoursePanel.add(new JLabel("Enter course number:"));
		JTextField courseNumField = new JTextField(7);
		addCoursePanel.add(courseNumField); 
		
		// Show panel and save option the user selects
		int userButtonChoice = JOptionPane.showConfirmDialog(null, addCoursePanel, 
				"Add Course", JOptionPane.OK_CANCEL_OPTION);
		
		String courseNum = courseNumField.getText();
		
		// Check to see what the user selected
		if(userButtonChoice == JOptionPane.OK_OPTION) {
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
				
		JPanel leftCoursePanel = new JPanel();
		leftCoursePanel.setPreferredSize(new Dimension(25, 500));
		leftCoursePanel.setBackground(Color.blue);
		leftCoursePanel.add(new JLabel(courseNum));
		parentCoursePanel.add(leftCoursePanel);
		
		JPanel rightCoursePanel = new JPanel();
		rightCoursePanel.setPreferredSize(new Dimension(250, 500));
		rightCoursePanel.setBackground(Color.red);
		parentCoursePanel.add(rightCoursePanel);
		
		// Add the new course to the menu
		menu.add(new JMenuItem(courseNum));
		
		add(parentCoursePanel);
	}

	public static void main(String[] args) {
		Main myApp = new Main();
		myApp.setVisible(true);
	}

}
