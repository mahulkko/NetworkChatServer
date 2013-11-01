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
	 * NetworkConnection to receive the messages
	 */
	private INetworkConnection connection;
	
	/**
	 * UserLookup for resolving all the id's
	 */
	private UserLookup lookup;

	/**
	 * UserManagement constructor
	 * @param connection - Connection to receive messages
	 */
	public UserManager(INetworkConnection connection) {
		this.connection = connection;
		this.lookup = new UserLookup();
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
