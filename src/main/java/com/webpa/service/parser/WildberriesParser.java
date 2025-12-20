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
public class WildberriesParser extends BaseJsoupParser implements MarketplaceParser {

    @Override
    public Marketplace marketplace() {
        return Marketplace.WILDBERRIES;
    }

    @Override
    public List<ParsedProduct> parse(ParseContext context) {
        String url = buildSearchUrl(context.query(), context.page(), context.pageSize());
        Document document = fetch(url);

        Elements cards = document.select("article.product-card");
        List<ParsedProduct> parsed = new ArrayList<>();
        for (Element card : cards) {
            String name = text(card, "span.product-card__name");
            String priceText = text(card, "ins.price__lower-price");
            BigDecimal price = parsePrice(priceText);
            String imageUrl = attr(card, "img.product-card__img", "src");
            String detailUrl = attr(card, "a.product-card__main", "href");
            Map<String, String> rawAttributes = collectSpecs(card);

            parsed.add(new ParsedProduct(
                    Marketplace.WILDBERRIES,
                    detailUrl,
                    name,
                    price,
                    imageUrl,
                    null,
                    null,
                    null,
                    null,
                    null,
                    text(card, "p.product-card__desc"),
                    Map.of("source", "wildberries", "pageUrl", url),
                    rawAttributes,
                    Map.of(),
                    List.of(imageUrl),
                    null
            ));
        }
        return parsed;
    }

    private String buildSearchUrl(String query, int page, int pageSize) {
        String encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
        return "https://www.wildberries.ru/catalog/0/search.aspx?search=" + encoded + "&page=" + page + "&sort=popular&pageSize=" + pageSize;
    }

    private Map<String, String> collectSpecs(Element card) {
        Map<String, String> specs = new LinkedHashMap<>();
        Elements items = select(card, "div.product-card__properties span");
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
