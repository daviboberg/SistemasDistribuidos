package Resources;

public class HotelFilters {

    public String location;
    public int rooms;
    public float price;
    public String start_date;
    public String end_date;
    public int capacity;

    public HotelFilters(String location, String going_date, String return_date, int number_passengers, int number_rooms, float price) {

        this.location = location;
        this.start_date = going_date;
        this.end_date = return_date;
        this.capacity= number_passengers;
        rooms = number_rooms;
        this.price = price;
    }

    public HotelFilters() {

    }
}
