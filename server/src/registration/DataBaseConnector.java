package registration;

import java.sql.Connection;
import java.sql.DriverManager;

/* Connects to the mySQL server on AWS
 * Used in every dataBase file
 */
public class DataBaseConnector {
	public static Connection connect() {
	   String dbName = ("sys");
  	   String userName = ("RIP");
  	   String password = ("Food1516");
  	   String hostname = ("aa1pxec1pjycn9h.cpquhfohdb53.us-east-2.rds.amazonaws.com");
  	   String port = ("3306");
  	   try {
  		   Class.forName("com.mysql.jdbc.Driver");
  		   String jdbcUrl = "JDBC:mysql://" + hostname + ":" + port + "/" + dbName + "?autoReconnect=true&useSSL=false";
  		   Connection con = DriverManager.getConnection(jdbcUrl, userName, password);
		if(con != null) {
			//System.out.println("Connection to database success"); not needed 
			return con;
		}
  	   }catch(Exception e){
  		   System.out.println(e);
  		   return null;
  	   }
  	   System.out.println("Connection to database failed");
  	   return null;
	}
}
