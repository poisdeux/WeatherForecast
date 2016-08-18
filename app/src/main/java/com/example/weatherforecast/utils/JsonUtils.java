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
