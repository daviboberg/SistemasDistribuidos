package Server;

import Resources.Airplane;
import Resources.Hotel;
import Resources.Interest;
import Resources.Resource;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class Observer {

    private static ArrayList<Interest> interests = new ArrayList<Interest>();

    static void addInterest(Interest interest) {
        List<Resource> resources = new ArrayList<Resource>();
        interests.add(interest);

        try {
            resources.addAll(Airplane.getAll());
            resources.addAll(Hotel.getAll());

            for (Resource resource : resources) {
                processEvents(resource);
            }
        } catch (SQLException | RemoteException e) {
            e.printStackTrace();
        }



    }

    static void processEvents(Resource resource) throws RemoteException {
        for (Interest interest : interests) {
            interest.processForResource(resource);
        }
    }
}
