package Client;

import Comunication.InterfaceServer;
import Resources.Resource;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.List;

public class Client {

    private ClientService service;

    Client(int port) throws RemoteException, NotBoundException {
        Registry name_server;
        name_server = LocateRegistry.getRegistry(port);

        InterfaceServer server;
        server = (InterfaceServer)name_server.lookup("echo");
        server = (InterfaceServer) name_server.lookup("ClientOperations");
        service = new ClientService(server);
    }

    public void call() throws RemoteException {
        service.callServer("Hello World!");
    }

    public List<Resource> getInformation(Resource resource) throws RemoteException, SQLException {
        return service.getInformation(resource);
    }

    public void postOrder(Resource resource) throws RemoteException {
        service.postOrder(resource);
    }
}
