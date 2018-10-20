package registration;

public class Ingredient {
	private int weight;
	private int recipeId;
	private String ingredients;
	
	
	public int getWeight(){
		return weight;
	}

	public void setWeight(int weight){
		this.weight=weight;
	}
	public int getRecipeId(){
		return recipeId;
	}

	public void setRecipeId(int recipeId){
		this.recipeId=recipeId;
	}

	public String getIngredients(){
		return ingredients;
	}

	public void setIngredients(String ingredients){
		this.ingredients=ingredients;
	}
}
