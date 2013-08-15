package NetworkConnection.impl;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

/**
 * ConnectionManagement Class for the NetworkConnection - Contains all the data
 * @author Martin Hulkkonen
 */
public class ConnectionManagement {
	
	/**
	 * Number of maximal connections
	 */
	private int SIZECLIENTS;
	
	/**
	 * Socket for the network connection
	 */
	private ServerSocket server;
	
	/**
	 * Port where the server is running on it
	 */
	private int port;
	
	/**
	 * Thread Array to manage the Thread Slots
	 */
	private Thread thread[];
	
	/**
	 * Receive Array to manage the functions of the threads
	 */
	private Communication communication[];
	
	/**
	 * Thread for the accept of clients
	 */
	private Thread acceptClient;
	
	/**
	 * Running state of the server
	 */
	private boolean isRunning;
	
	/**
	 * Object for the synchronization
	 */
	private Object lock;
	
	/**
	 * Logger for log4j connection
	 */
	static Logger log = Logger.getLogger("Connection.NetworkConnection.ConnectionManagement");
	
	/**
	 * Constructor of the ConnectionManagement
	 */
	public ConnectionManagement() {
		this.SIZECLIENTS = 1;
		this.server = null;
		this.port = -1;
		this.thread = new Thread[this.SIZECLIENTS];
		this.communication = new Communication[this.SIZECLIENTS];
		this.acceptClient = null;
		this.isRunning = false;
		this.initThreadArray();
	}
	
	/**
	 * Manage the connection of the clients and look for a free place
	 * @param socket - Socket where the server is listen on it
	 * @return - returns the value of the place where the new thread is located
	 */
	public int manageConnection(Socket socket) {
		// Checks for a free place in the Array
		log.info("Checks for a free place in the Thread");
		
		for(int i = 0; i < SIZECLIENTS; i++) {
			if(thread[i] == null) {
				log.info("Found a free place at position: " + i);
				
				// Start a new thread for the receive
				log.info("Create and start a new Thread");
				
				this.communication[i] = new Communication(socket, i, this);
				thread[i] = new Thread(this.communication[i]);
				thread[i].start();
				
				return i;
			}
		}
		log.warn("No free place found for a Thread!");
		ensureCapacity(SIZECLIENTS * 2);
		manageConnection(socket);
		return -1;
	}
	
	public boolean startReceivingMessagesFromThreadID(LinkedBlockingQueue<String> queue, int ThreadID) {
		synchronized(this.lock) {
			if(this.checkThreadSize(ThreadID)) {
				return this.communication[ThreadID].startReceivingMessages(queue);
			}
			return false;
		}
	}
	
	public boolean stopReceivingMessagesFromThreadID(LinkedBlockingQueue<String> queue, int ThreadID) {
		synchronized(this.lock) {
			if(this.checkThreadSize(ThreadID)) {
				return this.communication[ThreadID].stopReceivingMessages(queue);
			}
			return false;
		}
	}
	
	public void sendMessageToThreadID(String msg, int ThreadID){
		synchronized(this.lock) {
			if(this.checkThreadSize(ThreadID)) {
				this.communication[ThreadID].sendMessageToClient(msg);
			}			
		}
	}
	
	ServerSocket getSocket() {
		return this.server;
	}
	
	boolean setSocket(ServerSocket server) {
		if(server != null) { 
			this.server = server; 
			return true;
		}
		return false;
	}
	
	int getPort() {
		return this.port;
	}
	
	boolean setPort(int port) {
		if(port > 0) {
			this.port = port;
			return true;
		}
		return false;
	}
	
	boolean setThreadToNull(int ThreadID) {
		if(this.checkThreadSize(ThreadID)) {
			this.thread[ThreadID] = null;
			return true;
		}
		return false;
	}
	
	Thread getAcceptThread() {
		return this.acceptClient;
	}
	
	boolean setAcceptThread(Thread thread) {
		if(thread != null) {
			this.acceptClient = thread;
			return true;
		}
		return false;
	}
	
	void setServerStatus(boolean state) {
		this.isRunning = state;
	}
	
	boolean getServerStatus() {
		return this.isRunning;
	}
	
	int getMaxConnections() {
		return this.SIZECLIENTS;
	}
	
	private boolean checkThreadSize(int size) {
		if(this.SIZECLIENTS > size)
			return true;
		return false;
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
		Thread[] old = this.thread;
		log.info("create new thread array");
		this.thread = new Thread[newCapacity];
		initThreadArray();
		log.info("copy old thread array to the new array");
		System.arraycopy(old, 0, thread, 0, SIZECLIENTS);
		
		// Make the message thread bigger
		log.info("increase the space of the message array");
		Communication[] old2 = this.communication;
		log.info("create new message array");
		this.communication = new Communication[newCapacity];
		log.info("copy old message array to the new array");
		System.arraycopy(old2, 0, this.communication, 0, SIZECLIENTS);
		
		// Set new capacity
		log.info("set the capacity to the new capacity");
		SIZECLIENTS = newCapacity;
	} 
}
