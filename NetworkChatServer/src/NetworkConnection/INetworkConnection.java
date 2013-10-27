package NetworkConnection;

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
	 * 
	 * @param port - Port where the server is listen on it.
	 * @return Returns <b>true</b> if the server is starting correctly and <b>false</b> on error
	 */
	public boolean startServer(int port);
	
	/**
	 * 
	 * @return Returns <b>true</b> if the server is stopping correctly and <b>false</b> on error
	 */
	public boolean stopServer();
	
	/**
	 * 
	 * @param queue - Queue where the messages where saved
	 * @param threadId - Id of the thread where receive the messages
	 * @return Returns <b>true</b> if the Queue is successful added and returns <b>false</b> when something gone wrong
	 */
	public boolean startReceivingMessagesFromThreadId(LinkedBlockingQueue<String> queue, int threadId);
	
	/**
	 * 
	 * @param queue - Queue where the messages where saved
	 * @param threadId - Id of the thread where stop receive the messages
	 * @return Returns <b>true</b> if the Queue is successful added and returns <b>false</b> when something gone wrong
	 */
	public boolean stopReceivingMessagesFromThreadId(LinkedBlockingQueue<String> queue, int threadId);
	
	/**
	 * 
	 * @param msg - Message where send to the client 
	 * @param threadId - Id of the client
	 * @return Returns <b>true</b> when the message where sent to the client and <b>false</b> on failure
	 */
	public boolean sendMessageToThreadId(String msg, int threadId);
}
