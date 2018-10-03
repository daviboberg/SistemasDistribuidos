package Client;

import Comunication.InterfaceServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private ClientService service;

    public Client(int port) throws RemoteException, NotBoundException {
        Registry name_server;
        name_server = LocateRegistry.getRegistry(port);

        InterfaceServer server;
        server = (InterfaceServer)name_server.lookup("echo");
        service = new ClientService(server);
    }

    public void call() throws RemoteException {
        service.callServer("Hello World!");
    }
}
