package Client;

import Resources.Airplane;
import Resources.Resource;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

public class Client {

    private static final String REST_URI = "http://localhost:8600/";

    private javax.ws.rs.client.Client client = ClientBuilder.newClient();
    private Gson gson = new Gson();

    public ArrayList<Resource> getAirplanes() {
        Response response = getAll();
        String output = response.readEntity(String.class);

        JsonArray airplanes_string = gson.fromJson(output, JsonArray.class);

        ArrayList<Resource> airplanes = new ArrayList<>();
        for (int i = 0; i < airplanes_string.size(); i++) {
            airplanes.add(gson.fromJson(airplanes_string.get(i).toString(), Airplane.class));
        }

        return airplanes;
    }

    public ArrayList<Resource> getAirplanesByFilters(Airplane airplane) {
        Response response = getAllWithFilters(airplane);
        String output = response.readEntity(String.class);

        JsonArray airplanes_string = gson.fromJson(output, JsonArray.class);

        ArrayList<Resource> airplanes = new ArrayList<>();
        for (int i = 0; i < airplanes_string.size(); i++) {
            airplanes.add(gson.fromJson(airplanes_string.get(i).toString(), Airplane.class));
        }

        return airplanes;
    }

    private Response getAll() {
        return client
                .target(REST_URI)
                .path("Airplane")
                .request(APPLICATION_JSON)
                .get(Response.class);
    }

    private Response getAllWithFilters(Airplane airplane) {
        return client
                .target(REST_URI)
                .path("Airplane")
                .queryParam("origin", airplane.origin)
                .queryParam("destination", airplane.destination)
                .queryParam("date", airplane.date)
                .request(APPLICATION_JSON)
                .get(Response.class);
    }

    public Response buyTicket(Airplane airplane) {
        return client
                .target(REST_URI)
                .path("Airplane/buy/"+String.valueOf(airplane.id))
                .request(APPLICATION_JSON)
                .put(Entity.entity(airplane, MediaType.APPLICATION_JSON));
    }
}
