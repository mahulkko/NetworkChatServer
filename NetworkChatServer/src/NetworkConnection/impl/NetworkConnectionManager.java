package NetworkConnection.impl;

import java.io.IOException;
import java.net.ServerSocket;

public class NetworkConnectionManager {
	
	private ServerSocket mServer;
	private int mPort;
	private boolean mIsRunning;
	private Object mLock;
	
	public NetworkConnectionManager() {
		mServer = null;
		mPort = -1;
		mIsRunning = false;
		mLock = new Object();		
	}
	
	public boolean connect(int port) {
		synchronized (mLock) {
			if (!isServerRunning()) {
				try {
					mServer = new ServerSocket(port);
					mPort = port;
					mIsRunning = true;
					return true;
				} catch (IOException e) {
					return false;
				}
			}
			return false;
		}
	}
	
	public boolean disconnect() {
		synchronized (mLock) {
			if (isServerRunning()) {
				try {
					mServer.close();
					mIsRunning = false;
					return true;
				} catch (IOException e) {
					return false;
				}
			}
			return false;
		}
	}
	
	public ServerSocket getServerSocket() {
		return mServer;
	}
	
	public int getPort() {
		return mPort;
	}
	
	public boolean isServerRunning() {
		return mIsRunning;
	}
}

