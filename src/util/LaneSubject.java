package util;

/**
 *
 */
public interface LaneSubject {

    /**
     *
     * @param observer
     */
    public void subscribe(LaneObserver observer);

    /**
     *
     * @param observer
     */
    public void unsubscribe(LaneObserver observer);

    /**
     *
     */
    public void publish();
}
