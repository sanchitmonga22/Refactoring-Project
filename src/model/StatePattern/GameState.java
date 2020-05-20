package model.StatePattern;

import model.Lane;

/**
 * This is the class that will act as the context in the Run StatePattern
 *
 * When an instance is created, the current Lane will be passed through so
 * the ConcreteStates(GameHalted, GameFinished, GameReady) have access to
 * Lane's attributes.
 */

public class GameState {
    private GameStateInterface currentGameState;
    private Lane currentLane;

    /**
     * The constructor for a GameState object
     * @param currentLane: The current Lane that the Game is running on
     */
    public GameState(Lane currentLane){
        this.currentLane = currentLane;
    }

    /**
     * Will set the current state of the game
     * @param gameState: One of the concrete states mentioned above
     */
    public void setGameState(GameStateInterface gameState){
        currentGameState = gameState;
    }

    /**
     * The method that will call the appropriate handleGame() method for the given state
     */
    public void handleGame(){
        currentGameState.handleGame();
    }

    /**
     * Will get the currentLane
     * @return: currentLane
     */
    public Lane getLane() {
        return currentLane;
    }

}
