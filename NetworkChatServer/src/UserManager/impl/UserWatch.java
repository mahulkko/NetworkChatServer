package UserManager.impl;

import java.util.concurrent.LinkedBlockingQueue;

import Util.String.ISplitString;
import Util.String.impl.SplitString;
import Connection.NetworkConnection.INetworkConnection;

/**
 * UserWatch Thread
 * @author Martin Hulkkonen
 *
 */
public class UserWatch implements Runnable {

	/**
	 * Queue for receiving messages
	 */
	private LinkedBlockingQueue<String> queue;
	
	/**
	 * Userlookup for manage the user
	 */
	private UserLookup userLookup;
	
	/**
	 * SplitString into peaces
	 */
	private ISplitString splitString;
	
	/**
	 * UserWatch constructor
	 * @param connection - NetworkConnection to the clients
	 * @param lookup - UserLookup for manage the user
	 */
	public UserWatch(INetworkConnection connection, UserLookup lookup) {
		this.queue = new LinkedBlockingQueue<String>();
		connection.startReceivingMessagesFromAllThreads(queue);
		this.userLookup = lookup;
		this.splitString = new SplitString();
	}
	
	@Override
	public void run() {
		while (true) {
			// Implement here the protocol for new user.
		}
	}

}
