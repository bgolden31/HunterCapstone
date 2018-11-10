package registration;

import security.PasswordUtils;

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
		// TODO Auto-generated method stub
        String Password = user.getString("password");
        
        // Generate Salt. The generated value can be stored in DB. 
        String salt = PasswordUtils.getSalt(50);
        
        // Protect user's password. The generated value can be stored in DB.
        String securePassword = PasswordUtils.generateSecurePassword(Password, salt);
		
		String sql = "select * from user where username = ?";
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, user.getString("username"));
			ResultSet rs = st.executeQuery();
			if(rs.next())
				return "This account exists";
			sql = "insert into user (username, password, email, age, name, salt) value (?,?,?,?,?,?)";
			st = con.prepareStatement(sql);
			st.setString(1, user.getString("username"));
			st.setString(2, securePassword);
			st.setString(3, user.getString("email"));
			st.setInt(4, user.getInt("age"));
			st.setString(5, user.getString("name"));
			st.setString(6, salt);
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
		JSONObject response = new JSONObject();
		System.out.println("Getting user");  
		//System.out.println(verifyUserPassword(password, securedPassword, salt));
		String sql = "select username, email, age, name, password, salt from user where username = ?";
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, username);
			ResultSet rs = st.executeQuery();
			if(!rs.next()) {
				System.out.println(!rs.next());
				return null;
			}
			String securedPassword = rs.getString(5);
			String salt = rs.getString(6);
			System.out.println(securedPassword + PasswordUtils.verifyUserPassword(password, securedPassword, salt));//xxxxx
			if(PasswordUtils.verifyUserPassword(password, securedPassword, salt)) {
				System.out.println("Getting user"); 
				response.put("userName", rs.getString(1));
				response.put("email",rs.getString(2));
				response.put("age",rs.getInt(3));
				response.put("name", rs.getString(4));
				st.close();
				return response;
			}
		}catch(Exception e) {
			System.out.println(e);
		}		
		return null;
	}
	
}
