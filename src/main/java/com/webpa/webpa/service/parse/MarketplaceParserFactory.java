package com.webpa.webpa.service.parse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MarketplaceParserFactory {

    private final OzonParser ozonParser;
    private final WildberriesParser wildberriesParser;
    private final YandexMarketParser yandexMarketParser;

    @Autowired
    public MarketplaceParserFactory(OzonParser ozonParser, WildberriesParser wildberriesParser, YandexMarketParser yandexMarketParser) {
        this.ozonParser = ozonParser;
        this.wildberriesParser = wildberriesParser;
        this.yandexMarketParser = yandexMarketParser;
    }

    public MarketplaceParser getParser(String marketplace) {
        switch (marketplace.toLowerCase()) {
            case "ozon":
                return ozonParser;
            case "wildberries":
                return wildberriesParser;
            case "yandexmarket":
                return yandexMarketParser;
            default:
                throw new IllegalArgumentException("Unknown marketplace: " + marketplace);
        }
    }

    // Метод для возврата всех парсеров
    public List<MarketplaceParser> getAllParsers() {
        return Arrays.asList(ozonParser, wildberriesParser, yandexMarketParser);
    }
}
