package UserManager.impl;

import java.util.concurrent.LinkedBlockingQueue;

import Connection.NetworkConnection.INetworkConnection;
import UserManager.IUserManager;

public class UserManager implements IUserManager {
	
	private INetworkConnection connection;
	private LinkedBlockingQueue<String> queue;

	public UserManager(INetworkConnection connection) {
		this.connection = connection;
		this.queue = new LinkedBlockingQueue<String>();
		this.connection.startReceivingMessagesFromAllThreads(queue);
	}
}
