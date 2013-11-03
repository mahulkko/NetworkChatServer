package connection.network;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 
 * @author Martin Hulkkonen
 * <br>
 * <b>INetworkConnection</b> 
 *
 */
public interface INetworkConnection {

    /**
     * Starts the server on the given port
     * @param port - Port where the server is listen on it.
     * @return Returns <b>true</b> if the server is starting correctly and <b>false</b> on error
     */
    boolean startServer(int port);
    
    /**
     * Stops the running server
     * @return Returns <b>true</b> if the server is stopping correctly and <b>false</b> on error
     */
    boolean stopServer();
    
    /**
     * Starts receiving messages from a special single thread
     * @param queue - Queue where the messages where saved
     * @param threadId - Id of the thread where receive the messages
     * @return Returns <b>true</b> if the Queue is successful added and returns <b>false</b> when something gone wrong
     */
    boolean startReceivingMessagesFromThreadId(LinkedBlockingQueue<String> queue, int threadId);
    
    /**
     * Stops receiving messages from a special single thread 
     * @param queue - Queue where the messages where saved
     * @param threadId - Id of the thread where stop receive the messages
     * @return Returns <b>true</b> if the Queue is successful deleted and returns <b>false</b> when something gone wrong
     */
    boolean stopReceivingMessagesFromThreadId(LinkedBlockingQueue<String> queue, int threadId);
    
    /**
     * Starts receiving messages from all active threads
     * @param queue - Queue where the messages were saved
     * @return Returns <b>true</b> if the Queue is successful added and returns <b>false</b> when something gone wrong
     * <br><br><b>Note:</b> The threads where will connect in the future are included too.
     */
    boolean startReceivingMessagesFromAllThreads(LinkedBlockingQueue<String> queue);
    
    /**
     * Stops receiving messages from all active threads threads
     * @param queue - Queue where the messages where saved
     * @return Returns <b>true</b> if the Queue is successful deleted and returns <b>false</b> when something gone wrong
     * <br><br><b>Note:</b> All queues which were insert by the single message method were deleted too.
     */
    boolean stopReceivingMessagesFromAllThreads(LinkedBlockingQueue<String> queue);
    
    /**
     * Sends a message to the client with the thread id
     * @param msg - Message where send to the client 
     * @param threadId - Id of the client
     * @return Returns <b>true</b> when the message where sent to the client and <b>false</b> on failure
     */
    boolean sendMessageToThreadId(String msg, int threadId);
}
