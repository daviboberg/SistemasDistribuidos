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
    // flight origin
    public String origin;
    // flight destination
    public String destination;
    // flight seats
    public int seats;
    // desired seats
    public int number_of_seats;
    // flight price
    public float price;
    // flight departure
    public String date;

    // URI rest path for Airplane operations
    private static String path = "Airplane";

    // Json parser
    private static Gson gson = new Gson();

    public Airplane() {
    }

    // Airplane buy operation. POST request to buy airplane tickets
    /**
     * @param resource
     * @return
     */
    @POST
    @Path("/Airplane")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response buyAirplaneTicket(Resource resource) {
        // Call client buy to perform POST request
        Response response = Client.buy(Airplane.path, resource);
        // Read response as an String
        String output = response.readEntity(String.class);
        System.out.println(output);
        return response;
    }

    // Operation to retrieve client desired flights available.
    /**
     * @param airplane_filters
     * @return
     */
    public static ArrayList<Resource> getAirplanesByFilters(AirplaneFilters airplane_filters) {
        // Create hash map that is going to be used as request query params
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("origin", airplane_filters.origin);
        hashMap.put("destination", airplane_filters.destination);
        hashMap.put("date", airplane_filters.date);
        hashMap.put("price", String.valueOf(airplane_filters.price));
        hashMap.put("seats", String.valueOf(airplane_filters.seats));

        // perform GET request
        Response response = Client.getAllWithFilters(Airplane.path, hashMap);
        // get response as an String
        String output = response.readEntity(String.class);

        // If something is wrong
        if (response.getStatus() != 200) {
            System.out.println("Request Error");
            System.out.println(output);
            return new ArrayList<>();
        }

        JsonArray airplanes_string = gson.fromJson(output, JsonArray.class);

        // create an array of Airplane from response
        ArrayList<Resource> airplanes = new ArrayList<>();
        for (int i = 0; i < airplanes_string.size(); i++) {
            // Parse json to Hotel object and add to Airplane list
            airplanes.add(gson.fromJson(airplanes_string.get(i).toString(), Airplane.class));
        }

        return airplanes;
    }

    /**
     * @return
     */
    @Override
    public int getId() {
        return this.id;
    }


    // Hydrate Hotel required fields and call buyAirplaneTicket, that perform (call from Client) POST request
    /**
     * @param resource
     * @param quantity_to_buy
     */
    @Override
    public void buy(Resource resource, int quantity_to_buy) {
        Airplane airplane = (Airplane)resource;
        airplane.number_of_seats = quantity_to_buy;
        this.buyAirplaneTicket(airplane);
    }
}
