package com.mobile.tests.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CapabilitiesReader {

    private static final String CAPABILITIES_PATH = "config/capabilities.json";

    private CapabilitiesReader() {
        // Prevent instantiation
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getCapabilities() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> capabilitiesMap = mapper.readValue(new File(CAPABILITIES_PATH), Map.class);

            // ✅ Resolve relative app path into absolute path
            if (capabilitiesMap.containsKey("appium:app")) {
                String appPath = (String) capabilitiesMap.get("appium:app");
                File appFile = new File(appPath);

                if (!appFile.isAbsolute()) {
                    appFile = new File(System.getProperty("user.dir"), appPath);
                }

                if (!appFile.exists()) {
                    throw new RuntimeException("❌ App file not found at: " + appFile.getAbsolutePath());
                }

                capabilitiesMap.put("appium:app", appFile.getAbsolutePath());
            }

            System.out.println("✅ Loaded capabilities: " + capabilitiesMap);

            return new HashMap<>(capabilitiesMap);

        } catch (IOException e) {
            throw new RuntimeException("❌ Failed to load capabilities.json from path: " + CAPABILITIES_PATH, e);
        }
    }
}
