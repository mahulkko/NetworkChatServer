package connection.network.impl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

/**
 * 
 * @author Martin Hulkkonen
 *
 */
public class AcceptClients implements Runnable {

    /**
     * NetworkConnectionManager - handling the connection
     */
    private NetworkConnectionManager networkConnectionManager;
    
    /**
     * Socket from the server to listen for new clients
     */
    private ServerSocket server;
    
    /**
     * Logger for log4j connection
     */
    private static Logger log = Logger.getLogger("NetworkConnection.AcceptClients");
    
    /**
     * AcceptClients
     * @param manager - NetworkConnectionManager to handle the connections of the clients
     */
    public AcceptClients(NetworkConnectionManager manager) {
        log.info("Inizialize the accept clients thread");
        this.networkConnectionManager = manager;
        this.server = this.networkConnectionManager.getServerSocket();
    }
    
    /**
     * Thread for accepting new clients.
     */
    @Override
    public void run() {
        log.info("Start the accept client thread");
        while (this.networkConnectionManager.isServerRunning()) {
            try {
                log.info("Wait for a new client connection");
                Socket client = this.server.accept();
                log.info("New client accepted");
                this.networkConnectionManager.manageNewConnection(client);
            } catch (IOException e) {
                log.error("Failed to accept a new client");
            }
        }
        log.info("AcceptClients thread is stopping");
    }
}
