package Resources;

import java.net.URISyntaxException;

public interface Resource {

    /**
     * @return
     */
    public int getId();

    /**
     * @param resource
     * @param quantity_to_buy
     * @throws URISyntaxException
     */
    void buy(Resource resource, int quantity_to_buy) throws URISyntaxException;
}
