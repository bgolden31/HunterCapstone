package registration;

import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONObject;

@Path("user")
public class UserAPI {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String x() {
		System.out.print("start");
		return "GET success";
	}
	
	/* Returns users info, recipes they created, and their viewing history
	 * Send json in this form:
	 * {
    		"username": "example",
    		"password": "example"
		}		
	 */
	@Path("login")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String login(String data) throws SQLException{
		UserDatabase clients = new UserDatabase();
		UserRecipeDatabase userrecipes = new UserRecipeDatabase();
		UserHistoryDatabase userHistory = new UserHistoryDatabase();
		UserListDatabase userList = new UserListDatabase();
		try {
			JSONObject temp = new JSONObject(data);
			String username= temp.getString("username");
			String password= temp.getString("password"); 
			
			String response= clients.loginUser(username, password);
			if (response == "Username does not exist") {
				JSONObject combined = new JSONObject();
				combined.put("Username does not exist","a");
				return combined.toString();
			}
			else if (response == "Incorrect password") {
				JSONObject combined = new JSONObject();
				combined.put("Incorrect password","a");
				return combined.toString();
			}
			JSONObject temp2 = new JSONObject(response);
			JSONArray temp3 =  userrecipes.getUserRecipes(username);
			JSONArray temp4 = userHistory.getUserHistory(username);
			
			JSONObject temp5 = userList.getAllUserListInfo(username);
			JSONObject temp6 = userList.getUserFavorites(username);
	
			JSONArray temp7 = userList.getlikeList(username);
			JSONObject combined = new JSONObject();
			combined.put("UserInfo",temp2);
			combined.put("Recipes", temp3);
			combined.put("History", temp4);
			combined.put("List", temp5);
			combined.put("Favorites", temp6);
			combined.put("LikedList", temp7);
			return combined.toString();
		}
		finally {
			clients.closeCon();
			userrecipes.closeCon();
			userHistory.closeCon();
			userList.closeCon();
		}
	}

	/* Registers user and adds info into SQL user table
	 * Use form 
	 * {
		    "username": "ex,
		    "password": "12345",
		    "email": "xxxxxx@dd",
		    "age": 95,
		    "name": "ex"
		}
	*/
	@Path("register")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String register(String data) throws SQLException {
		UserDatabase clients = new UserDatabase();
		try {
			JSONObject temp = new JSONObject(data);
			return clients.registerUser(temp);
		}finally {
			clients.closeCon();
		}
	}
	//Delete based on username param
	@Path("delete/{username}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteUser(@PathParam("username") String username) throws SQLException {
		UserDatabase clients = new UserDatabase();
		try {
			return clients.deleteUser(username);
		}finally {
			clients.closeCon();
		}
	}
	//Takes a json of user info and username param to update
	@Path("update/{username}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public String updateUser(@PathParam("username") String username, String data) throws SQLException {
		UserDatabase clients = new UserDatabase();
		try {
			JSONObject temp = new JSONObject(data);
			return clients.updateUser(username, temp);
		}finally {
			clients.closeCon();
		}
	}
	
	@Path("refresh/{username}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String refreshUser(@PathParam("username") String username) throws SQLException {
		
		UserRecipeDatabase userrecipes = new UserRecipeDatabase();
		UserHistoryDatabase userHistory = new UserHistoryDatabase();
		UserListDatabase userList = new UserListDatabase();
		try {
		JSONArray temp3 =  userrecipes.getUserRecipes(username);
		JSONArray temp4 = userHistory.getUserHistory(username);
		
		JSONObject temp5 = userList.getAllUserListInfo(username);
		JSONObject temp6 = userList.getUserFavorites(username);
		
		JSONArray temp7 = userList.getlikeList(username);
		JSONObject combined = new JSONObject();
		combined.put("Recipes", temp3);
		combined.put("History", temp4);
		combined.put("List", temp5);
		combined.put("Favorites", temp6);
		combined.put("LikedList", temp7);
		return combined.toString();
		}finally {
			userrecipes.closeCon();
			userHistory.closeCon();
			userList.closeCon();
		}
	}
}
