package com.example.weatherforecast.utils;

import com.fasterxml.jackson.databind.JsonNode;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by martijn on 27/06/16.
 */
public class JsonUtils {

    /**
     * Converts the date string from the jsonNode with the given key to a LocalDate.
     * @param jsonNode holding the date string for the given key
     * @param key key of the json node which value should contain the date string
     * @param dtf pattern that should match the date string in the json node
     * @return
     */
    public static LocalDate localDateFromJsonNode(JsonNode jsonNode, String key, DateTimeFormatter dtf) {
        if (jsonNode == null) return null;
        JsonNode value = jsonNode.get(key);
        if (value == null) return null;
        return dtf.parseLocalDate(value.textValue());
    }

    public static Integer integerFromJsonNode(JsonNode jsonNode, String key) {
        if (jsonNode == null) return null;
        JsonNode value = jsonNode.get(key);
        if (value == null) return null;
        return value.asInt();
    }

}
