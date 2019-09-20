import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.*;

class Loader {
    private Map<String, String> urls = new HashMap<>();
    private Document document;
    private List<Trolleybus> trolleybuses = new ArrayList<>();
    private Hour hour;

    {
        urls.put("1", "https://vn.rozklad.in.ua/home/schedule/48");
        urls.put("2", "https://vn.rozklad.in.ua/home/schedule/50");
        urls.put("3", "https://vn.rozklad.in.ua/home/schedule/74");
        urls.put("4", "https://vn.rozklad.in.ua/home/schedule/52");
        urls.put("5", "https://vn.rozklad.in.ua/home/schedule/69");
        urls.put("6", "https://vn.rozklad.in.ua/home/schedule/71");
        urls.put("6A", "https://vn.rozklad.in.ua/home/schedule/111");
        urls.put("7", "https://vn.rozklad.in.ua/home/schedule/65");
        urls.put("9", "https://vn.rozklad.in.ua/home/schedule/55");
        urls.put("10", "https://vn.rozklad.in.ua/home/schedule/64");
        urls.put("11", "https://vn.rozklad.in.ua/home/schedule/59");
        urls.put("12", "https://vn.rozklad.in.ua/home/schedule/57");
        urls.put("13", "https://vn.rozklad.in.ua/home/schedule/53");
    }

    void load() {
        urls.forEach((trolleybusNumber, url) -> {
            try {
                document = Jsoup.connect(url).get();
                Trolleybus trolleybus = new Trolleybus(document.title(), trolleybusNumber);
                trolleybuses.add(trolleybus);
                System.out.println(document.title());
                loadWorkDaysHours(trolleybus);
                loadWeekendHours(trolleybus);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadWorkDaysHours(Trolleybus trolleybus) {
        Element table = document.select("table").get(0);
        Elements rows = table.select("tr");

        System.out.println("Будні дні");

        for (Element row : rows) {
            Elements cols = row.select("td");
            if (cols.isEmpty()) continue;
            if (!cols.get(0).text().equals("")) {
                hour = new Hour(cols.get(0).text());
                trolleybus.addWorkDaysHour(hour);
            }

            for (int i = 1; i < cols.size(); i++) {
                if (!cols.get(i).text().equals("")) {
                    hour.addMinute(cols.get(i).text());
                    System.out.print(cols.get(i).text() + " ");
                }
            }
            System.out.println();
        }
    }

    private void loadWeekendHours(Trolleybus trolleybus) {
        Element table = document.select("table").get(1);
        Elements rows = table.select("tr");

        System.out.println("Вихідні дні");

        for (Element row : rows) {
            Elements cols = row.select("td");
            if (cols.isEmpty()) continue;
            if (cols.get(0).text().equals("Маршрут не працює в ці дні")) {
                trolleybus.weekendHoursSetNull();
                return;
            }
            if (!cols.get(0).text().equals("")) {
                hour = new Hour(cols.get(0).text());
                trolleybus.addWeekendHour(hour);
            }

            for (int i = 1; i < cols.size(); i++) {
                if (!cols.get(i).text().equals("")) {
                    hour.addMinute(cols.get(i).text());
                    System.out.print(cols.get(i).text() + " ");
                }
            }
            System.out.println();
        }
    }

    public List<Trolleybus> getTrolleybuses() {
        return trolleybuses;
    }
}
