package registration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserFridgeDatabase {

	private Connection con= DataBaseConnector.connect();

	/* Based on JSON info, inserts ingredients for a user into UserFridge table
	 * @param  data JSON containing username and ingredient
	 * @returns Success/Failure/error
	 */	
	public String insertUserIngredient(JSONObject data) {
		try {	
			String sql = "insert ignore into userFridge (username, ingredient) value (?,?)";
			PreparedStatement st2 = con.prepareStatement(sql);
			st2.setString(1, data.getString("username"));
			st2.setString(2, data.getString("ingredient"));
			st2.executeUpdate();
			st2.close();
			return "User Ingredient insert success";
		}catch(Exception e) {
			System.out.println(e);
			return e.toString(); //Returns the error related
		}
	}
	
	/* Based on JSON info, gets all ingredients in specfic users Fridge from UserFridge table
	 * @param  username user
	 * Calls ingredientListBuilder()
	 * @returns JSONArray with ingredients and username
	 */	
	public JSONObject getUserIngredient(String username) {
		String sql = "select * from userFridge where username = ?";
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, username);
			ResultSet rs = st.executeQuery();
			JSONArray ingredientInfo = new JSONArray();
			while (rs.next()) {
				ingredientInfo.put( ingredientListBuilder (rs.getString(2)) );	
			}
			JSONObject info = new JSONObject();
			info.put("username", username);
			info.put("fridge", ingredientInfo);
			st.close();
			return info;
		}catch(Exception e) {
			System.out.println(e);
			JSONObject error = new JSONObject(e);
			return error;
		}
	}
	/* Helper function for getUserIngredients() to build JSONObject to fill JSONArray
	 * @param  ingredient ingredient name
	 * @returns JSONObject ingredient info 
	 */
	public JSONObject ingredientListBuilder(String ingredient) {
		JSONObject ingredientO = new JSONObject();
		ingredientO.put("ingredient" , ingredient);
		   return ingredientO; 
	}
	
	/* Based on JSON info, deletes ingredients for a user from UserFridge table
	 * @param  username user
	 * @param ingredient ingredient name
	 * @returns Delete Success /Failure/error
	 */	
	public String deleteUserIngredient(String username, String ingredient) {
		String sql = "delete from userFridge WHERE username=? AND ingredient=?";
		try {	
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, username);
			st.setString(2, ingredient);
			int i = st.executeUpdate();
			if( i == 0 ) {
				return "Nothing to delete";
			}
			st.close();
			return "User Ingredient delete success";
		}catch(Exception e) {
			System.out.println(e);
			return e.toString(); //Returns the error related
		}
	}
}
