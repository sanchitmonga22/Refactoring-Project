package view;

import controller.PinsetterEvent;
import util.PinsetterObserver;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 *  constructs a prototype PinSetter GUI
 */
public class PinSetterView implements PinsetterObserver {

    private List<JLabel> pinsetterList;         // List containing all the different labels
	private List<JPanel> panels;				// List containing all the different panels

	// Represent each rolls
    private JPanel firstRoll;
    private JPanel secondRoll;
    // Represents all the pins that are standing
    private JPanel pins;

    // main frame of the GUI
	private JFrame frame;

	/**
	 * Constructs a Pin Setter GUI displaying which roll it is with
	 * yellow boxes along the top (1 box for first roll, 2 boxes for second)
	 * and displays the pins as numbers in this format:
	 *
	 *                7   8   9   10
	 *                  4   5   6
	 *                    2   3
	 *                      1
	 *
	 */
    public PinSetterView ( int laneNum ) {
    	pinsetterList= new ArrayList<>();
		panels= new ArrayList<>();
    	// mainframe of the GUI
		frame = new JFrame ( "Lane " + laneNum + ":" );
		Container cpanel = frame.getContentPane ( );

		pins = new JPanel ( );

		pins.setLayout ( new GridLayout ( 4, 7 ) );

		cpanel.add ( initializeRolls(), BorderLayout.NORTH );

		initializeGrid();

		pins.setBackground ( Color.black );
		pins.setForeground ( Color.yellow );
		cpanel.add ( pins, BorderLayout.CENTER );
		frame.pack();
    }

	/**
	 * Initializes the top of the GUI that represents each roll
	 * @return	The top panel that contains each roll of the bowler
	 */
	public JPanel initializeRolls(){
		//********************Top of GUI indicates first or second roll

		JPanel top = new JPanel ( );

		firstRoll = new JPanel ( );
		firstRoll.setBackground( Color.yellow );

		secondRoll = new JPanel ( );
		secondRoll.setBackground ( Color.black );

		top.add ( firstRoll, BorderLayout.WEST );

		top.add ( secondRoll, BorderLayout.EAST );

		top.setBackground ( Color.black );

		return top;
	}

	/**
	 * Initializes the grid that contains all the pins required in the GUI
	 */
    public void initializeGrid(){
		//**********************Grid of the pins**************************

		for(int i=0;i<10;i++){
			JPanel panel= new JPanel();
			JLabel label= new JLabel(Integer.toString(i+1));
			panel.add(label);
			pinsetterList.add(label);
			panels.add(panel);
		}

		//Following represents each row of the pins

		//******************************Fourth Row**************

		pins.add ( panels.get(6) );
		pins.add ( new JPanel ( ) );
		pins.add ( panels.get(7) );
		pins.add ( new JPanel ( ) );
		pins.add ( panels.get(8) );
		pins.add ( new JPanel ( ) );
		pins.add ( panels.get(9) );

		//*****************************Third Row***********

		pins.add ( new JPanel ( ) );
		pins.add ( panels.get(3) );
		pins.add ( new JPanel ( ) );
		pins.add ( panels.get(4) );
		pins.add ( new JPanel ( ) );
		pins.add ( panels.get(5) );

		//*****************************Second Row**************

		pins.add ( new JPanel ( ) );
		pins.add ( new JPanel ( ) );
		pins.add ( new JPanel ( ) );
		pins.add ( panels.get(2) );
		pins.add ( new JPanel ( ) );
		pins.add ( panels.get(1) );
		pins.add ( new JPanel ( ) );
		pins.add ( new JPanel ( ) );

		//******************************First Row*****************

		pins.add ( new JPanel ( ) );
		pins.add ( new JPanel ( ) );
		pins.add ( new JPanel ( ) );
		pins.add ( panels.get(0) );
		pins.add ( new JPanel ( ) );
		pins.add ( new JPanel ( ) );
		pins.add ( new JPanel ( ) );
		//*********************************************************
	}

    /**
     * This method receives a pinsetter event.  The event is the current
     * state of the PinSetter and the method changes how the GUI looks
     * accordingly.  When pins are "knocked down" the corresponding label
     * is grayed out.  When it is the second roll, it is indicated by the
     * appearance of a second yellow box at the top.
     *
     * @param pe    The state of the pinsetter is sent in this event.
     */
    public void receivePinsetterEvent(PinsetterEvent pe){
		if ( !(pe.isFoulCommitted())) {
	    	JLabel tempPin = new JLabel ();
	    	for ( int c = 0; c < 10; c++ ) {
				boolean pin = pe.pinKnockedDown ( c );
				tempPin = pinsetterList.get ( c );
				if ( pin ) {
		    		tempPin.setForeground ( Color.lightGray );
				}
	    	}
    	}
		if ( pe.getThrowNumber() == 1 ) {
	   		 secondRoll.setBackground ( Color.yellow );
		}
		if ( pe.pinsDownOnThisThrow() == -1) {
			for ( int i = 0; i != 10; i++){
				pinsetterList.get(i).setForeground(Color.black);
			}
			secondRoll.setBackground( Color.black);
		}
    }

	/**
	 * This function is called when we've to show the frame
	 */
    public void show() {
    	frame.show();
    }

	/**
	 * This function is called when we have to hide/close the frame
	 */
    public void hide() {
    	frame.dispose();
    }
}
