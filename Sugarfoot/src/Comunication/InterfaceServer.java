package Comunication;

import Resources.Interest;
import Resources.Resource;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface InterfaceServer extends Remote {
    void call(String str, InterfaceClient client) throws RemoteException;

    void registerInterest(Interest interest) throws RemoteException;

    List<Resource> getInformation(Resource resource) throws RemoteException, SQLException;

    void postOrder(Resource resource) throws RemoteException;
}
