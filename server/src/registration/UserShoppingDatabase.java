package registration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserShoppingDatabase {

	private Connection con= DataBaseConnector.connect();
	
	/* Based on JSON info, inserts ingredients for a user into userShoppingList table
	 * @param  data JSON containing username and ingredient
	 * @returns Success/Failure/error
	 */	
	public String insertUserShopping(JSONObject data) {
		try {	
			String sql = "insert into userShoppingList (username, ingredient) value (?,?)";
			PreparedStatement st2 = con.prepareStatement(sql);
			st2.setString(1, data.getString("username"));
			st2.setString(2, data.getString("ingredient"));
			st2.executeUpdate();
			st2.close();
			return "User Shopping insert success";
		}catch(Exception e) {
			System.out.println(e);
			return e.toString(); //Returns the error related
		}
	}
	/* Based on JSON info, gets all ingredients in specfic users Shopping List from userShoppingList table
	 * @param  username user
	 * Calls shoppingListBuilder()
	 * @returns JSONArray with ingredients and username
	 */
	public JSONArray getUserShopping(String username) {
			String sql = "select * from userShoppingList where username = ?";
			try {
				PreparedStatement st = con.prepareStatement(sql);
				st.setString(1, username);
				ResultSet rs = st.executeQuery();
				System.out.println("Getting ingredient"); //xxxxx
				JSONArray ingredientInfo = new JSONArray();
				while (rs.next()) {
					ingredientInfo.put( shoppingListBuilder (rs.getString(2)) );	
				}
				st.close();
				return ingredientInfo;
			}catch(Exception e) {
				System.out.println(e);
				JSONArray error = new JSONArray(e);
				return error;
			}
		}
	/* Helper function for getUserShopping() to build JSONObject to fill JSONArray
	 * @param  ingredient ingredient name
	 * @returns JSONObject ingredient info 
	 */
	public JSONObject shoppingListBuilder(String ingredient) {
		JSONObject ingredientO = new JSONObject();
		ingredientO.put("ingredient" , ingredient);
		return ingredientO; 
	}
	
	/* Based on JSON info, deletes ingredients for a user from userShoppingList table
	 * @param  username user
	 * @param ingredient ingredient name
	 * @returns Delete Success /Failure/error
	 */
	public String deleteUserShopping(String username, String ingredient) {
		String sql = "delete from userShoppingList where username=? and ingredient=?";
		try {	
			
			PreparedStatement st2 = con.prepareStatement(sql);
			st2.setString(1, username);
			st2.setString(2, ingredient);
			int deleted = st2.executeUpdate();
			if (deleted == 0) {
				return "Nothing was deleted";
			}
			st2.close();
			return "User Shopping delete success";
		}catch(Exception e) {
			System.out.println(e);
			return e.toString(); //Returns the error related
		}
	}
}
