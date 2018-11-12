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
	
	//Takes info from Json and adds it to user table, with error checking and password hashing
	public String registerUser(JSONObject user) {
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
			//If user already exists early return
			if(rs.next())
				return "This account already exists";
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

	//Takes username and password and verify it matches on the user Table
	//Returns that users info
	public String loginUser(String username, String password) {
		JSONObject response = new JSONObject();
		System.out.println("Getting user");  
		//System.out.println(verifyUserPassword(password, securedPassword, salt));
		String sql = "select username, email, age, name, password, salt from user where username = ?";
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, username);
			ResultSet rs = st.executeQuery();
			if(!rs.next()) {
				return "Username does not exist";
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
				return response.toString();
			}
			else{
				return "Incorrect password";
			}
		}catch(Exception e) {
			System.out.println(e);
		}		
		return null;
	}
	//Removes user based on username
	public String deleteUser(String username) {
		try {	
			String sql = "delete from user where username = ?";
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, username);
			st.executeUpdate();
			st.close();
			return "User delete success";
		}catch(Exception e) {
			System.out.println(e);
		}
		return "User delete failed";
	}
	//Update user based on username and json info
	public String updateUser(String username, JSONObject user) {
		try {
			String Password = user.getString("password");
	        
	        // Generate Salt. The generated value can be stored in DB. 
	        String salt = PasswordUtils.getSalt(50);
	        
	        // Protect user's password. The generated value can be stored in DB.
	        String securePassword = PasswordUtils.generateSecurePassword(Password, salt);
	        
				String sql = "UPDATE user SET password = ?, email = ?, age = ?, name = ?, salt = ? where username = ?";
				PreparedStatement st = con.prepareStatement(sql);
				st = con.prepareStatement(sql);
				st.setString(1, securePassword);
				st.setString(2, user.getString("email"));
				st.setInt(3, user.getInt("age"));
				st.setString(4, user.getString("name"));
				st.setString(5, salt);
				st.setString(6, user.getString("username"));
				st.executeUpdate();
				st.close();
				return "User update success";
			}catch(Exception e) {
				System.out.println(e);
			}
			return "User update fail";
		}
}
