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
	/* Gets a json containing user and their ingredient and inserts it into UserIngredient table
	 * @param  JSON with search size and search terms
	 * @return Insert sucess/fail/ error
	 *Input form 
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
	
	/* Gets a username and returns their entired fridge from UserIngredient table
	 * @param username user
	 * @return JSON with their entired fridge from UserIngredient table 
	 */
	@Path("get/{username}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getUserHistory (@PathParam("username") String username) {
		return dataBase.getUserIngredient(username).toString();
	}
	/* Gets a username and deletes an ingredient from their fridge from UserIngredient table
	 * @param username user
	 * @param ingredient ingredient to delete
	 * @return Delete sucess/fail/ error
	 */
	//Use /delete/{username}?ingredient=c
	@Path("delete/{username}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteUserHistory (@PathParam("username")String username, @QueryParam("ingredient")String ingredient ) {
		return dataBase.deleteUserIngredient(username, ingredient).toString();
	}
}
