package NetworkConnection.impl;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import NetworkConnection.INetworkConnection;

/**
 * NetworkConnection Class - NetworkConnection
 * <br>
 * Written by Martin Hulkkonen
 * <br><br>
 * <b><u>Connection</u></b>
 * <br>
 * Manages the connection between the different clients witch connect to it 
 * Server managed the different connections from the clients
 * @author Martin Hulkkonen
 */

public class NetworkConnection implements INetworkConnection {
	
	/**
	 * Connection to start a server
	 */
	private ConnectionManagement con;
	
	/**
	 * Logger for log4j connection
	 */
	static Logger log = Logger.getLogger("Connection.NetworkConnection");
	
	/**
	 * Set the port to the value and initialize all for the start
	 * <br>
	 * @param port - Set the port where the serve should run 
	 */
	public NetworkConnection(int port) {
		this.con = new ConnectionManagement();
		this.con.setPort(port);
	}

	@Override
	public boolean startServer() {
		// If the server do not run
		log.info("Try to start the server");
		
		if(!con.getServerStatus()){
			try {
				// Make a new connection
				log.info("Create new Socket for the connection");
				ServerSocket server = new ServerSocket(this.con.getPort());
				this.con.setSocket(server);
				this.con.setServerStatus(true);
				log.info("Server is startet on Port: " + this.con.getPort());
				// Let the server work
				log.info("Create and Start new Thread for Accept Clients");
				this.con.setAcceptThread(new Thread(new AcceptClient(this.con)));
				this.con.getAcceptThread().start();
				return true;
			} catch (IOException e) {
				log.error("Could not start the server");
			}
		}	
		log.info("Server is already running");
		return false;
	}

	@Override
	public boolean stopServer() {
		// If the server is running
		log.info("Try to stop the Server");
		if(this.con.getServerStatus()){
			try {
				// Close the connection
				this.con.setServerStatus(false);
				this.con.getSocket().close();
				log.info("Server now halt");
				return true;
			} catch (IOException e) {
				log.error("Server didnt stop!");
				return false;
			}
		}
		log.info("Sever didnt running");
		return false;
	}
	
	@Override
	public void sendMessageToThreadID(int ThreadID, String msg) {
		con.sendMessageToThreadID(msg, ThreadID);
	}
	
	@Override
	public boolean startReceiveMessagesFromThreadID(int ThreadID, LinkedBlockingQueue<String> queue) {
		return con.startReceivingMessagesFromThreadID(queue, ThreadID);
	}
	
	@Override
	public boolean stopReceiveMessagesFromThreadID(int ThreadID, LinkedBlockingQueue<String> queue) {
		return con.stopReceivingMessagesFromThreadID(queue, ThreadID);
	}
	
	@Override
	public boolean startAutoReceiveFromAllThreads(LinkedBlockingQueue<String> queue) {
		return false;
	}
	
	@Override
	public boolean stopAutoReceiveFromAllThreads(LinkedBlockingQueue<String> queue) {
		return false;
	}
	
	@Override
	public boolean clientConnected(int ThreadID) {
		return false;
	}
	
	@Override
	public int getMaxConnections() {
		return this.con.getMaxConnections();
	}

	@Override
	public boolean isRunning() {
		return this.con.getServerStatus();
	}
}
