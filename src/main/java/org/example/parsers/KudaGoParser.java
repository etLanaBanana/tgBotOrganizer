package org.example.parsers;

import org.example.model.entity.Event;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KudaGoParser {

    public static List<Event> parseEvents(String city, Integer numPages) {
        List<Event> events = new ArrayList<>();

        try {
            for (int page = 1; page <= numPages; page++) {
                String url = String.format("https://kudago.com/%s/?page=%d", city, page);
                Document doc = Jsoup.connect(url).get();
                Elements eventElements = doc.select(".post-card");

                for (Element eventElement : eventElements) {
                    long id = Long.parseLong(eventElement.attr("data-id"));
                    String title = eventElement.select(".post-card__title").text();
                    String description = eventElement.select(".post-card__descr").text();
                    String metroStation = eventElement.select(".metro-station").text();
                    String uri = eventElement.select(".post-card__link").attr("href");
                    String price = eventElement.select(".post-card__price").text();

                    Event event = new Event(id, title, description, metroStation, city, uri, price);
                    events.add(event);
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при подключении к сайту");
        }

        return events;
    }
}