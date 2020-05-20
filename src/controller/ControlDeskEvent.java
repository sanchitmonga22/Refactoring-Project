package controller;

/**
 * Class that represents control desk event
 */
import java.util.*;

public class ControlDeskEvent {

	/** A representation of the wait queue, containing party names */
	private List<String> partyQueue;

    /** ControlDeskEvent()
     * Constructor for the ControlDeskEvent
     * @param partyQueue	a Vector of Strings containing the names of the parties in the wait queue
     */
	public ControlDeskEvent( List<String> partyQueue ) {
		this.partyQueue = partyQueue;
	}

    /** getPartyQueue()
     * Accessor for partyQueue
     * @return a List of Strings representing the names of the parties in the wait queue
     */
	public List<String> getPartyQueue() {
		return partyQueue;
	}
}
