package com.example.weatherforecast;

import com.example.weatherforecast.utils.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by martijn on 25/06/16.
 */
public class YahooWeatherApiResponse {
    private static final String RESULT_NODE = "results";
    private static final String QUERY_NODE = "query";
    private static final String FORECAST_NODE = "forecast";
    private static final String CHANNEL_NODE = "channel";
    private static final String ITEM_NODE = "item";
    private static final String DATE_NODE = "date";
    private static final String TEMP_HIGH_NODE = "high";
    private static final String TEMP_LOW_NODE = "low";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public ArrayList<WeatherData> parseJson(String json) throws YahooWeatherApiException {
        try {
            ObjectNode jsonResponse = (ObjectNode) objectMapper.readTree(json);

            JsonNode forecastNode = jsonResponse.path(QUERY_NODE)
                                                .path(RESULT_NODE)
                                                .path(CHANNEL_NODE)
                                                .path(ITEM_NODE)
                                                .path(FORECAST_NODE);

            if (forecastNode.isMissingNode()) {
                throw new YahooWeatherApiException(YahooWeatherApiException.INVALID_RESPONSE_FROM_HOST,
                                                   "Result doesn't contain a forecast node.");
            }

            ArrayList<WeatherData> forecast = new ArrayList<>();

            if (forecastNode.isArray()) {
                forecast = new ArrayList<>();
                for(JsonNode item : forecastNode) {
                    forecast.add(forecastJsonItemToWeatherData(item));
                }
            }

            return forecast;

        } catch (IOException e) {
            throw new YahooWeatherApiException(YahooWeatherApiException.INVALID_RESPONSE_FROM_HOST,
                                               e.getMessage());
        }
    }

    private WeatherData forecastJsonItemToWeatherData(JsonNode jsonNode) {
        LocalDate date = JsonUtils.localDateFromJsonNode(jsonNode, DATE_NODE,
                                                         DateTimeFormat.forPattern("dd MMM yyyy"));
        Integer tempLow = JsonUtils.integerFromJsonNode(jsonNode, TEMP_LOW_NODE);
        Integer tempHigh = JsonUtils.integerFromJsonNode(jsonNode, TEMP_HIGH_NODE);

        return new WeatherData(date, tempLow, tempHigh);
    }

}
