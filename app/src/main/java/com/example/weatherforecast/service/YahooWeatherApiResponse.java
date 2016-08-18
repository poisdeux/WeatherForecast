/*
 * Copyright 2016 Martijn Brekhof. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.weatherforecast.service;

import com.example.weatherforecast.WeatherData;
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
