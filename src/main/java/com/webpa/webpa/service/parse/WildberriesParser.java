package com.webpa.webpa.service.parse;

import com.webpa.webpa.models.ProductCard;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.*;

@Component
public class WildberriesParser implements MarketplaceParser {
    private static final String WB_COOKIES =
            "_cp=1; " +
                    "_wbauid=9868545131761658936; " +
                    "routeb=1762263394.809.74.968545|d4ae5f6f13c2fcce539dd766ca4b41fc; " +
                    "wbx-validation-key=027d2fbf-5634-4552-89fe-956501403d00; " +
                    "x_wbaas_token=1.1000.ed80ee820a4348b18a657304f3460b83.MHwxODUuMjIzLjE2OC4yMDJ8TW96aWxsYS81LjAgKFgxMTsgTGludXggeDg2XzY0OyBydjoxMzkuMCkgR2Vja28vMjAxMDAxMDEgRmlyZWZveC8xMzkuMHwxNzY2NzUzNjE3fHJldXNhYmxlfDJ8ZXlKb1lYTm9Jam9pSW4wPXwwfDN8MTc2NjE0ODgxN3wx.MEYCIQCAIUdeyy509pGfGIRGy9XLuKw1WAb1OkNbcA96A7dguQIhAOv+fbvYCTG+qu1leaR/MRxov/4Lvus7y1BRXu4Beq5o";

    @Override
    public List<ProductCard> parseProducts(String searchQuery) {
        List<ProductCard> productCards = new ArrayList<>();

        try {
            // Encode search query
            String word = URLEndoder(searchQuery);
            String url = String.format(
                    "https://www.wildberries.ru/__internal/search/exactmatch/ru/common/v18/search?ab_testing=false&ab_testing=false&appType=1&curr=rub&dest=-2228370&hide_dtype=9;11&hide_vflags=4294967296&inheritFilters=false&lang=ru&page=1&query=%s&resultset=catalog&sort=popular&spp=30&suppressSpellcheck=false&uclusters=2",
                   // "https://search.wb.ru/exactmatch/ru/common/v7/search?ab_testing=false&appType=1&curr=rub&dest=-1257786&query=%s&resultset=catalog&sort=popular&spp=30&suppressSpellcheck=false",
                    word);

            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (X11; Linux x86_64; rv:139.0) Gecko/20100101 Firefox/139.0");
            connection.setRequestProperty("Cookie", WB_COOKIES);


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
//                JsonObject dataObject = jsonObject.getAsJsonObject("data");
                JsonArray productsArray = jsonObject.getAsJsonArray("products");

                // Convert each product to ProductCard
                // while (numOfCounts >= 0) {
                // sizes/price/basic
                for (JsonElement productElement : productsArray) {
                    JsonObject product = productElement.getAsJsonObject();
                    ProductCard productCard = new ProductCard();
                    
                    // double price = Double.parseDouble(product.get("salePiceU").getAsString());
                    double basicPrice = 0;

                    try {
                        if (product.has("sizes") && product.get("sizes").isJsonArray()) {
                            JsonArray sizes = product.getAsJsonArray("sizes");

                            if (!sizes.isEmpty()) {
                                JsonObject size0 = sizes.get(0).getAsJsonObject();

                                if (size0.has("price")) {
                                    JsonObject price = size0.getAsJsonObject("price");

                                    // Цена продажи
                                    if (price.has("product")) {
                                        basicPrice = price.get("product").getAsDouble();
                                    }
                                    // fallback — базовая
                                    else if (price.has("basic")) {
                                        basicPrice = price.get("basic").getAsDouble();
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Ошибка при получении цены: " + e.getMessage());
                    }
                    
                    // Set basic fields
                    productCard.setMarketplaceId(product.get("id").getAsString());
                    productCard.setName(getStringValue(product, "name"));
                    productCard.setPrice(basicPrice/100); // Convert kopeks to rubles
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
    protected String getStringValue(JsonObject obj, String key) {
        return obj.has(key) ? obj.get(key).getAsString() : "";
    }

    protected int getIntValue(JsonObject obj, String key) {
        return obj.has(key) ? obj.get(key).getAsInt() : 0;
    }

    protected double getDoubleValue(JsonObject obj, String key) {
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
