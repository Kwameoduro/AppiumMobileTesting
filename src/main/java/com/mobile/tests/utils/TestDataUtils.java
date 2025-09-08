package com.mobile.tests.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class TestDataUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    private TestDataUtils() {
        // Prevent instantiation
    }

    // Reads a JSON file from classpath (src/test/resources) and returns as Map<String, Object>.

    public static Map<String, Object> getTestData(String filePath) {
        try (InputStream inputStream =
                     TestDataUtils.class.getClassLoader().getResourceAsStream(filePath)) {

            if (inputStream == null) {
                throw new RuntimeException(" Test data file not found: " + filePath);
            }

            return mapper.readValue(inputStream, new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            throw new RuntimeException(" Failed to read test data file: " + filePath, e);
        }
    }

    // Reads a nested object (Map) from JSON (e.g., validUser).

    public static Map<String, String> getNestedMap(String filePath, String key) {
        Map<String, Object> allData = getTestData(filePath);
        Object value = allData.get(key);

        if (value == null) {
            throw new RuntimeException(" Key '" + key + "' not found in " + filePath);
        }

        return mapper.convertValue(value, new TypeReference<Map<String, String>>() {});
    }


    // Reads a JSON array of objects (e.g., products).

    public static List<Map<String, String>> getListOfMaps(String filePath, String key) {
        Map<String, Object> allData = getTestData(filePath);
        Object value = allData.get(key);

        if (value == null) {
            throw new RuntimeException(" Key '" + key + "' not found in " + filePath);
        }

        return mapper.convertValue(value, new TypeReference<List<Map<String, String>>>() {});
    }


      // Reads a JSON array of strings (e.g., sort options).

    public static List<String> getListOfStrings(String filePath, String key) {
        Map<String, Object> allData = getTestData(filePath);
        Object value = allData.get(key);

        if (value == null) {
            throw new RuntimeException(" Key '" + key + "' not found in " + filePath);
        }

        return mapper.convertValue(value, new TypeReference<List<String>>() {});
    }


      // Reads a single value from a JSON file.

    public static String getData(String filePath, String key) {
        Map<String, Object> data = getTestData(filePath);
        Object value = data.get(key);

        if (value == null) {
            throw new RuntimeException(" Key '" + key + "' not found in " + filePath);
        }
        return value.toString();
    }
}
