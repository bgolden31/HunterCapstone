package registration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.json.JSONObject;

public class UserDatabase {
	
	private Connection con=null;
	
	//connect to database
	public UserDatabase(){
		con = DataBaseConnector.connect(con);
	}
	
	//register,1. get add username from database table, 2.compare to user input, 3. register, or reject
	public String register(JSONObject user) {
		//List<String> name = new ArrayList<>(); 
		String sql = "select * from user where username = ?";
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, user.getString("username"));
			ResultSet rs = st.executeQuery();
			if(rs.next())
				return "This account exists";
			sql = "insert into user (user_id, username, password, email, age, name) value (?,?,?,?,?,?)";
			st = con.prepareStatement(sql);
			st.setInt(1, user.getInt("user_id"));
			st.setString(2, user.getString("username"));
			st.setString(3, user.getString("password"));
			st.setString(4, user.getString("email"));
			st.setInt(5, user.getInt("age"));
			st.setString(6, user.getString("name"));
			st.executeUpdate();
			st.close();
			return "Register success";
		}catch(Exception e) {
			System.out.println(e);
		}
		return "Register fail";
	}

	//login 
	public JSONObject getUser(String username, String password) {
		// TODO Auto-generated method stub
		System.out.println(username + password);
		JSONObject response = new JSONObject();
		
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
			response.put("userId", rs.getInt(1));
			response.put("userName", rs.getString(2));
			response.put("password", rs.getString(3));
			response.put("email",rs.getString(4));
			response.put("age",rs.getInt(5));
			response.put("name", rs.getString(6));
			st.close();
			
			return response;
		}catch(Exception e) {
			System.out.println(e);
		}		
		return null;
	}
	
}
