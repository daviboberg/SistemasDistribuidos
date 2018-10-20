package Resources;

import Server.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PackageResource implements Resource {

    public int id;
    public Hotel hotel;
    public Airplane airplane_going;
    public Airplane airplane_return;
    public float price;

    /**
     * @param hotel
     * @param airplane_going
     * @param airplane_return
     * @param price
     */
    public PackageResource(Hotel hotel, Airplane airplane_going, Airplane airplane_return, float price) {
        this.hotel = hotel;
        this.airplane_going = airplane_going;
        this.airplane_return = airplane_return;
        this.price = price;
    }

    /**
     * @param resource
     * @return boolean
     */
    @Override
    public boolean equals(Resource resource) {
        return this.id == resource.getId();
    }

    /**
     * @return int
     */
    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     */
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

    /**
     *
     */
    public PackageResource() {
    }

    /**
     *
     */
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

    /**
     *
     */
    @Override
    public void update() {
        String query = "UPDATE package\n" +
                "SET `hotel_id`           = ?,\n" +
                "\t\t`airplane_going_id`  = ?,\n" +
                "\t\t`airplane_return_id` = ?,\n" +
                "\t\tprice                = (SELECT h.price + a_going.price + a_return.price\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\tFROM package p\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t JOIN hotel h ON h.id = p.hotel_id\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t JOIN airplane a_going ON a_going.id = p.airplane_going_id\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t JOIN airplane a_return ON a_return.id = p.airplane_return_id\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t WHERE p.id = ?)\n" +
                "WHERE id = ?";
        PreparedStatement statement = DatabaseConnection.getStatement(query);
        try {
            statement.setInt(1, this.hotel.id);
            statement.setInt(2, this.airplane_going.id);
            statement.setInt(3, this.airplane_return.id);
            statement.setInt(4, this.id);
            statement.setInt(5, this.id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return Reference
     */
    @Override
    public Reference getReference() {
        return Reference.PACKAGE;
    }

    /**
     * @return String
     */
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

    /**
     * @return float
     */
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

    /**
     * @return List<Resource>
     * @throws SQLException
     */
    @Override
    public List<Resource> find() throws SQLException {
        String query = "SELECT *\n" +
                "FROM package p\n" +
                "\t\t JOIN hotel h ON h.id = p.hotel_id\n" +
                "\t\t JOIN airplane a_going ON a_going.id = p.airplane_going_id\n" +
                "\t\t JOIN airplane a_return ON a_return.id = p.airplane_return_id\n" +
                "WHERE h.location = ?\n" +
                "\tAND h.available_initial_date <= ?\n" +
                "\tAND h.available_end_date >= ?\n" +
                "\tAND h.capacity >= ?\n" +
                "\tAND h.room_number >= ?\n" +
                "\tAND a_going.flight_date = ?\n" +
                "\tAND a_going.available_seats >= ?\n" +
                "\tAND a_going.origin = ?\n" +
                "\tAND a_going.destiny = ?\n" +
                "\tAND a_return.flight_date = ?\n" +
                "\tAND a_return.available_seats >= ?\n" +
                "\tAND a_return.origin = ?\n" +
                "\tAND a_return.destiny = ?\n" +
                "\tAND p.price <= ?;";

        PreparedStatement statement = DatabaseConnection.getStatement(query);
        assert statement != null;

        statement.setString(1, hotel.location);
        statement.setString(2, hotel.available_initial_date);
        statement.setString(3, hotel.available_end_date);
        statement.setInt(4, hotel.capacity);
        statement.setInt(5, hotel.room_number);
        statement.setString(6, airplane_going.flight_date);
        statement.setInt(7, airplane_going.available_seats);
        statement.setString(8, airplane_going.origin);
        statement.setString(9, airplane_going.destiny);
        statement.setString(10, airplane_return.flight_date);
        statement.setInt(11, airplane_return.available_seats);
        statement.setString(12, airplane_return.origin);
        statement.setString(13, airplane_return.destiny);
        statement.setFloat(14, price);
        ResultSet result = statement.executeQuery();

        List<Resource> packages = new ArrayList<>();
        while (result.next()) {
            int id = result.getInt("id");
            int hotel_id = result.getInt("hotel_id");
            int airplane_going_id = result.getInt("airplane_going_id");
            int airplane_return_id = result.getInt("airplane_return_id");
            float price = result.getFloat("price");
            price = price * this.hotel.room_number;

            Hotel hotel = new Hotel(this.hotel.location, this.hotel.available_initial_date, this.hotel.available_end_date, this.hotel.capacity, this.hotel.room_number);
            hotel.setId(hotel_id);
            Airplane airplane_going = new Airplane(this.airplane_going.origin, this.airplane_going.destiny, this.airplane_going.flight_date, this.airplane_going.available_seats);
            airplane_going.setId(airplane_going_id);
            Airplane airplane_return = new Airplane(this.airplane_return.origin, this.airplane_return.destiny, this.airplane_return.flight_date, this.airplane_return.available_seats);
            airplane_return.setId(airplane_return_id);
            PackageResource package_resource = new PackageResource(hotel, airplane_going, airplane_return, price);
            package_resource.setId(id);
            packages.add(package_resource);
        }

        return packages;
    }

    @Override
    public void buy() {
        this.hotel.buy();
        this.airplane_going.buy();
        this.airplane_return.buy();
    }
}
