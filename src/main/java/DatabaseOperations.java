import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DatabaseOperations {
    static void insertTrolleybuses() throws SQLException {
        DatabaseConnection.statement.executeUpdate("DELETE FROM trolleybuses");
        Loader.getTrolleybuses().forEach(trolleybus -> {
            String sql = "INSERT INTO trolleybuses (number, name)\n" +
                    "VALUES ('" + trolleybus.getNumber() + "', '" + trolleybus.getName() + "')";

            try {

                DatabaseConnection.statement.executeUpdate(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    static void insertStops() throws SQLException {
        DatabaseConnection.statement.executeUpdate("DELETE FROM stops");

        Set<String> nameSet = new HashSet<>();

        for (Trolleybus trolleybus : Loader.getTrolleybuses()) {
            for (Stop stop : trolleybus.getStops()) {
                nameSet.add(stop.getName());
            }
        }

        nameSet.forEach(name -> {
            String sql = "INSERT INTO stops (name)\n" +
                    "VALUES ('" + name + "')";
            try {

                DatabaseConnection.statement.executeUpdate(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    static void setTrolleybusesStops() throws SQLException {
        DatabaseConnection.statement.executeUpdate("DELETE FROM trolleybuses_stops");
        Loader.getTrolleybuses().forEach(trolleybus -> {
            trolleybus.getStops().forEach(stop -> {
                String sql = "INSERT INTO trolleybuses_stops (trolleybus, stop)\n" +
                        "VALUES ('" + trolleybus.getNumber() + "', '" + stop.getName() + "')";
                try {
                    DatabaseConnection.statement.executeUpdate(sql);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        });
    }
}