package manager.user.impl;

import java.util.List;
import org.apache.log4j.Logger;

import connection.network.INetworkConnection;
import manager.user.IUserManager;

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
     * Logger for log4j UserManager
     */
    private static Logger log = Logger.getLogger("UserManager");

    /**
     * UserManagement constructor
     * @param connection - Connection to receive messages
     */
    public UserManager(INetworkConnection connection) {
        log.info("Inizialise the UserManager");
        this.lookup = new UserLookup();
        log.info("Start the UserWatch Thread");
        UserWatch userWatch = new UserWatch(connection, this.lookup);
        Thread thread = new Thread(userWatch);
        thread.start();
    }

    /**
     * Resolves the ThreadId to a user name
     * @param threadId - thread id that should resolved
     * @return Returns the resolved name on success and on error it returns null
     */
    @Override
    public String getName(int threadId) {
        return this.lookup.getName(threadId);
    }

    /**
     * Resolves a user name to his threads
     * @param name - User name where should resolve to his threads
     * @return Returns a LinkedList of all the thread id's
     */
    @Override
    public List<Integer> getThreadId(String name) {
        return this.lookup.getThreadId(name);
    }

    /**
     * Shows all the people who are online at the moment
     * @return Returns a LinkedList of all online people
     */
    @Override
    public List<String> whoIsOnline() {
        return this.lookup.whoIsOnline();
    }
}
