package Merchant;

import Comunication.IMerchantServer;
import Resources.Hotel;
import Resources.Resource;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Merchant {

    private IMerchantServer server;

    public Merchant(int port) throws RemoteException, NotBoundException {
        Registry name_server;
        name_server = LocateRegistry.getRegistry(port);

        server = (IMerchantServer)name_server.lookup("MerchantOperations");
    }

    public Resource postResource(Resource resource) throws RemoteException {
        return server.postResource(resource);
    }

    public void deleteResource(Resource resource) throws RemoteException {
        server.deleteResource(resource);
    }
}
