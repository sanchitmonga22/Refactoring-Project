package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import controller.ControlDesk;
import controller.ControlDeskEvent;
import model.Lane;
import util.ControlDeskObserver;
import java.util.*;
import java.util.List;

/**
 * Class representing the view for the ControlDesk
 */
public class ControlDeskView implements ActionListener, ControlDeskObserver {

	private JButton addParty;				// represents the button for addParty
	private JButton finished; 				// represents the button for finished
	private JButton assign;					// represents the button for assign

	private JFrame win;
	private JList partyList;				// To store the names in a GUI list format
	private int maxMembers;					// maximum numbers of member in the party
	private ControlDesk controlDesk;        // The actual control desk that will communicate with the GUI

	/**
	 * Displays a GUI representation of the ControlDesk
	 */
	public ControlDeskView(ControlDesk controlDesk, int maxMembers) {

		this.controlDesk = controlDesk;
		this.maxMembers = maxMembers;
		int numLanes = controlDesk.getNumLanes();

		// Creating the main Frame for the GUI
		win = new JFrame("Control Desk");
		win.getContentPane().setLayout(new BorderLayout());
		((JPanel) win.getContentPane()).setOpaque(false);

		JPanel colPanel = new JPanel();
		colPanel.setLayout(new BorderLayout());

		// Adding all the components in the main panel
		colPanel.add(initializeControlPanel(), "East");
		colPanel.add(initializeLaneStatusPanel(numLanes), "Center");
		colPanel.add(initializePartyQueuePanel(), "West");

		win.getContentPane().add("Center", colPanel);
		win.pack();

		/* Close program when this window closes */
		win.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// Center Window on Screen
		Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
		win.setLocation(
			((screenSize.width) / 2) - ((win.getSize().width) / 2),
			((screenSize.height) / 2) - ((win.getSize().height) / 2));
		win.show();

	}

	/**
	 * Initializing the Control Panel to add it into the GUI
	 * @return Panel containing all the required components for the GUI
	 */
	public JPanel initializeControlPanel(){
		JPanel controlsPanel = new JPanel();
		controlsPanel.setLayout(new GridLayout(3, 1));
		controlsPanel.setBorder(new TitledBorder("Controls"));

		// adding the Add Party button in the GUI
		addParty = new JButton("Add Party");
		JPanel addPartyPanel = new JPanel();
		addPartyPanel.setLayout(new FlowLayout());
		addParty.addActionListener(this);
		addPartyPanel.add(addParty);
		controlsPanel.add(addPartyPanel);

		// adding the Assign Lanes button in the GUI
		assign = new JButton("Assign Lanes");
		JPanel assignPanel = new JPanel();
		assignPanel.setLayout(new FlowLayout());
		assign.addActionListener(this);
		assignPanel.add(assign);
		controlsPanel.add(assignPanel);

		// adding the Finished button in the GUI
		finished = new JButton("Finished");
		JPanel finishedPanel = new JPanel();
		finishedPanel.setLayout(new FlowLayout());
		finished.addActionListener(this);
		finishedPanel.add(finished);
		controlsPanel.add(finishedPanel);

		return controlsPanel;
	}

	/**
	 * Initializing the Lane Status Panel to add it into the GUI
	 * @return Panel containing all the required components for the GUI
	 */
	public JPanel initializeLaneStatusPanel(int numLanes){
		JPanel laneStatusPanel = new JPanel();
		laneStatusPanel.setLayout(new GridLayout(numLanes, 1));
		laneStatusPanel.setBorder(new TitledBorder("Lane Status"));

		HashSet<Lane> lanes=controlDesk.getLanes();
		int laneCount=0;

		// Iterating over the lanes and creating new lanes to add them to the GUI
		for(Lane lane:lanes){
			Lane curLane= lane;
			LaneStatusView laneStat = new LaneStatusView(curLane,(laneCount+1));
			curLane.subscribe(laneStat);
			curLane.getPinsetter().subscribe(laneStat);
			JPanel lanePanel = laneStat.showLane();
			lanePanel.setBorder(new TitledBorder("Lane" + ++laneCount ));
			laneStatusPanel.add(lanePanel);
		}

		return laneStatusPanel;
	}

	/**
	 * Initializing the Party Queue Panel to add it into the GUI
	 * @return Panel containing all the required components for the GUI
	 */
	public JPanel initializePartyQueuePanel(){
		JPanel partyPanel = new JPanel();
		partyPanel.setLayout(new FlowLayout());
		partyPanel.setBorder(new TitledBorder("Party Queue"));

		Vector empty = new Vector();
		empty.add("(Empty)");

		partyList = new JList(empty);
		partyList.setFixedCellWidth(120);
		partyList.setVisibleRowCount(10);
		JScrollPane partyPane = new JScrollPane(partyList);
		partyPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
		partyPanel.add(partyPane);

		return partyPanel;
	}

	/**
	 * Handles all the incoming events or a button is clicked
	 * @param e	the ActionEvent that triggered the handler
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(addParty)) {
			new AddPartyView(this, maxMembers);
		}
		if (e.getSource().equals(assign)) {
			controlDesk.assignLane();
		}
		if (e.getSource().equals(finished)) {
			win.dispose();
			System.exit(0);
		}
	}

	/**
	 * Receive a new party from addPartyView.
	 * @param addPartyView	the AddPartyView that is providing a new party
	 */
	public void updateAddParty(AddPartyView addPartyView) {
		controlDesk.addPartyQueue(addPartyView.getParty());
	}

	/**
	 * Receive a broadcast from a ControlDesk
	 * @param ce	the ControlDeskEvent that triggered the handler
	 */
	public void receiveControlDeskEvent(ControlDeskEvent ce) {
		List<String> partyQueue=ce.getPartyQueue();
		partyList.setListData(partyQueue.toArray(new String[partyQueue.size()]));
	}
}
