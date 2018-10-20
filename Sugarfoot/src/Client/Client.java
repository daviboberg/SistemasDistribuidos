package Client;

import Comunication.InterfaceServer;
import Resources.Interest;
import Resources.Resource;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.List;

class Client {

    private InterfaceServer service;

    /**
     * @param port
     * @throws RemoteException
     * @throws NotBoundException
     */
    Client(int port) throws RemoteException, NotBoundException {
        Registry name_server;
        name_server = LocateRegistry.getRegistry(port);

        service = (InterfaceServer) name_server.lookup("ClientOperations");
    }


    /**
     * @param resource
     * @return
     * @throws RemoteException
     * @throws SQLException
     */
    List<Resource> getInformation(Resource resource) throws RemoteException, SQLException {
        return service.getInformation(resource);
    }

    /**
     * @param resource
     * @throws RemoteException
     */
    void postOrder(Resource resource) throws RemoteException {
        service.postOrder(resource);
    }

    /**
     * @param interest
     * @throws RemoteException
     */
    void postInterest(Interest interest) throws RemoteException {
        service.registerInterest(interest);
    }
}
