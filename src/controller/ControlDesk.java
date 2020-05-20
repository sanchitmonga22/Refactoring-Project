package controller;

/**
 * Class that represents control desk
 */
import model.Bowler;
import model.Lane;
import model.Party;
import util.BowlerFile;
import util.ControlDeskObserver;
import java.util.*;
import java.io.*;

public class ControlDesk extends Thread {

	/** The collection of Lanes */
	private HashSet<Lane> lanes;

	/** The party wait queue */
	private Queue partyQueue;

	/** The number of lanes represented */
	private int numLanes;
	
	/** The collection of subscribers */
	private List<ControlDeskObserver> subscribers;

    /** ControlDesk()
     * Constructor for the ControlDesk class
     * @param numLanes	the number of lanes to be represented
     */
	public ControlDesk(int numLanes) {
		this.numLanes = numLanes;
		lanes = new HashSet<>();
		partyQueue = new Queue();

		subscribers = new ArrayList<>();

		for (int i = 0; i < numLanes; i++) {
			lanes.add(new Lane());
		}
		
		this.start();
	}
	
	/** run()
	 * Main loop for ControlDesk's thread
	 */
	public void run() {
		while (true) {
			
			assignLane();
			
			try {
				sleep(250);
			} catch (Exception e) {}
		}
	}
		

    /** registerPatron()
     * Retrieves a matching Bowler from the bowler database.
     * @param nickname	The Nickname of the Bowler
     * @return a Bowler object.
     *
     */
	private Bowler registerPatron(String nickname) {
		Bowler patron = null;

		try {
			// only one patron / nick.... no dupes, no checks

			patron = BowlerFile.getBowlerInfo(nickname);

		} catch (FileNotFoundException e) {
			System.err.println("Error..." + e);
		} catch (IOException e) {
			System.err.println("Error..." + e);
		}

		return patron;
	}

    /**assignLane()
     * Iterate through the available lanes and assign the parties in the wait queue if lanes are available.
     */
	public void assignLane() {
		for(Lane lane:lanes){
			if(partyQueue.hasMoreElements() && !lane.isPartyAssigned()) {
				System.out.println("ok... assigning this party");
				lane.assignParty(((Party) partyQueue.next()));
			}
		}
		publish(new ControlDeskEvent(getPartyQueue()));
	}

    /** addPartyQueue()
     * Creates a party from an ArrayList of nicknames and adds them to the wait queue.
     * @param partyNicks	An ArrayList of nicknames
     */
	public void addPartyQueue(List<String> partyNicks) {
		List<Bowler> partyBowlers = new ArrayList<>();
		for (int i = 0; i < partyNicks.size(); i++) {
			Bowler newBowler = registerPatron(partyNicks.get(i));
			partyBowlers.add(newBowler);
		}
		Party newParty = new Party(partyBowlers);
		partyQueue.add(newParty);
		publish(new ControlDeskEvent(getPartyQueue()));
	}

    /** getPartyQueue()
     * Returns an ArrayList of party names to be displayed in the GUI representation of the wait queue.
     * @return an ArrayList of Strings
     */
	public List<String> getPartyQueue() {
		List<String> queuedParties = new ArrayList<>();

		for ( int i=0; i < partyQueue.size(); i++ ) {
			List<Bowler> allMembers= ((Party)partyQueue.get(i)).getMembers();
			String nextParty = allMembers.get(0).getNickName() + "'s Party";
			queuedParties.add(nextParty);
		}
		return queuedParties;
	}

    /** getNumLanes()
     * Accessor for the number of lanes represented by the ControlDesk
     * @return an int containing the number of lanes represented
     */
	public int getNumLanes() {
		return numLanes;
	}

    /** subscribe()
     * Allows objects to subscribe as observers
     * 
     * @param adding	the ControlDeskObserver that will be subscribed
     *
     */
	public void subscribe(ControlDeskObserver adding) {
		subscribers.add(adding);
	}

    /** publish()
     * Broadcast an event to subscribing objects.
     * @param event	the ControlDeskEvent to broadcast
     */
	public void publish(ControlDeskEvent event) {
		for(ControlDeskObserver observer:subscribers){
			observer.receiveControlDeskEvent(event);
		}
	}

    /** getLanes()
     * Accessor method for lanes
     * @return a HashSet of Lanes
     */
	public HashSet<Lane> getLanes() {
		return lanes;
	}
}
