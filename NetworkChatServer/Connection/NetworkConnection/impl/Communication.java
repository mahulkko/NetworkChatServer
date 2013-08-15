package NetworkConnection.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

/**
 * Communication - Thread class for sending and receiving messages from clients
 * @author Martin Hulkkonen
 *
 */
public class Communication implements Runnable {
	
	/**
	 * Class for the Connection
	 */
	private ConnectionManagement con;
	
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
	 * Logger for log4j connection
	 */
	static Logger log = Logger.getLogger("Connection.NetworkConnection.Communication");
	
	/**
	 * Initialize the thread
	 * @param socket - Socket where the thread should communicate with
	 * @param ThreadID - ID where the thread is located on the thread array
	 * @param con - ConnectionManagement Class
	 */
	public Communication(Socket socket, int ThreadID, ConnectionManagement con){
		try {
			// Initialize the Input and Output Stream
			log.info("Initialize Receive Thread with ThreadID: " + ThreadID);
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.out = new PrintWriter(socket.getOutputStream(), true);
			this.queue = new LinkedList<LinkedBlockingQueue<String>>();
			this.lock = new Object();
			this.ThreadID = ThreadID;
			this.socket = socket;
			
		} catch (IOException e) {
			log.error("Failure by initializing the Thread with ThreadID: " + ThreadID);
		}
	}
	
	/**
	 * Start to receive messages from these thread
	 * @param queue - Queue that were insert in the list
	 * @return When the queue is successfully insert it will returns <b>true</b> on error it returns <b>false</b>
	 */
	public boolean startReceivingMessages(LinkedBlockingQueue<String> queue) {
		synchronized(this.lock) {
			log.info("Added queue to list");
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
			log.info("Removed queue from list");
			return this.queue.remove(queue);
		}
	}
	
	/**
	 * Sends a message to the client
	 * @param msg - Message that send to the client
	 */
	public void sendMessageToClient(String msg){
		log.info("Send Message to Client: " + msg);
		this.out.println(msg);
	}
	
	/**
	 * Thread function thats waiting for new messages from the client
	 */
	@Override
	public void run() {
		log.info("Receive Thread with ThreadID: " + this.ThreadID + " started");
		while(this.con.getServerStatus()) {
			try {
				String msg = this.in.readLine();
				log.info("New Message arrived: " + msg + "(ThreadID:" + this.ThreadID + ")");
				
				// Add messages to the queues
				// If there is not enough space it will wait
				log.info("Add message to the queues");
				synchronized(lock) {
					for(int i = 0; i < this.queue.size(); i++) {
						try {
							this.queue.get(i).put(msg);
						} catch (InterruptedException e) {
							log.error("Cant put the message in the queue");
						}
					}		
				}			
			} catch (IOException e) {
				log.error("Could not read the InputStream (ThreadID:" + this.ThreadID + ")");
				log.info("Client disconnected");
				break;
			}
		}
	this.con.setThreadToNull(this.ThreadID);
	try {
		log.info("Close connection from client (ThreadID:" + this.ThreadID + ")");
		this.socket.close();
	} catch (IOException e) {
		log.error("Connection didnt closed (ThreadID:" + this.ThreadID + ")");
	}
	log.info("Thread: " + this.ThreadID + " stopped");
	}
}
