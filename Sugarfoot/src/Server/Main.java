package Server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws RemoteException, AlreadyBoundException, SQLException, ClassNotFoundException {
        Server server = new Server(Integer.parseInt(args[0]));
        server.bindServices();

        while(true){}
    }
}
