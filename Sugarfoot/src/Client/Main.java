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

    private static final String TYPE_HOTEL = "hospedagem";
    private static final String TYPE_AIRPLANE = "passagem";
    private static final String TYPE_PACKAGE = "pacote";
    private static Scanner scanner = new Scanner(System.in);

    private static List<String> package_type = Arrays.asList("pacote", "hospedagem", "passagem", "interesse");
    private static String package_type_message = "pacote, hospedagem, passagem ou interesse?";
    
    private static Client client;
    public static void  main(String[] args) throws RemoteException, NotBoundException, SQLException {
        client = new Client(Integer.parseInt(args[0]));

        printHeader();

        while(true) {
            String package_type = Main.chooseOption(Main.package_type, Main.package_type_message);

            if (package_type.equals("interesse")) {
                Main.getInterest();
            } else {
                Main.callAction(package_type);
            }
        }

    }

    private static void callAction(String type) throws RemoteException, SQLException {
        if (type.equals(Main.TYPE_AIRPLANE)) {
            boolean with_return = Main.witReturn();

            List<Resource> airplanes_return = null;
            Airplane airplane_return = null;

            Airplane airplane = new Airplane();
            Main.requestAirplaneGoingInformation(airplane);
            List<Resource> airplanes = client.getInformation(airplane);
            Main.showInformation(airplanes, Main.TYPE_AIRPLANE);

            if (with_return) {
                airplane_return = new Airplane();
                airplane_return.origin = airplane.destiny;
                airplane_return.destiny = airplane.origin;
                Main.requestAirplaneReturnInformation(airplane_return);
                airplanes_return = client.getInformation(airplane_return);
                Main.showInformation(airplanes_return, Main.TYPE_AIRPLANE);

            }

            System.out.println("Comprar passagem de ida");
            tryToBuy(airplane, airplanes);

            if (with_return) {
                System.out.println("Comprar passagem de volta");
                tryToBuy(airplane_return, airplanes_return);
            }

        }

        if (type.equals(Main.TYPE_HOTEL)) {
            Hotel hotel = new Hotel();
            Main.requestHotel(hotel);
            List<Resource> hotels = client.getInformation(hotel);
            Main.showInformation(hotels, TYPE_HOTEL);

            tryToBuy(hotel, hotels);
        }

        if (type.equals(Main.TYPE_PACKAGE)) {
            PackageResource package_resource = new PackageResource();
            Main.requestPackage(package_resource);
            List<Resource> packages = client.getInformation(package_resource);
            Main.showInformation(packages, Main.TYPE_PACKAGE);

            tryToBuy(package_resource, packages);
        }
    }

    private static void tryToBuy(Resource resource, List<Resource> resources) throws RemoteException {
        if (resources.size() > 0) {
            int buy_id = Main.requestBuy();
            if (buy_id >= 0) {
                resource.setId(buy_id);
                Main.buy(resources, resource);
            }
        }
    }

    private static void buy(List<Resource> resources, Resource desired) throws RemoteException {
        for (Resource resource: resources) {
            if (resource.equals(desired)) {
                client.postOrder(desired);
                System.out.println("Item com Id: " + resource.getId() + " comprado com sucesso");
                System.out.println();
            }
        }
    }

    private static void requestPackage(PackageResource package_resource) {
        System.out.println("Digite origem:");
        String origin = scanner.nextLine();
        System.out.println("Digite destino:");
        String destiny = scanner.nextLine();
        System.out.println("Digite data inicial:");
        String initial_date = scanner.nextLine();
        System.out.println("Digite data final:");
        String end_date = scanner.nextLine();
        System.out.println("Digite número de pessoas:");
        int number_passengers = Integer.parseInt(scanner.nextLine());
        System.out.println("Digite número de quartos:");
        int number_rooms = Integer.parseInt(scanner.nextLine());
        System.out.println("Digite preço:");
        float price = Float.parseFloat(scanner.nextLine());

        Hotel hotel = new Hotel(destiny, initial_date, end_date, number_passengers, number_rooms);
        Airplane airplane_going = new Airplane(origin, destiny, initial_date, number_passengers);
        Airplane airplane_return = new Airplane(destiny, origin, end_date, number_passengers);
        package_resource.hotel = hotel;
        package_resource.airplane_going = airplane_going;
        package_resource.airplane_return = airplane_return;
        package_resource.price = price;
    }

    private static void requestAirplaneGoingInformation(Airplane airplane) {
        System.out.println("Consulta passagem Ida");
        System.out.println("Digite origem:");
        airplane.origin = scanner.nextLine();
        System.out.println("Digite destino:");
        airplane.destiny = scanner.nextLine();
        System.out.println("Digite data:");
        airplane.flight_date = scanner.nextLine();
        System.out.println("Digite numero passagens:");
        airplane.available_seats = Integer.parseInt(scanner.nextLine());
        System.out.println("Digite preço:");
        airplane.price = Float.parseFloat(scanner.nextLine());
    }

    private static void requestAirplaneReturnInformation(Airplane airplane) {
        System.out.println("Passagem Volta");
        System.out.println("Digite data:");
        airplane.flight_date = scanner.nextLine();
        System.out.println("Digite numero passagens:");
        airplane.available_seats = Integer.parseInt(scanner.nextLine());
        System.out.println("Digite preço:");
        airplane.price = Float.parseFloat(scanner.nextLine());
    }

    private static void requestHotel(Hotel hotel) {
        System.out.println("Consulta hotel");
        System.out.println("Digite localização:");
        hotel.location = scanner.nextLine();
        System.out.println("Digite data entrada:");
        hotel.available_initial_date = scanner.nextLine();
        System.out.println("Digite data saida:");
        hotel.available_end_date = scanner.nextLine();
        System.out.println("Digite numero de pessoas:");
        hotel.capacity = Integer.parseInt(scanner.nextLine());
        System.out.println("Digite numero de quartos:");
        hotel.room_number = Integer.parseInt(scanner.nextLine());
        System.out.println("Digite preço:");
        hotel.price = Float.parseFloat(scanner.nextLine());
    }

    private static int requestBuy() {
        System.out.println("Deseja comprar algum item listaod? (sim/nao)");
        String question = scanner.nextLine();
        if (!question.equals("sim")) {
            return -1;
        }

        System.out.println("Digite o Id do item desejado");
        return Integer.parseInt(scanner.nextLine());
    }

    private static boolean witReturn() {
        System.out.println("Ida ou Ida e volta? (ida=1, ida e volta=2)");
        return Integer.parseInt(Main.scanner.nextLine()) == 2;
    }

    private static void showInformation(List<Resource> result, String package_type) {
        switch (package_type) {
            case TYPE_AIRPLANE:
                System.out.println("Passagens disponíveis: ");
                for (Resource resource: result) {
                    Airplane airplane = (Airplane) resource;
                    System.out.println("Id: " + airplane.id + " preço total: " + airplane.price);
                }
                break;
            case Main.TYPE_HOTEL:
                System.out.println("Hospedagem disponíveis: ");
                for (Resource resource: result) {
                    Hotel hotel = (Hotel) resource;
                    System.out.println("Id: " + hotel.id + " preço total: " + hotel.price);
                }
                break;
            case TYPE_PACKAGE:
                System.out.println("Pacotes disponíveis: ");
                for (Resource resource: result) {
                    PackageResource package_resource = (PackageResource) resource;
                    System.out.println("Id: " + package_resource.id + " preço total: " + package_resource.price);
                }
                break;
        }
        System.out.println();
    }

    private static void printHeader() {
        System.out.println("SugarFoot Tourism Agency");
        System.out.println("Made by Bruno Clemente and Davi Boberg.");
        System.out.println();
    }

    private static String chooseOption(List<String> elements, String message) {
        while (true) {
            System.out.println();
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
