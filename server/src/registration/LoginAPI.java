package registration;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONObject;

@Path("login")
public class LoginAPI {

	UserDatabase clients = new UserDatabase();
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String x() {
		System.out.print("start");
		return "GET success";
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getProfile(String data){
		JSONObject temp = new JSONObject(data);
		String username= temp.getString("username");
		String password= temp.getString("password"); 
		JSONObject temp1 = clients.getUser(username, password);
		
		UserRecipeDatabase userrecipes = new UserRecipeDatabase();
		JSONArray temp2 =  userrecipes.getUserRecipes(username);
		UserHistoryDatabase userHistory = new UserHistoryDatabase();
		JSONObject temp3 = userHistory.getUserHistory(username);
		
		JSONObject combined = new JSONObject();
		combined.put("User Info",temp1);
		combined.put("Recipes", temp2);
		combined.put("History", temp3);
		return combined.toString();
	}
}
