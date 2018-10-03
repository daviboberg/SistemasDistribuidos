package Client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Main {
    public static void  main(String[] args) throws RemoteException, NotBoundException {
        Client client = new Client(Integer.parseInt(args[0]));
        client.call();
    }
}
