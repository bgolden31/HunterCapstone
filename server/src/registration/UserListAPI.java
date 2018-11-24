package registration;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

@Path("list")
public class UserListAPI {
	UserListDatabase dataBase = new UserListDatabase();
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String x() {
		System.out.print("start");
		return "GET success";
	}
	//Creates a list
	/*
	 * {"username": "cal",
		"listName" : "a"
	}
	 */
	@Path("create")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String createList (String data){
		JSONObject temp = new JSONObject(data);
		return dataBase.createList(temp);
	}
	//Deletes a list
	/*
	 * {"username": "cal",
		"listName" : "a",
		"listId": 1
	}
	*/
	@Path("delete")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteList (String data) {
		JSONObject temp = new JSONObject(data);
		return dataBase.deleteList(temp);
	}
	//Inserts a recipe into a list based on listId
	/*
		 * {"username": "cal",
			"recipeName" : "a", 
			"author" : "a",
			"recipeId" : -1,
			"listId" : 1
		}
	 */
	@Path("insert")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String insertToList (String data){
		JSONObject temp = new JSONObject(data);
		return dataBase.insertToList(temp);
	}
	
	//Removes from a list based on listId
	/*
		 * {"username": "cal",
			"recipeName" : "a", 
			"author" : "a",
			"recipeId" : -1,
			"listId" : 1
		}
	 */
	@Path("remove")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteFromList (String data) {
		JSONObject temp = new JSONObject(data);
		return dataBase.deletefromList(temp);
	}
	//Gets all the list name and ids based on username
	@Path("getList/{username}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getUserList (@PathParam("username")String username) {
		return dataBase.getUserList(username).toString();
	}
	//Gets a a list based on id, returns only listnames and listId
	@Path("getListInfo/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getUserListInfo (@PathParam("id") int id) {
		return dataBase.getUserListInfo(id).toString();
	}
	//Get all the list and the recipes in the list based on username
	@Path("getListInfoAll/{username}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllUserList (@PathParam("username")String username) {
		return dataBase.getAllUserListInfo(username).toString();
	}
	//Inserts a recipe into favorites
	/*
		 * {"username": "cal",
			"recipeName" : "a", 
			"author" : "a",
			"recipeId" : -1
		}
	 */
	@Path("favorites/insert")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String favoritesInsert (String data) {
		JSONObject temp = new JSONObject(data);
		return dataBase.favoriteInsert(temp).toString();
	}
	//Deletes a recipe from favorites
	/*
		 * {"username": "cal",
			"recipeName" : "a", 
			"author" : "a",
			"recipeId" : -1
		}
	 */
	@Path("favorites/delete")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public String favoritesDelete (String data) {
		JSONObject temp = new JSONObject(data);
		return dataBase.favoriteDelete(temp).toString();	
	}
	//Gets all the favor
	@Path("favorites/get/{username}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String favoritesGet (@PathParam("username")String username) {
		return dataBase.getUserFavorites(username).toString();
	}
}
