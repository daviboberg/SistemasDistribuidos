package Server;

import java.sql.*;

public class DatabaseConnection {

    private static Connection connection;

    public static Connection getOrCreate() throws SQLException {
        if (DatabaseConnection.connection != null)
            return DatabaseConnection.connection;

        DatabaseConnection.connection = DriverManager.getConnection("jdbc:sqlite:database");
        return DatabaseConnection.connection;
    }

    public static PreparedStatement getStatement(String sql) {
        try {
            Connection connection = DatabaseConnection.getOrCreate();
            return connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getLastInsertedId() {
        PreparedStatement statement = DatabaseConnection.getStatement("SELECT last_insert_rowid() as id");
        try {
            ResultSet result = statement.executeQuery();
            return result.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
