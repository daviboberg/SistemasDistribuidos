package Merchant;

import Resources.Airplane;
import Resources.Hotel;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Main {
    public static void main(String args[]) throws RemoteException, NotBoundException {
        Merchant TAM = new Merchant(Integer.parseInt(args[0]));
        Airplane plane = new Airplane();
        plane.flight_number = "TAM3331";

        plane = (Airplane) TAM.postResource(plane);

        plane.flight_number = "TAM2111";
        Airplane plane2 = (Airplane) TAM.postResource(plane);

        System.out.println(plane2.flight_number + plane2.id);
    }
}
