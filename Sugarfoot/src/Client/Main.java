package Client;

import Resources.Airplane;
import Resources.Hotel;
import Resources.PackageResource;
import Resources.Resource;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);

    private static List<String> request_type = Arrays.asList("compra", "consulta");
    private static String request_type_message = "consulta ou compra?";


    private static List<String> package_type = Arrays.asList("pacote", "hospedagem", "passagem");
    private static String package_type_message = "pacote, hospedagem ou passagem?";

    private static List<String> airplane_label_fields = Arrays.asList("ida_ou_ida_e_volta", "origem", "destino", "data_ida", "data_volta",  "numero_pessoas");
    private static String airplane_message = "ida ou ida_e_volta, origem, destino, data da ida (dd/mm/aaaa), data da volta (dd/mm/aaaa),  numero de pessoas";

    private static List<String> hotel_label_fields = Arrays.asList("destino", "data_entrada", "data_saida",  "numero_quartos", "numero_pessoas");
    private static String hotel_message = "destino, data de entrada (dd/mm/aaaa), data de saida (dd/mm/aaaa), numero de quartos, numero de pessoas";

    public static void  main(String[] args) throws RemoteException, NotBoundException, SQLException {
        Client client = new Client(Integer.parseInt(args[0]));

        printHeader();

        while(true) {
            String request_type = Main.chooseOption(Main.request_type, Main.request_type_message);
            String package_type = Main.chooseOption(Main.package_type, Main.package_type_message);

            Airplane airplane_going = null;
            Airplane airplane_return = null;
            Hotel hotel = null;
            String[] hotel_fields;
            String[] airplane_fields;
            switch (package_type) {
                case "hospedagem":
                    hotel_fields = Main.inputFields(Main.hotel_message);
                    hotel = Main.createHotel(hotel_fields);
                    break;
                case "passagem":
                    airplane_fields = Main.inputFields(Main.airplane_message);
                    airplane_going = Main.createAirplane(airplane_fields, false);
                    if (airplane_fields[0].equals("ida_e_volta")) {
                        airplane_return = Main.createAirplane(airplane_fields, true);
                    }
                    break;
                case "pacote":
                    hotel_fields = Main.inputFields(Main.hotel_message);
                    hotel = Main.createHotel(hotel_fields);
                    break;
            }


            processInputs(client, request_type, package_type, airplane_going, airplane_return, hotel);
        }

    }

    private static void processInputs(Client client, String request_type, String package_type, Airplane airplane_going, Airplane airplane_return, Hotel hotel) throws RemoteException, SQLException {
        Resource resource = null;
        if (package_type.equals("passagem")) {
            resource = airplane_going;

        } else if (package_type.equals("hospedagem")) {
            resource = hotel;

        } else if (package_type.equals("pacote")) {
            PackageResource type_package = new PackageResource();
            type_package.hotel = hotel;
            type_package.aiplane_going = airplane_going;
            type_package.airplane_return = airplane_return;
            resource = type_package;
        }

        List<Resource> result = null;
        if (request_type.equals("consulta")) {
            result = client.getInformation(resource);
            Main.showInformation(result, package_type);
        } else if (request_type.equals("compra")) {
            client.postOrder(resource);
        }
    }

    private static void showInformation(List<Resource> result, String package_type) {
        switch (package_type) {
            case "passagem":
                System.out.println("Passagens disponíveis: ");
                for (Resource resource: result) {
                    Airplane airplane = (Airplane) resource;
                    System.out.println("Passagem: ida, origem: " + airplane.origin + " destino: " + airplane.destiny + " data ida: " + airplane.flight_date + " assentos disponíveis: " + airplane.passengers);
                }
                break;
            case "hotel":
                break;
            case "pacote":
                break;
        }
        System.out.println();
    }

    private static Airplane createAirplane(String[] airplane_fields, boolean is_return) {
        Airplane airplane;
        String date = is_return ? airplane_fields[4] : airplane_fields[3];
        airplane = new Airplane(airplane_fields[0], airplane_fields[1], airplane_fields[2], date, Integer.parseInt(airplane_fields[5]));
        return airplane;
    }

    private static Hotel createHotel(String[] hotel_fields) {
        Hotel hotel;
        hotel = new Hotel(hotel_fields[0], hotel_fields[1], hotel_fields[2]);
        return hotel;
    }

    private static String chooseOption(List<String> elements, String message) {
        while (true) {
            System.out.println("Informar:");
            System.out.println(message);
            String package_type = Main.scanner.nextLine();
            if(elements.contains(package_type) )
                return package_type;
            else {
                System.out.println("Opção invalida");
            }
        }
    }

    private static String[] inputFields(String message) {
        System.out.println("Informar:");
        System.out.println(message);
        String package_type = Main.scanner.nextLine();
        return package_type.split(" ");

    }

    private static void printHeader() {
        System.out.println("SugarFoot Tourism Agency");
        System.out.println("Made by Bruno Clemente and Davi Boberg.");
        System.out.println("");
    }
}
