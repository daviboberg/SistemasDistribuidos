package Resources;

public class TravelPackageFilters {

    public String origin;
    public String destination;
    public String going_date;
    public String return_date;
    public int number_persons;
    public int number_rooms;
    public float price;

    public TravelPackageFilters(String origin, String destination, String going_date, String return_date, int number_persons, int number_rooms, float price) {
        this.origin = origin;
        this.destination = destination;
        this.going_date = going_date;
        this.return_date = return_date;
        this.number_persons = number_persons;
        this.number_rooms = number_rooms;
        this.price = price;
    }
}
