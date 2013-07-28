package NetworkChatServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.BasicConfigurator;

import Connection.IConnection;
import Connection.impl.Connection;

public class NetworkChatServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Configure the logger with Basic
		BasicConfigurator.configure();
				
		IConnection con = new Connection(12345);
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
	}
}
