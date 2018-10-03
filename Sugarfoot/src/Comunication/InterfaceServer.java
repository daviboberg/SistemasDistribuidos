package Comunication;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceServer extends Remote {
    void call(String str, InterfaceClient client) throws RemoteException;
}
