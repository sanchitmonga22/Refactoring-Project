package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Class that represents the view for the EndGamePrompt where the user will have the option to choose whether
 * or not they want to play another game
 */
public class EndGamePrompt implements ActionListener {

	private JFrame win;

	/** ButtonPressed:0 represents that no button has been pressed yet
	 *  ButtonPressed:1 represents the Yes button has been pressed ie the party wants to play another game
	 *  ButtonPressed:2 represents the No button has been pressed ie the party does not want to play another game */
	private int buttonPressed;

	// Buttons to represent Yes/No for the endGamePrompt
	private JButton yesButton;
	private JButton noButton;

	public EndGamePrompt( String partyName ) {

		buttonPressed =0;

		// Creating the main Frame for the GUI
		win = new JFrame("Another Game for " + partyName + "?" );
		win.getContentPane().setLayout(new BorderLayout());
		((JPanel) win.getContentPane()).setOpaque(false);

		JPanel colPanel = new JPanel();
		colPanel.setLayout(new GridLayout( 2, 1 ));

		// Adding the required components in the Panel
		colPanel.add(initializeLabelPanel(partyName));
		colPanel.add(initializeButtons());

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
	 * Initializing the panel including the Label in the GUI
	 * @param partyName	The name of the party that has to be asked to play another game
	 * @return The panel including all the components in the GUI
	 */
	public JPanel initializeLabelPanel(String partyName){
		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new FlowLayout());

		// adding the message in the Label
		JLabel message = new JLabel( "Party " + partyName +
				" has finished bowling.\nWould they like to bowl another game?" );

		labelPanel.add( message );
		return labelPanel;
	}

	/**
	 * Initializes the Yes No button for the endGamePrompt
	 * @return The JPanel that contains the view and essential components for the Yes/No prompt
	 */
	public JPanel initializeButtons(){
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 2));

		new Insets(4, 4, 4, 4);

		// adding the functionality for Yes button
		yesButton = new JButton("Yes");
		JPanel yesButtonPanel = new JPanel();
		yesButtonPanel.setLayout(new FlowLayout());
		yesButton.addActionListener(this);
		yesButtonPanel.add(yesButton);

		// adding the functionality for No Button
		noButton = new JButton("No");
		JPanel noButtonPanel = new JPanel();
		noButtonPanel.setLayout(new FlowLayout());
		noButton.addActionListener(this);
		noButtonPanel.add(noButton);

		// adding yes/no buttons in the Panel
		buttonPanel.add(yesButton);
		buttonPanel.add(noButton);

		return buttonPanel;
	}

	/**
	 * Handles all the events when buttons are pressed
	 * @param e The event that includes which button was pressed
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(yesButton)) {	// if yes button pressed
			buttonPressed=1;
		}
		if (e.getSource().equals(noButton)) {	// if no button pressed
			buttonPressed=2;
		}
	}

	/**
	 * Waits until user presses yes or no
	 * Result:1 represents the Yes button has been pressed ie the party wants to play another game
	 * Result:2 represents the No button has been pressed ie the party does not want to play another game
	 * @return  The result based on what button user pressed
	 */
	public int getResult() {
		while ( buttonPressed == 0 ) {
			try {
				Thread.sleep(10);
			} catch ( InterruptedException e ) {
				System.err.println( "Interrupted" );
			}
		}
		return buttonPressed;
	}

	/**
	 * Turns off the window
	 */
	public void destroy() {
		win.dispose();
	}
}

