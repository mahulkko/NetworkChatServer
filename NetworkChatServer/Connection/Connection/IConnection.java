package Connection;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Connection Interface - IConnection
 * <br>
 * Written by Martin Hulkkonen
 * <br>
 * Server managed the different connections from the clients
 * @author Martin Hulkkonen
 */
public interface IConnection {
	
	/**
	 * Starts the server and waits for connections
	 * @return On success it returns <b>true</b> on error it returns <b>false</b>
	 * 
	 */
	public boolean startServer();
		
	/**
	 * Close all running connections to the clients
	 * <br>
	 * @return On success it returns <b>true</b> on error it returns <b>false</b>
	 */
	public boolean stopServer();
	
	/**
	 * Sends a message to the Client with the specific ThreadID
	 * @param ThreadID - Selects the Client where the message where send to
	 * @param msg - Contains the message where to send
	 */
	public void sendMessageToThreadID(int ThreadID, String msg);
	
	/**
	 * Insert a new LinkedBlockingQueue for receiving messages
	 * @param ThreadID - ThreadID were where the messages come from
	 * @param queue - Queue were the messages from the server are saved
	 * @return When the queue is successfully insert it will returns <b>true</b> on error it returns <b>false</b>
	 * <br>
	 * <b><u>Note:</u></b>
	 * <br>
	 * You can use only one LinkedBlockingDeque for receiving messages from different threads
	 */
	public boolean startReceiveMessagesFromThreadID(int ThreadID, LinkedBlockingQueue<String> queue);
	
	/**
	 * Get the queue to stop receiving messages to it
	 * @param ThreadID - ThreadID where the messages come from
	 * @param queue - Queue where should stop getting messages
	 * @return When the queue is successfully deleted it will returns <b>true</b> on error it returns <b>false</b>
	 * <br>
	 * <b><u>Note:</u></b>
	 * <br>
	 * You can use only one LinkedBlockingDeque for receiving messages from different threads
	 */
	public boolean stopReceiveMessagesFromThreadID(int ThreadID, LinkedBlockingQueue<String> queue);
	
	/**
	 * Get the status of the Client Connection
	 * @param ThreadID - Selects the Client where you want get the status
	 * <br>
	 * @return Is the client connected to the server it will return <b>true</b> and on no connection it will return <b>false</b>
	 */
	public boolean clientConnected(int ThreadID);
	
	/**
	 * Get the Max Connection value of the clients
	 * @return It returns the Number of Max Connection 
	 */
	public int getMaxConnections();
	
	/**
	 * Returns the state of the server 
	 * @return Returns <b>true</b> if the server is running and <b>false</b> if the server is stopped
	 */
	public boolean isRunning();
}