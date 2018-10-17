package Server;

import Resources.Interest;
import Resources.Resource;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Observer {

    private ArrayList<Interest> interests;

    Observer() {
        this.interests = new ArrayList();
    }

    public void addInterest(Interest interest) {
        this.interests.add(interest);
    }

    void processEvents(Resource resource) throws RemoteException {
        for (Interest interest : this.interests) {
            if(!interest.inResource(resource)) {
                continue;
            }

            interest.client.echo("Teste");
        }
    }
}
