package Connection.NetworkConnection.impl;

import java.util.concurrent.LinkedBlockingQueue;

import Connection.NetworkConnection.INetworkConnection;

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
	
	/**
	 * NetworkConnection
	 */
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
	public boolean startReceivingMessagesFromAllThreads(LinkedBlockingQueue<String> queue) {
		return this.networkConnectionManager.startReceivingMessagesFromAllThreads(queue);
	}

	@Override
	public boolean stopReceivingMessagesFromAllThreads(LinkedBlockingQueue<String> queue) {
		return this.networkConnectionManager.stopReceivingMessagesFromAllThreads(queue);
	}

	@Override
	public boolean sendMessageToThreadId(String msg, int threadId) {
		return this.sendMessageToThreadId(msg, threadId);
	}
}
