package DatabaseConnection.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import DatabaseConnection.IDatabaseConnection;

public class DatabaseConnection implements IDatabaseConnection {
	
	public void test() {
		
		Statement stm;
		ResultSet result;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection( "jdbc:mysql://localhost/test?user=root&password=");
			stm = con.createStatement();
			stm.execute("CREATE TABLE martin " +
						"(firstname varchar(50), " +
						"lastname varchar(50));");
			System.out.println("ok");
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("Fehler");
			e.printStackTrace();
		}
	}

	@Override
	public boolean Connect() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean Disconnect() {
		// TODO Auto-generated method stub
		return false;
	}
}
