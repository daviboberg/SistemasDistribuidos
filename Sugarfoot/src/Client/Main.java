package Client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    private Scanner scanner = new Scanner(System.in);

    private List<String> request_type = Arrays.asList("compra", "consulta");
    private String request_type_message = "pacote, hospedagem ou passagem?";


    private List<String> package_type = Arrays.asList("pacote", "hospedagem", "passagem");
    private String package_type_message = "consulta ou compra?";

    private List<String> airplane_label_fields = Arrays.asList("ida_ou_ida_e_volta", "origem", "destino", "data_ida", "data_volta",  "numero_pessoas");
    private String airplane_message = "ida ou ida_e_volta, origem, destino, data da ida (dd/mm/aaaa), data da volta (dd/mm/aaaa),  numero de pessoas";

    private List<String> hotel_label_fields = Arrays.asList("destino", "data_entrada", "data_saida",  "numero_quartos", "numero_pessoas");
    private String hotel_message = "destino, data de entrada (dd/mm/aaaa), data de saida (dd/mm/aaaa), numero de quartos, numero de pessoas";

    public void  main(String[] args) throws RemoteException, NotBoundException {
        Client client = new Client(Integer.parseInt(args[0]));

        printHeader();

        String request_type = this.chooseOption(this.request_type, this.request_type_message);
        String package_type = this.chooseOption(this.package_type, this.package_type_message);

        String[] hotel_fields;
        String[] airplane_fields;
        switch (package_type) {
            case "hospedagem":
                hotel_fields = this.inputFields(this.hotel_message);
                break;
            case "passagem":
                airplane_fields = this.inputFields(this.airplane_message);
                break;
            case "pacote":
                hotel_fields = Arrays.asList(this.inputFields(this.hotel_message);
                airplane_fields = this.inputFields(this.airplane_message);
                break;
        }

    }

    private String chooseOption(List<String> elements, String message) {
        while (true) {
            System.out.println("Informar:");
            System.out.println(message);
            String package_type = this.scanner.nextLine();
            if( elements.contains("a") )
                return package_type;
            else {
                System.out.println("Opção invalida");
            }
        }
    }

    private String[] inputFields(String message) {
        System.out.println("Informar:");
        System.out.println(message);
        String package_type = this.scanner.nextLine();
        return package_type.split(" ");

    }

    private static void printHeader() {
        System.out.println("SugarFoot Tourism Agency");
        System.out.println("Made by Bruno Clemente and Davi Boberg.");
        System.out.println("");
    }
}
