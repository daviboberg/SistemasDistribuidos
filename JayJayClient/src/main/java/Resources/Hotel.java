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
import java.net.URISyntaxException;
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
    public String check_in_date;
    public String check_out_date;

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
    public void buy(Resource resource, int quantity_to_buy) throws URISyntaxException {

    }

    public void bookHotel(Resource resource, int quantity_to_buy, String check_in_date, String check_out_date) {
        Hotel hotel = (Hotel) resource;
        hotel.rooms = quantity_to_buy;
        hotel.check_in_date = check_in_date;
        hotel.check_out_date = check_out_date;
        this.locateHotelRoom(hotel);

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
        hashMap.put("check_in_date", hotel_filters.start_date);
        hashMap.put("check_out_date", hotel_filters.end_date);
        hashMap.put("price", String.valueOf(hotel_filters.price));

        Response response = Client.getAllWithFilters(Hotel.path, hashMap);
        String output = response.readEntity(String.class);

        System.out.println(output);

        if (response.getStatus() != 200) {
            System.out.println("Request Error");
            System.out.println(output);
            return new ArrayList<>();
        }

        JsonArray hotel_string = gson.fromJson(output, JsonArray.class);

        ArrayList<Resource> hotels = new ArrayList<>();
        for (int i = 0; i < hotel_string.size(); i++) {
            hotels.add(gson.fromJson(hotel_string.get(i).toString(), Hotel.class));
        }

        return hotels;
    }
}
