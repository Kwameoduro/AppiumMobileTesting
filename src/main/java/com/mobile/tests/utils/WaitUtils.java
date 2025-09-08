package com.mobile.tests.utils;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class WaitUtils {

    private static final int DEFAULT_TIMEOUT = Integer.parseInt(ConfigReader.getProperty("explicitWait"));
    private static final int DEFAULT_POLLING = 500; // milliseconds

    private WaitUtils() {
        // Prevent instantiation
    }


     // Helper to create WebDriverWait

    private static WebDriverWait getWait(AppiumDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }


    // Wait until the element is visible

    public static WebElement waitForVisibility(AppiumDriver driver, By locator) {
        try {
            return getWait(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
            System.err.println(" Element not visible: " + locator + " | " + e.getMessage());
            return null;
        }
    }


    // Wait until the element is clickable

    public static WebElement waitForClickable(AppiumDriver driver, By locator) {
        try {
            return getWait(driver).until(ExpectedConditions.elementToBeClickable(locator));
        } catch (Exception e) {
            System.err.println(" Element not clickable: " + locator + " | " + e.getMessage());
            return null;
        }
    }


     // Wait until the element is present in DOM (not necessarily visible)

    public static WebElement waitForPresence(AppiumDriver driver, By locator) {
        try {
            return getWait(driver).until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (Exception e) {
            System.err.println(" Element not present: " + locator + " | " + e.getMessage());
            return null;
        }
    }


    // Fluent wait for custom conditions

    public static <T> T fluentWait(AppiumDriver driver, Function<AppiumDriver, T> condition, int timeoutSec) {
        FluentWait<AppiumDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutSec))
                .pollingEvery(Duration.ofMillis(DEFAULT_POLLING))
                .ignoring(NoSuchElementException.class);

        return wait.until(condition);
    }


    // Wait until element contains specific text

    public static boolean waitForText(AppiumDriver driver, By locator, String expectedText) {
        try {
            return getWait(driver).until(ExpectedConditions.textToBePresentInElementLocated(locator, expectedText));
        } catch (Exception e) {
            System.err.println(" Text not found in element: " + locator + " | Expected: " + expectedText);
            return false;
        }
    }


    // Wait until the element disappears

    public static boolean waitForInvisibility(AppiumDriver driver, By locator) {
        try {
            return getWait(driver).until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (Exception e) {
            System.err.println(" Element still visible: " + locator + " | " + e.getMessage());
            return false;
        }
    }
}
