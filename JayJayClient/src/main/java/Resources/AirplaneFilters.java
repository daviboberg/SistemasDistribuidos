package Resources;

public class AirplaneFilters {
    public String flight_number;
    public String origin;
    public String destination;
    public int seats;
    public float price;
    public String date;
    public int number_passengers;
    public int numbet_of_seats;

    public AirplaneFilters(String origin, String destination, String going_date, int number_passengers, float price) {

        this.origin = origin;
        this.destination = destination;
        this.date = going_date;
        this.number_passengers = number_passengers;
        this.price = price;
    }

    public AirplaneFilters() {

    }
}
