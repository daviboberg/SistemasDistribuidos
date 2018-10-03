package Merchant;

import Resources.Hotel;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Main {
    public static void main(String args[]) throws RemoteException, NotBoundException {
        Merchant airbnb = new Merchant(Integer.parseInt(args[0]));
        Hotel hotel = new Hotel();
        hotel.id = 12345;
        airbnb.postResource(hotel);
        airbnb.deleteResource(hotel);
    }
}
