package Comunication;

import Resources.Resource;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IMerchantServer extends Remote {
    void postResource(Resource resource) throws RemoteException;
    void deleteResource(Resource resource) throws RemoteException;
}
