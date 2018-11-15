package Client;

import Resources.Resource;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

// Class used to perform REST GET and POST requests
public class Client {

    // Server URI
    private static final String REST_URI = "http://localhost:8600/";

    /**
     * @param path
     * @param filters
     * @return
     */
    // perform a GET request with filters params on URI, return resources found on server that satisfies filters
    public static Response getAllWithFilters(String path, Map<String, String> filters) {
        // Build a REST request
        WebTarget target = ClientBuilder.newClient()
                .target(REST_URI)
                .path(path);

        // Add filters on queries of request URI
        for (String key : filters.keySet()) {
            target = target.queryParam(key, filters.get(key));
        }

        // remaining query builder and get request, return Response
        return target.
                request(APPLICATION_JSON).
                get(Response.class);
    }

    /**
     * @param path
     * @param resource
     * @return
     */
    // perform POST request passing resource desired id on URI and remaining fields on body. Return request Response
    public static Response buy(String path, Resource resource) {
        return ClientBuilder.newClient()
                .target(REST_URI)
                .path(path + "/buy/" + String.valueOf(resource.getId()))
                .request(APPLICATION_JSON)
                .post(Entity.entity(resource, MediaType.APPLICATION_JSON));
    }
}
