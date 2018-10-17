package Resources;

import java.util.Date;

public class AirplaneVO {

    public int id;
    public String flight_number;
    public String origin;
    public String desntiny;
    public Date flight_date;

    public AirplaneVO(int id, String flight_number, String origin, String desntiny, Date flight_date) {
        this.id = id;
        this.flight_number = flight_number;
        this.origin = origin;
        this.desntiny = desntiny;
        this.flight_date = flight_date;
    }
}
