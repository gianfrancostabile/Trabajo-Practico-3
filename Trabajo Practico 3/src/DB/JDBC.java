package DB;


import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

public class JDBC{
	
	// JDBC driver name and database URL
	private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private final String DB_URL = "jdbc:mysql://localhost/DB_TP3_TSSI";
	
	// Database credentials
	private final String USER = "root";
	private final String PASS = "";
	
	public static Connection conn = null;
	
	private static JDBC instance = null;
	
	public static JDBC getInstance() {

		if(instance == null) {
			instance = new JDBC();
		}
		
		return instance;
	}
	
	private JDBC() {
		
		try {
			//STEP 2: Register JDBC driver
			Class.forName(this.JDBC_DRIVER);
			
			//STEP 3: Open a connection
			System.out.println("Conectando a la base de datos...");
			conn = (Connection) DriverManager.getConnection(this.DB_URL, this.USER, this.PASS);
			
		}	catch(SQLException se) {
			//Handle errors for JDBC
			se.printStackTrace();
			
		}	catch(Exception e) {
			//Handle errors for Class.forName
			e.printStackTrace();
			
		}
		
	}
	
	public void closeJDBC() {
		
		try {
			conn.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

}
