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
    public String location;
    public int rooms;
    public int capacity;
    public float price;
    private static String path = "Hotel";

    private static Gson gson = new Gson();


    @Override
    public String getPath() {
        return Hotel.path;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void buy(Resource resource) {
        this.locateHotelRoom(resource);

    }

    @POST
    @Path("/Hotel")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response locateHotelRoom(Resource resource ) {
        Response response = Client.buy(Hotel.path, resource);
        String output = response.readEntity(String.class);
        System.out.println(output);
        return response;
    }

    public static ArrayList<Resource> getHotelByFilters(HotelFilters hotel_filters) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("location", hotel_filters.location);
        hashMap.put("rooms", String.valueOf(hotel_filters.rooms));
        hashMap.put("start_date", hotel_filters.start_date);
        hashMap.put("end_date", hotel_filters.end_date);
        hashMap.put("price", String.valueOf(hotel_filters.price));

        Response response = Client.getAllWithFilters(Hotel.path, hashMap);
        String output = response.readEntity(String.class);

        if (response.getStatus() != 200) {
            System.out.println("Request Error");
            System.out.println(output);
            return new ArrayList<>();
        }

        JsonArray hotel_string = gson.fromJson(output, JsonArray.class);

        ArrayList<Resource> hotels = new ArrayList<>();
        for (int i = 0; i < hotel_string.size(); i++) {
            hotels.add(gson.fromJson(hotel_string.get(i).toString(), Airplane.class));
        }

        return hotels;
    }
}
