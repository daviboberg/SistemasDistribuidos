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
            Main.postHotel(new Hotel());
            return;
        }

        if (action.equals("inserir") && type.equals("voo")) {
            Main.postAirplane(new Airplane());
            return;
        }

        if (action.equals("inserir") && type.equals("pacote")) {
            //Main.postPackage(new Package());
            return;
        }

        if (action.equals("atualizar") && type.equals("hotel")) {
            Hotel hotel = new Hotel();
            hotel.id = getId();
            Main.postHotel(hotel);
            return;
        }

        if (action.equals("atualizar") && type.equals("voo")) {
            Airplane airplane = new Airplane();
            airplane.id = getId();
            Main.postAirplane(airplane);
            return;
        }

        if (action.equals("atualizar") && type.equals("pacote")) {
//            Package package = new Package();
//            package.id = getId();
//            Main.postPackage(package);
            return;
        }

        if (action.equals("deletar") && type.equals("hotel")) {
            Hotel hotel = new Hotel();
            hotel.id = getId();
            merchant.deleteResource(hotel);
            return;
        }

        if (action.equals("deletar") && type.equals("voo")) {
            Airplane airplane = new Airplane();
            airplane.id = getId();
            merchant.deleteResource(airplane);
            return;
        }

        if (action.equals("deletar") && type.equals("pacote")) {
//            Package package = new Package();
//            package.id = getId();
//            merchant.deleteResource(package)
            return;
        }
    }

    private static int getId() {
        System.out.println("Digite o ID:");
        return Integer.parseInt(scanner.nextLine());
    }

    private static void postAirplane(Airplane airplane) throws RemoteException {
        System.out.println("Digite o número do voo:");
        airplane.flight_number = scanner.nextLine();
        System.out.println("Digite a origem:");
        airplane.origin = scanner.nextLine();
        System.out.println("Digite o destino:");
        airplane.destiny = scanner.nextLine();
        System.out.println("Digite o dia:");
        airplane.flight_date = scanner.nextLine();
        airplane = (Airplane) merchant.postResource(airplane);
        System.out.println("ID = " + airplane.getId());
    }

    private static void postHotel(Hotel hotel) throws RemoteException {
        System.out.println("Digite o nome do hotel:");
        hotel.name = scanner.nextLine();
        System.out.println("Digite a localidade:");
        hotel.location = scanner.nextLine();
        System.out.println("Digite o número de quartos:");
        hotel.room_number = Integer.parseInt(scanner.nextLine());
        System.out.println("Digite a capcidade de cada quarto:");
        hotel.capacity = Integer.parseInt(scanner.nextLine());
        System.out.println("Digite o preço:");
        hotel.price = Integer.parseInt(scanner.nextLine());
        System.out.println("Digite a data inicial:");
        hotel.available_initial_date = scanner.nextLine();
        System.out.println("Digite a data final:");
        hotel.available_end_date = scanner.nextLine();
        hotel = (Hotel) merchant.postResource(hotel);
        System.out.println("ID = " + hotel.getId());
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
