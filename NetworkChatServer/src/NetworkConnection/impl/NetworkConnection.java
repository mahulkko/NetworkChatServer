package NetworkConnection.impl;

import java.util.concurrent.LinkedBlockingQueue;

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

	@Override
	public boolean startReceivingMessagesFromThreadId(LinkedBlockingQueue<String> queue, int threadId) {
		return this.networkConnectionManager.startReceivingMessagesFromThreadId(queue, threadId);
	}

	@Override
	public boolean stopReceivingMessagesFromThreadId(LinkedBlockingQueue<String> queue, int threadId) {
		return this.networkConnectionManager.stopReceivingMessagesFromThreadId(queue, threadId);
	}

	@Override
	public boolean sendMessageToThreadId(String msg, int threadId) {
		return this.sendMessageToThreadId(msg, threadId);
	}
}
