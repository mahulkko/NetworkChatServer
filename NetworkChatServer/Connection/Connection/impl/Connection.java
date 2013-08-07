
package Connection.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.log4j.Logger;
import Connection.IConnection;

//TODO Make for the thread classes new files and set all the vars to protected  

/**
 * Connection Class - Connection
 * <br>
 * Written by Martin Hulkkonen
 * <br><br>
 * <b><u>Connection</u></b>
 * <br>
 * Manages the connection between the different clients witch connect to it 
 * Server managed the different connections from the clients
 * @author Martin Hulkkonen
 */

public class Connection implements IConnection {

	/**
	 * Number of the Max Connections
	 */
	int SIZECLIENTS = 1; 
	
	/**
	 * ServerSocket for the connection
	 */
	private ServerSocket server;
	
	/**
	 * Thread Array to manage the Thread Slots
	 */
	private Thread thread[] = new Thread[SIZECLIENTS];
	
	/**
	 * Thread for the accept of clients
	 */
	private Thread acceptThread;
	
	/**
	 * Receive Array to manage the Functions of the Threads
	 */
	private Message message[] = new Message[SIZECLIENTS];
	
	/**
	 * Port where the Server is running on
	 */
	private int port;
	
	/**
	 * Boolean for the running state of the server
	 */
	private boolean isRunning;
	
	/**
	 * Logger for log4j Connection
	 */
	static Logger log = Logger.getLogger("Connection");
	
	/**
	 * Logger for log4j acceptClients
	 */
	static Logger logClients = Logger.getLogger("Connection.Clients");
	
	/**
	 * Logger for log4j Receive
	 */
	static Logger logMessage = Logger.getLogger("Connection.Message");
	
	/**
	 * Set the port to the value and initialize all for the start
	 * <br>
	 * @param port - Set the port where the serve should run 
	 */
	public Connection(int port) {
		
		// Init the server
		log.info("Initialize the Connection");
		
		this.server = null;
		this.port = port;
		this.isRunning = false;
		initThreadArray();
	}

	@Override
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
				
				// Let the server work
				log.info("Create and Start new Thread for Accept Clients");
				this.acceptThread = new Thread(new AcceptClients());
				this.acceptThread.start();
				
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
	
	@Override
	public void sendMessageToThreadID(int ThreadID, String msg) {
		// Send message to ThreadID
		if(this.clientConnected(ThreadID)) {
			log.info("Send Message to ThreadID: " + ThreadID);
			this.message[ThreadID].sendMessageToClient(msg);
		}
	}
	
	@Override
	public boolean startReceiveMessagesFromThreadID(int ThreadID, LinkedBlockingQueue<String> queue) {
		if(clientConnected(ThreadID)) {
			return this.message[ThreadID].startReceivingMessages(queue);
		}
		return false;
	}
	
	@Override
	public boolean stopReceiveMessagesFromThreadID(int ThreadID, LinkedBlockingQueue<String> queue) {
		if(clientConnected(ThreadID)) {
			return this.message[ThreadID].stopReceivingMessages(queue);
		}
		return false;
	}
	
	@Override
	public boolean clientConnected(int ThreadID) {
		// Proof if the client is connected
		if(ThreadID < this.SIZECLIENTS) {
			if(this.thread[ThreadID] == null) {
				return false;
			}
			return true;
		}
		log.info("ThreadID: " + ThreadID + " is out of range");
		return false;
	}
	
	@Override
	public int getMaxConnections() {
		return this.SIZECLIENTS;
	}

