package registration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class NutrientDatabase {
private Connection con=null;
	
	//connect to database
	public NutrientDatabase(){
		con = DataBaseConnector.connect(con);
	}
	
	//register,1. get add username from database table, 2.compare to user input, 3. register, or reject
	public String insertNutrient(Nutrient nutrient) {
		//List<String> name = new ArrayList<>(); 
		String sql = "select * from nutrient where nutrientsId = ?";
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, nutrient.getNutrientsId());
			ResultSet rs = st.executeQuery();
			if(rs.next())
				return "This Nutrient already exists";
			sql = "insert into nutrient (nutrientsId, fat, sugar, protein, fiber, "
					+ "sodium, cholesterol, carbs) value (?,?,?,?,?,?,?,?)";
			st = con.prepareStatement(sql);
			st.setInt(1, nutrient.getNutrientsId() );
			st.setDouble(2, nutrient.getFat() );
			st.setDouble(3, nutrient.getSugar() );
			st.setDouble(4, nutrient.getProtein() );
			st.setDouble(5, nutrient.getFiber() );
			st.setDouble(6, nutrient.getSodium() );
			st.setDouble(7, nutrient.getCholesterol() );
			st.setDouble(8, nutrient.getCarbs() );
			st.executeUpdate();
			st.close();
			return "Nutrient insert success";
		}catch(Exception e) {
			System.out.println(e);
		}
		return "Nutrient insert fail";
	}

	//login 
	public Nutrient getNutrient(int Nutrients_id) {
		// TODO Auto-generated method stub
		System.out.println(Nutrients_id);
		Nutrient temp = new Nutrient();
		String sql = "select * from nutrient where NutrientsId = ?";
		try {
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, Nutrients_id);
			ResultSet rs = st.executeQuery();
			System.out.println("Getting nutrient"); //xxxxx
			if(!rs.next()) {
				System.out.println(!rs.next());
				return null;
			}
			System.out.println("1 :"+rs.getInt(1) + "2 :"+rs.getInt(2)); 
			temp.setNutrientsId(rs.getInt(1));
			temp.setFat(rs.getDouble(2));
			temp.setSugar(rs.getDouble(3));
			temp.setProtein(rs.getDouble(4));
			temp.setFiber(rs.getDouble(5));
			temp.setSodium(rs.getDouble(6));
			temp.setCholesterol(rs.getDouble(7));
			temp.setCarbs(rs.getDouble(8));
			st.close();
			return temp;
		}catch(Exception e) {
			System.out.println(e);
		}		
		return null;
	}
}

