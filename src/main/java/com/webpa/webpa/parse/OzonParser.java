package com.webpa.webpa.parse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.webpa.webpa.ProductCard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;




@Component
public class OzonParser implements MarketplaceParser {

    @Override
    public List<ProductCard> parseProducts(String productName) {
        List<ProductCard> products = new ArrayList<>();
        String url = "https://www.ozon.ru/search/?text=" + productName;

        try {
            Document document = Jsoup.connect(url).get();
            Elements productElements = document.select(".js0_23 ");

            for (Element element : productElements) {
                String title = element.select(".tile-title").text();
                String price = element.select(".price").text();
                String productUrl = element.select("a.tile-link").attr("href");

                ProductCard product = new ProductCard();
                product.setName(title);
                //product.setPrice(getDoubleValue(price));
                product.setMarketplace("OZON");
                product.setUrl("https://www.ozon.ru" + productUrl);

                products.add(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
    }

    

}
