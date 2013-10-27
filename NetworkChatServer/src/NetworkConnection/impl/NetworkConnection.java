package NetworkConnection.impl;

import NetworkConnection.INetworkConnection;

/**
 * 
 * @author Martin Hulkkonen
 *
 */
public class NetworkConnection implements INetworkConnection {

	/**
	 * NetworkConnectionManager - Manage the connection
	 */
	private NetworkConnectionManager networkConnectionManager;
	
	public NetworkConnection() {
		this.networkConnectionManager = new NetworkConnectionManager();
	}

	@Override
	public boolean startServer(int port) {
		return this.networkConnectionManager.startServer(port);
	}

	@Override
	public boolean stopServer() {
		return this.networkConnectionManager.stopServer();
	}
}
