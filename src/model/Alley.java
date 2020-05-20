package model;

import controller.ControlDesk;

/**
 *  Class that is the outer container for the bowling sim
 *
 */

public class Alley {

    /** The ControlDesk for the Alley */
    public ControlDesk controldesk;

    /** Alley()
     * Constructor for the Alley
     * @param numLanes the number of lanes in the bowling alley
     */
    public Alley( int numLanes ) {
        controldesk = new ControlDesk( numLanes );
    }

    /** getControlDesk()
     * @return the ControlDesk used by the Alley
     */
	public ControlDesk getControlDesk() {
		return controldesk;
	}
	
}


    
