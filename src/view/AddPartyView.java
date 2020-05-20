package view;

import model.Bowler;
import util.BowlerFile;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.util.*;

/**
 * Class for GUI components need to add a party
 */
public class AddPartyView implements ActionListener, ListSelectionListener {


	private int maxSize;
	private JFrame win;

	/**Buttons to represent Add Patron, New Patron, Remove Patron and Finished */
	private JButton addPatron;
	private JButton newPatron;
	private JButton remPatron;
	private JButton finished;

	/** GUI Lists to display Parties and the bowlers */
	private JList<String> partyList;
	private JList<String> allBowlers;

	private ArrayList<String> party;
	private ArrayList<String> bowlerdb;

	private ControlDeskView controlDesk;
	private String selectedNick;
	private String selectedMember;

	/**
	 * Constructor for GUI used to Add Parties to the waiting party queue.
	 */
	public AddPartyView(ControlDeskView controlDesk, int maxSize) {

		this.controlDesk = controlDesk;
		this.maxSize = maxSize;
		party= new ArrayList<>();
		bowlerdb= new ArrayList<>();

		// Creating the main frame
		win = new JFrame("Add Party");
		win.getContentPane().setLayout(new BorderLayout());
		((JPanel) win.getContentPane()).setOpaque(false);

		JPanel colPanel = new JPanel();
		colPanel.setLayout(new GridLayout(1, 3));

		// Adding all the panels in the main Panel
		colPanel.add(initializePartyPanel());
		colPanel.add(initializeBowlerDatabase());
		colPanel.add(initializeButtonPanel());

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
	 * Initializes the view for the Party Panel in the GUI
	 * @return The panel containing all the elements in the GUI
	 */
	public JPanel initializePartyPanel(){
		JPanel partyPanel = new JPanel();
		partyPanel.setLayout(new FlowLayout());
		partyPanel.setBorder(new TitledBorder("Your Party"));

		String[] empty={"(empty)"};				// Initially the list will be empty
		partyList = new JList<>(empty);			// list containing all the members in the selected party
		partyList.setFixedCellWidth(120);
		partyList.setVisibleRowCount(5);
		partyList.addListSelectionListener(this);
		JScrollPane partyPane = new JScrollPane(partyList);
		partyPanel.add(partyPane);

		return partyPanel;
	}

	/**
	 * Initializes the view for the Bowler Database in the GUI
	 * @return The panel containing all the elements for the view
	 */
	public JPanel initializeBowlerDatabase(){
		JPanel bowlerPanel = new JPanel();
		bowlerPanel.setLayout(new FlowLayout());
		bowlerPanel.setBorder(new TitledBorder("Bowler Database"));

		try {
			bowlerdb= BowlerFile.getBowlers(); // getting the bowlers from the database
		} catch (Exception e) {
			System.err.println("File Error");
		}

		allBowlers = new JList<>(bowlerdb.toArray(new String[bowlerdb.size()]));	// list of all the registered bowlers
		allBowlers.setVisibleRowCount(8);
		allBowlers.setFixedCellWidth(120);
		JScrollPane bowlerPane = new JScrollPane(allBowlers);
		bowlerPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		allBowlers.addListSelectionListener(this);
		bowlerPanel.add(bowlerPane);

		return bowlerPanel;
	}

	/**
	 * Initializes the view for all the 4 buttons in the GUI
	 * @return The panel containing all the required elements for the 4 buttons
	 */
	public JPanel initializeButtonPanel(){
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(4, 1));

		new Insets(4, 4, 4, 4);

		// Creating the Add to Party button
		addPatron = new JButton("Add to Party");
		JPanel addPatronPanel = new JPanel();
		addPatronPanel.setLayout(new FlowLayout());
		addPatron.addActionListener(this);
		addPatronPanel.add(addPatron);

		//Creating the Remove Patron Button
		remPatron = new JButton("Remove Member");
		JPanel remPatronPanel = new JPanel();
		remPatronPanel.setLayout(new FlowLayout());
		remPatron.addActionListener(this);
		remPatronPanel.add(remPatron);

		// Creating the New Patron Button to add the new members
		newPatron = new JButton("New Patron");
		JPanel newPatronPanel = new JPanel();
		newPatronPanel.setLayout(new FlowLayout());
		newPatron.addActionListener(this);
		newPatronPanel.add(newPatron);

		// Creating the Finished button to finish Adding to the party
		finished = new JButton("Finished");
		JPanel finishedPanel = new JPanel();
		finishedPanel.setLayout(new FlowLayout());
		finished.addActionListener(this);
		finishedPanel.add(finished);

		// Adding all the panels created into Button panel
		buttonPanel.add(addPatronPanel);
		buttonPanel.add(remPatronPanel);
		buttonPanel.add(newPatronPanel);
		buttonPanel.add(finishedPanel);

		return buttonPanel;
	}

