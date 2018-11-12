package Client;

import Resources.Airplane;
import Resources.Resource;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import javax.ws.rs.core.Response;
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
    private static String package_type_message = "pacote, hospedagem, passagem ou interesse?";

    private static Client client;

    public static void  main(String[] args) {
        client = new Client();
        Main.exemplo();
    }

    public static void exemplo() {

        Gson gson = new Gson();
        ArrayList<Resource> all_airplanes = client.getAirplanes();
        System.out.println(gson.toJson(all_airplanes));

        Airplane airplane = new Airplane();
        airplane.destination = "Joinville";
        airplane.origin = "Curitiba";
        airplane.seats = 3;
        airplane.price = 100;
        airplane.date = "2018-11-07";
        ArrayList<Resource> airplanes = client.getAirplanesByFilters(airplane);

        System.out.println(gson.toJson(airplanes));

        Response response1 = client.buyTicket((Airplane) airplanes.get(0));
        String output1 = response1.readEntity(String.class);
        System.out.println(output1);
    }

}
