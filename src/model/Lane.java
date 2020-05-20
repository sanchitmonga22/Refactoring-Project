package model;

import controller.PinsetterEvent;
import model.StatePattern.GameFinished;
import model.StatePattern.GameHalted;
import model.StatePattern.GameReady;
import model.StatePattern.GameState;
import util.LaneObserver;
import util.LaneSubject;
import util.PinsetterObserver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Lane extends Thread implements LaneSubject, PinsetterObserver {
	private Party party;
	private Pinsetter setter;
	private HashMap<Bowler,int[]> scores;
	private List<LaneObserver> subscribers;

	private boolean gameIsHalted;
	private boolean partyAssigned;
	private boolean gameFinished;

	private Iterator bowlerIterator;
	private int ball;
	private int bowlIndex;
	private int frameNumber;
	private boolean tenthFrameStrike;

	private int[][] cumulScores;
	private boolean canThrowAgain;

	private int[][] finalScores;
	private int gameNumber;

	private Bowler currentThrower;			// = the thrower who just took a throw

	/** Lane()
	 * Constructs a new lane and starts its thread
	 * @pre none
	 * @post a new lane has been created and its thered is executing
	 */
	public Lane() {
		setter = new Pinsetter();
		scores = new HashMap<>();
		subscribers = new ArrayList<>();

		gameIsHalted = false;
		partyAssigned = false;

		gameNumber = 0;

		setter.subscribe( this );

		this.start();
	}



	/** run()
	 * entry point for execution of this lane 
	 */
	public void run() {

		while (true) {

			if (partyAssigned && !gameFinished) {	// Lane is occupied and bowler can throw

				while (gameIsHalted) {
					GameState game = new GameState(this);
					game.setGameState(new GameHalted(game));
					game.handleGame();

				}

				if (bowlerIterator.hasNext()) {
					GameState game = new GameState(this);
					game.setGameState(new GameReady(game));
					game.handleGame();
					bowlIndex++;
				}
				else{
					frameCompleted();
				}

			} else if (partyAssigned && gameFinished) {
				GameState game = new GameState(this);
				game.setGameState(new GameFinished(game));
				game.handleGame();

			}

			try {
				sleep(10);
			} catch (Exception e) {}
		}
	}

	/** bowlerSimulation()
     * updates the current thrower amd then simulates their turn
	 */
	public void bowlerSimulation(){
        currentThrower = (Bowler)bowlerIterator.next();

        canThrowAgain = true;
        tenthFrameStrike = false;
        ball = 0;
        while (canThrowAgain) {
            setter.ballThrown();		// simulate the thrower's ball hiting
            ball++;
        }
    }

    /** frameCompleted()
     * advances the game onto the next frame
     */
    public void frameCompleted(){
        frameNumber++;
        resetBowlerIterator();
        bowlIndex = 0;
        if (frameNumber > 9) {
            gameFinished = true;
            gameNumber++;
        }
    }

	/** receivePinsetterEvent()
	 * receives the thrown event from the pinsetter
	 * @pre none
	 * @post the event has been acted upon if desired
	 * @param pe 		The pinsetter event that has been received.
	 */
	public void receivePinsetterEvent(PinsetterEvent pe) {
		if (pe.pinsDownOnThisThrow() >=  0) {			// this is a real throw
			markScore(currentThrower, frameNumber + 1, pe.getThrowNumber(), pe.pinsDownOnThisThrow());
			// handle the case of 10th frame first
			if (frameNumber == 9) {
				if (pe.totalPinsDown() == 10) {
					setter.resetPins();
					if(pe.getThrowNumber() == 1) {
						tenthFrameStrike = true;
					}
				}
				if ((pe.totalPinsDown() != 10) && (pe.getThrowNumber() == 2 && !tenthFrameStrike)) {
					canThrowAgain = false;
				}
				if (pe.getThrowNumber() == 3) {
					canThrowAgain = false;
				}
			} else { // its not the 10th frame
				if (pe.pinsDownOnThisThrow() == 10) {		// threw a strike
					canThrowAgain = false;
				} else if (pe.getThrowNumber() == 2) {
					canThrowAgain = false;
				} else if (pe.getThrowNumber() == 3)
					System.out.println("I'm here...");
			}
		}
	}

	/** resetBowlerIterator()
	 * sets the current bower iterator back to the first bowler
	 * @pre the party as been assigned
	 * @post the iterator points to the first bowler in the party
	 */
	public void resetBowlerIterator() {
		bowlerIterator = (party.getMembers()).iterator();
	}

	/** resetScores()
	 * resets the scoring mechanism, must be called before scoring starts
	 * @pre the party has been assigned
	 * @post scoring system is initialized
	 */
	public void resetScores() {
		for(Bowler bowler:party.getMembers()){
			int[] toPut = new int[25];
			for ( int i = 0; i != 25; i++){
				toPut[i] = -1;
			}
			scores.put( bowler, toPut );
		}
		gameFinished = false;
		frameNumber = 0;
	}

	/** assignParty()
	 * assigns a party to this lane
	 * @pre none
	 * @post the party has been assigned to the lane
	 * @param theParty		Party to be assigned
	 */
	public void assignParty( Party theParty ) {
		party = theParty;
		resetBowlerIterator();
		partyAssigned = true;

		cumulScores = new int[party.getMembers().size()][10];
		finalScores = new int[party.getMembers().size()][128]; //Hardcoding a max of 128 games, bite me.
		gameNumber = 0;

		resetScores();
	}

	/** markScore()
	 * Method that marks a bowlers score on the board.
	 * @param Cur		The current bowler
	 * @param frame	The frame that bowler is on
	 * @param ball		The ball the bowler is on
	 * @param score	The bowler's score 
	 */
	private void markScore( Bowler Cur, int frame, int ball, int score ){
		int[] curScore;
		int index =  ( (frame - 1) * 2 + ball);

		curScore = scores.get(Cur);

		curScore[ index - 1] = score;
		scores.put(Cur, curScore);
		getScore( Cur, frame );
		publish();
	}


	/** getScore()
	 * Method that calculates a bowlers score
	 * @param Cur        The bowler that is currently up
	 * @param frame    The frame the current bowler is on
	 */
	private void getScore(Bowler Cur, int frame) {
		int[] curScore;
		int strikeballs;
		curScore = scores.get(Cur);
		for (int i = 0; i != 10; i++){
			cumulScores[bowlIndex][i] = 0;
		}
		int current = 2*(frame - 1)+ball-1;
		//Iterate through each ball until the current one.
		for (int i = 0; i != current+2; i++){
			//Spare:
			if( i%2 == 1 && curScore[i - 1] + curScore[i] == 10 && i < current - 1 && i < 19){
				//This ball was a the second of a spare.  
				//Also, we're not on the current ball.
				//Add the next ball to the ith one in cumul.
				cumulScores[bowlIndex][(i/2)] += curScore[i+1] + curScore[i];
				if (i > 1) {
					//cumulScores[bowlIndex][i/2] += cumulScores[bowlIndex][i/2 -1];
				}
			} else if( i < current && i%2 == 0 && curScore[i] == 10  && i < 18){
				strikeballs = 0;
				//This ball is the first ball, and was a strike.
				//If we can get 2 balls after it, good add them to cumul.
				if (curScore[i+2] != -1) {
					strikeballs = 1;
					if(curScore[i+3] != -1) {
						//Still got em.
						strikeballs = 2;
					} else if(curScore[i+4] != -1) {
						//Ok, got it.
						strikeballs = 2;
					}
				}
				if (strikeballs == 2){
					//Add up the strike.
					//Add the next two balls to the current cumulscore.
					cumulScores[bowlIndex][i/2] += 10;
					if(curScore[i+1] != -1) {
						cumulScores[bowlIndex][i/2] += curScore[i+1] + cumulScores[bowlIndex][(i/2)-1];
						if (curScore[i+2] != -1){
							if( curScore[i+2] != -2){
								cumulScores[bowlIndex][(i/2)] += curScore[i+2];
							}
						} else {
							if( curScore[i+3] != -2){
								cumulScores[bowlIndex][(i/2)] += curScore[i+3];
							}
						}
					} else {
						if ( i/2 > 0 ){
							cumulScores[bowlIndex][i/2] += curScore[i+2] + cumulScores[bowlIndex][(i/2)-1];
						} else {
							cumulScores[bowlIndex][i/2] += curScore[i+2];
						}
						if (curScore[i+3] != -1){
							if( curScore[i+3] != -2){
								cumulScores[bowlIndex][(i/2)] += curScore[i+3];
							}
						} else {
							cumulScores[bowlIndex][(i/2)] += curScore[i+4];
						}
					}
				} else {
					break;
				}
			}else {
				//We're dealing with a normal throw, add it and be on our way.
				if( i%2 == 0 && i < 18){
					if ( i/2 == 0 ) {
						//First frame, first ball.  Set his cumul score to the first ball
						if(curScore[i] != -2){
							cumulScores[bowlIndex][i/2] += curScore[i];
						}
					} else {
						//add his last frame's cumul to this ball, make it this frame's cumul.
						if(curScore[i] != -2){
							cumulScores[bowlIndex][i/2] += cumulScores[bowlIndex][i/2 - 1] + curScore[i];
						} else {
							cumulScores[bowlIndex][i/2] += cumulScores[bowlIndex][i/2 - 1];
						}
					}
				} else if (i < 18){
					if(curScore[i] != -1 && i > 2){
						if(curScore[i] != -2){
							cumulScores[bowlIndex][i/2] += curScore[i];
						}
					}
				}
				if (i/2 == 9){
					if (i == 18){
						cumulScores[bowlIndex][9] += cumulScores[bowlIndex][8];
					}
					if(curScore[i] != -2){
						cumulScores[bowlIndex][9] += curScore[i];
					}
				} else if (i/2 == 10) {
					if(curScore[i] != -2){
						cumulScores[bowlIndex][9] += curScore[i];
					}
				}
			}
		}
	}

	/** isPartyAssigned()
	 * checks if a party is assigned to this lane
	 * @return true if party assigned, false otherwise
	 */
	public boolean isPartyAssigned() {
		return partyAssigned;
	}

	/** subscribe()
	 * Method that will add a subscriber
	 * @param adding	Observer that is to be added
	 */
	public void subscribe( LaneObserver adding ) {
		subscribers.add( adding );
	}

	/** unsubscribe()
	 * Method that unsubscribes an observer from this object
	 * @param removing	The observer to be removed
	 */
	public void unsubscribe( LaneObserver removing ) {
		subscribers.remove( removing );
	}

	/** publish()
	 * broadcasts the LaneEvent for all subscribers
	 */
	public void publish() {
		if( subscribers.size() > 0 ) {
			for(LaneObserver observer:subscribers){
			observer.receiveLaneEvent();
			}
		}
	}

	/** getPinsetter()
	 * Accessor to get this Lane's pinsetter
	 * @return		A reference to this lane's pinsetter
	 */
	public Pinsetter getPinsetter() {
		return setter;
	}

	/** pauseGame()
	 * Pause the execution of this game
	 */
	public void pauseGame() {
		gameIsHalted = true;
		publish();
	}

	/** unPauseGame()
	 * Resume the execution of this game
	 */
	public void unPauseGame() {
		gameIsHalted = false;
		publish();
	}

	/** isMechanicalProblem()
	 * @return is the game halted?
	 */
	public boolean isMechanicalProblem() {
		return gameIsHalted;
	}

	/** getFrameNum()
	 * @return the current frame number
	 */
	public int getFrameNum() {
		return frameNumber+1;
	}

	/** getScore()
	 * @return the bowler's score
	 */
	public HashMap<Bowler,int[]> getScore( ) {
		return scores;
	}

	/** getIndex()
	 * @return the current index of the bowl
	 */
	public int getIndex() {
		return bowlIndex;
	}

	/** getBall()
	 * @return the current ball
	 */
	public int getBall( ) {
		return ball;
	}

	/** getCumulScore()
	 * @return the cumulative scores
	 */
	public int[][] getCumulScore(){
		return cumulScores;
	}

	/** getParty()
	 * @return returns the party assigned to the lane
	 */
	public Party getParty() {
		return party;
	}

	/** getBowler()
	 * @return get's the current thrower
	 */
	public Bowler getBowler() {
		return currentThrower;
	}

	/** getSetter()
	 * @return get's the current setter
	 */
	public Pinsetter getSetter() {
		return setter;
	}

	/** getBowlIndex()
	 * @return the index of the current bowl
	 */
	public int getBowlIndex() {
		return bowlIndex;
	}

	/** getFinalScores()
	 * @return the final scores of the game
	 */
	public int[][] getFinalScores() {
		return finalScores;
	}

	/** getGameNumber()
	 * @return the number of the game
	 */
	public int getGameNumber() {
		return gameNumber;
	}

	/** getCurrentThrower()
	 * @return the current thrower in the game
	 */
	public Bowler getCurrentThrower() {
		return currentThrower;
	}

	/** setParty()
	 * @param party the party to set to
	 */
	public void setParty(Party party) {
		this.party = party;
	}

	/** setPartyAssigned()
	 * @param partyAssigned whether the party has been assigned or not
	 */
	public void setPartyAssigned(boolean partyAssigned) {
		this.partyAssigned = partyAssigned;
	}
}
