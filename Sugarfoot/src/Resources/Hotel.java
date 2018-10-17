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

    public Hotel(String location, String available_initial_date, String available_end_date) {
        this.location = location;
        this.available_initial_date = available_initial_date;
        this.available_end_date = available_end_date;
    }

    public Hotel(String name, int room_number, String location, int capacity, String available_initial_date, String available_end_date, float price) {
        this.name = name;
        this.room_number = room_number;
        this.location = location;
        this.capacity = capacity;
        this.available_initial_date = available_initial_date;
        this.available_end_date = available_end_date;
        this.price = price;
    }

    public Hotel() {}

    @Override
    public boolean equals(Resource resource) {
        return id == resource.getId();
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void insert() {
        String query = "INSERT INTO hotel (`name`, `room_number`, `location`, `capacity`, `available_initial_date`, `available_end_date`, `price`) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = DatabaseConnection.getStatement(query);
        try {
            statement.setString(1, this.name);
            statement.setInt(2, this.room_number);
            statement.setString(3, this.location);
            statement.setString(4, this.name);
            statement.setInt(5, this.capacity);
            statement.setString(6, this.available_initial_date);
            statement.setString(7, this.available_end_date);
            statement.setFloat(6, this.price);
            statement.execute();
            this.id = DatabaseConnection.getLastInsertedId();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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

    @Override
    public void update() {
        String query = "UPDATE hotel SET `name` = ? WHERE id = ?";
        PreparedStatement statement = DatabaseConnection.getStatement(query);
        try {
            statement.setString(1, this.name);
            statement.setInt(2, this.id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
            hotels.add(newHotel);
        }
        return hotels;
    }

    @Override
    public Reference getReference() {
        return Reference.HOTEL;
    }

    public String getDestiny(){
        return this.location;
    }
    public float getPrice() {
        return (float)0.0;
    }

    @Override
    public List<Resource> find() throws SQLException {
        return null;
    }

}
