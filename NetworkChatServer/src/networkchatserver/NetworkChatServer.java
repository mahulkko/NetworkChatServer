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
public class NetworkChatServer {
    
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
        IUserManager userManager = new UserManager(connection);
        /*
        while (true) {
            try {
                System.out.println(queue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        */
        
        /*
        char space = 0x1e;
        String test = "Hallo ^^ " + space + "wie gehts es " + space + "dir " + space + "?";
        System.out.println(test);
        SplitString string = new SplitString();
        String[] splitt = string.splitStringByChar(test, space);
        
        for(int i = 0; i < splitt.length; i++) {
            System.out.println(splitt[i]);
        }
        */
    }
}
