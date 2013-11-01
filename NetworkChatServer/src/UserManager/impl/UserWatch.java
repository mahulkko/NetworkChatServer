package UserManager.impl;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

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
	 * UserLookup for manage the user
	 */
	private UserLookup userLookup;
	
	/**
	 * SplitString into peaces
	 */
	private ISplitString splitString;
	
	/**
	 * Logger for log4j UserWatch
	 */
	static Logger log = Logger.getLogger("UserManager.UserWatch");
	
	/**
	 * UserWatch constructor
	 * @param connection - NetworkConnection to the clients
	 * @param lookup - UserLookup for manage the user
	 */
	public UserWatch(INetworkConnection connection, UserLookup lookup) {
		log.info("Inizialize the UserWatch thread");
		this.queue = new LinkedBlockingQueue<String>();
		connection.startReceivingMessagesFromAllThreads(queue);
		this.userLookup = lookup;
		this.splitString = new SplitString();
	}
	
	@Override
	public void run() {
		log.info("UserWatch thread started");
		while (true) {
			// Implement here the protocol for new user.
		}
	}

}
