package NetworkConnection.impl;

import java.io.IOException;
import java.net.Socket;
import org.apache.log4j.Logger;

	
/**
 * AcceptClients - Thread class for the accept of new clients
 * @author Martin Hulkkonen
 *
 */
public class AcceptClients implements Runnable {
	
	/**
	 * Connection Class 
	 */
	private Connection con;
	
	/**
	 * Logger for log4j acceptClients
	 */
	static Logger log = Logger.getLogger("Connection.NetworkConnection.AcceptClients");
	
	public AcceptClients(Connection con) {
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
			while(con.isRunning()) {
				// Accept new connections
				log.info("Wait for new Clients");
				socket = con.getSocket().accept();
				log.info("New client accepted");
				con.startNewMessageThread(socket);
			}
		} catch (IOException e) {
			log.error("Could not Accept a new client");
		}
	}
}
