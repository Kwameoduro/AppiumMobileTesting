package com.mobile.tests.pages;

import com.mobile.tests.base.BasePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class CheckoutInformationPage extends BasePage {

    // Locators
    private final By checkoutTitle   = AppiumBy.xpath("//android.widget.TextView[@text=\"CHECKOUT: INFORMATION\"]");
    private final By firstNameField  = AppiumBy.xpath("//android.widget.EditText[@content-desc=\"test-First Name\"]");
    private final By lastNameField   = AppiumBy.xpath("//android.widget.EditText[@content-desc=\"test-Last Name\"]");
    private final By postalCodeField = AppiumBy.xpath("//android.widget.EditText[@content-desc=\"test-Zip/Postal Code\"]");
    private final By continueBtn     = AppiumBy.xpath("//android.view.ViewGroup[@content-desc=\"test-CONTINUE\"]");
    private final By cancelBtn       = AppiumBy.xpath("//android.view.ViewGroup[@content-desc=\"test-CANCEL\"]");
    private final By errorMessage    = AppiumBy.xpath("//android.view.ViewGroup[@content-desc=\"test-Error message\"]");

    public CheckoutInformationPage(AppiumDriver driver) {
        super(driver);
    }

     // Verify that Checkout Information page is displayed.

    public boolean isPageDisplayed() {
        return isDisplayed(checkoutTitle);
    }

     // Enter first name.

    public void enterFirstName(String firstName) {
        type(firstNameField, firstName);
    }

    // Enter last name.

    public void enterLastName(String lastName) {
        type(lastNameField, lastName);
    }

     // Enter the postal code.

    public void enterPostalCode(String postalCode) {
        type(postalCodeField, postalCode);
    }

    // Fill in the checkout form.

    public void fillCheckoutInformation(String firstName, String lastName, String postalCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
    }

    // Tap the 'continue' button and navigate to the Overview Page.

    public void continueToOverview() {
        click(continueBtn);
        new CheckoutOverviewPage(driver);
    }

    // Tap Cancel button (navigates back to cart).

    public CartPage tapCancel() {
        click(cancelBtn);
        return new CartPage(driver);
    }

    // Get the error message if validation fails.

    public String getErrorMessage() {
        return getText(errorMessage);
    }

}
