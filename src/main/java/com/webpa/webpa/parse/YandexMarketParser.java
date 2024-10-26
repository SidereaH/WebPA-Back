package com.webpa.webpa.parse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.webpa.webpa.*;
@Component
public class YandexMarketParser implements MarketplaceParser {

    @Override
    public List<ProductCard> parseProducts(String productName) {
        List<ProductCard> products = new ArrayList<>();
        String url = "https://market.yandex.ru/search?text=" + productName;

        try {
            Document document = Jsoup.connect(url).get();
            Elements productElements = document.select(".n-snippet-card2");

            for (Element element : productElements) {
                String title = element.select(".n-snippet-card2__title").text();
                String price = element.select(".n-snippet-card2__price .price").text();
                String productUrl = element.select("a.n-snippet-card2__title").attr("href");

                ProductCard product = new ProductCard();
                product.setName(title);
                product.setPrice(Integer.parseInt(price));
                product.setMarketplace("YandexMarket");
                product.setUrl("https://market.yandex.ru" + productUrl);

                products.add(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
    }
}
