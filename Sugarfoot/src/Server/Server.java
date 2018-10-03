package Server;

import Resources.Resource;

import javax.sql.DataSource;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private MerchantServerService merchant_server_service;
    private Registry name_server;
    private ServerService service;
    private List<Resource> resource_list;

    Server(int port) throws RemoteException, AlreadyBoundException, ClassNotFoundException, SQLException {
        this.resource_list = new ArrayList<>();
        this.service = new ServerService();
        this.merchant_server_service = new MerchantServerService(this.resource_list);

        name_server = LocateRegistry.createRegistry(port);

        Connection c;
        DataSource source = new org.sqlite.SQLiteDataSource();
        c = source.getConnection();
        Statement stmt = null;
        c.
        c = DriverManager.getConnection("jdbc:sqlite:test.db");
        ResultSet r = stmt.executeQuery("SELECT * FROM hotel");
        String s = r.getString("name");
        System.out.println(s);
    }

    void bindServices() throws AlreadyBoundException, RemoteException {
        name_server.bind("echo", this.service);
        name_server.bind("MerchantOperations", this.merchant_server_service);
    }
}
