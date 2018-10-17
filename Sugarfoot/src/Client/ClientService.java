package Client;

import Comunication.InterfaceClient;
import Comunication.InterfaceServer;
import Resources.Resource;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;

public class ClientService extends UnicastRemoteObject implements InterfaceClient {


    ClientService() throws RemoteException {
    }


    @Override
    public void postAnnounce(String string) throws RemoteException {
        System.out.println(string);
    }
}
