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
	 * Close all running conections to the clients
	 * <br>
	 * @return On success it returns <b>true</b> on error it returns <b>false</b>
	 */
	public boolean stopServer();
	
	/**
	 * Returns the state of the server 
	 * @return Returns <b>true</b> if the server is running and <b>false</b> if the server ist stopped
	 */
	public boolean isRunning();
}