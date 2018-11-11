package registration;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

@Path("recipe")
public class RecipeAPI {
	RecipeDatabase dataBase = new RecipeDatabase();
	UserRecipeDatabase UserRecipeDatabase = new UserRecipeDatabase();
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String x() {
		System.out.print("start");
		return "GET success";
	}
	
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
	
	@Path("get")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getRecipe (String data) {
		JSONObject temp = new JSONObject(data);
		int recipe= temp.getInt("recipeId");
		return dataBase.getRecipe(recipe).toString();
	}
	
	@Path("update/{id}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String updateRecipe (@PathParam("id") int recipeid, String data) {
		JSONObject temp = new JSONObject(data);
		return dataBase.updateRecipe(recipeid, temp);
	}
	
	@Path("delete/{id}")
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteRecipe (@PathParam("id") int recipeid, String data) {
		JSONObject temp = new JSONObject(data);
		return dataBase.deleteRecipe(recipeid, temp);
	}
}
