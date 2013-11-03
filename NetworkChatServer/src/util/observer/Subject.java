package util.observer;

/**
 * Subject Interface for the Observer
 * @author Martin Hulkkonen
 * @param <K> - Generic value for the update
 */
public interface Subject<K> {
    
    /**
     * Register new observer
     * @param observer - Observer
     */
    public void registerObserver(Observer<K> observer);

    /**
     * Remove the insert observer
     * @param observer - Observer
     */
    public void removeObserver(Observer<K> observer);

    /**
     * Notify all connected observer
     */
    public void notifyObservers();
}
