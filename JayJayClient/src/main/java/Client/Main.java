package Client;

import Resources.*;
import com.google.gson.Gson;

import javax.ws.rs.core.Response;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final String TYPE_HOTEL = "hospedagem";
    private static final String TYPE_AIRPLANE = "passagem";
    private static final String TYPE_PACKAGE = "pacote";
    private static Scanner scanner = new Scanner(System.in);

    private static List<String> package_type = Arrays.asList("pacote", "hospedagem", "passagem", "interesse");
    private static String package_type_message = "pacote, hospedagem ou passagem?";

    private static Client client;

    public static void main(String[] args) throws URISyntaxException {
        client = new Client();
//        Main.exemplo();

        printHeader();

        while(true) {
            String package_type = Main.chooseOption(Main.package_type, Main.package_type_message);
            Main.callAction(package_type);
        }
    }

    private static void printHeader() {
        System.out.println("JayJay Tourism Agency");
        System.out.println("Made by Bruno Clemente and Davi Boberg.");
        System.out.println();
    }


    private static void callAction(String type) throws URISyntaxException {
        if (type.equals(Main.TYPE_AIRPLANE)) {
            boolean with_return = Main.withReturn();

            List<Resource> airplanes_return = null;
            AirplaneFilters airplane_return_filters = null;

            AirplaneFilters airplane_filters = new AirplaneFilters();
            Main.requestAirplaneGoingInformation(airplane_filters);
            List<Resource> airplanes = Airplane.getAirplanesByFilters(airplane_filters);
            Main.showInformation(airplanes, Main.TYPE_AIRPLANE);

            if (with_return) {
                airplane_return_filters = new AirplaneFilters();
                airplane_return_filters.origin = airplane_filters.destination;
                airplane_return_filters.destination = airplane_filters.origin;
                Main.requestAirplaneReturnInformation(airplane_return_filters);
                airplanes_return = Airplane.getAirplanesByFilters(airplane_return_filters);
                Main.showInformation(airplanes_return, Main.TYPE_AIRPLANE);

            }

            System.out.println("Comprar passagem de ida");
            tryToBuy(new Airplane(), airplanes);

            if (with_return) {
                System.out.println("Comprar passagem de volta");
                tryToBuy(new Airplane(), airplanes_return);
            }

        }

        if (type.equals(Main.TYPE_HOTEL)) {
            HotelFilters hotel_filters = new HotelFilters();
            Main.requestHotel(hotel_filters);
            List<Resource> hotels = Hotel.getHotelByFilters(hotel_filters);
            Main.showInformation(hotels, TYPE_HOTEL);

            tryToBuy(new Hotel(), hotels);
        }
//
//        if (type.equals(Main.TYPE_PACKAGE)) {
//            PackageResource package_resource = new PackageResource();
//            Main.requestPackage(package_resource);
//            List<Resource> packages = client.getInformation(package_resource);
//            Main.showInformation(packages, Main.TYPE_PACKAGE);
//
//            tryToBuy(package_resource, packages);
//        }
    }

    private static void requestAirplaneGoingInformation(AirplaneFilters airplane_filters) {
        System.out.println("Consulta passagem Ida");
        System.out.println("Digite origem:");
        airplane_filters.origin = scanner.nextLine();
        System.out.println("Digite destino:");
        airplane_filters.destination = scanner.nextLine();
        System.out.println("Digite data:");
        airplane_filters.date = scanner.nextLine();
        System.out.println("Digite numero passagens:");
        airplane_filters.seats = Integer.parseInt(scanner.nextLine());
        System.out.println("Digite preço:");
        airplane_filters.price = Float.parseFloat(scanner.nextLine());
    }

    private static void requestAirplaneReturnInformation(AirplaneFilters airplane_filters) {
        System.out.println("Passagem Volta");
        System.out.println("Digite data:");
        airplane_filters.date = scanner.nextLine();
        System.out.println("Digite numero passagens:");
        airplane_filters.seats = Integer.parseInt(scanner.nextLine());
        System.out.println("Digite preço:");
        airplane_filters.price = Float.parseFloat(scanner.nextLine());
    }

    private static void requestHotel(HotelFilters hotel_filters) {
        System.out.println("Consulta hotel");
        System.out.println("Digite localização:");
        hotel_filters.location = scanner.nextLine();
        System.out.println("Digite data entrada:");
        hotel_filters.start_date = scanner.nextLine();
        System.out.println("Digite data saida:");
        hotel_filters.end_date = scanner.nextLine();
        System.out.println("Digite numero de pessoas:");
        hotel_filters.capacity = Integer.parseInt(scanner.nextLine());
        System.out.println("Digite numero de quartos:");
        hotel_filters.rooms = Integer.parseInt(scanner.nextLine());
        System.out.println("Digite preço:");
        hotel_filters.price = Float.parseFloat(scanner.nextLine());
    }

    private static void tryToBuy(Resource resource, List<Resource> resources) throws URISyntaxException {
        if (resources.size() > 0) {
            int buy_id = Main.requestBuy();
            if (buy_id >= 0) {
                Resource resource_to_buy = Main.findResourceInListById(resources, buy_id);
                resource.buy(resource_to_buy);
            }
        }
    }

    private static Resource findResourceInListById(List<Resource> resources, int id) {
        for (Resource resource : resources) {
            if (resource.getId() == id) {
                return resource;
            }
        }
        return null;
    }

    private static int requestBuy() {
        System.out.println("Deseja comprar algum item listado? (sim/nao)");
        String question = scanner.nextLine();
        if (!question.equals("sim")) {
            return -1;
        }

        System.out.println("Digite o Id do item desejado");
        return Integer.parseInt(scanner.nextLine());
    }

    private static boolean withReturn() {
        System.out.println("Ida ou Ida e volta? (ida=1, ida e volta=2)");
        return Integer.parseInt(Main.scanner.nextLine()) == 2;
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


    private static void showInformation(List<Resource> result, String package_type) {
        switch (package_type) {
            case TYPE_AIRPLANE:
                System.out.println("Passagens disponíveis: ");
                for (Resource resource: result) {
                    Airplane airplane = (Airplane) resource;
                    System.out.println("Id: " + airplane.id + " preço por passagem: " + airplane.price);
                }
                break;
//            case Main.TYPE_HOTEL:
//                System.out.println("Hospedagem disponíveis: ");
//                for (Resource resource: result) {
//                    Hotel hotel = (Hotel) resource;
//                    System.out.println("Id: " + hotel.id + " preço total: " + hotel.price);
//                }
//                break;
//            case TYPE_PACKAGE:
//                System.out.println("Pacotes disponíveis: ");
//                for (Resource resource: result) {
//                    PackageResource package_resource = (PackageResource) resource;
//                    System.out.println("Id: " + package_resource.id + " preço total: " + package_resource.price);
//                }
//                break;
        }
        System.out.println();
    }
}