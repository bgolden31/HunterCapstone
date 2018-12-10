package registration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserHistoryDatabase {
	private Connection con= DataBaseConnector.connect();
	
	/*Takes JSON info and insert it into userHistory table
	 * @param  JSON the data inserted
	 * @return Insert success/failure/error   */
	public String insertUserHistory(JSONObject data) throws SQLException {
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
		finally {
			con.close();
		}
	}	
	/* Gets all the recipe viewed by user in userHistory table
	 * Calls helper function buildHistoryObject ()
	 * @param  username user
	 * @return JSONArray of all the recipes viewed by user   */
	public JSONArray getUserHistory(String username) throws SQLException {
		System.out.println(username);
		String sql = "select * from userHistory where username = ? ORDER BY lastUpdated ASC";
		JSONArray  userHistory = new JSONArray(); 
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, username);
			ResultSet rs = st.executeQuery();
			
			
			while (rs.next()) {
				userHistory.put( buildHistoryObject ( rs.getString(2),rs.getString(3) , rs.getInt(4) ));	
			}
			st.close();
		    
		}catch(Exception e) {
			System.out.println(e);
			return null;
		}
		finally {
			con.close();
		}
		return userHistory;
	}	
	/* Helper function for getUserHistory, to build an object to fill history array
	 * @param  username user
	 * @param  author author
	 * @param  recipeID recipeId
	 * @return JSONArray of all the recipes viewed by user   */
	JSONObject buildHistoryObject(String recipeName, String author, int recipeId){
		   JSONObject history = new JSONObject();
		   history .put("recipeName" ,recipeName);
		   history .put("author" ,author );
		   history .put("recipeId" ,recipeId );
		   return history;
	}
	/* Deletes recipe from userHistory based on username and recipename
	 * @param  username user
	 * @param  recipename recipename
	 * @return Delete sucess/failure/error */
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
	public void closeCon() throws SQLException {
	       if(con != null) {
	           con.close();
	        }
	}
}
