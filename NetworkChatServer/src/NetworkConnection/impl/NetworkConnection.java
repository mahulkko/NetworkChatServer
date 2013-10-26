package NetworkConnection.impl;

import NetworkConnection.INetworkConnection;

public class NetworkConnection implements INetworkConnection {

	private NetworkConnectionManager mNetworkConnectionManager;
	
	public NetworkConnection() {
		mNetworkConnectionManager = new NetworkConnectionManager();
	}

	@Override
	public boolean connect(int port) {
		return this.mNetworkConnectionManager.connect(port);
	}

	@Override
	public boolean disconnect() {
		return this.mNetworkConnectionManager.disconnect();
	}
}
