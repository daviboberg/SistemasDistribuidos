package Client;

import Comunication.InterfaceServer;
import Resources.Interest;
import Resources.Resource;
import Server.ServerService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.List;

public class Client {

    private ServerService service;

    Client(int port) throws RemoteException, NotBoundException {
        Registry name_server;
        name_server = LocateRegistry.getRegistry(port);

        service = (ServerService) name_server.lookup("ClientOperations");
    }


    public List<Resource> getInformation(Resource resource) throws RemoteException, SQLException {
        return service.getInformation(resource);
    }

    public void postOrder(Resource resource) throws RemoteException {
        service.postOrder(resource);
    }

    public void postInterest(Interest interest) throws RemoteException {
        service.registerInterest(interest);
    }
}
