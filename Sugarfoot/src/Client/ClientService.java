package Client;

import Comunication.InterfaceClient;
import Comunication.InterfaceServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientService extends UnicastRemoteObject implements InterfaceClient {

    private InterfaceServer server;

    ClientService(InterfaceServer server) throws RemoteException {
        this.server = server;
    }

    @Override
    public void echo(String str) throws RemoteException {
        System.out.println(str);
    }

    public void callServer(String str) throws RemoteException {
        server.call(str, this);
    }
}
