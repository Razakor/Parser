import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        DatabaseConnection.connect();
        Loader.load();
        DatabaseOperations.insert();
        DatabaseConnection.connection.close();
    }
}
