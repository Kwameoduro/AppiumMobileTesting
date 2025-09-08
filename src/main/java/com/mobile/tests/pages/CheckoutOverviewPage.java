package com.mobile.tests.pages;

import com.mobile.tests.base.BasePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CheckoutOverviewPage extends BasePage {

    // Locators
    private final By overviewTitle = AppiumBy.xpath("//android.widget.TextView[@text=\"CHECKOUT: OVERVIEW\"]");
    private final By itemNames     = AppiumBy.xpath("//android.widget.TextView[@content-desc='test-Item title']");
    private final By itemPrices    = AppiumBy.xpath("//android.widget.TextView[@content-desc='test-Price']");
    private final By subtotalLabel = AppiumBy.xpath("//android.widget.TextView[contains(@text,'Item total:')]");
    private final By taxLabel      = AppiumBy.xpath("//android.widget.TextView[contains(@text,'Tax:')]");
    private final By totalLabel    = AppiumBy.xpath("//android.widget.TextView[contains(@text,'Total:')]");
    private final By finishBtn     = AppiumBy.xpath("//android.view.ViewGroup[@content-desc=\"test-FINISH\"]");
    private final By cancelBtn     = AppiumBy.xpath("//android.view.ViewGroup[@content-desc=\"test-CANCEL\"]");

    public CheckoutOverviewPage(AppiumDriver driver) {
        super(driver);
    }


    // Verify that Checkout Overview page is displayed.

    public boolean isPageDisplayed() {
        return isDisplayed(overviewTitle);
    }


    // Get all item names listed in overview.

    public List<WebElement> getOverviewItems() {
        return driver.findElements(itemNames);
    }


    // Get all item prices listed in overview.

    public List<WebElement> getItemPrices() {
        return driver.findElements(itemPrices);
    }


     // Get subtotal text.

    public String getSubtotal() {
        return getText(subtotalLabel);
    }


    // Get tax text.

    public String getTax() {
        return getText(taxLabel);
    }


    // Get total text.

    public String getTotal() {
        return getText(totalLabel);
    }


    // Finish checkout and navigate to Checkout Complete page.

    public void finishCheckout() {
        driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().description(\"test-FINISH\"));"
        ));

        click(finishBtn);
        new CheckoutCompletePage(driver);
    }


    // Cancel checkout and navigate back to Cart page.

    public CartPage cancelCheckout() {
        click(cancelBtn);
        return new CartPage(driver);
    }

}
