package util;

import controller.ControlDeskEvent;

/**
 * Interface for classes that observe control desk events
 */
public interface ControlDeskObserver {

	/**
	 * Used by ControlDeskView to receive events from the ControlDesk
	 * @param ce
	 */
    void receiveControlDeskEvent(ControlDeskEvent ce);

}
