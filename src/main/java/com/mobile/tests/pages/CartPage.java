package com.mobile.tests.pages;

import com.mobile.tests.base.BasePage;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CartPage extends BasePage {

    // Locators
    private final By cartTitle     = AppiumBy.xpath("//android.widget.TextView[@text=\"YOUR CART\"]");
    private final By cartItems     = AppiumBy.xpath("//android.view.ViewGroup[@content-desc='test-Item']");
    private final By itemNames     = AppiumBy.xpath("//android.widget.TextView[@content-desc='test-Item title']");
    private final By itemPrices    = AppiumBy.xpath("//android.widget.TextView[@content-desc='test-Price']");
    private final By removeButtons = AppiumBy.xpath("//android.view.ViewGroup[@content-desc='test-REMOVE']");
    private final By continueBtn   = AppiumBy.xpath("//android.view.ViewGroup[@content-desc=\"test-CONTINUE SHOPPING\"]");
    private final By checkoutBtn   = AppiumBy.xpath("//android.view.ViewGroup[@content-desc=\"test-CHECKOUT\"]");
    private final By emptyCartMessage = AppiumBy.xpath("//android.view.ViewGroup[@content-desc='test-Cart']/android.view.ViewGroup/android.widget.ImageView");

    // Proper constructor with driver injection
    public CartPage(AppiumDriver driver) {
        super(driver);
    }

     // Verify that Cart page is displayed

    public boolean isCartPageDisplayed() {
        return isDisplayed(cartTitle);
    }

     // Get the list of items in the cart

    public List<WebElement> getCartItems() {
        return driver.findElements(cartItems);
    }

    // Get item names from cart

    public List<WebElement> getItemNames() {
        return driver.findElements(itemNames);
    }

    // Get item prices from cart

    public List<WebElement> getItemPrices() {
        return driver.findElements(itemPrices);
    }

    // Remove the first item in cart

    public void removeFirstItem() {
        List<WebElement> buttons = driver.findElements(removeButtons);
        if (!buttons.isEmpty()) {
            buttons.get(0).click();
        }
    }

    // Continue shopping

    public void continueShopping() {
        click(continueBtn);
    }

    // Proceed to checkout

    public boolean proceedToCheckout() {
        click(checkoutBtn);
        return false;
    }

    public boolean isEmptyCartMessageDisplayed() {
        return isDisplayed(emptyCartMessage);
    }
}
