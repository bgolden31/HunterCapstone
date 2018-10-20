package registration;

public class Ingredient {
	private int weight;
	private int recipe_id;
	private String ingredients;
	
	
	public int getWeight(){
		return weight;
	}

	public void setWeight(int weight){
		this.weight=weight;
	}
	public int getRecipe_id(){
		return recipe_id;
	}

	public void setRecipe_id(int recipe_id){
		this.recipe_id=recipe_id;
	}

	public String getIngredients(){
		return ingredients;
	}

	public void setIngredients(String ingredients){
		this.ingredients=ingredients;
	}
}
