package Resources;

import Server.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Airplane implements Resource {
    public int id;
    public String flight_number;

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
        String query = "INSERT INTO airplane (`flight_number`) VALUES (?)";
        PreparedStatement statement = DatabaseConnection.getStatement(query);
        try {
            statement.setString(1, this.flight_number);
            statement.execute();
            this.id = DatabaseConnection.getLastInsertedId();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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

    @Override
    public void update() {
        String query = "UPDATE airplane SET `flight_number` = ? WHERE id = ?";
        PreparedStatement statement = DatabaseConnection.getStatement(query);
        try {
            statement.setString(1, this.flight_number);
            statement.setInt(2, this.id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
