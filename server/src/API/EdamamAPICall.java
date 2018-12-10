package API;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import registration.DataBaseConnector;

public class EdamamAPICall {
	final String APPKEY = "9f7f5f81a5d43726ab9b7ca292d7e583";
	final String APPID = "ac3847c7";
    final String APIURL ="https://api.edamam.com/search";
    public static JSONObject search(int size, String q, JSONObject newRecipeArray ) throws JSONException, IOException, SQLException {
    	try {
    	HttpURLConnection con = connect(size,q);
    	//read all string from con
    	BufferedReader in = new BufferedReader(
    			new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
        	response.append(inputLine);
        }
        in.close(); //end the read
        con.disconnect();
        JSONObject myResponse = new JSONObject(response.toString());
        JSONArray recipeArray = new JSONArray();
        recipeArray = myResponse.getJSONArray("hits");
        return getRecipes(size, recipeArray, newRecipeArray, q);
    	}
    	catch(Exception e) {
    		String error1= "{\"Error\": \"" + e + "\"}";
			JSONObject error = new JSONObject(error1);
			return error;
		}
    }
    
    //connect to recipe search api to edamam
    public static HttpURLConnection connect(int size, String q) {
    	HttpURLConnection con=null;
    	try {
    		//todo : change url for search every things
    		final String APPKEY = "9f7f5f81a5d43726ab9b7ca292d7e583";
    		final String APPID = "ac3847c7";
    	    final String APIURL ="https://api.edamam.com/search";
   	 		String url = APIURL + "?q=" + q + "&app_id=" + APPID + "&app_key=" + APPKEY + "&from=0&to=" + size;
   	 		URL obj = new URL(url);
   	 		con = (HttpURLConnection) obj.openConnection();
   	 		con.setRequestMethod("GET");
   	 		con.setRequestProperty("Content-Type", "application/json");
   	 		con.setRequestProperty("Accept", "application/json");
   	 		return con;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return null;
    }
    
    public static JSONObject getRecipes(int size, JSONArray recipeArray, JSONObject newRecipeArray, String q) throws JSONException, SQLException {
        Connection con= DataBaseConnector.connect();
        try {
    	for(int i =0; i < size && i < recipeArray.length(); i++) {
	   	 	JSONObject temp = new JSONObject(recipeArray.getJSONObject(i).getJSONObject("recipe").toString());
	   	 	JSONObject recipe = new JSONObject();
	   	 	recipe.accumulate("label", temp.getString("label"));
	   	 	recipe.accumulate("description", "");
	   	 	recipe.accumulate("url", temp.getString("url"));
	   	 	recipe.accumulate("servings", temp.getInt("yield"));
	   	 	recipe.accumulate("image", temp.getString("image"));
	   	 	recipe.accumulate("calories", temp.getDouble("calories"));
	   	 	recipe.accumulate("totalTime", temp.getDouble("totalTime"));
	   	 	recipe.accumulate("author", temp.getString("source"));
	   	 	recipe.accumulate("recipeId", -1);
	   	 	try {
	   	 	String sql = "select * from recipeInfo where recipe_name = ? AND author = ?";
	   	 	PreparedStatement st  = con.prepareStatement(sql);
			st.setString(1,  temp.getString("label"));
			st.setString(2, temp.getString("source"));
			ResultSet rs = st.executeQuery();
			if(!rs.next()) {
				recipe.accumulate("rating", -1);
			}
			else{
				recipe.accumulate("rating", rs.getDouble(4));
			}
	   	 	recipe.put("ingredients", temp.getJSONArray("ingredients"));
	   	 	recipe.accumulate("nutrients", getNutrients(temp));
	   	 	
	   	 	st.close();
	   	 	rs.close();
			}catch(Exception e) {
			System.out.println(e);
		}
	   	 	//APIDatabase.insertEdamamRecipe(recipe, q);
	   	 	newRecipeArray.append("APIRecipes", recipe);
	    }
	    //System.out.print(newRecipeArray)
	    return newRecipeArray;
        }
        finally {
        	con.close();
        }
	}
    
	//setup all nutrients from edamam
    public static JSONObject getNutrients(JSONObject recipe) throws JSONException {
		JSONObject temp = new JSONObject(recipe.getJSONObject("totalNutrients").toString());
		JSONObject nutrients = new JSONObject();
		nutrients.accumulate("fat", getNutrient(temp, "FAT"));
		nutrients.accumulate("carbs", getNutrient(temp, "CHOCDF"));
		nutrients.accumulate("fiber", getNutrient(temp, "FIBTG"));
		nutrients.accumulate("sugar",  getNutrient(temp, "SUGAR"));
		nutrients.accumulate("protein",  getNutrient(temp, "PROCNT"));
		nutrients.accumulate("cholesterol",  getNutrient(temp, "CHOLE"));
		nutrients.accumulate("sodium",  getNutrient(temp, "NA"));
		//System.out.println(nutrients + "\n");
		return nutrients;
    }
    
    //check if nutrients have the key of different nutrients
	public static double getNutrient(JSONObject Nutrients, String nutrientName) throws JSONException {
		if(Nutrients.has(nutrientName)) {
			return Nutrients.getJSONObject(nutrientName).getDouble("quantity");
		}else {
			return 0;
		}
	}
}
