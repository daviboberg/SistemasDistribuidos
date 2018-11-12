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
import java.util.List;
import java.util.Map;

public class TravelPackage implements Resource {

    private static String path = "Package";
    private static Gson gson = new Gson();


    @Override
    public String getPath() {
        return null;
    }
    @Override
    public int getId() {
        return 0;
    }

    @Override
    public void buy(Resource resource) {
        this.buyTravelPackage(resource);
    }

    public static List<Resource> getPackageByFilters(TravelPackageFilters travel_package_filters) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("origin", travel_package_filters.origin);
        hashMap.put("destination", travel_package_filters.destination);
        hashMap.put("going_date", travel_package_filters.going_date);
        hashMap.put("return_date", travel_package_filters.return_date);
        hashMap.put("number_persons", String.valueOf(travel_package_filters.number_persons));
        hashMap.put("number_rooms", String.valueOf(travel_package_filters.number_rooms));

        Response response = Client.getAllWithFilters(TravelPackage.path, hashMap);
        String output = response.readEntity(String.class);

        if (response.getStatus() != 200) {
            System.out.println("Request Error");
            System.out.println(output);
            return new ArrayList<>();
        }

        JsonArray package_string = gson.fromJson(output, JsonArray.class);

        ArrayList<Resource> packages = new ArrayList<>();
        for (int i = 0; i < package_string.size(); i++) {
            packages.add(gson.fromJson(package_string.get(i).toString(), TravelPackage.class));
        }

        return packages;
    }

    @POST
    @Path("/Package")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response buyTravelPackage(Resource resource ) {
        Response response = Client.buy(TravelPackage.path, resource);
        String output = response.readEntity(String.class);
        System.out.println(output);
        return response;
    }
}
