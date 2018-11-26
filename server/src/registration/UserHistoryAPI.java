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

@Path("UserHistory")
public class UserHistoryAPI {
	UserHistoryDatabase dataBase = new UserHistoryDatabase();
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String x() {
		System.out.print("start");
		return "GET success";
	}

	//Gets a json containing user and recipe info and inserts it into UserHistory table
	/*Input form 
	 * {
	 * 		"username": "cal",
			"recipeName" : "a", 
			"author" : "a"
			"recipeId" : 1
		}
		
		 //Put recipeid -1 if its is from api
	 */
	@Path("insert")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String insertUserHistory (String data){
		JSONObject temp = new JSONObject(data);
		return dataBase.insertUserHistory(temp);
	}
	
	//Gets a username and returns their entired viewed history from UserHistory table
	@Path("get/{username}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getUserHistory (@PathParam("username") String username) {
		return dataBase.getUserHistory(username).toString();
	}
	//Deletes from userHistory based on username and recipename 
	//Use /delete/{username}?recipe=a
	@Path("delete/{username}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteUserHistory (@PathParam("username")String username, @QueryParam("recipe")String recipe) {
		return dataBase.deleteUserHistory(username, recipe).toString();
	}
}