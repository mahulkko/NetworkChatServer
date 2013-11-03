package connection.network.impl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import util.observer.acceptclients.IObserverAcceptClients;
import util.observer.acceptclients.ISubjectAcceptClients;

/**
 * 
 * @author Martin Hulkkonen
 *
 */
public class AcceptClients implements Runnable, ISubjectAcceptClients {
    
    /**
     * Socket from the server to listen for new clients
     */
    private ServerSocket server;
    
    /**
     * List for the Observer
     */
    private List<IObserverAcceptClients> observer;
    
    /**
     * Logger for log4j connection
     */
    private static Logger log = Logger.getLogger("NetworkConnection.AcceptClients");
    
    /**
     * AcceptClients
     * @param manager - NetworkConnectionManager to handle the connections of the clients
     */
    public AcceptClients(ServerSocket socket) {
        log.info("Inizialize the accept clients thread");
        this.server = socket;
        this.observer = new LinkedList<IObserverAcceptClients>();
    }
    
    /**
     * Thread for accepting new clients.
     */
    @Override
    public void run() {
        log.info("Start the accept client thread");
        while (true) {
            try {
                log.info("Wait for a new client connection");
                Socket client = this.server.accept();
                log.info("New client accepted");
                this.notifyObservers(client);
            } catch (IOException e) {
                log.error("Failed to accept a new client");
                break;
            }
        }
        log.info("AcceptClients thread is stopping");
    }

    /**
     * Register new observer
     * @param observer - Observer
     */
    @Override
    public void registerObserver(IObserverAcceptClients observer) {
       this.observer.add(observer);
    }

    /**
     * Remove the insert observer
     * @param observer - Observer
     */
    @Override
    public void removeObserver(IObserverAcceptClients observer) {
       this.observer.remove(observer);
    }

    /**
     * Notify all connected observer
     */
    @Override
    public void notifyObservers(Socket client) {
        for (int i = 0; i < this.observer.size(); ++i) {
            this.observer.get(i).updateClients(client);
        }
    }
}
