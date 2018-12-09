package registration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

public class RatingDatabase {

	private Connection con= DataBaseConnector.connect();
	
	/* Updates the Recipes rating given A USER based on JSONObject info and adds it into recipeRating table
	 * If rating does not exist for that recipe by that user, it creates one
	 * If rating FOR THAT user exists it updates
	 * @param  data the JSON containing user, recipe name, author, rating and recipeId
	 * @returns Success/Failure/error and updated rating
	 */
	public String updateRecipeInfo(JSONObject data) throws SQLException {
		String sql = "select * from recipeRating where username =? AND recipe_name = ? AND  author = ? AND recipeId = ?";
		PreparedStatement st= null;
		ResultSet rs =null;
		try {
			st = con.prepareStatement(sql);
			st.setString(1, data.getString("username"));
			st.setString(2, data.getString("recipeName"));
			st.setString(3, data.getString("author"));
			st.setInt(4, data.getInt("recipeId"));
			rs = st.executeQuery();
			if (rs.next()) {
			    do {
			    	sql = "UPDATE recipeRating SET rating= ? where username =? AND recipe_name = ? AND  author = ? AND recipeId = ?";
			    	PreparedStatement  st2 = con.prepareStatement(sql);
					st2.setString(2, data.getString("username"));
					st2.setString(3, data.getString("recipeName"));
					st2.setString(4, data.getString("author"));
					st2.setInt(5, data.getInt("recipeId"));
					st2.setDouble(1, data.getDouble("rating"));
					st2.executeUpdate();
					st2.close();
			    } while(rs.next());
			} 
			else {
				sql = "insert into recipeRating (username, recipe_name, author, recipeId, rating) value (?,?,?,?,?)";
				PreparedStatement  st2 = con.prepareStatement(sql);
				st2.setString(1, data.getString("username"));
				st2.setString(2, data.getString("recipeName"));
				st2.setString(3, data.getString("author"));
				st2.setInt(4, data.getInt("recipeId"));
				st2.setDouble(5, data.getDouble("rating"));
				st2.executeUpdate();
				st2.close();
			}
			rs.close();
			st.close();
		}catch(Exception e) {
			System.out.println(e);
			return e.toString(); //Returns the error related
		}
		return "RecipeRating insert success";
	}
	/* Updates the overall Recipes rating based on JSONObject info and updates it in recipeInfo table
	 * If rating does not exist, it creates one and calculates accordingly
	 * If rating exists it updates
	 * @param  data the JSON containing recipe name, author, and recipeId
	 * @returns Success/Failure/error and updated rating
	 */
	public String updateRating(JSONObject data) throws SQLException {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {	
			String sql = "select * from recipeRating where recipe_name = ? AND  author = ? AND recipeId = ?";
			st = con.prepareStatement(sql);
			st.setString(1, data.getString("recipeName"));
			st.setString(2, data.getString("author"));
			st.setInt(3, data.getInt("recipeId"));
			rs = st.executeQuery();
			double sum = 0;
			double count=0;
			while(rs.next()) {
				sum = sum + rs.getFloat("rating"); 
				count++;
			}
			double average = sum/count;
			System.out.println("lol");
			sql = "SELECT * FROM recipeInfo WHERE recipe_name = ? AND author = ? AND recipeId = ?";
			st = con.prepareStatement(sql);
			st.setString(1, data.getString("recipeName"));
			st.setString(2, data.getString("author"));
			st.setInt(3, data.getInt("recipeId"));;
			rs = st.executeQuery();
			if (rs.next()) {
			    do {
			    	sql = "UPDATE recipeInfo SET rating = ? WHERE recipe_name = ? AND  author = ? AND recipeId = ?";
			    	PreparedStatement  st2 = con.prepareStatement(sql);
					st2.setString(2, data.getString("recipeName"));
					st2.setString(3, data.getString("author"));
					st2.setInt(4, data.getInt("recipeId"));
					st2.setDouble(1, average);
					
					st2.executeUpdate();
					st2.close();
			    } while(rs.next());
			}
			else {
				sql = "insert into recipeInfo (recipe_name, author, recipeId, rating) value (?,?,?,?)";
				PreparedStatement  st2 = con.prepareStatement(sql);
				st2.setString(1, data.getString("recipeName"));
				st2.setString(2, data.getString("author"));
				st2.setInt(3, data.getInt("recipeId"));
				st2.setDouble(4, average);
				st2.executeUpdate();
				st2.close();
			}
			return "RecipeRating insert success with rating " + Double.toString(average) ;
		}catch(Exception e) {
			System.out.println(e);
			return e.toString(); //Returns the error related
		}
	}

