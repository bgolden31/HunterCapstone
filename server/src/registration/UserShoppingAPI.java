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

@Path("UserShopping")
public class UserShoppingAPI {
	UserShoppingDatabase dataBase = new UserShoppingDatabase();
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String x() {
		System.out.print("start");
		return "GET success";
	}

	//Gets a json containing user and recipe info and inserts it into UserShopping table
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
		return dataBase.insertUserShopping(temp);
	}
	
	//Gets a username and returns their entired Shopping list from UserShopping table
	@Path("get/{username}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getUserHistory (@PathParam("username") String username) {
		return dataBase.getUserShopping(username).toString();
	}
	//Deletes from UserShopping based on username and id
	//Use /delete/{username}?ingredient=c
	@Path("delete/{username}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteUserHistory (@PathParam("username")String username, @QueryParam("ingredient")String ingredient ) {
		System.out.println(username);
		System.out.println(ingredient);
		return dataBase.deleteUserShopping(username, ingredient).toString();
	}
}
