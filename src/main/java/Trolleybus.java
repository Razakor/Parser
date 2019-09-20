import java.util.ArrayList;
import java.util.List;

class Trolleybus {
    private String name;
    private String shortName;
    private List<Hour> workDaysHours = new ArrayList<>();
    private List<Hour> weekendHours = new ArrayList<>();

    public Trolleybus(String name, String shortName) {
        this.name = name;
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public List<Hour> getWorkDaysHours() {
        return workDaysHours;
    }

    public void addWorkDaysHour(Hour hour) {
        workDaysHours.add(hour);
    }

    public List<Hour> getWeekendHours() {
        return workDaysHours;
    }

    public void addWeekendHour(Hour hour) {
        weekendHours.add(hour);
    }

    public void weekendHoursSetNull() {
        weekendHours = null;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
