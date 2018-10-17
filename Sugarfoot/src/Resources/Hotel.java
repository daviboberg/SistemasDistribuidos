package Resources;

import Server.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Hotel implements Resource{
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
}
