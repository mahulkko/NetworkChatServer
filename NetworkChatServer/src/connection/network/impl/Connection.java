package connection.network.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

/**
 * 
 * @author Martin Hulkkonen
 *
 */
public class Connection implements Runnable {

    /**
     * BufferedReder for the InputStream
     */
    private BufferedReader in;
    
    /**
     * PrintWriter for the OutputStream
     */
    private PrintWriter out;
    
    /**
     * ThreadID of the Thread
     */
    private int threadId;
    
    /**
     * Queue for saving messages from the clients
     */
    private List<LinkedBlockingQueue<String>> queue;
    
    /**
     * Socket from the connection to the client
     */
    private Socket socket;
    
    /**
     * NetworkConnectionManager
     */
    private NetworkConnectionManager networkConnectionManager;
    
    /**
     * Logger for log4j connection
     */
    private static Logger log = Logger.getLogger("NetworkConnection.Connection");
    
    /**
     * SpaceChar for splitting the strings
     */
    private static final char SPACER = 0x1e;
    
    /**
     * Connection of each single client
     * @param socket - Socket of the new client
     * @param threadId - Id of the thread
     */
    public Connection(Socket socket, int threadId, NetworkConnectionManager manager) {
        try {
            log.info("Initialize the connection (ThreadId " + threadId + ")");
            this.threadId = threadId;
            this.socket = socket;
            this.networkConnectionManager = manager;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(this.socket.getOutputStream(), true);
            this.queue = new LinkedList<LinkedBlockingQueue<String>>();
        } catch (IOException e) {
            log.error("Failed to initialize the connection (ThreadId " + this.threadId + ")");
        }
    }

    /**
     * Start to receive messages from these thread
     * @param queue - Queue that were insert in the list
     * @return When the queue is successfully insert it will returns <b>true</b> on error it returns <b>false</b>
     */
    public boolean startReceivingMessages(LinkedBlockingQueue<String> queue) {
        if (!this.queue.contains(queue)) {
            log.info("Start receiving messages from client - Queue insert (ThreadId " + this.threadId + ")");
            return this.queue.add(queue);
        }
        log.info("Queue already insert (ThreadId " + this.threadId + ")");
        return false;
    }

    /**
     * Stop to receive messages from these thread
     * @param queue - Queue that were deleted from the list
     * @return When the queue is successfully deleted it will returns <b>true</b> on error it returns <b>false</b>
     */
    public boolean stopReceivingMessages(LinkedBlockingQueue<String> queue) {
        if (this.queue.contains(queue)) {
            log.info("Stop receiving messages from client - Queue deleted (ThreadId " + this.threadId + ")");
            return this.queue.remove(queue);
        }
        log.info("Queue isnt insert in here (ThreadId " + this.threadId + ")");
        return false;
    }

    /**
     * Sends a message to the client
     * @param msg - Message that send to the client
     */
    public void sendMessageToClient(String msg){
        log.info("Send message to client: " + msg + " (ThreadId " + this.threadId + ")");
        this.out.println(msg);
    }

    /**
     * Thread for the connection of each client
     * Manage the receive from the client
     */
    @Override
    public void run() {
        while(this.networkConnectionManager.isServerRunning()) {
            try {
                log.info("Wait for a new message from client (ThreadId " + this.threadId + ")");
                String msg = this.in.readLine();
                if (!msg.contentEquals("null")) {
                    log.info("New message from client: " + msg + " (ThreadId " + this.threadId + ")");
                    log.info("Add message to the queues (ThreadId " + this.threadId + ")");
                    
                    for(int i = 0; i < this.queue.size(); ++i) {
                        try {
                            this.queue.get(i).put(String.valueOf(this.threadId) + SPACER + msg);
                        } catch (InterruptedException e) {
                            log.info("Failed to put the message in the queue (ThreadId " + this.threadId + ")");
                        }
                    }
                }
            } catch (IOException e) {
                log.info("Failed to read the InputStream (ThreadId " + this.threadId + ")");
                log.info("Client disconnected (ThreadId " + this.threadId + ")");
                break;
            }
        }
        
        log.info("Send message to all that the client has disconnected");
        for(int i = 0; i < this.queue.size(); ++i) {
            try {
                this.queue.get(i).put(String.valueOf(this.threadId) + SPACER + "0002");
            } catch (InterruptedException e) {
                log.info("Failed to put the message in the queue (ThreadId " + this.threadId + ")");
            }
        }

        log.info("Cleanup the connection (ThreadId " + this.threadId + ")");
        this.networkConnectionManager.setThreadToNull(threadId);
        this.networkConnectionManager.setConnectionToNull(threadId);
        try {
            log.info("Try to close the connection from the client (ThreadId " + this.threadId + ")");
            this.socket.close();
            this.in.close();
            this.out.close();
            log.info("Connection closed (ThreadId " + this.threadId + ")");
        } catch (IOException e) {
            log.error("Failed to close the connection (ThreadId " + this.threadId + ")");
        }
        log.info("Connection thread stopps now (ThreadId " + this.threadId + ")");
    }
}
