package DatabaseConnection;

/**
 * DatabaseConnection Interface
 * Manage the connection to a Database
 * @author Martin Hulkkonen
 *
 */
public interface IDatabaseConnection {
	
	boolean Connect();
	
	boolean Disconnect();
}
