package Client;

import Resources.Airplane;
import Resources.Hotel;
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

            Airplane airplane_ida = null;
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
                    airplane_ida = Main.createAirplane(airplane_fields, false);
                    if (airplane_fields[0].equals("ida_e_volta")) {
                        airplane_return = Main.createAirplane(airplane_fields, true);
                    }
                    break;
                case "pacote":
                    hotel_fields = Main.inputFields(Main.hotel_message);
                    hotel = Main.createHotel(hotel_fields);
                    break;
            }


//            Main.request(request_type, package_type, airplane_ida, airplane_return, hotel);
            List<Resource> available_airplanes = client.getInformation(airplane_ida);
            System.out.println(available_airplanes);
        }

    }

//    private static void request(String request_type, String package_type, Airplane airplane_ida, Airplane airplane_return, Hotel hotel) {
//        if (request_type.equals("consulta")) {
//            Main.showInformation(package_type, airplane_ida, airplane_return, hotel);
//        } else if(request_type.equals("compra")) {
//            Main.postOrder(package_type, airplane_ida, airplane_ida, hotel);
//        }
//    }
//
//    private static void postOrder(String package_type, Airplane airplane_ida, Airplane airplane_return, Hotel hotel) {
//        if (package_type.equals("passagem")) {
//            Main.postOrderAviao(airplane_ida, airplane_return);
//        }
//    }
//
//    private static void showInformation(String package_type, Airplane airplane_ida, Airplane airplane_return, Hotel hotel) {
//
//    }

    private static Airplane createAirplane(String[] airplane_fields, boolean is_return) {
        Airplane airplane;
        String date = is_return ? airplane_fields[3] : airplane_fields[4];
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
