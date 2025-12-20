package com.webpa.service.parser;

import com.webpa.domain.enums.Marketplace;
import com.webpa.dto.ParsedProduct;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class YandexMarketParser extends BaseJsoupParser implements MarketplaceParser {

    @Override
    public Marketplace marketplace() {
        return Marketplace.YANDEX_MARKET;
    }

    @Override
    public List<ParsedProduct> parse(ParseContext context) {
        String url = buildSearchUrl(context.query(), context.page());
        Document document = fetch(url);
        Elements cards = document.select("article[data-zone-name='snippet-card']");

        List<ParsedProduct> parsed = new ArrayList<>();
        for (Element card : cards) {
            String name = text(card, "h3 > a");
            String priceText = text(card, "span[data-auto='snippet-price-current']");
            BigDecimal price = parsePrice(priceText);
            String detailUrl = attr(card, "h3 > a", "href");
            String imageUrl = attr(card, "img", "src");
            Map<String, String> specs = collectSpecs(card);

            parsed.add(new ParsedProduct(
                    Marketplace.YANDEX_MARKET,
                    detailUrl != null && !detailUrl.startsWith("http") ? "https://market.yandex.ru" + detailUrl : detailUrl,
                    name,
                    price,
                    imageUrl,
                    null,
                    null,
                    null,
                    null,
                    null,
                    text(card, "div[data-zone-name='description']"),
                    Map.of("source", "yandex_market", "pageUrl", url),
                    specs,
                    Map.of(),
                    imageUrl == null ? List.of() : List.of(imageUrl),
                    null
            ));
        }
        return parsed;
    }

    private String buildSearchUrl(String query, int page) {
        String encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
        return "https://market.yandex.ru/search?text=" + encoded + "&page=" + page;
    }

    private Map<String, String> collectSpecs(Element card) {
        Map<String, String> specs = new LinkedHashMap<>();
        Elements items = select(card, "ul[data-auto='snippet-specs'] li");
        for (Element item : items) {
            String[] parts = item.text().split(":", 2);
            if (parts.length == 2) {
                specs.put(parts[0].trim(), parts[1].trim());
            }
        }
        return specs;
    }

    private BigDecimal parsePrice(String price) {
        if (price == null) {
            return null;
        }
        String cleaned = price.replaceAll("[^0-9,\\.]", "").replace(",", ".");
        if (cleaned.isEmpty()) {
            return null;
        }
        return new BigDecimal(cleaned);
    }
}
