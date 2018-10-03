package Server;

import Comunication.IMerchantServer;
import Resources.Resource;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class MerchantServerService extends UnicastRemoteObject implements IMerchantServer {
    private List<Resource> resource_list;

    public MerchantServerService(List<Resource> resource_list) throws RemoteException {
        super();
        this.resource_list = resource_list;
    }

    @Override
    public Resource postResource(Resource resource) throws RemoteException {
        this.resource_list.add(resource);
        System.out.println("Merchant posted this id: " + resource.getId());
        if (resource.getId() == 0) {
            resource.insert();
            return resource;
        }

        resource.update();
        return resource;
    }

    @Override
    public void deleteResource(Resource resource) throws RemoteException {
        System.out.println("Merchant deleted this id: " + resource.getId());
        this.resource_list.remove(resource);
        resource.delete();
    }
}
