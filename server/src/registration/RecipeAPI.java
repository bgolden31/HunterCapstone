package registration;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
	public String insertRecipe (Recipe data){
		return dataBase.insertrecipe(data);
	}
	
	//new 10/20/2018
	@Path("insert2")
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
	public Recipe getRecipe (Recipe data) {
		System.out.print("start");
		return dataBase.getRecipe(data.getrecipeId());
	}
}
