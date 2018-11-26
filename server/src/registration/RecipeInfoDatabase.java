package registration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONObject;

public class RecipeInfoDatabase {

	private Connection con= DataBaseConnector.connect();
	
	public String updateRecipeInfo(JSONObject data) {
		String sql = "select * from recipeRating where username =? AND recipe_name = ? AND  author = ? AND recipeId = ?";
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, data.getString("username"));
			st.setString(2, data.getString("recipeName"));
			st.setString(3, data.getString("author"));
			st.setInt(4, data.getInt("recipeId"));
			ResultSet rs = st.executeQuery();
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
			return "RecipeRating insert success";
		}catch(Exception e) {
			System.out.println(e);
		}
		return "Recipe rating insert fail";
	}
	public String updateRating(JSONObject data) {
		try {	
			String sql = "select * from recipeRating where recipe_name = ? AND  author = ? AND recipeId = ?";
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, data.getString("recipeName"));
			st.setString(2, data.getString("author"));
			st.setInt(3, data.getInt("recipeId"));
			ResultSet rs = st.executeQuery();
			double sum = 0;
			double count=0;
			while(rs.next()) {
				sum = sum + rs.getFloat("rating"); 
				count++;
			}
			double average = sum/count;
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
			return "RecipeRating insert success" + Double.toString(average) ;
		}catch(Exception e) {
			System.out.println(e);
		}
		return "Recipe rating insert fail";
	}
	public String deleteRecipeInfo(JSONObject data) {
		String sql = "delete from recipeRating where username = ? AND recipe_name = ? AND  author = ? AND recipeId = ?";
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, data.getString("username"));
			st.setString(2, data.getString("recipeName"));
			st.setString(3, data.getString("author"));
			st.setInt(4, data.getInt("recipeId"));
			st.executeUpdate();
			
			sql = "select * from recipeRating where recipeId = ?";
			PreparedStatement st2 = con.prepareStatement(sql);
			st2.setInt(1, data.getInt("recipeId"));
			ResultSet rs = st2.executeQuery();
			if (!rs.next()) {
				System.out.println("DELETED");
				sql = "delete from recipeInfo WHERE recipe_name = ? AND  author = ? AND recipeId = ?";
			    	PreparedStatement  st3 = con.prepareStatement(sql);
					st3.setString(1, data.getString("recipeName"));
					st3.setString(2, data.getString("author"));
					st3.setInt(3, data.getInt("recipeId"));
					st3.executeUpdate();
					return "RecipeRating was removed completely";
			}
			return "RecipeRating delete success";
		}catch(Exception e) {
			System.out.println(e);
		}
		return "RecipeRating delete fail";
	}
	
	public String getRecipeInfo(JSONObject data) {
		String sql = "SELECT * FROM recipeInfo WHERE recipe_name = ? AND author = ? AND recipeId = ?";
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, data.getString("recipeName"));
			st.setString(2, data.getString("author"));
			st.setInt(3, data.getInt("recipeId"));
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				JSONObject recipeInfo = new JSONObject();
				recipeInfo.put("recipeName", rs.getString(1));
				recipeInfo.put("author", rs.getString(2));
				recipeInfo.put("recipeId", rs.getInt(3));
				recipeInfo.put("rating",rs.getDouble(4));
				st.close();
				return recipeInfo.toString();
			}
		}catch(Exception e) {
			System.out.println(e);
		}
		return "RecipeRating delete fail";
	}
}
