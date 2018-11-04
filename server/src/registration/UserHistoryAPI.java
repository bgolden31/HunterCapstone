package registration;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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

	//new 10/20/2018
	@Path("insert")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String insertRecipe (String data){
		JSONObject temp = new JSONObject(data);
		return dataBase.insertUserHistory(temp);
	}
	//end
	
	@Path("get")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getRecipe (String data) {
		JSONObject temp = new JSONObject(data);
		String username= temp.getString("username");
		return dataBase.getUserHistory(username).toString();
	}
}
