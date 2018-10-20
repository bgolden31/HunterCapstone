package registration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RecipeDatabase {
	
	private Connection con=null;
	
	//connect to database
	public RecipeDatabase(){
		con = DataBaseConnector.connect(con);
	}
	
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
