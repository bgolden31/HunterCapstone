package registration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import API.APIDatabase;
import API.EdamamAPICall;

public class RecipeDatabase {
	
	private Connection con= DataBaseConnector.connect();
	
	/*connect to database
	public RecipeDatabase(){
		con = DataBaseConnector.connect(con);
	}*/
	
	//Takes Json containing recipe info to insert to recipe table
	//Returns the recipeId to be used in UserRecipe Table
	public int insertrecipe(JSONObject data) {
		try {	
			String sql = "insert into recipe (label, description, image, url, servings, "
					+ "calories, totalTime) value (?,?,?,?,?,?,?)";
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, data.getString("label"));
			st.setString(2, data.getString("description"));
			st.setString(3, data.getString("image"));
			st.setString(4, data.getString("URL"));
			st.setInt(5, data.getInt("servings"));
			st.setDouble(6, data.getDouble("calories"));
			st.setInt(7, data.getInt("totalTime"));
			st.executeUpdate();
			st.close();
			
			sql = "select LAST_INSERT_ID()";
			Statement st2 = con.createStatement();
			ResultSet rs = st2.executeQuery(sql);
			
			//Insert "nutrients" object and "ingredients" array into nutrients and ingredients table
			if(rs.next()) {
			insertNutrient(rs.getInt(1), data.getJSONObject("nutrients"));
			insertIngredient(rs.getInt(1), data.getJSONArray("ingredients"));
			return rs.getInt(1);
			}
		}catch(Exception e) {
			System.out.println(e);
		}
		return -1; //Return -1 if it fails
	}
	
	//insert the nutrients
		public void insertNutrient(int id, JSONObject data) {
			try {
				if(data.length() == 0)
					return ;
				//System.out.println(data.getDouble("fat") + " || " +data.getDouble("sugar"));
				String sql = "insert into nutrients (nutrientsId, fat, sugar, protein, fiber, "
						+ "sodium, cholesterol, carbs) value (?,?,?,?,?,?,?,?)";
				PreparedStatement st = con.prepareStatement(sql);
				st.setInt(1, id);
				st.setDouble(2, data.getDouble("fat"));
				st.setDouble(3, data.getDouble("sugar"));
				st.setDouble(4, data.getDouble("protein"));
				st.setDouble(5, data.getDouble("fiber"));
				st.setDouble(6, data.getDouble("sodium"));
				st.setDouble(7, data.getDouble("cholesterol"));
				st.setDouble(8, data.getDouble("carbs"));
				st.executeUpdate();
				st.close();
			}catch(Exception e){
				System.out.println(e);
			}
		}
		
		//insert the ingredients
		public void insertIngredient(int id, JSONArray data) {
			//System.out.println("data : " + data );
			try {
				if(data.length() == 0)
					return ;
				String sql = "insert into ingredients (recipeId, weight, ingredients) value (?,?,?)";
				PreparedStatement st = con.prepareStatement(sql);
				for(int i = 0; i< data.length(); i++) {
					JSONObject temp = data.getJSONObject(i);
					st.setInt(1, id);
					st.setInt(2, temp.getInt("amount"));
					st.setString(3, temp.getString("name"));
					st.executeUpdate();
				}
				st.close();
			}catch(Exception e){
				System.out.println(e);
			}
		}

	//Returns a recipe json based on recipeId
	public JSONObject getRecipe(int recipeId) {
		System.out.println(recipeId);
		String sql = "select * from recipe where recipeId = ?";
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, recipeId);
			ResultSet rs = st.executeQuery();
			System.out.println("Getting recipe"); //xxxxx
			if(!rs.next()) {
				System.out.println(!rs.next());
				return null;
			}
			JSONObject recipeInfo = new JSONObject();
			recipeInfo.put("recipeId", recipeId);
			recipeInfo.put("label", rs.getString(2));
			recipeInfo.put("description", rs.getString(3));
			recipeInfo.put("image", rs.getString(4));
			recipeInfo.put("url",rs.getString(5));
			recipeInfo.put("servings", rs.getInt(6));
			recipeInfo.put("calories", rs.getDouble(7));
			recipeInfo.put("totalTime", rs.getInt(8));
			st.close();

			//Puts the nutrients objects and ingredients array
			recipeInfo.put("nutrients", getNutrientInfo(recipeId));
			recipeInfo.put("ingredient", getIngredientInfo(recipeId));
		    return recipeInfo;

		}catch(Exception e) {
			System.out.println(e);
		}		
		return null;
	}

	//Returns a nutrient json based recipeId
	public JSONObject getNutrientInfo(int recipeId) {
		JSONObject nutrientInfo = new JSONObject();
		String sql = "select * from nutrients where NutrientsId = ?";
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, recipeId);
			ResultSet rs = st.executeQuery();
			System.out.println("Getting nutrient"); //xxxxx
			if(!rs.next()) {
				System.out.println(!rs.next());
				return null;
			}
			nutrientInfo.put("nutrientId", rs.getInt(1));
			nutrientInfo.put("fat", rs.getDouble(2));
			nutrientInfo.put("sugar", rs.getDouble(3));
			nutrientInfo.put("protein", rs.getDouble(4));
			nutrientInfo.put("fiber", rs.getDouble(5));
			nutrientInfo.put("sodium", rs.getDouble(6));
			nutrientInfo.put("cholesterol", rs.getDouble(7));
			nutrientInfo.put("carbs", rs.getDouble(8));
			st.close();
		}catch(Exception e) {
			System.out.println(e);
		}
		return nutrientInfo;
	}
	//Returns a ingredient array based recipeId
	public JSONArray getIngredientInfo(int recipeId) {
		String sql = "select * from ingredients where recipeId = ?";
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, recipeId);
			ResultSet rs = st.executeQuery();
			System.out.println("Getting ingredient"); //xxxxx
			JSONArray ingredientInfo = new JSONArray();
			while (rs.next()) {
				ingredientInfo.put( ingredientBuilder( rs.getString(3),rs.getInt(1) ) );	
			}
			st.close();
			return ingredientInfo;
		}catch(Exception e) {
			System.out.println(e);
		}		
		return null;
	}
	
	public JSONObject ingredientBuilder(String ingredient, int amount) {
		JSONObject ingredientO = new JSONObject();
		ingredientO.put("name" ,ingredient);
		ingredientO.put("amount" ,amount);
		return ingredientO;
	}

	//Deletes the recipe from recipe table based on recipeId
	public String deleteRecipe(int recipeid) {
		try {	
			String sql = "delete from recipe where recipeId = ?";
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, recipeid);
			st.executeUpdate();
			st.close();
			return "Recipe delete success";
		}catch(Exception e) {
			System.out.println(e);
		}
		return "Recipe delete fail";
	}
	//Updates the recipe from recipe table based on recipeId
	public String updateRecipe(int recipeid, JSONObject data) {
		try {	
			String sql = "UPDATE recipe SET label= ?, description =?, image = ?, URL = ?, servings = ?, calories = ?, totalTime = ? where recipeId = ?";
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, data.getString("label"));
			st.setString(2, data.getString("description"));
			st.setString(3, data.getString("image"));
			st.setString(4, data.getString("URL"));
			st.setInt(5, data.getInt("servings"));
			st.setDouble(6, data.getDouble("calories"));
			st.setInt(7, data.getInt("totalTime"));
			st.setInt(8, recipeid);
			st.executeUpdate();
			st.close();
	
			updateNutrient(recipeid, data.getJSONObject("nutrients"));
			updateIngredient(recipeid, data.getJSONArray("ingredients"));
			
			return "Recipe update success";
		}catch(Exception e) {
			System.out.println(e);
		}
		return "Recipe delete fail";
	}
	/*Updates the nutritional info from nutrient table based on recipeId
	Rather than updating it, it just deletes and reinserts the new info */
	public void updateNutrient(int recipeid, JSONObject data) {
		try {	
			String sql = "delete from nutrients where nutrientsId = ?";
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, recipeid);
			st.executeUpdate();
			st.close();
			
			insertNutrient(recipeid, data);
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	/*Updates the ingredient info from ingredient table based on recipeId
	Rather than updating it, it just deletes and reinserts the new info*/
	public void updateIngredient(int recipeid, JSONArray data) {
		try {	
			String sql = "delete from ingredients where recipeId = ?";
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, recipeid);
			st.executeUpdate();
			st.close();
			insertIngredient(recipeid, data);
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	/*Takes search parameters from JSON and passes it to EdamamAPICall.search to search on the third party API
	 */
	/*Takes search parameters from JSON and passes it to EdamamAPICall.search to search on the third party API
	 */
	public JSONObject apiSearch(int size, String q) throws JSONException, IOException {
		//todo : for better searching
		String sql = "Select r.recipeID from recipe r "
				+ "join nutrients n on r.recipeid = n.nutrientsid "
				+ "join ingredients i on r.recipeId = i.recipeid"
				+ " where ingredients = \"" + q 
				+"\" limit "
				+ size/2;
		JSONObject recipe = new JSONObject();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				recipe.append("recipes", getRecipe(rs.getInt(1)));
			}
			st.close();
			if(recipe.has("recipes")) {
				size -= recipe.getJSONArray("recipes").length();
			}
		}catch(Exception e) {
			System.out.println(e);
		}	
		try {
			sql = "Select r.recipeID from APIrecipe r join APIdata A on r.recipeId = A.recipeId "
					+ "where A.searchString = \"" + q 
					+"\" limit "
					+ size;
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				recipe.append("recipes", APIDatabase.getEdamamRecipe(rs.getInt(1)));
			}
			st.close();
			if(recipe.has("recipes")) {
				size -= recipe.getJSONArray("recipes").length();
			}
		}catch(Exception e) {
			System.out.println(e);
		}
		return size==0?recipe:EdamamAPICall.search(size, q, recipe);
	}
	
	/*Takes search parameters from JSON and passes it to EdamamAPICall.search to search on the third party API
	 */
	public JSONObject databaseStrictSearch(JSONObject data) throws JSONException, IOException {
		//todo : for better searching
		int size = data.getInt("size");
		String q = data.getString("search");

		return getRecipesFromDatabaseStrict(size, q);
	}
	
	public JSONObject getRecipesFromDatabaseStrict(int size, String search) throws JSONException, IOException {
	//todo : for better searching
	 String [] words = search.split("%20");
	 String sql="SELECT distinct recipeId FROM ingredients WHERE ingredients in (";
	 int length = words.length;
	 for(int i =0; i< length ; i++) {
		sql += "'"+ words[i] + "'," ;
	 }
	 sql =  sql.substring(0, sql.length() - 1);
	 
	 sql += ") and recipeId not in (SELECT recipeId FROM ingredients WHERE ingredients not in (";
	 for(int i =0; i< length ; i++) {
			sql += "'"+ words[i] + "'," ;
		 }
	 sql =  sql.substring(0, sql.length() - 1);
	 sql += ")) LIMIT " + size;
	 
	 JSONArray temp = new JSONArray();
	try {
		PreparedStatement st = con.prepareStatement(sql);
		ResultSet rs = st.executeQuery();
		while(rs.next()) {
			temp.put( getRecipe( rs.getInt(1) ) );
		}
		 System.out.println(sql);
		 JSONObject temp1 = new JSONObject();
		 temp1.put("recipe", temp);
		return temp1;
	}catch(Exception e) {
		System.out.println(e);
	}
	return null;
	}	/*Takes search parameters from JSON and passes it to EdamamAPICall.search to search on the third party API
	 */
	public JSONObject databaseSearchEx(JSONObject data) throws JSONException, IOException {
		//todo : for better searching
		int size = data.getInt("size");
		String q = data.getString("search");

		return getRecipesFromDatabaseEx(size, q);
	}
	
	public JSONObject getRecipesFromDatabaseEx(int size, String search) throws JSONException, IOException {
	//todo : for better searching
	 String [] words = search.split("%20");
	 String sql="SELECT distinct recipeId FROM ingredients WHERE ingredients in (";
	 int length = words.length;
	 for(int i =0; i< length ; i++) {
		sql += "'"+ words[i] + "'," ;
	 }
	 sql =  sql.substring(0, sql.length() - 1);
	 sql += ") LIMIT " + size;
	 
	 JSONArray temp = new JSONArray();
	try {
		PreparedStatement st = con.prepareStatement(sql);
		ResultSet rs = st.executeQuery();
		while(rs.next()) {
			temp.put( getRecipe( rs.getInt(1) ) );
		}
		 System.out.println(sql);
		 JSONObject temp1 = new JSONObject();
		 temp1.put("recipe", temp);
		return temp1;
	}catch(Exception e) {
		System.out.println(e);
	}
	return null;
	}
}