	/**
	 * Performs action when any of the 4 buttons are clicked
	 * @param e	The action event that updates whether something was clicked or not
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(addPatron)) {
			addPatronClicked();
		}
		if (e.getSource().equals(remPatron)) {
			remPatronClicked();
		}
		if (e.getSource().equals(newPatron)) {
			newPatronClicked();
		}
		if (e.getSource().equals(finished)) {
			finishedClicked();
		}
	}

	/**
	 * If a add an existing patron to the party button was clicked
	 */
	public void addPatronClicked(){
		if (selectedNick != null && party.size() < maxSize) {
			if (party.contains(selectedNick)) {
				System.err.println("Member already in Party");
			} else {
				party.add(selectedNick);
				partyList.setListData(party.toArray(new String[party.size()]));
			}
		}
	}

	/**
	 * If remove a patron button is clicked
	 */
	public void remPatronClicked(){
		if (selectedMember != null) {
			party.remove(selectedMember);
			partyList.setListData(party.toArray(new String[party.size()]));
		}
	}

	/**
	 * If the finish adding the part button is clicked
	 */
	public void finishedClicked(){
		if ( party != null && party.size() > 0) {
			controlDesk.updateAddParty( this );
		}
		win.hide();
	}

	/**
	 * If add a new patron button is clicked
	 */
	public void newPatronClicked(){
		NewPatronView newPatron = new NewPatronView( this );
	}

	/**
	 * Handle all the actions for the lists
	 * @param e the ListActionEvent that triggered the handler
 	*/
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource().equals(allBowlers)) {
			selectedNick = ((String) ((JList) e.getSource()).getSelectedValue());
		}
		if (e.getSource().equals(partyList)) {
			selectedMember = ((String) ((JList) e.getSource()).getSelectedValue());
		}
	}

	/**
	 * Called by NewPatronView to notify AddPartyView to update
	 * @param newPatron the NewPatronView that called this method
 	*/
	public void updateNewPatron(NewPatronView newPatron) {
		try {
			Bowler checkBowler = BowlerFile.getBowlerInfo( newPatron.getNick() );
			if ( checkBowler == null ) {

				// adding info for the new patrons added in the system and updating the database
				BowlerFile.putBowlerInfo( newPatron.getNick(), newPatron.getFull(), newPatron.getEmail());
				bowlerdb = BowlerFile.getBowlers();
				allBowlers.setListData(bowlerdb.toArray(new String[bowlerdb.size()]));
				party.add(newPatron.getNick());
				partyList.setListData(party.toArray(new String[party.size()]));

			} else {
				System.err.println( "A Bowler with that name already exists." );
			}
		} catch (Exception e2) {
			System.err.println("File I/O Error");
		}
	}

	/**
	 * Accessor for Party
	 * @return ArrayList containing the list of all the members in the party
	 */
	public ArrayList<String> getParty() {
		return party;
	}
}
