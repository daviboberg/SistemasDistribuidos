package Resources;

import java.net.URISyntaxException;

public interface Resource {

    public String getPath();

    public int getId();

    void buy(Resource resource, int quantity_to_buy) throws URISyntaxException;
}
