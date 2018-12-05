package registration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import API.APIDatabase;
import API.EdamamAPICall;

public class RecipeDatabase {
	
	private Connection con= DataBaseConnector.connect();
	
	/* Take a JSON with all the recipe info and inserts it into recipe table
	 * Also uses insertNutrient() and insertIngredient() to insert into nutrient and ingredient table
	 * @param  data  JSONObject with all the recipe info
	 * @return the recipeId to be used in UserRecipe Table   */
	public int insertrecipe(JSONObject data) {
		try {
			String sql = "insert into recipe (label, description, image, url, servings, "
					+ "calories, totalTime) value (?,?,?,?,?,?,?)";
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, data.getString("label"));
			st.setString(2, data.getString("description"));
			st.setString(3, data.getString("image"));
			st.setString(4, data.getString("url"));
			st.setInt(5, data.getInt("servings"));
			st.setDouble(6, data.getDouble("calories"));
			st.setInt(7, data.getInt("totalTime"));
			st.executeUpdate();
			st.close();
			//Gets the last recipeId
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
	
	/* Take a JSON with nutrient info and inserts it into nutrients table
	 * @param  data  JSONObject with nutrient info  */
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
		
	/* Take a JSON with Ingredient info and inserts it into ingredients table
	 * @param  data  JSONarray with Ingredient info */
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
				st.setInt(2, temp.getInt("weight"));
				st.setString(3, temp.getString("text"));
				st.executeUpdate();
			}
			st.close();
		}catch(Exception e){
			System.out.println(e);
		}
	}
	/* Returns the recipe and all of its info from recipe table based on the recipeId 
	 * Also uses getNutrient and getIngredient() to retrieve respective info
	 * @param  recipeId  the recipeId
	 * @returns a Recipe jsonobject based on recipeId
	 */
	public JSONObject getRecipe(int recipeId) {
		String sql = "select * from recipe where recipeId = ?";
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, recipeId);
			ResultSet rs = st.executeQuery();
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
			
			sql = "select * from userRecipes where recipe_Id = ?";
			st = con.prepareStatement(sql);
			st.setInt(1, recipeId);
			rs = st.executeQuery();
			if(!rs.next()) {
				recipeInfo.put("author", "n/a");
			}else {
				recipeInfo.put("author", rs.getString(1));
			}
			st.close();

			sql = "select * from recipeInfo where recipeId = ?";
			st = con.prepareStatement(sql);
			st.setInt(1, recipeId);
			rs = st.executeQuery();
			if(!rs.next()) {
				recipeInfo.put("rating", -1);
			}
			else{
				recipeInfo.put("rating", rs.getDouble(4));
			}
			
			st.close();

			//Puts the nutrients objects and ingredients array
			recipeInfo.put("nutrients", getNutrientInfo(recipeId));
			recipeInfo.put("ingredients", getIngredientInfo(recipeId));
		    return recipeInfo;

		}catch(Exception e) {
			System.out.println(e);
			JSONObject error = new JSONObject(e);
			return error;
		}
	}

	/* Returns nutrient info for a recipe from nutrient table based on the recipeId 
	 * @param  recipeId  the recipeId
	 * @returns a nutrient jsonobject based on recipeId
	 */
	public JSONObject getNutrientInfo(int recipeId) {
		JSONObject nutrientInfo = new JSONObject();
		String sql = "select * from nutrients where NutrientsId = ?";
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, recipeId);
			ResultSet rs = st.executeQuery();
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
	/* Returns ingredient info for a recipe from ingredients table based on the recipeId 
	 * @param  recipeId  the recipeId
	 * @returns a  ingredient jsonarray based on recipeId
	 */
	public JSONArray getIngredientInfo(int recipeId) {
		String sql = "select * from ingredients where recipeId = ?";
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, recipeId);
			ResultSet rs = st.executeQuery();
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
	/* Helper function to build the ingredient array, this creates the JSONObjects within it 
	 * @param  ingredient ingredient name
	 * @param  amount  amount of ingredient
	 * @returns a ingredient jsonobject ingredient and amount
	 */
	public JSONObject ingredientBuilder(String ingredient, int amount) {
		JSONObject ingredientO = new JSONObject();
		ingredientO.put("text" ,ingredient);
		ingredientO.put("weight" ,amount);
		return ingredientO;
	}

	/* Deletes the recipe entirely from database
	 * @param  recipeId  the recipeId to be deleted
	 * @returns Delete sucess/failure or error
	 */
	public String deleteRecipe(int recipeid) {
		System.out.println(recipeid);
		try {
			String sql = "delete from recipe where recipeId = ?";
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, recipeid);
			int i = st.executeUpdate();
			if( i==0) {
				return "Nothing was deleted";//Doesnt exist
			}
			else {
				sql = "delete from userRecipeList where recipeId = ?";
				st = con.prepareStatement(sql);
				st.setInt(1, recipeid);
				st.executeUpdate();
				
				sql = "delete from userFavorites where recipeId = ?";
				st = con.prepareStatement(sql);
				st.setInt(1, recipeid);
				st.executeUpdate();
				
				sql = "delete from recipeInfo where recipeId = ?";
				st = con.prepareStatement(sql);
				st.setInt(1, recipeid);
				st.executeUpdate(); 
				
				sql = "delete from recipeRating where recipeId = ?";
				st = con.prepareStatement(sql);
				st.setInt(1, recipeid);
				st.executeUpdate(); 
				
				sql = "delete from userHistory where recipeId = ?";
				st = con.prepareStatement(sql);
				st.setInt(1, recipeid);
				st.executeUpdate(); 
			}
			st.close();
			return "Recipe delete success";
		}catch(Exception e) {
			System.out.println(e);
		}
		return "Recipe delete fail";
	}
	/* Updates the recipe in recipe table and calls 
	 * updateNutrient and updateIngredient to update them in their respective tables
	 * @param  recipeId  the recipeId to be updated
	 * @param  data the JSON containing updated data
	 * @returns Update sucess/failure or error
	 */
	public String updateRecipe(int recipeid, JSONObject data) {
		try {	
			String sql = "UPDATE recipe SET label= ?, description =?, image = ?, url = ?, servings = ?, calories = ?, totalTime = ? where recipeId = ?";
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, data.getString("label"));
			st.setString(2, data.getString("description"));
			st.setString(3, data.getString("image"));
			st.setString(4, data.getString("url"));
			st.setInt(5, data.getInt("servings"));
			st.setDouble(6, data.getDouble("calories"));
			st.setInt(7, data.getInt("totalTime"));
			st.setInt(8, recipeid);
			int i = st.executeUpdate();
			if( i==0) {
				return "Nothing can be updated, this recipe doesn't exist";
			}
			else {
				updateNutrient(recipeid, data.getJSONObject("nutrients"));
				updateIngredient(recipeid, data.getJSONArray("ingredients"));
			}
			st.close();
			return "Recipe update success";
		}catch(Exception e) {
			System.out.println(e);
			return e.toString(); //Returns the error related
		}
	}
	
	/* Updates the nutritional info in nutrient table
	 * @param  recipeId  the recipeId to be updated
	 * @param  data the JSON containing updated data
	 * @returns Update sucess/failure or error
	 */
	public void updateNutrient(int recipeid, JSONObject data) {
		try {	
			String sql = "delete from nutrients where nutrientsId = ?";
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, recipeid);
			st.executeUpdate();
			st.close();
			//Rather than updating, just delete and reinsert
			insertNutrient(recipeid, data);
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	/* Updates the ingredient info in ingredient table
	 * @param  recipeId  the recipeId to be updated
	 * @param  data the JSON containing updated data
	 * @returns Update sucess/failure or error
	 */
	public void updateIngredient(int recipeid, JSONArray data) {
		try {	
			String sql = "delete from ingredients where recipeId = ?";
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, recipeid);
			st.executeUpdate();
			st.close();
			//Rather than updating it, delete and reinserts the new info
			insertIngredient(recipeid, data);
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	/* Passes the info from JSON to a helper to search for recipes in both the database and the API
	 * @param  data the JSON containing search param data
	 * @returns A JSON containing recipes from both the database and API
	 */
	public JSONObject DBAPISearchEx(JSONObject data) throws JSONException, IOException {
		//todo : for better searching
		int size = data.getInt("size");
		String q = data.getString("search");

		return DBAPISearchExHelper(size, q);
	}
	/* Searches the database then the API for recipes with those terms or ingredients based on size and search
	 * NOTE this can take multiple search terms as long as they are seperated by %20
	 * @param  size the max number of recipes to be returned
	 * @param  search string with terms that needs to be parsed to search database and passed to the API
	 * @returns A JSON containing recipes from both the database and API matching search
	 */
	public JSONObject DBAPISearchExHelper(int size, String search) throws JSONException, IOException {
	//50% of the recipes will come from the database and the API
	 int tempSize = size/2;
	 String [] words = search.split("%20");
	 String sql="SELECT distinct recipeId FROM ingredients WHERE ingredients in (";
	 int length = words.length;
	 for(int i =0; i< length ; i++) {
		sql += "'"+ words[i] + "'," ;
	 }
	 sql =  sql.substring(0, sql.length() - 1);
	 sql += ") LIMIT " + tempSize;
	 JSONArray temp = new JSONArray();
	try {
		PreparedStatement st = con.prepareStatement(sql);
		ResultSet rs = st.executeQuery();
		while(rs.next()) {
			temp.put( getRecipe( rs.getInt(1) ) );
		}
	}catch(Exception e) {
		JSONObject error = new JSONObject(e);
		return error;
	}
		JSONObject temp1 = new JSONObject();
		temp1.put("DatabaseRecipes", temp);
		//If there isn't enough recipes from database the rest are from API
		size -= temp1.getJSONArray("DatabaseRecipes").length();
		//Updates the JSON with recipes from API
		return EdamamAPICall.search(size, search, temp1);
	}
	
	/* Searches the database then the API for recipes, for matching a ingredient/terms based on size and search 
	 * BUT ONLY takes one word terms  
	 * @param  size the max number of recipes to be returned
	 * @param  search string with a term/ingredient to search database, and then passed to the API search
	 * @returns A JSON containing recipes from both the database and API
	 */
	public JSONObject apiSearch(int size, String q) throws JSONException, IOException {
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
				recipe.append("DatabaseRecipes", getRecipe(rs.getInt(1)));
			}
			st.close();
			if(recipe.has("DatabaseRecipes")) {
				size -= recipe.getJSONArray("DatabaseRecipes").length();
			}
		}catch(Exception e) {
			System.out.println(e);
			JSONObject error = new JSONObject(e);
			return error;
		}	
		try {
			sql = "Select r.recipeID from APIrecipe r join APIdata A on r.recipeId = A.recipeId "
					+ "where A.searchString = \"" + q 
					+"\" limit "
					+ size;
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				recipe.append("APIRecipes", APIDatabase.getEdamamRecipe(rs.getInt(1)));
			}
			st.close();
			if(recipe.has("APIRecipes")) {
				size -= recipe.getJSONArray("APIRecipes").length();
			}
		}catch(Exception e) {
			System.out.println(e);
			JSONObject error = new JSONObject(e);
			return error;
		}
		return size==0?recipe:EdamamAPICall.search(size, q, recipe);
	}
	
	/* Passes the info from JSON to a helper to search for recipes in ONLY the database
	 * @param  data the JSON containing search param data
	 * @returns A JSON containing recipes ONLY from the database
	 */
	public JSONObject databaseStrictSearch(JSONObject data) throws JSONException, IOException {
		//todo : for better searching
		int size = data.getInt("size");
		String q = data.getString("search");

		return getRecipesFromDatabaseStrict(size, q);
	}
	/* Searches ONLY the database for matching ingredient/terms based on size and search 
	 * RECIPES MUST MATCH ALL INGREDIENTS, RECIPES CANNOT HAVE EXTRA, BUT CAN HAVE FROM ONE TO ALL THE INGREDIENTS  
	 * @param  size the max number of recipes to be returned
	 * @param  search string with terms/ingredients to search database,
	 * @returns A JSON containing recipes from both the database and API
	 */
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
			 JSONObject temp1 = new JSONObject();
			 temp1.put("DatabaseRecipes", temp);
			return temp1;
		}catch(Exception e) {
			JSONObject error = new JSONObject(e);
			return error;
		}
	}
}