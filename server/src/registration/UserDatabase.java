package registration;

import security.PasswordUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

public class UserDatabase {
	
	private Connection con= DataBaseConnector.connect();
	public UserDatabase() {}
	/* Registers a users based on JSON info and adds their info into user table 
	 * Calls password hashing functions to encrypt password
	 * @param  user JSON containing user info
	 * @returns Success/Failure/error
	 */	
	public String registerUser(JSONObject user) {
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
			return e.toString(); //Returns the error related
		}
	}
	/* Takes username and password and verify it matches on the user Table
	 * Calls password hashing functions to encrypt password
	 * @param  username user
	 * @param  password password
	 * @returns JSON with users info
	 */	
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
			return e.toString(); //Returns the error related
		}	
	}
	
	/* Takes username deletes all trace of that user from all tables
	 * Deletes all trace of user and their recipes from 
	 * userRecipe, userFavorite, recipeInfo, recipeRating, userHistory, user, recipe
	 * Their likedList, list, and favorites
	 * @param  username user
	 * @returns Delete success/failure or error
	 */	
	public String deleteUser(String username) {
		try {
			String sql = "select * from userRecipes where username = ?";
			PreparedStatement st2 = con.prepareStatement(sql);
			st2.setString(1, username);
			RecipeDatabase dataBase = new RecipeDatabase();

			ResultSet rs1 = st2.executeQuery();
			while (rs1.next()) {
				dataBase.deleteRecipe(rs1.getInt(2));	
			}
			st2.close();
			
			sql = "delete from user where username = ?";
			PreparedStatement st3 = con.prepareStatement(sql);
			st3.setString(1, username);
			int i = st3.executeUpdate();
			if(i==0) {
				 return "Nothing was deleted because user doesnt exists ";
			}
			else {
				sql = "delete from userFavorites where username = ?";
				PreparedStatement st = con.prepareStatement(sql);
				st.setString(1, username);
				 i = st.executeUpdate();
				st.close();
				
				sql = "delete from userRecipeList where username = ?";
				st = con.prepareStatement(sql);
				st.setString(1, username);
				i = st.executeUpdate();
				st.close();
				
				sql = "delete from likedTable where username = ?";
				st = con.prepareStatement(sql);
				st.setString(1, username);
				i = st.executeUpdate();
				st.close();
			}
			st3.close();
			return "User delete success";
		}catch(Exception e) {
			System.out.println(e);
			return e.toString();
		}
	}
	/* Based on JSON info, Updates a user info in user table 
	 * Calls password hashing functions to encrypt password
	 * @param  user JSON containing user info
	 * @returns Success/Failure/error
	 */	
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
				int i = st.executeUpdate();
				if( i==0) {
					return "User doesn't exist, can't update";
				}
				st.close();
				return "User update success";
			}catch(Exception e) {
				System.out.println(e);
				return e.toString(); //Returns the error related
			}
		}
	/* Check if a user exists in user table
	 * @param  username user
	 * @returns boolean of whether or not user exists
	 */	
	public boolean checkUser(String username) {
		try {
			String sql = "select * from user where username = ?";
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, username);
			ResultSet rs = st.executeQuery();
			if(!rs.next()) {
				return false;
			}
			return true;
			}catch(Exception e) {
				System.out.println(e);
			}
		return false;
	}
	
	public void closeCon() throws SQLException  {
       if(con != null) {
           con.close();
        }
	}
}
