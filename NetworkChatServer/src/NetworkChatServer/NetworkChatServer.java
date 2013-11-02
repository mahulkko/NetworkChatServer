package NetworkChatServer;

import org.apache.log4j.BasicConfigurator;
import UserManager.IUserManager;
import UserManager.impl.UserManager;
import Connection.NetworkConnection.INetworkConnection;
import Connection.NetworkConnection.impl.NetworkConnection;

public class NetworkChatServer {

	public static void main(String[] args) {
		BasicConfigurator.configure();
		
		INetworkConnection connection = new NetworkConnection();
		connection.startServer(12345);
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
