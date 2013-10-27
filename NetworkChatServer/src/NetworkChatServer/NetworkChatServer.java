package NetworkChatServer;

import org.apache.log4j.BasicConfigurator;

import NetworkConnection.INetworkConnection;
import NetworkConnection.impl.NetworkConnection;

public class NetworkChatServer {

	public static void main(String[] args) {
		BasicConfigurator.configure();
		
		INetworkConnection connection = new NetworkConnection();
		connection.startServer(12345);
		while (true) {
			
		}
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
