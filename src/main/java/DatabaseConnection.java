import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

class DatabaseConnection {
    static Connection connection;
    static Statement statement;

    static void connect() throws SQLException {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/taskdb?" +
                            "user=Razakor&password=admin&serverTimezone=UTC");
            statement = connection.createStatement();

    }
}