package com.mobile.tests.base;

import com.mobile.tests.core.DriverManager;
import com.mobile.tests.utils.CapabilitiesReader;
import com.mobile.tests.utils.ConfigReader;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public abstract class BaseTest {

    protected AppiumDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        System.out.println("===== Test Setup Started =====");

        // Print environment details from capabilities.json instead of config.properties
        Map<String, Object> caps = CapabilitiesReader.getCapabilities();
        System.out.println("Platform: " + caps.get("platformName"));
        System.out.println("Device: " + caps.get("appium:deviceName"));
        System.out.println("App Path: " + caps.get("appium:app"));

        // Initialize driver
        DriverManager.initDriver();
        driver = DriverManager.getDriver();

        System.out.println("===== Driver Initialized Successfully =====");
    }


    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            if (!result.isSuccess()) {
                takeScreenshot(result.getName(), result.getTestClass().getRealClass().getSimpleName());
            }
        } catch (Exception e) {
            System.err.println("Error while capturing screenshot: " + e.getMessage());
        } finally {
            DriverManager.quitDriver();
            driver = null; // prevent stale driver usage
            System.out.println("===== Driver Quit Successfully =====");
        }
    }

    /**
     * Capture screenshot and save it under /screenshots/[TestClass] folder
     */
    private void takeScreenshot(String testName, String className) {
        if (driver == null) return;

        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String screenshotDir = System.getProperty("user.dir") + "/screenshots/" + className + "/";
        String screenshotPath = screenshotDir + testName + "_" + timestamp + ".png";

        try {
            Files.createDirectories(Paths.get(screenshotDir));
            Files.copy(srcFile.toPath(), Paths.get(screenshotPath));
            System.out.println("📸 Screenshot saved at: " + screenshotPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save screenshot", e);
        }
    }
}
