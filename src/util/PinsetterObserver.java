package util;

import controller.PinsetterEvent;

/**
 * Interface that will be implemented by the classes that are observing the Pinsetter
 */
public interface PinsetterObserver {

	/**
	 * defines the method for an object to receive a pinsetter event
	 * @param pe
	 */
    void receivePinsetterEvent(PinsetterEvent pe);

}

