import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    public static Connection connectToDatabase() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/imaschema?allowPublicKeyRetrieval=true&useSSL=false";
        String username = "root";
        String password = "password";
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }
}
