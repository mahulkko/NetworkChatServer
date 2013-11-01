package UserManager.impl;

import java.util.LinkedList;
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
	 * UserManagement constructor
	 * @param connection - Connection to receive messages
	 */
	public UserManager(INetworkConnection connection) {
		this.lookup = new UserLookup();
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
