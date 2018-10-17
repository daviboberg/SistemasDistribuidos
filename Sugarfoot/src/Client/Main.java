package Client;


import Resources.Airplane;
import Resources.Hotel;
import Resources.PackageResource;
import Resources.Resource;
import Resources.*;

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

    private static Client client;
    public static void  main(String[] args) throws RemoteException, NotBoundException, SQLException {
        client = new Client(Integer.parseInt(args[0]));

        printHeader();

        while(true) {
            String request_type = Main.chooseOption(Main.request_type, Main.request_type_message);
            String package_type = Main.chooseOption(Main.package_type, Main.package_type_message);


            Main.callAction(request_type, package_type);
        }

    }

    private static void callAction(String action, String type) throws RemoteException, SQLException {
        if (type.equals("passagem")) {
            boolean ida_e_volta = Main.idaEVolta();

            if (!ida_e_volta) {
                Main.consultaAirplaneIda(new Airplane());
            }
        }

        if (action.equals("consulta") && type.equals("hotel")) {
            Main.consultaHotel(new Hotel());
        }
    }

    private static void consultaAirplaneIda(Airplane airplane) throws RemoteException, SQLException {
        System.out.println("Digite origem:");
        airplane.origin = scanner.nextLine();
        System.out.println("Digite destino:");
        airplane.destiny = scanner.nextLine();
        System.out.println("Digite data:");
        airplane.flight_date = scanner.nextLine();
        System.out.println("Digite numero passagens:");
        airplane = (Airplane) client.getInformation(airplane);
        System.out.println("ID = " + airplane.getId());
    }

    private static void consultaHotel(Hotel hotel) throws RemoteException, SQLException {
        System.out.println("Digite location:");
        hotel.location = scanner.nextLine();
        System.out.println("Digite data entrada:");
        hotel.available_initial_date = scanner.nextLine();
        System.out.println("Digite data saida:");
        hotel.available_end_date = scanner.nextLine();
        System.out.println("Digite numero de pessoas:");
        hotel.capacity = Integer.parseInt(scanner.nextLine());
        System.out.println("Digite numero de quartos:");
        hotel.number_rooms = Integer.parseInt(scanner.nextLine());
        hotel = (Hotel) client.getInformation(hotel);
        System.out.println("ID = " + hotel.getId());
    }

    private static boolean idaEVolta() {
        System.out.println("Ida ou Ida e volta? (ida=1, ida e volta=2)");
        return Integer.parseInt(Main.scanner.nextLine()) == 2 ? true : false;
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
            type_package.airplane_going = airplane_going;
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
                    System.out.println("Passagem: ida, origem: " + airplane.origin + " destino: " + airplane.destiny + " data ida: " + airplane.flight_date + " assentos disponíveis: " + airplane.available_seats);
                }
                break;
            case "hotel":
                break;
            case "pacote":
                break;
        }
        System.out.println();
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

    private static void getInterest() throws RemoteException {
        String read;
        Interest interest = new Interest();
        System.out.println("Você tem interesse em Passagens? [sim/não]");
        read = scanner.nextLine();
        if (read.equals("sim")) {
            interest.references.add(Reference.AIRPLANE);
        }
        System.out.println("Você tem interesse em Hoteis? [sim/não]");
        read = scanner.nextLine();
        if (read.equals("sim")) {
            interest.references.add(Reference.HOTEL);
        }
        System.out.println("Você tem interesse em Pacotes? [sim/não]");
        read = scanner.nextLine();
        if (read.equals("sim")) {
            interest.references.add(Reference.PACKAGE);
        }
        System.out.println("Digite o destino:");
        interest.destiny = scanner.nextLine();
        System.out.println("Digite o preço máximo:");
        interest.price = Float.parseFloat(scanner.nextLine());
        interest.client = new ClientService();

        client.postInterest(interest);
    }
}
