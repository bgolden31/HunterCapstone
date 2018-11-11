package registration;

import org.json.*;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("register")
public class RegisterAPI {
	UserDatabase clients = new UserDatabase();
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getRegister(){
		return "Register is POST only";
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String setRegister(String data) {
		JSONObject temp = new JSONObject(data);
		return clients.register(temp);
	}

}
