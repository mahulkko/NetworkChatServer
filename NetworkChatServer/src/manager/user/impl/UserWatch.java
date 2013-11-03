package manager.user.impl;

import java.util.concurrent.LinkedBlockingQueue;
import org.apache.log4j.Logger;

import connection.network.INetworkConnection;
import util.string.ISplitString;
import util.string.impl.SplitString;

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
    private static Logger log = Logger.getLogger("UserManager.UserWatch");

    /**
     * SpaceChar for splitting the strings
     */
    private static final char SPACER = 0x1e;
    
    /**
     * Constant for Number 3
     */
    private static final int THREE = 3;

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

    /**
     * Thread for looking of new connected user
     */
    @Override
    public void run() {
        log.info("UserWatch thread started");
        while (true) {
            try {
                String msg = this.queue.take();
                String splitMsg[] = this.splitString.splitStringByChar(msg, SPACER);
                try {
                    int command = Integer.parseInt(splitMsg[1]);
                    int threadId = Integer.parseInt(splitMsg[0]);

                    switch (command) {
                    case 0001:
                        if (splitMsg.length == THREE) {
                            this.userLookup.userConnected(splitMsg[2], threadId);
                        }
                        break;

                    case 0002:
                        this.userLookup.userDisconnected(threadId);
                        break;
                    }

                } catch (NumberFormatException e) {
                    log.error("Failed to read the command");
                }
                log.info(this.userLookup.whoIsOnline().toString());
            } catch (InterruptedException e) {
                log.error("Failed to take a message from the queue");
            }
        }
    }

}
