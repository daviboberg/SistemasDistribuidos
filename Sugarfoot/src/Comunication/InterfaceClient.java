package Comunication;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface InterfaceClient extends Remote {
    void echo(String str) throws RemoteException;
}
