package UserManager.impl;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import Connection.NetworkConnection.INetworkConnection;
import UserManager.IUserManager;

/**
 * UserManager
 * @author Martin Hulkkonen
 *
 */
public class UserManager implements IUserManager {
	
	/**
	 * UserLookup for resolving all the id's
	 */
	private UserLookup lookup;
	
	/**
	 * UserWatch for watching of new connected user
	 */
	private UserWatch userWatch;
	
	/**
	 * Thread for the UserWatch
	 */
	private Thread thread;
	
	/**
	 * Logger for log4j UserManager
	 */
	static Logger log = Logger.getLogger("UserManager");

	/**
	 * UserManagement constructor
	 * @param connection - Connection to receive messages
	 */
	public UserManager(INetworkConnection connection) {
		log.info("Inizialise the UserManager");
		this.lookup = new UserLookup();
		log.info("Start the UserWatch Thread");
		this.userWatch = new UserWatch(connection, this.lookup);
		this.thread = new Thread(this.userWatch);
		this.thread.start();
	}

	@Override
	public String getName(int threadId) {
		return this.lookup.getName(threadId);
	}

	@Override
	public LinkedList<Integer> getThreadId(String name) {
		return this.lookup.getThreadId(name);
	}

	@Override
	public LinkedList<String> whoIsOnline() {
		return this.lookup.whoIsOnline();
	}
}
