import java.util.ArrayList;
import java.util.List;

public class Hour {
    private String hour;
    private List<String> minutes = new ArrayList<>();

    public Hour(String hour) {
        this.hour = hour;
    }

    public String getHour() {
        return hour;
    }

    public List getMinutes() {
        return minutes;
    }

    public void addMinute(String minute) {
        minutes.add(minute);
    }
}
