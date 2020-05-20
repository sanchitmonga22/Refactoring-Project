package view;

import controller.PinsetterEvent;
import model.Lane;
import model.Pinsetter;
import util.LaneObserver;
import util.PinsetterObserver;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This contains the view of all the Lanes
 */
public class LaneStatusView implements ActionListener, LaneObserver, PinsetterObserver {

	private JPanel jp;

	private JLabel curBowler;
	private JLabel pinsDown;

	private JButton viewLane;
	private JButton viewPinSetter;
	private JButton maintenance;

	private PinSetterView psv;
	private LaneView lv;
	private Lane lane;

	int laneNum;

	boolean laneShowing;
	boolean psShowing;

	public LaneStatusView(Lane lane, int laneNum ) {

		this.lane = lane;
		this.laneNum = laneNum;

		laneShowing=false;
		psShowing=false;

		// Initializing PinsetterView
		psv = new PinSetterView( laneNum );
		Pinsetter ps = lane.getPinsetter();
		ps.subscribe(psv);

		// Initializing laneView
		lv = new LaneView( lane, laneNum );
		lane.subscribe(lv);

		// Creating the main frame of the GUI
		jp = new JPanel();
		jp.setLayout(new FlowLayout());
		JLabel cLabel = new JLabel( "Now Bowling:" );
		curBowler = new JLabel( "(no one)" );
		JLabel pdLabel = new JLabel( "Pins Down: " );
		pinsDown = new JLabel( "0" );

		// Adding all the components in the mainFrame
		jp.add( cLabel );
		jp.add( curBowler );
		jp.add( pdLabel );
		jp.add( pinsDown );
		jp.add(initializeButtonPanel());

	}

	/**
	 * Initializes the Button panel including all the required buttons
	 * @return The JPanel that will be added to the mainFrame and includes all the buttons
	 */
	public JPanel initializeButtonPanel(){
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());

		new Insets(4, 4, 4, 4);

		// Creating button for View Lane
		viewLane = new JButton("View Lane");
		JPanel viewLanePanel = new JPanel();
		viewLanePanel.setLayout(new FlowLayout());
		viewLane.addActionListener(this);
		viewLanePanel.add(viewLane);

		// Creating button for PinSetter
		viewPinSetter = new JButton("Pinsetter");
		JPanel viewPinSetterPanel = new JPanel();
		viewPinSetterPanel.setLayout(new FlowLayout());
		viewPinSetter.addActionListener(this);
		viewPinSetterPanel.add(viewPinSetter);

		// Creating button for Maintenance call
		maintenance = new JButton("     ");
		maintenance.setBackground( Color.GREEN );
		JPanel maintenancePanel = new JPanel();
		maintenancePanel.setLayout(new FlowLayout());
		maintenance.addActionListener(this);
		maintenancePanel.add(maintenance);

		viewLane.setEnabled( false );
		viewPinSetter.setEnabled( false );

		// Adding all the buttons to the panel
		buttonPanel.add(viewLanePanel);
		buttonPanel.add(viewPinSetterPanel);
		buttonPanel.add(maintenancePanel);

		return buttonPanel;
	}

	/**
	 * Displays the lane
	 * @return The Panel containing the lane view
	 */
	public JPanel showLane() {
		return jp;
	}

	/**
	 * Updates the view according to the changes in the system
	 * @param e Contains data for the buttons that are clicked
	 */
	public void actionPerformed( ActionEvent e ) {
		if ( lane.isPartyAssigned() ) { 
			if (e.getSource().equals(viewPinSetter)) {
				if ( psShowing == false ) {
					psv.show();
					psShowing=true;
				} else if ( psShowing == true ) {
					psv.hide();
					psShowing=false;
				}
			}
		}
		if (e.getSource().equals(viewLane)) {
			if ( lane.isPartyAssigned() ) { 
				if ( laneShowing == false ) {
					lv.show();
					laneShowing=true;
				} else if ( laneShowing == true ) {
					lv.hide();
					laneShowing=false;
				}
			}
		}
		if (e.getSource().equals(maintenance)) {
			if ( lane.isPartyAssigned() ) {
				lane.unPauseGame();
				maintenance.setBackground( Color.GREEN );
			}
		}
	}

	/**
	 * Updates the view according to the changes in the system
	 */
	public void receiveLaneEvent() {
		curBowler.setText(lane.getBowler().getNickName());
		if ( lane.isMechanicalProblem() ) {
			maintenance.setBackground( Color.RED );
		}	
		if ( lane.isPartyAssigned() == false ) {
			viewLane.setEnabled( false );
			viewPinSetter.setEnabled( false );
		} else {
			viewLane.setEnabled( true );
			viewPinSetter.setEnabled( true );
		}
	}

	/**
	 * Updates the Number of pins down
	 * @param pe The event that contains the data for the number of pins that are down
	 */
	public void receivePinsetterEvent(PinsetterEvent pe) {
		pinsDown.setText( (Integer.valueOf(pe.totalPinsDown())).toString() );
	}
}
