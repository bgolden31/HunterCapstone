package registration;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("main")
public class serverdemo {
	UserDatabase clients = new UserDatabase();
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String x() {
		System.out.print("start");
		return "work at here";
	}
	
}