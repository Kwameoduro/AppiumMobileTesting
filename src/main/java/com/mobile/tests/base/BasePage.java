package com.mobile.tests.base;

import com.mobile.tests.utils.WaitUtils;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Objects;

public abstract class BasePage {

    protected AppiumDriver driver;

    // Accept driver passed from tests/pages
    public BasePage(AppiumDriver driver) {
        this.driver = driver;
    }


     // Click the element by locator

    protected void click(By locator) {
        try {
            WebElement element = WaitUtils.waitForClickable(driver, locator);
            assert element != null;
            element.click();
            System.out.println("Clicked element: " + locator);
        } catch (Exception e) {
            System.err.println("Failed to click element: " + locator + " | Reason: " + e.getMessage());
            throw e;
        }
    }


     // Click the element directly

    protected void click(WebElement element) {
        try {
            element.click();
            System.out.println("Clicked element: " + element);
        } catch (Exception e) {
            System.err.println("Failed to click element: " + element + " | Reason: " + e.getMessage());
            throw e;
        }
    }


     // Type text into input field (clears before typing)

    protected void type(By locator, String text) {
        try {
            WebElement element = WaitUtils.waitForVisibility(driver, locator);
            assert element != null;
            element.clear();
            element.sendKeys(text);
            System.out.println("Typed '" + text + "' into: " + locator);
        } catch (Exception e) {
            System.err.println("Failed to type into: " + locator + " | Reason: " + e.getMessage());
            throw e;
        }
    }


     // Type text without clearing existing value

    protected void typeWithoutClear(By locator, String text) {
        try {
            WebElement element = WaitUtils.waitForVisibility(driver, locator);
            assert element != null;
            element.sendKeys(text);
            System.out.println("Appended '" + text + "' into: " + locator);
        } catch (Exception e) {
            System.err.println("Failed to append text into: " + locator + " | Reason: " + e.getMessage());
            throw e;
        }
    }


     // Get element text safely

    protected String getText(By locator) {
        try {
            WebElement element = WaitUtils.waitForVisibility(driver, locator);
            assert element != null;
            String text = element.getText();
            System.out.println("Retrieved text from " + locator + " : " + text);
            return text != null ? text.trim() : "";
        } catch (Exception e) {
            System.err.println("Failed to get text from " + locator + " | Reason: " + e.getMessage());
            return "";
        }
    }


     // Check if the element is displayed

    protected boolean isDisplayed(By locator) {
        try {
            boolean visible = Objects.requireNonNull(WaitUtils.waitForVisibility(driver, locator)).isDisplayed();
            System.out.println("Element displayed: " + locator);
            return visible;
        } catch (Exception e) {
            System.out.println("Element not displayed: " + locator);
            return false;
        }
    }


     // Safe find element

    protected WebElement find(By locator) {
        try {
            WebElement element = WaitUtils.waitForVisibility(driver, locator);
            System.out.println("Found element: " + locator);
            return element;
        } catch (Exception e) {
            System.err.println("Could not find element: " + locator + " | Reason: " + e.getMessage());
            throw e;
        }
    }
}
