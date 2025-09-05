package com.mobile.tests.pages;

import com.mobile.tests.base.BasePage;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.AppiumBy;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage extends BasePage {

    // Locators (using AccessibilityId for stability & speed)
    private final By usernameField = AppiumBy.accessibilityId("test-Username");
    private final By passwordField = AppiumBy.accessibilityId("test-Password");
    private final By loginButton   = AppiumBy.accessibilityId("test-LOGIN");
    private final By loginTitle = AppiumBy.xpath("//android.widget.ScrollView[@content-desc=\"test-Login\"]/android.view.ViewGroup/android.widget.ImageView[1]");


    private final By emptyusername = AppiumBy.xpath("//android.widget.TextView[@text=\"Username is required\"]");
    private final By emptypassword = AppiumBy.xpath("//android.widget.TextView[@text=\"Password is required\"]");

    private final By menuButton = AppiumBy.xpath("//android.view.ViewGroup[@content-desc='test-Menu']/android.view.ViewGroup/android.widget.ImageView");
    private final By logoutButton = AppiumBy.xpath("//android.view.ViewGroup[@content-desc='test-LOGOUT']");


    // Multiple possible error locators (to handle app version differences)
    private final By[] errorLocators = {
            AppiumBy.xpath("//android.widget.TextView[@text=\"Username and password do not match any user in this service.\"]"),
            AppiumBy.accessibilityId("test-Error"),
            AppiumBy.androidUIAutomator("new UiSelector().text(\"Sorry, this user has been locked out.\")"),
            AppiumBy.id("com.swaglabsmobileapp:id/errorMessage") // native Android fallback
    };

    // ✅ Constructor: inject AppiumDriver into BasePage
    public LoginPage(AppiumDriver driver) {
        super(driver);
    }

    /**
     * Enter username
     */
    public void enterUsername(String username) {
        type(usernameField, username);
    }

    /**
     * Enter password
     */
    public void enterPassword(String password) {
        type(passwordField, password);
    }

    /**
     * Tap on login button
     */
    public void tapLogin() {
        click(loginButton);
    }

    public boolean isPageDisplayed() {
        return isDisplayed(loginTitle);
    }

    /**
     * Perform login action
     */
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        tapLogin();
    }

    /**
     * Get error message text (tries multiple locators for robustness)
     */
    public String getErrorMessage() {
        for (By locator : errorLocators) {
            if (isDisplayed(locator)) {
                return getText(locator);
            }
        }
        System.err.println("⚠️ No error message found on LoginPage.");
        return "";
    }

    /**
     * Check if login page is displayed
     */
    public boolean isLoginPageDisplayed() {
        return isDisplayed(usernameField);
    }

    public String getEmptyUsernameError() {
        try {
            // Wait up to 5 seconds for the error message to be visible
            return new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(driver -> {
                        WebElement el = driver.findElement(emptyusername);
                        return el.isDisplayed() ? el.getText().trim() : null;
                    });
        } catch (TimeoutException e) {
            return ""; // Return empty string if error message never appears
        }


    }

    public String getEmptyPasswordError() {
        try {
            // Wait up to 5 seconds for the error message to be visible
            return new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(driver -> {
                        WebElement el = driver.findElement(emptypassword);
                        return el.isDisplayed() ? el.getText().trim() : null;
                    });
        } catch (TimeoutException e) {
            return ""; // Return empty string if error message never appears
        }
    }

    /** Clicks the menu and logs out */
    public void logout() {
        // Click menu button
        driver.findElement(menuButton).click();

        // Wait until logout button is visible, then click it
        WebElement logoutBtn = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(logoutButton));

        logoutBtn.click();
    }


    /** Optionally, verify logout success by checking login page element */
    public boolean isLoggedOut() {
        try {
            // Assuming the login button is a reliable login page indicator
            return new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(driver -> driver.findElement(AppiumBy.accessibilityId("test-LOGIN")).isDisplayed());
        } catch (TimeoutException e) {
            return false;
        }
    }
}

