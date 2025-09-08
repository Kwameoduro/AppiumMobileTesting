package com.mobile.tests.pages;

import com.mobile.tests.base.BasePage;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ProductsPage extends BasePage {

    // Locators
    private final By productsTitle = AppiumBy.xpath("//android.widget.TextView[@text=\"PRODUCTS\"]");
    private final By sortButton = AppiumBy.accessibilityId("test-Modal Selector Button");
    private final By productNames = AppiumBy.xpath("//android.widget.TextView[@content-desc='test-Item title']");
    private final By productPrices = AppiumBy.xpath("//android.widget.TextView[@content-desc='test-Price']");
    private final By addToCartBtn = AppiumBy.xpath("//android.view.ViewGroup[@content-desc='test-ADD TO CART']");
    private final By cartIcon = AppiumBy.accessibilityId("test-Cart");

    // Menu and logout locators
    private final By menuButton = AppiumBy.xpath(
            "//android.view.ViewGroup[@content-desc='test-Menu']/android.view.ViewGroup/android.widget.ImageView");
    private final By logoutButton = AppiumBy.xpath(
            "//android.view.ViewGroup[@content-desc='test-LOGOUT']");

    // Constructor
    public ProductsPage(AppiumDriver driver) {
        super(driver);
    }


    // Verify that Products page is displayed

    public boolean isPageDisplayed() {
        return isDisplayed(productsTitle);
    }


    // Get the list of all product names (as text)

    public List<String> getAllProductNames() {
        List<WebElement> elements = driver.findElements(productNames);
        List<String> names = new ArrayList<>();
        for (WebElement e : elements) {
            names.add(e.getText().trim());
        }
        System.out.println(" Retrieved product names: " + names);
        return names;
    }


    // Get the list of all product prices (as text)

    public List<String> getAllProductPrices() {
        List<WebElement> elements = driver.findElements(productPrices);
        List<String> prices = new ArrayList<>();
        for (WebElement e : elements) {
            prices.add(e.getText().trim());
        }
        System.out.println(" Retrieved product prices: " + prices);
        return prices;
    }


    // Sort products (tap sort button)

    public void tapSortButton() {
        click(sortButton);
        System.out.println(" Tapped sort button.");
    }


    // Add first visible product to cart

    public void addFirstProductToCart() {
        List<WebElement> addButtons = driver.findElements(addToCartBtn);
        if (!addButtons.isEmpty()) {
            addButtons.get(0).click();
            System.out.println(" Added first product to cart.");
        } else {
            throw new IllegalStateException(" No 'Add to Cart' button found on Products page.");
        }
    }


    // Open shopping cart

    public void goToCart() {
        click(cartIcon);
        System.out.println(" Navigated to cart.");
    }


    // Clicks the menu and logs out

    public void logout() {
        // Click the menu button
        driver.findElement(menuButton).click();

        // Wait for the logout button to appear and click it
        WebElement logoutBtn = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(logoutButton));
        logoutBtn.click();
        System.out.println(" Logged out successfully.");
    }
}
