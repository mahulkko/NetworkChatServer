package DatabaseConnection;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 * DatabaseConnection Interface
 * Manage the connection to a Database
 * @author Martin Hulkkonen
 *
 */
public interface IDatabaseConnection {
	
	/**
	 * Establish a connection to a database
	 * @param server - Address of the SQL Server
	 * @param database - Name of the database on the server
	 * @param user - User name of the 
	 * @param passwd - Password of the database
	 * @return On success it will return <b>true</b> on failure it returns <b>false</b>
	 */
	boolean Connect(String server,String database, String user, String passwd);
	
	/**
	 * Returns the Statement of a database connection to execute sql querrys
	 * @return On success it will return the <b>Statement</b> on error it will return <b>null</b>
	 * <br><br>
	 * <b><u>Note:</u></b>
	 * <br>
	 * This function is not thread safe
	 */
	Statement getStatement();
	
	/**
	 * Returns the ResultSet of a database connection to get the result
	 * @return On success it will return the <b>ResultSet</b> on error it will return <b>null</b>
	 * <br><br>
	 * <b><u>Note:</u></b>
	 * <br>
	 * This function is not thread safe
	 */
	ResultSet getResultSet();
	
	/**
	 * Close a open connection from a database
	 * @return On success it will return <b>true</b> on failure it returns <b>false</b>
	 */
	boolean Disconnect();
	
	
}
