package com.mobile.tests.core;

import com.mobile.tests.utils.ConfigReader;
import com.mobile.tests.utils.CapabilitiesReader;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Map;

public class DriverManager {

    private static final ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();

    private DriverManager() {
        // Prevent instantiation
    }

    /**
     * Initialize Appium Driver
     */
    public static void initDriver() {
        if (driver.get() == null) {
            try {
                String serverURL;

                // ✅ Get server URL either from AppiumServerManager or config.properties
                boolean startAppium = Boolean.parseBoolean(ConfigReader.getProperty("startAppiumServer"));
                if (startAppium) {
                    AppiumServerManager.startServer();
                    serverURL = AppiumServerManager.getServerUrl();
                } else {
                    serverURL = ConfigReader.getProperty("appiumServerURL");
                }

                // ✅ Load capabilities from CapabilitiesReader
                Map<String, Object> capabilitiesMap = CapabilitiesReader.getCapabilities();
                DesiredCapabilities capabilities = new DesiredCapabilities();
                capabilitiesMap.forEach(capabilities::setCapability);

                // ✅ Create Appium Driver
                AppiumDriver appiumDriver = new AppiumDriver(new URL(serverURL), capabilities);

                // ✅ Configure implicit wait from config.properties
                int implicitWait = Integer.parseInt(ConfigReader.getProperty("implicitWait"));
                appiumDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

                driver.set(appiumDriver);

                System.out.println("✅ Appium driver started successfully with capabilities: " + capabilitiesMap);

            } catch (MalformedURLException e) {
                throw new RuntimeException("❌ Invalid Appium server URL. Check config.properties", e);
            } catch (Exception e) {
                throw new RuntimeException("❌ Failed to initialize Appium driver", e);
            }
        }
    }

    /**
     * Get current Appium driver
     */
    public static AppiumDriver getDriver() {
        if (driver.get() == null) {
            throw new IllegalStateException("Driver not initialized. Call initDriver() first.");
        }
        return driver.get();
    }

    /**
     * Quit driver and cleanup
     */
    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
            boolean startAppium = Boolean.parseBoolean(ConfigReader.getProperty("startAppiumServer"));
            if (startAppium) {
                AppiumServerManager.stopServer();
            }
            System.out.println("✅ Appium driver stopped successfully");
        }
    }
}
