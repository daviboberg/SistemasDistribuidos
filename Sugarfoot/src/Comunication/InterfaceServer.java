package Comunication;

import Resources.Interest;

import java.lang.reflect.Array;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceServer extends Remote {
    void call(String str, InterfaceClient client) throws RemoteException;

    void registerInterest(Interest interest) throws RemoteException;
}
