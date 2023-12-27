package org.example.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class KudaGoParser {
    Document document;
    {
        try {
            document = Jsoup.connect("https://kudago.com/msk/").get();

            System.out.println(document.title());
            document.select("body > div.page-container-wrapper.page-mix.city-mix > " +
                            "div.centered-container.centered-container-page > section > div > " +
                            "div.page-header > div > div > div")
                    .forEach(element -> System.out.println(element.text()));
        } catch (IOException e) {
            System.err.println("Такого сайта нет");
        }
    }
}
