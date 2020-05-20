package controller;

public class PinsetterEvent {

	private boolean[] pinsStillStanding;
	private boolean foulCommitted;
	private int throwNumber;
	private int pinsDownThisThrow;

	/** PinsetterEvent()
	 * Constructor for a new pinsetter event
	 * @pre none
	 * @post the object has been initialized
	 */
	public PinsetterEvent(boolean[] pinsStanding, boolean foul, int throwNum, int pinsDownThisThrow) {
		pinsStillStanding = new boolean[10];

		for (int i=0; i <= 9; i++) {
			pinsStillStanding[i] = pinsStanding[i];
		}
		
		foulCommitted = foul;
		throwNumber = throwNum;
		this.pinsDownThisThrow = pinsDownThisThrow;
	}

	/** pinKnockedDown()
	 * check if a pin has been knocked down
	 * @return true if pin [i] has been knocked down
	 */
	public boolean pinKnockedDown(int i) {
		return !pinsStillStanding[i];
	}
	
	/** pinsDownOnThisThrow()
	 * @return the number of pins knocked down associated with this event
	 */
	public int pinsDownOnThisThrow() {
		return pinsDownThisThrow;
	}
	
	/** totalPinsDown()
	 * @return the total number of pins down for pinsetter that generated the event
	 */
	public int totalPinsDown() {
		int count = 0;
		
		for (int i=0; i <= 9; i++) {
			if (pinKnockedDown(i)) {
				count++;
			}
		}
		
		return count;
	}
	
	/** isFoulCommitted()
	 * @return true if a foul was committed on the lane, false otherwise
	 */
	public boolean isFoulCommitted() {
		return foulCommitted;
	}

	/** getThrowNumber()
	 * @return current number of throws taken on this lane after last reset
	 */
	public int getThrowNumber() {
		return throwNumber;
	}
}

