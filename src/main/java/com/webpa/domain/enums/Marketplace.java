package com.webpa.domain.enums;

import java.util.Arrays;

public enum Marketplace {
    WILDBERRIES("wildberries", "https://www.wildberries.ru"),
    OZON("ozon", "https://www.ozon.ru"),
    YANDEX_MARKET("yandex_market", "https://market.yandex.ru"),
    CUSTOM("custom", "");

    private final String code;
    private final String baseUrl;

    Marketplace(String code, String baseUrl) {
        this.code = code;
        this.baseUrl = baseUrl;
    }

    public String getCode() {
        return code;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public static Marketplace fromCode(String code) {
        return Arrays.stream(values())
                .filter(item -> item.code.equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown marketplace: " + code));
    }
}
