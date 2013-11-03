package util.observer.connection;

/**
 * 
 * @author Martin Hulkkonen
 */
public interface IObserverConnection {
    
    /**
     * Update the observer
     * @param threadId - Id of the new connected thread
     */
    void updateConnection(int threadId);
}
