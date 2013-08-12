package NetworkConnection.impl;

import java.util.concurrent.LinkedBlockingQueue;
import NetworkConnection.INetworkConnection;

/**
 * NetworkConnection Class - NetworkConnection
 * <br>
 * Written by Martin Hulkkonen
 * <br><br>
 * <b><u>Connection</u></b>
 * <br>
 * Manages the connection between the different clients witch connect to it 
 * Server managed the different connections from the clients
 * @author Martin Hulkkonen
 */

public class NetworkConnection implements INetworkConnection {
	
	/**
	 * Connection to start a server
	 */
	private Connection con;
	
	/**
	 * Set the port to the value and initialize all for the start
	 * <br>
	 * @param port - Set the port where the serve should run 
	 */
	public NetworkConnection(int port) {
		this.con = new Connection(port);
	}

	@Override
	public boolean startServer() {
		return false;
	}

	@Override
	public boolean stopServer() {
		return false;
	}
	
	@Override
	public void sendMessageToThreadID(int ThreadID, String msg) {
	
	}
	
	@Override
	public boolean startReceiveMessagesFromThreadID(int ThreadID, LinkedBlockingQueue<String> queue) {
		return false;
	}
	
	@Override
	public boolean stopReceiveMessagesFromThreadID(int ThreadID, LinkedBlockingQueue<String> queue) {
		return false;
	}
	
	@Override
	public boolean startAutoReceiveFromAllThreads(LinkedBlockingQueue<String> queue) {
		return false;
	}
	
	@Override
	public boolean stopAutoReceiveFromAllThreads(LinkedBlockingQueue<String> queue) {
		return false;
	}
	
	@Override
	public boolean clientConnected(int ThreadID) {
		return false;
	}
	
	@Override
	public int getMaxConnections() {
		return 0;
	}

	@Override
	public boolean isRunning() {
		return false;
	}
}
