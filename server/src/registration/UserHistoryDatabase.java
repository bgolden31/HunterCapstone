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
	
	public String insertUserHistory(JSONObject data) {
		try {	
			String sql = "insert into userHistory (username, recipe_name, author) value (?,?,?)";
			PreparedStatement st2 = con.prepareStatement(sql);
			st2.setString(1, data.getString("username"));
			st2.setString(2, data.getString("recipeName"));
			st2.setString(3, data.getString("author"));
			st2.executeUpdate();
			st2.close();
			return "User History insert success";
		}catch(Exception e) {
			System.out.println(e);
		}
		return "User History insert fail";
	}
	
	public JSONObject getUserHistory(String username) {
		System.out.println(username);
		String sql = "select * from userHistory where username = ?";
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, username);
			ResultSet rs = st.executeQuery();
			JSONArray  userHistory = new JSONArray(); 
			
			while (rs.next()) {
				userHistory.put( buildHistoryObject ( rs.getString(1),rs.getString(2),rs.getString(3) ));	
			}
			
			st.close();
			JSONObject response= new JSONObject();
			response.put("userhistory", userHistory);
		    return response;

		}catch(Exception e) {
			System.out.println(e);
		}		
		return null;
	}
	JSONObject buildHistoryObject(String username, String recipeName, String author){
		   JSONObject history = new JSONObject();
		   history .put("username" , username);
		   history .put("recipeName" ,recipeName);
		   history .put("author" ,author );
		   return history;
	} 
	
}
