package NetworkConnection.impl;

import java.io.IOException;
import java.net.Socket;
import org.apache.log4j.Logger;

	
/**
 * AcceptClients - Thread class for the accept of new clients
 * @author Martin Hulkkonen
 *
 */
public class AcceptClient implements Runnable {
	
	/**
	 * Connection Class 
	 */
	private ConnectionManagement con;
	
	/**
	 * Logger for log4j acceptClients
	 */
	static Logger log = Logger.getLogger("Connection.NetworkConnection.AcceptClient");
	
	public AcceptClient(ConnectionManagement con) {
		this.con = con;
	}

	/**
	 * Waiting for new clients and accept them.
	 */
	@Override
	public void run() {
		Socket socket;
		log.info("AcceptClients Thread started");
		try {
			// Let the server work
			while(this.con.getServerStatus()) {
				// Accept new connections
				log.info("Wait for new Clients");
				socket = this.con.getSocket().accept();
				log.info("New client accepted");
				this.con.manageConnection(socket);
			}
		} catch (IOException e) {
			log.error("Could not Accept a new client");
		}
	}
}
