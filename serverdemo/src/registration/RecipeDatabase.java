package registration;

import API.EdamamAPICall;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import API.EdamamAPICall;

public class RecipeDatabase {
	
	private Connection con=null;
	
	//connect to database
	public RecipeDatabase(){
		con = DataBaseConnector.connect(con);
	}
	
	// daven add start 10/20/18
	
	public String insertrecipe(JSONObject data) {
		//List<String> name = new ArrayList<>(); 
		//Recipe recipe = new Recipe();
		//System.out.println(data);
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
			if(rs.next()) {
			insertNutrient(rs.getInt(1), data.getJSONObject("nutrients"));
			insertIngredient(rs.getInt(1), data.getJSONArray("ingredients"));
			return "Recipe insert success";
			}
		}catch(Exception e) {
			System.out.println(e);
		}
		return "Recipe insert fail";
	}
	
	//insert the nutrients
		public void insertNutrient(int id, JSONObject data) {
			try {
				if(data.length() == 0)
					return ;
				//System.out.println(data.getDouble("fat") + " || " +data.getDouble("sugar"));
				String sql = "insert into nutrient (nutrientsId, fat, sugar, protein, fiber, "
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
				String sql = "insert into ingredient (recipeId, weight, ingredients) value (?,?,?)";
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

	//login 
	public JSONObject getRecipe(int recipeId) {
		System.out.println(recipeId);
		String sql = "select * from recipe where recipeId = ?";
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, recipeId);
			ResultSet rs = st.executeQuery();
			//System.out.println("Getting recipe"); //xxxxx
			if(!rs.next()) {
				System.out.println(!rs.next());
				return null;
			}
			//System.out.println("lol"); 
			JSONObject recipeInfo = new JSONObject();
			recipeInfo.put("recipeId", recipeId);
			recipeInfo.put("label", rs.getString(2));
			recipeInfo.put("description", rs.getString(3));
			recipeInfo.put("image", rs.getString(4));
			recipeInfo.put("URL",rs.getString(5));
			recipeInfo.put("servings", rs.getInt(6));
			recipeInfo.put("calories", rs.getDouble(7));
			recipeInfo.put("totalTime", rs.getInt(8));
			st.close();

			recipeInfo.put("nutrient", getNutrientInfo(recipeId));
			recipeInfo.put("ingredient", getIngredientInfo(recipeId));
		    return recipeInfo;

		}catch(Exception e) {
			System.out.println(e);
		}		
		return null;
	}


	public JSONObject getNutrientInfo(int recipeId) {
		JSONObject nutrientInfo = new JSONObject();
		String sql = "select * from nutrient where NutrientsId = ?";
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, recipeId);
			ResultSet rs = st.executeQuery();
			//System.out.println("Getting nutrient"); //xxxxx
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

	public JSONArray getIngredientInfo(int recipeId) {
		String sql = "select * from ingredient where recipeId = ?";
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, recipeId);
			ResultSet rs = st.executeQuery();
			//System.out.println("Getting ingredient"); //xxxxx
			//if(!rs.next()) {
			//	System.out.println(!rs.next());
			//	return null;
			//}
			JSONArray ingredientInfo = new JSONArray();
			while (rs.next()) {
				JSONObject temp = new JSONObject();
				temp.accumulate("name", rs.getString(3));
				temp.accumulate("weight", rs.getInt(1));
				ingredientInfo.put(temp);	
			}
			st.close();
			return ingredientInfo;
		}catch(Exception e) {
			System.out.println(e);
		}		
		return null;
	}
	
	public JSONObject getRecipes(int size, String q) throws JSONException, IOException {
		//todo : for better searching
		String sql = "Select r.recipeID from recipe r "
				+ "join nutrient n on r.recipeid = n.nutrientsid "
				+ "join ingredient i on r.recipeId = i.recipeid"
				+ " where ingredients = \"" + q 
				+"\" limit "
				+ size;
				//+"\" desc limit " + size/2;
		JSONObject recipe = new JSONObject();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				recipe.append("recipes", getRecipe(rs.getInt(1)));
			}
			st.close();
			size -= recipe.getJSONArray("recipes").length();
			System.out.println(recipe.getJSONArray("recipes").length());
		}catch(Exception e) {
			System.out.println(e);
		}	
		return EdamamAPICall.search(size, q, recipe);
	}
}

