package com.mobile.tests.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        loadProperties("config/config.properties"); // default path
    }

    private ConfigReader() {
        // Prevent instantiation
    }

    private static void loadProperties(String path) {
        try (InputStream input = new FileInputStream(path)) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(" Failed to load config.properties file from path: " + path, e);
        }
    }


     // Get property value by key.

    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isEmpty()) {
            throw new RuntimeException(" Property '" + key + "' not found in config.properties");
        }
        return value.trim();
    }


    // Get property with a default value.

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue).trim();
    }


    // Reload config.properties (useful if you switch environments).

    public static void reload(String path) {
        properties.clear();
        loadProperties(path);
    }
}
