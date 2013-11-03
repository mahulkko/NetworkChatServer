package util.observer.acceptclients;

import java.net.Socket;


/**
 * Subject Interface for the Observer
 * @author Martin Hulkkonen
 */
public interface ISubjectAcceptClients {
    
    /**
     * Register new observer
     * @param observer - Observer
     */
    void registerObserver(IObserverAcceptClients observer);

    /**
     * Remove the insert observer
     * @param observer - Observer
     */
    void removeObserver(IObserverAcceptClients observer);

    /**
     * Notify all connected observer
     */
    void notifyObservers(Socket client);
}
