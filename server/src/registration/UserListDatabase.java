package registration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserListDatabase {
	private Connection con= DataBaseConnector.connect();

	/* Create a new list based on JSON info and inserts it into userList table 
	 * @param  JSON the data inserted
	 * @return List create success/failure/error */
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
			return "User List creation success";
		}catch(Exception e) {
			System.out.println(e);
			return e.toString(); //Returns the error related
		}
	}
	/* Based on JSON info deletes a list from userList table 
	 * @param  JSON the data inserted
	 * @return List Deletion success/failure/error */
	public String deleteList(JSONObject data) {
		try {	
			String sql = "delete from userList where username = ? and listName= ? and listId = ?";
			PreparedStatement st2 = con.prepareStatement(sql);
			st2.setString(1, data.getString("username"));
			st2.setString(2, data.getString("listName"));
			st2.setInt(3, data.getInt("listId"));
			st2.executeUpdate();
			st2.close();
			return "List delete success";
		}catch(Exception e) {
			System.out.println(e);
			return e.toString(); //Returns the error related
		}
	}
	/* Based on JSON info inserts recipe to a list in userRecipeList table 
	 * @param  JSON the data inserted
	 * @return Recipe to List insertion success/failure/error */
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
				return "Recipe already exists in this list or the list doesnt exist";
			}
			st2.close();
			return "Recipe to List insert success";
		}catch(Exception e) {
			System.out.println(e);
			return e.toString(); //Returns the error related
		}
	}
	/* Based on JSON info deletes recipe from a list in userRecipeList table 
	 * @param  JSON the data inserted
	 * @return Recipe to List deletion success/failure/error */
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
			return "Recipe delete from list success";
		}catch(Exception e) {
			System.out.println(e);
			return e.toString(); //Returns the error related
		}
	}

	/* 	Gets all the lists created by user in userList table, 
	 * NO RECIPE INFO ONLY LIST NAME AND ID
	 * Calls buildListObject() 
	 * @param  username user
	 * @return JSON Object containing all the list created by user */
	public JSONObject getUserList(String username) {
		String sql = "select * from userList where username = ?";
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, username);
			ResultSet rs = st.executeQuery();
			
			JSONObject response= new JSONObject();
			JSONArray userList = new JSONArray(); 
			while (rs.next()) {
				userList.put(buildListObject (rs.getString(2),rs.getInt(3), username ));	
			}
		
			response.put("username", username);
			response.put("listInfo", userList);
			st.close();
		    return response;

		}catch(Exception e) {
			System.out.println(e);
			JSONObject error = new JSONObject(e);
			return error;
		}
	}
	
	/* Helper to  build List object used in listArray
	 * @param  listname listname
	 * @param  listId listId 
	 * @return JSON Object containing all the list created by user */
	JSONObject buildListObject(String listName, int listId, String username){
		   JSONObject List = new JSONObject();
		   List.put("listName," ,listName );
		   List.put("listId" ,listId);
		   List.put("listAuthor," ,username );
		   return List;
	} 
	
	/* 	Gets all the lists created by user in userList table, 
	 * ALSO CONTAINS RECIPE INFO, recipe name, author and id
	 * Calls buildListObject() 
	 * @param  username user
	 * @return JSON Object containing all the list created by user */
	public JSONObject getAllUserListInfo(String username) {
		String sql = "select * from userList where username = ?";
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, username);
			ResultSet rs = st.executeQuery();
			
			JSONObject response= new JSONObject();
			JSONArray list = new JSONArray(); 
			while (rs.next()) {
				list.put( getAllUserListInfoHelper (rs.getString(1), rs.getString(2), rs.getInt(3)) );	
			}
		
			response.put("listAuthor", username);
			response.put("listInfo", list);
			st.close();
		    return response;

		}catch(Exception e) {
			System.out.println(e);
			JSONObject error = new JSONObject(e);
			return error;
		}
	}
	/* Helper to  build List object used in getAllUserListInfo
	 * WITH RECIPE INFO, calls buildListInfoObject()
	 * @param  username username
	 * @param  listname listname
	 * @param  listId listId 
	 * @return JSON Object containing a list and the recipes in the list by a user */
	public JSONObject getAllUserListInfoHelper(String username, String listName, int listId) {
		String sql = "select * from userRecipeList where username = ? and listId = ?";
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, username);
			st.setInt(2, listId);
			ResultSet rs = st.executeQuery();
			
			JSONObject response= new JSONObject();
			JSONArray list = new JSONArray(); 
			while (rs.next()) {
				list.put(buildListInfoObject (rs.getString(2),rs.getString(3) , rs.getInt(4)  ));	
			}
			
			st.close();
			response.put("listName", listName);
			response.put("listId", listId);
			response.put("info", list);
		    return response;

		}catch(Exception e) {
			System.out.println(e);
			JSONObject error = new JSONObject(e);
			return error;
		}
	}	
	/* Helper to  build List object used in listArray
	 * @param  recipeName recipeName
	 * @param  author author
	 * @param  recipeId recipeId
	 * @return JSON Object containing a recipe in their list */
	JSONObject buildListInfoObject(String recipeName, String author, int recipeId){
		   JSONObject history = new JSONObject();
		   history .put("recipeName" ,recipeName);
		   history .put("author" ,author );
		   history .put("recipeId" ,recipeId );
		   return history;
	} 
	
	/* Gets the list indicated by the listID in userList table, 
	 * CONTAINS listname, id and the resulting recipe info, recipe name, author and id
	 * Calls buildListInfoObject() 
	 * @param  username user
	 * @return JSON Object containing lists info and the recipes in the list*/
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
				response.put("listAuthor", rs.getString(1));
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
			JSONObject error = new JSONObject(e);
			return error;
		}
	}
	/* Based on JSON info, inserts recipe into userFavorites table 
	 * @param  JSON the data inserted
	 * @return Favorite insert success/failure/error */
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
			return e.toString(); //Returns the error related
		}
	}	
	/* Based on JSON info, deletes recipe from userFavorites table 
	 * @param  JSON the data inserted
	 * @return Favorite delete success/failure/error */
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
			return "Delete from favorites success";
		}catch(Exception e) {
			System.out.println(e);
			return e.toString(); //Returns the error related
		}
	}
	/* Gets all the favorites for a user in userFavorites table
	 * CONTAINS recipe info, recipe name, author and id
	 * @param  username user
	 * @return JSON Object containing all the favorites for a user */
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
				userHistory.put(buildListInfoObject (rs.getString(2),rs.getString(3) , rs.getInt(4) ));
			}
			
			st.close();
			response.put("info", userHistory);
			response.put("username", username);
		    return response;

		}catch(Exception e) {
			System.out.println(e);
			JSONObject error = new JSONObject(e);
			return error;
		}
	}

	/* Based on JSONData, inserts list into likedtable for user
	 * @param  data data containing username, listname and listId
	 * @return List like success/delete/error */
	public String likeList(JSONObject data) {
		try {	
			String sql = "insert ignore into likedTable (username, listName, listId) value (?,?,?)";
			PreparedStatement st2 = con.prepareStatement(sql);
			st2.setString(1, data.getString("username"));
			st2.setString(2, data.getString("listName"));
			st2.setInt(3, data.getInt("listId"));
			int i= st2.executeUpdate();
			if(i==0) {
				st2.close();
				return "List is already liked by this user or the user,listName, listId doesnt exist";
			}
			st2.close();
			return "List like success";
		}catch(Exception e) {
			System.out.println(e);
			return e.toString(); //Returns the error related
		}
	}
	
	/* Based on JSONData, removes list from likedtable for user
	 * @param  data data containing username, listname and listId
	 * @return List unlike success/delete/error */
	public String unlikeList(JSONObject data) {
		try {	
			String sql = "delete from likedTable where username = ? AND  listName = ?  AND listId = ?";
			PreparedStatement st2 = con.prepareStatement(sql);
			st2.setString(1, data.getString("username"));
			st2.setString(2, data.getString("listName"));
			st2.setInt(3, data.getInt("listId"));
			int i= st2.executeUpdate();
			if(i==0) {
				st2.close();
				return "List was never liked";
			}
			st2.close();
			return "List unlike success";
		}catch(Exception e) {
			System.out.println(e);
			return e.toString(); //Returns the error related
		}
	}
	/* Based on user, returns all the list liked by that user
	 * @param  user username
	 * @return JSONArray with all of the lists info and recipes*/
	public JSONArray getlikeList(String username) {
		try {	
			String sql = "select * from likedTable where username = ?";
			PreparedStatement st2 = con.prepareStatement(sql);
			st2.setString(1, username);
			ResultSet rs= st2.executeQuery();
			
			JSONArray temp = new JSONArray();
			while (rs.next()) {
				temp.put(getUserListInfo(rs.getInt(3)));
			}
			st2.close();
			return temp;
		}catch(Exception e) {
			System.out.println(e);
			JSONArray error = new JSONArray(e);
			return error;
		}
	}
	public void closeCon() throws SQLException {
	       if(con != null) {
	           con.close();
	        }
	}
}
