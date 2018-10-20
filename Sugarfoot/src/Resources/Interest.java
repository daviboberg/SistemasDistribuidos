package Resources;

import Comunication.InterfaceClient;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Interest implements Serializable {

    public ArrayList<Reference> references;
    public String destiny;
    public Float price;
    public InterfaceClient client;

    public Interest () {
        references = new ArrayList<>();
    }

    public boolean processForResource(Resource resource) throws RemoteException {
        Reference resource_reference = resource.getReference();
        for (Reference reference : this.references) {
            if (!reference.equals(resource_reference))
                continue;

            if (!this.destiny.equals(resource.getDestiny()))
                return false;

            if (this.price <= resource.getPrice())
                return false;

            System.out.println(resource.getId());

            client.postAnnounce(this.getAnnounce(resource));
            return true;
        }

        return false;
    }

    private String getAnnounce(Resource resource) {
        Reference reference = resource.getReference();
        String type = (reference == Reference.AIRPLANE) ? "Voo" :
                      (reference == Reference.HOTEL) ? "Hotel" : "Pacote";
        return "Um novo interesse em " + type + " foi encontrado com o ID:" + resource.getId();
    }

}
