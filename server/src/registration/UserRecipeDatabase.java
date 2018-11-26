package registration;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.json.JSONArray;

public class UserRecipeDatabase {
	private Connection con= DataBaseConnector.connect();
	/* Returns all the recipes created by user, based on username
	 * 
	 */
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
	/* Inserts into userRecipe table, with recipeId and username
	 */
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
