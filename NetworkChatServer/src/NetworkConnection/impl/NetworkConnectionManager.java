package NetworkConnection.impl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

/**
 * 
 * @author Martin Hulkkonen
 *
 */
public class NetworkConnectionManager {
	
	/**
	 * ServerSocket where the server runs
	 */
	private ServerSocket server;
	
	/**
	 * Port where the server is listen on it
	 */
	private int port;
	
	/**
	 * State of the server
	 */
	private boolean isRunning;
	
	/**
	 * Look variable for synchronization
	 */
	private Object lock;
	
	/**
	 * Number of the maximal clients for the server
	 */
	private int SIZECLIENTS = 1;
	
	/**
	 * ThreadArray for the handling of the communication
	 */
	private Thread thread[] = new Thread[SIZECLIENTS];
	
	/**
	 * ConnectionArray for the connection of each client
	 */
	private Connection connection[] = new Connection[SIZECLIENTS];
	
	/**
	 * Thread for accept clients
	 */
	private Thread acceptClients;
	
	/**
	 * Logger for log4j connection
	 */
	static Logger log = Logger.getLogger("NetworkConnection.NetworkConnectionManager");
	
	/**
	 * NetworkConnectionManager
	 * Manage the connection for each connected client
	 */
	public NetworkConnectionManager() {
		log.info("Initialize the network connection");
		this.server = null;
		this.port = -1;
		this.isRunning = false;
		this.lock = new Object();		
	}
	
	/**
	 * 
	 * @param port - port where the server is listen on it
	 * @return Returns <b>true</b> if the server is starting correctly and <b>false</b> on error
	 */
	public boolean startServer(int port) {
		log.info("Try to start the server");
		synchronized (this.lock) {
			if (!isServerRunning()) {
				try {
					log.info("Create new socket for the connection");
					this.server = new ServerSocket(port);
					log.info("Server ist started on port: " + port);
					
					log.info("Try to start a new thread for accepting clients");
					this.acceptClients = new Thread(new AcceptClients(this));
					this.acceptClients.start();
					
					this.port = port;
					this.isRunning = true;
					
					return true;
				} catch (IOException e) {
					log.error("Could not start the server!");
					return false;
				}
			}
			log.info("Server is already running");
			return false;
		}
	}
	
	/**
	 * 
	 * @return Returns <b>true</b> if the server is stopping correctly and <b>false</b> on error
	 */
	public boolean stopServer() {
		log.info("Try to stop the server");
		synchronized (this.lock) {
			if (isServerRunning()) {
				try {
					log.info("Close the server socket");
					this.server.close();
					this.isRunning = false;
					this.server = null;
					this.port = -1;
					log.info("Server successfully closed");
					return true;
				} catch (IOException e) {
					log.error("Could not stop the server!");
					return false;
				}
			}
			log.info("Server didnt running");
			return false;
		}
	}
	
	/**
	 * 
	 * @param socket - Socket of the new accepted client
	 * @return Returns the ThreadId of the accepted thread
	 */
	public int manageNewConnection(Socket socket) {
		log.info("Search a place for the client");
		for (int i = 0; i < SIZECLIENTS; i++) {
			if (thread[i] == null) {
				log.info("Found a place for the client");
				log.info("Create a new connection thread - ThreadId: " + i);
				this.connection[i] = new Connection(socket, i);
				thread[i] = new Thread(this.connection[i]);
				log.info("Start the connection thread - ThreadId: " +i);
				thread[i].start();
				return i;
			}
		}
		log.info("No place found for the client");
		ensureCapacity(SIZECLIENTS * 2);
		manageNewConnection(socket);
		return -1;
	}
	
	/**
	 * 
	 * @return Returns the used server socket
	 */
	public ServerSocket getServerSocket() {
		return this.server;
	}
	
	/**
	 * 
	 * @return Returns the port where the server is listen on it
	 */
	public int getPort() {
		return this.port;
	}
	
	/**
	 * 
	 * @return Returns the state of the server: <b>true</b> running / <b>false</b> stopped
	 */
	public boolean isServerRunning() {
		return this.isRunning;
	}
	
	/**
	 * Ensure the capacity of the maximal number of clients for the server
	 */
	private void ensureCapacity(int newCapacity) {
		if (newCapacity < SIZECLIENTS) {
			log.error("Old capacity is bigger than the new one");
			return;
		}
		log.info("Make the thread array bigger");
		Thread[] old = thread;
		thread = new Thread[newCapacity];
		initializeThreadArray();
		System.arraycopy(old, 0, thread, 0, SIZECLIENTS);
		
		log.info("Make the connection array bigger");
		Connection[] old2 = connection;
		connection = new Connection[newCapacity];
		System.arraycopy(old2, 0, connection, 0, SIZECLIENTS);
		log.info("Arrays have now a new capayity of " + newCapacity + " clients");
		SIZECLIENTS = newCapacity;
	} 
	
	/**
	 * initialize the thread array with standard values
	 */
	private void initializeThreadArray() {
		log.info("Inizialite the thread array new");
		for (int i = 0; i < SIZECLIENTS; i++) {
			thread[i] = null;
		}
	}
}

