package registration;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Path("recipe")
public class RecipeAPI {
	RecipeDatabase dataBase = new RecipeDatabase();
	UserRecipeDatabase UserRecipeDatabase = new UserRecipeDatabase();
	UserDatabase UserDatabase = new UserDatabase();
	RatingDatabase RecipeInfoDatabase = new RatingDatabase();
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String x() {
		System.out.print("start");
		return "GET success";
	}
		
	/**
	 * Take a JSON inserts it into recipe table
	 * Also inserts it into userRecipe table, with recipeID from the recipeInsert()
	 * @param  data  JSONObject with all the recipe info
	 * @return Insert success/failure or the error
	 * 
	 * Requires JSON in this form
	 * {
	    "username": "ex",
	    "label" : "aaaaaaaaaa",
	    "description" : "howmayfirerice in the library",
	    "image" : "hwihwieiqoejqwondijenfewinvmsov.com",
	    "URL" : "adasdhweidewbfyuwiebvbyurv.com",
	    "servings" : 33,
	    "calories" : 2.3,
	    "totalTime" : 5 ,
	    "nutrients" : {
	        "fat" : 2.1,
	        "sugar" : 4.5,
	        "protein" : 6.6,
	        "fiber" : 4.4,
	        "sodium" : 5.5,
	        "cholesterol" : 7.7,
	        "carbs" : 3.4
	    },
	    "ingredients" : [
	       {"text" : "rice", "weight": 2 },
	       {"text" : "egg", "weight": 2 }
	    ]
		}
	 * 
	 */
	@Path("insert")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String insertRecipe (String data){
		JSONObject temp = new JSONObject(data);
		String username= temp.getString("username");
		if(UserDatabase.checkUser(username)) {
			int recipeID= dataBase.insertrecipe(temp);
			if (recipeID == -1) {
				return "Recipe insert fail";
			}
			UserRecipeDatabase.insertUserRecipes(recipeID, username);
			return "recipe Insert success";
		}
		return "User does not exist";
	}
	/* Returns json with recipe info based on recipeID
	 * Calls the getRecipe() function 
	 * @param  recipeId the recipeId in database
	 * @return JSONObject with that recipe */
	@Path("get/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getRecipe (@PathParam("id") int recipeid) {
		return dataBase.getRecipe(recipeid).toString();
	}
	/* Updates a recipe based on recipeID with new JSONObject info 
	 * Calls the updateRecipe() function 
	 * @param  recipeId the recipeId in database
	 * @return Update Success/failure/error
	 * 
	 * @JSONFormat same as insert */
	@Path("update/{id}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String updateRecipe (@PathParam("id") int recipeid, String data) {
		JSONObject temp = new JSONObject(data);
		return dataBase.updateRecipe(recipeid, temp);
	}
	/* Deletes a recipe based on recipeID
	 * Calls the deleteRecipe() function, will delete every trace of the recipe in database 
	 * @param  recipeId the recipeId in database
	 * @return Delete Success/failure/error
	 */	
	@Path("delete/{id}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteRecipe (@PathParam("id") int recipeid) {
		return dataBase.deleteRecipe(recipeid);
	}
	/* Gets all the recipes created by that user
	 * Calls the getUserRecipe() function 
	 * @param  username the user's name
	 * @return JSON with all the user's recipes
	 */	
	@Path("userRecipe/{username}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getUserRecipe (@PathParam("username") String username)  { 
		UserRecipeDatabase userrecipes = new UserRecipeDatabase();
		JSONArray temp =  userrecipes.getUserRecipes(username);
		return temp.toString();
	}
	/* Updates the rating of a recipes for that user based on JSON info
	 * Calls the updateRecipeInfo() function 
	 * @param  data JSON containing the rating and recipe info
	 * @return Sucess/Failure/Error and the updated rating
	 * Input form:
	 * {
		    "username": "cal",
		    "recipeName": "a",
		    "author" : "a",
		    "recipeId": 49,
		    "rating" : 5
		}
	 */
	@Path("update/rating")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String getRecipeRating (String data)  { 
		JSONObject temp = new JSONObject(data);
		return RecipeInfoDatabase.updateRecipeInfo(temp) + RecipeInfoDatabase.updateRating(temp);
	}
	/* Deletes the rating of a recipes for that user based on JSON info
	 * Calls the deleterating and updateRating() function 
	 * @param  data JSON containing the rating and recipe info
	 * @return Sucess/Failure/Error and the updated rating
	 * 
	 * SAME JSON FORMAT AS UPDATE/RATING
	 * */
	@Path("delete/rating")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteRecipeRating (String data)  { 
		JSONObject temp = new JSONObject(data);
		String temp1= RecipeInfoDatabase.deleteRecipeInfo(temp);
		if (temp1== "RecipeRating was removed completely") {
			return "RecipeRating was removed completely, because it only had one or no ratings";
		}
		return  temp1 + RecipeInfoDatabase.updateRating(temp);
	}
	/* Gets the rating of a recipes based on recipe Id only works for database recipes
	 * Calls the getRecipeInfo() function 
	 * @param  recipeId the recipe id 
	 * @return JSON with recipename author id and rating
	 */
	@Path("get/rating/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getRating (@PathParam("id") int recipeId)  { 
		return RecipeInfoDatabase.getRecipeInfo(recipeId);
	}
	/* Gets the rating of a recipes based on recipename and author
	 * Calls the getRecipeInfo() function 
	 * @param  recipename recipe's name 
	 * @param  author  recipes author
	 * @return JSON with recipename author id and rating
	 */
	@Path("get/rating/{recipe}/{author}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getRating (@PathParam("recipe")String recipeName, @PathParam("author")String author)  { 
		return RecipeInfoDatabase.getRecipeInfo(recipeName, author);
	}
	
