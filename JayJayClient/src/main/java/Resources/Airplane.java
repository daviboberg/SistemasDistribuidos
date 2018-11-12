package Resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

public class Airplane extends Resource {

    public int id;
    public String flight_number;
    public String origin;
    public String destination;
    public int seats;
    public float price;
    public String date;

    public Airplane(int id, String fligh_number, String origin, String destination, int seats, float price, String date) {
        this.id = id;
        this.flight_number = fligh_number;
        this.origin = origin;
        this.destination = destination;
        this.seats = seats;
        this.price = price;
        this.date = date;
    }

    public Airplane() {
    }

    @POST
    @Path("/Airplane")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response buyAirplaneTicket(Airplane airplane ) throws URISyntaxException
    {
        if(airplane == null){
            return Response.status(400).entity("Please add airplane details !!").build();
        }

        return Response.created(new URI("/Airplane/"+airplane.getId())).build();
    }

    private int getId() {
        return this.id;
    }
}
