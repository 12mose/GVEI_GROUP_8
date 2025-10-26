// DBConnection.java
import java.sql.*;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/gvei_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = ""; // Your MySQL password

    public static Connection getConnection() throws SQLException {
        try {
            // For MySQL Connector/J 8.0+
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}