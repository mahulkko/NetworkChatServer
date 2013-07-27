
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
import Connection.IConnection;

/**
 * Server managed the different connections from the clients
 * @author Martin Hulkkonen
 */

// TODO: make the SIZECLIENTS count with the arrays dynamically 
// -> Ensure Capacity for the array 
// TODO: In the run thread the message queue can be full so the next messages gone lost
// TODO: Add a logger

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
	private Receive receive[] = new Receive[SIZECLIENTS];
	
	/**
	 * Port where the Server is running on
	 */
	private int port;
	
	/**
	 * Boolean for the running state of the server
	 */
	private boolean isRunning;
	
	/**
	 * Set the port to the value and initialize all for the start
	 * <br>
	 * @param port - Set the port where the serve should run 
	 */
	public Connection(int port) {
		this.server = null;
		this.port = port;
		this.isRunning = false;
		initThreadArray();
	}
	
	private void initThreadArray() {
		// Set all to null
		for(int i = 0; i < SIZECLIENTS; i++) {
			thread[i] = null;
		}
	}
	
	private int manageConnection(Socket socket) {
		// Checks for a free place in the Array
		for(int i = 0; i < SIZECLIENTS; i++) {
			if(thread[i] == null) {
				// Start a new thread for the receive
				this.receive[i] = new Receive(socket, i);
				thread[i] = new Thread(this.receive[i]);
				thread[i].start();
				return i;
			}
		}
		return -1;
	}

	@Override
	public boolean startServer() {
		// If the server do not run
		if(!isRunning()){
			try {
				// Make a new connection
				this.server = new ServerSocket( this.port );
				this.isRunning = true;
				
				// Let the server work
				this.acceptThread = new Thread(new AcceptClients());
				this.acceptThread.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
		return false;
	}

	@Override
	public boolean stopServer() {
		// If the server is running
		if(isRunning()){
			try {
				// Close the connection
				this.isRunning = false;
				this.server.close();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
	@Override
	public void sendMessageToThreadID(int ThreadID, String msg) {
		// Send message to ThreadID
		if(this.clientConnected(ThreadID)) {
			this.receive[ThreadID].sendMessageToClient(msg);
		}
	}
	
	@Override
	public String getMessageFromThreadID(int ThreadID) {
		// Get message from thread
		if(this.clientConnected(ThreadID)) {
			return this.receive[ThreadID].getMessageFromClient();
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
			try {
				// Let the server work
				while(isRunning()) {
					// Accept new connections
					socket = server.accept();
					System.out.println("Verbindung auf ID: " + manageConnection(socket) + " angenommen");	
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// Inner class for the threads to receive messages
	private class Receive implements Runnable {

		private BufferedReader in;
		private PrintWriter out;
		private int ThreadID;
		private LinkedBlockingQueue<String> queue;
		
		public Receive(Socket socket, int ThreadID){
			try {
				// Initialize the Input and Output Stream
				this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				this.out = new PrintWriter(socket.getOutputStream(), true);
				this.queue = new LinkedBlockingQueue<String>();
				this.ThreadID = ThreadID;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public String getMessageFromClient() {
			return this.queue.poll();
		}
		
		public void sendMessageToClient(String msg){
			this.out.println(msg);
		}
		
		@Override
		public void run() {
			while(isRunning) {
				try {
					//TODO: If the queue is full all the next messages are lost
					this.queue.offer(this.in.readLine());
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			}
		System.out.println("Thread: " + this.ThreadID + " beendet");
		thread[this.ThreadID] = null;
		}
	}
}
