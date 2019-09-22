import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

class DatabaseOperations {

    static void insert() throws SQLException {
        insertTrolleybuses();
        insertStops();
        setTrolleybusesStops();
    }

    private static void insertTrolleybuses() throws SQLException {
        DatabaseConnection.statement.executeUpdate("DELETE FROM trolleybuses_stops");
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

    private static void insertStops() throws SQLException {
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

    private static void setTrolleybusesStops()  {
        Loader.getTrolleybuses().forEach(trolleybus -> trolleybus.getStops().forEach(stop -> {
            String sql = "INSERT INTO trolleybuses_stops (trolleybus, stop)\n" +
                    "VALUES ('" + trolleybus.getNumber() + "', '" + stop.getName() + "')";
            try {
                DatabaseConnection.statement.executeUpdate(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }));
    }
}