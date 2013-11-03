package connection.databaseConnection.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import connection.databaseConnection.IDatabaseConnection;

/**
 * 
 * @author Martin Hulkkonen
 *
 */
public class DatabaseConnection implements IDatabaseConnection {
    
    /**
     * Connection to the database
     */
    private Connection con;
    
    /**
     * Statement to execute sql commands
     */
    private Statement stm;
    
    /**
     * ResultSet for the statements from the database
     */
    private ResultSet result;
    
    /**
     * State of the connection from the server
     */
    private boolean isConnected;
    
    /**
     * Logger for log4j connection
     */
    private static Logger log = Logger.getLogger("Connection.DatabaseConnection");
    
    
    /**
     * Constructor of the DatabaseConnection
     */
    public DatabaseConnection() {
        try {
        // Load driver for the database
        log.info("Initialize the database connection");
        log.info("Try to load the database driver");
        Class.forName("com.mysql.jdbc.Driver");
        this.isConnected = false;
        log.info("Driver successfull loaded");
        } catch (ClassNotFoundException e) {
            log.error("Could not load the database driver");
        }
    }

    /**
     * Establish a connection to a database
     * @param server - Address of the SQL Server
     * @param database - Name of the database on the server
     * @param user - User name of the 
     * @param passwd - Password of the database
     * @return On success it will return <b>true</b> on failure it returns <b>false</b>
     */
    @Override
    public boolean connect(String server, String database, String user, String passwd) {
        log.info("Try to connect to the database");
        if(!this.isConnected) {
            try {
                this.con = DriverManager.getConnection( "jdbc:mysql://" + server + "/" + database + "?user=" + user + "&password=" + passwd);
                this.stm = this.con.createStatement();  
                this.isConnected = true;
                log.info("Successful connected to the server");
                return true;
            } catch (SQLException e) {
                log.error("Could not connect to the database");
                return false; 
            }
        }
        log.info("Connection already established to the server");
        return false;
    }

    /**
     * Returns the Statement of a database connection to execute sql querrys
     * @return On success it will return the <b>Statement</b> on error it will return <b>null</b>
     * <br><br>
     * <b><u>Note:</u></b>
     * <br>
     * This function is not thread safe
     */
    @Override
    public Statement getStatement() {
        if (this.isConnected) { 
            return this.stm; 
        }
        return null;
    }

    /**
     * Returns the ResultSet of a database connection to get the result
     * @return On success it will return the <b>ResultSet</b> on error it will return <b>null</b>
     * <br><br>
     * <b><u>Note:</u></b>
     * <br>
     * This function is not thread safe
     */
    @Override
    public ResultSet getResultSet() {
        if (this.isConnected) { 
            return this.result; 
        }
        return null;
    }


    /**
     * Close a open connection from a database
     * @return On success it will return <b>true</b> on failure it returns <b>false</b>
     */
    @Override
    public boolean disconnect() {
        log.info("Try to disconnect from the server");
        if(this.isConnected) {
            try {
                log.info("Close the ResultSet");
                if(this.result != null) { 
                    this.result.close(); 
                }
                log.info("Close the Statement");
                if(this.stm != null) { 
                    this.stm.close(); 
                }
                log.info("Close the connection");
                if(this.con != null) { 
                    this.con.close(); 
                }
                this.isConnected = false;
                log.info("Successful disconected");
                return true;
            } catch (SQLException e) {
                log.error("Faild to close the connection from the server");
                return false;
            }
        }
        log.info("There is no connection to the server");
        return false;
    }
}
