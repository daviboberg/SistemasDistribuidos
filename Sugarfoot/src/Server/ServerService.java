package Server;

import Comunication.InterfaceClient;
import Comunication.InterfaceServer;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerService extends UnicastRemoteObject implements InterfaceServer {

    ServerService() throws RemoteException {
        super();
    }

    @Override
    public void call(String str, InterfaceClient client) throws RemoteException {
        client.echo(str);
    }

}