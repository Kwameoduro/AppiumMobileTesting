package com.mobile.tests.core;

import com.mobile.tests.utils.ConfigReader;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

import java.io.File;

public class AppiumServerManager {

    private static AppiumDriverLocalService service;

    private AppiumServerManager() {
        // Prevent instantiation
    }


     // Start the Appium server

    public static void startServer() {
        if (service == null || !service.isRunning()) {
            try {
                // Load values from config.properties (with fallbacks)
                String ip = ConfigReader.getProperty("appiumServerIP");
                int port = Integer.parseInt(ConfigReader.getProperty("appiumServerPort"));
                String logPath = ConfigReader.getProperty("appiumLogFile");

                AppiumServiceBuilder builder = new AppiumServiceBuilder()
                        .withIPAddress(ip)
                        .usingPort(port > 0 ? port : 4723)
                        .withLogFile(new File(logPath));

                service = AppiumDriverLocalService.buildService(builder);
                service.start();

                System.out.println("Appium server started on: " + service.getUrl());

            } catch (Exception e) {
                throw new RuntimeException("Failed to start Appium server", e);
            }
        }
    }


      // Stop the Appium server

    public static void stopServer() {
        if (service != null && service.isRunning()) {
            service.stop();
            System.out.println("Appium server stopped.");
        }
    }


      // Get Appium server URL

    public static String getServerUrl() {
        if (service == null || !service.isRunning()) {
            throw new IllegalStateException("Appium server is not running!");
        }
        return service.getUrl().toString();
    }
}
