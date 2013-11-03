package userManager.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

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
     * Logger for log4j UserLookup
     */
    private static Logger log = Logger.getLogger("UserManager.UserLookup");
    
    /**
     * UserLookup constructor
     */
    public UserLookup() {
        log.info("Inizialize the HashMaps");
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
            log.info("User \"" + name + "\" already connected - Added a new ThreadId \"" + threadId + "\" to the connected user");
            this.nameLookup.get(name).add(threadId);
        } else {
            LinkedList<Integer> list = new LinkedList<Integer>();
            list.add(threadId);
            this.nameLookup.put(name, list);
            log.info("User \"" + name + "\" has no connection at this moment - Create a new entry");
        }
        log.info("Make a entry for resolving the ThreadId \"" + threadId + "\" to name \"" + name +  "\"");
        this.threadLookup.put(threadId, name);
    }
    
    /**
     * When a connection is closed
     * @param threadId - Thread id of the closed connection
     */
    public void userDisconnected(int threadId) {
        if (this.threadLookup.containsKey(threadId)) {
            String name = this.threadLookup.remove(Integer.valueOf(threadId));
            log.info("User \"" + name + "\" closed the connection with threadId \"" + threadId + "\" - remove the entry");
            this.nameLookup.get(name).remove(Integer.valueOf(threadId));
            log.info("Proof if user \"" + name + "\" is still connected");
            if (this.nameLookup.get(name).size() == 0) {
                log.info("User \"" + name + "\" is no more online - remove all and clean up");
                this.nameLookup.remove(name);
            } else {
                log.info("User \"" + name + "\" is online");
            }
        } else {
            log.info("User with threadId \"" + threadId + "\" already disconnected");
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
    public List<Integer> getThreadId(String name) {
        return this.nameLookup.get(name);
    }
    
    /**
     * Shows all the people who are online at the moment
     * @return Returns a LinkedList of all online people
     */
    public List<String> whoIsOnline() {
        LinkedList<String> list = new LinkedList<String>();
        for(Iterator<String> it = this.nameLookup.keySet().iterator(); it.hasNext();) {
            list.add(it.next());
        }
        return list;
    }
}
