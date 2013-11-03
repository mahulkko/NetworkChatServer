package util.observer.connection;


/**
 * Subject Interface for the Observer
 * @author Martin Hulkkonen
 */
public interface ISubjectConnection {
    
    /**
     * Register new observer
     * @param observer - Observer
     */
    void registerObserver(IObserverConnection observer);

    /**
     * Remove the insert observer
     * @param observer - Observer
     */
    void removeObserver(IObserverConnection observer);

   /**
    * Notify all observer
    * @param threadId - threadId
    */
    void notifyObservers(int threadId);
}
