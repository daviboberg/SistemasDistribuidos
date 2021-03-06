package Resources;

import Server.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Airplane implements Resource {
    public int id;
    public String flight_number;
    public String origin;
    public String destiny;
    public String flight_date;
    public int available_seats;
    public float price;

    /**
     * @param flight_number
     * @param origin
     * @param destiny
     * @param flight_date
     * @param available_seats
     * @param price
     */
    private Airplane(String flight_number, String origin, String destiny, String flight_date, int available_seats, float price) {
        this.flight_number = flight_number;
        this.origin = origin;
        this.destiny = destiny;
        this.flight_date = flight_date;
        this.available_seats = available_seats;
        this.price = price;
    }

    /**
     *
     */
    public Airplane() {
    }

    /**
     * @param origin
     * @param destiny
     * @param date
     * @param number_passengers
     */
    public Airplane(String origin, String destiny, String date, int number_passengers) {
        this.origin = origin;
        this.destiny = destiny;
        this.flight_date = date;
        this.available_seats = number_passengers;
    }

    /**
     * @param resource
     * @return
     */
    @Override
    public boolean equals(Resource resource) {
        return this.id == resource.getId();
    }

    /**
     * @return
     */
    @Override
    public int getId() {
        return this.id;
    }

    /**
     * @param id
     */
    @Override
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     */
    @Override
    public void insert() {
        String query = "INSERT INTO airplane (`flight_number`, `origin`, `destiny`, `flight_date`, `available_seats`, `price`) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = DatabaseConnection.getStatement(query);
        try {
            statement.setString(1, this.flight_number);
            statement.setString(2, this.origin);
            statement.setString(3, this.destiny);
            statement.setString(4, this.flight_date);
            statement.setInt(5, this.available_seats);
            statement.setFloat(6, this.price);
            statement.execute();
            this.id = DatabaseConnection.getLastInsertedId();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    @Override
    public void delete() {
        String query = "DELETE FROM airplane WHERE id = ?";
        PreparedStatement statement = DatabaseConnection.getStatement(query);

        try {
            statement.setInt(1, this.id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    @Override
    public void update() {
        String query = "UPDATE airplane SET `flight_number` = ?, `origin` = ?, `destiny` = ?, `flight_date` = ?, `available_seats` = ?, `price` = ? WHERE id = ?";
        PreparedStatement statement = DatabaseConnection.getStatement(query);
        try {
            statement.setString(1, this.flight_number);
            statement.setString(2, this.origin);
            statement.setString(3, this.destiny);
            statement.setString(4, this.flight_date);
            statement.setInt(5, this.available_seats);
            statement.setFloat(6, this.price);
            statement.setInt(7, this.id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return
     */
    @Override
    public Reference getReference() {
        return Reference.AIRPLANE;
    }

    /**
     * @return
     */
    public String getDestiny(){
        return this.destiny;
    }

    /**
     * @return
     */
    public float getPrice() {
        return this.price;
    }

    /**
     * @return
     * @throws SQLException
     */
    @Override
    public List<Resource> find() throws SQLException {
        String query = "SELECT * FROM airplane WHERE origin = ? AND destiny = ? AND flight_date = ? AND available_seats >= ?";
        PreparedStatement statement = DatabaseConnection.getStatement(query);
        assert statement != null;
        statement.setString(1, origin);
        statement.setString(2, destiny);
        statement.setString(3, flight_date);
        statement.setInt(4, available_seats);
        ResultSet result = statement.executeQuery();
        List<Resource> airplanes = new ArrayList<>();
        while (result.next()) {
            int id = result.getInt("id");
            String flight_number = result.getString("flight_number");
            origin = result.getString("origin");
            destiny = result.getString("destiny");
            flight_date = result.getString("flight_date");
            int available_seats = result.getInt("available_seats");
            float price = result.getFloat("price");
            price = price * this.available_seats;
            Airplane newAirplane = new Airplane(flight_number, origin, destiny, flight_date, available_seats, price);
            newAirplane.id = id;
            airplanes.add(newAirplane);
        }

        return airplanes;
    }

    /**
     *
     */
    @Override
    public void buy() {
        String query = "UPDATE airplane SET `available_seats` = (SELECT available_seats - ? FROM airplane WHERE id = ?) WHERE id = ?";
        PreparedStatement statement = DatabaseConnection.getStatement(query);
        try {
            statement.setInt(1, this.available_seats);
            statement.setInt(2, this.id);
            statement.setInt(3, this.id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return
     * @throws SQLException
     */
    public static List<Airplane> getAll() throws SQLException {
        String query = "SELECT * FROM airplane";
        PreparedStatement statement = DatabaseConnection.getStatement(query);
        assert statement != null;
        ResultSet result = statement.executeQuery();
        List<Airplane> airplanes = new ArrayList<>();
        while (result.next()) {
            int id = result.getInt("id");
            String flight_number = result.getString("flight_number");
            String origin = result.getString("origin");
            String destiny = result.getString("destiny");
            String flight_date = result.getString("flight_date");
            int available_seats = result.getInt("available_seats");
            float price = result.getFloat("price");
            Airplane newAirplane = new Airplane(flight_number, origin, destiny, flight_date, available_seats, price);
            newAirplane.setId(id);
            airplanes.add(newAirplane);
        }
        return airplanes;
    }

}
