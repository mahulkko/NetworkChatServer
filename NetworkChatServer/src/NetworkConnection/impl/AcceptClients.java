package NetworkConnection.impl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class AcceptClients implements Runnable {

	private NetworkConnectionManager mNetworkConnectionManager;
	Socket mClient;
	ServerSocket mServer;
	
	public AcceptClients(NetworkConnectionManager manager) {
		this.mNetworkConnectionManager = manager;
		mServer = this.mNetworkConnectionManager.getServerSocket();
	}
	
	@Override
	public void run() {
		while(this.mNetworkConnectionManager.isServerRunning()) {
			try {
				mClient = mServer.accept();
			} catch (IOException e) {
				// Implement me
			}
		}
	}
}
