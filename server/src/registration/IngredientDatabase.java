package registration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class IngredientDatabase {
	
	private Connection con=null;
	
	//connect to database
	public IngredientDatabase(){
		con = DataBaseConnector.connect(con);
	}
	
	//register,1. get add username from database table, 2.compare to user input, 3. register, or reject
	public String insertIngredient(Ingredient Ingredient) {
		//List<String> name = new ArrayList<>(); 
		String sql = "select * from ingredient where recipeId = ?";
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, Ingredient.getRecipeId() );
			System.out.println(Ingredient.getRecipeId() );
			ResultSet rs = st.executeQuery();
			if(rs.next())
				return "This recipe already exists";
			sql = "insert into ingredient (weight, recipeId, ingredients) value (?,?,?)";
			st = con.prepareStatement(sql);
			st.setInt(1, Ingredient.getWeight() );
			st.setInt(2, Ingredient.getRecipeId() );
			st.setString(3, Ingredient.getIngredients() );
			st.executeUpdate();
			st.close();
			return "Ingredient insert success";
		}catch(Exception e) {
			System.out.println(e);
		}
		return "Ingredient insert fail";
	}

	//login 
	public Ingredient getIngredient(int recipeId) {
		// TODO Auto-generated method stub
		System.out.println(recipeId);
		Ingredient temp = new Ingredient();
		String sql = "select * from ingredient where recipeId = ?";
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, recipeId);
			ResultSet rs = st.executeQuery();
			System.out.println("Getting ingredient"); //xxxxx
			if(!rs.next()) {
				System.out.println(!rs.next());
				return null;
			}
			//System.out.println("1 :"+rs.getInt(1) + "2 :"+rs.getInt(2)); 
			temp.setWeight(rs.getInt(1));
			temp.setRecipeId(rs.getInt(2));
			temp.setIngredients(rs.getString(3));
			st.close();
			return temp;
		}catch(Exception e) {
			System.out.println(e);
		}		
		return null;
	}
}
