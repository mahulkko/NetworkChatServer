package NetworkConnection.impl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

/**
 * Connection Class for the NetworkConnection - Makes the connection to the clients
 * @author Martin Hulkkonen
 */
public class Connection {
	
	/**
	 * Number of the Max Connections
	 */
	private int SIZECLIENTS = 1; 
	
	/**
	 * ServerSocket for the connection
	 */
	private ServerSocket server;
	
	/**
	 * Port where the Server is running on
	 */
	private int port;
	
	/**
	 * Boolean for the running state of the server
	 */
	private boolean isRunning;
	
	/**
	 * Thread Array to manage the Thread Slots
	 */
	private Thread thread[] = new Thread[SIZECLIENTS];
	
	/**
	 * Receive Array to manage the functions of the threads
	 */
	private Message message[] = new Message[SIZECLIENTS];
	
	/**
	 * Logger for log4j connection
	 */
	static Logger log = Logger.getLogger("Connection.NetworkConnection.Connection");
	
	/**
	 * Initialize the Server for the NetworkConnection
	 * @param port - Port where the Server is listen on it
	 */
	public Connection(int port) {
		// Init the server
		log.info("Initialize the Connection");
		this.server = null;
		this.port = port;
		this.isRunning = false;
		initThreadArray();
	}

	/**
	 * Starts the server for the connection
	 * @return On success it returns <b>true</b> on failure <b>false</b>
	 */
	public boolean startServer() {
		// If the server do not run
		log.info("Try to start the server");
		
		if(!isRunning()){
			try {
				// Make a new connection
				log.info("Create new Socket for the connection");
				
				this.server = new ServerSocket( this.port );
				this.isRunning = true;
				
				log.info("Server is startet on Port: " + this.port);
				
				return true;
			} catch (IOException e) {
				log.error("Could not start the server");
			}
		}	
		log.info("Server is already running");
		return false;
	}

	/**
	 * Stops the server for the connection
	 * @return On success it returns <b>true</b> on failure <b>false</b>
	 */
	public boolean stopServer() {
		// If the server is running
		log.info("Try to stop the Server");
		
		if(isRunning()){
			try {
				// Close the connection
				this.isRunning = false;
				this.server.close();
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
	
	public boolean startNewMessageThread(Socket socket) {
		// Start a new thread for the receive
		int ThreadPlace = findFreePlace();
		if(thread[ThreadPlace] == null) {
			log.info("Create and start a new Thread on place:" + ThreadPlace);
			this.message[ThreadPlace] = new Message(socket, ThreadPlace, this);
			thread[ThreadPlace] = new Thread(this.message[ThreadPlace]);
			thread[ThreadPlace].start();
			return true;
		}
		log.info("ThreadPlace: " + ThreadPlace + " has already a running thread!");
		return false;		
	}
	
	/**
	 * Shows the state of the Server
	 * @return Returns the running state of the server
	 */
	public boolean isRunning() {
		return this.isRunning;
	}
	
	/**
	 * Server Socket connection
	 * @return returns the ServerSocket of the connection
	 */
	public ServerSocket getSocket() {
		return this.server;
	}
	
	/**
	 * Set the Thread to null
	 * @param ThreadID - Thread Place where set to null
	 * @return On success it returns <b>true</b> on failure <b>false</b>
	 */
	public boolean setThreadToNull(int ThreadID) {
		if (this.SIZECLIENTS > ThreadID) {
			this.thread[ThreadID] = null;
			return true;
		}
		return false;
	}
	
	/**
	 * Looks for a free place in the array for the new thread
	 * @return - returns the value of the place where the new thread is located
	 */
	private int findFreePlace() {
		// Checks for a free place in the Array
		log.info("Checks for a free place in the Thread");
		for(int i = 0; i < SIZECLIENTS; i++) {
			if(thread[i] == null) {
				log.info("Found a free place at position: " + i);
				return i;
			}
		}
		log.warn("No free place found for a Thread!");
		ensureCapacity(SIZECLIENTS * 2);
		findFreePlace();
		return -1;
	}
	
	/**
	 * Sets all fields on the thread array to null
	 */
	private void initThreadArray() {
		// Set all to null
		log.info("Set all Threads to null");
		
		for(int i = 0; i < SIZECLIENTS; i++) {
			thread[i] = null;
		}
	}
	
	/**
	 * increase the place of the message and thread array if there is no space
	 * @param newCapacity - Size of the new arrays
	 */
	private void ensureCapacity(int newCapacity) {
		// The new capacity must bigger then the old capacity
		log.info("ensureCapacity - make the thread/message array bigger");
		if(newCapacity < SIZECLIENTS) {
			log.error("New capacity is less then the old capayity - no change was made");
			return;
		}
		
		// Make the thread array bigger
		log.info("increase the space of the thread array");
		Thread[] old = thread;
		log.info("create new thread array");
		thread = new Thread[newCapacity];
		initThreadArray();
		log.info("copy old thread array to the new array");
		System.arraycopy(old, 0, thread, 0, SIZECLIENTS);
		
		// Make the message thread bigger
		log.info("increase the space of the message array");
		Message[] old2 = message;
		log.info("create new message array");
		message = new Message[newCapacity];
		log.info("copy old message array to the new array");
		System.arraycopy(old2, 0, message, 0, SIZECLIENTS);
		
		// Set new capacity
		log.info("set the capacity to the new capacity");
		SIZECLIENTS = newCapacity;
	} 
}
