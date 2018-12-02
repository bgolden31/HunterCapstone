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
	
	RecipeInfoDatabase RecipeInfoDatabase = new RecipeInfoDatabase();
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String x() {
		System.out.print("start");
		return "GET success";
	}
		
	/* Call to insert json containing recipe info into SQL database
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
	       {"name" : "rice", "amount": 2 },
	       {"name" : "egg", "amount": 2 }
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
		int recipeID= dataBase.insertrecipe(temp);
		if (recipeID == -1) {
			return "Recipe insert fail";
		}
		UserRecipeDatabase.insertUserRecipes(recipeID, username);
		return "recipe Insert success";
	}
	//end
	
	/* Returns json with recipe info, 
	 * Input form:
	 * {
			"recipeId" : 29
		}
	 */
	@Path("get")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getRecipe (String data) {
		JSONObject temp = new JSONObject(data);
		int recipe= temp.getInt("recipeId");
		return dataBase.getRecipe(recipe).toString();
	}
	
	/* Takes {id} as recipeId and a json with recipe info in order to update
	 * Same form as insert
	 */
	@Path("update/{id}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String updateRecipe (@PathParam("id") int recipeid, String data) {
		JSONObject temp = new JSONObject(data);
		return dataBase.updateRecipe(recipeid, temp);
	}
	
	//Deletes recipe with recipeId == {id}
	@Path("delete/{id}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteRecipe (@PathParam("id") int recipeid) {
		return dataBase.deleteRecipe(recipeid);
	}

	//for search API, when front end give the name, we need to return a list of recipe to them
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
	// Gets all the recipes in database based on search params, recipes do not have extra ingredients  
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
	// Gets all the recipes that username can make based on their Fridge, recipes do not have extra ingredients  
	@Path("recommended/strict/{username}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String recommendedStrictSearch (@PathParam("username") String username) throws JSONException, IOException { 
		UserFridgeDatabase IngredientdataBase= new UserFridgeDatabase();
		JSONObject Fridge = IngredientdataBase.getUserIngredient(username);
		JSONArray Ingredient= Fridge.getJSONArray("fridge");
		String ingredients ="";
		for(int n = 0; n < Ingredient.length(); n++)
		{
		    JSONObject object = Ingredient.getJSONObject(n);
		    ingredients += object.get("ingredient") + "%20";
		    // do some stuff....
		}
		
		
		String data = "{ size:30, search: \"" + ingredients + "\" }";
		System.out.println(data);
		JSONObject temp = new JSONObject(data);
		return dataBase.databaseStrictSearch(temp).toString();
	}
	// Gets all the recipes in DATABASE AND API based on search params, recipes have extra ingredients  
	/* Input form:
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
	// Gets all the recipes that username can make based on their Fridge, recipes will have can extra ingredients  
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
		System.out.println(data);
		JSONObject temp = new JSONObject(data);
		return dataBase.DBAPISearchEx(temp).toString();
	}
	
	
	// Gets all the recipes based on username
	@Path("userRecipe/{username}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getUserRecipe (@PathParam("username") String username)  { 
		UserRecipeDatabase userrecipes = new UserRecipeDatabase();
		JSONArray temp =  userrecipes.getUserRecipes(username);
		return temp.toString();
	}
	/* Gives a recipe a rating for that user and returns overall rating
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
	/* Deletes recipe rating for that user and returns overall rating
	 * Input form:
	 * {
		    "username": "cal",
		    "recipeName": "a",
		    "author" : "a",
		    "recipeId": 49,
		    "rating" : 5
		}
	 */
	@Path("delete/rating")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteRecipeRating (String data)  { 
		JSONObject temp = new JSONObject(data);
		String temp1= RecipeInfoDatabase.deleteRecipeInfo(temp);
		if (temp1== "RecipeRating was removed completely") {
			return "RecipeRating was removed completely";
		}
		return  temp1 + RecipeInfoDatabase.updateRating(temp);
	}
	/* Returns json with overall recipe rating, 
	 * Input form:
	 * {
		    "recipeName": "a",
		    "author" : "a",
		    "recipeId": 49,
		}
	 */
	@Path("get/rating")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String getRating (String data)  { 
		JSONObject temp = new JSONObject(data);
		return RecipeInfoDatabase.getRecipeInfo(temp);
	}
}