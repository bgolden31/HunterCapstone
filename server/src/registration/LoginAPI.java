package registration;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
<<<<<<< HEAD
		String password= temp.getString("password"); 
=======
		String password= temp.getString("password");
>>>>>>> ed4658efbf7df79fe42e913aa5c0a135dc2b01e9
		return clients.getUser(username, password).toString();
	}
}
