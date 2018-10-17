package Resources;

import Server.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Hotel implements Resource {
    public int id;
    public String name;

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
        String query = "INSERT INTO hotel (`name`) VALUES (?)";
        PreparedStatement statement = DatabaseConnection.getStatement(query);
        try {
            statement.setString(1, this.name);
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

    @Override
    public Reference getReference() {
        return Reference.HOTEL;
    }

    public String getDestiny(){
        return "";
    }
    public float getPrice() {
        return (float)0.0;
    }

}
