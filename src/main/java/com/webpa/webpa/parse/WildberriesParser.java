package com.webpa.webpa.parse;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.webpa.webpa.*;
import java.util.*;
import org.json.CDL;

@Component
public class WildberriesParser implements MarketplaceParser {

    @Override
    public List<ProductCard> parseProducts(String searchQuery) {
        List<ProductCard> productCards = new ArrayList<>();

        try {
            // Encode search query
            String word = URLEndoder(searchQuery);
            String url = String.format(
                    "https://search.wb.ru/exactmatch/ru/common/v7/search?ab_testing=false&appType=1&curr=rub&dest=-1257786&query=%s&resultset=catalog&sort=popular&spp=30&suppressSpellcheck=false",
                    word);

            // Make HTTP request
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            // Read response
            try (InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                    BufferedReader reader = new BufferedReader(isr)) {

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                // Parse JSON
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonObject jsonObject = gson.fromJson(response.toString(), JsonObject.class);
                JsonObject dataObject = jsonObject.getAsJsonObject("data");
                JsonArray productsArray = dataObject.getAsJsonArray("products");

                // Convert each product to ProductCard
                for (JsonElement productElement : productsArray) {
                    JsonObject product = productElement.getAsJsonObject();
                    ProductCard productCard = new ProductCard();

                    // Set basic fields
                    productCard.setName(getStringValue(product, "name"));
                    productCard.setPrice(getIntValue(product, "salePriceU") / 100); // Convert kopeks to rubles
                    productCard.setImage(getStringValue(product, "pics"));
                    productCard.setUrl(
                            "https://www.wildberries.ru/catalog/" + getStringValue(product, "id") + "/detail.aspx");
                    productCard.setMarketplace("Wildberries");

                    // Set main info
                    Map<String, Object> mainInfo = new HashMap<>();
                    mainInfo.put("brand", getStringValue(product, "brand"));
                    mainInfo.put("rating", getDoubleValue(product, "rating"));
                    mainInfo.put("feedbacks", getIntValue(product, "feedbacks"));
                    productCard.setMainInfo(mainInfo);

                    // Set main characteristics
                    Map<String, Object> mainCharacteristics = new HashMap<>();
                    if (product.has("characteristics")) {
                        JsonArray chars = product.getAsJsonArray("characteristics");
                        for (JsonElement charElement : chars) {
                            JsonObject char_ = charElement.getAsJsonObject();
                            mainCharacteristics.put(
                                    getStringValue(char_, "name"),
                                    getStringValue(char_, "value"));
                        }
                    }
                    productCard.setMainCharacteristics(mainCharacteristics);
                    
            

                    // Set additional information
                    Map<String, Object> additionalInfo = new HashMap<>();
                    additionalInfo.put("seller", getStringValue(product, "supplier"));
                    additionalInfo.put("supplierRating", getDoubleValue(product, "supplierRating"));
                    additionalInfo.put("available", getIntValue(product, "wh"));
                    productCard.setAdditionalInformation(additionalInfo);

                    // Add to list
                    productCards.add(productCard);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            // You might want to throw a custom exception here instead
        }

        return productCards;
    }

    // Helper methods to safely get values from JsonObject
    private String getStringValue(JsonObject obj, String key) {
        return obj.has(key) ? obj.get(key).getAsString() : "";
    }

    private int getIntValue(JsonObject obj, String key) {
        return obj.has(key) ? obj.get(key).getAsInt() : 0;
    }

    private double getDoubleValue(JsonObject obj, String key) {
        return obj.has(key) ? obj.get(key).getAsDouble() : 0.0;
    }


    public static String URLEndoder(String word) {// Слово для преобразования
        try {
            // Преобразование слова в байты с использованием UTF-8
            byte[] bytes = word.getBytes("UTF-8");
            StringBuilder binaryString = new StringBuilder();

            for (byte b : bytes) {
                // URL-кодирование каждого байта
                String binaryChar = String.format("%%%02X", b); // Преобразование в шестнадцатеричный вид
                binaryString.append(binaryChar);
            }

            System.out.println("Слово в URL-кодированном бинарном коде: " + binaryString.toString());
            return binaryString.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
