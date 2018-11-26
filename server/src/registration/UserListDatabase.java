package registration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserListDatabase {
	private Connection con= DataBaseConnector.connect();
	//Inserts into userHistory table
	public String createList(JSONObject data) {
		try {	
			String sql = "insert ignore into userList (username, listName) value (?,?)";
			PreparedStatement st2 = con.prepareStatement(sql);
			st2.setString(1, data.getString("username"));
			st2.setString(2, data.getString("listName"));
			int i= st2.executeUpdate();
			if(i==0) {
				st2.close();
				return "User List already exists";
			}
			st2.close();
			return "User List insert success";
		}catch(Exception e) {
			System.out.println(e);
		}
		return "User List insert fail";
	}
	//Deletes recipe from userHistory based on username and recipename
	public String deleteList(JSONObject data) {
		try {	
			String sql = "delete from userList where username = ? and listName= ? and listId = ?";
			PreparedStatement st2 = con.prepareStatement(sql);
			st2.setString(1, data.getString("username"));
			st2.setString(2, data.getString("listName"));
			st2.setInt(3, data.getInt("listId"));
			st2.executeUpdate();
			st2.close();
			return "User History delete success";
		}catch(Exception e) {
			System.out.println(e);
		}
		return "User History delete fail";
	}
	public String insertToList(JSONObject data) {
		try {	
			String sql = "insert ignore into userRecipeList (username, recipe_name, author, recipeId, listId) value (?,?,?,?,?)";
			PreparedStatement st2 = con.prepareStatement(sql);
			st2.setString(1, data.getString("username"));
			st2.setString(2, data.getString("recipeName"));
			st2.setString(3, data.getString("author"));
			st2.setInt(4, data.getInt("recipeId"));
			st2.setInt(5, data.getInt("listId"));
			int i= st2.executeUpdate();
			if(i==0) {
				st2.close();
				return "Recipe already exists in this list";
			}
			st2.close();
			return "Recipe to List insert success";
		}catch(Exception e) {
			System.out.println(e);
		}
		return "Recipe to List insert fail";
	}
	//Deletes recipe from userHistory based on username and recipename
	public String deletefromList(JSONObject data) {
		try {	
			String sql = "delete from userRecipeList where username = ? AND recipe_name = ?  AND author = ?  AND recipeId = ? AND listId = ?";
			PreparedStatement st2 = con.prepareStatement(sql);
			st2.setString(1, data.getString("username"));
			st2.setString(2, data.getString("recipeName"));
			st2.setString(3, data.getString("author"));
			st2.setInt(4, data.getInt("recipeId"));
			st2.setInt(5, data.getInt("listId"));
			st2.executeUpdate();
			st2.close();
			return "User History delete success";
		}catch(Exception e) {
			System.out.println(e);
		}
		return "User History delete fail";
	}
	//Gets all the recipes view by user in userHistory table
	public JSONObject getUserList(String username) {
		String sql = "select * from userList where username = ?";
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, username);
			ResultSet rs = st.executeQuery();
			
			JSONObject response= new JSONObject();
			JSONArray userHistory = new JSONArray(); 
			while (rs.next()) {
				userHistory.put(buildListObject (rs.getString(2),rs.getInt(3) ));	
			}
		
			response.put("username", username);
			response.put("listInfo", userHistory);
			st.close();
		    return response;

		}catch(Exception e) {
			System.out.println(e);
		}		
		return null;
	}
	//Helper to  build history object
	JSONObject buildListObject(String listName, int listId){
		   JSONObject history = new JSONObject();
		   history .put("listName," ,listName );
		   history .put("listId" ,listId);
		   return history;
	} 
	
	//Gets all the recipes view by user in userHistory table
	public JSONObject getAllUserListInfo(String username) {
		String sql = "select * from userList where username = ?";
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, username);
			ResultSet rs = st.executeQuery();
			
			JSONObject response= new JSONObject();
			JSONArray userHistory = new JSONArray(); 
			while (rs.next()) {
				userHistory.put( getAllUserListInfoHelper (rs.getString(1), rs.getString(2), rs.getInt(3)) );	
			}
		
			response.put("username", username);
			response.put("listInfo", userHistory);
			st.close();
		    return response;

		}catch(Exception e) {
			System.out.println(e);
		}		
		return null;
	}
	public JSONObject getAllUserListInfoHelper(String username, String listName, int listId) {
		String sql = "select * from userRecipeList where username = ? and listId = ?";
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, username);
			st.setInt(2, listId);
			ResultSet rs = st.executeQuery();
			
			JSONObject response= new JSONObject();
			JSONArray userHistory = new JSONArray(); 
			while (rs.next()) {
				userHistory.put(buildListInfoObject (rs.getString(2),rs.getString(3) , rs.getInt(4)  ));	
			}
			
			st.close();
			response.put("listName", listName);
			response.put("listId", listId);
			response.put("info", userHistory);
		    return response;

		}catch(Exception e) {
			System.out.println(e);
		}		
		return null;
	}
	//Helper to  build history object
	JSONObject buildListInfoObject(String recipeName, String author, int recipeId){
		   JSONObject history = new JSONObject();
		   history .put("recipeName" ,recipeName);
		   history .put("author" ,author );
		   history .put("recipeId" ,recipeId );
		   return history;
	} 
	
	//Gets all the recipes view by user in userHistory table
	public JSONObject getUserListInfo(int id) {
		
		try {
			String sql = "select * from userList where listId = ?";
			PreparedStatement  st = con.prepareStatement(sql);
			st = con.prepareStatement(sql);
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
			
			JSONObject response= new JSONObject();
			while (rs.next()) {
				response.put("listName", rs.getString(2));
				response.put("username", rs.getString(1));
			}
			response.put("listId", id);
			
			st.close();
			
			sql = "select * from userRecipeList where listId = ?";
			st = con.prepareStatement(sql);
			st.setInt(1, id);
			rs = st.executeQuery();

			JSONArray userHistory = new JSONArray(); 
			while (rs.next()) {
				userHistory.put(buildListInfoObject (rs.getString(2),rs.getString(3) , rs.getInt(4)  ));
			}
			
			st.close();
			response.put("info", userHistory);
		    return response;

		}catch(Exception e) {
			System.out.println(e);
		}		
		return null;
	}
	public String favoriteInsert(JSONObject data) {
		try {	
			String sql = "insert into userFavorites (username, recipe_name, author, recipeId) value (?,?,?,?)";
			PreparedStatement st2 = con.prepareStatement(sql);
			st2.setString(1, data.getString("username"));
			st2.setString(2, data.getString("recipeName"));
			st2.setString(3, data.getString("author"));
			st2.setInt(4, data.getInt("recipeId"));
			int i= st2.executeUpdate();
			if(i==0) {
				st2.close();
				return "User List already exists";
			}
			st2.close();
			return "User List insert success";
		}catch(Exception e) {
			System.out.println(e);
		}
		return "User List insert fail";
	}
	//Deletes recipe from userHistory based on username and recipename
	public String favoriteDelete(JSONObject data) {
		try {	
			String sql = "delete from userFavorites where username = ? AND recipe_name = ?  AND author = ?  AND recipeId = ?";
			PreparedStatement st2 = con.prepareStatement(sql);
			st2.setString(1, data.getString("username"));
			st2.setString(2, data.getString("recipeName"));
			st2.setString(3, data.getString("author"));
			st2.setInt(4, data.getInt("recipeId"));
			st2.executeUpdate();
			st2.close();
			return "User History delete success";
		}catch(Exception e) {
			System.out.println(e);
		}
		return "User History delete fail";
	}
	//Get favorites 
	public JSONObject getUserFavorites(String username) {
		
		try {
			String sql = "select * from userFavorites where username = ?";
			PreparedStatement  st = con.prepareStatement(sql);
			st = con.prepareStatement(sql);
			st.setString(1, username);
			ResultSet rs = st.executeQuery();
			
			JSONObject response= new JSONObject();
			JSONArray userHistory = new JSONArray(); 
			while (rs.next()) {
				userHistory.put(buildListInfoObject (rs.getString(2),rs.getString(3) , rs.getInt(4)  ));
			}
			
			st.close();
			response.put("info", userHistory);
			response.put("username", username);
		    return response;

		}catch(Exception e) {
			System.out.println(e);
		}		
		return null;
	}
}