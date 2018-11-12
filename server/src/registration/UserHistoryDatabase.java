package registration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserHistoryDatabase {
	private Connection con=null;
	
	//connect to database
	public UserHistoryDatabase(){
		con = DataBaseConnector.connect(con);
	}
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
		}
		return "User History insert fail";
	}
	//Gets all the recipes view by user in userHistory table
	public JSONObject getUserHistory(String username) {
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
			JSONObject response= new JSONObject();
			response.put(username, userHistory);
		    return response;

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
			String sql = "delete from userHistory username = ?, recipe_name = ?";
			PreparedStatement st2 = con.prepareStatement(sql);
			st2.setString(1, username);
			st2.setString(2, recipename);
			st2.executeUpdate();
			st2.close();
			return "User History delete success";
		}catch(Exception e) {
			System.out.println(e);
		}
		return "User History delete fail";
	}
}
