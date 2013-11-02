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
		char space = 0x1e;
		log.info("UserWatch thread started");
		while (true) {
			try {
				String msg = this.queue.take();
				String splitMsg[] = this.splitString.splitStringByChar(msg, space);
				
				int command = Integer.parseInt(splitMsg[1]);
				int threadId = Integer.parseInt(splitMsg[0]);
				
				switch (command) {
					case 0001:
						this.userLookup.userConnected(splitMsg[2], threadId);
						break;
						
					case 0002:
						this.userLookup.userDisconnected(threadId);
						break;
						
				}
				log.info(this.userLookup.whoIsOnline().toString());
			} catch (InterruptedException e) {
				log.error("Failed to take a message from the queue");
			}
		}
	}

}
