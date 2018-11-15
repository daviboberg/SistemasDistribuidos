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

public class Hotel implements Resource {

    public int id;
    // Hotel location
    public String location;
    // Hotel number of rooms
    public int rooms;
    // Hotel room capacity
    public int capacity;
    // Hotel room price
    public float price;
    // Hotel check-in date
    public String check_in_date;
    // Hotel check-out date
    public String check_out_date;

    // URI path for Hotel operations
    private static String path = "Hotel";

    // JSON parser
    private static Gson gson = new Gson();

    /**
     * @param resource
     * @param quantity_to_buy
     * @param check_in_date
     * @param check_out_date
     * @param capacity
     */
    // Hydrate Hotel required fields and call bookHotelRoom, that perform (call from Client) POST request
    public void bookHotel(Resource resource, int quantity_to_buy, String check_in_date, String check_out_date, int capacity) {
        Hotel hotel = (Hotel) resource;
        hotel.rooms = quantity_to_buy;
        hotel.check_in_date = check_in_date;
        hotel.check_out_date = check_out_date;
        hotel.capacity = capacity;
        this.bookHotelRoom(hotel);

    }


    // POST function for Hotel, Produces and Consumes JSON.
    /**
     * @param resource
     * @return
     */
    @POST
    @Path("/Hotel")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response bookHotelRoom(Resource resource ) {
        // Call client buy to perform POST request
        Response response = Client.buy(Hotel.path, resource);
        // Read response as an String
        String output = response.readEntity(String.class);
        System.out.println(output);
        return response;
    }

    // Operation to retrieve client desired hotel room available.
    /**
     * @param hotel_filters
     * @return
     */
    public static ArrayList<Resource> getHotelByFilters(HotelFilters hotel_filters) {
        // Create hash map that is going to be used as request query params
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("location", hotel_filters.location);
        hashMap.put("rooms", String.valueOf(hotel_filters.rooms));
        hashMap.put("check_in_date", hotel_filters.start_date);
        hashMap.put("check_out_date", hotel_filters.end_date);
        hashMap.put("price", String.valueOf(hotel_filters.price));
        hashMap.put("capacity", String.valueOf(hotel_filters.capacity));

        // perform GET request
        Response response = Client.getAllWithFilters(Hotel.path, hashMap);
        // get response as an String
        String output = response.readEntity(String.class);

        System.out.println(output);

        // If something is wrong
        if (response.getStatus() != 200) {
            System.out.println("Request Error");
            System.out.println(output);
            return new ArrayList<>();
        }

        JsonArray hotel_string = gson.fromJson(output, JsonArray.class);

        // create an array of Hotels from response
        ArrayList<Resource> hotels = new ArrayList<>();
        for (int i = 0; i < hotel_string.size(); i++) {
            // Parse json to Hotel object and add to Hotel list
            hotels.add(gson.fromJson(hotel_string.get(i).toString(), Hotel.class));
        }

        return hotels;
    }

    @Override
    public int getId() {
        return this.id;
    }

    // Not used on Hotel. Bad design
    @Override
    public void buy(Resource resource, int quantity_to_buy) {
    }
}
