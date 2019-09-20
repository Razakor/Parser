import java.util.ArrayList;
import java.util.List;

class Trolleybus {
    private String name;
    private String number;
    private List<Stop> stops = new ArrayList<>();

    public Trolleybus(String name, String shortName) {
        this.name = name;
        this.number = shortName;
    }

    public String getName() {
        return name;
    }

    public String getnumber() {
        return number;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public void addStop(Stop stop) {
        stops.add(stop);
    }
}
