package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Class for GUI components need to add a new patron
 */
public class NewPatronView implements ActionListener {

	private JFrame win;

	// Represents the buttons to abort or finish adding the information for the registration
	private JButton abort;
	private JButton finished;

	// For labelling the input fields
	private JLabel nickLabel;
	private JLabel fullLabel;
	private JLabel emailLabel;

	// For taking input of the given data from the user
	private JTextField nickField;
	private JTextField fullField;
	private JTextField emailField;

	// General Data of a member
	private String nick;
	private String full;
	private String email;

	private AddPartyView addPartyView;

	public NewPatronView(AddPartyView v) {

		addPartyView=v;

		// Main frame for the GUI
		win = new JFrame("Add Patron");
		win.getContentPane().setLayout(new BorderLayout());
		((JPanel) win.getContentPane()).setOpaque(false);

		JPanel colPanel = new JPanel();
		colPanel.setLayout(new BorderLayout());

		// Adding components in the main panel
		colPanel.add(initializeAddNewPatronPanel(), "Center");
		colPanel.add(initializeButtonsPanel(), "East");

		win.getContentPane().add("Center", colPanel);
		win.pack();

		// Center Window on Screen
		Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
		win.setLocation(
			((screenSize.width) / 2) - ((win.getSize().width) / 2),
			((screenSize.height) / 2) - ((win.getSize().height) / 2));
		win.show();

	}

	/**
	 * Initializes all the labels and input fields that have the required information to add the new patron
	 * @return The JPanel that contains the components to register a new patron into the system
	 */
	public JPanel initializeAddNewPatronPanel(){
		JPanel patronPanel = new JPanel();
		patronPanel.setLayout(new GridLayout(3, 1));
		patronPanel.setBorder(new TitledBorder("Your Info"));

		JPanel nickPanel = new JPanel();
		nickPanel.setLayout(new FlowLayout());
		nickLabel = new JLabel("Nick Name");
		nickField = new JTextField("", 15);
		nickPanel.add(nickLabel);
		nickPanel.add(nickField);

		JPanel fullPanel = new JPanel();
		fullPanel.setLayout(new FlowLayout());
		fullLabel = new JLabel("Full Name");
		fullField = new JTextField("", 15);
		fullPanel.add(fullLabel);
		fullPanel.add(fullField);

		JPanel emailPanel = new JPanel();
		emailPanel.setLayout(new FlowLayout());
		emailLabel = new JLabel("E-Mail");
		emailField = new JTextField("", 15);
		emailPanel.add(emailLabel);
		emailPanel.add(emailField);

		patronPanel.add(nickPanel);
		patronPanel.add(fullPanel);
		patronPanel.add(emailPanel);

		return patronPanel;
	}

	/**
	 * Adds the buttons into the JPanel
	 * @return The JPanel including the required Buttons
	 */
	public JPanel initializeButtonsPanel(){
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(4, 1));

		new Insets(4, 4, 4, 4);

		finished = new JButton("Add Patron");
		JPanel finishedPanel = new JPanel();
		finishedPanel.setLayout(new FlowLayout());
		finished.addActionListener(this);
		finishedPanel.add(finished);

		abort = new JButton("Abort");
		JPanel abortPanel = new JPanel();
		abortPanel.setLayout(new FlowLayout());
		abort.addActionListener(this);
		abortPanel.add(abort);

		buttonPanel.add(abortPanel);
		buttonPanel.add(finishedPanel);

		return buttonPanel;
	}

	/**
	 * Updates the view if there is a button pressed or any changes made in the system
	 * @param e The action event that contains the data for any changes made in the system.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(abort)) {
			win.dispose();
		}
		if (e.getSource().equals(finished)) {
			nick = nickField.getText();
			full = fullField.getText();
			email = emailField.getText();
			addPartyView.updateNewPatron(this);
			win.dispose();
		}
	}

	/**
	 * @return The nick name of the new member
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * @return The full name of the new member
	 */
	public String getFull() {
		return full;
	}

	/**
	 * @return The email of the new member
	 */
	public String getEmail() {
		return email;
	}
}
