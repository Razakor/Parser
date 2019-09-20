import java.util.ArrayList;
import java.util.List;

public class Stop {
    private String name;
    private String url;
    private List<Hour> workDaysHours = new ArrayList<>();
    private List<Hour> weekendHours = new ArrayList<>();

    public Stop(String name, String url) {
        this.name = name;
        this.url = url;
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

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
