/*insert, read the edamam from our database which save some edamam's recipe*/

package API;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;

import registration.DataBaseConnector;

public class APIDatabase {
	
	private static Connection con = DataBaseConnector.connect();
	
	/*public APIDatabase(){
		con = DataBaseConnector.connect(con);
	}*/
	
	/*save edamam data to APIrecipe table*/
	public static void insertEdamamRecipe(JSONObject data, String q) {
		try {
			/*JSONArray recipesArray = data.getJSONArray("recipes");
			for(int i = 0; i < recipesArray.length(); i++) {
				JSONObject temp = recipesArray.getJSONObject(i);
				System.out.println(temp.toString());*/
				String sql = "insert into APIrecipe (label, description, image, url, servings, "
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
			
				sql = "select LAST_INSERT_ID()";
				Statement st2 = con.createStatement();
				ResultSet rs = st2.executeQuery(sql);
			
			
				if(rs.next()) {
					insertSearchString(q, rs.getInt(1));
					insertEdamamNutrient(rs.getInt(1), data.getJSONObject("nutrients"));
					insertEdamamIngredient(rs.getInt(1), data.getJSONArray("ingredients"));
					st2.close();
				}
			//}
		}catch(Exception e) {
			System.out.println(e);
		} 
	}
	
		/*save edamam data to EdamamNutrient table*/
		public static void insertEdamamNutrient(int id, JSONObject data) {
			try {
				if(data.length() == 0)
					return ;
				
				String sql = "insert into APInutrients (nutrientsId, fat, sugar, protein, fiber, "
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
		/*save edamam data to EdamamIngredient table*/
		public static void insertEdamamIngredient(int id, JSONArray data) {
			try {
				if(data.length() == 0)
					return ;
				String sql = "insert into APIingredients (recipeId, weight, text) value (?,?,?)";
				PreparedStatement st = con.prepareStatement(sql);
				for(int i = 0; i< data.length(); i++) {
					JSONObject temp = data.getJSONObject(i);
					st.setInt(1, id);
					st.setDouble(2, temp.getDouble("weight"));
					st.setString(3, temp.getString("text"));
					st.executeUpdate();
				}
				st.close();
			}catch(Exception e){
				System.out.println(e);
			}
		}
		
		/*save edamam data to getRecipe table*/
		public static JSONObject getEdamamRecipe(int recipeId) {
			String sql = "select * from APIrecipe where recipeId = ?";
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

				//after get nutrients and ingredients
				recipeInfo.put("nutrient", getEdamamNutrientInfo(recipeId));
				recipeInfo.put("ingredient", getEdamamIngredientInfo(recipeId));
			    return recipeInfo;

			}catch(Exception e) {
				System.out.println(e);
			}		
			return null;
		}
		
		//get nutrients
		public static JSONObject getEdamamNutrientInfo(int recipeId) {
			JSONObject nutrientInfo = new JSONObject();
			String sql = "select fat, sugar, protein, fiber, sodium, cholesterol, carbs from APInutrients where NutrientsId = ?";
			try {
				PreparedStatement st = con.prepareStatement(sql);
				st.setInt(1, recipeId);
				ResultSet rs = st.executeQuery();
				if(!rs.next()) {
					System.out.println(!rs.next());
					return null;
				}
				nutrientInfo.put("fat", rs.getDouble(1));
				nutrientInfo.put("sugar", rs.getDouble(2));
				nutrientInfo.put("protein", rs.getDouble(3));
				nutrientInfo.put("fiber", rs.getDouble(4));
				nutrientInfo.put("sodium", rs.getDouble(5));
				nutrientInfo.put("cholesterol", rs.getDouble(6));
				nutrientInfo.put("carbs", rs.getDouble(7));
				st.close();
			}catch(Exception e) {
				System.out.println(e);
			}
			return nutrientInfo;
		}
		
		//get ingredient
		public static JSONArray getEdamamIngredientInfo(int recipeId) {
			String sql = "select * from APIingredients where recipeId = ?";
			try {
				PreparedStatement st = con.prepareStatement(sql);
				st.setInt(1, recipeId);
				ResultSet rs = st.executeQuery();
				JSONArray ingredientInfo = new JSONArray();
				while (rs.next()) {
					JSONObject temp =new JSONObject();
					temp.accumulate("amount", rs.getDouble("weight"));
					temp.accumulate("text", rs.getString("text"));
					ingredientInfo.put(temp);
				}
				st.close();
				return ingredientInfo;
			}catch(Exception e) {
				System.out.println(e);
			}		
			return null;
		}
		
		
		public static void insertSearchString(String q, int id) {
			String sql = "insert into APIdata (searchString, recipeid) value (?,?)";
			try {
				PreparedStatement st = con.prepareStatement(sql);
				st.setString(1, q);
				st.setInt(2, id);
				st.executeUpdate();
				st.close();
			}catch(Exception e){
			System.out.println(e);
			}
		}
}
