package Resources;

import Server.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PackageResource implements Resource {

    public int id;
    public Hotel hotel;
    public Airplane airplane_going;
    public Airplane airplane_return;

    @Override
    public boolean equals(Resource resource) {
        return this.id == resource.getId();
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void insert() {
        String query = "INSERT INTO package (`hotel_id`, `airplane_going_id`, `airplane_return_id`) VALUES (?, ?, ?)";
        PreparedStatement statement = DatabaseConnection.getStatement(query);
        try {
            statement.setInt(1, this.hotel.id);
            statement.setInt(2, this.airplane_going.id);
            statement.setInt(3, this.airplane_return.id);
            statement.execute();
            this.id = DatabaseConnection.getLastInsertedId();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PackageResource() {
    }

    @Override
    public void delete() {
        String query = "DELETE FROM package WHERE id = ?";
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
        String query = "UPDATE package SET `hotel_id` = ?, `airplane_going_id` = ?, `airplane_return_id` = ? WHERE id = ?";
        PreparedStatement statement = DatabaseConnection.getStatement(query);
        try {
            statement.setInt(1, this.hotel.id);
            statement.setInt(2, this.airplane_going.id);
            statement.setInt(3, this.airplane_return.id);
            statement.setInt(4, this.id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Reference getReference() {
        return Reference.PACKAGE;
    }

    @Override
    public String getDestiny() {
        String query = "SELECT location FROM hotel WHERE id = ?";
        PreparedStatement statement = DatabaseConnection.getStatement(query);
        assert statement != null;
        try {
            statement.setInt(1, this.hotel.id);
        ResultSet result = statement.executeQuery();
            return result.getString("location");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public float getPrice() {
        String query = "SELECT h.price + a1.price + a2.price AS price\n" +
                "FROM package p\n" +
                "       JOIN hotel h ON p.hotel_id = h.id\n" +
                "       JOIN airplane a1 ON a1.id = p.airplane_going_id\n" +
                "       JOIN airplane a2 ON a2.id = p.airplane_return_id\n" +
                "WHERE p.id = ?;";
        PreparedStatement statement = DatabaseConnection.getStatement(query);
        assert statement != null;
        try {
            statement.setInt(1, this.id);
            ResultSet result = statement.executeQuery();
            return result.getFloat("price");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public List<Resource> find() throws SQLException {
        return null;
    }
}
