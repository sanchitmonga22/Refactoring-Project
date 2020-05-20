package model.StatePattern;

import model.Lane;
import util.ScoreHistoryFile;

import java.util.Date;

/**
 * The Concrete State class represents the game in its ready, or playing, state
 */
public class GameReady implements GameStateInterface{
    private final GameState gameState;

    /**
     * Constructor for the GameReady state class
     * Will take the current gameState with the current Lane
     * @param gameState: current gameState
     */
    public GameReady(GameState gameState){
        this.gameState = gameState;
    }

    /**
     * The handleGame() implementation for GameReady
     * Will handle the game operations will it is running
     */
    @Override
    public void handleGame() {

        Lane lane = gameState.getLane();

        lane.bowlerSimulation();

        if (lane.getFrameNum() == 9){
            lane.getFinalScores()[lane.getBowlIndex()][lane.getGameNumber()] = lane.getCumulScore()[lane.getBowlIndex()][9];
            try{
                Date date = new Date();
                String dateString = "" + date.getHours() + ":" + date.getMinutes() + " " + date.getMonth() + "/" + date.getDay() + "/" + (date.getYear() + 1900);
                ScoreHistoryFile.addScore(lane.getCurrentThrower().getNickName(), dateString,
                        Integer.toString(lane.getCumulScore()[lane.getBowlIndex()][9]));
            } catch (Exception e) {System.err.println("Exception in addScore. "+ e );}
        }

        lane.getSetter().reset();
    }

}
