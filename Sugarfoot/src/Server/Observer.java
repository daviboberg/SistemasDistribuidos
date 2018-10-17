package Server;

import Resources.Interest;
import Resources.Resource;

import java.rmi.RemoteException;
import java.util.ArrayList;

class Observer {

    private static ArrayList<Interest> interests;

    static void addInterest(Interest interest) {
        interests.add(interest);
    }

    static void processEvents(Resource resource) throws RemoteException {
        for (Interest interest : interests) {
            if(!interest.inResource(resource)) {
                continue;
            }

            interest.client.echo("Teste");
        }
    }
}
