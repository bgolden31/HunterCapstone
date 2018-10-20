package registration;

public class Nutrient {
	private int nutrients_id;
	private double fat;
	private double sugar;
	private double protein;
	private double fiber;
	private double sodium;
	private double cholesterol;
	private double carbs;

	public int getNutrients_id(){
		return nutrients_id;
	}

	public void setNutrients_id(int nutrients_id){
		this.nutrients_id=nutrients_id;
	}
	public double getFat(){
		return fat;
	}

	public void setFat(double fat){
		this.fat=fat;
	}

	public double getSugar(){
		return sugar;
	}

	public void setSugar(double sugar){
		this.sugar=sugar;
	}

	public double getProtein(){
		return protein;
	}

	public void setProtein(double protein){
		this.protein=protein;
	}

	public double getFiber(){
		return fiber;
	}

	public void setFiber(double fiber){
		this.fiber=fiber;
	}

	public double getSodium(){
		return sodium;
	}

	public void setSodium(double sodium){
		this.sodium=sodium;
	}

	public double getCholesterol(){
		return cholesterol;
	}

	public void setCholesterol(double cholesterol){
		this.cholesterol=cholesterol;
	}

	public double getCarbs(){
		return carbs;
	}

	public void setCarbs(double carbs){
		this.carbs=carbs;
	}
}

