package NetworkChatServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import DatabaseConnection.impl.DatabaseConnection;
import NetworkConnection.INetworkConnection;
import NetworkConnection.impl.NetworkConnection;
import String.impl.SplitString;


public class NetworkChatServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		//TODO create the database class 
		//TODO create the class for the user management
		
		// Configure the logger with Basic
		BasicConfigurator.configure();
		
		DatabaseConnection con = new DatabaseConnection();
		con.Connect("localhost", "test","root","");
		con.Disconnect();
	
		/*
		INetworkConnection con = new NetworkConnection(12345);
		con.startServer();
		
		InputStreamReader isr = new InputStreamReader(System.in);
	    BufferedReader br = new BufferedReader(isr);
		
		while(true) {
			
			try {
				String msg = br.readLine();
				for(int i = 0; i < con.getMaxConnections(); i++) {
					con.sendMessageToThreadID(i, msg);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