	/* Deletes the recipes rating FOR A USER based on JSONObject info from recipeRating table
	 * If rating deleted, would cause no ratings left it is removed from recipeInfo table
	 * @param  data the JSON containing recipe name, author, and recipeId
	 * @returns Success/Failure/error and updated rating
	 */
	public String deleteRecipeInfo(JSONObject data) throws SQLException {
		String sql = "delete from recipeRating where username = ? AND recipe_name = ? AND  author = ? AND recipeId = ?";
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement(sql);
			st.setString(1, data.getString("username"));
			st.setString(2, data.getString("recipeName"));
			st.setString(3, data.getString("author"));
			st.setInt(4, data.getInt("recipeId"));
			st.executeUpdate();
			
			sql = "select * from recipeRating where recipeId = ?";
			PreparedStatement st2 = con.prepareStatement(sql);
			st2.setInt(1, data.getInt("recipeId"));
			rs = st2.executeQuery();
			if (!rs.next()) {
				System.out.println("DELETED");
				sql = "delete from recipeInfo WHERE recipe_name = ? AND  author = ? AND recipeId = ?";
			    	PreparedStatement  st3 = con.prepareStatement(sql);
					st3.setString(1, data.getString("recipeName"));
					st3.setString(2, data.getString("author"));
					st3.setInt(3, data.getInt("recipeId"));
					st3.executeUpdate();
					st2.close();
					st3.close();
					return "RecipeRating was removed completely";
			}
			st2.close();
			return "RecipeRating delete success";
		}catch(Exception e) {
			System.out.println(e);
			return e.toString(); //Returns the error related
		}
	}
	//UNUSED?
	/* Deletes all the recipes info and rating from recipeRating table and recipeInfo table based on recipe info 
	 * @param  data the JSON containing recipe name, author, and recipeId
	 * @returns Success/Failure/error
	 */
	public String deleteRecipeInfoAll(JSONObject data) throws SQLException {
		try {
			String sql = "delete from recipeInfo WHERE recipe_name = ? AND  author = ? AND recipeId = ?";
	    	PreparedStatement st3 = con.prepareStatement(sql);
			st3.setString(1, data.getString("recipeName"));
			st3.setString(2, data.getString("author"));
			st3.setInt(3, data.getInt("recipeId"));
			st3.executeUpdate();
			st3.close();
			sql = "delete from recipeRating where AND recipe_name = ? AND  author = ? AND recipeId = ?";
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, data.getString("recipeName"));
			st.setString(2, data.getString("author"));
			st.setInt(3, data.getInt("recipeId"));
			st.executeUpdate();
			st.close();
			return "RecipeRatings Info was removed completely";
		}catch(Exception e) {
			System.out.println(e);
			return e.toString(); //Returns the error related
		}
	}
	
	/* Gets the recipes rating from recipeInfo table based on recipe info based on recipeId, 
	 * only works with database Recipes
	 * @param  recipeId the recipeId
	 * @returns Success/Failure/error
	 */	
	public String getRecipeInfo(int recipeId) throws SQLException {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM recipeInfo WHERE recipeId = ?";
			st = con.prepareStatement(sql);
			st.setInt(1, recipeId);
			rs = st.executeQuery();
			if(rs.next()) {
				JSONObject recipeInfo = new JSONObject();
				recipeInfo.put("recipeName", rs.getString(1));
				recipeInfo.put("author", rs.getString(2));
				recipeInfo.put("recipeId", rs.getInt(3));
				recipeInfo.put("rating",rs.getDouble(4));
				return recipeInfo.toString();
			}
			return "No ratings found";
		}catch(Exception e) {
			System.out.println(e);
			return e.toString(); //Returns the error related
		}
	}
	/* Gets the recipes rating from recipeInfo table based on username and author, 
	 * Works with database Recipes and API assuming their isnt two recipes from either with the same name and author
	 * @param  recipename the recipe name
	 * @param  author the authors name
	 * @returns Success/Failure/error
	 */	
	public String getRecipeInfo(String recipename, String author) throws SQLException {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM recipeInfo WHERE recipe_name = ? AND author = ?";
			st = con.prepareStatement(sql);
			st.setString(1, recipename);
			st.setString(2, author);
			rs = st.executeQuery();
			if(rs.next()) {
				JSONObject recipeInfo = new JSONObject();
				recipeInfo.put("recipeName", rs.getString(1));
				recipeInfo.put("author", rs.getString(2));
				recipeInfo.put("recipeId", rs.getInt(3));
				recipeInfo.put("rating",rs.getDouble(4));
				st.close();
				return recipeInfo.toString();
			}
			return "No ratings found";
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
