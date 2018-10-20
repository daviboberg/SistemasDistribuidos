package Server;

import java.sql.*;

public class DatabaseConnection {

    private static Connection connection;

    /**
     * @return database connection
     * @throws SQLException
     */
    private static Connection getOrCreate() throws SQLException {
        if (DatabaseConnection.connection != null)
            return DatabaseConnection.connection;

        DatabaseConnection.connection = DriverManager.getConnection("jdbc:sqlite:database");
        return DatabaseConnection.connection;
    }

    /**
     * @param sql
     * @return preaparedStatement for sql
     */
    public static PreparedStatement getStatement(String sql) {
        try {
            Connection connection = DatabaseConnection.getOrCreate();
            return connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return last inserted id or -1 in case of failure
     */
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
