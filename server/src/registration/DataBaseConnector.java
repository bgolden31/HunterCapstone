package registration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/* Connects to the mySQL server on AWS
 * Used in every dataBase file
 */
public class DataBaseConnector {
	public static Connection connect() {
	   String dbName = ("sys");
  	   String userName = ("RIP");
  	   String password = ("Food1516");
  	   String hostname = ("please1.cpquhfohdb53.us-east-2.rds.amazonaws.com");
  	   String port = ("3306");
  	   try {
  		   Class.forName("com.mysql.jdbc.Driver");
  		   String jdbcUrl = "JDBC:mysql://" + hostname + ":" + port + "/" + dbName + "?autoReconnect=true&useSSL=false";
  		   Connection con = DriverManager.getConnection(jdbcUrl, userName, password);
		if(con != null) {
			System.out.println("Connection to database success"); //not needed 
			return con;
		}
  	   }catch(Exception e){
  		   System.out.println(e);
  		   return null;
  	   }
  	   System.out.println("Connection to database failed");
  	   return null;
	}
	public static void closeSt(PreparedStatement st) throws SQLException {
       if(st !=null) {
          st.close();
       } 

	}
	public static void closeRs(ResultSet rs) throws SQLException {
		if(rs != null) {
            rs.close();
		}
	}
	public static void closeCon(Connection con) throws SQLException {
       if(con != null) {
           con.close();
        }
	}
}
