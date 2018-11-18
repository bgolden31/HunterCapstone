package registration;

public class Recipehistory {
	private int recipe_id;
	private char relationship;
	private java.util.Date date;

	public int getRecipe_id(){
		return recipe_id;
	}

	public void setRecipe_id(int recipe_id){
		this.recipe_id=recipe_id;
	}

	public char getRelationship(){
		return relationship;
	}

	public void setRelationship(char relationship){
		this.relationship=relationship;
	}

	public java.util.Date getDate(){
		return date;
	}

	public void setDate(java.util.Date date){
		this.date=date;
	}
}
