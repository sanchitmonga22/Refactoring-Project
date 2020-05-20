package model;

import java.util.*;

/**
 *  Container that holds bowlers
 */

public class Party {

	/** Vector of bowlers in this party */	
    private List<Bowler> myBowlers;
	
	/** Party()
	 * Constructor for a Party
	 * @param bowlers	Vector of bowlers that are in this party
	 */
    public Party( List<Bowler> bowlers ) {
		myBowlers = new ArrayList<>(bowlers);
    }

	/** getMembers()
	 * Accessor for members in this party
	 * @return 	A vector of the bowlers in this party
	 */
    public List<Bowler> getMembers() {
		return myBowlers;
    }

}
