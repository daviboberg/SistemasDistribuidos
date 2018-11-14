package Client;

import Resources.*;

import java.net.URISyntaxException;
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
            boolean bought = tryToBuy(new Airplane(), airplanes, airplane_filters.seats);
            if (!bought) {
                System.out.println("Passagem de ida indisponível");
            }

            if (with_return) {
                System.out.println("Comprar passagem de volta");
                bought = tryToBuy(new Airplane(), airplanes_return, airplane_return_filters.seats);
                if (!bought) {
                    System.out.println("Passagem de volta indisponível");
                }
            }

        }

        if (type.equals(Main.TYPE_HOTEL)) {
            HotelFilters hotel_filters = new HotelFilters();
            Main.requestHotel(hotel_filters);
            List<Resource> hotels = Hotel.getHotelByFilters(hotel_filters);

            Main.showInformation(hotels, TYPE_HOTEL);

            tryToBookHotel(new Hotel(), hotels, hotel_filters.rooms, hotel_filters.start_date, hotel_filters.end_date);
        }

        if (type.equals(Main.TYPE_PACKAGE)) {
            HotelFilters hotel_filters = new HotelFilters();
            AirplaneFilters airplane_going_filter = new AirplaneFilters();
            AirplaneFilters airplane_return_filter = new AirplaneFilters();
            Main.requestPackage(hotel_filters, airplane_going_filter, airplane_return_filter);

            List<Resource> package_hotels = Hotel.getHotelByFilters(hotel_filters);
            Main.showInformation(package_hotels, Main.TYPE_HOTEL);
            System.out.println("Alugar quarto de hotel");
            tryToBookHotel(new Hotel(), package_hotels, hotel_filters.rooms, hotel_filters.start_date, hotel_filters.end_date);

            List<Resource> package_airplane_going = Airplane.getAirplanesByFilters(airplane_going_filter);
            Main.showInformation(package_airplane_going, Main.TYPE_AIRPLANE);
            System.out.println("Comprar passagem de ida");
            tryToBuy(new Airplane(), package_airplane_going, airplane_going_filter.numbet_of_seats);

            List<Resource> package_airplane_return = Airplane.getAirplanesByFilters(airplane_return_filter);
            Main.showInformation(package_airplane_return, Main.TYPE_AIRPLANE);
            System.out.println("Comprar passagem de volta");
            tryToBuy(new Airplane(), package_airplane_return, airplane_return_filter.numbet_of_seats);

        }
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

    private static void requestPackage(HotelFilters hotel_filters, AirplaneFilters airplane_going_filter, AirplaneFilters airplane_return_filter) {
        System.out.println("Digite origem:");
        String origin = scanner.nextLine();
        System.out.println("Digite destino:");
        String destination = scanner.nextLine();
        System.out.println("Digite data inicial:");
        String going_date = scanner.nextLine();
        System.out.println("Digite data final:");
        String return_date = scanner.nextLine();
        System.out.println("Digite número de pessoas:");
        int number_passengers = Integer.parseInt(scanner.nextLine());
        System.out.println("Digite número de quartos:");
        int number_rooms = Integer.parseInt(scanner.nextLine());
        System.out.println("Digite preço do hotel:");
        float price = Float.parseFloat(scanner.nextLine());
        System.out.println("Digite preço da passagem:");
        float price_airplane = Float.parseFloat(scanner.nextLine());

        hotel_filters.location = destination;
        hotel_filters.price = price;
        hotel_filters.capacity = number_passengers;
        hotel_filters.rooms = number_rooms;
        hotel_filters.start_date = going_date;
        hotel_filters.end_date = return_date;

        airplane_going_filter.origin = origin;
        airplane_going_filter.destination = destination;
        airplane_going_filter.seats = number_passengers;
        airplane_going_filter.date = going_date;
        airplane_going_filter.price = price_airplane;

        airplane_return_filter.origin = destination;
        airplane_return_filter.destination = origin;
        airplane_return_filter.seats = number_passengers;
        airplane_return_filter.date = return_date;
        airplane_return_filter.price = price_airplane;
    }

    private static boolean tryToBookHotel(Hotel hotel, List<Resource> hotels, int quantity_to_buy, String check_in_date, String check_out_date) throws URISyntaxException {
        if (hotels.size() <= 0) {
            return false;
        }

        int buy_id = Main.requestBuy();
        if (buy_id >= 0) {
            Hotel resource_to_buy = (Hotel)Main.findResourceInListById(hotels, buy_id);
            hotel.bookHotel(resource_to_buy, quantity_to_buy, check_in_date, check_out_date);
        }
        return true;
    }

    private static boolean tryToBuy(Resource resource, List<Resource> resources, int quantity_to_buy) throws URISyntaxException {

        if (resources.size() <= 0) {
            return false;
        }

        int buy_id = Main.requestBuy();
        if (buy_id >= 0) {
            Resource resource_to_buy = Main.findResourceInListById(resources, buy_id);
            resource.buy(resource_to_buy, quantity_to_buy);
        }
        return true;
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
        System.out.println(package_type);
        switch (package_type) {
            case TYPE_AIRPLANE:
                System.out.println("Passagens disponíveis: ");
                for (Resource resource: result) {
                    Airplane airplane = (Airplane) resource;
                    System.out.println("Id: " + airplane.id + " preço por passagem: " + airplane.price);
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
                    TravelPackage package_resource = (TravelPackage) resource;
                    System.out.println("Id: " + package_resource.id + " preço total: " + package_resource.price);
                }
                break;
        }
        System.out.println();
    }
}
