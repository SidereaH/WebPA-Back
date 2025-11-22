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
public class OzonParser extends BaseJsoupParser implements MarketplaceParser {

    @Override
    public Marketplace marketplace() {
        return Marketplace.OZON;
    }

    @Override
    public List<ParsedProduct> parse(ParseContext context) {
        String url = buildSearchUrl(context.query(), context.page());
        Document document = fetch(url);
        Elements cards = document.select("div[data-widget='searchResultsV2'] div");

        List<ParsedProduct> parsed = new ArrayList<>();
        for (Element card : cards) {
            if (!card.hasAttr("data-widget")) {
                continue;
            }
            String name = text(card, "a span.tsBody500Medium");
            String priceText = text(card, "span.c3014-a1"); // ozon price span
            BigDecimal price = parsePrice(priceText);
            String detailUrl = attr(card, "a", "href");
            String imageUrl = attr(card, "img", "src");
            Map<String, String> rawAttributes = collectSpecs(card);

            parsed.add(new ParsedProduct(
                    Marketplace.OZON,
                    detailUrl != null ? "https://www.ozon.ru" + detailUrl : null,
                    name,
                    price,
                    imageUrl,
                    null,
                    null,
                    null,
                    null,
                    null,
                    text(card, "div.tsSecondary"),
                    Map.of("source", "ozon", "pageUrl", url),
                    rawAttributes,
                    Map.of(),
                    imageUrl == null ? List.of() : List.of(imageUrl),
                    null
            ));
        }
        return parsed;
    }

    private String buildSearchUrl(String query, int page) {
        String encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
        return "https://www.ozon.ru/search/?page=" + page + "&page_size=36&text=" + encoded;
    }

    private Map<String, String> collectSpecs(Element card) {
        Map<String, String> specs = new LinkedHashMap<>();
        Elements items = select(card, "div.a0c6-a3");
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
