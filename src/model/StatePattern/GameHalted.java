package model.StatePattern;

import model.Lane;

/**
 * The Concrete State class represents the game halted state
 */
public class GameHalted implements GameStateInterface{

    private final GameState gameState;

    /**
     * Constructor for the GameHalted state class
     * Will take the current gameState with the current Lane
     * @param gameState: current gameState
     */
    public GameHalted(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * The handleGame() implementation for GameHalted
     * Will handle the game when it is halted
     */
    @Override
    public void handleGame() {
        Lane lane = gameState.getLane();
        try {
            lane.sleep(10);
        } catch (Exception e) {}
    }

}
