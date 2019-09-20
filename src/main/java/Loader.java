import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.*;

class Loader {
    private List<Trolleybus> trolleybuses = new ArrayList<>();
    private Map<String, String> urls = new HashMap<>();
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
                Document document = Jsoup.connect(url).get();
                Trolleybus trolleybus = new Trolleybus(document.title(), trolleybusNumber);
                trolleybuses.add(trolleybus);
                loadStops(trolleybus, url, document);
                loadWorkDaysHours(trolleybus.getStops());
                loadWeekendHours(trolleybus.getStops());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadStops(Trolleybus trolleybus, String url, Document document) {
        Element chosenRote = document.select("div.chosen-route.active").get(0);
        Element ul = chosenRote.select("ul").get(0);
        Elements li = ul.select("li");
        for (Element l : li) {
            String s = l.attr("onClick");
            s = s.substring(61, s.length() - 1);
            s = s.substring(s.indexOf(' ') + 1);
            String stopUrl = url + '/' + s;
            String name = l.select("p").text();
            trolleybus.addStop(new Stop(name, stopUrl));
        }
    }

    private void loadWorkDaysHours(List<Stop> stops) {
        stops.parallelStream().forEach(stop -> {
            try {
                Document stopDocument = Jsoup.connect(stop.getUrl()).get();
                Element table = stopDocument.select("table").get(0);
                Elements rows = table.select("tr");

                for (Element row : rows) {
                    Elements cols = row.select("td");
                    if (cols.isEmpty()) continue;
                    if (!cols.get(0).text().equals("")) {
                        hour = new Hour(cols.get(0).text());
                        stop.addWorkDaysHour(hour);
                    }

                    for (int i = 1; i < cols.size(); i++) {
                        if (!cols.get(i).text().equals("")) {
                            hour.addMinute(cols.get(i).text());
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadWeekendHours(List<Stop> stops) {
        stops.parallelStream().forEach(stop -> {
            try {
                Document stopDocument = Jsoup.connect(stop.getUrl()).get();
                Element table = stopDocument.select("table").get(0);
                Elements rows = table.select("tr");

                for (Element row : rows) {
                    Elements cols = row.select("td");
                    if (cols.isEmpty()) continue;
                    if (cols.get(0).text().equals("Маршрут не працює в ці дні")) {
                        stop.weekendHoursSetNull();
                        return;
                    }
                    if (!cols.get(0).text().equals("")) {
                        hour = new Hour(cols.get(0).text());
                        stop.addWeekendHour(hour);
                    }

                    for (int i = 1; i < cols.size(); i++) {
                        if (!cols.get(i).text().equals("")) {
                            hour.addMinute(cols.get(i).text());
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public List<Trolleybus> getTrolleybuses() {
        return trolleybuses;
    }

}
