import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        DatabaseConnection.connect();
        Loader loader = new Loader();
        loader.load();
        DatabaseOperations.insertTrolleybuses();
        DatabaseOperations.insertStops();
        DatabaseOperations.setTrolleybusesStops();
        DatabaseConnection.connection.close();
    }
}
