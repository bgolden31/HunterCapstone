package registration;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

@Path("recipe")
public class RecipeAPI {
	RecipeDatabase dataBase = new RecipeDatabase();
	
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
		return dataBase.insertrecipe(temp);
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
	
	//11/7/2018
	//for search API, when front end give the name, we need to return a list of recipe to them
	@Path("search")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String searchRecipe (String data) throws JSONException, IOException { 
		JSONObject temp = new JSONObject(data);
		int size = temp.getInt("size");
		String q = temp.getString("search");
		return dataBase.getRecipes(size, q).toString();
	}
	
}
