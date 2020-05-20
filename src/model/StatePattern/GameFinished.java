package model.StatePattern;

import model.Bowler;
import model.Lane;
import model.ScoreReport;
import view.EndGamePrompt;
import view.EndGameReport;

import java.util.Iterator;
import java.util.List;

/**
 * The Concrete State class represents the game finished state
 */
public class GameFinished implements GameStateInterface{

    private final GameState gameState;

    /**
     * Constructor for the GameFinished state class
     * Will take the current gameState with the current Lane
     * @param gameState: current gameState
     */
    public GameFinished(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * The handleGame() implementation for GameFinished
     * Will handle the game operations when the game is finished
     */
    @Override
    public void handleGame() {
        Lane lane = gameState.getLane();

        EndGamePrompt egp = new EndGamePrompt(lane.getParty().getMembers().get(0).getNickName()
                + "'s Party");
        int result = egp.getResult();
        egp.destroy();

        System.out.println("result was: " + result);

        if (result == 1) {                    // yes, want to play again
            lane.resetScores();
            lane.resetBowlerIterator();
        } else if (result == 2) {           // no, dont want to play another game
            List<String> printList;
            EndGameReport egr = new EndGameReport(lane.getParty().getMembers().get(0).getNickName()
                    + "'s Party", lane.getParty());
            printList = egr.getResult();

            lane.setPartyAssigned(false);

            Iterator scoreIt = lane.getParty().getMembers().iterator();
            lane.setParty(null);
            lane.publish();

            int myIndex = 0;

            while (scoreIt.hasNext()) {
                Bowler thisBowler = (Bowler) scoreIt.next();
                ScoreReport sr = new ScoreReport(thisBowler, lane.getFinalScores()[myIndex++], lane.getGameNumber());
                sr.sendEmail(thisBowler.getEmail());
                for (String s : printList) {
                    if (thisBowler.getNickName().equals(s)) {
                        System.out.println("Printing " + thisBowler.getNickName());
                        sr.sendPrintout();
                    }
                }
            }
        }
    }

}
