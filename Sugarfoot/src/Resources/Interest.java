package Resources;

import Comunication.InterfaceClient;

import java.io.Serializable;
import java.util.ArrayList;

public class Interest implements Serializable {

    public ArrayList<Reference> references;
    public String destiny;
    public Float price;
    public InterfaceClient client;

    public boolean inResource(Resource resource) {
        Reference resource_reference = resource.getReference();
        for (Reference reference : this.references) {
            if (reference.equals(resource_reference))
                return true;


        }

        return false;
    }

}
