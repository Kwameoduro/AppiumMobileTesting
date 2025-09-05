package com.mobile.tests.pages;

import com.mobile.tests.base.BasePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class CheckoutCompletePage extends BasePage {

    // Locators
    private final By completeTitle = AppiumBy.xpath("//android.widget.TextView[@text=\"CHECKOUT: COMPLETE!\"]");
    private final By thankYouMsg   = AppiumBy.xpath("//android.widget.TextView[@text=\"THANK YOU FOR YOU ORDER\"]");
    private final By backHomeBtn   = AppiumBy.xpath("//android.view.ViewGroup[@content-desc=\"test-BACK HOME\"]");

    public CheckoutCompletePage(AppiumDriver driver) {
        super(driver);
    }

    /**
     * Verify that Checkout Complete page is displayed.
     */
    public boolean isPageDisplayed() {
        return isDisplayed(completeTitle);
    }

    /**
     * Get confirmation (thank you) message.
     */
    public String getConfirmationMessage() {
        return getText(thankYouMsg);
    }

    /**
     * Tap on Back Home button and return to Products page.
     */
    public ProductsPage tapBackHome() {
        click(backHomeBtn);
        return new ProductsPage(driver);
    }
}
