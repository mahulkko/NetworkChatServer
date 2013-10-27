package NetworkConnection;

/**
 * 
 * @author Martin Hulkkonen
 * <br>
 * <b>INetworkConnection</b> 
 *
 */
public interface INetworkConnection {

	/**
	 * 
	 * @param port - Port where the server is listen on it.
	 * @return Returns <b>true</b> if the server is starting correctly and <b>false</b> on error
	 */
	public boolean startServer(int port);
	
	/**
	 * 
	 * @return Returns <b>true</b> if the server is stopping correctly and <b>false</b> on error
	 */
	public boolean stopServer();
}
