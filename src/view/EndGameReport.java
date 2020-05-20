package view;

import model.Bowler;
import model.Party;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.util.*;
import java.util.List;

/**
 * This class produces the end report when the game is finished and provides the option to print the report
 */
public class EndGameReport implements ActionListener, ListSelectionListener {

	private JFrame win;					// main frame

	private JButton printButton;		// Represents the print button
	private JButton finished;			// Represents the finished button
	private JList memberList;			// List of all the members in the party

	// Lists to store the bowlers with their names
	private List<String> myList;
	private List<String> retVal;

	private int result;
	private String selectedMember;

	public EndGameReport( String partyName, Party party ) {
	
		result =0;
		retVal = new ArrayList<>();

		// Creating the main Frame for the GUI
		win = new JFrame("End Game Report for " + partyName + "?" );
		win.getContentPane().setLayout(new BorderLayout());
		((JPanel) win.getContentPane()).setOpaque(false);

		JPanel colPanel = new JPanel();
		colPanel.setLayout(new GridLayout( 1, 2 ));

		// Adding all the functionality in the main panel
		colPanel.add(initializePartyPanel(party));
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
	 * Initializes the Party panel displaying all the current members in the party
	 * @param party The actual party object
	 * @return The JPanel which will be added in the main frame with list of all the party members
	 */
	public JPanel initializePartyPanel(Party party){
		JPanel partyPanel = new JPanel();
		partyPanel.setLayout(new FlowLayout());
		partyPanel.setBorder(new TitledBorder("Party Members"));

		// Creating a list of all the members in the party with their nickNames
		myList= new ArrayList<>();
		for(Bowler bowler: party.getMembers()){
			myList.add(bowler.getNickName());
		}

		// Displaying the list of all the members with their nickNames
		memberList = new JList(myList.toArray(new String[myList.size()]));
		memberList.setFixedCellWidth(120);
		memberList.setVisibleRowCount(5);
		memberList.addListSelectionListener(this);
		JScrollPane partyPane = new JScrollPane(memberList);
		partyPanel.add(partyPane);
		partyPanel.add(memberList);

		return partyPanel;
	}

	/**
	 * Initializes the Button Panel that contains Print Report and Finished button
	 * @return The JPanel containing both the buttons
	 */
	public JPanel initializeButtonPanel(){
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2, 1));

		new Insets(4, 4, 4, 4);

		// functionality for printButton
		printButton = new JButton("Print Report");
		JPanel printButtonPanel = new JPanel();
		printButtonPanel.setLayout(new FlowLayout());
		printButton.addActionListener(this);
		printButtonPanel.add(printButton);
		buttonPanel.add(printButton);

		// functionality for finishedButton
		finished = new JButton("Finished");
		JPanel finishedPanel = new JPanel();
		finishedPanel.setLayout(new FlowLayout());
		finished.addActionListener(this);
		finishedPanel.add(finished);
		buttonPanel.add(finished);

		return buttonPanel;
	}

	/**
	 * Performs the action based on the button pressed
	 * @param e The event that contains the data of which button was pressed.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(printButton)) {
			retVal.add(selectedMember);
		}
		if (e.getSource().equals(finished)) {		
			win.dispose();
			result = 1;
		}
	}

	/**
	 * If a member was selected from the list
	 * @param e The event that contains which member was selected
	 */
	public void valueChanged(ListSelectionEvent e) {
		selectedMember = ((String) ((JList) e.getSource()).getSelectedValue());
	}

	/**
	 * Called by Lane to determine whether or not a member wants to print their report
	 * @return The list of the names of the members who need to printout the endReport
	 */
	public List<String> getResult() {
		while ( result == 0 ) {
			try {
				Thread.sleep(10);
			} catch ( InterruptedException e ) {
				System.err.println( "Interrupted" );
			}
		}
		return retVal;	
	}
}