	/* Gets all the recipes in DATABASE AND API based on search params, recipes have extra ingredients
	 * Calls the DBAPISearchEx() function 
	 * @param  JSON with search size and search terms
	 * @return JSON with all the recipes in DATABASE AND API based on search params, recipes have extra ingredients  
	   Input form:
		 * {
				"size" : 29,
				"search" : "chicken"
			}
			FOR 1+ WORD SEARCH USE %20 ie "chicken%20rice"
	*/
	@Path("search")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String databaseSearchEx (String data) throws JSONException, IOException { 
		JSONObject temp = new JSONObject(data);
		return dataBase.DBAPISearchEx(temp).toString();
	}
	/* Gets all the recipes that username can make based on their Fridge, recipes have extra ingredients  
	 * Calls the getUserIngredient to get all their ingredients and build them into on string and passes it to DBAPISearchEx() 
	 * @param  username the users whose fridge will be searched
	 * @param  size the max number of recipes returned
	 * @return JSON with all the recipes in DATABASE AND API based on users fridge, recipes have extra ingredients  
	*/
	//Use path recommended/{username}?size=10
	@Path("recommended/{username}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String recommendedSearchEx (@PathParam("username") String username, @QueryParam("size")int size) throws JSONException, IOException { 
		UserFridgeDatabase IngredientdataBase= new UserFridgeDatabase();
		JSONObject Fridge = IngredientdataBase.getUserIngredient(username);
		JSONArray Ingredient= Fridge.getJSONArray("fridge");
		String ingredients ="";
		for(int n = 0; n < Ingredient.length(); n++)
		{
		    JSONObject object = Ingredient.getJSONObject(n);
		    ingredients += object.get("ingredient") + "%20";
		}
		String data = "{ size:"+ size + ", search: \"" + ingredients + "\" }";
		JSONObject temp = new JSONObject(data);
		return dataBase.DBAPISearchEx(temp).toString();
	}

	//UNUSED?
	/* Gets all the recipes in DATABASE AND API based on search params, recipes have extra ingredients
	 * Calls the apiSearch() function, only one word searches allowed
	 * @param  JSON with search size and search terms
	 * @return JSON with all the recipes in DATABASE AND API based on search params, recipes have extra ingredients  
	/* Input form:
		 * {
				"size" : 29,
				"search" : "chicken"
			}
	*/
	@Path("search/api")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String searchRecipe (String data) throws JSONException, IOException { 
		JSONObject temp = new JSONObject(data);
		int size = temp.getInt("size");
		String q = temp.getString("search");
			return dataBase.apiSearch(size, q).toString();
	}
	/* Gets all the recipes in DATABASE AND API based on search params, recipes DO NOT have extra ingredients 
	 * Calls the apiSearch() function, only one word searches allowed
	 * @param  JSON with search size and search terms
	 * @return JSON with all the recipes in DATABASE AND API based on search params, recipes DONT HAVE extra ingredients   
	/* Input form:
		 * {
				"size" : 29,
				"search" : "chicken"
			}
			FOR 1+ WORD SEARCH USE %20 ie "chicken%20rice"
	*/
	@Path("search/strict/database")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String databaseSearch (String data) throws JSONException, IOException { 
		JSONObject temp = new JSONObject(data);
		return dataBase.databaseStrictSearch(temp).toString();
	}
	/* Gets all the recipes that username can make based on their Fridge, recipes DONT HAVE extra ingredients  
	 * Calls the getUserIngredient to get all their ingredients and build them into on string and passes it to databaseStrictSearch() 
	 * @param  username the users whose fridge will be searched
	 * @return JSON with all the recipes in DATABASE AND API based on users fridge, recipes have DONT HAVE ingredients  
	*/
	//Use path recommended/strict/{username}?size=10
	@Path("recommended/strict/{username}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String recommendedStrictSearch (@PathParam("username") String username, @QueryParam("size")int size) throws JSONException, IOException { 
		UserFridgeDatabase IngredientdataBase= new UserFridgeDatabase();
		JSONObject Fridge = IngredientdataBase.getUserIngredient(username);
		JSONArray Ingredient= Fridge.getJSONArray("fridge");
		String ingredients ="";
		for(int n = 0; n < Ingredient.length(); n++)
		{
		    JSONObject object = Ingredient.getJSONObject(n);
		    ingredients += object.get("ingredient") + "%20";
		}
		String data = "{ size:"+ size + ", search: \"" + ingredients + "\" }";
		System.out.println(data);
		JSONObject temp = new JSONObject(data);
		return dataBase.databaseStrictSearch(temp).toString();
	}
}