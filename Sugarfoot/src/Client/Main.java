package Client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    static private Scanner scanner = new Scanner(System.in);

    static private List<String> request_type = Arrays.asList("compra", "consulta");
    static private String request_type_message = "pacote, hospedagem ou passagem?";


    static private List<String> package_type = Arrays.asList("pacote", "hospedagem", "passagem");
    static private String package_type_message = "consulta ou compra?";

    static private List<String> airplane_label_fields = Arrays.asList("ida_ou_ida_e_volta", "origem", "destino", "data_ida", "data_volta",  "numero_pessoas");
    static private String airplane_message = "ida ou ida_e_volta, origem, destino, data da ida (dd/mm/aaaa), data da volta (dd/mm/aaaa),  numero de pessoas";

    static private List<String> hotel_label_fields = Arrays.asList("destino", "data_entrada", "data_saida",  "numero_quartos", "numero_pessoas");
    static private String hotel_message = "destino, data de entrada (dd/mm/aaaa), data de saida (dd/mm/aaaa), numero de quartos, numero de pessoas";

    public static void main(String[] args) throws RemoteException, NotBoundException {
        Client client = new Client(Integer.parseInt(args[0]));

        printHeader();

        String request_type = Main.chooseOption(Main.request_type, Main.request_type_message);
        String package_type = Main.chooseOption(Main.package_type, Main.package_type_message);

        String[] hotel_fields;
        String[] airplane_fields;
        switch (package_type) {
            case "hospedagem":
                hotel_fields = Main.inputFields(Main.hotel_message);
                break;
            case "passagem":
                airplane_fields = Main.inputFields(Main.airplane_message);
                break;
            case "pacote":
                //hotel_fields = Arrays.asList(Main.inputFields(Main.hotel_message));
                airplane_fields = Main.inputFields(Main.airplane_message);
                break;
        }

    }

    static private String chooseOption(List<String> elements, String message) {
        while (true) {
            System.out.println("Informar:");
            System.out.println(message);
            String package_type = Main.scanner.nextLine();
            if( elements.contains("a") )
                return package_type;
            else {
                System.out.println("Opção invalida");
            }
        }
    }

    static private String[] inputFields(String message) {
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
