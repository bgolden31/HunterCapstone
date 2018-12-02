package registration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserHistoryDatabase {
	private Connection con= DataBaseConnector.connect();
	//Inserts into userHistory table
	public String insertUserHistory(JSONObject data) {
		try {	
			String sql = "insert into userHistory (username, recipe_name, author, recipeId) value (?,?,?,?)";
			PreparedStatement st2 = con.prepareStatement(sql);
			st2.setString(1, data.getString("username"));
			st2.setString(2, data.getString("recipeName"));
			st2.setString(3, data.getString("author"));
			st2.setInt(4, data.getInt("recipeId"));
			st2.executeUpdate();
			st2.close();
			return "User History insert success";
		}catch(Exception e) {
			System.out.println(e);
			return e.toString(); //Returns the error related
		}
	}
	//Gets all the recipes view by user in userHistory table
	public JSONArray getUserHistory(String username) {
		System.out.println(username);
		String sql = "select * from userHistory where username = ?";
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, username);
			ResultSet rs = st.executeQuery();
			JSONArray  userHistory = new JSONArray(); 
			
			while (rs.next()) {
				userHistory.put( buildHistoryObject ( rs.getString(2),rs.getString(3) , rs.getInt(4) ));	
			}
			st.close();
		    return userHistory;
		}catch(Exception e) {
			System.out.println(e);
		}		
		return null;
	}
	//Helper to  build history object
	JSONObject buildHistoryObject(String recipeName, String author, int recipeId){
		   JSONObject history = new JSONObject();
		   history .put("recipeName" ,recipeName);
		   history .put("author" ,author );
		   history .put("recipeId" ,recipeId );
		   return history;
	} 
	//Deletes recipe from userHistory based on username and recipename
	public String deleteUserHistory(String username, String recipename) {
		try {	
			String sql = "delete from userHistory where username=? and recipe_name = ?";
			PreparedStatement st2 = con.prepareStatement(sql);
			st2.setString(1, username);
			st2.setString(2, recipename);
			st2.executeUpdate();
			st2.close();
			return "User History delete success";
		}catch(Exception e) {
			System.out.println(e);
			return e.toString(); //Returns the error related
		}
	}
}
