package registration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDatabase {
	
	private Connection con=null;
	
	//connect to database
	public UserDatabase(){
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
	}
	
	//register,1. get add username from database table, 2.compare to user input, 3. register, or reject
	public String register(User user) {
		//List<String> name = new ArrayList<>(); 
		String sql = "select * from user where username = ?";
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, user.getUsername());
			ResultSet rs = st.executeQuery();
			if(rs.next())
				return "This account exists";
			sql = "insert into user (user_id, username, password, email, age, name) value (?,?,?,?,?,?)";
			st = con.prepareStatement(sql);
			st.setInt(1, user.getUser_id());
			st.setString(2, user.getUsername());
			st.setString(3, user.getPassword());
			st.setString(4, user.getEmail());
			st.setInt(5, user.getAge());
			st.setString(6, user.getName());
			st.executeUpdate();
			st.close();
			return "Register success";
		}catch(Exception e) {
			System.out.println(e);
		}
		return "Register fail";
	}

	//login 
	public User getUser(String username, String password) {
		// TODO Auto-generated method stub
		System.out.println(username + password);
		User temp = new User();
		String sql = "select * from user where username = ? and password = ?";
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, username);
			st.setString(2, password);
			ResultSet rs = st.executeQuery();
			System.out.println("Getting user"); //xxxxx
			if(!rs.next()) {
				System.out.println(!rs.next());
				return null;
			}
			System.out.println("1 :"+rs.getInt(1) + "2 :"+rs.getString(2)); 
			temp.setUser_id(rs.getInt(1));
			temp.setUsername(rs.getString(2));
			temp.setPassword(rs.getString(3));
			temp.setEmail(rs.getString(4));
			temp.setAge(rs.getInt(5));
			temp.setName(rs.getString(6));
			st.close();
			return temp;
		}catch(Exception e) {
			System.out.println(e);
		}		
		return null;
	}
	
}
