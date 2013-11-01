package UserManager.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * User Lookup
 * @author Martin Hulkkonen
 *
 */
public class UserLookup {

	/**
	 * Map for resolving name to the thread id's
	 */
	private Map<String,LinkedList<Integer>> nameLookup;
	
	/**
	 * Map for resolving thread id to name
	 */
	private Map<Integer,String> threadLookup;
	
	/**
	 * UserLookup constructor
	 */
	public UserLookup() {
		this.nameLookup = new HashMap<String, LinkedList<Integer>>();
		this.threadLookup = new HashMap<Integer,String>();
	}
	
	/**
	 * Registered here a new user who comes online
	 * @param name - Name of the user
	 * @param threadId - thread id of the user
	 */
	public void userConnected(String name, int threadId) {
		if (this.nameLookup.containsKey(name)) {
			this.nameLookup.get(name).add(threadId);
		} else {
			LinkedList<Integer> list = new LinkedList<Integer>();
			list.add(threadId);
			this.nameLookup.put(name, list);
			this.nameLookup.get(name).add(threadId);
		}
		this.threadLookup.put(threadId, name);
	}
	
	/**
	 * When a connection is closed
	 * @param threadId - Thread id of the closed connection
	 */
	public void userDisconnected(int threadId) {
		String name = this.threadLookup.remove(threadId);
		this.nameLookup.get(name).remove(threadId);
		
		if (this.nameLookup.get(name).size() == 0) {
			this.nameLookup.remove(name);
		}		
	}
	
	/**
	 * Resolves the ThreadId to a user name
	 * @param threadId - thread id that should resolved
	 * @return Returns the resolved name on success and on error it returns null
	 */
	public String getName(int threadId) {
		return this.threadLookup.get(threadId);
	}
	
	/**
	 * Resolves a user name to his threads
	 * @param name - User name where should resolve to his threads
	 * @return Returns a LinkedList of all the thread id's
	 */
	public LinkedList<Integer> getThreadId(String name) {
		return this.nameLookup.get(name);
	}
	
	/**
	 * Shows all the people who are online at the moment
	 * @return Returns a LinkedList of all online people
	 */
	public LinkedList<String> whoIsOnline() {
		LinkedList<String> list = new LinkedList<String>();
		for(Iterator<String> it = this.nameLookup.keySet().iterator(); it.hasNext();) {
			list.add(it.next());
		}
		return list;
	}
}
