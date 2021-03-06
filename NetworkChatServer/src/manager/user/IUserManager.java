package manager.user;

import java.util.List;

/**
 * IUserManager
 * @author Martin Hulkkonen
 *
 */
public interface IUserManager {

    /**
     * Resolves the ThreadId to a user name
     * @param threadId - thread id that should resolved
     * @return Returns the resolved name on success and on error it returns null
     */
    String getName(int threadId);
    
    /**
     * Resolves a user name to his threads
     * @param name - User name where should resolve to his threads
     * @return Returns a LinkedList of all the thread id's
     */
    List<Integer> getThreadId(String name);
    
    /**
     * Shows all the people who are online at the moment
     * @return Returns a LinkedList of all online people
     */
    List<String> whoIsOnline();
    
}
