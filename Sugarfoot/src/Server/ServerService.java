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

    @Override
    public void call(String str, InterfaceClient client) throws RemoteException {
        client.echo(str);
    }

    @Override
    public void registerInterest(Interest interest) throws RemoteException {
        Observer.addInterest(interest);
    }

    @Override
    public List<Resource> getInformation(Resource resource) throws RemoteException, SQLException {
        return resource.find();
    }

    @Override
    public void postOrder(Resource resource) throws RemoteException {
        return;
    }

}
