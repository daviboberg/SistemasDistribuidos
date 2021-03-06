package Server;

import Resources.Resource;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private MerchantServerService merchant_server_service;
    private Registry name_server;
    private ServerService service;
    private List<Resource> resource_list;

    /**
     * @param port
     * @throws RemoteException
     */
    Server(int port) throws RemoteException {
        this.resource_list = new ArrayList<>();
        this.service = new ServerService();
        this.merchant_server_service = new MerchantServerService(this.resource_list);

        name_server = LocateRegistry.createRegistry(port);
    }

    /**
     * @throws AlreadyBoundException
     * @throws RemoteException
     */
    void bindServices() throws AlreadyBoundException, RemoteException {
        name_server.bind("echo", this.service);
        name_server.bind("MerchantOperations", this.merchant_server_service);
        name_server.bind("ClientOperations", this.service);
    }
}
