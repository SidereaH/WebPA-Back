package com.webpa.webpa;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.lang.reflect.Type;
import java.util.Map;

@Converter
public class JsonConverter implements AttributeConverter<Map<String, Object>, String> {
    private final static Gson gson = new Gson();
    private final static Type type = new TypeToken<Map<String, Object>>(){}.getType();

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        return gson.toJson(attribute);
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        return gson.fromJson(dbData, type);
    }
}