package registration;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

@Path("UserFridge")
public class UserFridgeAPI {
	UserFridgeDatabase dataBase = new UserFridgeDatabase();
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String x() {
		System.out.print("start");
		return "GET success";
	}

	//Gets a json containing user and recipe info and inserts it into UserIngredient table
	/*Input form 
	 * {
	 * 		"username": "cal",
			"ingredient" : "a", 
		}
	 */
	@Path("insert")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String insertUserHistory (String data){
		JSONObject temp = new JSONObject(data);
		return dataBase.insertUserIngredient(temp);
	}
	
	//Gets a username and returns their entired fridge from UserIngredient table
	@Path("get/{username}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getUserHistory (@PathParam("username") String username) {
		return dataBase.getUserIngredient(username).toString();
	}
	//Deletes from UserIngredient based on username and id
	@Path("delete")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteUserHistory (String data) {
		JSONObject temp = new JSONObject(data);
		return dataBase.deleteUserIngredient(temp).toString();
	}
}
