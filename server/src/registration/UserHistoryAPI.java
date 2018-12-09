package registration;

import java.sql.SQLException;

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
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String x() {
		System.out.print("start");
		return "GET success";
	}
	/*Takes JSON containing user and recipe info and insert it into userHistory table
	 * @param  data the data inserted
	 * @return Insert success/failure/error   
	 *Input form 
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
	public String insertUserHistory (String data) throws SQLException{
		UserHistoryDatabase dataBase = new UserHistoryDatabase();
		try {
		JSONObject temp = new JSONObject(data);
		return dataBase.insertUserHistory(temp);
		}
		finally {
			dataBase.closeCon();
		}
	}
	
	/* Gets a username and returns their entired viewed history from UserHistory table
	 * @param  username user
	 * @return JSONArray of all the recipes viewed by user   */
	@Path("get/{username}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getUserHistory (@PathParam("username") String username) throws SQLException {
		UserHistoryDatabase dataBase = new UserHistoryDatabase();
		try {
		return dataBase.getUserHistory(username).toString();
		}
		finally {
			dataBase.closeCon();
		}
	}
	/* Deletes recipe from userHistory based on username and recipename
	 * @param  username user
	 * @param  recipename recipename
	 */
	//Use /delete/{username}?recipe=a
	@Path("delete/{username}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteUserHistory (@PathParam("username")String username, @QueryParam("recipe")String recipe) throws SQLException {
		UserHistoryDatabase dataBase = new UserHistoryDatabase();
		try {
		return dataBase.deleteUserHistory(username, recipe).toString();
		}
		finally {
			dataBase.closeCon();
		}
	}
}
