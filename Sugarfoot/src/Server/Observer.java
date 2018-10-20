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

    private static ArrayList<Interest> interests = new ArrayList<>();

    /**
     * Add interest of client
     * @param interest to add
     */
    static void addInterest(Interest interest) {
        List<Resource> resources = new ArrayList<>();
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

    /**
     * @param resource to process
     * @throws RemoteException
     */
    static void processEvents(Resource resource) throws RemoteException {
        boolean result = false;
        for (Interest interest : interests) {
            result = interest.processForResource(resource);
            if (result) {
                interests.remove(interest);
                break;
            }
        }
    }
}
