package registeration;


import org.glassfish.jersey.jsonb.*;

import org.json.*;

import com.mysql.cj.xdevapi.JsonString;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("register")
public class Register {
	Profile clients = new Profile();
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getRegister(){
		return "no return for this get mothod, only register(post)";
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String setRegister(User newuser) {
		return clients.register(newuser);
	}

}
