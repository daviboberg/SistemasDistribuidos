package Merchant;

import Resources.Airplane;
import Resources.Hotel;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Main {

    private static Scanner scanner;
    private static Merchant merchant;

    public static void main(String args[]) throws RemoteException, NotBoundException {
        merchant = new Merchant(Integer.parseInt(args[0]));
        scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Você deseja 'inserir', 'atualizar' ou 'deletar'?");
            String action = scanner.nextLine();
            String type = Main.chooseType();

            callAction(action, type);
        }
    }

    private static void callAction(String action, String type) throws RemoteException {
        if (action.equals("inserir") && type.equals("hotel")) {
            Main.insertHotel();
            return;
        }

        if (action.equals("inserir") && type.equals("voo")) {
            Main.insertVoo();
            return;
        }

        if (action.equals("inserir") && type.equals("pacote")) {
            //Main.insertPacote();
            return;
        }

        if (action.equals("atualizar") && type.equals("hotel")) {
            Main.insertHotel();
            return;
        }

        if (action.equals("atualizar") && type.equals("voo")) {
            Main.insertHotel();
            return;
        }

        if (action.equals("atualizar") && type.equals("pacote")) {
            Main.insertHotel();
            return;
        }

        if (action.equals("deletar") && type.equals("hotel")) {
            Main.insertHotel();
            return;
        }

        if (action.equals("deletar") && type.equals("voo")) {
            Main.insertHotel();
            return;
        }

        if (action.equals("deletar") && type.equals("pacote")) {
            Main.insertHotel();
            return;
        }
    }

    private static void insertVoo() throws RemoteException {
        Airplane airplane = new Airplane();
        //TODO
        merchant.postResource(airplane);
    }

    private static void insertHotel() throws RemoteException {
        Hotel hotel = new Hotel();
        //TODO
        merchant.postResource(hotel);
    }

    private static String chooseType() {
        System.out.println("Que tipo de recurso? [hotel, voo, pacote]");
        return scanner.nextLine();
    }

    private static void printHeader() {
        System.out.println("SugarFoot Tourism Agency");
        System.out.println("Made by Bruno Clemente and Davi Boberg.");
        System.out.println("");
        System.out.println("Merchant Area:");
        System.out.println("");
    }
}
