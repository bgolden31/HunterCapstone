package registration;

import com.mysql.cj.jdbc.Blob;

public class Recipe {
	private int recipeId;
	private int nutrientsId;
	private String label;
	private String description;
	private Blob image;
	private Blob url;
	private int servings;
	private double calories;
	private int totalTime;
	
	public  Recipe() {
		recipeId = 0;
		nutrientsId= 0;
		label="";
		description="";
		//image= 0;
		//url = 0 ;
		servings = 0;
		calories = 0;
		totalTime = 0;
	}
	
	public Recipe(int recipeId, int nutrientsId, String label, String description, int servings, 
			double calories,  int totalTime) {
		this.recipeId= recipeId;
		this.nutrientsId= nutrientsId;
		this.label= label;
		this.description= description;
		//image= 0;
		//url = 0 ;
		this.servings= servings;
		this.calories= calories;
		this.totalTime= totalTime;
		
	}
	
	public int getrecipeId() {
		return recipeId;
	}

	public void setRecipeId(int recipeId) {
		this.recipeId = recipeId;
	}
	
	public int getnutrientsId() {
		return nutrientsId;
	}

	public void setnutrientsId (int nutrientsId) {
		this.nutrientsId  = nutrientsId  ;
	}
	
	public String getlabel() {
		return label;
	}

	public void setlabel (String label ) {
		this.label = label ;
	}
	public String getdescription() {
		return description;
	}

	public void setdescription(String description ) {
		this.description  = description  ;
	}
	public int getservings() {
		return servings;
	}

	public void setservings (int servings ) {
		this.servings  = servings  ;
	}
	public double getcalories() {
		return calories ;
	}

	public void setcalories (double calories ) {
		this.calories  =calories  ;
	}
	public int gettotalTime() {
		return  totalTime;
	}

	public void settotalTime (int totalTime ) {
		this.totalTime  =totalTime  ;
	}

}
