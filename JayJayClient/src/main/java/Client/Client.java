package Client;

import Resources.Airplane;
import Resources.AirplaneFilters;
import Resources.Resource;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import java.util.HashMap;
import java.util.Map;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

public class Client {

    private static final String REST_URI = "http://localhost:8600/";

    private javax.ws.rs.client.Client client = ClientBuilder.newClient();
    private Gson gson = new Gson();

    public static Response getAll(String path) {
        return ClientBuilder.newClient()
                .target(REST_URI)
                .path(path)
                .request(APPLICATION_JSON)
                .get(Response.class);
    }

    public static Response getAllWithFilters(String path, Map<String, String> filters) {
        WebTarget target = ClientBuilder.newClient()
                .target(REST_URI)
                .path(path);

        for (String key : filters.keySet()) {
            target = target.queryParam(key, filters.get(key));
        }

        return target.
                request(APPLICATION_JSON).
                get(Response.class);
    }

    public static Response buy(String path, Resource resource) {
        return ClientBuilder.newClient()
                .target(REST_URI)
                .path(path + "/buy/" + String.valueOf(resource.getId()))
                .request(APPLICATION_JSON)
                .put(Entity.entity(resource, MediaType.APPLICATION_JSON));
    }
}
