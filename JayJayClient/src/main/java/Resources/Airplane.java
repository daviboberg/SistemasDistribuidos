package Resources;

import Client.Client;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Airplane implements Resource {

    public int id;
    public String flight_number;
    public String origin;
    public String destination;
    public int seats;
    public int number_of_seats;
    public float price;
    public String date;
    private static String path = "Airplane";

    private static Gson gson = new Gson();

    public Airplane() {
    }

    @POST
    @Path("/Airplane")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response buyAirplaneTicket(Resource resource) {
        Response response = Client.buy(Airplane.path, resource);
        String output = response.readEntity(String.class);
        System.out.println(output);
        return response;
    }

    public static ArrayList<Resource> getAirplanesByFilters(AirplaneFilters airplane_filters) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("origin", airplane_filters.origin);
        hashMap.put("destination", airplane_filters.destination);
        hashMap.put("date", airplane_filters.date);
        hashMap.put("price", String.valueOf(airplane_filters.price));
        hashMap.put("seats", String.valueOf(airplane_filters.seats));

        Response response = Client.getAllWithFilters(Airplane.path, hashMap);
        String output = response.readEntity(String.class);

        if (response.getStatus() != 200) {
            System.out.println("Request Error");
            System.out.println(output);
            return new ArrayList<>();
        }

        JsonArray airplanes_string = gson.fromJson(output, JsonArray.class);

        ArrayList<Resource> airplanes = new ArrayList<>();
        for (int i = 0; i < airplanes_string.size(); i++) {
            airplanes.add(gson.fromJson(airplanes_string.get(i).toString(), Airplane.class));
        }

        return airplanes;
    }

    @Override
    public String getPath() {
        return Airplane.path;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void buy(Resource resource, int quantity_to_buy) {
        Airplane airplane = (Airplane)resource;
        airplane.number_of_seats = quantity_to_buy;
        this.buyAirplaneTicket(airplane);
    }
}
