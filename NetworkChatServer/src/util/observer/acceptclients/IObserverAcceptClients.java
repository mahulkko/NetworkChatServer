package util.observer.acceptclients;

import java.net.Socket;

/**
 * 
 * @author Martin Hulkkonen
 */
public interface IObserverAcceptClients {
    
    /**
     * Update the observer
     * @param threadId - Id of the new connected thread
     */
    void updateClients(Socket client);
}
