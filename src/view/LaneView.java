package view;

import model.Bowler;
import model.Lane;
import model.Party;
import util.LaneObserver;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;

/**
 * The actual view of each and every lane with all the information
 */
public class LaneView implements LaneObserver, ActionListener {

	private boolean initDone;

	JFrame frame;				// Main frame for the GUI
	Container cpanel;

	List<Bowler> bowlers;		// All the bowlers that are currently bowling

	JPanel[][] balls;
	JLabel[][] ballLabel;
	JPanel[][] scores;
	JLabel[][] scoreLabel;

	JPanel[][] ballGrid;
	JPanel[] pins;

	JButton maintenance;		// Button to call the maintenance

	Lane lane;

	public LaneView(Lane lane, int laneNum) {
		this.lane = lane;

		initDone = true;
		// Main frame of the GUI
		frame = new JFrame("Lane " + laneNum + ":");

		cpanel = frame.getContentPane();
		cpanel.setLayout(new BorderLayout());

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frame.dispose();
			}
		});

		cpanel.add(new JPanel());
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

	/**
	 *
	 * @param party The party object containing all the info
	 * @return The JPanel with all the information of the bowlers
	 */
	private JPanel makeFrame(Party party) {

		initDone = false;
		bowlers = party.getMembers();
		int numBowlers = bowlers.size();

		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(0, 1));

		balls = new JPanel[numBowlers][23];
		ballLabel = new JLabel[numBowlers][23];
		scores = new JPanel[numBowlers][10];
		scoreLabel = new JLabel[numBowlers][10];
		ballGrid = new JPanel[numBowlers][10];
		pins = new JPanel[numBowlers];

		for (int i = 0; i != numBowlers; i++) {
			for (int j = 0; j != 23; j++) {
				ballLabel[i][j] = new JLabel(" ");
				balls[i][j] = new JPanel();
				balls[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
				balls[i][j].add(ballLabel[i][j]);
			}
		}

		for (int i = 0; i != numBowlers; i++) {
			for (int j = 0; j != 9; j++) {
				ballGrid[i][j] = new JPanel();
				ballGrid[i][j].setLayout(new GridLayout(0, 3));
				ballGrid[i][j].add(new JLabel("  "), BorderLayout.EAST);
				ballGrid[i][j].add(balls[i][2 * j], BorderLayout.EAST);
				ballGrid[i][j].add(balls[i][2 * j + 1], BorderLayout.EAST);
			}
			int j = 9;
			ballGrid[i][j] = new JPanel();
			ballGrid[i][j].setLayout(new GridLayout(0, 3));
			ballGrid[i][j].add(balls[i][2 * j]);
			ballGrid[i][j].add(balls[i][2 * j + 1]);
			ballGrid[i][j].add(balls[i][2 * j + 2]);
		}

		for (int i = 0; i != numBowlers; i++) {
			pins[i] = new JPanel();
			pins[i].setBorder(BorderFactory.createTitledBorder(bowlers.get(i).getNickName()));
			pins[i].setLayout(new GridLayout(0, 10));
			for (int k = 0; k != 10; k++) {
				scores[i][k] = new JPanel();
				scoreLabel[i][k] = new JLabel("  ", SwingConstants.CENTER);
				scores[i][k].setBorder(BorderFactory.createLineBorder(Color.BLACK));
				scores[i][k].setLayout(new GridLayout(0, 1));
				scores[i][k].add(ballGrid[i][k], BorderLayout.EAST);
				scores[i][k].add(scoreLabel[i][k], BorderLayout.SOUTH);
				pins[i].add(scores[i][k], BorderLayout.EAST);
			}
			panel.add(pins[i]);
		}

		initDone = true;
		return panel;
	}

	/**
	 * This function is used to update the scores in the GUI everytime it is called
	 */
	public void receiveLaneEvent() {
		if (lane.isPartyAssigned()) {
			int numBowlers = lane.getParty().getMembers().size();
			while (!initDone) {
				try {
					Thread.sleep(1);
				} catch (Exception e) {
				}
			}

			if (lane.getFrameNum() == 1 && lane.getBall() == 0 && lane.getIndex() == 0) {
				System.out.println("Making the frame.");
				cpanel.removeAll();
				// Updating the GUI
				cpanel.add(makeFrame(lane.getParty()), "Center");
				cpanel.add(initializeButtonPanel(), "South");
				frame.pack();
			}

			int[][] laneCumulScore = lane.getCumulScore();
			for (int k = 0; k < numBowlers; k++) {

				for (int i = 0; i <= lane.getFrameNum() - 1; i++) {
					if (laneCumulScore[k][i] != 0)
						scoreLabel[k][i].setText((Integer.valueOf(laneCumulScore[k][i])).toString());
				}

				for (int i = 0; i < 21; i++) {

					// getting scores of the bowlers in the given lane
					int[] bowlers1= lane.getScore().get(bowlers.get(k));

					if (bowlers1[i] != -1)
						if (bowlers1[i] == 10 && (i % 2 == 0 || i == 19)){
							ballLabel[k][i].setText("X");
						}
						else if (i > 0 && bowlers1[i] + bowlers1[i - 1] == 10 && i % 2 == 1){
							ballLabel[k][i].setText("/");
						}
						else if ( bowlers1[i] == -2 ){
							ballLabel[k][i].setText("F");
						} else {
							ballLabel[k][i].setText((Integer.valueOf(bowlers1[i])).toString());
						}
				}
			}
		}
	}

	/**
	 * Initializes the view for the maintenance call
	 * @return JPanel containing all the information
	 */
	public JPanel initializeButtonPanel(){
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());

		new Insets(4, 4, 4, 4);

		maintenance = new JButton("Maintenance Call");
		JPanel maintenancePanel = new JPanel();
		maintenancePanel.setLayout(new FlowLayout());
		maintenance.addActionListener(this);
		maintenancePanel.add(maintenance);

		buttonPanel.add(maintenancePanel);
		return buttonPanel;
	}
	/**
	 * If maintenance call button is pressed
	 * @param e The event that contains data for any changes made in the system
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(maintenance)) {
			lane.pauseGame();
		}
	}
}
