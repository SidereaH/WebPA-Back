package com.webpa.service.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

public abstract class BaseJsoupParser {

    protected Document fetch(String url) {
        try {
            return Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (compatible; WebPA-Bot/1.0)")
                    .timeout((int) Duration.ofSeconds(15).toMillis())
                    .get();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Не удалось загрузить страницу: " + url, e);
        }
    }

    protected String text(Element element, String cssQuery) {
        return Optional.ofNullable(element.selectFirst(cssQuery))
                .map(Element::text)
                .orElse(null);
    }

    protected String attr(Element element, String cssQuery, String attribute) {
        return Optional.ofNullable(element.selectFirst(cssQuery))
                .map(el -> el.attr(attribute))
                .orElse(null);
    }

    protected Elements select(Element element, String cssQuery) {
        return element.select(cssQuery);
    }
}
