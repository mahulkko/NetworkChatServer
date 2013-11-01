package UserManager.impl;

import java.util.concurrent.LinkedBlockingQueue;

import Connection.NetworkConnection.INetworkConnection;

public class UserWatch implements Runnable {

	private LinkedBlockingQueue<String> queue;
	
	public UserWatch(INetworkConnection connection) {
		this.queue = new LinkedBlockingQueue<String>();
		connection.startReceivingMessagesFromAllThreads(queue);
	}
	
	@Override
	public void run() {

	}

}
