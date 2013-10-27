package NetworkConnection.impl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

public class AcceptClients implements Runnable {

	/**
	 * NetworkConnectionManager - handling the connection
	 */
	private NetworkConnectionManager networkConnectionManager;
	
	/**
	 * Socket for accepting a new client
	 */
	Socket client;
	
	/**
	 * Socket from the server to listen for new clients
	 */
	ServerSocket server;
	
	/**
	 * Logger for log4j connection
	 */
	static Logger log = Logger.getLogger("NetworkConnection.AcceptClients");
	
	/**
	 * AcceptClients
	 * @param manager - NetworkConnectionManager to handle the connections of the clients
	 */
	public AcceptClients(NetworkConnectionManager manager) {
		log.info("Inizialize the accept clients thread");
		this.networkConnectionManager = manager;
		this.server = this.networkConnectionManager.getServerSocket();
	}
	
	@Override
	public void run() {
		log.info("Start the accept client thread");
		while (this.networkConnectionManager.isServerRunning()) {
			try {
				log.info("Wait for a new client connection");
				this.client = this.server.accept();
				log.info("New client accepted");
				this.networkConnectionManager.manageNewConnection(this.client);
			} catch (IOException e) {
				log.error("Failed to accept a new client");
			}
		}
	}
}
