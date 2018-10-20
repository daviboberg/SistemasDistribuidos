package Merchant;

import Comunication.IMerchantServer;
import Resources.Resource;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Merchant {

    private IMerchantServer server;

    /**
     * Create a merchant
     * @param port
     * @throws RemoteException
     * @throws NotBoundException
     */
    Merchant(int port) throws RemoteException, NotBoundException {
        Registry name_server;
        name_server = LocateRegistry.getRegistry(port);

        server = (IMerchantServer)name_server.lookup("MerchantOperations");
    }

    /**
     * Post a new resource to server
     * @param resource to add
     * @return resource added
     * @throws RemoteException
     */
    Resource postResource(Resource resource) throws RemoteException {
        return server.postResource(resource);
    }

    /**
     * Remove some resource from server
     * @param resource to remove
     * @throws RemoteException
     */
    void deleteResource(Resource resource) throws RemoteException {
        server.deleteResource(resource);
    }
}
