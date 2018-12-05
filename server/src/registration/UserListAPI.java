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
	/*Takes JSON containing list info and insert it into userList table for that user
	 * @param  data the data inserted
	 * @return List creation success/failure/error
		JSON Form
	 * {
	 * "username": "cal",
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
	/*Takes JSON containing list info and deletes it from userList table
	 * @param  data the data inserted
	 * @return List creation success/failure/error
		JSON Form
	 * {
	 * "username": "cal",
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
	/*Takes JSON containing user and recipe info and insert it into userRecipeList table
	 * @param  data the data inserted
	 * @return Insert success/failure/error
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
	/*Takes JSON containing user and recipe info and removes it from userRecipeList table
	 * @param  data the data inserted
	 * @return Recipe removal success/failure/error
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
	/* Returns all listname and listid created by user based on username, returns ONLY listnames and listId 
	 * NO RECIPE INFO 
	 * @param  data the data inserted
	 * @return JSON with listnames and listId NO RECIPES
	 */
	@Path("getList/{username}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getUserList (@PathParam("username")String username) {
		return dataBase.getUserList(username).toString();
	}
	/* Based on listId, returns info for ONE list with it's listnames and listId and recipe info
	 * @param  data the data inserted
	 * @return JSON for one list, with it's listnames and listId and recipe info
	 */
	@Path("getListInfo/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getUserListInfo (@PathParam("id") int id) {
		return dataBase.getUserListInfo(id).toString();
	}
	/* Returns all listname and listid created by user based on username, returns listnames and listId and recipe info
	 * HAS RECIPE INFO
	 * @param  data the data inserted
	 * @return JSON with listnames and listId HAS RECIPE INFO
	 */
	@Path("getListInfoAll/{username}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllUserList (@PathParam("username")String username) {
		return dataBase.getAllUserListInfo(username).toString();
	}
	/*Takes JSON containing user and recipe info and insert it into userFavorite table for that user
	 * @param  data the data inserted
	 * @return Favorite success/failure/error
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
	/*Takes JSON containing user and recipe info and removes it into userFavorite table for that user
	 * @param  data the data inserted
	 * @return Favorite success/failure/error
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
	/* Returns all favorites for a user
	 * HAS RECIPE INFO
	 * @param  username user
	 * @return JSON with all of the users favorites
	 */
	@Path("favorites/get/{username}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String favoritesGet (@PathParam("username")String username) {
		return dataBase.getUserFavorites(username).toString();
	}
	
	/*Takes JSON containing user and recipe info and inserts it into likedTable for that user
	 * @param  data the data inserted
	 * @return Like success/failure/error
		 * {"username": "cal",
			"recipeName" : "a", 
			"author" : "a",
			"recipeId" : -1
		}
	 */
	@Path("like")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String listLike (String data) {
		JSONObject temp = new JSONObject(data);
		return dataBase.likeList(temp).toString();
	}
	/*Takes JSON containing user and list info and inserts it into likedTable for that user
	 * @param  data the data inserted
	 * @return Like success/failure/error
		{	"username": "cal1",
			"listId": 22,
			"listName": "aaaaaaa"
		}
	 */
	@Path("unlike")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public String listUnlike (String data) {
		JSONObject temp = new JSONObject(data);
		return dataBase.unlikeList(temp).toString();
	}
	/* Returns all liked list for a user
	 * HAS RECIPE INFO
	 * @param  username user
	 * @return JSON with all of the users liked list
	 */
	@Path("liked/{username}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String likedGetUser(@PathParam("username")String username) {
		return dataBase.getlikeList(username).toString();
	}
}
