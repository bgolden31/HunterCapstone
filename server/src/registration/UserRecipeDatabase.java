package registration;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserRecipeDatabase {
	private Connection con=null;

	//connect to database
	public UserRecipeDatabase(){
		con = DataBaseConnector.connect(con);
	}
	
	public JSONArray getUserRecipes(String username){
		String sql = "select * from userRecipes where username = ?";
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, username);
			ResultSet rs = st.executeQuery();
			JSONArray  userRecipes = new JSONArray(); 
			RecipeDatabase dataBase = new RecipeDatabase();
			while (rs.next()) {
				userRecipes.put( dataBase.getRecipe (rs.getInt(2)));	
			}
			st.close();
			return userRecipes;
		}catch(Exception e) {
			System.out.println(e);
		}		
		return null;
	}
	
	public void insertUserRecipes(int  recipeID, String username){
		String sql = "insert into userRecipes (username,  recipe_id) value (?,?)";
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, username);
			st.setInt(2,  recipeID);
			st.executeUpdate();
			st.close();
		}catch(Exception e) {
			System.out.println(e);
		}		
	}
	
	
	
	
	
}
