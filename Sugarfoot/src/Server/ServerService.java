package Server;

import Comunication.InterfaceClient;
import Comunication.InterfaceServer;
import Resources.Interest;
import Resources.Resource;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;

public class ServerService extends UnicastRemoteObject implements InterfaceServer {

    ServerService() throws RemoteException {
        super();
    }

    /**
     * @param interest
     */
    @Override
    public void registerInterest(Interest interest) {
        Observer.addInterest(interest);
    }

    /**
     * @param resource
     * @return
     * @throws SQLException
     */
    @Override
    public List<Resource> getInformation(Resource resource) throws SQLException {
        return resource.find();
    }

    /**
     * @param resource
     */
    @Override
    public void postOrder(Resource resource) {
        resource.buy();
    }

}
