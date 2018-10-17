package Client;

import Comunication.InterfaceClient;
import Comunication.InterfaceServer;
import Resources.Resource;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;

public class ClientService extends UnicastRemoteObject implements InterfaceClient {

    private InterfaceServer server;

    ClientService(InterfaceServer server) throws RemoteException {
        this.server = server;
    }

    @Override
    public void echo(String str) throws RemoteException {
        System.out.println(str);
    }

    @Override
    public void postAnnounce(String string) throws RemoteException {
        System.out.println(string);
    }

    public void callServer(String str) throws RemoteException {
        server.call(str, this);
    }


    public List<Resource> getInformation(Resource resource) throws RemoteException, SQLException {
        return server.getInformation(resource);
    }

    public void postOrder(Resource resource) throws RemoteException {
        server.postOrder(resource);
    }
}
