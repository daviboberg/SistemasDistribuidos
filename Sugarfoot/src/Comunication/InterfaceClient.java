package Comunication;

import Resources.Resource;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface InterfaceClient extends Remote {
    void echo(String str) throws RemoteException;

    void getInformation(Resource resource) throws RemoteException;

    void postOrder(Resource resource) throws RemoteException;
}
