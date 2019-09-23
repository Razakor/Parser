import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

class DatabaseOperations {

    static void insert() throws SQLException {
        insertTrolleybuses();
        insertStops();
        setTrolleybusesStops();
        insertHours();
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

    private static void insertHours() throws SQLException {
        DatabaseConnection.statement.executeUpdate("DELETE FROM hours");
        DatabaseConnection.statement.executeUpdate("DELETE FROM minutes");
        AtomicInteger id = new AtomicInteger();
        Loader.getTrolleybuses().forEach(trolleybus -> trolleybus.getStops().forEach(stop -> {
            stop.getWorkDaysHours().forEach(hour -> {
                id.getAndIncrement();
                String sql = "INSERT INTO hours (id, trolleybus_number, stop_name, val, is_work_day)\n" +
                        "VALUES ('" + id.get() + "', '" + trolleybus.getNumber() + "', '" + stop.getName() + "', '" + hour.getHour() + "', '1')";
                try {
                    DatabaseConnection.statement.executeUpdate(sql);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                insertMinutes(hour, id);
            });
            stop.getWeekendHours().forEach(hour -> {
                id.getAndIncrement();
                String sql = "INSERT INTO hours (id, trolleybus_number, stop_name, val, is_work_day)\n" +
                        "VALUES ('" + id.get() + "', '" + trolleybus.getNumber() + "', '" + stop.getName() + "', '" + hour.getHour() + "', '0')";
                try {
                    DatabaseConnection.statement.executeUpdate(sql);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                insertMinutes(hour, id);
            });
        }));
    }

    private static void insertMinutes(Hour hour, AtomicInteger id) {
        hour.getMinutes().forEach( minute -> {
            String sql = "INSERT INTO minutes (hour_id, val)\n" +
                    "VALUES ('" + id.get() + "', '" + minute + "')";
            try {
                DatabaseConnection.statement.executeUpdate(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}