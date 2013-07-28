
/* Connection Class - Connection
 * Written by Martin Hulkkonen
 * 
 * Connection 
 * ==========
 * Manages the connection between the different clients witch connect to it 
 * 
 */

package Connection.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import Connection.IConnection;

/**
 * Server managed the different connections from the clients
 * @author Martin Hulkkonen
 */

// TODO: make the SIZECLIENTS count with the arrays dynamically 
// -> Ensure Capacity for the array 

public class Connection implements IConnection {

	/**
	 * Number of the Max Connections
	 */
	int SIZECLIENTS = 100; 
	
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
		
		// Configure the logger with Basic
		BasicConfigurator.configure();
		
		// Init the server
		log.info("Initialize the Connection");
		
		this.server = null;
		this.port = port;
		this.isRunning = false;
		initThreadArray();
	}
	
	private void initThreadArray() {
		// Set all to null
		log.info("Set all Threads to null");
		
		for(int i = 0; i < SIZECLIENTS; i++) {
			thread[i] = null;
		}
	}
	
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
		return -1;
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
	public String getMessageFromThreadID(int ThreadID) {
		// Get message from thread
		if(this.clientConnected(ThreadID)) {
			log.info("Get Message from ThreadID: " + ThreadID);
			return this.message[ThreadID].getMessageFromClient();
		}
		return null;
	}
	
	public String getMessageFromThreadIDBlocked(int ThreadID) {
		// Get message from thread
		if(this.clientConnected(ThreadID)) {
			log.info("Get Message from ThreadID: " + ThreadID);
			return this.message[ThreadID].getMessageFromClientBlocked();
		}
		return null;
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
	
	// Inner class for the accept of the clients
	private class AcceptClients implements Runnable {

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
	
	// Inner class for the threads to receive messages
	private class Message implements Runnable {

		private BufferedReader in;
		private PrintWriter out;
		private int ThreadID;
		private LinkedBlockingQueue<String> queue;
		private Socket socket;
		
		public Message(Socket socket, int ThreadID){
			try {
				// Initialize the Input and Output Stream
				logMessage.info("Initialize Receive Thread with ThreadID: " + ThreadID);
				
				this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				this.out = new PrintWriter(socket.getOutputStream(), true);
				this.queue = new LinkedBlockingQueue<String>();
				this.ThreadID = ThreadID;
				this.socket = socket;
				
			} catch (IOException e) {
				logMessage.error("Failure by initializing the Thread with ThreadID: " + ThreadID);
			}
		}
		
		public String getMessageFromClient() {
			String msg = this.queue.poll();
			logMessage.info("Received Message from Client: " + msg);
			return msg;
		}
		
		public String getMessageFromClientBlocked() {
			String msg;
			try {
				msg = this.queue.take();
				logMessage.info("Received Message from Client: " + msg);
				return msg;
			} catch (InterruptedException e) {
				logMessage.error("Cant get a Message from Client!");
			}
			return null;
		}
		
		public void sendMessageToClient(String msg){
			logMessage.info("Send Message to Client: " + msg);
			this.out.println(msg);
		}
		
		@Override
		public void run() {
			logMessage.info("Receive Thread with ThreadID: " + this.ThreadID + " started");
			while(isRunning) {
				try {
					String msg = this.in.readLine();
					this.queue.offer(msg);
					logMessage.info("New Message arrived: " + msg + "(ThreadID:" + this.ThreadID + ")");
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
