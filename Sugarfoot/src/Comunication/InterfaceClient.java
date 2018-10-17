package Comunication;

import Resources.Resource;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;


public interface InterfaceClient extends Remote {

    void postAnnounce(String string) throws RemoteException;
}
