package registration;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("nutrient")
public class NutrientApi {
	NutrientDatabase dataBase = new NutrientDatabase();
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String x() {
		System.out.print("start");
		return "GET success";
	}
	@Path("get")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Nutrient getNutrient (Nutrient data) {
		System.out.print("start");
		return dataBase.getNutrient(data.getNutrientsId() );
	}
	
	@Path("insert")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String insertNutrient (Nutrient data){
		return dataBase.insertNutrient(data);
	}
}
