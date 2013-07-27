package NetworkChatServer;

import Connection.IConnection;
import Connection.impl.Connection;

public class NetworkChatServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IConnection con = new Connection(12345);
		con.startServer();
		
		while(true) {
			for(int i = 0; i < con.getMaxConnections(); i++) {
				String message = con.getMessageFromThreadID(i);
				if(message != null) {
					System.out.println("New Message from Client " + i + ": " + message);
				}
			}
		}
	}

}