	@Override
	public boolean isRunning() {
		return this.isRunning;
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
	
	/**
	 * Manage the connection of the clients and look for a free place
	 * @param socket - Socket where the server is listen on it
	 * @return - returns the value of the place where the new thread is located
	 */
	private int manageConnection(Socket socket) {
		// Checks for a free place in the Array
		log.info("Checks for a free place in the Thread");
		
		for(int i = 0; i < SIZECLIENTS; i++) {
			if(thread[i] == null) {
				log.info("Found a free place at position: " + i);
				
				// Start a new thread for the receive
				log.info("Create and start a new Thread");
				
				this.message[i] = new Message(socket, i);
				thread[i] = new Thread(this.message[i]);
				thread[i].start();
				
				return i;
			}
		}
		log.warn("No free place found for a Thread!");
		ensureCapacity(SIZECLIENTS * 2);
		manageConnection(socket);
		return -1;
	}
	
	
	/**
	 * AcceptClients - Thread class for the accept of new clients
	 * @author Martin Hulkkonen
	 *
	 */
	private class AcceptClients implements Runnable {

		/**
		 * Waiting for new clients and accept them.
		 */
		@Override
		public void run() {
			Socket socket;
			
			logClients.info("AcceptClients Thread started");
			try {
				// Let the server work
				while(isRunning()) {
					// Accept new connections
					logClients.info("Wait for new Clients");
					socket = server.accept();
					logClients.info("New client accepted");
					manageConnection(socket);
				}
			} catch (IOException e) {
				logClients.error("Could not Accept a new client");
			}
		}
	}
	
	/**
	 * Message - Thread class for sending and receiving messages from clients
	 * @author Martin Hulkkonen
	 *
	 */
	private class Message implements Runnable {

		/**
		 * BufferedReder for the InputStream
		 */
		private BufferedReader in;
		
		/**
		 * PrintWriter for the OutputStream
		 */
		private PrintWriter out;
		
		/**
		 * ThreadID of the Thread
		 */
		private int ThreadID;
		
		/**
		 * Object for the synchronization
		 */
		private Object lock;
		
		/**
		 * Queue for saving messages from the clients
		 */
		private LinkedList<LinkedBlockingQueue<String>> queue;
		
		/**
		 * Socket from the connection to the client
		 */
		private Socket socket;
		
		/**
		 * Initialize the thread
		 * @param socket - Socket where the thread should communicate with
		 * @param ThreadID - ID where the thread is located on the thread array
		 */
		public Message(Socket socket, int ThreadID){
			try {
				// Initialize the Input and Output Stream
				logMessage.info("Initialize Receive Thread with ThreadID: " + ThreadID);
				
				this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				this.out = new PrintWriter(socket.getOutputStream(), true);
				this.queue = new LinkedList<LinkedBlockingQueue<String>>();
				this.lock = new Object();
				this.ThreadID = ThreadID;
				this.socket = socket;
				
			} catch (IOException e) {
				logMessage.error("Failure by initializing the Thread with ThreadID: " + ThreadID);
			}
		}
		
		/**
		 * Start to receive messages from these thread
		 * @param queue - Queue that were insert in the list
		 * @return When the queue is successfully insert it will returns <b>true</b> on error it returns <b>false</b>
		 */
		public boolean startReceivingMessages(LinkedBlockingQueue<String> queue) {
			synchronized(this.lock) {
				logMessage.info("Added queue to list");
				return this.queue.add(queue);
			}
		}
		
		/**
		 * Stop to receive messages from these thread
		 * @param queue - Queue that were deleted from the list
		 * @return When the queue is successfully deleted it will returns <b>true</b> on error it returns <b>false</b>
		 */
		public boolean stopReceivingMessages(LinkedBlockingQueue<String> queue) {
			synchronized(this.lock) {
				logMessage.info("Removed queue from list");
				return this.queue.remove(queue);
			}
		}
		
		/**
		 * Sends a message to the client
		 * @param msg - Message that send to the client
		 */
		public void sendMessageToClient(String msg){
			logMessage.info("Send Message to Client: " + msg);
			this.out.println(msg);
		}
		
		/**
		 * Thread function thats waiting for new messages from the client
		 */
		@Override
		public void run() {
			logMessage.info("Receive Thread with ThreadID: " + this.ThreadID + " started");
			while(isRunning) {
				try {
					String msg = this.in.readLine();
					logMessage.info("New Message arrived: " + msg + "(ThreadID:" + this.ThreadID + ")");
					
					// Add messages to the queues
					// If there is not enough space it will wait
					logMessage.info("Add message to the queues");
					synchronized(lock) {
						for(int i = 0; i < this.queue.size(); i++) {
							try {
								this.queue.get(i).put(msg);
							} catch (InterruptedException e) {
								logMessage.error("Cant put the message in the queue");
							}
						}		
					}			
				} catch (IOException e) {
					logMessage.error("Could not read the InputStream (ThreadID:" + this.ThreadID + ")");
					logMessage.info("Client disconnected");
					break;
				}
			}
		thread[this.ThreadID] = null;
		try {
			logMessage.info("Close connection from client (ThreadID:" + this.ThreadID + ")");
			this.socket.close();
		} catch (IOException e) {
			logMessage.error("Connection didnt closed (ThreadID:" + this.ThreadID + ")");
		}
		logMessage.info("Thread: " + this.ThreadID + " stopped");
		}
	}
}
