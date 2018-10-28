package registration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

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
			String sql = "Select count(*) from recipe";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			int recipeId = 1;
			if(rs.next()) {
				recipeId = rs.getInt(1) + 1;
			}
			//System.out.println(recipeId);
			//System.out.println(data.getString("label") + " || " + data.getString("description")  + " || " +  data.getString("image") + " || " +  data.getString("URL") + " || " + data.getInt("servings") + " || " +  data.getDouble("calories") + " || " + data.getInt("totalTime"));
			st.close();
			sql = "insert into recipe (recipeId, nutrientsId, label, description, image, url, servings, "
					+ "calories, totalTime) value (?,?,?,?,?,?,?,?,?)";
			PreparedStatement st2 = con.prepareStatement(sql);
			st2.setInt(1, recipeId);
			st2.setInt(2, recipeId);
			st2.setString(3, data.getString("label"));
			st2.setString(4, data.getString("description"));
			st2.setString(5, data.getString("image"));
			st2.setString(6, data.getString("URL"));
			st2.setInt(7, data.getInt("servings"));
			st2.setDouble(8, data.getDouble("calories"));
			st2.setInt(9, data.getInt("totalTime"));
			st2.executeUpdate();
			st2.close();
			
			insertNutrient(recipeId, data.getJSONObject("nutrients"));
			insertIngredient(recipeId, data.getJSONArray("ingredients"));
			return "Recipe insert success";
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
		
	// daven add end	10/20/18
	//register,1. get add username from database table, 2.compare to user input, 3. register, or reject
	public String insertrecipe(Recipe recipe) {
		//List<String> name = new ArrayList<>(); 
		String sql = "select * from recipe where recipeId = ?";
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, recipe.getrecipeId());
			ResultSet rs = st.executeQuery();
			if(rs.next())
				return "This recipe already exists";
			sql = "insert into recipe (recipeId, nutrientsId, label, description, servings, calories, totalTime) value (?,?,?,?,?,?,?)";
			st = con.prepareStatement(sql);
			st.setInt(1, recipe.getrecipeId() );
			st.setInt(2, recipe.getnutrientsId() );
			st.setString(3, recipe.getlabel() );
			st.setString(4, recipe.getdescription() );
			st.setInt(5, recipe.getservings() );
			st.setDouble(6, recipe.getcalories() );
			st.setInt(7, recipe.gettotalTime() );
			st.executeUpdate();
			st.close();
			return "Recipe insert success";
		}catch(Exception e) {
			System.out.println(e);
		}
		return "Recipe insert fail";
	}

	//login 
	public Recipe getRecipe(int recipeId) {
		// TODO Auto-generated method stub
		System.out.println(recipeId);
		Recipe temp = new Recipe();
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
			System.out.println("1:"+rs.getInt(5) + " 2 :"+rs.getDouble(6)); 
			temp.setRecipeId(rs.getInt(1));
			temp.setnutrientsId(rs.getInt(2));
			temp.setlabel(rs.getString(3));
			temp.setdescription(rs.getString(4));
			temp.setservings(rs.getInt(7));
			temp.setcalories(rs.getDouble(8));
			temp.settotalTime(rs.getInt(9));
			st.close();
			return temp;
		}catch(Exception e) {
			System.out.println(e);
		}		
		return null;
	}
}
