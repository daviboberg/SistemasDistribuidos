package Resources;

import Server.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class Hotel implements Resource {
    public int id;
    public String name;
    public int room_number;
    public String location;
    public int capacity;
    public String available_initial_date;
    public String available_end_date;
    public float price;

    /**
     * @param name
     * @param room_number
     * @param location
     * @param capacity
     * @param available_initial_date
     * @param available_end_date
     * @param price
     */
    private Hotel(String name, int room_number, String location, int capacity, String available_initial_date, String available_end_date, float price) {
        this.name = name;
        this.room_number = room_number;
        this.location = location;
        this.capacity = capacity;
        this.available_initial_date = available_initial_date;
        this.available_end_date = available_end_date;
        this.price = price;
    }

    /**
     *
     */
    public Hotel() {}

    /**
     * @param initial_date
     * @param end_date
     * @param capacity
     * @param price
     */
    private Hotel(String initial_date, String end_date, int capacity, float price) {
        this.available_initial_date = initial_date;
        this.available_end_date = end_date;
        this.capacity = capacity;
        this.price = price;
    }

    /**
     * @param location
     * @param initial_date
     * @param end_date
     * @param number_clients
     * @param number_rooms
     */
    public Hotel(String location, String initial_date, String end_date, int number_clients, int number_rooms) {
        this.location = location;
        this.available_initial_date = initial_date;
        this.available_end_date = end_date;
        this.room_number = number_rooms;
        this.capacity = number_clients;
    }

    /**
     * @param resource
     * @return
     */
    @Override
    public boolean equals(Resource resource) {
        return id == resource.getId();
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
        String query = "INSERT INTO hotel (`name`, `room_number`, `location`, `capacity`, `available_initial_date`, `available_end_date`, `price`) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = DatabaseConnection.getStatement(query);
        try {
            statement.setString(1, this.name);
            statement.setInt(2, this.room_number);
            statement.setString(3, this.location);
            statement.setInt(4, this.capacity);
            statement.setString(5, this.available_initial_date);
            statement.setString(6, this.available_end_date);
            statement.setFloat(7, this.price);
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
        String query = "DELETE FROM hotel WHERE id = ?";
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
        String query = "UPDATE hotel SET `name` = ?, `room_number` = ?, `location` = ?, `capacity` = ?, `available_initial_date` = ?, `available_end_date` = ?, `price` = ?, WHERE id = ?";
        PreparedStatement statement = DatabaseConnection.getStatement(query);
        try {
            statement.setString(1, this.name);
            statement.setInt(2, this.room_number);
            statement.setString(3, this.location);
            statement.setInt(4, this.capacity);
            statement.setString(5, this.available_initial_date);
            statement.setString(6, this.available_end_date);
            statement.setFloat(7, this.price);
            statement.setInt(8, this.id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return
     * @throws SQLException
     */
    public static List<Hotel> getAll() throws SQLException {
        String query = "SELECT * FROM hotel";
        PreparedStatement statement = DatabaseConnection.getStatement(query);
        assert statement != null;
        ResultSet result = statement.executeQuery();
        List<Hotel> hotels = new ArrayList<>();
        while (result.next()) {
            int id = result.getInt("id");
            String name = result.getString("name");
            int room_number = result.getInt("room_number");
            String location = result.getString("location");
            int capacity = result.getInt("capacity");
            String available_initial_date = result.getString("available_initial_date");
            String available_end_date = result.getString("available_end_date");
            float price = result.getFloat("price");
            Hotel newHotel = new Hotel(name, room_number, location, capacity, available_initial_date, available_end_date, price);
            newHotel.setId(id);
            hotels.add(newHotel);
        }
        return hotels;
    }

    /**
     * @return
     */
    @Override
    public Reference getReference() {
        return Reference.HOTEL;
    }

    /**
     * @return
     */
    public String getDestiny(){
        return this.location;
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
        String query = "SELECT * FROM hotel WHERE location = ? AND available_initial_date <= ? AND available_end_date >= ? AND capacity >= ? AND room_number >= ? AND price <= ?";
        PreparedStatement statement = DatabaseConnection.getStatement(query);
        assert statement != null;
        statement.setString(1, location);
        statement.setString(2, available_initial_date);
        statement.setString(3, available_end_date);
        statement.setInt(4, capacity);
        statement.setInt(5, room_number);
        statement.setFloat(6, price);

        ResultSet result = statement.executeQuery();
        List<Resource> hotels = new ArrayList<>();
        while (result.next()) {
            int id = result.getInt("id");
            String room_number = result.getString("room_number");
            String initial_date = result.getString("available_initial_date");
            String end_date = result.getString("available_end_date");
            int capacity = result.getInt("capacity");
            float price = result.getFloat("price");
            price = price * this.room_number;
            Hotel hotel = new Hotel(initial_date, end_date, capacity, price);
            hotel.id = id;
            hotels.add(hotel);
        }

        return hotels;
    }

    /**
     *
     */
    @Override
    public void buy() {
        String query = "UPDATE hotel SET `room_number` = (SELECT room_number - ? FROM hotel WHERE id = ?) WHERE id = ?";
        PreparedStatement statement = DatabaseConnection.getStatement(query);
        try {
            statement.setInt(1, this.room_number);
            statement.setInt(2, this.id);
            statement.setInt(3, this.id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
