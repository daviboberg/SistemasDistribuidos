package Resources;

import Server.DatabaseConnection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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


    public List<AirplaneVO> findAll(String origin, String destiny, Date flight_date) throws SQLException {
        String query = "SELECT * FROM airplane WHERE origin = ? AND destiny = ? AND flight_date = ?";
        PreparedStatement statement = DatabaseConnection.getStatement(query);
        statement.setString(1, origin);
        statement.setString(2, destiny);
        statement.setDate(3, flight_date);
        ResultSet result = statement.executeQuery();
        List<AirplaneVO> airplanes = new ArrayList<>();
        while (result.next()) {
            int id = result.getInt("id");
            String flight_number = result.getString("flight_number");
            origin = result.getString("origin");
            destiny = result.getString("destiny");
            flight_date = result.getDate("flight_date");
            AirplaneVO newAirplane = new AirplaneVO(id, flight_number, origin, destiny, flight_date);
            airplanes.add(newAirplane);
        }
        return airplanes;
    }

    @Override
    public Reference getReference() {
        return Reference.AIRPLANE;
    }

    public String getDestiny(){
        return "";
    }

    public float getPrice() {
        return (float)0.0;
    }

}
