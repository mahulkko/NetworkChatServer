
/* Connection Class - Connection
 * Written by Martin Hulkkonen
 * 
 * Connection 
 * ==========
 * Makes a connection to a Server given with the parameters 
 * address for the address and port for the port of the server
 * 
 * Note:
 * The functions getInputStream() and getOutputStream() can return 
 * a null pointer. 
 * With the function isConnected() you can proof if the Streams have
 * valid values.
 * 
 */

package Connection.impl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Connection.IConnection;

/**
 * Server managed the different connections from the clients
 * @author Martin Hulkkonen
 */
public class Connection implements IConnection {

	private ServerSocket server;
	private Socket s;
	private int port;
	private boolean isRunning;
	
	public Connection(int port) {
		this.server = null;
		this.s = null;
		this.port = port;
		this.isRunning = false;
	}

	@Override
	public boolean startServer() {
		if(!isRunning()){
			try {
				this.server = new ServerSocket( this.port );
				this.s = this.server.accept();
				this.isRunning = true;
				while(isRunning()){
					
				}
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}	
		return false;
	}

	@Override
	public boolean stopServer() {
		if(isRunning()){
			try {
				this.s.close();
				this.server.close();
				this.isRunning = false;
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean isRunning() {
		return this.isRunning;
	}
}
