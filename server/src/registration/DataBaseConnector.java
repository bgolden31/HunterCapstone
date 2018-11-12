package registration;

import java.sql.Connection;
import java.sql.DriverManager;

//Connects to SQL server on AWS
public class DataBaseConnector {
	public static Connection connect (Connection con) {
	String dbName = ("sys");
     String userName = ("RIP");
     String password = ("Food1516");
     String hostname = ("aa1pxec1pjycn9h.cpquhfohdb53.us-east-2.rds.amazonaws.com");
     String port = ("3306");
     try {
		Class.forName("com.mysql.jdbc.Driver");
	
     String jdbcUrl = "JDBC:mysql://" + hostname + ":" + port + "/" + dbName + "?autoReconnect=true&useSSL=false";
		con = DriverManager.getConnection(jdbcUrl, userName, password);
		if(con == null)
			System.out.println("Connection to database failed");
		else
			System.out.println("Connection to database success");			
	}catch(Exception e){
		System.out.println(e);
	}
     return con;
	}
}
