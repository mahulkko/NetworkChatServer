package networkchatserver;

import org.apache.log4j.BasicConfigurator;

import connection.network.INetworkConnection;
import connection.network.impl.NetworkConnection;
import manager.user.IUserManager;
import manager.user.impl.UserManager;

/**
 * 
 * @author Martin Hulkkonen
 *
 */
public final class NetworkChatServer {
    
    /**
     * NetworkChatServer
     */
    private NetworkChatServer() {
        // Nothing in here
    }
    
    /**
     * Constant for Number 3
     */
    private static final int PORT = 12345;

    /**
     * Main function
     * @param args - not used here
     */    
    public static void main(String[] args) {
        /**
         * Logger
         */
        BasicConfigurator.configure();
        
        /**
         * NetworkConnection
         */
        INetworkConnection connection = new NetworkConnection();
        connection.startServer(PORT);
        
        /**
         * UserManager
         */
        @SuppressWarnings("unused")
        IUserManager userManager = new UserManager(connection);
    }
}
