package Comunication;

import Resources.Resource;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;


public interface InterfaceClient extends Remote {
    void echo(String str) throws RemoteException;

    List<Resource> getInformation(Resource resource) throws RemoteException, SQLException;

    void postOrder(Resource resource) throws RemoteException;

    void postAnnounce(String string) throws RemoteException;
}
