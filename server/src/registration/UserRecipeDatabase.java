package registration;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.json.JSONArray;
import org.json.JSONObject;

public class UserRecipeDatabase {
	private Connection con= DataBaseConnector.connect();
	
	/* Searches userRecipe table returns all the recipes created by a user based on username
	 * Uses getRecipe() to search recipe table
	 * @param  username user
	 * @return JSONARay with all the recipes created by that user   */
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
			JSONArray error = new JSONArray(e);
			return error;
		}		
	}

	/* Upon recipe creation and insertion,this records the recipeID in userRecipe table
	 * @param  username user
	 * @param  recipeId recipeId*/
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
