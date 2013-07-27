/*
 * Connection Interface - IConnection
 * Written by Martin Hulkkonen
 */

package Connection;

/**
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
	 * Get a message from the Client with the specific ThreadID
	 * @param ThreadID - Selects the Client where the message should read
	 * <br>
	 * @return On success it returns the <b>String</b> with the Value on error or if there is no message it returns <b>null</b>
	 */
	public String getMessageFromThreadID(int ThreadID);
	
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