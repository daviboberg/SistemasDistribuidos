package Resources;

import Comunication.InterfaceClient;

import java.io.Serializable;
import java.util.ArrayList;

public class Interest implements Serializable {

    ArrayList<String> references;
    String destiny;
    Float price;
    InterfaceClient client;


    public enum References {
        AIRPLANE,
        HOTEL,
        PACKAGE
    }
}
